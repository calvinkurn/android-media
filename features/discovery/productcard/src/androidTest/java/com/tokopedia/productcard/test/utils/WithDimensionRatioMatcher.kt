package com.tokopedia.productcard.test.utils

import android.view.View
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
        val ratioList = ratio.split(":")
        if (ratioList.size != 2 && item == null) return false

        val imageRatio: Float = ratioList[1].toFloat() / ratioList[0].toFloat()

        return item!!.measuredWidth * imageRatio == item.measuredHeight.toFloat()
    }
}