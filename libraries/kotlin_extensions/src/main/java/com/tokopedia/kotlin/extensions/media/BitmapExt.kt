package com.tokopedia.kotlin.extensions.media

import android.content.Context
import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

/**
 * Created by jegul on 2019-08-20.
 */
fun Bitmap.toTempFile(context: Context, filename: String): File {
    val byteOutputStream = ByteArrayOutputStream()
    val file = createLocalFile(context, "$filename.jpg")
    compress(Bitmap.CompressFormat.JPEG, 100, byteOutputStream)
    val fo = FileOutputStream(file)
    fo.use { it.write(byteOutputStream.toByteArray()) }
    return file
}

private fun createLocalFile(context: Context, fileNameWithFormat: String): File {
    return File(context.filesDir, "Tokopedia/"+ fileNameWithFormat)
}