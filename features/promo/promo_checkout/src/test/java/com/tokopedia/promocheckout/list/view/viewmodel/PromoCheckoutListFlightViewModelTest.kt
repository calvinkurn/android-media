package com.tokopedia.promocheckout.list.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.promocheckout.list.domain.FlightCheckVoucherUsecase
import com.tokopedia.promocheckout.mockdata.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class PromoCheckoutListFlightViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = object : CoroutineDispatchers {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Unconfined
        override val io: CoroutineDispatcher
            get() = Dispatchers.Unconfined
        override val default: CoroutineDispatcher
            get() = Dispatchers.Unconfined
        override val immediate: CoroutineDispatcher
            get() = Dispatchers.Unconfined
        override val computation: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }

    private val flightCheckVoucherUseCase: FlightCheckVoucherUsecase = mockk(relaxed = true)
    private lateinit var viewModel: PromoCheckoutListFlightViewModel

    @Before
    fun setup(){
        viewModel = PromoCheckoutListFlightViewModel(dispatcher, flightCheckVoucherUseCase)
    }

    @Test
    fun checkPromo_isSuccess(){
        //given
        coEvery {
            flightCheckVoucherUseCase.execute(any(),any() as String)
        } returns Success(DUMMY_DATA_UI_MODEL)

        //then
        viewModel.checkPromoCode(CART_ID, PROMO_CODE, HEX_COLOR)

        //when
        assert(viewModel.flightCheckVoucherResult.value is Success)
        assert(viewModel.showLoadingPromoFlight.value is Boolean)
        assertEquals(Success(DUMMY_DATA_UI_MODEL), (viewModel.flightCheckVoucherResult.value) as Success)
        assertEquals(false, viewModel.showLoadingPromoFlight.value)
    }

    @Test
    fun checkPromo_isFailed(){
        //given
        coEvery {
            flightCheckVoucherUseCase.execute(any(),any() as String)
        } returns Fail(FAILED_CHECK_VOUCHER_MESSAGE_EXCEPTION)

        //then
        viewModel.checkPromoCode(CART_ID, PROMO_CODE, HEX_COLOR)

        //when
        assert(viewModel.flightCheckVoucherResult.value is Fail)
        assert(viewModel.showLoadingPromoFlight.value is Boolean)
        assertEquals(Fail(FAILED_CHECK_VOUCHER_MESSAGE_EXCEPTION), (viewModel.flightCheckVoucherResult.value) as Fail)
        assertEquals(false, viewModel.showLoadingPromoFlight.value)
    }

    @Test
    fun checkPromo_isGeneralError(){
        //given
        coEvery {
            flightCheckVoucherUseCase.execute(any(),any() as String)
        } returns Fail(FAILED_CHECK_VOUCHER_GENERAL_EXCEPTION)

        //then
        viewModel.checkPromoCode(CART_ID, PROMO_CODE, HEX_COLOR)

        //when
        assert(viewModel.flightCheckVoucherResult.value is Fail)
        assert(viewModel.showLoadingPromoFlight.value is Boolean)
        assertEquals(Fail(FAILED_CHECK_VOUCHER_GENERAL_EXCEPTION), (viewModel.flightCheckVoucherResult.value) as Fail)
        assertEquals(false, viewModel.showLoadingPromoFlight.value)
    }
}