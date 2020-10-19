package com.tokopedia.kyc_centralized

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.kyc_centralized.domain.GetUserProjectInfoUseCase
import com.tokopedia.kyc_centralized.util.TestDispatcherProvider
import com.tokopedia.kyc_centralized.view.viewmodel.UserIdentificationViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.user_identification_common.domain.pojo.CheckKtpStatusPojo
import com.tokopedia.user_identification_common.domain.pojo.KycUserProjectInfoPojo
import com.tokopedia.user_identification_common.domain.usecase.GetStatusKtpUseCase
import com.tokopedia.user_identification_common.domain.usecase.RegisterKycUseCase
import com.tokopedia.user_identification_common.domain.usecase.UploadUserIdentificationUseCase
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user_identification_common.domain.pojo.RegisterIdentificationPojo
import com.tokopedia.user_identification_common.domain.pojo.UploadIdentificationPojo
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import util.isEqualsTo
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class UserIdentificationViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getUserProjectInfoUseCase: GetUserProjectInfoUseCase = mockk(relaxed = true)
    private val getStatusKtpUseCase: GetStatusKtpUseCase = mockk(relaxed = true)
    private val registerKycUseCase: RegisterKycUseCase = mockk(relaxed = true)
    private val uploadUserIdentificationUseCase: UploadUserIdentificationUseCase = mockk(relaxed = true)

    private val userProjectInfoObservable: Observer<Result<KycUserProjectInfoPojo>> = mockk(relaxed = true)
    private val ktpStatusObservable: Observer<Result<CheckKtpStatusPojo>> = mockk(relaxed = true)
    private val registerKycObservable: Observer<Result<RegisterIdentificationPojo>> = mockk(relaxed = true)

    private lateinit var viewModel: UserIdentificationViewModel

    private val dispatcher = TestDispatcherProvider()

    @Before fun setup() {
        viewModel = UserIdentificationViewModel(
                getUserProjectInfoUseCase,
                getStatusKtpUseCase,
                registerKycUseCase,
                uploadUserIdentificationUseCase,
                dispatcher
        )

        viewModel.userProjectInfo.observeForever(userProjectInfoObservable)
        viewModel.ktpStatus.observeForever(ktpStatusObservable)
        viewModel.registerKyc.observeForever(registerKycObservable)
    }

    @Test
    fun `it should get the user project info`() = runBlockingTest {
        val expectedReturn = KycUserProjectInfoPojo()
        val expectedValue = Success(expectedReturn)
        val testProjectId = 0

        coEvery {
            getUserProjectInfoUseCase.executeOnBackground()
        } returns expectedReturn

        viewModel.getUserProjectInfo(testProjectId)

        verify { userProjectInfoObservable.onChanged(expectedValue) }
        viewModel.userProjectInfo isEqualsTo expectedValue
    }

    @Test
    fun `it should throw error user project info`() = runBlockingTest {
        val expectedReturn = Throwable("Oops")
        val expectedValue = Fail(expectedReturn)
        val testProjectId = 0

        coEvery {
            getUserProjectInfoUseCase.executeOnBackground()
        } throws expectedReturn

        viewModel.getUserProjectInfo(testProjectId)

        verify { userProjectInfoObservable.onChanged(expectedValue) }
        viewModel.userProjectInfo isEqualsTo expectedValue
    }

    @Test
    fun `it should get the status ktp`() = runBlockingTest {
        val expectedReturn = CheckKtpStatusPojo()
        val expectedValue = Success(expectedReturn)
        val testImage = "Image"

        coEvery {
            getStatusKtpUseCase.executeOnBackground()
        } returns expectedReturn

        viewModel.getStatusKtp(testImage)

        verify { ktpStatusObservable.onChanged(expectedValue) }
        viewModel.ktpStatus isEqualsTo expectedValue
    }

    @Test
    fun `it should throw status ktp`() = runBlockingTest {
        val expectedReturn = Throwable("Oops")
        val expectedValue = Fail(expectedReturn)
        val testImage = "Image"

        coEvery {
            getStatusKtpUseCase.executeOnBackground()
        } throws expectedReturn

        viewModel.getStatusKtp(testImage)

        verify { ktpStatusObservable.onChanged(expectedValue) }
        viewModel.ktpStatus isEqualsTo expectedValue
    }

    @Test
    fun `it should successfully upload userIdentification`() = runBlockingTest {
        val expectedReturn = UploadIdentificationPojo().apply {
            this.kycUpload.isSuccess = 1
        }

        val testKycType = 0
        val testPicObjKyc = "Image Object"
        val testProjectId = 1

        coEvery {
            uploadUserIdentificationUseCase.executeOnBackground()
        } returns expectedReturn

        //Upload User Identification KTP
        viewModel.uploadUserIdentification(testKycType, testPicObjKyc, testProjectId)
        assertEquals(expectedReturn.kycUpload.isSuccess, 1)

        //Upload User Identification Selfie
        viewModel.uploadUserIdentification(testKycType, testPicObjKyc, testProjectId)
        assertEquals(expectedReturn.kycUpload.isSuccess, 1)
    }

    @Test
    fun `it failed to upload userIdentification`() = runBlockingTest {
        val expectedReturn = UploadIdentificationPojo().apply {
            this.kycUpload.isSuccess = 0
        }

        val testKycType = 0
        val testPicObjKyc = "Image Object"
        val testProjectId = 1

        coEvery {
            uploadUserIdentificationUseCase.executeOnBackground()
        } returns expectedReturn

        //Upload User Identification KTP
        viewModel.uploadUserIdentification(testKycType, testPicObjKyc, testProjectId)
        assertEquals(expectedReturn.kycUpload.isSuccess, 0)

        //Upload User Identification Selfie
        viewModel.uploadUserIdentification(testKycType, testPicObjKyc, testProjectId)
        assertEquals(expectedReturn.kycUpload.isSuccess, 0)
    }

    @Test
    fun `it should throw upload userIdentification`() = runBlockingTest {
        val testKycType = 0
        val testPicObjKyc = "Image Object"
        val testProjectId = 1

        val expectedReturn = Throwable("Oops")

        coEvery {
            uploadUserIdentificationUseCase.executeOnBackground()
        } throws expectedReturn

        //Upload User Identification KTP
        viewModel.uploadUserIdentification(testKycType, testPicObjKyc, testProjectId)

        //Upload User Identification Selfie
        viewModel.uploadUserIdentification(testKycType, testPicObjKyc, testProjectId)

        assertTrue {
            viewModel.registerKyc.value is Fail
        }
    }

    @Test
    fun `it should successfully register kyc`() = runBlockingTest {
        val expectedReturn = RegisterIdentificationPojo()
        val expectedValue = Success(expectedReturn)
        val testProjectId = 0

        coEvery {
            registerKycUseCase.executeOnBackground()
        } returns expectedReturn

        viewModel.registerKyc(testProjectId)

        verify { registerKycObservable.onChanged(expectedValue) }
        viewModel.registerKyc isEqualsTo expectedValue
    }

    @Test
    fun `it should throw error register kyc`() = runBlockingTest {
        val expectedReturn = Throwable("Oops")
        val testProjectId = 0

        coEvery {
            registerKycUseCase.executeOnBackground()
        } throws expectedReturn

        viewModel.registerKyc(testProjectId)

        assertTrue {
            viewModel.registerKyc.value is Fail
        }
    }

    @Test
    fun `it should cancel all jobs`() = runBlockingTest {
        viewModel.onCleared()
        verify {
            getUserProjectInfoUseCase.cancelJobs()
            getStatusKtpUseCase.cancelJobs()
            registerKycUseCase.cancelJobs()
        }
    }
}