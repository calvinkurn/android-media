package com.tokopedia.picker.ui.uimodel.internal

import androidx.annotation.StringRes
import com.tokopedia.picker.R

data class CameraSelectionMode(
    @StringRes var name: Int = 0,
    var isSelected: Boolean = false
) {
    companion object {
        fun create() = mutableListOf(
            CameraSelectionMode(R.string.picker_camera_picture_mode, false),
            CameraSelectionMode(R.string.picker_camera_video_mode, false),
        )
    }
}