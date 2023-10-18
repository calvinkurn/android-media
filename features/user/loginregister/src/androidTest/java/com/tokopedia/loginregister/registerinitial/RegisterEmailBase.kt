package com.tokopedia.loginregister.registerinitial

import android.content.Context
import android.content.Intent
import androidx.test.espresso.Espresso.onView
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
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckPojo
import com.tokopedia.loginregister.registerinitial.view.activity.RegisterEmailActivity
import com.tokopedia.loginregister.stub.FakeGraphqlRepository
import com.tokopedia.loginregister.stub.usecase.GraphqlUseCaseStub
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

open class RegisterEmailBase : LoginRegisterBase() {

    var isDefaultRegisterCheck = true

    @get:Rule
    var activityTestRule = IntentsTestRule(
        RegisterEmailActivity::class.java,
        false,
        false
    )

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Inject
    lateinit var registerCheckUseCase: GraphqlUseCaseStub<RegisterCheckPojo>

    @Inject
    lateinit var fakeGraphqlRepository: FakeGraphqlRepository

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

    protected fun setRegisterCheckDefaultResponse() {
        val data = RegisterCheckData(
            isExist = true,
            userID = "123456",
            registerType = "email",
            view = "yoris.prayogooooo@tokopedia.com"
        )
        registerCheckUseCase.response = RegisterCheckPojo(data)
    }

    fun runTest(test: () -> Unit) {
        if (isDefaultRegisterCheck) {
            setRegisterCheckDefaultResponse()
        }
        setupActivity()
        test.invoke()
    }

    fun inputEmailRegister(value: String) {
        val viewInteraction = onView(
            allOf(
                withId(com.tokopedia.unifycomponents.R.id.text_field_input),
                isDescendantOfA(withId(R.id.wrapper_email))
            )
        )
            .check(matches(isDisplayed()))
        viewInteraction.perform(typeText(value))
    }

    fun emailInputIsEnabled(id: Int) {
        onView(withId(id))
            .check(matches(isEnabled()))
    }

    fun inputName(value: String) {
        onView(allOf(withId(com.tokopedia.unifycomponents.R.id.text_field_input), isDescendantOfA(withId(R.id.wrapper_name))))
            .check(matches(isDisplayed()))
            .perform(clearText(), typeText(value))
    }

    fun inputPassword(value: String) {
        onView(
            allOf(
                withId(com.tokopedia.unifycomponents.R.id.text_field_input),
                isDescendantOfA(withId(R.id.wrapper_password))
            )
        ).check(matches(isDisplayed())).perform(clearText(), typeText(value))
    }
}
