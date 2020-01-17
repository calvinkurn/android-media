package com.tokopedia.attachvoucher.view.viewmodel

import androidx.lifecycle.Observer
import com.tokopedia.attachvoucher.Constant
import com.tokopedia.attachvoucher.InstantTaskExecutorRuleSpek
import com.tokopedia.attachvoucher.data.GetVoucherResponse
import com.tokopedia.attachvoucher.data.Voucher
import com.tokopedia.attachvoucher.data.VoucherType
import com.tokopedia.attachvoucher.usecase.GetVoucherUseCase
import com.tokopedia.common.network.util.CommonUtil
import io.mockk.*
import org.junit.Assert.assertEquals
import org.spekframework.spek2.Spek


object AttachVoucherViewModelTest : Spek({

    InstantTaskExecutorRuleSpek(this)

    group("Load vouchers") {

        val exShopId = "6696"
        val exVouchersGetResponse = createDummyVouchers()
        val vouchers = exVouchersGetResponse.vouchers
        val exThrowable = Throwable()

        val getVoucherUseCase by memoized { mockk<GetVoucherUseCase>() }
        val viewModel by memoized { AttachVoucherViewModel(getVoucherUseCase) }

        group("No shop id") {
            beforeEachTest {
                viewModel.shopId = ""
            }
            test("Voucher is not loaded") {
                viewModel.loadVouchers()
                verify(exactly = 0) { getVoucherUseCase.getVouchers(any(), any(), any()) }
            }
        }

        group("Has Shop id") {
            beforeEachTest {
                viewModel.shopId = exShopId
            }
            test("Call the right shopId") {
                every { getVoucherUseCase.getVouchers(any(), any(), exShopId.toInt()) } just Runs
                viewModel.loadVouchers()
                verify { getVoucherUseCase.getVouchers(any(), any(), exShopId.toInt()) }
            }
            group("On Success load invoices") {
                beforeEachTest {
                    every { getVoucherUseCase.getVouchers(captureLambda(), any(), exShopId.toInt()) } answers {
                        val onSuccess = lambda<(GetVoucherResponse) -> Unit>()
                        onSuccess.invoke(exVouchersGetResponse)
                    }
                }
                test("Vouchers list changed on success") {
                    val voucherObservers = mockk<Observer<List<Voucher>>>(relaxed = true)
                    viewModel.filteredVouchers.observeForever(voucherObservers)
                    viewModel.loadVouchers()
                    assertEquals(3, vouchers.size)
                    verify { voucherObservers.onChanged(vouchers) }
                }
                test("Filter value changed to -1 on success") {
                    val filterObserver = mockk<Observer<Int>>(relaxed = true)
                    viewModel.filter.observeForever(filterObserver)
                    viewModel.loadVouchers()
                    verify { filterObserver.onChanged(AttachVoucherViewModel.NO_FILTER) }
                }
                test("Filter cashback clicked") {
                    val filterObserver = mockk<Observer<Int>>(relaxed = true)
                    val voucherObservers = mockk<Observer<List<Voucher>>>(relaxed = true)
                    viewModel.filteredVouchers.observeForever(voucherObservers)
                    viewModel.filter.observeForever(filterObserver)

                    viewModel.loadVouchers()
                    viewModel.toggleFilter(VoucherType.CASH_BACK)

                    assertEquals(1, viewModel.filteredVouchers.value?.size)
                    verify { filterObserver.onChanged(VoucherType.CASH_BACK) }
                }

            }
            group("On Error load invoices") {
                beforeEachTest {
                    every { getVoucherUseCase.getVouchers(any(), captureLambda(), exShopId.toInt()) } answers {
                        val onError = lambda<(Throwable) -> Unit>()
                        onError.invoke(exThrowable)
                    }
                }
                test("Error value changed on error") {
                    val errorObserver = mockk<Observer<Throwable>>(relaxed = true)
                    viewModel.error.observeForever(errorObserver)
                    viewModel.loadVouchers()
                    verify { errorObserver.onChanged(exThrowable) }
                }
            }
        }
    }
})

fun createDummyVouchers(): GetVoucherResponse {
    return CommonUtil.fromJson(Constant.vouchersReponse, GetVoucherResponse::class.java)
}