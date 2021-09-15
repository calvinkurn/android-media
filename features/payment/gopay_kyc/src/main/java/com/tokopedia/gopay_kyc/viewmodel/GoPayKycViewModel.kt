package com.tokopedia.gopay_kyc.viewmodel

import androidx.lifecycle.MutableLiveData
import com.otaliastudios.cameraview.CameraUtils
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.gopay_kyc.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.gopay_kyc.domain.data.CameraImageResult
import com.tokopedia.gopay_kyc.domain.data.KycStatusResponse
import com.tokopedia.gopay_kyc.domain.usecase.CheckKycStatusUseCase
import com.tokopedia.gopay_kyc.domain.usecase.InitiateKycUseCase
import com.tokopedia.gopay_kyc.domain.usecase.SaveCaptureImageUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GoPayKycViewModel @Inject constructor(
    private val checkKycStatusUseCase: CheckKycStatusUseCase,
    private val saveCaptureImageUseCase: SaveCaptureImageUseCase,
    @CoroutineMainDispatcher val dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    var isCameraOpen = false
    var canGoBack = true
    var mCapturingPicture = false

    val cameraImageResultLiveData = MutableLiveData<CameraImageResult>()
    var kycEligibilityStatus = false
    fun getCapturedImagePath() = cameraImageResultLiveData.value?.finalCameraResultPath ?: ""

    fun processAndSaveImage(
        imageByte: ByteArray,
        captureWidth: Int,
        captureHeight: Int,
        ordinal: Int
    ) {
        try {
            CameraUtils.decodeBitmap(imageByte, captureWidth, captureHeight) { bitmap ->
                if (bitmap != null) {
                    saveCaptureImageUseCase.parseAndSaveCapture(
                        ::onSaveSuccess,
                        ::onSaveError,
                        imageByte, ordinal
                    )
                }

            }
        } catch (e: Throwable) {
            saveCaptureImageUseCase.parseAndSaveCapture(
                ::onSaveSuccess,
                ::onSaveError,
                imageByte, ordinal
            )
        }

    }

    private fun onSaveSuccess(cameraImageResult: CameraImageResult) {
        cameraImageResultLiveData.postValue(cameraImageResult)
    }

    private fun onSaveError(throwable: Throwable) {
        // do nothing
    }

    fun checkKycStatus() {
        checkKycStatusUseCase.cancelJobs()
        checkKycStatusUseCase.checkKycStatus(
            ::onKycStatusSuccess,
            ::onKycStatusFail
        )
    }

    private fun onKycStatusSuccess(kycStatusResponse: KycStatusResponse) {
        if (kycStatusResponse.code == CODE_SUCCESS)
            kycEligibilityStatus = kycStatusResponse.kycStatusData.isEligible
    }

    private fun onKycStatusFail(throwable: Throwable) {

    }

    override fun onCleared() {
        super.onCleared()

    }

    companion object {
        const val CODE_SUCCESS = "SUCCESS"
    }
}