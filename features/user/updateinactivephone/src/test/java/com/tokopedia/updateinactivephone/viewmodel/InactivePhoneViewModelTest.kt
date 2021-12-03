package com.tokopedia.updateinactivephone.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant
import com.tokopedia.updateinactivephone.domain.data.GetStatusInactivePhoneNumber
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneUserDataModel
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

    private var viewModel: InactivePhoneViewModel? = null
    private var inactivePhoneUserDataModel = InactivePhoneUserDataModel(
        email = "rivaldy.firmansyah@gmail.com",
        oldPhoneNumber = "084444123123",
        newPhoneNumber = "084444123456",
        userIndex = 1,
        userIdEnc = "#EncryptedUserId",
        validateToken = "#UserToken"
    )

    private val mockThrowable = Throwable("Opps!")

    @Before
    fun setup() {
        viewModel = InactivePhoneViewModel(phoneValidationUseCase, getStatusInactivePhoneNumberUseCase, dispatcherProviderTest)
        viewModel?.phoneValidation?.observeForever(observerPhoneValidation)
        viewModel?.getStatusPhoneNumber?.observeForever(observerGetStatusInactivePhoneNumber)
    }

    @After
    fun tearDown() {
        viewModel?.phoneValidation?.removeObserver(observerPhoneValidation)
        viewModel?.getStatusPhoneNumber?.removeObserver(observerGetStatusInactivePhoneNumber)
    }

    @Test
    fun `Validate Phone Number - Success`() {
        assert(viewModel?.isValidPhoneNumber("089683321123") == true)
    }

    @Test
    fun `Validate Phone Number - empty number`() {
        assert(viewModel?.isValidPhoneNumber("") == true)
    }

    @Test
    fun `Validate Phone Number - Error invalid phone format`() {
        val isValid = viewModel?.isValidPhoneNumber("NOMOR")

        verify {
            observerPhoneValidation.onChanged(any())
        }

        assert(viewModel?.phoneValidation?.value is Fail)

        val result = viewModel?.phoneValidation?.value as Fail
        assertEquals(result.throwable.message, InactivePhoneConstant.ERROR_INVALID_PHONE_NUMBER)
        if (isValid != null) {
            assert(!isValid)
        }
    }

    @Test
    fun `Validate Phone Number - Error exceed max`() {
        val isValid = viewModel?.isValidPhoneNumber("12345678900987654321")

        verify {
            observerPhoneValidation.onChanged(any())
        }

        assert(viewModel?.phoneValidation?.value is Fail)

        val result = viewModel?.phoneValidation?.value as Fail
        assertEquals(result.throwable.message, InactivePhoneConstant.ERROR_PHONE_NUMBER_MAX)
        if (isValid != null) {
            assert(!isValid)
        }
    }

    @Test
    fun `Validate Phone Number - Error less than min`() {
        val isValid = viewModel?.isValidPhoneNumber("08444412")

        verify {
            observerPhoneValidation.onChanged(any())
        }

        assert(viewModel?.phoneValidation?.value is Fail)

        val result = viewModel?.phoneValidation?.value as Fail
        assertEquals(result.throwable.message, InactivePhoneConstant.ERROR_PHONE_NUMBER_MIN)
        if (isValid != null) {
            assert(!isValid)
        }
    }

    @Test
    fun `Phone Validate - Success validate on server`() {
        val mockResponse = PhoneValidationDataModel(PhoneValidationDataModel.Validation(isSuccess = true))

        coEvery {
            phoneValidationUseCase(any())
        } returns mockResponse

        viewModel?.userValidation(inactivePhoneUserDataModel)

        verify {
            observerPhoneValidation.onChanged(any())
        }

        assert(viewModel?.phoneValidation?.value is Success)

        val result = viewModel?.phoneValidation?.value as Success
        assert(result.data.validation.isSuccess)
    }

    @Test
    fun `Phone Validate - Failed validate on server`() {
        val mockResponse = PhoneValidationDataModel(PhoneValidationDataModel.Validation(isSuccess = false))

        coEvery {
            phoneValidationUseCase(any())
        } returns mockResponse

        viewModel?.userValidation(inactivePhoneUserDataModel)

        verify {
            observerPhoneValidation.onChanged(any())
        }

        assert(viewModel?.phoneValidation?.value is Success)

        val result = viewModel?.phoneValidation?.value as Success
        assert(!result.data.validation.isSuccess)
    }

    @Test
    fun `Phone Validate - Fail`() {
        coEvery {
            phoneValidationUseCase(any())
        }.throws(mockThrowable)

        viewModel?.userValidation(inactivePhoneUserDataModel)

        coVerify {
            observerPhoneValidation.onChanged(any())
        }

        assert(viewModel?.phoneValidation?.value is Fail)
    }

    @Test
    fun `Phone Validate - empty params`() {
        inactivePhoneUserDataModel = InactivePhoneUserDataModel(
                email = "",
                oldPhoneNumber = ""
        )

        viewModel?.userValidation(inactivePhoneUserDataModel)

        coVerify {
            observerPhoneValidation.onChanged(any())
        }

        assert(viewModel?.phoneValidation?.value is Fail)
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

        viewModel?.getStatusPhoneNumber(inactivePhoneUserDataModel)

        coVerify {
            observerGetStatusInactivePhoneNumber.onChanged(expectedValue)
        }

        assertEquals(viewModel?.getStatusPhoneNumber?.value, expectedValue)
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

        viewModel?.getStatusPhoneNumber(inactivePhoneUserDataModel)

        coVerify {
            observerGetStatusInactivePhoneNumber.onChanged(expectedValue)
        }

        assertEquals(viewModel?.getStatusPhoneNumber?.value, expectedValue)

        val result = viewModel?.getStatusPhoneNumber?.value as Success
        assert(!result.data.statusInactivePhoneNumber.isSuccess)
        assert(!result.data.statusInactivePhoneNumber.isAllowed)
    }

    @Test
    fun `Get Status Inactive Phone Validate - Fail`() {
        coEvery {
            getStatusInactivePhoneNumberUseCase(any())
        }.throws(mockThrowable)

        viewModel?.getStatusPhoneNumber(inactivePhoneUserDataModel)

        coVerify {
            observerGetStatusInactivePhoneNumber.onChanged(any())
        }

        assert(viewModel?.getStatusPhoneNumber?.value is Fail)
    }
}