package com.tokopedia.tokopedianow.test.utils.matcher

import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.tokopedianow.common.view.productcard.TokoNowProductCardView.Companion.DEFAULT_MAX_LINES
import com.tokopedia.tokopedianow.common.view.productcard.TokoNowProductCardView.Companion.MAX_LINES_NEEDED_TO_CHANGE
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

internal class TokoNowProductCardNameTypographyDisplayedMatcher(
    private val productName: String,
    private val needToChangeMaxLinesName: Boolean
): TypeSafeMatcher<com.tokopedia.unifyprinciples.Typography>() {

    override fun describeTo(description: Description) { /* nothing to do */ }

    override fun matchesSafely(item: com.tokopedia.unifyprinciples.Typography): Boolean {
        val maxLines = if (needToChangeMaxLinesName) MAX_LINES_NEEDED_TO_CHANGE else DEFAULT_MAX_LINES
        return ViewMatchers.isDisplayed().matches(item) && ViewMatchers.withText(productName).matches(item) && item.maxLines == maxLines
    }

}
