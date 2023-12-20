package com.tokopedia.productcard.test.utils

import android.text.Spannable
import android.text.style.ImageSpan
import android.view.View
import android.widget.TextView
import androidx.core.text.getSpans
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Description
import org.hamcrest.Matcher

internal fun isDisplayedWithTextAndImage(text: String): Matcher<View?> =
    IsDisplayedWithTextAndImage(text)

private class IsDisplayedWithTextAndImage(
    private val text: String,
): BoundedMatcher<View, TextView>(TextView::class.java) {

    override fun describeTo(description: Description?) {
        description?.appendText("is displayed with image and text: $text")
    }

    override fun matchesSafely(item: TextView): Boolean {
        val currentText = item.text
        return if (currentText is Spannable) {
            currentText.getSpans<Any>().any { it is ImageSpan }
                && currentText.substring(2) == text
        } else {
            withText(text).matches(item)
        }
    }
}
