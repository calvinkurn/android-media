package com.tokopedia.productcard.test.utils

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.unifycomponents.QuantityEditorUnify
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher

@Suppress("UNCHECKED_CAST")
internal fun isQuantityEditorDisplayedWithValue(expectedValue: Int): Matcher<View?> {
    return IsQuantityEditorDisplayedWithValue(expectedValue) as Matcher<View?>
}

private class IsQuantityEditorDisplayedWithValue(
        private val expectedValue: Int
): TypeSafeMatcher<QuantityEditorUnify>() {

    override fun describeTo(description: Description?) {
        description?.appendText("is displayed with value $expectedValue.")
    }

    override fun matchesSafely(item: QuantityEditorUnify): Boolean {
        return isDisplayed().matches(item)
                && `is`(expectedValue).matches(item.getValue())
    }
}