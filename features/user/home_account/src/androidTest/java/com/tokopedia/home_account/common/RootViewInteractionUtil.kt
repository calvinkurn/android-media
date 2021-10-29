package com.tokopedia.home_account.common

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import com.tokopedia.home_account.common.RootViewAction.searchFor
import org.hamcrest.Matcher
import java.lang.Thread.sleep

object RootViewInteractionUtil {

    fun waitOnView(
            viewMatcher: Matcher<View>,
            waitMillis: Int = 5000,
            waitMillisPerTry: Long = 100
    ): ViewInteraction {
        val maxTries = waitMillis / waitMillisPerTry.toInt()
        var tries = 0
        for (i in 0..maxTries)
            try {
                tries++
                onView(isRoot()).perform(searchFor(viewMatcher))
                return onView(viewMatcher)
            } catch (e: Exception) {
                if (tries == maxTries) {
                    throw e
                }
                sleep(waitMillisPerTry)
            }
        throw NoSuchElementException()
    }
}