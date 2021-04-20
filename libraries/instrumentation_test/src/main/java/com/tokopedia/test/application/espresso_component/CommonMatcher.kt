package com.tokopedia.test.application.espresso_component

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers

object CommonMatcher {
    /**
     * Matcher to find view with string tag.
     * Use this to find your view using tag, please provide unique tag for your view
     *
     * @param tagStringValue tag string value of your view
     */
    fun withTagStringValue(tagStringValue: String): Matcher<View> {
        return ViewMatchers.withTagValue(Matchers.`is`(tagStringValue))
    }

    /**
     * Matcher to find firstView from top.
     * Use this if you are struggling with Espresso AmbiguousViewMatcherException because there is
     * multiple view with same id/tag in a viewport
     *
     * @param matcher to combine it with Matcher
     */
    fun <T> firstView(matcher: Matcher<T>): Matcher<T> {
        return object : BaseMatcher<T>() {
            var isFirst = true
            override fun matches(item: Any): Boolean {
                if (isFirst && matcher.matches(item)) {
                    isFirst = false
                    return true
                }
                return false
            }

            override fun describeTo(description: Description) {
                description.appendText("should return first matching item")
            }
        }
    }

    /**
     * Matcher to find view at specific position
     * Use this if you are struggling with Espresso multiple view with same id/tag in a viewport
     *
     * e.g :
     * You have RecyclerView with 4 items.
     * 1. R.id.tvTitle with text "10"
     * 2. R.id.tvTitle with text "30"
     * 3. R.id.tvTitle with text "40"
     * 4. R.id.tvTitle with text "10"
     *
     * you want to perform click on tvTitle in the 2nd item, you can use :
     * onView(getElementFromMatchAtPosition(withId(R.id.tvTitle), 1)).perform(click())
     *
     * you want to check if text "10" in the 4th item is displayed, you can use :
     * onView(getElementFromMatchAtPosition(withText("10"), 1).check(matches(isDisplayed()))
     * note : 4th items is the second item with text "10", so it will be in position 1 for withText("10")
     *
     * @param matcher view matcher that you want to find
     * @param position view position that you want to get
     */
    fun getElementFromMatchAtPosition(
            matcher: Matcher<View>,
            position: Int
    ): Matcher<View?>? {
        return object : BaseMatcher<View?>() {
            var counter = 0
            override fun matches(item: Any): Boolean {
                if (matcher.matches(item)) {
                    if (counter == position) {
                        counter++
                        return true
                    }
                    counter++
                }
                return false
            }

            override fun describeTo(description: Description) {
                description.appendText("Element at hierarchy position $position")
            }
        }
    }
}