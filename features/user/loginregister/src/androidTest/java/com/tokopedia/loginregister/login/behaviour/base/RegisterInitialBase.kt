package com.tokopedia.loginregister.login.behaviour.base

import android.content.Context
import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.loginregister.discover.pojo.DiscoverData
import com.tokopedia.loginregister.discover.pojo.DiscoverPojo
import com.tokopedia.loginregister.discover.pojo.ProviderData
import com.tokopedia.loginregister.login.behaviour.data.DiscoverUseCaseStub
import com.tokopedia.loginregister.login.behaviour.data.GetProfileUseCaseStub
import com.tokopedia.loginregister.login.behaviour.data.GraphqlUseCaseStub
import com.tokopedia.loginregister.login.behaviour.di.FakeActivityComponentFactory
import com.tokopedia.loginregister.login.behaviour.di.RegisterInitialComponentStub
import com.tokopedia.loginregister.login.di.ActivityComponentFactory
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckPojo
import com.tokopedia.loginregister.registerinitial.view.activity.RegisterInitialActivity
import com.tokopedia.sessioncommon.domain.usecase.GeneratePublicKeyUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

open class RegisterInitialBase: LoginRegisterBase() {

    var isDefaultRegisterCheck = true
    var isDefaultDiscover = true

    @get:Rule
    var activityTestRule = IntentsTestRule(
        RegisterInitialActivity::class.java, false, false
    )

    @Inject
    lateinit var generatePublicKeyUseCase: GeneratePublicKeyUseCase

    @Inject
    lateinit var registerCheckUseCase: GraphqlUseCaseStub<RegisterCheckPojo>

    @Inject
    lateinit var discoverUseCaseStub: DiscoverUseCaseStub

    @Inject
    lateinit var getProfileUseCaseStub: GetProfileUseCaseStub

    @ExperimentalCoroutinesApi
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
//        )
    }

    protected fun setRegisterCheckDefaultResponse() {
        val data = RegisterCheckData(isExist = true , userID = "123456", registerType = "email", view = "yoris.prayogooooo@tokopedia.com")
        registerCheckUseCase.response = RegisterCheckPojo(data)
    }

    protected fun setDefaultDiscover() {
        val mockProviders = arrayListOf(
            ProviderData("gplus", "Google", "https://accounts.tokopedia.com/gplus-login", "", "#FFFFFF"),
        )
        val response = DiscoverPojo(DiscoverData(mockProviders, ""))
        discoverUseCaseStub.response = response
    }

    fun runTest(test: () -> Unit) {
        if(isDefaultDiscover) {
            setDefaultDiscover()
        }
        if(isDefaultRegisterCheck) {
            setRegisterCheckDefaultResponse()
        }
        setupActivity()
        clearEmailInput()
        test.invoke()
    }
}