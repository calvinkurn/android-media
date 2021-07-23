package com.tokopedia.loginregister.login.behaviour.case.registeremail

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.login.behaviour.base.RegisterEmailBase
import org.hamcrest.CoreMatchers
import org.hamcrest.core.AnyOf.anyOf
import org.junit.Test

class RegisterEmailNegativeCase: RegisterEmailBase() {

    val emptyErrorText = "Harus diisi"
    val lengthLessThan3Text = "Minimum 3 karakter"

    @Test
    /* Disable Wrapper Email on Created */
    fun disableEmailInput() {
        runTest {
            shouldBeDisabled(R.id.wrapper_email)
        }
    }

    @Test
    /* Disable button "Daftar" when input name is empty */
    fun disableNextButton_ifNameEmpty() {
        runTest {
            inputName("")
            inputPassword("abcdefg12345")
            shouldBeDisabled(R.id.register_button)
        }
    }

    @Test
   /* Disable button "Daftar" when input password is empty */
    fun disableNextButton_ifPwEmpty() {
        runTest {
            inputName("Yoris Prayogo")
            inputPassword("")
            shouldBeDisabled(R.id.register_button)
        }
    }

    @Test
    /* Disable button "Daftar" when password length is less than 8 character */
    fun disableNextButton_ifPwLengthLessThan8() {
        runTest {
            inputName("Yoris Prayogo")
            inputPassword("1234567")
            shouldBeDisabled(R.id.register_button)
        }
    }

    @Test
    /* Enable Email when passing empty params */
    fun enableEmailIfEmptyParams() {
        runTest {
            shouldBeEnabled(R.id.wrapper_email)
        }
    }

    @Test
    /* Show error if name is empty */
    fun showError_IfNameDeleted() {
        runTest {
            inputName("Yoris Prayogo")
            Espresso.onView(
                CoreMatchers.allOf(
                    ViewMatchers.withId(R.id.text_field_input),
                    ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.wrapper_name))
                )
            ).perform(ViewActions.clearText())

            Espresso.onView(anyOf(ViewMatchers.withText(emptyErrorText)))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
    }

    @Test
    /* Show error if password is empty */
    fun showError_IfPassDeleted() {
        runTest {
            inputPassword("abcdefg123456")
            Espresso.onView(
                CoreMatchers.allOf(
                    ViewMatchers.withId(R.id.text_field_input),
                    ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.wrapper_password))
                )
            ).perform(ViewActions.clearText())

            Espresso.onView(anyOf(ViewMatchers.withText(emptyErrorText)))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
    }

    @Test
    /* Show error if name length < 3 */
    fun showError_IfNameLengthLessThan3() {
        runTest {
            inputName("yo")
            Espresso.onView(
                CoreMatchers.allOf(
                    ViewMatchers.withId(R.id.text_field_input),
                    ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.wrapper_name))
                )
            ).perform(ViewActions.clearText())

            Espresso.onView(anyOf(ViewMatchers.withText(lengthLessThan3Text)))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
    }

    @Test
    /* Show password hint */
    fun showPasswordHint() {
        runTest {
            inputPassword("abcdef")
            Espresso.onView(
                CoreMatchers.allOf(
                    ViewMatchers.withId(R.id.text_field_input),
                    ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.wrapper_password))
                )
            ).perform(ViewActions.clearText())

            Espresso.onView(anyOf(ViewMatchers.withText("minimum 8 karakter")))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
    }

    @Test
    /* Show error when show password icon clicked while password is empty */
    fun showError_IfShowPassClickedWhenEmpty() {
        runTest {
            Espresso.onView(
                CoreMatchers.allOf(
                    ViewMatchers.withId(R.id.text_field_icon_1),
                    ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.wrapper_password))
                )
            ).perform(ViewActions.click())
            Espresso.onView(anyOf(ViewMatchers.withText(emptyErrorText)))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
    }

}