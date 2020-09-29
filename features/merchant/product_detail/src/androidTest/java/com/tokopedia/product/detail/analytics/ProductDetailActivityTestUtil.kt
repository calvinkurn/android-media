package com.tokopedia.product.detail.analytics

import android.app.Activity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule

object ProductDetailActivityTestUtil {

    fun performClickMatches(idView: Int): ViewInteraction = onView(withId(idView)).check(matches(isDisplayed())).perform(click())

    fun performMatches(idView: Int): ViewInteraction = onView(withId(idView)).check(matches(isDisplayed()))

    fun performScrollToClick(idView: Int): ViewInteraction = onView(withId(idView)).check(matches(isDisplayed())).perform(scrollTo()).perform(click())

    fun <T: Activity> performClose(activityTestRule: IntentsTestRule<T>) {
        activityTestRule.finishActivity()
    }
}