package com.tokopedia.picker.common.utils.wrapper

import android.graphics.BitmapFactory
import com.tokopedia.picker.common.utils.fileExtension
import com.tokopedia.picker.common.utils.isImageFormat
import com.tokopedia.picker.common.utils.isVideoFormat
import java.io.File

class PickerFile constructor(
    filePath: String
) : File(filePath) {

    fun safeDelete() {
        if (exists()) delete()
    }

    fun isGif() = fileExtension(path).equals(
        GIF_EXT,
        ignoreCase = true
    )

    fun isImage() = isImageFormat(path)

    fun isVideo() = isVideoFormat(path)

    fun isSizeMoreThan(sizeInBytes: Long): Boolean {
        if (!exists()) return false

        return length() > sizeInBytes
    }

    fun isMaxImageRes(value: Int): Boolean {
        val bitmapOptions = getBitmapOptions()

        val width = bitmapOptions.outWidth
        val height = bitmapOptions.outHeight

        return width > value || height > value
    }

    fun isMinImageRes(value: Int): Boolean {
        val bitmapOptions = getBitmapOptions()

        val width = bitmapOptions.outWidth
        val height = bitmapOptions.outHeight

        return width < value || height < value
    }

    private fun getBitmapOptions(): BitmapFactory.Options {
        val bitmapOptions = BitmapFactory.Options()
        bitmapOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, bitmapOptions)
        return bitmapOptions
    }

    companion object {
        private const val GIF_EXT = "gif"

        fun File.asPickerFile(): PickerFile {
            return PickerFile(path)
        }

        fun String.asPickerFile(): PickerFile {
            return PickerFile(this)
        }
    }

}
