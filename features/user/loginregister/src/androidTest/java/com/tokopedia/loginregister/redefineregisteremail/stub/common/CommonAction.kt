package com.tokopedia.loginregister.redefineregisteremail.stub.common

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withInputType
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf

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

fun clickOnView(resId: Int) {
    onView(withId(resId))
        .check(matches(isDisplayed()))
        .perform(click())
}

fun clickOnText(text: String) {
    onView(withText(text))
        .check(matches(isDisplayed()))
        .perform(forceClick())
}

fun isEnable(isEnabled: Boolean, resId: Int) {
    onView(withId(resId))
        .check(
            matches(
                if (isEnabled) isEnabled() else not(isEnabled())
            )
        )
}

fun isInputTypeEnable(isEnabled: Boolean, inputType: Int) {
    onView(withInputType(inputType))
        .check(
            matches(
                if (isEnabled) isEnabled() else not(isEnabled())
            )
        )
}

fun isFieldEnable(isEnabled: Boolean, inputType: Int) {
    onView(withInputType(inputType))
        .check(
            matches(
                if (isEnabled) isEnabled() else not(isEnabled())
            )
        )
}

fun inputText(inputType: Int, text: String) {
    onView(withInputType(inputType))
        .perform(typeText(text), closeSoftKeyboard())
}

fun clearText(inputType: Int) {
    onView(withInputType(inputType))
        .perform(clearText(), closeSoftKeyboard())
}

fun clickOnButtonDialog(resId: Int) {
    onView(withId(resId))
        .check(matches(isDisplayed()))
        .perform(forceClick())
}

private fun forceClick(): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return AllOf.allOf(isClickable(), isEnabled(), isDisplayed())
        }

        override fun getDescription(): String {
            return "force click"
        }

        override fun perform(uiController: UiController, view: View) {
            view.performClick() // perform click without checking view coordinates.
            uiController.loopMainThreadUntilIdle()
        }
    }
}
