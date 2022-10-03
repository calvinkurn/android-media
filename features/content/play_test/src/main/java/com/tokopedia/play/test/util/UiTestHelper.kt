package com.tokopedia.play.test.util

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not

/**
 * Created By : Jonathan Darwin on October 03, 2022
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

fun isVisible(@IdRes vararg ids: Int) {
    ids.forEach { id -> select(id).isVisible() }
}

fun isHidden(@IdRes vararg ids: Int) {
    ids.forEach { id -> select(id).isHidden() }
}