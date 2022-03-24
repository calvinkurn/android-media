package com.tokopedia.media.picker.ui.activity.main

import com.tokopedia.media.common.uimodel.MediaUiModel

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

    fun onShowMediaLimitReachedToast()
    fun onShowVideoLimitReachedToast()
    fun onShowVideoMinDurationToast()
    fun onShowVideoMaxDurationToast()
    fun onShowVideoMaxFileSizeToast()
    fun onShowImageMinResToast()
    fun onShowImageMaxResToast()
    fun onShowImageMaxFileSizeToast()
    fun navigateToCameraPage()
}