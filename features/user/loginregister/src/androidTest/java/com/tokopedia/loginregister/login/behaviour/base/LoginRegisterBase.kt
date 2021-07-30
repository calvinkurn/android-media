package com.tokopedia.loginregister.login.behaviour.base

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.loginregister.R
import org.hamcrest.CoreMatchers

open class LoginRegisterBase {

    fun clickSubmit(){
        val viewInteraction = Espresso.onView(ViewMatchers.withId(R.id.register_btn))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(ViewActions.click())
    }

    fun clearEmailInput() {
        val viewInteraction = Espresso.onView(ViewMatchers.withId(R.id.input_email_phone))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(ViewActions.clearText())
    }

    fun inputEmailOrPhone(value: String) {
        val viewInteraction = Espresso.onView(ViewMatchers.withId(R.id.input_email_phone))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(ViewActions.typeText(value))
    }

    fun shouldBeDisabled(id: Int) {
        Espresso.onView(ViewMatchers.withId(id))
            .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isEnabled())))
    }

    fun shouldBeDisplayed(id: Int) {
        Espresso.onView(ViewMatchers.withId(id))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    fun shouldBeHidden(id: Int) {
        Espresso.onView(ViewMatchers.withId(id))
            .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
    }

    fun isDisplayingGivenText(id: Int, givenText: String) {
        Espresso.onView(ViewMatchers.withId(id))
            .check(ViewAssertions.matches(ViewMatchers.withText(givenText))).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }

    fun isTextInputHasError(id: Int, errorText: String) {
        Espresso.onView(ViewMatchers.withId(id))
            .check(ViewAssertions.matches(ViewMatchers.hasErrorText(errorText))).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }

    fun isDialogDisplayed(text: String) {
        Espresso.onView(ViewMatchers.withText(text))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    fun deleteEmailOrPhoneInput() {
        val viewInteraction = Espresso.onView(ViewMatchers.withId(R.id.input_email_phone))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(ViewActions.clearText())
    }

    fun clickTopLogin() {
        val viewInteraction = Espresso.onView(ViewMatchers.withId(R.id.actionTextID))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(ViewActions.click())
    }

    fun clickSocmedButton() {
        Espresso.onView(ViewMatchers.withId(R.id.socmed_btn))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(ViewActions.click())
    }

}