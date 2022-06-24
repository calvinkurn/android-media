package com.tokopedia.loginregister.login.behaviour.base

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.discover.pojo.DiscoverData
import com.tokopedia.loginregister.discover.pojo.DiscoverPojo
import com.tokopedia.loginregister.discover.pojo.ProviderData
import com.tokopedia.loginregister.login.behaviour.data.*
import com.tokopedia.loginregister.login.behaviour.di.FakeActivityComponentFactory
import com.tokopedia.loginregister.login.di.ActivityComponentFactory
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckPojo
import com.tokopedia.loginregister.login.view.activity.LoginActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

open class LoginBase: LoginRegisterBase() {

    var isDefaultRegisterCheck = true
    var isDefaultDiscover = true

    @get:Rule
    var activityTestRule = IntentsTestRule(
            LoginActivity::class.java, false, false
    )

    @Inject
    lateinit var registerCheckUseCaseStub: RegisterCheckUseCaseStub

    @Inject
    lateinit var discoverUseCaseStub: DiscoverUseCaseStub

    @Inject
    lateinit var loginTokenV2UseCaseStub: LoginTokenV2UseCaseStub

    @Inject
    lateinit var generatePublicKeyUseCaseStub: GeneratePublicKeyUseCaseStub

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

    protected fun setRegisterCheckDefaultResponse() {
        val data = RegisterCheckData(isExist = true, useHash = true , userID = "123456", registerType = "email", view = "yoris.prayogo@tokopedia.com")
        registerCheckUseCaseStub.response = RegisterCheckPojo(data = data)
    }

    protected fun setDefaultDiscover() {
        val mockProviders = arrayListOf(
            ProviderData("gplus", "Google", "https://accounts.tokopedia.com/gplus-login", "", "#FFFFFF"),
        )
        val response = DiscoverPojo(DiscoverData(mockProviders, ""))
        discoverUseCaseStub.response = response
    }

    protected fun setupLoginActivity(
            intentModifier: (Intent) -> Unit = {}
    ) {
        val intent = Intent()

        intentModifier(intent)
        activityTestRule.launchActivity(intent)
    }

    fun runTest(test: () -> Unit) {
        if(isDefaultDiscover) {
            setDefaultDiscover()
        }
        if(isDefaultRegisterCheck) {
            setRegisterCheckDefaultResponse()
        }
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

        onView(withId(R.id.ub_forgot_password)).perform(click())
    }

    fun clickUbahButton() {
        onView(withId(R.id.change_button)).check(matches(ViewMatchers.isDisplayed())).perform(click())
    }

    fun inputPassword(value: String) {
        val viewInteraction = onView(withId(R.id.text_field_input)).check(matches(isDisplayed()))
        viewInteraction.perform(ViewActions.typeText(value))
    }

    fun shouldBeEnabled(id: Int) {
        onView(withId(id)).check(matches(isEnabled()))
    }

    fun isEmailExtensionDisplayed() {
        shouldBeDisplayed(R.id.emailExtension)
    }

    fun isEmailExtensionDismissed() {
        shouldBeHidden(R.id.emailExtension)
    }

}