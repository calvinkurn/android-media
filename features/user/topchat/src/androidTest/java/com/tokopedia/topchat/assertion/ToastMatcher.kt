package com.tokopedia.topchat.assertion

import android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
import android.view.WindowManager.LayoutParams.TYPE_TOAST
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Root
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

class ToastMatcher(private val maxFailures: Int) : TypeSafeMatcher<Root>() {

    private var failures = 0

    override fun describeTo(description: Description) {
        description.appendText("is toast")
    }

    public override fun matchesSafely(root: Root): Boolean {
        val type = root.windowLayoutParams.get().type
        if (type == TYPE_TOAST || type == TYPE_APPLICATION_OVERLAY) {
            val windowToken = root.decorView.windowToken
            val appToken = root.decorView.applicationWindowToken
            if (windowToken === appToken) {
                return true
            }
        }
        return (++failures >= maxFailures)
    }

    companion object {
        private const val DEFAULT_MAX_FAILURES = 5

        /**
        * How to use:
         * ToastMatcher.onToast(R.string.toast_text)
         * ToastMatcher.onToast(R.string.toast_text, 3)
         * ToastMatcher.onToast("Toast Text")
         * ToastMatcher.onToast("Toast Text", 3)
        */
        fun onToast(text: String, maxRetries: Int = DEFAULT_MAX_FAILURES): ViewInteraction? {
            return onView(withText(text)).inRoot(isToast(maxRetries))
        }

        fun onToast(textId: Int, maxRetries: Int = DEFAULT_MAX_FAILURES): ViewInteraction? {
            return onView(withText(textId)).inRoot(isToast(maxRetries))
        }

        private fun isToast(maxRetries: Int = DEFAULT_MAX_FAILURES): Matcher<Root> {
            return ToastMatcher(maxRetries)
        }
    }

}