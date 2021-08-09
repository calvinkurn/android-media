package com.tokopedia.promocheckout.list.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.promocheckout.common.domain.model.event.EventVerifyBody
import com.tokopedia.promocheckout.list.domain.EventCheckVoucherUseCase
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
class PromoCheckoutListEventViewModelTest{
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

    private val eventCheckVoucherUseCase: EventCheckVoucherUseCase = mockk(relaxed = true)
    private lateinit var viewModel: PromoCheckoutListEventViewModel
    private val eventVerifyBody: EventVerifyBody = EventVerifyBody()

    @Before
    fun setup(){
        viewModel = PromoCheckoutListEventViewModel(dispatcher, eventCheckVoucherUseCase)
    }

    @Test
    fun checkPromo_isSuccess(){
        //given
        coEvery {
            eventCheckVoucherUseCase.execute(any() as HashMap<String, Boolean>,any())
        } returns Success(DUMMY_DATA_UI_MODEL)

        //then
        viewModel.checkPromoCode(false, eventVerifyBody)

        //when
        assert(viewModel.eventCheckVoucherResult.value is Success)
        assert(viewModel.showLoadingPromoEvent.value is Boolean)
        assertEquals(Success(DUMMY_DATA_UI_MODEL), (viewModel.eventCheckVoucherResult.value) as Success)
        assertEquals(false, viewModel.showLoadingPromoEvent.value)
    }

    @Test
    fun checkPromo_isFailed(){
        //given
        coEvery {
            eventCheckVoucherUseCase.execute(any() as HashMap<String, Boolean>,any())
        } returns Fail(FAILED_CHECK_VOUCHER_MESSAGE_EXCEPTION)

        //then
        viewModel.checkPromoCode(false, eventVerifyBody)

        //when
        assert(viewModel.eventCheckVoucherResult.value is Fail)
        assert(viewModel.showLoadingPromoEvent.value is Boolean)
        assertEquals(Fail(FAILED_CHECK_VOUCHER_MESSAGE_EXCEPTION), (viewModel.eventCheckVoucherResult.value) as Fail)
        assertEquals(false, viewModel.showLoadingPromoEvent.value)
    }

    @Test
    fun checkPromo_isGeneralError(){
        //given
        coEvery {
            eventCheckVoucherUseCase.execute(any() as HashMap<String, Boolean>,any())
        } returns Fail(FAILED_CHECK_VOUCHER_GENERAL_EXCEPTION)

        //then
        viewModel.checkPromoCode(false, eventVerifyBody)

        //when
        assert(viewModel.eventCheckVoucherResult.value is Fail)
        assert(viewModel.showLoadingPromoEvent.value is Boolean)
        assertEquals(Fail(FAILED_CHECK_VOUCHER_GENERAL_EXCEPTION), (viewModel.eventCheckVoucherResult.value) as Fail)
        assertEquals(false, viewModel.showLoadingPromoEvent.value)
    }
}