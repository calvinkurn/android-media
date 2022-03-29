package com.tokopedia.media.picker.ui.activity.main

import com.tokopedia.picker.common.uimodel.MediaUiModel

interface PickerActivityListener {
    fun tabVisibility(isShown: Boolean)

    fun mediaSelected(): List<MediaUiModel>
    fun hasVideoLimitReached(): Boolean
    fun hasMediaLimitReached(): Boolean
    fun isMinVideoDuration(model: MediaUiModel): Boolean
    fun isMaxVideoDuration(model: MediaUiModel): Boolean
    fun isMaxVideoSize(model: MediaUiModel): Boolean
    fun isMinImageResolution(model: MediaUiModel): Boolean
    fun isMaxImageResolution(model: MediaUiModel): Boolean
    fun isMaxImageSize(model: MediaUiModel): Boolean
    fun isMinStorageThreshold(): Boolean

    fun onShowMediaLimitReachedGalleryToast()
    fun onShowVideoLimitReachedGalleryToast()

    fun onShowMediaLimitReachedCameraToast()
    fun onShowVideoLimitReachedCameraToast()

    fun onShowVideoMinDurationToast()
    fun onShowVideoMaxDurationToast()
    fun onShowVideoMaxFileSizeToast()
    fun onShowImageMinResToast()
    fun onShowImageMaxResToast()
    fun onShowImageMaxFileSizeToast()
    fun onShowMinStorageThresholdToast()

    fun onCameraThumbnailClicked()
    fun navigateToCameraPage()
}