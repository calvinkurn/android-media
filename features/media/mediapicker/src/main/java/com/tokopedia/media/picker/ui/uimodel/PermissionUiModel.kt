package com.tokopedia.media.picker.ui.uimodel

import android.Manifest.permission.*
import com.tokopedia.media.R
import com.tokopedia.media.common.types.PickerPageType
import com.tokopedia.media.picker.ui.PickerUiConfig

data class PermissionUiModel(
    var title: Int,
    var name: String,
    var isGranted: Boolean = false
) {

    companion object {
        private fun storage() = PermissionUiModel(
            R.string.picker_permission_gallery,
            READ_EXTERNAL_STORAGE
        )

        private fun camera() = PermissionUiModel(
            R.string.picker_permission_camera,
            CAMERA
        )

        private fun microphone() = PermissionUiModel(
            R.string.picker_permission_microphone,
            RECORD_AUDIO
        )

        fun get(): List<PermissionUiModel> {
            return when (PickerUiConfig.pageType) {
                PickerPageType.CAMERA -> {
                    if (PickerUiConfig.isPhotoModeOnly()) {
                        listOf(camera())
                    } else {
                        listOf(camera(), microphone())
                    }
                }
                PickerPageType.GALLERY -> listOf(storage())
                else -> listOf(storage(), camera(), microphone())
            }
        }
    }

}