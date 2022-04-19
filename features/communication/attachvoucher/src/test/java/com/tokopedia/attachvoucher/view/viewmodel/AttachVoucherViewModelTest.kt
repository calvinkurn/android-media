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
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Spy


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

    @Spy
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
        viewModel = spyk(AttachVoucherViewModel(getVoucherUseCase, dispatcherProvider))
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
        viewModel.loadVouchers(1)

        // Then
        verify(exactly = 1) { viewModel.cancelCurrentLoad() }
    }

    @Test
    fun `load voucher without cancel`() {
        // Given
        viewModel.isLoading = false
        // When
        viewModel.loadVouchers(1)

        // Then
        verify(exactly = 0) { viewModel.cancelCurrentLoad() }


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
        assertEquals(VoucherType.paramCashback, viewModel.filter.value)
    }

    @Test
    fun `Filter cashback not clicked`() {
        // Given
        coEvery {
            getVoucherUseCase(any())
        } returns Dummy.exVouchers

        // When
        viewModel.loadVouchers(Dummy.firstPage)

        // Then
        assertNull(viewModel.filter.value)
        assertTrue(viewModel.hasNoFilter())
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
        assertEquals(true, viewModel.hasNext)
    }

    @Test
    fun `hasn't next get from use case`() {
        coEvery {
            getVoucherUseCase.hasNext
        } returns false

        // When
        viewModel.loadVouchers(Dummy.firstPage)

        // Then
        assertEquals(false, viewModel.hasNext)
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
        assertEquals(viewModel.currentPage, Dummy.firstPage)
    }

    @Test
    fun `filter toogle is clicked`() {
        //Given
        viewModel.toggleFilter(1)
        coVerify { filterObserver.onChanged(1) }
    }

    @Test
    fun `filter toogle is clicked twice`() {
        viewModel.toggleFilter(1)
        viewModel.toggleFilter(1)
        coVerify { filterObserver.onChanged(-1) }
    }

    @Test
    fun `should give false when there is a filter`() {
        //When
        viewModel.setFilter(1)

        //Then
        assert(!viewModel.hasNoFilter())
    }

    @Test
    fun `should give true when filter is NO_FILTER`() {
        //When
        viewModel.setFilter(NO_FILTER)

        //Then
        assert(viewModel.hasNoFilter())
    }

    @Test
    fun `should give true when filter is null`() {
        //When
        viewModel.setFilter(null)

        //Then
        assert(viewModel.hasNoFilter())
    }

    @Test
    fun `test set currentPage`() {
        // Given
        val expectedPage = 1

        // When
        viewModel.currentPage = expectedPage

        // Then
        assertEquals(expectedPage, viewModel.currentPage)
    }

    @Test
    fun `test set isLoading`() {
        // Given
        val expectedLoading = true

        // When
        viewModel.isLoading = expectedLoading

        // Then
        assertEquals(expectedLoading, viewModel.isLoading)
    }
}