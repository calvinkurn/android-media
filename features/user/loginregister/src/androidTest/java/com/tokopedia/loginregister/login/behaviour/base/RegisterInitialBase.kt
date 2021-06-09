package com.tokopedia.loginregister.login.behaviour.base

import android.content.Context
import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.discover.data.DiscoverItemDataModel
import com.tokopedia.loginregister.login.behaviour.activity.RegisterInitialActivityStub
import com.tokopedia.loginregister.login.behaviour.data.DiscoverUseCaseStub
import com.tokopedia.loginregister.login.behaviour.data.GraphqlUseCaseStub
import com.tokopedia.loginregister.login.behaviour.di.DaggerBaseAppComponentStub
import com.tokopedia.loginregister.login.behaviour.di.DaggerRegisterInitialComponentStub
import com.tokopedia.loginregister.login.behaviour.di.RegisterInitialComponentStub
import com.tokopedia.loginregister.login.behaviour.di.modules.AppModuleStub
import com.tokopedia.loginregister.login.behaviour.di.modules.DaggerMockLoginRegisterComponent
import com.tokopedia.loginregister.login.idling.FragmentTransactionIdle
import com.tokopedia.loginregister.login.view.model.DiscoverDataModel
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckPojo
import com.tokopedia.sessioncommon.domain.usecase.GeneratePublicKeyUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

open class RegisterInitialBase: LoginRegisterBase() {

    var isDefaultRegisterCheck = true
    var isDefaultDiscover = true

    @get:Rule
    var activityTestRule = IntentsTestRule(
        RegisterInitialActivityStub::class.java, false, false
    )

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    protected val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext

    protected open lateinit var activity: RegisterInitialActivityStub

    protected lateinit var registerInitialComponentStub: RegisterInitialComponentStub

    protected open lateinit var fragmentTransactionIdling: FragmentTransactionIdle

    @Inject
    lateinit var generatePublicKeyUseCase: GeneratePublicKeyUseCase

    @Inject
    lateinit var registerCheckUseCase: GraphqlUseCaseStub<RegisterCheckPojo>

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
        registerInitialComponentStub = DaggerRegisterInitialComponentStub.builder()
            .mockLoginRegisterComponent(loginRegisterComponent)
            .build()
        registerInitialComponentStub.inject(this)
    }

    @After
    fun tear() {
        activityTestRule.finishActivity()
    }

    protected fun inflateTestFragment() {
        activity.setupTestFragment(registerInitialComponentStub)
        waitForFragmentResumed()
    }

    protected fun setupActivity(
        intentModifier: (Intent) -> Unit = {}
    ) {
        val intent = Intent()

        intentModifier(intent)
        activityTestRule.launchActivity(intent)
        activity = activityTestRule.activity
        fragmentTransactionIdling = FragmentTransactionIdle(
            activity.supportFragmentManager,
            RegisterInitialActivityStub.TAG
        )
    }

    protected fun waitForFragmentResumed() {
        IdlingRegistry.getInstance().register(fragmentTransactionIdling)
        Espresso.onView(ViewMatchers.withId(R.id.register_input_view))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        IdlingRegistry.getInstance().unregister(fragmentTransactionIdling)
    }

    protected fun launchDefaultFragment() {
        setupActivity {
            it.putExtras(Intent(context, RegisterInitialActivityStub::class.java))
        }
        inflateTestFragment()
    }

    protected fun setRegisterCheckDefaultResponse() {
        val data = RegisterCheckData(isExist = true , userID = "123456", registerType = "email", view = "yoris.prayogooooo@tokopedia.com")
        registerCheckUseCase.response = RegisterCheckPojo(data)
    }

    protected fun setDefaultDiscover() {
        val mockProviders = arrayListOf(
            DiscoverItemDataModel("gplus", "Google", "https://accounts.tokopedia.com/gplus-login", "", "#FFFFFF"),
            DiscoverItemDataModel("facebook", "Facebook", "https://accounts.tokopedia.com/fb-login", "", "#FFFFFF")
        )
        val response = DiscoverDataModel(mockProviders, "")
        discoverUseCaseStub.response = response
    }

    fun runTest(test: () -> Unit) {
//        setRegisterCheckDefaultResponse()
        if(isDefaultDiscover) {
            setDefaultDiscover()
        }
        if(isDefaultRegisterCheck) {
            setRegisterCheckDefaultResponse()
        }
        launchDefaultFragment()
        clearEmailInput()
        test.invoke()
    }

}