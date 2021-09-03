package com.tokopedia.updateinactivephone.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.updateinactivephone.domain.data.*
import com.tokopedia.updateinactivephone.domain.usecase.ImageUploadUseCase
import com.tokopedia.updateinactivephone.domain.usecase.PhoneValidationUseCase
import com.tokopedia.updateinactivephone.domain.usecase.SubmitDataUseCase
import com.tokopedia.updateinactivephone.domain.usecase.SubmitExpeditedInactivePhoneUseCase
import com.tokopedia.updateinactivephone.features.submitnewphone.InactivePhoneDataUploadViewModel
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.every
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertEquals
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

    val observerPhoneValidation = mockk<Observer<Result<PhoneValidationDataModel>>>(relaxed = true)
    val observerImageUpload = mockk<Observer<Result<ImageUploadDataModel>>>(relaxed = true)
    val observerSubmitData = mockk<Observer<Result<InactivePhoneSubmitDataModel>>>(relaxed = true)
    val observerSubmitExpeditedData = mockk<Observer<Result<SubmitExpeditedInactivePhoneDataModel>>>(relaxed = true)

    lateinit var viewmodel: InactivePhoneDataUploadViewModel

    val phoneNumber = "62800000000000"
    val email = "asdfghk@tokopedia.com"
    val mockThrowable = Throwable("Opss!")

    @Before
    fun setup() {
        viewmodel = InactivePhoneDataUploadViewModel(
                phoneValidationUseCase,
                imageUploadUseCase,
                submitDataUseCase,
                submitExpeditedInactivePhoneUseCase,
                dispatcherProviderTest
        )

        viewmodel.phoneValidation.observeForever(observerPhoneValidation)
        viewmodel.imageUpload.observeForever(observerImageUpload)
        viewmodel.submitData.observeForever(observerSubmitData)
        viewmodel.submitDataExpedited.observeForever(observerSubmitExpeditedData)
    }

    @After
    fun tearDown() {
        viewmodel.onCleared()

        viewmodel.phoneValidation.removeObserver(observerPhoneValidation)
        viewmodel.imageUpload.removeObserver(observerImageUpload)
        viewmodel.submitData.removeObserver(observerSubmitData)
        viewmodel.submitDataExpedited.removeObserver(observerSubmitExpeditedData)
    }

    /** Phone Validation */
    @Test
    fun `phone validation - Success validate`() {
        val mockResponse = PhoneValidationDataModel(
                PhoneValidationDataModel.Validation(isSuccess = true)
        )

        every {
            phoneValidationUseCase.execute(any(), any())
        } answers {
            firstArg<(PhoneValidationDataModel) -> Unit>().invoke(mockResponse)
        }

        viewmodel.userValidation(phoneNumber, email, 1)

        every {
            observerPhoneValidation.onChanged(any())
        }

        assert(viewmodel.phoneValidation.value is Success)

        val result = viewmodel.phoneValidation.value as Success
        assert(result.data.validation.isSuccess)
    }

    @Test
    fun `phone validation - Failed validate`() {
        every {
            phoneValidationUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewmodel.userValidation(phoneNumber, email, 1)

        every {
            observerPhoneValidation.onChanged(any())
        }

        assert(viewmodel.phoneValidation.value is Fail)

        val result = viewmodel.phoneValidation.value as Fail
        assertEquals(result.throwable, mockThrowable)
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

        viewmodel.uploadImage(email, phoneNumber, 1, "pathFile", "source")

        verify {
            observerImageUpload.onChanged(any())
        }

        assert(viewmodel.imageUpload.value is Success)

        val result = viewmodel.imageUpload.value as Success
        assert(result.data.data.pictureObject.isNotEmpty())
    }

    @Test
    fun `Image Upload - Failed upload image`() {
        every {
            imageUploadUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewmodel.uploadImage(email, phoneNumber, 1, "pathFile", "source")

        verify {
            observerImageUpload.onChanged(any())
        }

        assert(viewmodel.imageUpload.value is Fail)

        val result = viewmodel.imageUpload.value as Fail
        assertEquals(result.throwable, mockThrowable)
    }

    /** Submit Data New Phone */
    @Test
    fun `Submit Data - Success submit request`() {
        val mockResponse = InactivePhoneSubmitDataModel(InactivePhoneSubmitDataModel.SubmitInactivePhoneUser(isSuccess = true))

        every {
            submitDataUseCase.execute(any(), any())
        } answers {
            firstArg<(InactivePhoneSubmitDataModel) -> Unit>().invoke(mockResponse)
        }

        viewmodel.submitForm("email", phoneNumber, phoneNumber, 1, "idCardObj", "selfieObj")

        verify {
            observerSubmitData.onChanged(any())
        }

        assert(viewmodel.submitData.value is Success)

        val result = viewmodel.submitData.value as Success
        assert(result.data.status.isSuccess)
    }

    @Test
    fun `Submit Data - Failed submit request`() {
        every {
            submitDataUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewmodel.submitForm("email", phoneNumber, phoneNumber, 1, "idCardObj", "selfieObj")

        verify {
            observerSubmitData.onChanged(any())
        }

        assert(viewmodel.submitData.value is Fail)

        val result = viewmodel.submitData.value as Fail
        assertEquals(result.throwable, mockThrowable)
    }

    @Test
    fun `Submit Expedited Data - Success`() {
        val mockResponse = SubmitExpeditedInactivePhoneDataModel(
            errorMessage = "",
            isSuccess = true
        )

        coEvery {
            submitExpeditedInactivePhoneUseCase(mapOf())
        } returns mockResponse

        viewmodel.submitNewPhoneNumber(phoneNumber, email)

        verify {
            observerSubmitExpeditedData.onChanged(any())
        }

        assert(viewmodel.submitDataExpedited.value is Success)

        val result = viewmodel.submitDataExpedited.value as Success
        assert(result.data.isSuccess)
    }

    @Test
    fun `Submit Expedited Data - Failed`() {
        val mockResponse = SubmitExpeditedInactivePhoneDataModel(
            errorMessage = "",
            isSuccess = false
        )

        coEvery {
            submitExpeditedInactivePhoneUseCase(mapOf())
        } returns mockResponse

        viewmodel.submitNewPhoneNumber(phoneNumber, email)

        verify {
            observerSubmitExpeditedData.onChanged(any())
        }

        assert(viewmodel.submitDataExpedited.value is Success)

        val result = viewmodel.submitDataExpedited.value as Success
        assert(!result.data.isSuccess)

    }
}