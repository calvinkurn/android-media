package com.tokopedia.gopay_kyc.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.gopay_kyc.domain.data.KycStatusData
import com.tokopedia.gopay_kyc.domain.data.KycStatusResponse
import com.tokopedia.gopay_kyc.domain.usecase.CheckKycStatusUseCase
import com.tokopedia.gopay_kyc.domain.usecase.SaveCaptureImageUseCase
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before

import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GoPayKycViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val checkKycStatusUseCase = mockk<CheckKycStatusUseCase>(relaxed = true)
    private val saveCaptureImageUseCase = mockk<SaveCaptureImageUseCase>(relaxed = true)
    private val dispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: GoPayKycViewModel

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)

    @Before
    fun setUp() {
        viewModel = GoPayKycViewModel(checkKycStatusUseCase, saveCaptureImageUseCase, dispatcher)
    }

    @Test
    fun `Execute checkKycStatus load failure`() {
        coEvery { checkKycStatusUseCase.checkKycStatus(any(), any()) } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.checkKycStatus()
        /*verify {
            viewModel.isUpgradeLoading.postValue(true)
            viewModel.isUpgradeLoading.postValue(false)
        }*/
    }

    @Test
    fun `Execute checkKycStatus code failed`() {
        val data = KycStatusResponse("FAIL", KycStatusData(false, ""))
        coEvery { checkKycStatusUseCase.checkKycStatus(any(), any()) } coAnswers {
            firstArg<(KycStatusResponse) -> Unit>().invoke(data)
        }
        viewModel.checkKycStatus()
        /*verifyOrder {
            viewModel.isUpgradeLoading.postValue(true)
            viewModel.isUpgradeLoading.postValue(false)
        }*/
    }

    @Test
    fun `Execute checkKycStatus code SUCCESS`() {
        val data = KycStatusResponse("SUCCESS", KycStatusData(true, ""))
        coEvery { checkKycStatusUseCase.checkKycStatus(any(), any()) } coAnswers {
            firstArg<(KycStatusResponse) -> Unit>().invoke(data)
        }
        viewModel.checkKycStatus()
        assert(viewModel.kycEligibilityStatus.value?.isEligible == data.kycStatusData.isEligible)
    }

    @Test
    fun `Execute processAndSaveImage `() {
       /* mockkStatic(BitmapFactory::class)
        val data = ByteArray(2)
        val captureCallback = slot<(Bitmap) -> Unit>()
        every { BitmapFactory.decodeByteArray(any(), any(), any(), any()) } returns null

        every { CameraUtils.decodeBitmap(data, 10, 10, captureCallback) } answers {
            captureCallback.captured.invoke()
        }
        coEvery { saveCaptureImageUseCase.parseAndSaveCapture(any(), any(), any(), any()) } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.processAndSaveImage(data, 10, 10, 1)
        assert(viewModel.captureErrorLiveData.value is Throwable)*/
    }

}