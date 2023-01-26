package com.tokopedia.tokopedianow.test.utils.matcher

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

internal class ViewDisplayedWithTextMatcher(
    private val text: String
) : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description) { /* nothing to do */ }

    override fun matchesSafely(item: View): Boolean = isDisplayed().matches(item) && withText(text).matches(item)
}
