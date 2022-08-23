package com.tokopedia.media.picker.helper.matchers

import android.view.View
import android.widget.TextView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description

inline fun <reified T: View> withTextColor(color: Int): BoundedMatcher<View, T> {
    return object : BoundedMatcher<View, T>(T::class.java) {
        override fun describeTo(description: Description?) {
            description?.appendText("with text color:")
        }

        override fun matchesSafely(item: T): Boolean {
            return color == (item as TextView).currentTextColor
        }
    }
}