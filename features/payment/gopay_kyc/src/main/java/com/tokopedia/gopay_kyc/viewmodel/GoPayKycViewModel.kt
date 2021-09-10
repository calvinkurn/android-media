package com.tokopedia.gopay_kyc.viewmodel

import androidx.lifecycle.MutableLiveData
import com.otaliastudios.cameraview.CameraUtils
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.gopay_kyc.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.gopay_kyc.domain.data.CameraImageResult
import com.tokopedia.gopay_kyc.domain.usecase.SaveCaptureImageUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GoPayKycViewModel @Inject constructor(
    private val saveCaptureImageUseCase: SaveCaptureImageUseCase,
    @CoroutineMainDispatcher val dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    val cameraImageResultLiveData = MutableLiveData<CameraImageResult>()
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

    }
}