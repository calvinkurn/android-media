package com.tokopedia.productcard.test

import android.view.View
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions
import org.hamcrest.Matcher

internal fun ViewInteraction.checkProductCardAtPosition(position: Int, elementMatchers: Map<Int, Matcher<View?>>): ViewInteraction {
    return check(ViewAssertions.matches(matchProductCardAtPosition(position, elementMatchers)))
}

internal fun matchProductCardAtPosition(position: Int, itemMatcherList: Map<Int, Matcher<View?>>): Matcher<View?>? {
    return ProductCardInPositionMatcher(position, itemMatcherList)
}

fun isNotDisplayed(): Matcher<View?> {
    return IsNotDisplayedMatcher()
}