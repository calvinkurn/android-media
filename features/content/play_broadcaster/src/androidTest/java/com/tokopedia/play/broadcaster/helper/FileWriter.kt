package com.tokopedia.play.broadcaster.helper

import android.os.Environment
import androidx.test.platform.app.InstrumentationRegistry
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

/**
 * Created by kenny.hadisaputra on 23/02/22
 */
class FileWriter {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
    private val documentsFolder = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!.path.toString()

    fun write(folderName: String = "", fileName: String, text: String) {
        val folder = File(documentsFolder, folderName)
        if (!folder.exists()) folder.mkdirs()

        val file = File(folder, fileName)

        val outputStream = BufferedOutputStream(FileOutputStream(file))
        outputStream.use {
            it.write(text.toByteArray())
            it.flush()
        }

        println("Write is successful")
    }
}