package com.tokopedia.promocheckout.list.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.promocheckout.common.view.uimodel.PromoDigitalModel
import com.tokopedia.promocheckout.list.domain.DigitalCheckVoucherUseCase
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
class PromoCheckoutListDigitalViewModelTest {
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

    private val digitalCheckVoucherUseCase: DigitalCheckVoucherUseCase = mockk(relaxed = true)
    private lateinit var viewModel: PromoCheckoutListDigitalViewModel
    private val promoDigitalModel: PromoDigitalModel = PromoDigitalModel()

    @Before
    fun setup(){
        viewModel = PromoCheckoutListDigitalViewModel(dispatcher, digitalCheckVoucherUseCase)
    }

    @Test
    fun checkPromo_isSuccess(){
        //given
        coEvery {
            digitalCheckVoucherUseCase.execute(any())
        } returns Success(DUMMY_DATA_UI_MODEL)

        //then
        viewModel.checkPromoCode(PROMO_CODE, promoDigitalModel)

        //when
        assert(viewModel.digitalCheckVoucherResult.value is Success)
        assert(viewModel.showLoadingPromoDigital.value is Boolean)
        assertEquals(Success(DUMMY_DATA_UI_MODEL), (viewModel.digitalCheckVoucherResult.value) as Success)
        assertEquals(false, viewModel.showLoadingPromoDigital.value)
    }

    @Test
    fun checkPromo_isFailed(){
        //given
        coEvery {
            digitalCheckVoucherUseCase.execute(any())
        } returns Fail(FAILED_CHECK_VOUCHER_MESSAGE_EXCEPTION)

        //then
        viewModel.checkPromoCode(PROMO_CODE, promoDigitalModel)

        //when
        assert(viewModel.digitalCheckVoucherResult.value is Fail)
        assert(viewModel.showLoadingPromoDigital.value is Boolean)
        assertEquals(Fail(FAILED_CHECK_VOUCHER_MESSAGE_EXCEPTION), (viewModel.digitalCheckVoucherResult.value) as Fail)
        assertEquals(false, viewModel.showLoadingPromoDigital.value)
    }

    @Test
    fun checkPromo_isGeneralError(){
        //given
        coEvery {
            digitalCheckVoucherUseCase.execute(any())
        } returns Fail(FAILED_CHECK_VOUCHER_GENERAL_EXCEPTION)

        //then
        viewModel.checkPromoCode(PROMO_CODE, promoDigitalModel)

        //when
        assert(viewModel.digitalCheckVoucherResult.value is Fail)
        assert(viewModel.showLoadingPromoDigital.value is Boolean)
        assertEquals(Fail(FAILED_CHECK_VOUCHER_GENERAL_EXCEPTION), (viewModel.digitalCheckVoucherResult.value) as Fail)
        assertEquals(false, viewModel.showLoadingPromoDigital.value)
    }
}