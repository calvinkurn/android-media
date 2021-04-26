package com.tokopedia.loginregister.login

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.facebook.FacebookSdk
import com.google.firebase.FirebaseApp
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.login.helper.LoginSocmedTestHelper
import com.tokopedia.loginregister.login.helper.LoginSocmedTestHelper.Companion.LOGIN_SOCMED_TRACKER
import com.tokopedia.loginregister.login.helper.LoginTestHelper.Companion.LOGIN_EMAIL_P1
import com.tokopedia.loginregister.login.helper.LoginTestHelper.Companion.LOGIN_EMAIL_REGISTER_P1
import com.tokopedia.loginregister.login.helper.LoginTestHelper.Companion.LOGIN_FORGOT_PASS_P1
import com.tokopedia.loginregister.login.helper.LoginTestHelper.Companion.LOGIN_PHONE_P1
import com.tokopedia.loginregister.login.helper.actionTest
import com.tokopedia.loginregister.login.helper.waitForData
import com.tokopedia.loginregister.login.stub.response.LoginMockResponse
import com.tokopedia.loginregister.login.view.activity.LoginActivity
import com.tokopedia.loginregister.registerinitial.view.activity.RegisterInitialActivity
import com.tokopedia.managepassword.forgotpassword.view.activity.ForgotPasswordActivity
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(targetContext)

    @get:Rule
    var activityRule: IntentsTestRule<LoginActivity> = object : IntentsTestRule<LoginActivity>(LoginActivity::class.java) {

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            FacebookSdk.sdkInitialize(InstrumentationRegistry.getInstrumentation().targetContext)
            FirebaseApp.initializeApp(InstrumentationRegistry.getInstrumentation().targetContext)
            setupGraphqlMockResponse(LoginMockResponse())
        }

        override fun getActivityIntent(): Intent {
            return Intent(targetContext, LoginActivity::class.java)
        }

        override fun afterActivityLaunched() {
            super.afterActivityLaunched()
            waitForData()
        }
    }

    @Before
    fun setup() {
        gtmLogDBSource.deleteAll().toBlocking().first()
    }

    @Test
    fun validateLoginEmailTracker() {
        actionTest {
            simulateLoginEmail()
        } assertTest {
            performClose(activityRule)
            validate(gtmLogDBSource, targetContext, LOGIN_EMAIL_P1)
            gtmLogDBSource.finishTest()
        }
    }

    @Test
    fun validateLoginPhoneTracker() {
        actionTest {
            simulateLoginPhone()
        } assertTest {
            performClose(activityRule)
            validate(gtmLogDBSource, targetContext, LOGIN_PHONE_P1)
            gtmLogDBSource.finishTest()
        }
    }

    @Test
    fun validateForgotPassClicked() {
        actionTest {
            simulateClickForgotPass()
        } assertTest {
            performClose(activityRule)
            validate(gtmLogDBSource, targetContext, LOGIN_FORGOT_PASS_P1)
            gtmLogDBSource.finishTest()
        }
    }

    @Test
    fun validateRegisterClick() {
        actionTest {
            simulateClickRegister()
        } assertTest {
            performClose(activityRule)
            validate(gtmLogDBSource, targetContext, LOGIN_EMAIL_REGISTER_P1)
            gtmLogDBSource.finishTest()
        }
    }

    @Test
    fun validateLoginSocmedTracker() {
        actionTest {
            simulateLoginSocmed()
        } assertTest {
            performClose(activityRule)
            validate(gtmLogDBSource, targetContext, LOGIN_SOCMED_TRACKER)
            gtmLogDBSource.finishTest()
        }
    }

    fun simulateClickForgotPass() {
        intending(hasComponent(ForgotPasswordActivity::class.java.name)).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        clickForgotPass()
    }

    fun simulateLoginSocmed() {
        intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        LoginSocmedTestHelper.clickSocmedButton()
        LoginSocmedTestHelper.clickGoogleLogin()
        LoginSocmedTestHelper.clickSocmedButton()
        LoginSocmedTestHelper.clickFacebookLogin()
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
        val viewInteraction = Espresso.onView(withId(R.id.input_email_phone)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(typeText(value))
    }

    private fun inputPassword(value: String) {
        val viewInteraction = Espresso.onView(withId(R.id.text_field_input)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(typeText(value))
    }

    private fun clickSubmit(){
        val viewInteraction = Espresso.onView(withId(R.id.register_btn)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(click())
    }

    private fun clickForgotPass() {
        val viewInteraction = Espresso.onView(withId(R.id.forgot_pass)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(click())
    }

    private fun clickRegisterBottom(){
        Espresso.onView(Matchers.allOf(withId(R.id.register_button), ViewMatchers.withContentDescription(R.string.content_desc_register_button_phone)))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(click())
    }

    private fun clickChangePassword() {
        val viewInteraction = Espresso.onView(withId(R.id.change_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(click())
    }
}
