package com.tokopedia.product.detail.util

import android.view.View
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Description

/**
 * Created by Yehezkiel on 20/04/21
 */

fun ViewInteraction.assertVisible() : ViewInteraction {
    this.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    return this
}

fun ViewInteraction.assertNotVisible() : ViewInteraction {
    this.check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))
    return this
}

class ViewAttributeMatcher(
        private val checkAttribute: (View?) -> Boolean
) : BoundedMatcher<View?, View>(View::class.java) {
    override fun describeTo(description: Description?) {
        description?.appendText("Check attribute view")
    }

    override fun matchesSafely(item: View?): Boolean {
        return checkAttribute.invoke(item)
    }
}