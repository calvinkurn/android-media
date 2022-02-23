package com.tokopedia.play.broadcaster.helper

import android.graphics.Bitmap
import androidx.test.runner.screenshot.Screenshot
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.io.IOException

/**
 * Created by kenny.hadisaputra on 23/02/22
 */
class ScreenshotTestRule : TestWatcher() {
    override fun finished(description: Description?) {
        super.finished(description)

        val className = description?.testClass?.simpleName ?: "NullClassname"
        val methodName = description?.methodName ?: "NullMethodName"
        val filename = "$className - $methodName"

        val capture = Screenshot.capture()
        capture.name = filename
        capture.format = Bitmap.CompressFormat.PNG

        val processors = setOf(
            ScreenCaptureProcessor()
        )

        try {
            capture.process(processors)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
}