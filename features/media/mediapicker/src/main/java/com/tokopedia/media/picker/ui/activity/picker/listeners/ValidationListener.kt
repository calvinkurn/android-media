package com.tokopedia.media.picker.ui.activity.picker.listeners

import com.tokopedia.picker.common.uimodel.MediaUiModel

interface ValidationListener {
    fun hasVideoLimitReached(): Boolean
    fun hasMediaLimitReached(): Boolean
    fun isMinVideoDuration(model: MediaUiModel): Boolean
    fun isMaxVideoDuration(model: MediaUiModel): Boolean
    fun isMaxVideoSize(model: MediaUiModel): Boolean
    fun isMinImageResolution(model: MediaUiModel): Boolean
    fun isMaxImageResolution(model: MediaUiModel): Boolean
    fun isMaxImageSize(model: MediaUiModel): Boolean
    fun isMinStorageThreshold(): Boolean
}