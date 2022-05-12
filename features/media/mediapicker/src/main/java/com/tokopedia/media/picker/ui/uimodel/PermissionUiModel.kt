package com.tokopedia.media.picker.ui.uimodel

import android.Manifest.permission.*
import com.tokopedia.media.R
import com.tokopedia.picker.common.types.PageType

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

        fun getOrCreate(
            @PageType page: Int,
            isVideoModeOnly: Boolean
        ): List<PermissionUiModel> {
            return when (page) {
                PageType.CAMERA -> {
                    if (isVideoModeOnly) {
                        listOf(camera(), microphone())
                    } else {
                        listOf(camera())
                    }
                }
                PageType.GALLERY -> listOf(storage())
                else -> listOf(camera(), microphone(), storage())
            }
        }
    }

}