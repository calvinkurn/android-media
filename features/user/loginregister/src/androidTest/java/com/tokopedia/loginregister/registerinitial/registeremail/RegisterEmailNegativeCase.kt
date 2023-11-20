package com.tokopedia.loginregister.registerinitial.registeremail

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.registerinitial.RegisterEmailBase
import com.tokopedia.test.application.annotations.UiTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.core.AnyOf.anyOf
import org.junit.Test

@UiTest
class RegisterEmailNegativeCase : RegisterEmailBase() {

    private val emptyErrorText = "Harus diisi"
    private val lengthLessThan3Text = "Minimum 3 karakter"

    @Test
    fun disableNextButton_ifNameEmpty() {
        runTest {
            emailInputIsEnabled(R.id.wrapper_email)
            /* Disable button "Daftar" when input name is empty */
            inputName("")
            inputPassword("abcdefg12345")
            shouldBeDisabled(R.id.register_button)

            inputName("Yoris Prayogo")
            inputPassword("")
            shouldBeDisabled(R.id.register_button)

            /* Disable button "Daftar" when password length is less than 8 character */
            inputName("Yoris Prayogo")
            inputPassword("1234567")
            onView(anyOf(withText("minimum 8 karakter"))).check(matches(isDisplayed()))
            shouldBeDisabled(R.id.register_button)

            /* Show error if name length < 3 */
            inputName("yo")
            onView(anyOf(withText(lengthLessThan3Text))).check(matches(isDisplayed()))

            /* Show error if password is empty */
            inputPassword("abcdefg123456")
            onView(
                allOf(
                    withId(com.tokopedia.unifycomponents.R.id.text_field_input),
                    isDescendantOfA(withId(R.id.wrapper_password))
                )
            ).perform(clearText())

            onView(allOf(withText(emptyErrorText), isDescendantOfA(withId(R.id.wrapper_password))))
                .check(matches(isDisplayed()))

            /* Show error if name is empty */
            inputName("Yoris Prayogo")
            onView(
                allOf(
                    withId(com.tokopedia.unifycomponents.R.id.text_field_input),
                    isDescendantOfA(withId(R.id.wrapper_name))
                )
            ).perform(clearText())

            onView(allOf(withText(emptyErrorText), isDescendantOfA(withId(R.id.wrapper_name))))
                .check(matches(isDisplayed()))
        }
    }

    @Test
    /* Show error when show password icon clicked while password is empty */
    fun showError_IfShowPassClickedWhenEmpty() {
        runTest {
            onView(
                allOf(
                    withId(com.tokopedia.unifycomponents.R.id.text_field_icon_1),
                    isDescendantOfA(withId(R.id.wrapper_password))
                )
            ).perform(click())

            onView(withText(emptyErrorText))
                .check(matches(isDisplayed()))
        }
    }

}
