package com.tokopedia.updateinactivephone.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.updateinactivephone.domain.data.*
import com.tokopedia.updateinactivephone.domain.usecase.*
import com.tokopedia.updateinactivephone.features.submitnewphone.InactivePhoneDataUploadViewModel
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
class InactivePhoneDataUploadViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcherProviderTest = CoroutineTestDispatchersProvider
    val phoneValidationUseCase = mockk<PhoneValidationUseCase>(relaxed = true)
    val imageUploadUseCase = mockk<ImageUploadUseCase>(relaxed = true)
    val submitDataUseCase = mockk<SubmitDataUseCase>(relaxed = true)
    val submitExpeditedInactivePhoneUseCase = mockk<SubmitExpeditedInactivePhoneUseCase>(relaxed = true)
    val verifyNewPhoneUseCase = mockk<VerifyNewPhoneUseCase>(relaxed = true)

    val observerPhoneValidation = mockk<Observer<Result<PhoneValidationDataModel>>>(relaxed = true)
    val observerImageUpload = mockk<Observer<Result<ImageUploadDataModel>>>(relaxed = true)
    val observerSubmitData = mockk<Observer<Result<InactivePhoneSubmitDataModel>>>(relaxed = true)
    val observerSubmitExpeditedData = mockk<Observer<Result<SubmitExpeditedDataModel>>>(relaxed = true)
    val observerVerifyNewPhone = mockk<Observer<Result<VerifyNewPhoneDataModel>>>(relaxed = true)

    private var viewmodel: InactivePhoneDataUploadViewModel? = null
    private val inactivePhoneUserDataModel = InactivePhoneUserDataModel(
        email = "rivaldy.firmansyah@gmail.com",
        oldPhoneNumber = "084444123123",
        newPhoneNumber = "084444123456",
        userIndex = 1,
        userIdEnc = "#EncryptedUserId",
        validateToken = "#UserToken"
    )
    private val submitDataModel = SubmitDataModel(
        email = inactivePhoneUserDataModel.email,
        oldPhone = inactivePhoneUserDataModel.oldPhoneNumber,
        newPhone = inactivePhoneUserDataModel.newPhoneNumber,
        userIndex = inactivePhoneUserDataModel.userIndex,
        idCardImage = "#IdCardImageFileObject",
        selfieImage = "#SelfieImageFileObject"
    )
    
    val mockThrowable = Throwable("Opss!")

    @Before
    fun setup() {
        viewmodel = InactivePhoneDataUploadViewModel(
            phoneValidationUseCase,
            imageUploadUseCase,
            submitDataUseCase,
            submitExpeditedInactivePhoneUseCase,
            verifyNewPhoneUseCase,
            dispatcherProviderTest
        )

        viewmodel?.phoneValidation?.observeForever(observerPhoneValidation)
        viewmodel?.imageUpload?.observeForever(observerImageUpload)
        viewmodel?.submitData?.observeForever(observerSubmitData)
        viewmodel?.submitDataExpedited?.observeForever(observerSubmitExpeditedData)
        viewmodel?.verifyNewPhone?.observeForever(observerVerifyNewPhone)
    }

    @After
    fun tearDown() {
        viewmodel?.phoneValidation?.removeObserver(observerPhoneValidation)
        viewmodel?.imageUpload?.removeObserver(observerImageUpload)
        viewmodel?.submitData?.removeObserver(observerSubmitData)
        viewmodel?.submitDataExpedited?.removeObserver(observerSubmitExpeditedData)
        viewmodel?.verifyNewPhone?.removeObserver(observerVerifyNewPhone)
    }

    /** Phone Validation */
    @Test
    fun `phone validation - Success validate`() {
        val mockResponse = PhoneValidationDataModel(PhoneValidationDataModel.Validation(isSuccess = true))

        coEvery {
            phoneValidationUseCase(any())
        } returns mockResponse

        viewmodel?.userValidation(inactivePhoneUserDataModel)

        verify {
            observerPhoneValidation.onChanged(any())
        }

        assert(viewmodel?.phoneValidation?.value is Success)

        val result = viewmodel?.phoneValidation?.value as Success
        assert(result.data.validation.isSuccess)
    }

    @Test
    fun `phone validation - Failed validate`() {
        val mockResponse = PhoneValidationDataModel(PhoneValidationDataModel.Validation(isSuccess = false))

        coEvery {
            phoneValidationUseCase(any())
        } returns mockResponse

        viewmodel?.userValidation(inactivePhoneUserDataModel)

        verify {
            observerPhoneValidation.onChanged(any())
        }

        assert(viewmodel?.phoneValidation?.value is Success)

        val result = viewmodel?.phoneValidation?.value as Success
        assert(!result.data.validation.isSuccess)
    }

    @Test
    fun `phone validation - Fail`() {
        coEvery {
            phoneValidationUseCase(any())
        }.throws(mockThrowable)

        viewmodel?.userValidation(inactivePhoneUserDataModel)

        verify {
            observerPhoneValidation.onChanged(any())
        }

        assert(viewmodel?.phoneValidation?.value is Fail)
    }


    /** Upload Image */
    @Test
    fun `Image Upload - Success upload image`() {
        val mockResponse = ImageUploadDataModel(
            status = ImageUploadUseCase.STATUS_OK,
            data = ImageUploadDataModel.PictureObjectDataModel(
                pictureObject = "data sample"
            ))

        every {
            imageUploadUseCase.execute(any(), any())
        } answers {
            firstArg<(ImageUploadDataModel) -> Unit>().invoke(mockResponse)
        }

        viewmodel?.uploadImage(
            inactivePhoneUserDataModel.email, 
            inactivePhoneUserDataModel.newPhoneNumber, 
            inactivePhoneUserDataModel.userIndex,
            "pathFile",
            "source"
        )

        verify {
            observerImageUpload.onChanged(any())
        }

        assert(viewmodel?.imageUpload?.value is Success)

        val result = viewmodel?.imageUpload?.value as Success
        assert(result.data.data.pictureObject.isNotEmpty())
    }

    @Test
    fun `Image Upload - Failed upload image - with error`() {
        val mockResponse = ImageUploadDataModel(
                errors = mutableListOf("Opps!"),
                data = ImageUploadDataModel.PictureObjectDataModel(pictureObject = "data sample")
        )

        every {
            imageUploadUseCase.execute(any(), any())
        } answers {
            firstArg<(ImageUploadDataModel) -> Unit>().invoke(mockResponse)
        }

        viewmodel?.uploadImage(
            inactivePhoneUserDataModel.email,
            inactivePhoneUserDataModel.newPhoneNumber,
            inactivePhoneUserDataModel.userIndex,
            "pathFile",
            "source"
        )

        verify {
            observerImageUpload.onChanged(any())
        }

        assert(viewmodel?.imageUpload?.value is Fail)
    }

    @Test
    fun `Image Upload - Failed upload image`() {
        val mockResponse = ImageUploadDataModel(
                errors = mutableListOf(),
                data = ImageUploadDataModel.PictureObjectDataModel(pictureObject = "")
        )

        every {
            imageUploadUseCase.execute(any(), any())
        } answers {
            firstArg<(ImageUploadDataModel) -> Unit>().invoke(mockResponse)
        }

        viewmodel?.uploadImage(
            inactivePhoneUserDataModel.email,
            inactivePhoneUserDataModel.newPhoneNumber,
            inactivePhoneUserDataModel.userIndex,
            "pathFile",
            "source"
        )

        verify {
            observerImageUpload.onChanged(any())
        }

        assert(viewmodel?.imageUpload?.value is Fail)
    }

    @Test
    fun `Image Upload - Fail`() {
        every {
            imageUploadUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        
        viewmodel?.uploadImage(
            inactivePhoneUserDataModel.email,
            inactivePhoneUserDataModel.newPhoneNumber,
            inactivePhoneUserDataModel.userIndex,
            "pathFile",
            "source"
        )

        verify {
            observerImageUpload.onChanged(any())
        }

        assert(viewmodel?.imageUpload?.value is Fail)

        val result = viewmodel?.imageUpload?.value as Fail
        assertEquals(result.throwable, mockThrowable)
    }

    @Test
    fun `Image Upload - Fail throwable`() {
        every {
            imageUploadUseCase.execute(any(), any())
        }.throws(mockThrowable)

        viewmodel?.uploadImage(
            inactivePhoneUserDataModel.email,
            inactivePhoneUserDataModel.newPhoneNumber,
            inactivePhoneUserDataModel.userIndex,
            "pathFile",
            "source"
        )

        verify {
            observerImageUpload.onChanged(any())
        }

        assert(viewmodel?.imageUpload?.value is Fail)

        val result = viewmodel?.imageUpload?.value as Fail
        assertEquals(result.throwable, mockThrowable)
    }

    /** Submit Data New Phone */
    @Test
    fun `Submit Data - Success submit request`() {
        val mockResponse = InactivePhoneSubmitDataModel(InactivePhoneSubmitDataModel.SubmitInactivePhoneUser(isSuccess = true))

        coEvery {
            submitDataUseCase(any())
        } returns mockResponse

        viewmodel?.submitForm(submitDataModel)

        verify {
            observerSubmitData.onChanged(any())
        }

        assert(viewmodel?.submitData?.value is Success)

        val result = viewmodel?.submitData?.value as Success
        assert(result.data.status.isSuccess)
    }

    @Test
    fun `Submit Data - Failed submit request`() {
        val mockResponse = InactivePhoneSubmitDataModel(InactivePhoneSubmitDataModel.SubmitInactivePhoneUser(isSuccess = false))

        coEvery {
            submitDataUseCase(any())
        } returns mockResponse

        viewmodel?.submitForm(submitDataModel)

        verify {
            observerSubmitData.onChanged(any())
        }

        assert(viewmodel?.submitData?.value is Success)

        val result = viewmodel?.submitData?.value as Success
        assert(!result.data.status.isSuccess)
    }

    @Test
    fun `Submit Data - Fail`() {
        coEvery {
            submitDataUseCase(any())
        }.throws(mockThrowable)

        viewmodel?.submitForm(submitDataModel)

        verify {
            observerSubmitData.onChanged(any())
        }

        assert(viewmodel?.submitData?.value is Fail)
    }

    @Test
    fun `Submit Expedited Data - Success`() {
        val mockResponse = SubmitExpeditedDataModel(SubmitExpeditedDataModel.SubmitDataModel(
            errorMessage = mutableListOf(),
            isSuccess = 1
        ))

        val expectedValue = Success(mockResponse)

        coEvery {
            submitExpeditedInactivePhoneUseCase(any())
        } returns mockResponse

        viewmodel?.submitNewPhoneNumber(inactivePhoneUserDataModel)

        verify {
            observerSubmitExpeditedData.onChanged(expectedValue)
        }

        assertEquals(viewmodel?.submitDataExpedited?.value, expectedValue)

        val result = viewmodel?.submitDataExpedited?.value as Success
        assertEquals(result.data.submit.isSuccess, 1)
    }

    @Test
    fun `Submit Expedited Data - Failed`() {
        val mockResponse = SubmitExpeditedDataModel(SubmitExpeditedDataModel.SubmitDataModel(
            errorMessage = mutableListOf(),
            isSuccess = 0
        ))

        val expectedValue = Success(mockResponse)

        coEvery {
            submitExpeditedInactivePhoneUseCase(any())
        } returns mockResponse

        viewmodel?.submitNewPhoneNumber(inactivePhoneUserDataModel)

        verify {
            observerSubmitExpeditedData.onChanged(expectedValue)
        }

        assertEquals(viewmodel?.submitDataExpedited?.value, expectedValue)

        val result = viewmodel?.submitDataExpedited?.value as Success
        assertEquals(result.data.submit.isSuccess, 0)
    }

    @Test
    fun `Submit Expedited Data - Fail`() {
        coEvery {
            submitExpeditedInactivePhoneUseCase(any())
        }.throws(mockThrowable)

        viewmodel?.submitNewPhoneNumber(inactivePhoneUserDataModel)

        coVerify {
            observerSubmitExpeditedData.onChanged(any())
        }

        assert(viewmodel?.submitDataExpedited?.value is Fail)
    }

    @Test
    fun `verify new phone - success`() {
        val mockResponse = VerifyNewPhoneDataModel(VerifyNewPhoneDataModel.VerifyDataModel(
            isSuccess = true,
            errorMessage = ""
        ))

        val expectedValue = Success(mockResponse)

        coEvery {
            verifyNewPhoneUseCase(any())
        } returns mockResponse

        viewmodel?.verifyNewPhone(inactivePhoneUserDataModel)

        coVerify {
            observerVerifyNewPhone.onChanged(expectedValue)
        }

        assertEquals(viewmodel?.verifyNewPhone?.value, expectedValue)

        val result = viewmodel?.verifyNewPhone?.value as Success
        assert(result.data.verify.isSuccess)
    }

    @Test
    fun `verify new phone - failed`() {
        val mockResponse = VerifyNewPhoneDataModel(VerifyNewPhoneDataModel.VerifyDataModel(
            isSuccess = false,
            errorMessage = ""
        ))

        val expectedValue = Success(mockResponse)

        coEvery {
            verifyNewPhoneUseCase(any())
        } returns mockResponse

        viewmodel?.verifyNewPhone(inactivePhoneUserDataModel)

        coVerify {
            observerVerifyNewPhone.onChanged(expectedValue)
        }

        assertEquals(viewmodel?.verifyNewPhone?.value, expectedValue)

        val result = viewmodel?.verifyNewPhone?.value as Success
        assert(!result.data.verify.isSuccess)
    }

    @Test
    fun `verify new phone - fail`() {
        coEvery {
            verifyNewPhoneUseCase(any())
        }.throws(mockThrowable)

        viewmodel?.verifyNewPhone(inactivePhoneUserDataModel)

        coVerify {
            observerVerifyNewPhone.onChanged(any())
        }

        assert(viewmodel?.verifyNewPhone?.value is Fail)
    }
}