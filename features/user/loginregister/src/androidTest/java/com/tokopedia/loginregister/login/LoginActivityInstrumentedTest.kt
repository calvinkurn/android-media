package com.tokopedia.loginregister.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
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
import com.tokopedia.loginfingerprint.utils.crypto.Cryptography
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.PartialRegisterInputUtils.Companion.EMAIL_TYPE
import com.tokopedia.loginregister.common.domain.usecase.DynamicBannerUseCase
import com.tokopedia.loginregister.discover.usecase.DiscoverUseCase
import com.tokopedia.loginregister.login.domain.RegisterCheckUseCase
import com.tokopedia.loginregister.login.domain.StatusFingerprintUseCase
import com.tokopedia.loginregister.login.domain.StatusPinUseCase
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckPojo
import com.tokopedia.loginregister.login.stub.LoginEmailPhoneFragmentStub
import com.tokopedia.loginregister.login.stub.activity.LoginEmailPhoneActivityStub
import com.tokopedia.loginregister.login.view.presenter.LoginEmailPhonePresenter
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialUseCase
import com.tokopedia.loginregister.registerinitial.domain.usecase.RegisterValidationUseCase
import com.tokopedia.loginregister.registerinitial.view.activity.RegisterInitialActivity
import com.tokopedia.loginregister.ticker.domain.usecase.TickerInfoUseCase
import com.tokopedia.sessioncommon.domain.usecase.GetProfileUseCase
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.any
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*


/**
 * Created by Yoris Prayogo on 09/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class LoginActivityInstrumentedTest {

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
    }

    @After
    fun afterSetup(){
        Intents.release()
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

}