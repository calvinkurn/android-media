package com.tokopedia.test.application.id_generator

import android.content.ContentValues
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.test.platform.app.InstrumentationRegistry
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by kenny.hadisaputra on 15/04/22
 */
class FileWriter {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext

    fun write(folderName: String = "", fileName: String, text: String) {
        val (path, outputStream) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
                put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_DOWNLOADS}/$folderName")
            }
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues) ?: return
            uri.path to (resolver.openOutputStream(uri) ?: throw IOException("Failed to open output stream."))
        } else {
            val downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val folder = File(downloadsFolder, folderName)
            if (!folder.exists()) folder.mkdirs()

            val file = File(folder, fileName)

            val outputStream = BufferedOutputStream(FileOutputStream(file))
            file.absolutePath to outputStream
        }

        outputStream.use {
            it.write(text.toByteArray())
            it.flush()
        }
        println("Write is successful at $path")
    }
}
