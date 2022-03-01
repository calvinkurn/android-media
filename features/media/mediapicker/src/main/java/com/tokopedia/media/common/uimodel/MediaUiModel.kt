package com.tokopedia.media.common.uimodel

import android.content.Context
import android.net.Uri
import android.os.Parcelable
import com.tokopedia.media.picker.utils.files.extractVideoDuration
import com.tokopedia.media.picker.utils.files.isVideoFormat
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
open class MediaUiModel(
    val id: Long = 0L,
    val name: String = "",
    val path: String = "",
    val uri: Uri? = null,
    val isFromPickerCamera: Boolean = false,
) : Parcelable {

    fun isVideo() = isVideoFormat(path)

    fun isVideoDurationValid(context: Context): Boolean {
        if (!isVideo()) return false

        val extractDuration = extractVideoDuration(context, path) ?: 0
        return extractDuration >= VIDEO_DURATION_MINIMUM
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return other is MediaUiModel &&
                id == other.id &&
                name == other.name &&
                path == other.path &&
                uri == other.uri &&
                isFromPickerCamera == other.isFromPickerCamera
    }

    override fun hashCode(): Int {
        var hashCode = id.hashCode()
        hashCode = 5 * hashCode + name.hashCode()
        hashCode = 5 * hashCode + path.hashCode()
        hashCode = 5 * hashCode + uri.hashCode()
        hashCode = 5 * hashCode + isFromPickerCamera.hashCode()
        return hashCode
    }

    companion object {
        const val VIDEO_DURATION_MINIMUM = 3000

        fun File.cameraToUiModel() = MediaUiModel(
            id = System.currentTimeMillis(),
            name = name,
            path = path,
            uri = Uri.parse(path),
            isFromPickerCamera = true,
        )
    }

}