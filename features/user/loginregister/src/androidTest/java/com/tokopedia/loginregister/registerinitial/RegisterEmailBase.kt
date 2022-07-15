package com.tokopedia.loginregister.registerinitial

import android.content.Context
import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.stub.usecase.GraphqlUseCaseStub
import com.tokopedia.loginregister.di.FakeActivityComponentFactory
import com.tokopedia.loginregister.di.RegisterInitialComponentStub
import com.tokopedia.loginregister.login.behaviour.base.LoginRegisterBase
import com.tokopedia.loginregister.login.di.ActivityComponentFactory
import com.tokopedia.loginregister.stub.FakeGraphqlRepository
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckPojo
import com.tokopedia.loginregister.registerinitial.view.activity.RegisterEmailActivity
import com.tokopedia.sessioncommon.domain.usecase.GeneratePublicKeyUseCase
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

open class RegisterEmailBase: LoginRegisterBase() {

    var isDefaultRegisterCheck = true

    @get:Rule
    var activityTestRule = IntentsTestRule(
        RegisterEmailActivity::class.java, false, false
    )

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Inject
    lateinit var generatePublicKeyUseCase: GeneratePublicKeyUseCase

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
        val data = RegisterCheckData(isExist = true , userID = "123456", registerType = "email", view = "yoris.prayogooooo@tokopedia.com")
        registerCheckUseCase.response = RegisterCheckPojo(data)
    }

    fun runTest(test: () -> Unit) {
        if(isDefaultRegisterCheck) {
            setRegisterCheckDefaultResponse()
        }
        setupActivity()
        test.invoke()
    }

    fun inputEmailRegister(value: String) {
        val viewInteraction = Espresso.onView(allOf(ViewMatchers.withId(R.id.text_field_input), isDescendantOfA(ViewMatchers.withId(R.id.wrapper_email))))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(ViewActions.typeText(value))
    }

    fun shouldBeEnabled(id: Int) {
        Espresso.onView(ViewMatchers.withId(id))
            .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
    }

    fun inputName(value: String) {
        val viewInteraction = Espresso.onView(allOf(ViewMatchers.withId(R.id.text_field_input), isDescendantOfA(ViewMatchers.withId(R.id.wrapper_name))))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(ViewActions.typeText(value))
    }

    fun inputPassword(value: String) {
        val viewInteraction = Espresso.onView(allOf(ViewMatchers.withId(R.id.text_field_input), isDescendantOfA(ViewMatchers.withId(R.id.wrapper_password))))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(ViewActions.typeText(value))
    }

}