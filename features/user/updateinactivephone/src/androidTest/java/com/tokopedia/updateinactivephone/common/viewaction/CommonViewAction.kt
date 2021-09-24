package com.tokopedia.updateinactivephone.common.viewaction

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.updateinactivephone.common.viewmatcher.withRecyclerView
import org.hamcrest.Matcher

fun clickChildViewWithId(id: Int): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View>? {
            return null
        }

        override fun getDescription(): String {
            return "Click on a child view with specified id."
        }

        override fun perform(uiController: UiController?, view: View) {
            val v: View = view.findViewById(id)
            v.performClick()
        }
    }
}

fun isDisplayed(resId: Int) {
    onView(withId(resId))
        .check(matches(isDisplayed()))
}

fun isTextDisplayed(resId: Int, text: String) {
    onView(withId(resId))
        .check(matches(withText(text)))
}

fun clickOnPosition(resId: Int, itemId: Int, position: Int) {
    onView(withRecyclerView(resId)
        .atPositionOnView(position, itemId))
        .perform(click())
}

fun clickOnButton(resId: Int) {
    onView(withId(resId))
        .perform(click())
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