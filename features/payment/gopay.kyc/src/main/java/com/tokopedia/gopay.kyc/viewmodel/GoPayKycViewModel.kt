package com.tokopedia.gopay.kyc.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.gopay.kyc.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.gopay.kyc.domain.data.CameraImageResult
import com.tokopedia.gopay.kyc.domain.data.KycStatusData
import com.tokopedia.gopay.kyc.domain.data.KycStatusResponse
import com.tokopedia.gopay.kyc.domain.usecase.CheckKycStatusUseCase
import com.tokopedia.gopay.kyc.domain.usecase.SaveCaptureImageUseCase
import kotlinx.coroutines.CoroutineDispatcher
import java.lang.IllegalStateException
import javax.inject.Inject

class GoPayKycViewModel @Inject constructor(
    private val checkKycStatusUseCase: CheckKycStatusUseCase,
    private val saveCaptureImageUseCase: SaveCaptureImageUseCase,
    @CoroutineMainDispatcher val dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    var isCameraOpen = false
    var mCapturingPicture = false
    var canGoBack = true // canGoBack is true for review screen and false for capture screen

    val cameraImageResultLiveData = MutableLiveData<CameraImageResult>()
    val captureErrorLiveData = MutableLiveData<Throwable>()
    val kycEligibilityStatus = MutableLiveData<KycStatusData>()
    val errorLiveData = MutableLiveData<Throwable>()
    val isUpgradeLoading = MutableLiveData<Boolean>()

    fun getCapturedImagePath() = cameraImageResultLiveData.value?.finalCameraResultPath ?: ""

    fun processAndSaveImage(imageByte: ByteArray?) {
        imageByte?.let {
            saveCaptureImageUseCase.parseAndSaveCapture(
                    ::onSaveSuccess,
                    ::onSaveError,
                    imageByte
            )
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
        else onKycStatusFail(IllegalStateException(ILLEGAL_STATE))
    }

    private fun onKycStatusFail(throwable: Throwable) {
        isUpgradeLoading.postValue(false)
        errorLiveData.postValue(throwable)
    }

    override fun onCleared() {
        super.onCleared()
        checkKycStatusUseCase.cancelJobs()
        saveCaptureImageUseCase.cancelJobs()
    }

    companion object {
        const val CODE_SUCCESS = "SUCCESS"
        const val ILLEGAL_STATE = "Silakan coba lagi"
    }
}