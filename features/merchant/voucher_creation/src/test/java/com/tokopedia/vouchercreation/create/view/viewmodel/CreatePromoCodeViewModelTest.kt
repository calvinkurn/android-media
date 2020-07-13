package com.tokopedia.vouchercreation.create.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.create.domain.usecase.validation.PromoCodeValidationUseCase
import com.tokopedia.vouchercreation.create.view.uimodel.validation.PromoCodeValidation
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

@ExperimentalCoroutinesApi
class CreatePromoCodeViewModelTest {

    @RelaxedMockK
    lateinit var promoCodeValidationUseCase: PromoCodeValidationUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @After
    fun cleanup() {
        testDispatcher.cleanupTestCoroutines()
    }

    private val testDispatcher by lazy {
        TestCoroutineDispatcher()
    }

    private val mViewModel by lazy {
        CreatePromoCodeViewModel(testDispatcher, promoCodeValidationUseCase)
    }

    @Test
    fun `success validate promo code`() = runBlocking {
        val dummySuccessValidation = PromoCodeValidation("")

        coEvery {
            promoCodeValidationUseCase.executeOnBackground()
        } returns dummySuccessValidation

        mViewModel.validatePromoCode(anyString())

        mViewModel.coroutineContext[Job]?.children?.forEach { it.join() }

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

        mViewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            promoCodeValidationUseCase.executeOnBackground()
        }

        assert(mViewModel.promoCodeValidationLiveData.value is Fail)
    }

}