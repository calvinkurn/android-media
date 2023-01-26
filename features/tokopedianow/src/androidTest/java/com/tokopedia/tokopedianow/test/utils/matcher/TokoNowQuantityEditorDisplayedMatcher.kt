package com.tokopedia.tokopedianow.test.utils.matcher

import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.tokopedianow.common.view.productcard.TokoNowQuantityEditorView
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

internal class TokoNowQuantityEditorDisplayedMatcher(
    private val minOrder: Int,
    private val maxOrder: Int,
    private val orderQuantity: Int
): TypeSafeMatcher<TokoNowQuantityEditorView>() {

    override fun describeTo(description: Description) { /* nothing to do */ }

    override fun matchesSafely(item: TokoNowQuantityEditorView): Boolean = isDisplayed().matches(item) &&
        `is`(minOrder).matches(item.minQuantity) &&
        `is`(maxOrder).matches(item.maxQuantity) &&
        `is`(orderQuantity).matches(item.getQuantity())

}
