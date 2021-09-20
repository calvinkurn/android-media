package com.tokopedia.gopay_kyc.viewmodel

import androidx.lifecycle.MutableLiveData
import com.otaliastudios.cameraview.CameraUtils
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.gopay_kyc.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.gopay_kyc.domain.data.CameraImageResult
import com.tokopedia.gopay_kyc.domain.data.KycStatusData
import com.tokopedia.gopay_kyc.domain.data.KycStatusResponse
import com.tokopedia.gopay_kyc.domain.usecase.CheckKycStatusUseCase
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
    val captureErrorLiveData = MutableLiveData<Throwable>()
    val kycEligibilityStatus = MutableLiveData<KycStatusData>()
    val isUpgradeLoading = MutableLiveData<Boolean>()

    fun getCapturedImagePath() = cameraImageResultLiveData.value?.finalCameraResultPath ?: ""

    fun processAndSaveImage(
        imageByte: ByteArray?,
        captureWidth: Int,
        captureHeight: Int,
        ordinal: Int
    ) {
        imageByte?.let {
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
        } ?: kotlin.run {  onSaveError(NullPointerException("Empty Data byte")) }

    }

    private fun onSaveSuccess(cameraImageResult: CameraImageResult) {
        cameraImageResultLiveData.postValue(cameraImageResult)
    }

    private fun onSaveError(throwable: Throwable) {
        captureErrorLiveData.postValue(throwable)
    }

    fun checkKycStatus() {
        isUpgradeLoading.postValue(true)
        checkKycStatusUseCase.cancelJobs()
        checkKycStatusUseCase.checkKycStatus(
            ::onKycStatusSuccess,
            ::onKycStatusFail
        )
    }

    private fun onKycStatusSuccess(kycStatusResponse: KycStatusResponse) {
        isUpgradeLoading.postValue(false)
        if (kycStatusResponse.code == CODE_SUCCESS)
            kycEligibilityStatus.postValue(kycStatusResponse.kycStatusData)
    }

    private fun onKycStatusFail(throwable: Throwable) {
        isUpgradeLoading.postValue(false)
    }

    override fun onCleared() {
        super.onCleared()
        checkKycStatusUseCase.cancelJobs()
        saveCaptureImageUseCase.cancelJobs()
    }

    companion object {
        const val CODE_SUCCESS = "SUCCESS"
    }
}