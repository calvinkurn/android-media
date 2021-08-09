package com.tokopedia.promocheckout.detail.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.promocheckout.detail.domain.GetDetailPromoCheckoutUseCase
import com.tokopedia.promocheckout.list.domain.DealsCheckVoucherUseCase
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
class PromoCheckoutDetailDealsViewModelTest{
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

    private val dealsCheckVoucherUseCase: DealsCheckVoucherUseCase = mockk(relaxed = true)
    private val getDetailPromoCheckoutUseCase: GetDetailPromoCheckoutUseCase = mockk(relaxed = true)
    private lateinit var viewModel: PromoCheckoutDetailDealsViewModel
    private val jsonObject: JsonObject = JsonObject()

    @Before
    fun setup(){
        viewModel = PromoCheckoutDetailDealsViewModel(dispatcher, dealsCheckVoucherUseCase, getDetailPromoCheckoutUseCase)
    }

    @Test
    fun checkPromoCode_isSuccess(){
        //given
        coEvery {
            dealsCheckVoucherUseCase.execute(any() as HashMap<String, Boolean>,any())
        } returns Success(DUMMY_DATA_UI_MODEL)

        //then
        viewModel.checkPromoCode(false, jsonObject)

        //when
        assert(viewModel.dealsCheckVoucherResult.value is Success)
        assert(viewModel.showProgressLoadingPromoDeals.value is Boolean)
        assertEquals(Success(DUMMY_DATA_UI_MODEL), (viewModel.dealsCheckVoucherResult.value) as Success)
        assertEquals(false, viewModel.showProgressLoadingPromoDeals.value)
    }

    @Test
    fun checkPromoCode_isFailed(){
        //given
        coEvery {
            dealsCheckVoucherUseCase.execute(any() as HashMap<String, Boolean>,any())
        } returns Fail(FAILED_CHECK_VOUCHER_MESSAGE_EXCEPTION)

        //then
        viewModel.checkPromoCode(false, jsonObject)

        //when
        assert(viewModel.dealsCheckVoucherResult.value is Fail)
        assert(viewModel.showProgressLoadingPromoDeals.value is Boolean)
        assertEquals(Fail(FAILED_CHECK_VOUCHER_MESSAGE_EXCEPTION), (viewModel.dealsCheckVoucherResult.value) as Fail)
        assertEquals(false, viewModel.showProgressLoadingPromoDeals.value)
    }

    @Test
    fun checkPromoCode_isGeneralError(){
        //given
        coEvery {
            dealsCheckVoucherUseCase.execute(any() as HashMap<String, Boolean>,any())
        } returns Fail(FAILED_CHECK_VOUCHER_GENERAL_EXCEPTION)

        //then
        viewModel.checkPromoCode(false, jsonObject)

        //when
        assert(viewModel.dealsCheckVoucherResult.value is Fail)
        assert(viewModel.showProgressLoadingPromoDeals.value is Boolean)
        assertEquals(Fail(FAILED_CHECK_VOUCHER_GENERAL_EXCEPTION), (viewModel.dealsCheckVoucherResult.value) as Fail)
        assertEquals(false, viewModel.showProgressLoadingPromoDeals.value)
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
        assert(viewModel.showLoadingPromoDeals.value is Boolean)
        assertEquals(Success(SUCCESS_DETAIL_PROMO), (viewModel.promoCheckoutDetail.value) as Success)
        assertEquals(false, viewModel.showLoadingPromoDeals.value)
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
        assert(viewModel.showLoadingPromoDeals.value is Boolean)
        assertEquals(Fail(FAILED_CHECK_VOUCHER_MESSAGE_EXCEPTION), (viewModel.promoCheckoutDetail.value) as Fail)
        assertEquals(false, viewModel.showLoadingPromoDeals.value)
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
        assert(viewModel.showLoadingPromoDeals.value is Boolean)
        assertEquals(Fail(FAILED_CHECK_VOUCHER_GENERAL_EXCEPTION), (viewModel.promoCheckoutDetail.value) as Fail)
        assertEquals(false, viewModel.showLoadingPromoDeals.value)
    }
}