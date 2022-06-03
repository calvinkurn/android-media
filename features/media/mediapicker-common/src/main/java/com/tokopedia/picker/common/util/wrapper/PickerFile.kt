package com.tokopedia.picker.common.util.wrapper

import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.text.TextUtils
import android.webkit.MimeTypeMap
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import java.io.File
import java.net.URLConnection
import android.media.MediaMetadataRetriever.METADATA_KEY_DURATION
import com.tokopedia.picker.common.util.DEFAULT_DURATION_LABEL

class PickerFile constructor(
    filePath: String
) : File(filePath) {

    fun safeDelete() {
        if (exists()) delete()
    }

    fun isGif(): Boolean {
        return extension().equals("gif", ignoreCase = true)
    }

    fun isImage(): Boolean {
        return getFileFormatByMime("image")
    }

    fun isVideo(): Boolean {
        return getFileFormatByMime("video")
    }

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
        return videoDuration(context).videoDurationFormat()
    }

    fun videoDuration(context: Context?): Long {
        val uri = Uri.fromFile(this)

        return try {
            with(MediaMetadataRetriever()) {
                setDataSource(context, uri)
                val durationData = extractMetadata(METADATA_KEY_DURATION)
                release()

                durationData.toLongOrZero()
            }
        } catch (e: Throwable) {
            0L
        }
    }

    private fun Long.videoDurationFormat(): String {
        val duration = this?: 0L

        if (duration == 0L) return DEFAULT_DURATION_LABEL

        val second = duration / 1000 % 60
        val minute = duration / (1000 * 60) % 60
        val hour = duration / (1000 * 60 * 60) % 24

        return if (hour > 0) {
            String.format("%02d:%02d:%02d", hour, minute, second)
        } else {
            String.format("%02d:%02d", minute, second)
        }
    }

    private fun getFileFormatByMime(prefix: String): Boolean {
        val mimeType =
            if (TextUtils.isEmpty(extension())) {
                URLConnection.guessContentTypeFromName(path)
            } else {
                MimeTypeMap
                    .getSingleton()
                    .getMimeTypeFromExtension(extension())
            }

        return mimeType != null && mimeType.startsWith(prefix)
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
        fun File.asPickerFile(): PickerFile {
            return PickerFile(path)
        }
    }

}