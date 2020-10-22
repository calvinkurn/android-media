package com.tokopedia.attachvoucher.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.attachvoucher.FileUtil
import com.tokopedia.attachvoucher.data.VoucherUiModel
import com.tokopedia.attachvoucher.data.voucherv2.GetMerchantPromotionGetMVListResponse
import com.tokopedia.attachvoucher.mapper.VoucherMapper
import com.tokopedia.attachvoucher.usecase.GetVoucherUseCase
import com.tokopedia.attachvoucher.usecase.GetVoucherUseCase.MVFilter.VoucherType
import com.tokopedia.attachvoucher.view.viewmodel.AttachVoucherViewModel.Companion.NO_FILTER
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class AttachVoucherViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getVoucherUseCase: GetVoucherUseCase

    @RelaxedMockK
    lateinit var voucherObservers: Observer<List<VoucherUiModel>>

    @RelaxedMockK
    lateinit var filterObserver: Observer<Int>

    lateinit var viewModel: AttachVoucherViewModel

    object Dummy {
        val firstPage = 1
        val noFilterVoucherType = GetVoucherUseCase.MVFilter.VoucherType.noFilter
        val exShopId = "6696"
        val exVouchersGetResponse: GetMerchantPromotionGetMVListResponse = FileUtil.parse(
                "/get_vouchers_response.json",
                GetMerchantPromotionGetMVListResponse::class.java
        )
        val exVouchers = VoucherMapper().map(exVouchersGetResponse)
        val exThrowable = Throwable()
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = AttachVoucherViewModel(getVoucherUseCase)
    }


    @Test
    fun `Vouchers list changed on success`() {
        // Given
        every {
            getVoucherUseCase.getVouchers(Dummy.firstPage, Dummy.noFilterVoucherType, captureLambda(), any())
        } answers {
            val onSuccess = lambda<(List<VoucherUiModel>) -> Unit>()
            onSuccess.invoke(Dummy.exVouchers)
        }

        // When
        viewModel.voucher.observeForever(voucherObservers)
        viewModel.loadVouchers(Dummy.firstPage)

        // Then
        verify { voucherObservers.onChanged(Dummy.exVouchers) }
    }

    @Test
    fun `Cancel previous load voucher`() {
        // Given
        every { getVoucherUseCase.isLoading } returns true
        every {
            getVoucherUseCase.getVouchers(any(), any(), any(), any())
        } just Runs

        // When
        viewModel.loadVouchers(Dummy.firstPage)

        // Then
        verify { getVoucherUseCase.cancelCurrentLoad() }
    }


    @Test
    fun `Filter cashback clicked`() {
        // Given
        viewModel.filter.observeForever(filterObserver)
        every {
            getVoucherUseCase.getVouchers(Dummy.firstPage, any(), any(), any())
        } just Runs

        assertTrue(viewModel.hasNoFilter())

        // When
        viewModel.toggleFilter(VoucherType.paramCashback)
        viewModel.loadVouchers(Dummy.firstPage)

        // Then
        verify { filterObserver.onChanged(VoucherType.paramCashback) }
        assertFalse(viewModel.hasNoFilter())
    }

    @Test
    fun `Filter cashback clicked twice`() {
        // Given
        viewModel.filter.observeForever(filterObserver)

        // When
        viewModel.toggleFilter(VoucherType.paramCashback)
        viewModel.toggleFilter(VoucherType.paramCashback)
        viewModel.loadVouchers(Dummy.firstPage)

        // Then
        verify { filterObserver.onChanged(NO_FILTER) }
    }

    @Test
    fun `Filter cashback clicked then click free-ongkir filter`() {
        //  Given
        viewModel.filter.observeForever(filterObserver)

        // When
        viewModel.toggleFilter(VoucherType.paramCashback)
        viewModel.toggleFilter(VoucherType.paramFreeOngkir)
        viewModel.loadVouchers(Dummy.firstPage)

        // Then
        verify { filterObserver.onChanged(VoucherType.paramFreeOngkir) }
    }

    @Test
    fun `Error value changed on error`() {
        // Given
        val errorObserver = mockk<Observer<Throwable>>(relaxed = true)
        every { getVoucherUseCase.getVouchers(Dummy.firstPage, any(), any(), captureLambda()) } answers {
            val onError = lambda<(Throwable) -> Unit>()
            onError.invoke(Dummy.exThrowable)
        }

        // When
        viewModel.error.observeForever(errorObserver)
        viewModel.loadVouchers(Dummy.firstPage)

        // Then
        verify { errorObserver.onChanged(Dummy.exThrowable) }
    }

    @Test
    fun `hasNext get from use case`() {
        // Given
        every { getVoucherUseCase.hasNext } returns true

        // When
        viewModel.hasNext

        // Then
        assertTrue(viewModel.hasNext)
    }

    @Test
    fun `currentPage is updated when load voucher`() {
        // Given
        every { getVoucherUseCase.getVouchers(Dummy.firstPage, any(), any(), any()) } just Runs

        // When
        viewModel.loadVouchers(Dummy.firstPage)

        // Then
        assertTrue(viewModel.currentPage == Dummy.firstPage)
    }
}