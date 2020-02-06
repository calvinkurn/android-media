package com.tokopedia.productcard.test

import android.view.View
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

internal class IsNotDisplayedMatcher: TypeSafeMatcher<View?>() {
    override fun describeTo(description: Description?) {
        description?.appendText("is not displayed")
    }

    override fun matchesSafely(item: View?): Boolean {
        return item != null && item.visibility == View.GONE
    }
}