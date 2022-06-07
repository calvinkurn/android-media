package com.tokopedia.picker.common.util.wrapper

import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaMetadataRetriever.METADATA_KEY_DURATION
import android.net.Uri
import android.webkit.MimeTypeMap
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.picker.common.mapper.humanize
import com.tokopedia.picker.common.util.getFileFormatByMimeType
import java.io.File

class PickerFile constructor(
    filePath: String
) : File(filePath) {

    fun safeDelete() {
        if (exists()) delete()
    }

    fun isGif() = extension().equals(
        GIF_EXT,
        ignoreCase = true
    )

    fun isImage() = getFileFormatByMimeType(
        type = MIME_TYPE_IMAGE,
        path = path,
        extension = extension()
    )

    fun isVideo() = getFileFormatByMimeType(
        type = MIME_TYPE_VIDEO,
        path = path,
        extension = extension()
    )

    fun isSizeMoreThan(sizeInBytes: Long): Boolean {
        if (!exists()) return false

        return length() > sizeInBytes
    }

    fun isMaxImageRes(value: Int): Boolean {
        val bitmapOptions = getBitmapOptions()

        val width = bitmapOptions.outWidth
        val height = bitmapOptions.outHeight

        return width > value && height > value
    }

    fun isMinImageRes(value: Int): Boolean {
        val bitmapOptions = getBitmapOptions()

        val width = bitmapOptions.outWidth
        val height = bitmapOptions.outHeight

        return width < value && height < value
    }

    fun readableVideoDuration(context: Context?): String {
        return videoDuration(context).humanize()
    }

    fun videoDuration(context: Context?): Int {
        val uri = Uri.fromFile(this)

        return try {
            with(MediaMetadataRetriever()) {
                setDataSource(context, uri)
                val durationData = extractMetadata(METADATA_KEY_DURATION)
                release()

                durationData.toIntOrZero()
            }
        } catch (e: Throwable) {
            0
        }
    }

    /*
    * Get the bitmap detail from image.
    * */
    private fun getBitmapOptions(): BitmapFactory.Options {
        val bitmapOptions = BitmapFactory.Options()
        bitmapOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, bitmapOptions)
        return bitmapOptions
    }

    /*
    * A custom method for getting the file extension.
    *
    * @example:
    * File -> /data/DCIM/Pictures/isfa.jpg
    *
    * @return:
    * jpg
    * */
    private fun extension(): String {
        val extension = MimeTypeMap.getFileExtensionFromUrl(path)
        if (!extension.isNullOrEmpty()) return extension

        return if (path.contains(".")) {
            path.substring(path.lastIndexOf(".") + 1, path.length)
        } else {
            ""
        }
    }

    companion object {
        private const val MIME_TYPE_IMAGE = "image"
        private const val MIME_TYPE_VIDEO = "video"

        private const val GIF_EXT = "gif"

        fun File.asPickerFile(): PickerFile {
            return PickerFile(path)
        }

        fun String.asPickerFile(): PickerFile {
            return PickerFile(this)
        }
    }

}