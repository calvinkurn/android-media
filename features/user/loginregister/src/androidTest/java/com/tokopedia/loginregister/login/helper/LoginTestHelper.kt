package com.tokopedia.loginregister.login.helper

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.view.View
import android.widget.EditText
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingPolicies
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.google.android.material.tabs.TabLayout
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.unifycomponents.TabsUnify
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf.allOf
import java.util.concurrent.TimeUnit

class LoginTestHelper {

    fun <T : Activity> performClose(activityTestRule: ActivityTestRule<T>) {
        activityTestRule.finishActivity()
    }

    fun <T : Activity> restartActivity(activityTestRule: ActivityTestRule<T>) {
        activityTestRule.runOnUiThread {
            activityTestRule.activity.recreate()
        }
    }

    fun clickAction(idView: Int) {
        onView(withId(idView)).check(ViewAssertions.matches(isDisplayed()))
                .perform(ViewActions.click())
    }

    fun selectTabAtPosition(tabIndex: Int): ViewAction {
        return object : ViewAction {
            override fun getDescription() = "with tab at index $tabIndex"

            override fun getConstraints() = allOf(isDisplayed(), isAssignableFrom(TabsUnify::class.java))

            override fun perform(uiController: UiController, view: View) {
                val tabsUnify = view as TabsUnify
                val tabAtIndex: TabLayout.Tab = tabsUnify.tabLayout.getTabAt(tabIndex)
                        ?: throw PerformException.Builder()
                                .withCause(Throwable("No tab at index $tabIndex"))
                                .build()

                tabAtIndex.select()
            }
        }
    }

    infix fun assertTest(action: LoginTestHelper.() -> Unit) = LoginTestHelper().apply(action)

    fun GtmLogDBSource.finishTest() {
        deleteAll().subscribe()
    }

    fun validate(gtmLogDbSource: GtmLogDBSource,
                 targetContext: Context,
                 fileName: String) {
        assertThat(getAnalyticsWithQuery(gtmLogDbSource, targetContext, fileName),
                hasAllSuccess())
    }

    companion object {
        const val LOGIN_EMAIL_P1 = "tracker/user/loginregister/email/login_email_p1.json"
        const val LOGIN_PHONE_P1 = "tracker/user/loginregister/email/login_phone_p1.json"
        const val LOGIN_FORGOT_PASS_P1 = "tracker/user/loginregister/email/login_forgot_pass_p1.json"
        const val LOGIN_EMAIL_REGISTER_P1 = "tracker/user/loginregister/email/login_email_register_p1.json"
        const val OTP_LOGIN_PHONE_NUMBER = "tracker/user/otp/otp_login_phone_p1.json"
    }
}

fun pauseTestFor(milliseconds: Long) {
    try {
        Thread.sleep(milliseconds)
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }
}

fun actionTest(action: LoginTestHelper.() -> Unit) = LoginTestHelper().apply(action)

fun intendingIntent() {
    Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
}

fun setUpTimeoutIdlingResource() {
    IdlingPolicies.setMasterPolicyTimeout(5, TimeUnit.MINUTES)
    IdlingPolicies.setIdlingResourceTimeout(5, TimeUnit.MINUTES)
}

fun fakeLogin() {
    InstrumentationAuthHelper.loginInstrumentationTestUser1()
}

fun clearLogin() {
    InstrumentationAuthHelper.clearUserSession()
}

fun waitForData() {
    Thread.sleep(1000L)
}

fun waitForTrackerSent() {
    Thread.sleep(1000L)
}

fun forceTypeText(text: String): ViewAction {
    return object : ViewAction {
        override fun getDescription(): String {
            return "force type text"
        }

        override fun getConstraints(): Matcher<View> {
            return allOf(isEnabled())
        }

        override fun perform(uiController: UiController?, view: View?) {
            (view as? EditText)?.append(text)
            uiController?.loopMainThreadUntilIdle()
        }
    }
}
