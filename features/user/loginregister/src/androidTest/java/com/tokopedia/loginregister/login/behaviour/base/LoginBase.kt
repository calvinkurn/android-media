package com.tokopedia.loginregister.login.behaviour.base

import android.content.Intent
import android.text.InputType
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withInputType
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.ViewIdGenerator.waitOnView
import com.tokopedia.loginregister.di.FakeActivityComponentFactory
import com.tokopedia.loginregister.login.di.ActivityComponentFactory
import com.tokopedia.loginregister.login.view.activity.LoginActivity
import com.tokopedia.loginregister.stub.FakeGraphqlRepository
import com.tokopedia.loginregister.stub.usecase.GetProfileUseCaseStub
import com.tokopedia.loginregister.stub.usecase.LoginTokenUseCaseStub
import com.tokopedia.loginregister.stub.usecase.LoginTokenV2UseCaseStub
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

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
    lateinit var loginTokenV2UseCaseStub: LoginTokenV2UseCaseStub

    @Inject
    lateinit var getProfileUseCaseStub: GetProfileUseCaseStub

    @Inject
    lateinit var loginTokenUseCaseStub: LoginTokenUseCaseStub

    @Before
    open fun before() {
        val fakeComponentFactory = FakeActivityComponentFactory()
        ActivityComponentFactory.instance = fakeComponentFactory
        fakeComponentFactory.loginComponent.inject(this)
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
        val viewInteraction = onView(withId(R.id.actionTextID)).check(matches(isDisplayed()))
        viewInteraction.perform(click())
    }

    fun clickBottomRegister() {
        val viewInteraction = onView(withId(R.id.register_button)).check(matches(isDisplayed()))
        viewInteraction.perform(click())
    }

    fun clickForgotPass() {
        val viewInteraction = onView(withId(R.id.forgot_pass)).check(matches(isDisplayed()))
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
}
