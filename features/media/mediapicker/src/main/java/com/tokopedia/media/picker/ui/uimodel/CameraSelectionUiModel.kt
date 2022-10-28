package com.tokopedia.media.picker.ui.uimodel

import androidx.annotation.StringRes
import com.tokopedia.media.R

data class CameraSelectionUiModel(
    @StringRes var name: Int = 0,
    var isSelected: Boolean = false
) {
    companion object {
        fun create() = mutableListOf(
            CameraSelectionUiModel(R.string.picker_camera_picture_mode, true),
            CameraSelectionUiModel(R.string.picker_camera_video_mode, false),
        )
    }
}