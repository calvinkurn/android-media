package com.tokopedia.tokopedianow.test.utils.matcher

import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.tokopedianow.common.view.productcard.TokoNowProductCardView.Companion.DEFAULT_MAX_LINES
import com.tokopedia.tokopedianow.common.view.productcard.TokoNowProductCardView.Companion.MAX_LINES_NEEDED_TO_CHANGE
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import com.tokopedia.unifyprinciples.Typography
import org.hamcrest.CoreMatchers.`is`

internal class TokoNowProductCardNameTypographyDisplayedMatcher(
    private val productName: String,
    private val needToChangeMaxLinesName: Boolean
): TypeSafeMatcher<Typography>() {

    override fun describeTo(description: Description) { /* nothing to do */ }

    override fun matchesSafely(item: Typography): Boolean {
        val maxLines = if (needToChangeMaxLinesName) MAX_LINES_NEEDED_TO_CHANGE else DEFAULT_MAX_LINES
        return isDisplayed().matches(item) && withText(productName).matches(item) && `is`(maxLines).matches(item.maxLines)
    }

}
