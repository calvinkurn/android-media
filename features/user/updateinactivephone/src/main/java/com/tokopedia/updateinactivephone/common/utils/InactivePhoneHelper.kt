package com.tokopedia.updateinactivephone.common.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.jvm.Throws


fun convertToBitmap(path: String): Bitmap? {
    val file = File(path)
    if (file.exists()) {
        val bmOptions = BitmapFactory.Options()
        bmOptions.inSampleSize = 2
        bmOptions.inJustDecodeBounds = false
        val bitmap =  BitmapFactory.decodeFile(file.absolutePath, bmOptions)
        return bitmap?.let { modifyOrientation(it, file.absolutePath) }
    }

    return null
}

@Throws(IOException::class)
fun modifyOrientation(bitmap: Bitmap, filePath: String): Bitmap? {
    val ei = ExifInterface(filePath)
    return when (ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotate(bitmap, 90f)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotate(bitmap, 180f)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotate(bitmap, 270f)
        ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> flip(bitmap, horizontal = true, vertical = false)
        ExifInterface.ORIENTATION_FLIP_VERTICAL -> flip(bitmap, horizontal = false, vertical = true)
        else -> bitmap
    }
}

fun rotate(bitmap: Bitmap, degrees: Float): Bitmap? {
    val matrix = Matrix()
    matrix.postRotate(degrees)
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}

fun flip(bitmap: Bitmap, horizontal: Boolean, vertical: Boolean): Bitmap? {
    val matrix = Matrix()
    matrix.preScale(if (horizontal) -1f else 1f, if (vertical) -1f else 1f)
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}


@Throws(IOException::class)
fun convertBitmapToImageFile(bitmap: Bitmap, quality: Int, filePath: String): File {
    val file = File(filePath)
    if (file.exists()) {
        file.delete()
    }

    try {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bytes)
        val fo = FileOutputStream(file)
        fo.write(bytes.toByteArray())
        fo.flush()
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return file
}