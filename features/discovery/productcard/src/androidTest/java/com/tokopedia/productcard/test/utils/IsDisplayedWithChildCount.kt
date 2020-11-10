package com.tokopedia.productcard.test.utils

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

internal fun isDisplayedWithChildCount(childCount: Int): Matcher<View?> {
    return IsDisplayedWithChildCount(childCount)
}

private class IsDisplayedWithChildCount(
        private val childCount: Int
) : TypeSafeMatcher<View>() {

    override fun describeTo(description: Description?) {
        description?.appendText("is displayed with child count: $childCount")
    }

    override fun matchesSafely(item: View?): Boolean {
        return item != null
                && isDisplayed().matches(item)
                && item is ViewGroup
                && item.childCount == childCount
    }
}