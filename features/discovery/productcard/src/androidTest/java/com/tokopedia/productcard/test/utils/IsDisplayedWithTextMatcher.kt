package com.tokopedia.productcard.test.utils

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

internal fun isDisplayedWithText(text: String): Matcher<View?> {
    return IsDisplayedWithTextMatcher(text)
}

private class IsDisplayedWithTextMatcher(
        private val text: String
) : TypeSafeMatcher<View>() {

    override fun describeTo(description: Description?) {
        description?.appendText("is displayed with text: $text")
    }

    override fun matchesSafely(item: View?): Boolean {
        return item != null && isDisplayed().matches(item) && withText(text).matches(item)
    }
}