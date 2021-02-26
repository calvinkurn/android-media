package com.tokopedia.loginregister.login

import android.widget.EditText
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.loginfingerprint.data.preference.FingerprintSetting
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.TkpdIdlingResource
import com.tokopedia.loginregister.common.view.PartialRegisterInputView
import com.tokopedia.loginregister.login.stub.LoginEmailPhoneFragmentStub
import com.tokopedia.loginregister.login.stub.PartialInputTextView
import com.tokopedia.loginregister.login.stub.activity.LoginEmailPhoneActivityStub
import com.tokopedia.loginregister.login.stub.response.LoginMockResponse
import com.tokopedia.test.application.annotations.UiAnalyticsTest
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.unifycomponents.TextFieldUnify
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
@UiAnalyticsTest
class LoginActivityInstrumentedTest {

    val trackerPath = "tracker/user/loginregister/login_register_p1.json"

    private var tkpdIdlingResource: IdlingResource? = null

    @get:Rule
    var mActivityTestRule = ActivityTestRule(LoginEmailPhoneActivityStub::class.java)

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var userSession: UserSessionInterface
    private lateinit var fingerprintSetting: FingerprintSetting
    private lateinit var activity: LoginEmailPhoneActivityStub

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    lateinit var fragment: LoginEmailPhoneFragmentStub

    private var email = "yoris.prayogo+3@tokopedia.com"
    private var phone = "082242454504"

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        Intents.init()
        Dispatchers.setMain(TestCoroutineDispatcher())
        gtmLogDBSource.deleteAll().subscribe()
        userSession = mActivityTestRule.activity.stubUserSession
        fingerprintSetting = mActivityTestRule.activity.stubFingerprintSetting
        activity = mActivityTestRule.activity
        fragment = activity.setupTestFragment() as LoginEmailPhoneFragmentStub

        val idlingResource = TkpdIdlingResource("LOGIN")
        tkpdIdlingResource = idlingResource.countingIdlingResource

        IdlingRegistry.getInstance().register(tkpdIdlingResource)
        setupGraphqlMockResponse(LoginMockResponse())
        mActivityTestRule.launchActivity(activity.intent)
    }

    @After
    fun afterSetup() {
        Intents.release()
        IdlingRegistry.getInstance().unregister(tkpdIdlingResource)
        mActivityTestRule.finishActivity()
    }

    @Test
    fun validateTracker() {
        onLoginViaEmail()
        forgotPassClick()
        clickUbahButton()
        onLoginViaPhone()
        onRegisterFooterSpannableClick()
        clickGoogleLogin()

        Thread.sleep(500)

        assertThat(
            getAnalyticsWithQuery(gtmLogDBSource, context, trackerPath),
            hasAllSuccess()
        )
    }

    fun clickOnButtonMasuk(){
        mActivityTestRule.runOnUiThread {
            fragment.partialActionButton.performClick()
        }
    }

    /* Show socmed container if socmed button clicked */
    fun onSocmedBtnClick() {
        onView(allOf(withText(R.string.social_media), withContentDescription(R.string.content_desc_socmed_btn_phone))).perform(click())
        onView(withId(R.id.socmed_container)).check(matches(isDisplayed()))
    }

    fun clickGoogleLogin(){
        onSocmedBtnClick()
        Thread.sleep(600)
        onView(withText("Google"))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()))
                .perform(click())

    }

    fun clickFacebookLogin(){
        onSocmedBtnClick()
        Thread.sleep(600)
        onView(withText("Facebook"))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()))
                .perform(click())
    }

    /* click login email */
    fun onLoginViaEmail() {
        Thread.sleep(600)
        onView(allOf(withId(R.id.input_email_phone), withContentDescription(R.string.content_desc_input_email_phone))).perform(PartialInputTextView())
        onView(allOf(withId(R.id.register_btn), withText("Selanjutnya"), withContentDescription("button submit"))).perform(click())
    }

    fun clickUbahButton(){
        onView(withId(R.id.change_button)).perform(click())
    }

    /* click login email */
    fun onLoginViaPhone() {
        onView(allOf(withId(R.id.input_email_phone), withContentDescription(R.string.content_desc_input_email_phone))).perform(replaceText(phone))
        onView(withId(R.id.register_btn)).perform(click())
    }

    fun setInputEmailPhone(){
        val partial = fragment.view?.findViewById<PartialRegisterInputView>(R.id.login_input_view)
        mActivityTestRule.runOnUiThread {
            partial?.findViewById<EditText>(R.id.input_email_phone)?.setText(email)
        }
    }

    fun loginEmailPassword(){
        setInputEmailPhone()
        Thread.sleep(1000)
        val partial = fragment.view?.findViewById<TextFieldUnify>(R.id.wrapper_password)
        mActivityTestRule.runOnUiThread {
            partial?.textFieldInput?.setText("test1234")
        }
        Thread.sleep(1000)
        clickOnButtonMasuk()
    }

    /* goto forgot password if clicked */
    fun forgotPassClick() {
        Thread.sleep(1000)
        onView(allOf(withId(R.id.forgot_pass), withContentDescription(R.string.content_desc_forgot_pass))).perform(click())
    }

    fun onRegisterFooterSpannableClick(){
        onView(allOf(withId(R.id.register_button), withContentDescription(R.string.content_desc_register_button_phone))).perform(click())
    }
}