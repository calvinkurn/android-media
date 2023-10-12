package com.tokopedia.test.application.util

import android.graphics.Bitmap.CompressFormat
import android.os.Environment.DIRECTORY_PICTURES
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.runner.screenshot.BasicScreenCaptureProcessor
import androidx.test.runner.screenshot.ScreenCaptureProcessor
import androidx.test.runner.screenshot.Screenshot
import okio.IOException
import timber.log.Timber
import java.io.File

fun takeScreenshot(description: String) {
    val filename: String = description
    val capture = Screenshot.capture()
    capture.name = filename
    capture.format = CompressFormat.PNG
    val processors: HashSet<ScreenCaptureProcessor> = HashSet()
    processors.add(BasicDumpOverwriteCaptureProcessor())
    try {
        capture.process(processors)
    } catch (e: IOException) {
        Timber.e(e)
    }
}

class BasicDumpOverwriteCaptureProcessor : BasicScreenCaptureProcessor() {
    init {
        mTag = "IDTScreenCaptureProcessor"
        mFileNameDelimiter = "-"
        mDefaultFilenamePrefix = "Giorgos"
        mDefaultScreenshotPath = getNewFilename()
    }

    override fun getFilename(prefix: String?): String {
        return prefix.orEmpty()
    }

    private fun getNewFilename(): File? {
        val context = getInstrumentation().targetContext.applicationContext
        return context.getExternalFilesDir(DIRECTORY_PICTURES)
    }
}
