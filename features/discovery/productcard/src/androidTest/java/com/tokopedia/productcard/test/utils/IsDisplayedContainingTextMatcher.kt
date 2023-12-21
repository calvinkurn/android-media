package com.tokopedia.productcard.test.utils

import android.view.View
import android.widget.TextView
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withSubstring
import org.hamcrest.Description
import org.hamcrest.Matcher

internal fun isDisplayedContainingText(text: String): Matcher<View?> =
    IsDisplayedContainingTextMatcher(text)

private class IsDisplayedContainingTextMatcher(
    private val text: String,
): BoundedMatcher<View, TextView>(TextView::class.java) {

    override fun describeTo(description: Description?) {
        description?.appendText("is displayed containing text: $text")
    }

    override fun matchesSafely(item: TextView): Boolean {
        return isDisplayed().matches(item) && withSubstring(text).matches(item)
    }
}
