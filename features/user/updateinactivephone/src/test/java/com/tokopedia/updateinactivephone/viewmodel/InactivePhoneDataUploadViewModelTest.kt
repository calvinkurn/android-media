package com.tokopedia.updateinactivephone.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.updateinactivephone.revamp.domain.data.ImageUploadDataModel
import com.tokopedia.updateinactivephone.revamp.domain.data.InactivePhoneSubmitDataModel
import com.tokopedia.updateinactivephone.revamp.domain.data.PhoneValidationDataModel
import com.tokopedia.updateinactivephone.revamp.domain.data.UploadHostDataModel
import com.tokopedia.updateinactivephone.revamp.domain.usecase.GetUploadHostUseCase
import com.tokopedia.updateinactivephone.revamp.domain.usecase.ImageUploadUseCase
import com.tokopedia.updateinactivephone.revamp.domain.usecase.PhoneValidationUseCase
import com.tokopedia.updateinactivephone.revamp.domain.usecase.SubmitDataUseCase
import com.tokopedia.updateinactivephone.revamp.view.viewmodel.InactivePhoneDataUploadViewModel
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.every
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class InactivePhoneDataUploadViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val dispatcher = TestCoroutineDispatcher()
    val phoneValidationUseCase = mockk<PhoneValidationUseCase>(relaxed = true)
    val imageUploadUseCase = mockk<ImageUploadUseCase>(relaxed = true)
    val getUploadHostUseCase = mockk<GetUploadHostUseCase>(relaxed = true)
    val submitDataUseCase = mockk<SubmitDataUseCase>(relaxed = true)

    val observerPhoneValidation = mockk<Observer<Result<PhoneValidationDataModel>>>(relaxed = true)
    val observerImageUpload = mockk<Observer<Result<ImageUploadDataModel>>>(relaxed = true)
    val observerGetUploadHost = mockk<Observer<Result<UploadHostDataModel>>>(relaxed = true)
    val observerSubmitData = mockk<Observer<Result<InactivePhoneSubmitDataModel>>>(relaxed = true)

    lateinit var viewmodel: InactivePhoneDataUploadViewModel

    val phoneNumber = "62800000000000"
    val mockThrowable = Throwable("Opss!")

    @Before
    fun setup() {
        viewmodel = InactivePhoneDataUploadViewModel(
                phoneValidationUseCase,
                imageUploadUseCase,
                getUploadHostUseCase,
                submitDataUseCase,
                dispatcher
        )

        viewmodel.phoneValidation.observeForever(observerPhoneValidation)
        viewmodel.imageUpload.observeForever(observerImageUpload)
        viewmodel.uploadHost.observeForever(observerGetUploadHost)
        viewmodel.submitData.observeForever(observerSubmitData)
    }

    @After
    fun tearDown() {
        viewmodel.onCleared()

        viewmodel.phoneValidation.removeObserver(observerPhoneValidation)
        viewmodel.imageUpload.removeObserver(observerImageUpload)
        viewmodel.uploadHost.removeObserver(observerGetUploadHost)
        viewmodel.submitData.removeObserver(observerSubmitData)
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

        viewmodel.userValidation(phoneNumber, 1)

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

        viewmodel.userValidation(phoneNumber, 1)

        every {
            observerPhoneValidation.onChanged(any())
        }

        assert(viewmodel.phoneValidation.value is Fail)

        val result = viewmodel.phoneValidation.value as Fail
        assertEquals(result.throwable, mockThrowable)
    }

    /** Get Upload Host */
    @Test
    fun `get host - Success`() {
        val host = "http://sample.com"
        val mockResponse = UploadHostDataModel(UploadHostDataModel.UploadHostData(
                UploadHostDataModel.GeneratedHost(uploadHost = host))
        )

        every {
            getUploadHostUseCase.execute(any(), any())
        } answers {
            firstArg<(UploadHostDataModel) -> Unit>().invoke(mockResponse)
        }

        viewmodel.getUploadHost()

        verify {
            observerGetUploadHost.onChanged(any())
        }

        assert(viewmodel.uploadHost.value is Success)

        val result = viewmodel.uploadHost.value as Success
        assert(result.data.data.generatedHost.uploadHost.isNotEmpty())
    }

    @Test
    fun `get host - Fail`() {
        every {
            getUploadHostUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewmodel.getUploadHost()

        verify {
            observerGetUploadHost.onChanged(any())
        }

        assert(viewmodel.uploadHost.value is Fail)

        val result = viewmodel.uploadHost.value as Fail
        assertEquals(result.throwable, mockThrowable)
    }

    /** Upload Image */
    @Test
    fun `Image Upload - Success upload image`() {
        val mockResponse = ImageUploadDataModel(picObj = "data sample")

        every {
            imageUploadUseCase.execute(any(), any())
        } answers {
            firstArg<(ImageUploadDataModel) -> Unit>().invoke(mockResponse)
        }

        viewmodel.uploadImage("url", "userId", "path", "source")

        verify {
            observerImageUpload.onChanged(any())
        }

        assert(viewmodel.imageUpload.value is Success)

        val result = viewmodel.imageUpload.value as Success
        assert(result.data.picObj.isNotEmpty())
    }

    @Test
    fun `Image Upload - Failed upload image`() {
        every {
            imageUploadUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewmodel.uploadImage("url", "userId", "path", "source")

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

        viewmodel.submitForm("email", "newPhone", 1, "idCardObj", "selfieObj")

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

        viewmodel.submitForm("email", "newPhone", 1, "idCardObj", "selfieObj")

        verify {
            observerSubmitData.onChanged(any())
        }

        assert(viewmodel.submitData.value is Fail)

        val result = viewmodel.submitData.value as Fail
        assertEquals(result.throwable, mockThrowable)
    }
}