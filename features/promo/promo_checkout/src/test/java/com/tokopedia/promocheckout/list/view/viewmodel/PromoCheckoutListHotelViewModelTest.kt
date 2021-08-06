package com.tokopedia.promocheckout.list.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.promocheckout.list.domain.HotelCheckVoucherUseCase
import com.tokopedia.promocheckout.mockdata.*
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import org.junit.Assert.assertEquals
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * @author: astidhiyaa on 05/08/21.
 */
@RunWith(JUnit4::class)
class PromoCheckoutListHotelViewModelTest{
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

    private val hotelCheckVoucherUseCase: HotelCheckVoucherUseCase = mockk(relaxed = true)
    private lateinit var viewModel: PromoCheckoutListHotelViewModel

    @Before
    fun setup(){
        viewModel = PromoCheckoutListHotelViewModel(dispatcher, hotelCheckVoucherUseCase)
    }

    @Test
    fun checkPromo_isSuccess(){
        //given
        coEvery {
            hotelCheckVoucherUseCase.execute(any(),any() as String)
        }returns Success(DUMMY_DATA_UI_MODEL)

        //then
        viewModel.checkPromoCode(CART_ID, PROMO_CODE, HEX_COLOR)

        //when
        assert(viewModel.hotelCheckVoucherResult.value is Success)
        assert(viewModel.showLoadingPromoHotel.value is Boolean)
        assertEquals(Success(DUMMY_DATA_UI_MODEL), (viewModel.hotelCheckVoucherResult.value) as Success)
        assertEquals(false, viewModel.showLoadingPromoHotel.value)
    }

    @Test
    fun checkPromo_isFailed(){
        //given
        coEvery {
            hotelCheckVoucherUseCase.execute(any(),any() as String)
        }returns Fail(FAILED_CHECK_VOUCHER_MESSAGE_EXCEPTION)

        //then
        viewModel.checkPromoCode(CART_ID, PROMO_CODE, HEX_COLOR)

        //when
        assert(viewModel.hotelCheckVoucherResult.value is Fail)
        assert(viewModel.showLoadingPromoHotel.value is Boolean)
        assertEquals(Fail(FAILED_CHECK_VOUCHER_MESSAGE_EXCEPTION), (viewModel.hotelCheckVoucherResult.value) as Fail)
        assertEquals(false, viewModel.showLoadingPromoHotel.value)
    }

    @Test
    fun checkPromo_isGeneralError(){
        //given
        coEvery {
            hotelCheckVoucherUseCase.execute(any(),any() as String)
        }returns Fail(FAILED_CHECK_VOUCHER_GENERAL_EXCEPTION)

        //then
        viewModel.checkPromoCode(CART_ID, PROMO_CODE, HEX_COLOR)

        //when
        assert(viewModel.hotelCheckVoucherResult.value is Fail)
        assert(viewModel.showLoadingPromoHotel.value is Boolean)
        assertEquals(Fail(FAILED_CHECK_VOUCHER_GENERAL_EXCEPTION), (viewModel.hotelCheckVoucherResult.value) as Fail)
        assertEquals(false, viewModel.showLoadingPromoHotel.value)
    }
}