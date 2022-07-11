package com.tokopedia.usercomponents.common

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed

fun isDisplayed(vararg resIds: Int) {
    resIds.forEach {
        onView(withId(it))
            .check(matches(isDisplayed()))
    }
}

fun isTextDisplayed(vararg texts: String) {
    texts.forEach {
        onView(withText(it))
            .check(matches(isDisplayed()))
    }
}

fun isNotDisplayed(vararg resIds: Int) {
    resIds.forEach {
        onView(withId(it))
            .check(doesNotExist())
    }
}

fun clickOnButton(resId: Int) {
    onView(withId(resId))
        .check(matches(isDisplayed()))
        .perform(click())
}