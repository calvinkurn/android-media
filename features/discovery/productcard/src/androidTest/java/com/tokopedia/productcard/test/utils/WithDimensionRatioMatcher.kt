package com.tokopedia.productcard.test.utils

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

internal fun withDimensionRatio(ratio: String): Matcher<View> {
    return WithDimensionRatioMatcher(ratio)
}

private class WithDimensionRatioMatcher(
    private val ratio: String
): TypeSafeMatcher<View>() {
    override fun describeTo(description: Description?) {
        description?.appendText("dimension ratio should be: $ratio")
    }

    override fun matchesSafely(item: View?): Boolean {
        if (item == null) return false

        return (item.layoutParams as ConstraintLayout.LayoutParams).dimensionRatio == ratio
    }
}