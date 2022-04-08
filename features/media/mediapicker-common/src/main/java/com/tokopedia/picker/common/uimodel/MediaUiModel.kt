package com.tokopedia.picker.common.uimodel

import android.content.Context
import android.net.Uri
import android.os.Parcelable
import com.tokopedia.picker.common.utils.extractVideoDuration
import com.tokopedia.picker.common.utils.isMaxImageRes
import com.tokopedia.picker.common.utils.isMinImageRes
import com.tokopedia.picker.common.utils.isVideoFormat
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
open class MediaUiModel(
    val id: Long = 0L,
    val name: String = "",
    val path: String = "",
    val uri: Uri? = null,

    /*
    * this data come from camera tab,
    * the media file is deletable.
    * */
    var isFromPickerCamera: Boolean = false,
) : Parcelable {

    fun isVideo() = isVideoFormat(path)

    fun videoDuration(context: Context): Long {
        return extractVideoDuration(context, path) ?: 0
    }

    fun isMaxImageRes(value: Int): Boolean {
        return isMaxImageRes(path, value)
    }

    fun isMinImageRes(value: Int): Boolean {
        return isMinImageRes(path, value)
    }

    fun isMoreThan(maxSizeInBytes: Long): Boolean {
        val file = File(path)

        if (!file.exists()) return false
        return file.length() > maxSizeInBytes
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
        fun File.toUiModel() = MediaUiModel(
            id = System.currentTimeMillis(),
            name = name,
            path = path,
            uri = Uri.parse(path)
        )

        fun File.cameraToUiModel() = toUiModel().also {
            it.isFromPickerCamera = true
        }
    }

}