package com.tokopedia.productcard.test

import android.graphics.Bitmap
import androidx.test.runner.screenshot.Screenshot
import org.junit.rules.TestWatcher
import org.junit.runner.Description

internal class ScreenshotWatcher: TestWatcher() {

    override fun succeeded(description: Description?) {
        captureScreenshot("${description?.methodName}_succeeded")
    }

    private fun captureScreenshot(screenCaptureName: String) {
        try {
            tryCaptureScreenshot(screenCaptureName)
        }
        catch (throwable: Throwable) {
            catchCaptureScreenshotError(throwable)
        }
    }

    private fun tryCaptureScreenshot(screenCaptureName: String) {
        val screenCapture = Screenshot.capture()
        screenCapture.format = Bitmap.CompressFormat.PNG
        screenCapture.name = screenCaptureName

        screenCapture.process()
    }

    private fun catchCaptureScreenshotError(throwable: Throwable) {
        throwable.printStackTrace()
    }

    override fun failed(e: Throwable?, description: Description?) {
        captureScreenshot("${description?.methodName}_failed")
    }
}