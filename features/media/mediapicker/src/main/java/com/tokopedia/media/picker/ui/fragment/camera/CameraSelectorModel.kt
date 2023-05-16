package com.tokopedia.media.picker.ui.fragment.camera

import androidx.annotation.StringRes
import com.tokopedia.media.R

data class CameraSelectorModel(
    @StringRes var name: Int = 0,
    var isSelected: Boolean = false
) {
    companion object {
        fun create() = mutableListOf(
            CameraSelectorModel(R.string.picker_camera_picture_mode, true),
            CameraSelectorModel(R.string.picker_camera_video_mode, false),
        )
    }
}
