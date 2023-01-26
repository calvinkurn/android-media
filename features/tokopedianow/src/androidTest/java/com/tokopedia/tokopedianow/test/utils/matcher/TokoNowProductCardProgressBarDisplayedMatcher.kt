package com.tokopedia.tokopedianow.test.utils.matcher

import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.unifycomponents.ProgressBarUnify
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

internal class TokoNowProductCardProgressBarDisplayedMatcher (
    private val progressBarPercentage: Int
): TypeSafeMatcher<ProgressBarUnify>() {
    override fun describeTo(description: Description) { /* nothing to do */ }

    override fun matchesSafely(item: ProgressBarUnify): Boolean {
        return isDisplayed().matches(item) && `is`(progressBarPercentage).matches(item.getValue())
    }
}
