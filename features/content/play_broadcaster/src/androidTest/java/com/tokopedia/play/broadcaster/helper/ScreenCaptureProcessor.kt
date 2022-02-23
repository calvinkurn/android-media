package com.tokopedia.play.broadcaster.helper

import android.os.Environment.DIRECTORY_PICTURES
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.runner.screenshot.BasicScreenCaptureProcessor
import java.io.File

/**
 * Created by kenny.hadisaputra on 23/02/22
 */
class ScreenCaptureProcessor : BasicScreenCaptureProcessor() {
    init {
        mTag = "ScreenCaptureProcessor"
        mFileNameDelimiter = "-"
        mDefaultFilenamePrefix = "PlayBro"
        mDefaultScreenshotPath = getNewFilename()
    }

    private fun getNewFilename(): File? {
        val context = getInstrumentation().targetContext.applicationContext
        return context.getExternalFilesDir(DIRECTORY_PICTURES)
    }
}