package com.tokopedia.updateinactivephone.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant
import com.tokopedia.updateinactivephone.domain.data.GetStatusInactivePhoneNumber
import com.tokopedia.updateinactivephone.domain.data.PhoneValidationDataModel
import com.tokopedia.updateinactivephone.domain.data.StatusInactivePhoneNumberDataModel
import com.tokopedia.updateinactivephone.domain.usecase.GetStatusInactivePhoneNumberUseCase
import com.tokopedia.updateinactivephone.domain.usecase.PhoneValidationUseCase
import com.tokopedia.updateinactivephone.features.InactivePhoneViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class InactivePhoneViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcherProviderTest = CoroutineTestDispatchersProvider
    val phoneValidationUseCase = mockk<PhoneValidationUseCase>(relaxed = true)
    val getStatusInactivePhoneNumberUseCase = mockk<GetStatusInactivePhoneNumberUseCase>(relaxed = true)

    val observerPhoneValidation = mockk<Observer<Result<PhoneValidationDataModel>>>(relaxed = true)
    val observerGetStatusInactivePhoneNumber = mockk<Observer<Result<StatusInactivePhoneNumberDataModel>>>(relaxed = true)

    lateinit var viewModel: InactivePhoneViewModel

    var phoneNumber = "62800000000000"
    var email = "account@test.com"
    var userIndex = 1
    val mockThrowable = Throwable("Opss!")

    @Before
    fun setup() {
        viewModel = InactivePhoneViewModel(phoneValidationUseCase, getStatusInactivePhoneNumberUseCase, dispatcherProviderTest)
        viewModel.phoneValidation.observeForever(observerPhoneValidation)
        viewModel.getStatusPhoneNumber.observeForever(observerGetStatusInactivePhoneNumber)
    }

    @After
    fun tearDown() {
        viewModel.onCleared()
        viewModel.phoneValidation.removeObserver(observerPhoneValidation)
        viewModel.getStatusPhoneNumber.removeObserver(observerGetStatusInactivePhoneNumber)
    }

    @Test
    fun `Validate Phone Number - Success`() {
        assert(viewModel.isValidPhoneNumber("089683321123"))
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

        viewModel.userValidation(phoneNumber, email)

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

        viewModel.userValidation(phoneNumber, email)

        verify {
            observerPhoneValidation.onChanged(any())
        }

        assert(viewModel.phoneValidation.value is Fail)

        val result = viewModel.phoneValidation.value as Fail
        assertEquals(result.throwable, mockThrowable)
    }

    @Test
    fun `Get Status Inactive Phone Validate - Success`() {
        val mockResponse = StatusInactivePhoneNumberDataModel(GetStatusInactivePhoneNumber(
            isSuccess = true,
            isAllowed = true,
            userIdEnc = "7Bf2:d2ZGkaFaEsLrKuc4ZrULR79PoEwgtTaW/npKvoRyCRdViRc=",
            errorMessage = ""
        ))

        val expectedValue = Success(mockResponse)

        coEvery {
            getStatusInactivePhoneNumberUseCase(any())
        } returns mockResponse

        viewModel.getStatusPhoneNumber(email, phoneNumber, userIndex)

        coVerify {
            observerGetStatusInactivePhoneNumber.onChanged(expectedValue)
        }

        assertEquals(viewModel.getStatusPhoneNumber.value, expectedValue)
    }

    @Test
    fun `Get Status Inactive Phone Validate - Failed not allowed`() {
        val mockResponse = StatusInactivePhoneNumberDataModel(GetStatusInactivePhoneNumber(
            isSuccess = false,
            isAllowed = false,
            userIdEnc = "",
            errorMessage = "Opps!"
        ))

        val expectedValue = Success(mockResponse)

        coEvery {
            getStatusInactivePhoneNumberUseCase(any())
        } returns mockResponse

        viewModel.getStatusPhoneNumber(email, phoneNumber, userIndex)

        coVerify {
            observerGetStatusInactivePhoneNumber.onChanged(expectedValue)
        }

        assertEquals(viewModel.getStatusPhoneNumber.value, expectedValue)

        val result = viewModel.getStatusPhoneNumber.value as Success
        assert(!result.data.statusInactivePhoneNumber.isSuccess)
        assert(!result.data.statusInactivePhoneNumber.isAllowed)
    }
}