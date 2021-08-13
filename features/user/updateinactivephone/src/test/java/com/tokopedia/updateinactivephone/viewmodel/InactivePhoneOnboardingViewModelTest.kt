package com.tokopedia.updateinactivephone.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant
import com.tokopedia.updateinactivephone.domain.data.PhoneValidationDataModel
import com.tokopedia.updateinactivephone.domain.usecase.PhoneValidationUseCase
import com.tokopedia.updateinactivephone.view.viewmodel.InactivePhoneOnboardingViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class InactivePhoneOnboardingViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val dispatcher = TestCoroutineDispatcher()
    val phoneValidationUseCase = mockk<PhoneValidationUseCase>(relaxed = true)
    val observerPhoneValidation = mockk<Observer<Result<PhoneValidationDataModel>>>(relaxed = true)

    lateinit var viewModel: InactivePhoneOnboardingViewModel

    var phoneNumber = "62800000000000"
    var email = "account@test.com"
    val mockThrowable = Throwable("Opss!")

    @Before
    fun setup() {
        viewModel = InactivePhoneOnboardingViewModel(phoneValidationUseCase, dispatcher)
        viewModel.phoneValidation.observeForever(observerPhoneValidation)
    }

    @After
    fun tearDown() {
        viewModel.onCleared()
        viewModel.phoneValidation.removeObserver(observerPhoneValidation)
    }

    @Test
    fun `Validate Phone Number - Error invalid phone format`() {
        viewModel.isValidPhoneNumber("NOMOR")

        verify {
            observerPhoneValidation.onChanged(any())
        }

        assert(viewModel.phoneValidation.value is Fail)

        val result = viewModel.phoneValidation.value as Fail
        assertEquals(result.throwable.message, InactivePhoneConstant.ERROR_INVALID_PHONE_NUMBER)
    }

    @Test
    fun `Validate Phone Number - Error exceed max`() {
        viewModel.isValidPhoneNumber("12345678900987654321")

        verify {
            observerPhoneValidation.onChanged(any())
        }

        assert(viewModel.phoneValidation.value is Fail)

        val result = viewModel.phoneValidation.value as Fail
        assertEquals(result.throwable.message, InactivePhoneConstant.ERROR_PHONE_NUMBER_MAX)
    }

    @Test
    fun `Validate Phone Number - Error less than min`() {
        viewModel.isValidPhoneNumber("123")

        verify {
            observerPhoneValidation.onChanged(any())
        }

        assert(viewModel.phoneValidation.value is Fail)

        val result = viewModel.phoneValidation.value as Fail
        assertEquals(result.throwable.message, InactivePhoneConstant.ERROR_PHONE_NUMBER_MIN)
    }

    @Test
    fun `Phone Validate - Success validate on server`() {
        val mockResponse = PhoneValidationDataModel(PhoneValidationDataModel.Validation(isSuccess = true))

        every {
            phoneValidationUseCase.execute(any(), any())
        } answers {
            firstArg<(PhoneValidationDataModel) -> Unit>().invoke(mockResponse)
        }

        viewModel.phoneValidation(phoneNumber, email)

        verify {
            observerPhoneValidation.onChanged(any())
        }

        assert(viewModel.phoneValidation.value is Success)

        val result = viewModel.phoneValidation.value as Success
        assert(result.data.validation.isSuccess)
    }

    @Test
    fun `Phone Validate - Failed validate on server`() {
        every {
            phoneValidationUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewModel.phoneValidation(phoneNumber, email)

        verify {
            observerPhoneValidation.onChanged(any())
        }

        assert(viewModel.phoneValidation.value is Fail)

        val result = viewModel.phoneValidation.value as Fail
        assertEquals(result.throwable, mockThrowable)
    }
}