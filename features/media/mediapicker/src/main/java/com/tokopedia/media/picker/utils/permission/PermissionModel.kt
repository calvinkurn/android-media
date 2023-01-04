package com.tokopedia.media.picker.utils.permission

import android.Manifest
import android.content.Context
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.annotation.ChecksSdkIntAtLeast
import com.tokopedia.media.R
import com.tokopedia.picker.common.types.ModeType
import com.tokopedia.picker.common.types.PageType

data class PermissionModel(
    var title: Int,
    var name: String,
    var isGranted: Boolean = false
)

private fun camera() = PermissionModel(R.string.picker_permission_camera, Manifest.permission.CAMERA)
private fun microphone() = PermissionModel(R.string.picker_permission_microphone, Manifest.permission.RECORD_AUDIO)
private fun storage() = PermissionModel(R.string.picker_permission_gallery, Manifest.permission.READ_EXTERNAL_STORAGE)
private fun images() = PermissionModel(R.string.picker_permission_images, Manifest.permission.READ_MEDIA_IMAGES)
private fun videos() = PermissionModel(R.string.picker_permission_videos, Manifest.permission.READ_MEDIA_VIDEO)

@ChecksSdkIntAtLeast(api = VERSION_CODES.TIRAMISU)
private fun isAndroid13OrAbove() = VERSION.SDK_INT >= VERSION_CODES.TIRAMISU

private fun cameraPermissions(@ModeType mode: Int): List<PermissionModel> {
    return when (mode) {
        ModeType.IMAGE_ONLY -> listOf(camera())

        /*
        * ModeType.COMMON & ModeType.VIDEO_ONLY,
        * ModeType.COMMON means for both image & video
        * */
        else -> listOf(camera(), microphone())
    }
}

fun galleryPermissions(@ModeType mode: Int): List<PermissionModel> {
    return if (isAndroid13OrAbove()) {
        when (mode) {
            ModeType.IMAGE_ONLY -> listOf(images())
            ModeType.VIDEO_ONLY -> listOf(videos())
            else -> listOf(images(), videos())
        }
    } else {
        return listOf(storage())
    }
}

fun permissions(
    @PageType page: Int,
    @ModeType mode: Int
): List<PermissionModel> {
    return when (page) {
        // camera only
        PageType.CAMERA -> cameraPermissions(mode)

        // gallery only
        PageType.GALLERY -> galleryPermissions(mode)

        // both for camera & gallery page
        else -> {
            cameraPermissions(mode) + galleryPermissions(mode)
        }
    }
}

fun hasPermissionRequiredGranted(
    context: Context,
    @PageType page: Int,
    @ModeType mode: Int
): Boolean {
    return permissions(page, mode)
        .map {
            isGranted(context, it.name)
        }.all { it }
}
