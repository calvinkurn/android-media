package com.tokopedia.play.broadcaster.helper

import android.os.Environment
import android.view.View
import android.view.ViewGroup
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.play.broadcaster.test.BuildConfig
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

/**
 * Created by kenny.hadisaputra on 23/02/22
 */
class FileWriter {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
    private val documentsFolder = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)

    fun write(fileName: String, text: String) {
        val file = File(documentsFolder, fileName)
        val outputStream = BufferedOutputStream(FileOutputStream(file))
        outputStream.use {
            it.write(text.toByteArray())
            it.flush()
        }

        println("Write is successful")
    }
}