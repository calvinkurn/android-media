package com.tokopedia.media.picker.ui.uimodel

import android.Manifest.permission.*
import com.tokopedia.media.R
import com.tokopedia.picker.common.types.ModeType
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

        private fun collectModeTypePermission(
            @ModeType mode: Int
        ): List<PermissionUiModel> {
            return when (mode) {
                ModeType.IMAGE_ONLY -> listOf(camera())

                // it occurs for ModeType.COMMON and ModeType.VIDEO_ONLY
                else -> listOf(camera(), microphone())
            }
        }

        fun getOrCreate(
            @PageType page: Int,
            @ModeType mode: Int
        ): List<PermissionUiModel> {
            return when (page) {
                PageType.GALLERY -> listOf(storage())
                PageType.CAMERA -> collectModeTypePermission(mode)
                else -> {
                    collectModeTypePermission(mode)
                        .toMutableList()
                        .also {
                            // in PageType.COMMON, we also need to add `storage()` permission
                            // to show the gallery media items
                            it.add(storage())
                        }
                }
            }
        }
    }

}