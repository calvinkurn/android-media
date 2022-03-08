package com.tokopedia.media.common.uimodel

import android.content.Context
import android.net.Uri
import android.os.Parcelable
import com.tokopedia.media.common.utils.extractVideoDuration
import com.tokopedia.media.common.utils.getBitmapOptions
import com.tokopedia.media.common.utils.isVideoFormat
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

    fun getVideoDuration(context: Context): Long {
        return extractVideoDuration(context, path) ?: 0
    }

    fun isMaxImageRes(value: Int): Boolean {
        val bitmapOptions = getBitmapOptions(path)
        val width = bitmapOptions.outWidth
        val height = bitmapOptions.outHeight
        return width > value && height > value
    }

    fun isMinImageRes(value: Int): Boolean {
        val bitmapOptions = getBitmapOptions(path)
        val width = bitmapOptions.outWidth
        val height = bitmapOptions.outHeight
        return width < value && height < value
    }

    fun isMaxFileSize(maxSizeInBytes: Long): Boolean {
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