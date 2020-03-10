package com.tokopedia.attachvoucher.view.viewmodel

import androidx.lifecycle.Observer
import com.tokopedia.attachvoucher.FileUtil
import com.tokopedia.attachvoucher.InstantTaskExecutorRuleSpek
import com.tokopedia.attachvoucher.data.GetVoucherResponse
import com.tokopedia.attachvoucher.data.VoucherUiModel
import com.tokopedia.attachvoucher.data.VoucherType
import com.tokopedia.attachvoucher.usecase.GetVoucherUseCase
import com.tokopedia.attachvoucher.view.viewmodel.AttachVoucherViewModel.Companion.NO_FILTER
import com.tokopedia.common.network.util.CommonUtil
import io.mockk.*
import org.junit.Assert.assertEquals
import org.spekframework.spek2.Spek


object AttachVoucherViewModelTest : Spek({

    InstantTaskExecutorRuleSpek(this)

    group("Load vouchers") {

        val stringVouchersResponse = FileUtil.readFileContent(javaClass, "/get_vouchers_response.json")

        // Test Data
        val exShopId = "6696"
        val exVouchersGetResponse = createDummyVouchers(stringVouchersResponse)
        val exVouchers = exVouchersGetResponse.vouchers
        val exThrowable = Throwable()

        val getVoucherUseCase by memoized { mockk<GetVoucherUseCase>() }
        val viewModel by memoized { AttachVoucherViewModel(getVoucherUseCase) }

        group("No shop id") {
            test("Voucher is not loaded") {
                viewModel.loadVouchers("")
                verify(exactly = 0) { getVoucherUseCase.getVouchers(any(), any(), any()) }
            }
        }

        group("Has Shop id") {
            test("Call the right shopId") {
                every { getVoucherUseCase.getVouchers(any(), any(), exShopId.toInt()) } just Runs
                viewModel.loadVouchers(exShopId)
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
                    val voucherObservers = mockk<Observer<List<VoucherUiModel>>>(relaxed = true)
                    viewModel.filteredVouchers.observeForever(voucherObservers)
                    viewModel.loadVouchers(exShopId)
                    assertEquals(3, exVouchers.size)
                    verify { voucherObservers.onChanged(exVouchers) }
                }
                test("Filter value changed to -1 on success") {
                    val filterObserver = mockk<Observer<Int>>(relaxed = true)
                    viewModel.filter.observeForever(filterObserver)
                    viewModel.loadVouchers(exShopId)
                    verify { filterObserver.onChanged(NO_FILTER) }
                }
                test("Filter cashback clicked") {
                    val filterObserver = mockk<Observer<Int>>(relaxed = true)
                    val voucherObservers = mockk<Observer<List<VoucherUiModel>>>(relaxed = true)
                    viewModel.filteredVouchers.observeForever(voucherObservers)
                    viewModel.filter.observeForever(filterObserver)

                    viewModel.loadVouchers(exShopId)
                    viewModel.toggleFilter(VoucherType.CASH_BACK)

                    assertEquals(1, viewModel.filteredVouchers.value?.size)
                    verify { filterObserver.onChanged(VoucherType.CASH_BACK) }
                }
                test("Filter cashback clicked twice") {
                    val filterObserver = mockk<Observer<Int>>(relaxed = true)
                    val voucherObservers = mockk<Observer<List<VoucherUiModel>>>(relaxed = true)
                    viewModel.filteredVouchers.observeForever(voucherObservers)
                    viewModel.filter.observeForever(filterObserver)

                    viewModel.loadVouchers(exShopId)
                    viewModel.toggleFilter(VoucherType.CASH_BACK)
                    viewModel.toggleFilter(VoucherType.CASH_BACK)

                    assertEquals(3, viewModel.filteredVouchers.value?.size)
                    verify { filterObserver.onChanged(NO_FILTER) }
                }
                test("Filter cashback clicked then click free-ongkir filter") {
                    val filterObserver = mockk<Observer<Int>>(relaxed = true)
                    val voucherObservers = mockk<Observer<List<VoucherUiModel>>>(relaxed = true)
                    viewModel.filteredVouchers.observeForever(voucherObservers)
                    viewModel.filter.observeForever(filterObserver)

                    viewModel.loadVouchers(exShopId)
                    viewModel.toggleFilter(VoucherType.CASH_BACK)
                    viewModel.toggleFilter(VoucherType.FREE_ONGKIR)

                    assertEquals(2, viewModel.filteredVouchers.value?.size)
                    verify { filterObserver.onChanged(VoucherType.FREE_ONGKIR) }
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
                    viewModel.loadVouchers(exShopId)
                    verify { errorObserver.onChanged(exThrowable) }
                }
            }
        }
    }
})

fun createDummyVouchers(vouchersResponse: String): GetVoucherResponse {
    return CommonUtil.fromJson(vouchersResponse, GetVoucherResponse::class.java)
}