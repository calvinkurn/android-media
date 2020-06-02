package com.tokopedia.vouchercreation.common.utils

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

private const val FILE_DIR = "voucher_images"

fun Bitmap.getSavedImageDirPath(context: Context, filename: String): String {
    val contextWrapper = ContextWrapper(context)
    val fileDir = contextWrapper.getDir(FILE_DIR, Context.MODE_PRIVATE)
    val filePath = File(fileDir, "${filename}.jpg")

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
    return filePath.toString()
}
