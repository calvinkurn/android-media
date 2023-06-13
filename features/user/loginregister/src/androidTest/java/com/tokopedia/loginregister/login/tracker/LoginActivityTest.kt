package com.tokopedia.loginregister.login.tracker

import android.app.Activity
import android.app.Instrumentation
import android.text.InputType
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withInputType
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.CassavaTestRuleMatcher.getAnalyticValidator
import com.tokopedia.loginregister.common.CassavaTestRuleMatcher.validate
import com.tokopedia.loginregister.login.behaviour.base.LoginBase
import com.tokopedia.loginregister.registerinitial.view.activity.RegisterInitialActivity
import com.tokopedia.loginregister.utils.LoginSocmedTestHelper
import com.tokopedia.test.application.annotations.CassavaTest
import org.hamcrest.CoreMatchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@CassavaTest
@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginActivityTest: LoginBase() {

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    @Test
    fun validateLoginEmailTracker() {
        //When
        runTest {
            simulateLoginEmail()
        }

        //Then
        val query = listOf(
            getAnalyticValidator(
                "clickLogin",
                "login page",
                "click on button selanjutnya - email",
                "success"
            ),
            getAnalyticValidator(
                "clickLogin",
                "login page",
                "click on button masuk",
                "click"
            )
        )
        validate(cassavaTestRule, query)
    }

    @Test
    fun validateLoginPhoneTracker() {
        //When
        runTest {
            simulateLoginPhone()
        }

        //Then
        val query = listOf(
            getAnalyticValidator(
                "clickLogin",
                "login page",
                "click on button masuk",
                "click"
            ),
            getAnalyticValidator(
                "clickLogin",
                "login page",
                "click on masuk dengan phone number",
                "click - login"
            )
        )
        validate(cassavaTestRule, query)
    }

    @Test
    fun validateForgotPassClicked() {
        //When
        runTest {
            simulateClickForgotPass()
        }

        //Then
        val query = listOf(
            getAnalyticValidator(
                "clickAccount",
                "widget login page",
                "click on button lupa kata sandi",
                "widget butuh bantuan"
            )
        )
        validate(cassavaTestRule, query)
    }

    @Test
    fun validateRegisterClick() {
        //When
        runTest {
            simulateClickRegister()
        }

        //Then
        val query = listOf(
            getAnalyticValidator(
                "clickLogin",
                "login page",
                "click daftar bottom",
                ""
            )
        )
        validate(cassavaTestRule, query)
    }

    @Test
    fun validateLoginSocmedTracker() {
        //When
        runTest {
            simulateLoginSocmed()
        }

        //Then
        val query = listOf(
            getAnalyticValidator(
                "clickLogin",
                "login page",
                "click on button socmed",
                ""
            ),
            getAnalyticValidator(
                "clickLogin",
                "login page",
                "click on masuk dengan google",
                "click"
            )
        )
        validate(cassavaTestRule, query)
    }

    fun simulateClickForgotPass() {
        intending(hasData(ApplinkConstInternalUserPlatform.FORGOT_PASSWORD)).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        clickForgotPass()
    }

    fun simulateLoginSocmed() {
        intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        LoginSocmedTestHelper.clickSocmedButton()
        LoginSocmedTestHelper.clickGoogleLogin()

        LoginSocmedTestHelper.clickSocmedButton()
    }

    fun simulateLoginEmail() {
        inputEditText("yoris.prayogo+3@tokopedia.com")
        clickSubmit()
        inputPassword("qwerty12345")
        clickSubmit()
    }

    fun simulateClickRegister(){
        intending(hasComponent(RegisterInitialActivity::class.java.getName())).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        clickRegisterBottom()
    }

    fun simulateLoginPhone() {
        inputEditText("082242454504")
        clickSubmit()
    }

    private fun inputEditText(value: String) {
        val viewInteraction = Espresso.onView(withInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(typeText(value), closeSoftKeyboard())
    }

    private fun clickRegisterBottom(){
        Espresso.onView(CoreMatchers.allOf(withId(R.id.register_button), ViewMatchers.withContentDescription(R.string.content_desc_register_button_phone)))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(click())
    }

    private fun clickChangePassword() {
        val viewInteraction = Espresso.onView(withId(R.id.change_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(click())
    }
}
