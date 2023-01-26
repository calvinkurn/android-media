package com.tokopedia.tokopedianow.test.utils.matcher

import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.tokopedianow.common.view.productcard.TokoNowWishlistButtonView
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class TokoNowWishlistButtonDisplayedMatcher (
    private val hasBeenSelected: Boolean
): TypeSafeMatcher<TokoNowWishlistButtonView>() {

    override fun describeTo(description: Description) { /* nothing to do */ }

    override fun matchesSafely(item: TokoNowWishlistButtonView): Boolean = ViewMatchers.isDisplayed().matches(item) && `is`(hasBeenSelected).matches(item.getValue())

}
