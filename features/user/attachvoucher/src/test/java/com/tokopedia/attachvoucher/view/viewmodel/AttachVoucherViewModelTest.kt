package com.tokopedia.attachvoucher.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
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
import kotlinx.coroutines.Dispatchers
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

    @RelaxedMockK
    lateinit var errorObserver: Observer<Throwable>

    private val mapper = VoucherMapper()

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
        viewModel = AttachVoucherViewModel(getVoucherUseCase, CoroutineDispatchersProvider, mapper)
        viewModel.voucher.observeForever(voucherObservers)
        viewModel.filter.observeForever(filterObserver)
        viewModel.error.observeForever(errorObserver)

    }


    @Test
    fun `Vouchers list changed on success`() {
        // Given
        coEvery {
            getVoucherUseCase(any())
        } returns Dummy.exVouchersGetResponse

        // When
        viewModel.loadVouchers(Dummy.firstPage)

        // Then
        verify { voucherObservers.onChanged(Dummy.exVouchers) }

    }

    @Test
    fun `Cancel previous load voucher`() {
        // Given
        viewModel.isLoading = true
        coEvery {
            getVoucherUseCase(any())
        } returns Dummy.exVouchersGetResponse

        // When
        viewModel.loadVouchers(Dummy.firstPage)

        // Then
        verify { viewModel.cancelCurrentLoad() }
    }


    @Test
    fun `Filter cashback clicked`() {
        // Given
        coEvery {
            getVoucherUseCase(any())
        } returns Dummy.exVouchersGetResponse

        // When
        viewModel.toggleFilter(VoucherType.paramCashback)
        viewModel.loadVouchers(Dummy.firstPage)

        // Then
        verify { filterObserver.onChanged(VoucherType.paramCashback) }
        assertFalse(viewModel.hasNoFilter())
    }

    @Test
    fun `Filter cashback clicked twice`() {
        // When
        viewModel.toggleFilter(VoucherType.paramCashback)
        viewModel.toggleFilter(VoucherType.paramCashback)

        // Then
        verify { filterObserver.onChanged(NO_FILTER) }
    }

    @Test
    fun `Filter cashback clicked then click free-ongkir filter`() {
        // When
        viewModel.toggleFilter(VoucherType.paramCashback)
        viewModel.toggleFilter(VoucherType.paramFreeOngkir)

        // Then
        verify { filterObserver.onChanged(VoucherType.paramFreeOngkir) }
    }

    @Test
    fun `Error value changed on error`() {
        // Given
        coEvery {
            getVoucherUseCase(any())
        } returns Dummy.exVouchersGetResponse

        // When
        viewModel.loadVouchers(Dummy.firstPage)

        // Then
        verify { errorObserver.onChanged(Dummy.exThrowable) }
    }

    @Test
    fun `hasNext get from use case`() {
        // Given
        viewModel.hasNext = true
        coEvery {
            getVoucherUseCase(any())
        } just Runs

        // When
        viewModel.loadVouchers(Dummy.firstPage)

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