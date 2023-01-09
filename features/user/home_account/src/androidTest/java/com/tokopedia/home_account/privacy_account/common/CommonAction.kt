package com.tokopedia.home_account.privacy_account.common

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.Visibility.GONE
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.isNotChecked
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.CoreMatchers.not

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

fun isGoneView(resId: Int) {
    onView(withId(resId))
        .check(matches(withEffectiveVisibility(GONE)))
}

fun isNotDisplayed(vararg resIds: Int) {
    resIds.forEach {
        onView(withId(it))
            .check(doesNotExist())
    }
}

fun clickOnView(resId: Int) {
    onView(withId(resId))
        .check(matches(isDisplayed()))
        .perform(click())
}

fun isChecked(isCheck: Boolean, resId: Int) {
    onView(withId(resId))
        .check(
            matches(
                if (isCheck) isChecked() else isNotChecked()
            )
        )
}

fun isEnable(isEnabled: Boolean, resId: Int) {
    onView(withId(resId))
        .check(
            matches(
                if (isEnabled) isEnabled() else not(isEnabled())
            )
        )
}

fun clickOnSpannable(linkText: String, resId: Int) {
    onView(withId(resId)).perform(clickOnSpannable(linkText))
}
