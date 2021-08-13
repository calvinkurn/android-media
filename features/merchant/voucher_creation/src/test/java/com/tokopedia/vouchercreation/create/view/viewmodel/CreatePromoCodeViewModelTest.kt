package com.tokopedia.vouchercreation.create.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.vouchercreation.create.domain.usecase.validation.PromoCodeValidationUseCase
import com.tokopedia.vouchercreation.create.view.uimodel.validation.PromoCodeValidation
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

@ExperimentalCoroutinesApi
class CreatePromoCodeViewModelTest {

    @RelaxedMockK
    lateinit var promoCodeValidationUseCase: PromoCodeValidationUseCase

    lateinit var mViewModel: CreatePromoCodeViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mViewModel = CreatePromoCodeViewModel(CoroutineTestDispatchersProvider, promoCodeValidationUseCase)
    }

    @Test
    fun `success validate promo code`() = runBlocking {
        val dummySuccessValidation = PromoCodeValidation("")

        coEvery {
            promoCodeValidationUseCase.executeOnBackground()
        } returns dummySuccessValidation

        mViewModel.validatePromoCode(anyString())

        coVerify {
            promoCodeValidationUseCase.executeOnBackground()
        }

        assert(mViewModel.promoCodeValidationLiveData.value == Success(dummySuccessValidation))
    }

    @Test
    fun `fail validate promo code`() = runBlocking {
        val dummyThrowable = MessageErrorException("")

        coEvery {
            promoCodeValidationUseCase.executeOnBackground()
        } throws dummyThrowable

        mViewModel.validatePromoCode(anyString())

        coVerify {
            promoCodeValidationUseCase.executeOnBackground()
        }

        assert(mViewModel.promoCodeValidationLiveData.value is Fail)
    }

}