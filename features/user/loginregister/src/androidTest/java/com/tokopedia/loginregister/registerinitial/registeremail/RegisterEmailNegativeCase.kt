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
import com.tokopedia.loginregister.utils.TextFieldUnifyMatcher
import com.tokopedia.test.application.annotations.UiTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.core.AnyOf.anyOf
import org.hamcrest.core.Is.`is`
import org.junit.Test

@UiTest
class RegisterEmailNegativeCase: RegisterEmailBase() {

    val emptyErrorText = "Harus diisi"
    val lengthLessThan3Text = "Minimum 3 karakter"

    @Test
    /* Disable Wrapper Email on Created */
    fun disableEmailInput() {
        runTest {
            TextFieldUnifyMatcher.isEnabled(`is`(R.id.wrapper_email), `is`(false))
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
            onView(allOf(
                    withId(R.id.text_field_input),
                    isDescendantOfA(withId(R.id.wrapper_name))
                )
            ).perform(clearText())

            onView(anyOf(withText(emptyErrorText)))
                .check(matches(isDisplayed()))
        }
    }

    @Test
    /* Show error if password is empty */
    fun showError_IfPassDeleted() {
        runTest {
            inputPassword("abcdefg123456")
            onView(
                CoreMatchers.allOf(
                    withId(R.id.text_field_input),
                    isDescendantOfA(withId(R.id.wrapper_password))
                )
            ).perform(clearText())

            onView(anyOf(withText(emptyErrorText)))
                .check(matches(isDisplayed()))
        }
    }

    @Test
    /* Show error if name length < 3 */
    fun showError_IfNameLengthLessThan3() {
        runTest {
            inputName("yo")
            onView(anyOf(withText(lengthLessThan3Text)))
                .check(matches(isDisplayed()))
        }
    }

    @Test
    /* Show password hint */
    fun showPasswordHint() {
        runTest {
            inputPassword("abcdef")
            onView(anyOf(withText("minimum 8 karakter")))
                .check(matches(isDisplayed()))
        }
    }

    @Test
    /* Show error when show password icon clicked while password is empty */
    fun showError_IfShowPassClickedWhenEmpty() {
        runTest {
            onView(allOf(
                    withId(R.id.text_field_icon_1),
                    isDescendantOfA(withId(R.id.wrapper_password))
                )
            ).perform(click())

            onView(anyOf(withText(emptyErrorText)))
                .check(matches(isDisplayed()))
        }
    }

}
