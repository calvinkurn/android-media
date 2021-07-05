package com.tokopedia.otp.common.utility

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import com.tokopedia.otp.common.action.RootViewAction.searchFor
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
        throw Exception("Error finding a view matching view matcher")
    }
}