package com.tokopedia.usercomponents.common

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*

fun isDisplayed(resId: Int) {
    onView(withId(resId))
        .check(matches(isDisplayed()))
}

fun isDisplayed(listResId: List<Int>) {
    listResId.forEach {
        isDisplayed(it)
    }
}

fun isTextDisplayed(text: String) {
    onView(withText(text))
        .check(matches(isDisplayed()))
}

fun isTextDisplayed(listText: List<String>) {
    listText.forEach {
        isTextDisplayed(it)
    }
}

fun isNotDisplayed(resId: Int) {
    onView(withId(resId))
        .check(doesNotExist())
}

fun isNotDisplayed(listResId: List<Int>) {
    listResId.forEach {
        isNotDisplayed(it)
    }
}

fun clickOnButton(resId: Int) {
    onView(withId(resId))
        .check(matches(isDisplayed()))
        .perform(click())
}