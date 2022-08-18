package com.tokopedia.media.editor.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

fun getEditorSaveFolderDir(context: Context): String {
    return "${context.externalCacheDir?.path}/editor-cache/"
}

fun getDestinationUri(context: Context, filename: String? = null): Uri {
    val folderPath = getEditorSaveFolderDir(context)
    val dir = File(folderPath)
    if (!dir.exists()) dir.mkdir()

    return if (filename == null)
        Uri.fromFile(File("${folderPath}/${generateFileName()}.png"))
    else
        Uri.fromFile(File("${folderPath}/$filename.png"))
}

@SuppressLint("SimpleDateFormat")
fun generateFileName(): String {
    return SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
}

fun shouldNull(value: Float): Float? {
    return if (value != -1f) {
        value
    } else null
}

fun shouldNull(value: Int): Int? {
    return if (value != -1) {
        value
    } else null
}

//formula to determine brightness 0.299 * r + 0.0f + 0.587 * g + 0.0f + 0.114 * b + 0.0f
// if total of dark pixel > total of pixel * 0.45 count that as dark image
fun Bitmap.isDark(): Boolean {
    try {
        val ratio = this.width / this.height
        val widthBitmapChecker = 50
        val heightBitmapChecker = widthBitmapChecker / ratio
        val bitmapChecker =
            Bitmap.createScaledBitmap(this, widthBitmapChecker, heightBitmapChecker, false)
        val darkThreshold = bitmapChecker.width * bitmapChecker.height * 0.45f
        var darkPixels = 0
        val pixels = IntArray(bitmapChecker.width * bitmapChecker.height)
        bitmapChecker.getPixels(
            pixels,
            0,
            bitmapChecker.width,
            0,
            0,
            bitmapChecker.width,
            bitmapChecker.height
        )
        val luminanceThreshold = 150
        for (i in pixels.indices) {
            val color = pixels[i]
            val r = Color.red(color)
            val g = Color.green(color)
            val b = Color.blue(color)
            val luminance = 0.299 * r + 0.0f + 0.587 * g + 0.0f + 0.114 * b + 0.0f
            if (luminance < luminanceThreshold) {
                darkPixels++
            }
        }
        bitmapChecker.recycle()
        return darkPixels >= darkThreshold
    } catch (t: Throwable) {
        return false
    }
}

fun writeBitmapToStorage(
    context: Context,
    bitmapParam: Bitmap,
    filename: String? = null
): File? {
    return try {
        val file = getDestinationUri(context, filename).toFile()
        file.createNewFile()

        val bos = ByteArrayOutputStream()
        bitmapParam.compress(Bitmap.CompressFormat.PNG, 0, bos)
        val bitmapData = bos.toByteArray()

        val fos = FileOutputStream(file)
        fos.write(bitmapData)
        fos.flush()
        fos.close()

        file
    } catch (e: Exception) {
        null
    }
}