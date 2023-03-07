package com.tokopedia.tkpd.feed_component.helper

import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher

/**
 * Created By : Jonathan Darwin on September 23, 2022
 */

fun select(@IdRes id: Int): ViewInteraction {
    return onView(withId(id))
}

fun select(text: String): ViewInteraction {
    return onView(withText(text))
}

fun selectTag(tag: String): ViewInteraction {
    return onView(withTagValue(`is`(tag)))
}

fun ViewInteraction.clickView() {
    perform(click())
}

fun ViewInteraction.isVisible() {
    check(matches(isDisplayed()))
}

fun ViewInteraction.isHidden() {
    check(matches(not(isDisplayed())))
}

fun delay(delayInMillis: Long = 500): ViewInteraction {
    return onView(isRoot()).perform(waitFor(delayInMillis))
}

fun waitFor(delay: Long): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> = ViewMatchers.isRoot()
        override fun getDescription(): String = "wait for $delay milliseconds"
        override fun perform(uiController: UiController, v: View?) {
            uiController.loopMainThreadForAtLeast(delay)
        }
    }
}
