package com.tokopedia.loginregister.registerinitial.instrument_test.social_media

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.CassavaTestRuleMatcher
import com.tokopedia.loginregister.common.CassavaTestRuleMatcher.validate
import com.tokopedia.loginregister.common.Event
import com.tokopedia.loginregister.login.behaviour.base.RegisterInitialBase
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class RegisterInitialSocMedTest: RegisterInitialBase() {

    @get:Rule
    var cassavaTestRule = CassavaTestRule()


    @Test
    fun check_failed_register_tracker_google() {
        isDefaultRegisterCheck = false
        registerCheckUseCase.isError = true

        runTest {
            checkRegisterGoogle()
        }

        validate(cassavaTestRule, getAnalyticValidatorListGoogleFailed())
    }

    @Test
    fun check_success_register_tracker_google() {

        runTest {
            checkRegisterGoogle()
        }

        validate(cassavaTestRule, getAnalyticValidatorListGoogleSuccess())
    }

    private fun checkRegisterGoogle() {
        intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, Intent()))
        Thread.sleep(1000)

        onView(ViewMatchers.withId(R.id.socmed_btn))
            .perform(click())

        onView(ViewMatchers.withText("Google"))
            .perform(click())
    }

    private fun getAnalyticValidatorListGoogleSuccess(): List<Map<String, String>> {
        return listOf(
            CassavaTestRuleMatcher.getAnalyticValidator(
                "clickRegister",
                "register page",
                "click on button google",
                "click"
            ),
            //Cannot mock GoogleSignInStatusCodes
        )
    }

    private fun getAnalyticValidatorListGoogleFailed(): List<Map<String, String>> {
        return listOf(
            CassavaTestRuleMatcher.getAnalyticValidator(
                "clickRegister",
                "register page",
                "click on button google",
                "click"
            )
            //Cannot mock GoogleSignInStatusCodes
        )
    }

    @After
    fun finishTest() {
        isDefaultRegisterCheck = true
    }
}