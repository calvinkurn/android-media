package com.tokopedia.media.picker.ui.activity.main

import com.tokopedia.media.common.uimodel.MediaUiModel

interface PickerActivityListener {
    fun tabVisibility(isShown: Boolean)

    fun mediaSelected(): List<MediaUiModel> // get selected items
    fun hasVideoLimitReached(): Boolean // it the video has reached a limited of media
    fun hasMediaLimitReached(): Boolean // it has reached a limited of media
    fun isMinVideoDuration(model: MediaUiModel): Boolean // is the video under min limit duration
    fun isMaxVideoDuration(model: MediaUiModel): Boolean // is the video above max limit duration
    fun isMaxVideoSize(model: MediaUiModel): Boolean // check the max video size
    fun isMinImageResolution(model: MediaUiModel): Boolean // is the image under min image resolution
    fun isMaxImageResolution(model: MediaUiModel): Boolean // is the image above max image resolution
    fun isMaxImageSize(model: MediaUiModel): Boolean // check the max image size

    fun onShowMediaLimitReachedToast()
    fun onShowVideoLimitReachedToast()
    fun onShowVideoMinDurationToast()
    fun onShowVideoMaxDurationToast()
    fun onShowVideoMaxFileSizeToast()
    fun onShowImageMinResToast()
    fun onShowImageMaxResToast()
    fun onShowImageMaxFileSizeToast()
}