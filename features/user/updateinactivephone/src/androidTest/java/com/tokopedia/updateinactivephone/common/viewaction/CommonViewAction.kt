package com.tokopedia.updateinactivephone.common.viewaction

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.test.application.matcher.RecyclerViewMatcher
import com.tokopedia.updateinactivephone.common.viewmatcher.withRecyclerView
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher

fun isDisplayed(resId: Int) {
    onView(withId(resId))
        .check(matches(isDisplayed()))
}

fun isTextDisplayed(text: String) {
    onView(withText(text))
        .check(matches(isDisplayed()))
}

fun isTextNotDisplayed(text: String) {
    onView(withText(text))
        .check(doesNotExist())
}

fun isChildTextDisplayed(resId: Int, childRes: Int, position: Int, text: String) {
    onView(RecyclerViewMatcher(resId).atPositionOnView(position, childRes))
        .check(matches(withText(text)))
}

fun clickOnPosition(resId: Int, itemId: Int, position: Int) {
    onView(withRecyclerView(resId)
        .atPositionOnView(position, itemId))
        .perform(click())
}

fun clickOnButton(resId: Int) {
    onView(withId(resId))
        .check(matches(isDisplayed()))
        .perform(click())
}

fun clickOnButtonWithTextAndId(textButton: String, resId: Int) {
    onView(allOf(withText(textButton), withId(resId)))
        .check(matches(isDisplayed()))
        .perform(click())
}

fun scrollToView(resId: Int) {
    onView(withId(resId))
        .perform(scrollTo())
}

fun scrollAndIsDisplayed(resId: Int) {
    scrollToView(resId)
    isDisplayed(resId)
}

fun scrollToPosition(resId: Int, position: Int) {
    onView(withId(resId)).perform(
        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position, ViewActions.scrollTo())
    )
}

fun smoothScrollToPosition(resId: Int, position: Int) {
    onView(withId(resId)).perform(
        smoothScrollTo(position)
    )
}

fun assertRecyclerViewItem(resId: Int, matcher: Matcher<in View>) {
    onView(withId(resId))
        .check(matches(matcher))
}

fun setText(resId: Int, text: String) {
    onView(withId(resId))
        .perform(typeText(text), closeSoftKeyboard())
}

fun simulateOnBackPressed() {
    Thread.sleep(2000)
    Espresso.pressBackUnconditionally()
}
