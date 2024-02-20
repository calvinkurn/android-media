package com.tokopedia.loginregister.login.behaviour.base

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withInputType
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.scp.auth.GotoSdk
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.ViewIdGenerator.waitOnView
import com.tokopedia.loginregister.di.FakeActivityComponentFactory
import com.tokopedia.loginregister.login.di.ActivityComponentFactory
import com.tokopedia.loginregister.login.view.activity.LoginActivity
import com.tokopedia.loginregister.registerinitial.const.RegisterConstants
import com.tokopedia.loginregister.stub.FakeGraphqlRepository
import com.tokopedia.loginregister.stub.usecase.LoginTokenUseCaseStub
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject
import com.tokopedia.header.R as headerR

open class LoginBase : LoginRegisterBase() {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        LoginActivity::class.java,
        false,
        false
    )

    @Inject
    lateinit var fakeRepo: FakeGraphqlRepository

    @Inject
    lateinit var loginTokenUseCaseStub: LoginTokenUseCaseStub

    private val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext

    @Before
    open fun before() {
        val fakeComponentFactory = FakeActivityComponentFactory()
        ActivityComponentFactory.instance = fakeComponentFactory
        fakeComponentFactory.loginComponent.inject(this)
        GotoSdk.init(applicationContext as BaseMainApplication)
    }

    @After
    fun tear() {
        activityTestRule.finishActivity()
    }

    protected fun setupLoginActivity(
        intentModifier: (Intent) -> Unit = {}
    ) {
        val intent = Intent()

        intentModifier(intent)
        activityTestRule.launchActivity(intent)
    }

    fun runTest(test: () -> Unit) {
        mockkStatic(FirebaseCrashlytics::class)
        every {
            FirebaseCrashlytics.getInstance().recordException(any())
        } returns mockk(relaxed = true)

        setupLoginActivity()
        clearEmailInput()
        test.invoke()
    }

    fun clickTopRegister() {
        val viewInteraction = onView(withId(headerR.id.actionTextID)).check(matches(isDisplayed()))
        viewInteraction.perform(click())
    }

    fun clickBottomRegister() {
        val viewInteraction = onView(withId(R.id.register_button)).check(matches(isDisplayed()))
        viewInteraction.perform(click())
    }

    fun clickForgotPass() {
        val viewInteraction = onView(withId(R.id.need_help)).check(matches(isDisplayed()))
        viewInteraction.perform(click())

        waitOnView(withId(R.id.ub_forgot_password)).perform(click())
    }

    fun clickUbahButton() {
        onView(withId(R.id.change_button)).check(matches(ViewMatchers.isDisplayed()))
            .perform(click())
    }

    fun inputPassword(value: String) {
        val viewInteraction = onView(
            withInputType(
                InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
            )
        ).check(matches(isDisplayed()))
        viewInteraction.perform(ViewActions.typeText(value))
    }

    fun isEmailExtensionDisplayed() {
        shouldBeDisplayed(R.id.emailExtension)
    }

    fun isEmailExtensionDismissed() {
        shouldBeHidden(R.id.emailExtension)
    }

    protected fun mockOtpPageRegisterEmail() {
        Intents.intending(IntentMatchers.hasData(UriUtil.buildUri(ApplinkConstInternalUserPlatform.COTP, RegisterConstants.OtpType.OTP_TYPE_REGISTER.toString()).toString())).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                Intent().apply {
                    putExtras(
                        Bundle().apply {
                            putString(ApplinkConstInternalGlobal.PARAM_UUID, "abc1234")
                            putString(ApplinkConstInternalGlobal.PARAM_TOKEN, "abv1234")
                            putString(ApplinkConstInternalGlobal.PARAM_EMAIL, "yoris.prayogo@gmail.com")
                        }
                    )
                }
            )
        )
    }
}
