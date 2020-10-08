package com.tokopedia.loginregister.login

import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.tokopedia.loginfingerprint.data.preference.FingerprintSetting
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.login.common.LoginIdlingResourceTestRule
import com.tokopedia.loginregister.login.stub.LoginEmailPhoneFragmentStub
import com.tokopedia.loginregister.login.stub.activity.LoginEmailPhoneActivityStub
import com.tokopedia.loginregister.login.stub.response.LoginMockResponse
import com.tokopedia.loginregister.registerinitial.view.activity.RegisterInitialActivity
import com.tokopedia.otp.verification.view.activity.VerificationActivity
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Created by Yoris Prayogo on 09/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class LoginActivityInstrumentedTest {

    private var idlingResource: IdlingResource? = null

    @get:Rule
    val loginIdlingResourceRule = LoginIdlingResourceTestRule()

    @get:Rule
    var mActivityTestRule = ActivityTestRule(LoginEmailPhoneActivityStub::class.java)

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var userSession: UserSessionInterface
    private lateinit var fingerprintSetting: FingerprintSetting
    private lateinit var activity: LoginEmailPhoneActivityStub

    lateinit var fragment: LoginEmailPhoneFragmentStub
    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        Intents.init()
        Dispatchers.setMain(TestCoroutineDispatcher())
        userSession = mActivityTestRule.activity.stubUserSession
        fingerprintSetting = mActivityTestRule.activity.stubFingerprintSetting
        activity = mActivityTestRule.activity
        fragment = activity.setupTestFragment() as LoginEmailPhoneFragmentStub

        idlingResource = LoginIdlingResource.getIdlingResource()
        IdlingRegistry.getInstance().register(idlingResource)
        setupGraphqlMockResponse(LoginMockResponse())
    }

    @After
    fun afterSetup(){
        Intents.release()
        IdlingRegistry.getInstance().unregister(idlingResource)
        mActivityTestRule.finishActivity()
    }

    @Test
    /* Show socmed container if socmed button clicked */
    fun onSocmedBtnClick(){
        onView(allOf(withText(R.string.social_media), withContentDescription(R.string.content_desc_socmed_btn_phone))).perform(click())
        onView(withId(R.id.socmed_container)).check(matches(isDisplayed()))
    }

    @Test
    /* Go to register initial if clicked */
    fun onRegisterFooterSpannableClick(){
        onView(withContentDescription(R.string.content_desc_register_button_phone)).perform(click())
        intended(hasComponent(RegisterInitialActivity::class.java.name))
    }

//    @Test
//    /* Go to register initial if clicked */
//    fun onRegisterToolbarClick(){
//        onView(withId(LoginEmailPhoneFragment.ID_ACTION_REGISTER)).perform(click())
//        intended(hasComponent(RegisterInitialActivity::class.java.name))
//    }

    @Test
    /* show fingerprint button if available */
    fun showFingerprintBtnIfAvailable(){
        fragment.setFingerprintEnable(true)
        fingerprintSetting.registerFingerprint()

        onView(allOf(withId(R.id.fingerprint_btn), withText("Sidik Jari"))).check(matches(isDisplayed()))
    }

    @Test
    /* Go to tokopedia care on click */
    fun goToTokopediaCare (){
        onView(withId(R.id.to_tokopedia_care)).perform(click())
    }

    @Test
    /* Show password field if email exist */
    fun showPasswordField (){
        onView(allOf(withId(R.id.input_email_phone), withContentDescription(R.string.content_desc_input_email_phone))).perform(replaceText("yorisprayogo@gmail.com"))
        onView(withId(R.id.register_btn)).perform(click())
        onView(allOf(withId(R.id.wrapper_password), withContentDescription(R.string.content_desc_wrapper_pass_partial))).check(matches(isDisplayed()))
    }

    @Test
    /* goto forgot password if clicked */
    fun forgotPassClick(){
        onView(allOf(withId(R.id.input_email_phone), withContentDescription(R.string.content_desc_input_email_phone))).perform(replaceText("yorisprayogo@gmail.com"))
        onView(withId(R.id.register_btn)).perform(click())
        onView(withId(R.id.forgot_pass)).perform(click())
//        intended(hasComponent(RouteManager.getIntent(activity, ApplinkConstInternalGlobal.FORGOT_PASSWORD).component))
    }

    @Test
    /* goto verification phone if phone number exist */
    fun goToVerificationPhone(){
        val activityUnderTest: LoginEmailPhoneActivityStub = mActivityTestRule.activity
        activityUnderTest.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))

        onView(allOf(withId(R.id.input_email_phone), withContentDescription(R.string.content_desc_input_email_phone))).perform(replaceText("082242454504"))
        onView(withId(R.id.register_btn)).perform(click())
        intended(hasComponent(VerificationActivity::class.java.name))
    }

}