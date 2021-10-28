package com.tokopedia.attachvoucher.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.attachvoucher.FileUtil
import com.tokopedia.attachvoucher.data.FilterParam
import com.tokopedia.attachvoucher.data.GetVoucherParam
import com.tokopedia.attachvoucher.data.VoucherUiModel
import com.tokopedia.attachvoucher.data.voucherv2.GetMerchantPromotionGetMVListResponse
import com.tokopedia.attachvoucher.data.voucherv2.MerchantPromotionGetMVList
import com.tokopedia.attachvoucher.mapper.VoucherMapper
import com.tokopedia.attachvoucher.usecase.GetVoucherUseCase
import com.tokopedia.attachvoucher.usecase.GetVoucherUseCase.MVFilter.VoucherType
import com.tokopedia.attachvoucher.view.viewmodel.AttachVoucherViewModel.Companion.NO_FILTER
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
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

    lateinit var viewModel: AttachVoucherViewModel

    private val dispatcherProvider = CoroutineTestDispatchersProvider


    object Dummy {
        val firstPage = 1
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
        viewModel = AttachVoucherViewModel(getVoucherUseCase, dispatcherProvider)
        viewModel.voucher.observeForever(voucherObservers)
        viewModel.filter.observeForever(filterObserver)
        viewModel.error.observeForever(errorObserver)

    }


    @Test
    fun `Vouchers list changed on success`() {
        // Given
        coEvery {
            getVoucherUseCase(any())
        } returns Dummy.exVouchers

        // When
        viewModel.loadVouchers(Dummy.firstPage)

        // Then
        coVerify { getVoucherUseCase(any()) }
        verify { voucherObservers.onChanged(Dummy.exVouchers) }
    }

    @Test
    fun `Cancel previous load voucher`() {
        // Given
        viewModel.isLoading = true

        // When
        viewModel.cancelCurrentLoad()

        // Then
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `Filter cashback clicked`() {
        // Given
        coEvery {
            getVoucherUseCase(any())
        } returns Dummy.exVouchers

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
        } throws Dummy.exThrowable

        // When
        viewModel.loadVouchers(Dummy.firstPage)

        // Then
        coVerify { errorObserver.onChanged(Dummy.exThrowable) }
    }

    @Test
    fun `hasNext get from use case`() {
        // Given
        coEvery {
            getVoucherUseCase.hasNext
        } returns true

        // When
        viewModel.loadVouchers(Dummy.firstPage)

        // Then
        assertTrue(viewModel.hasNext)
    }

    @Test
    fun `currentPage is updated when load voucher`() {
        // Given
        coEvery {
            getVoucherUseCase(any())
        } returns Dummy.exVouchers

        // When
        viewModel.loadVouchers(Dummy.firstPage)

        // Then
        assertTrue(viewModel.currentPage == Dummy.firstPage)
    }
}