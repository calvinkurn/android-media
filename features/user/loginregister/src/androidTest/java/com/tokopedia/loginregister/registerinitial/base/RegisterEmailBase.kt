package com.tokopedia.loginregister.registerinitial.base

import android.content.Context
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.di.FakeActivityComponentFactory
import com.tokopedia.loginregister.di.RegisterInitialComponentStub
import com.tokopedia.loginregister.login.behaviour.base.LoginRegisterBase
import com.tokopedia.loginregister.login.di.ActivityComponentFactory
import com.tokopedia.loginregister.registerinitial.view.activity.RegisterEmailActivity
import com.tokopedia.loginregister.stub.FakeGraphqlRepository
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject
import com.tokopedia.unifycomponents.R as unifycomponentsR

open class RegisterEmailBase : LoginRegisterBase() {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        RegisterEmailActivity::class.java,
        false,
        false
    )

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Inject
    lateinit var fakeRepo: FakeGraphqlRepository

    @Before
    open fun before() {
        var registerComponent: RegisterInitialComponentStub
        ActivityComponentFactory.instance = FakeActivityComponentFactory().also {
            registerComponent = it.registerComponent
        }
        registerComponent.inject(this)
    }

    @After
    fun tear() {
        activityTestRule.finishActivity()
    }

    protected fun setupActivity(
        intentModifier: (Intent) -> Unit = {}
    ) {
        val intent = Intent()

        intentModifier(intent)
        activityTestRule.launchActivity(intent)
    }

    fun runTest(test: () -> Unit) {
        setupActivity()
        test.invoke()
    }

    fun emailInputIsEnabled(id: Int) {
        onView(withId(id))
            .check(matches(isEnabled()))
    }

    fun inputName(value: String) {
        onView(
            allOf(
                withId(unifycomponentsR.id.text_field_input),
                isDescendantOfA(withId(R.id.wrapper_name))
            )
        )
            .check(matches(isDisplayed()))
            .perform(clearText(), typeText(value))
    }

    fun inputPassword(value: String) {
        onView(
            allOf(
                withId(unifycomponentsR.id.text_field_input),
                isDescendantOfA(withId(R.id.wrapper_password))
            )
        ).check(matches(isDisplayed())).perform(clearText(), typeText(value))
    }

    fun clickRegister() {
        val viewInteraction = onView(withId(R.id.register_button))
            .check(matches(isDisplayed()))
        viewInteraction.perform(ViewActions.click())
    }
}
