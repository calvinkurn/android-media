package com.tokopedia.promocheckout.detail.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.promocheckout.detail.domain.GetDetailPromoCheckoutUseCase
import com.tokopedia.promocheckout.detail.domain.TravelCancelVoucherUseCase
import com.tokopedia.promocheckout.list.domain.HotelCheckVoucherUseCase
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

/**
 * @author: astidhiyaa on 06/08/21.
 */
@RunWith(JUnit4::class)
class PromoCheckoutDetailHotelViewModelTest{
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
    private val getDetailPromoCheckoutUseCase: GetDetailPromoCheckoutUseCase = mockk(relaxed = true)
    private val travelCancelVoucherUseCase: TravelCancelVoucherUseCase = mockk(relaxed = true)
    private lateinit var viewModel: PromoCheckoutDetailHotelViewModel

    @Before
    fun setup(){
        viewModel = PromoCheckoutDetailHotelViewModel(dispatcher, hotelCheckVoucherUseCase, getDetailPromoCheckoutUseCase, travelCancelVoucherUseCase)
    }

    @Test
    fun checkPromoCode_isSuccess(){
        //given
        coEvery {
            hotelCheckVoucherUseCase.execute(any(), any() as String)
        } returns Success(DUMMY_DATA_UI_MODEL)

        //then
        viewModel.checkPromoCode(PROMO_CODE, CART_ID, HEX_COLOR)

        //when
        assert(viewModel.hotelCheckVoucherResult.value is Success)
        assert(viewModel.showProgressLoadingPromoHotel.value is Boolean)
        assertEquals(Success(DUMMY_DATA_UI_MODEL), (viewModel.hotelCheckVoucherResult.value) as Success)
        assertEquals(false, viewModel.showProgressLoadingPromoHotel.value)
    }

    @Test
    fun checkPromoCode_isFailed(){
        //given
        coEvery {
            hotelCheckVoucherUseCase.execute(any(), any() as String)
        } returns Fail(FAILED_CHECK_VOUCHER_MESSAGE_EXCEPTION)

        //then
        viewModel.checkPromoCode(PROMO_CODE, CART_ID, HEX_COLOR)

        //when
        assert(viewModel.hotelCheckVoucherResult.value is Fail)
        assert(viewModel.showProgressLoadingPromoHotel.value is Boolean)
        assertEquals(Fail(FAILED_CHECK_VOUCHER_MESSAGE_EXCEPTION), (viewModel.hotelCheckVoucherResult.value) as Fail)
        assertEquals(false, viewModel.showProgressLoadingPromoHotel.value)
    }

    @Test
    fun checkPromoCode_isGeneralError(){
        //given
        coEvery {
            hotelCheckVoucherUseCase.execute(any(), any() as String)
        } returns Fail(FAILED_CHECK_VOUCHER_GENERAL_EXCEPTION)

        //then
        viewModel.checkPromoCode(PROMO_CODE, CART_ID, HEX_COLOR)

        //when
        assert(viewModel.hotelCheckVoucherResult.value is Fail)
        assert(viewModel.showProgressLoadingPromoHotel.value is Boolean)
        assertEquals(Fail(FAILED_CHECK_VOUCHER_GENERAL_EXCEPTION), (viewModel.hotelCheckVoucherResult.value) as Fail)
        assertEquals(false, viewModel.showProgressLoadingPromoHotel.value)
    }

    @Test
    fun getDetailPromo_isSuccess(){
        //given
        coEvery {
            getDetailPromoCheckoutUseCase.execute(any())
        } returns Success(SUCCESS_DETAIL_PROMO)

        //then
        viewModel.getDetailPromo(PROMO_CODE)

        //when
        assert(viewModel.promoCheckoutDetail.value is Success)
        assert(viewModel.showLoadingPromoHotel.value is Boolean)
        assertEquals(Success(SUCCESS_DETAIL_PROMO), (viewModel.promoCheckoutDetail.value) as Success)
        assertEquals(false, viewModel.showLoadingPromoHotel.value)
    }

    @Test
    fun getDetailPromo_isFailed(){
        //given
        coEvery {
            getDetailPromoCheckoutUseCase.execute(any())
        } returns Fail(FAILED_CHECK_VOUCHER_MESSAGE_EXCEPTION)

        //then
        viewModel.getDetailPromo(PROMO_CODE)

        //when
        assert(viewModel.promoCheckoutDetail.value is Fail)
        assert(viewModel.showLoadingPromoHotel.value is Boolean)
        assertEquals(Fail(FAILED_CHECK_VOUCHER_MESSAGE_EXCEPTION), (viewModel.promoCheckoutDetail.value) as Fail)
        assertEquals(false, viewModel.showLoadingPromoHotel.value)
    }

    @Test
    fun getDetailPromo_isGeneralError(){
        //given
        coEvery {
            getDetailPromoCheckoutUseCase.execute(any())
        } returns Fail(FAILED_CHECK_VOUCHER_GENERAL_EXCEPTION)

        //then
        viewModel.getDetailPromo(PROMO_CODE)

        //when
        assert(viewModel.promoCheckoutDetail.value is Fail)
        assert(viewModel.showLoadingPromoHotel.value is Boolean)
        assertEquals(Fail(FAILED_CHECK_VOUCHER_GENERAL_EXCEPTION), (viewModel.promoCheckoutDetail.value) as Fail)
        assertEquals(false, viewModel.showLoadingPromoHotel.value)
    }

    @Test
    fun cancelPromo_isSuccess(){
        //given
        coEvery {
            travelCancelVoucherUseCase.execute()
        } returns Success(SUCCESS_CANCEL_VOUCHER)

        //then
        viewModel.cancelPromo()

        //when
        assert(viewModel.cancelVoucher.value is Success)
        assert(viewModel.showLoadingPromoHotel.value is Boolean)
        assertEquals(Success(SUCCESS_CANCEL_VOUCHER), (viewModel.cancelVoucher.value) as Success)
        assertEquals(false, viewModel.showLoadingPromoHotel.value)
    }

    @Test
    fun cancelPromo_isFailed(){
        //given
        coEvery {
            travelCancelVoucherUseCase.execute()
        } returns Fail(FAILED_CHECK_VOUCHER_MESSAGE_EXCEPTION)

        //then
        viewModel.cancelPromo()

        //when
        assert(viewModel.cancelVoucher.value is Fail)
        assert(viewModel.showLoadingPromoHotel.value is Boolean)
        assertEquals(Fail(FAILED_CHECK_VOUCHER_MESSAGE_EXCEPTION), (viewModel.cancelVoucher.value) as Fail)
        assertEquals(false, viewModel.showLoadingPromoHotel.value)
    }

    @Test
    fun cancelPromo_isGeneralError(){
        //given
        coEvery {
            travelCancelVoucherUseCase.execute()
        } returns Fail(FAILED_CHECK_VOUCHER_GENERAL_EXCEPTION)

        //then
        viewModel.cancelPromo()

        //when
        assert(viewModel.cancelVoucher.value is Fail)
        assert(viewModel.showLoadingPromoHotel.value is Boolean)
        assertEquals(Fail(FAILED_CHECK_VOUCHER_GENERAL_EXCEPTION), (viewModel.cancelVoucher.value) as Fail)
        assertEquals(false, viewModel.showLoadingPromoHotel.value)
    }
}