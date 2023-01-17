package com.tokopedia.kyc_centralized.util

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.util.TreeIterables
import org.hamcrest.Matcher


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
            Espresso.onView(ViewMatchers.isRoot()).perform(searchFor(viewMatcher))
            return Espresso.onView(viewMatcher)
        } catch (e: Exception) {
            if (tries == maxTries) {
                throw e
            }
            Thread.sleep(waitMillisPerTry)
        }
    throw NoSuchElementException()
}

fun searchFor(matcher: Matcher<View>): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return ViewMatchers.isRoot()
        }

        override fun getDescription(): String {
            return "Searching for view $matcher in the root view"
        }

        override fun perform(uiController: UiController, view: View) {
            var tries = 0
            val childViews: Iterable<View> = TreeIterables.breadthFirstViewTraversal(view)
            childViews.forEach {
                tries++
                if (matcher.matches(it)) {
                    return
                }
            }
            throw NoMatchingViewException.Builder()
                .withRootView(view)
                .withViewMatcher(matcher)
                .build()
        }
    }
}
