package com.tokopedia.loginregister.registerinitial

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.junit.Assert
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


internal object WaitUiHelper {
    fun waifForWithText(text: String, duration: Long) {
        var element: ViewInteraction
        do {
            waitFor(duration)

            //simple example using withText Matcher.
            element = onView(withText(text))
        } while (!MatcherExtension.exists(element))
    }

    private fun waitFor(ms: Long) {
        val signal = CountDownLatch(1)
        try {
            signal.await(ms, TimeUnit.MILLISECONDS)
        } catch (e: InterruptedException) {
            Assert.fail(e.message)
        }
    }
}