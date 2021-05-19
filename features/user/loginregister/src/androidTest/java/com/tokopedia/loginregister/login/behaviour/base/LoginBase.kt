package com.tokopedia.loginregister.login.behaviour.base

import android.content.Context
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.discover.data.DiscoverItemDataModel
import com.tokopedia.loginregister.login.behaviour.activity.LoginActivityStub
import com.tokopedia.loginregister.login.behaviour.data.DiscoverUseCaseStub
import com.tokopedia.loginregister.login.behaviour.data.RegisterCheckUseCaseStub
import com.tokopedia.loginregister.login.behaviour.di.DaggerBaseAppComponentStub
import com.tokopedia.loginregister.login.behaviour.di.DaggerLoginComponentStub
import com.tokopedia.loginregister.login.behaviour.di.LoginComponentStub
import com.tokopedia.loginregister.login.behaviour.di.modules.AppModuleStub
import com.tokopedia.loginregister.login.behaviour.di.modules.DaggerMockLoginRegisterComponent
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckPojo
import com.tokopedia.loginregister.login.idling.FragmentTransactionIdle
import com.tokopedia.loginregister.login.view.model.DiscoverDataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

open class LoginBase {
    @get:Rule
    var activityTestRule = IntentsTestRule(
            LoginActivityStub::class.java, false, false
    )

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    protected val applicationContext: Context
        get() = InstrumentationRegistry
                .getInstrumentation().context.applicationContext

    protected open lateinit var activity: LoginActivityStub

    protected lateinit var loginComponent: LoginComponentStub

    protected open lateinit var fragmentTransactionIdling: FragmentTransactionIdle

    @Inject
    lateinit var registerCheckUseCaseStub: RegisterCheckUseCaseStub

    @Inject
    lateinit var discoverUseCaseStub: DiscoverUseCaseStub

    @ExperimentalCoroutinesApi
    @Before
    open fun before() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        val baseAppComponent = DaggerBaseAppComponentStub.builder()
                .appModuleStub(AppModuleStub(applicationContext))
                .build()
        val loginRegisterComponent =  DaggerMockLoginRegisterComponent.builder()
                .baseAppComponentStub(baseAppComponent)
                .build()
        loginComponent = DaggerLoginComponentStub.builder()
                .mockLoginRegisterComponent(loginRegisterComponent)
                .build()
        loginComponent.inject(this)
    }

    @After
    fun tear() {
        activityTestRule.finishActivity()
    }

    protected fun setRegisterCheckDefaultResponse() {
        val data = RegisterCheckData(isExist = true, userID = "123456", registerType = "email", view = "yoris.prayogo@tokopedia.com")
        registerCheckUseCaseStub.response = RegisterCheckPojo(data = data)
    }

    protected fun setDefaultDiscover() {
        val mockProviders = arrayListOf(DiscoverItemDataModel("1", "Test Providers", "cek url", "", ""))
        val response = DiscoverDataModel(mockProviders, "")
        discoverUseCaseStub.response = response
    }

    protected fun inflateTestFragment() {
        activity.setupTestFragment(loginComponent)
        waitForFragmentResumed()
    }

    protected fun setupLoginActivity(
            intentModifier: (Intent) -> Unit = {}
    ) {
        val intent = Intent()

        intentModifier(intent)
        activityTestRule.launchActivity(intent)
        activity = activityTestRule.activity
        fragmentTransactionIdling = FragmentTransactionIdle(
                activity.supportFragmentManager,
                LoginActivityStub.TAG
        )
    }

    protected fun waitForFragmentResumed() {
        IdlingRegistry.getInstance().register(fragmentTransactionIdling)
        onView(withId(R.id.login_input_view))
                .check(matches(isDisplayed()))
        IdlingRegistry.getInstance().unregister(fragmentTransactionIdling)
    }

    protected fun launchDefaultFragment() {
        setupLoginActivity {
            it.putExtras(Intent(context, LoginActivityStub::class.java))
        }
        inflateTestFragment()
    }

    fun testRegisterCheck() {
        val data = RegisterCheckData(isExist = true, userID = "123456", registerType = "email")
        registerCheckUseCaseStub.response = RegisterCheckPojo(data = data)

        launchDefaultFragment()
        inputEmailOrPhone("yoris.prayogo+543@tokopedia.com")
        clickSubmit()

        onView(withId(R.id.wrapper_password)).check(matches(ViewMatchers.isDisplayed()))
    }

    fun clickTopRegister() {
        val viewInteraction = onView(withId(R.id.actionTextID)).check(matches(isDisplayed()))
        viewInteraction.perform(ViewActions.click())
    }

    fun clickForgotPass() {
        val viewInteraction = onView(withId(R.id.forgot_pass)).check(matches(isDisplayed()))
        viewInteraction.perform(ViewActions.click())
    }

    fun clickSubmit(){
        val viewInteraction = onView(withId(R.id.register_btn)).check(matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(ViewActions.click())
    }

    fun clickUbahButton() {
        onView(withId(R.id.change_button)).check(matches(ViewMatchers.isDisplayed())).perform(ViewActions.click())
    }

    fun inputEmailOrPhone(value: String) {
        val viewInteraction = onView(withId(R.id.input_email_phone)).check(matches(isDisplayed()))
        viewInteraction.perform(ViewActions.typeText(value))
    }

    fun deleteEmailOrPhoneInput() {
        val viewInteraction = onView(withId(R.id.input_email_phone)).check(matches(isDisplayed()))
        viewInteraction.perform(clearText())
    }

    fun clickSocmedButton() {
        onView(withId(R.id.socmed_btn))
                .check(matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.click())
    }

    fun shouldBeEnabled(id: Int) {
        onView(withId(id)).check(matches(isEnabled()))
    }

    fun shouldBeDisabled(id: Int) {
        onView(withId(id)).check(matches(not(isEnabled())))
    }

    fun shouldBeDisplayed(id: Int) {
        onView(withId(id)).check(matches(isDisplayed()))
    }

    fun shouldBeHidden(id: Int) {
        onView(withId(id)).check(matches(not(isDisplayed())))
    }

    fun isDisplayingGivenText(id: Int, givenText: String) {
        onView(withId(id)).check(matches(withText(givenText))).check(matches(isDisplayed()))
    }

    fun isTextInputHasError(id: Int, errorText: String) {
        onView(withId(id)).check(matches(hasErrorText(errorText))).check(matches(isDisplayed()))
    }

    fun isDialogDisplayed(text: String) {
        onView(withText(text))
                .inRoot(RootMatchers.isDialog())
                .check(matches(isDisplayed()))
    }

}