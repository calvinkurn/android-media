package com.tokopedia.kotlin.extensions.media

import android.graphics.Bitmap
import android.os.Environment
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

/**
 * Created by jegul on 2019-08-20.
 */
fun Bitmap.toTempFile(filename: String): File {
    val byteOutputStream = ByteArrayOutputStream()
    val file = createTempFile("$filename.jpg")
    compress(Bitmap.CompressFormat.JPEG, 100, byteOutputStream)
    val fo = FileOutputStream(file)
    fo.use { it.write(byteOutputStream.toByteArray()) }
    return file
}

fun createTempFile(fileNameWithFormat: String): File {
    val path = Environment.getExternalStorageDirectory().toString() + File.separator + "tkpdtemp" + File.separator
    val pathFile = File(path)
    if (pathFile.exists() && pathFile.isDirectory) {
        Timber.tag("Files").v("Exist")
        val fs = pathFile.listFiles()
        if (fs != null && fs.size > 5)
            for (file in fs) {
                file.delete()
            }
    } else {
        Timber.tag("Files").v("Not Exist")
        pathFile.mkdir()
    }

    return File("$path$fileNameWithFormat").apply { createNewFile() }
}