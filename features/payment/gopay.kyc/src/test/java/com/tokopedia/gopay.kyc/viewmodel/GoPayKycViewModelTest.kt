package com.tokopedia.gopay.kyc.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.gopay.kyc.domain.data.CameraImageResult
import com.tokopedia.gopay.kyc.domain.data.KycStatusData
import com.tokopedia.gopay.kyc.domain.data.KycStatusResponse
import com.tokopedia.gopay.kyc.domain.usecase.CheckKycStatusUseCase
import com.tokopedia.gopay.kyc.domain.usecase.SaveCaptureImageUseCase
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
    private val loadingObserver: Observer<Boolean> = mockk(relaxed = true)

    private val dispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: GoPayKycViewModel

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)

    @Before
    fun setUp() {
        viewModel = GoPayKycViewModel(checkKycStatusUseCase, saveCaptureImageUseCase, dispatcher)
        viewModel.isUpgradeLoading.observeForever(loadingObserver)
    }

    @Test
    fun `Execute checkKycStatus load failure`() {
        coEvery { checkKycStatusUseCase.checkKycStatus(any(), any()) } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.checkKycStatus()
        verifyOrder {
            loadingObserver.onChanged(true)
            loadingObserver.onChanged(false)
        }
        assert(viewModel.errorLiveData.value is Throwable)
    }

    @Test
    fun `Execute checkKycStatus code failed`() {
        val data = KycStatusResponse("FAIL", KycStatusData(false, ""))
        coEvery { checkKycStatusUseCase.checkKycStatus(any(), any()) } coAnswers {
            firstArg<(KycStatusResponse) -> Unit>().invoke(data)
        }
        viewModel.checkKycStatus()
        verifyOrder {
            loadingObserver.onChanged(true)
            loadingObserver.onChanged(false)
        }
        assert(viewModel.errorLiveData.value is Throwable)
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
    fun `Execute processAndSaveImage error Exception`() {
        coEvery { saveCaptureImageUseCase.parseAndSaveCapture(any(), any(), any()) } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.processAndSaveImage(ByteArray(1))
        assert(viewModel.captureErrorLiveData.value is Throwable)
    }

    @Test
    fun `Execute processAndSaveImage bytearray null`() {
        viewModel.processAndSaveImage(null)
        assert((viewModel.captureErrorLiveData.value as Throwable).message == "Empty Data byte")
    }

    @Test
    fun `Execute processAndSaveImage code Success`() {
        val cameraImageResult = CameraImageResult(1080,1080, "/path", listOf())
        coEvery { saveCaptureImageUseCase.parseAndSaveCapture(any(), any(), any()) } coAnswers {
            firstArg<(CameraImageResult) -> Unit>().invoke(cameraImageResult)
        }
        viewModel.processAndSaveImage(ByteArray(1))
        assert((viewModel.cameraImageResultLiveData.value as CameraImageResult).bitmapHeight == 1080)
        assert((viewModel.cameraImageResultLiveData.value as CameraImageResult).bitmapWidth == 1080)
        assert((viewModel.cameraImageResultLiveData.value as CameraImageResult).finalCameraResultPath == "/path")

    }

}