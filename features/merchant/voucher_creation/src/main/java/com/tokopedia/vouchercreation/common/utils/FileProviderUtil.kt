package com.tokopedia.vouchercreation.common.utils

import android.content.Context
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

private const val FILE_DIR = "voucher_images"

fun Bitmap.getSavedImageDirPath(context: Context, filename: String): String {
    val file = getSavedImageDirFile(context, filename)
    return file.toString()
}

fun Bitmap.getSavedImageDirFile(context: Context, filename: String): File {
    val basePath = File(context.filesDir, FILE_DIR).also {
        if (!it.exists()) {
            it.mkdir()
        }
    }
    val filePath = File(basePath, "${filename}.jpg")
    val fos = FileOutputStream(filePath)

    try {
        compress(Bitmap.CompressFormat.JPEG, 100, fos)
    } catch (ex: Exception) {
        ex.printStackTrace()
    } finally {
        try {
            fos.close()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
    }
    return filePath
}
