package com.tokopedia.gopay_kyc.viewmodel

import android.graphics.Bitmap
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.otaliastudios.cameraview.BitmapCallback
import com.otaliastudios.cameraview.CameraUtils
import com.tokopedia.gopay_kyc.domain.data.KycStatusData
import com.tokopedia.gopay_kyc.domain.data.KycStatusResponse
import com.tokopedia.gopay_kyc.domain.usecase.CheckKycStatusUseCase
import com.tokopedia.gopay_kyc.domain.usecase.SaveCaptureImageUseCase
import com.tokopedia.usecase.coroutines.Result
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
/*
    @Test
    fun `Execute processAndSaveImage`() {
        mockkStatic(CameraUtils::class)
        val image = ByteArray(2)

        every { CameraUtils.decodeBitmap(image, 1, 1) {} } answers {
            (args[3] as BitmapCallback).onBitmapReady(null)
        }

        viewModel.processAndSaveImage(image, 1, 1, 1)

        verify {
            CameraUtils.decodeBitmap(image, 1,1) { }
        }
    }*/

}