package com.tokopedia.loginregister.registerinitial

import android.Manifest
import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.loginregister.di.FakeActivityComponentFactory
import com.tokopedia.loginregister.di.RegisterInitialComponentStub
import com.tokopedia.loginregister.login.behaviour.base.LoginRegisterBase
import com.tokopedia.loginregister.login.di.ActivityComponentFactory
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckPojo
import com.tokopedia.loginregister.registerinitial.view.activity.RegisterInitialActivity
import com.tokopedia.loginregister.stub.FakeGraphqlRepository
import com.tokopedia.loginregister.stub.usecase.GetProfileUseCaseStub
import com.tokopedia.loginregister.stub.usecase.GraphqlUseCaseStub
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

open class RegisterInitialBase : LoginRegisterBase() {

    var isDefaultRegisterCheck = true

    @get:Rule
    var grantPermission: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.READ_PHONE_STATE
    )

    @get:Rule
    var activityTestRule = IntentsTestRule(
        RegisterInitialActivity::class.java,
        false,
        false
    )

    @Inject
    lateinit var registerCheckUseCase: GraphqlUseCaseStub<RegisterCheckPojo>

    @Inject
    lateinit var getProfileUseCaseStub: GetProfileUseCaseStub

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
        val data = RegisterCheckData(isExist = true, userID = "123456", registerType = "email", view = "yoris.prayogooooo@tokopedia.com")
        registerCheckUseCase.response = RegisterCheckPojo(data)
    }

    fun runTest(test: () -> Unit) {
        if (isDefaultRegisterCheck) {
            setRegisterCheckDefaultResponse()
        }
        setupActivity()
        clearEmailInput()
        test.invoke()
    }
}
