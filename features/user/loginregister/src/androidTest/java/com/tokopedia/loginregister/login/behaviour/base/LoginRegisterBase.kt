package com.tokopedia.loginregister.login.behaviour.base

import android.text.InputType
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.loginregister.R
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers.not

open class LoginRegisterBase {

    fun clickSubmit(){
        val viewInteraction = onView(withId(R.id.register_btn))
            .check(matches(isDisplayed()))
        viewInteraction.perform(ViewActions.click())
    }

    fun clearEmailInput() {
        val viewInteraction = onView(withInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE))
            .check(matches(isDisplayed()))
        viewInteraction.perform(ViewActions.clearText())
    }

    fun inputEmailOrPhone(value: String) {
        val viewInteraction = onView(withInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE))
            .check(matches(isDisplayed()))
        viewInteraction.perform(clearText(), typeText(value))
    }

    fun shouldBeDisabledWithInputType(inputType: Int) {
        onView(withInputType(inputType))
            .check(matches(not(isEnabled())))
    }

    fun shouldBeDisabled(id: Int) {
        onView(withId(id))
            .check(matches(not(isEnabled())))
    }

    fun shouldBeDisplayed(id: Int) {
        onView(withId(id))
            .check(matches(isDisplayed()))
    }

    fun shouldBeHidden(id: Int) {
        onView(withId(id))
            .check(matches(not(isDisplayed())))
    }

    fun isDisplayingGivenText(givenText: String) {
        onView(withText(givenText))
            .check(matches(withText(givenText))).check(
                matches(isDisplayed())
            )
    }

    fun isDisplayingGivenText(id: Int, givenText: String) {
        onView(withId(id))
            .check(matches(withText(givenText))).check(
            matches(isDisplayed())
        )
    }

    fun isDisplayingSubGivenText(givenText: String) {
        onView(withText(givenText))
            .check(matches(withSubstring(givenText))).check(
                matches(isDisplayed())
            )
    }

    fun isDisplayingSubGivenText(id: Int, givenText: String) {
        onView(withId(id))
            .check(matches(withSubstring(givenText))).check(
                matches(isDisplayed())
            )
    }

    fun isTextInputHasError(id: Int, errorText: String) {
        onView(withId(id))
            .check(matches(hasErrorText(errorText))).check(
            matches(isDisplayed())
        )
    }

    fun isDialogDisplayed(text: String) {
        onView(withText(text))
            .inRoot(RootMatchers.isDialog())
            .check(matches(isDisplayed()))
    }

    fun deleteEmailOrPhoneInput() {
        val viewInteraction = onView(withInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE))
            .check(matches(isDisplayed()))
        viewInteraction.perform(clearText())
    }

    fun clickTopLogin() {
        val viewInteraction = onView(withId(com.tokopedia.header.R.id.actionTextID))
            .check(matches(isDisplayed()))
        viewInteraction.perform(ViewActions.click())
    }

    fun clickSocmedButton() {
        onView(withId(R.id.socmed_btn))
            .check(matches(isDisplayed()))
            .perform(ViewActions.click())
    }

}
