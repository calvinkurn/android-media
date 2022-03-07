package com.tokopedia.media.picker.ui.uimodel

import android.Manifest
import com.tokopedia.media.R

data class PermissionUiModel(
    var title: Int,
    var permission: String
) {

    companion object {
        fun storage() = PermissionUiModel(
            R.string.picker_permission_gallery,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        fun camera() = PermissionUiModel(
            R.string.picker_permission_camera,
            Manifest.permission.CAMERA
        )

        fun microphone() = PermissionUiModel(
            R.string.picker_permission_microphone,
            Manifest.permission.RECORD_AUDIO
        )
    }

}