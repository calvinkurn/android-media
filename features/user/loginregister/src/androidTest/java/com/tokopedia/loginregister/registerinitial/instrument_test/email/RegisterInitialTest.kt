package com.tokopedia.loginregister.registerinitial.instrument_test.email

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.CassavaTestRuleMatcher
import com.tokopedia.loginregister.common.CassavaTestRuleMatcher.validate
import com.tokopedia.loginregister.common.Event
import com.tokopedia.loginregister.login.behaviour.activity.VerificationActivityStub
import com.tokopedia.loginregister.login.behaviour.base.RegisterInitialBase
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckPojo
import com.tokopedia.sessioncommon.data.profile.ProfileInfo
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.test.application.annotations.UiTest
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UiTest
@RunWith(AndroidJUnit4::class)
@LargeTest
class RegisterInitialTest: RegisterInitialBase() {

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    private val emailNotRegistered = "kelvin.saputra+qa99@tokopedia.com"

    @Test
    fun check_register_email_success_tracker() {
        //Given
        isDefaultRegisterCheck = false
        val data = RegisterCheckData(
            isExist = false ,
            isPending = false,
            userID = "0",
            registerType = "email",
            view = emailNotRegistered
        )
        registerCheckUseCase.response = RegisterCheckPojo(data)

        val profileInfo = ProfileInfo(userId = "123456", fullName = "Kelvin Saputra")
        val profilePojo = ProfilePojo(profileInfo)
        getProfileUseCaseStub.response = profilePojo

        //When
        runTest {
            checkRegisterEmail()
        }

        //Then
        validate(cassavaTestRule, getAnalyticValidatorListSuccess())
    }

    @Test
    fun check_register_email_failed_tracker() {
        //Given
        isDefaultRegisterCheck = false
        registerCheckUseCase.isError = true

        //When
        runTest {
            checkRegisterEmail()
        }

        //Then
        validate(cassavaTestRule, getAnalyticValidatorListFailed())
    }

    private fun checkRegisterEmail() {
        Thread.sleep(1000)
        setupMockOtpIntent()

        onView(withId(R.id.input_email_phone))
                .perform(replaceText(""))
                .perform(typeText(emailNotRegistered))

        onView(withId(R.id.register_btn))
                .perform(click())
    }

    private fun setupMockOtpIntent() {
        val testIntent = Intent()
        testIntent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, emailNotRegistered)
        testIntent.putExtra(ApplinkConstInternalGlobal.PARAM_TOKEN, "testToken")
        testIntent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, "testSource")
        Intents.intending(IntentMatchers.hasComponent(VerificationActivityStub::class.java.name))
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, testIntent))
    }

    private fun getAnalyticValidatorListSuccess(): List<Map<String, String>> {
        return listOf(
            CassavaTestRuleMatcher.getAnalyticValidator(
                "clickRegister",
                "register page",
                "click on button daftar - email",
                "click"
            )
        )
    }

    private fun getAnalyticValidatorListFailed(): List<Map<String, String>> {
        return listOf(
            CassavaTestRuleMatcher.getAnalyticValidator(
                "clickRegister",
                "register page",
                "click on button daftar",
                "click"
            ),
            CassavaTestRuleMatcher.getAnalyticValidator(
                "clickRegister",
                "register page",
                "click on button daftar",
                "failed - ${Event.ANY}"
            )
        )
    }

    @After
    fun finishTest() {
        isDefaultRegisterCheck = true
    }
}