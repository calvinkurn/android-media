package com.tokopedia.home_account.base

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.home_account.common.idling.FragmentTransactionIdle
import com.tokopedia.home_account.data.model.BalanceAndPointDataModel
import com.tokopedia.home_account.data.model.CentralizedUserAssetDataModel
import com.tokopedia.home_account.di.*
import com.tokopedia.home_account.stub.data.GraphqlRepositoryStub
import com.tokopedia.home_account.stub.data.TestState
import com.tokopedia.home_account.stub.di.DaggerFakeBaseAppComponent
import com.tokopedia.home_account.stub.di.FakeAppModule
import com.tokopedia.home_account.stub.view.activity.HomeAccountUserActivityStub
import com.tokopedia.home_account.view.activity.HomeAccountUserActivity
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.user.session.UserSessionInterface
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class HomeAccountTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        HomeAccountUserActivityStub::class.java, false, false
    )

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    lateinit var repo: GraphqlRepositoryStub

    lateinit var userSession: UserSessionInterface

    protected open lateinit var activity: HomeAccountUserActivity
    protected open lateinit var fragmentTransactionIdling: FragmentTransactionIdle

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext

    @Before
    open fun before() {
        val appComponent = DaggerFakeBaseAppComponent.builder().fakeAppModule(FakeAppModule(applicationContext)).build()
        repo = appComponent.graphqlRepository() as GraphqlRepositoryStub
        homeAccountUserComponents = DaggerHomeAccountUserComponentsStub.builder()
            .fakeBaseAppComponent(appComponent)
            .fakeHomeAccountUserModules(FakeHomeAccountUserModules(context))
            .homeAccountUserUsecaseModules(HomeAccountUserUsecaseModules())
            .homeAccountUserQueryModules(HomeAccountUserQueryModules())
            .build()
        userSession = homeAccountUserComponents.userSession()
        ApplicationProvider.getApplicationContext<BaseMainApplication>().setComponent(appComponent)
    }

    @After
    open fun tearDown() {
        activityTestRule.finishActivity()
    }

    fun runTest(test: () -> Unit) {
        launchDefaultFragment()
        test.invoke()
    }

    protected fun launchDefaultFragment() {
        setupHomeAccountUserActivity {
//            it.putExtras(Intent(context, HomeAccountUserActivityStub::class.java))
        }
        inflateTestFragment()
    }

    private fun inflateTestFragment() {
//        activity.setupTestFragment(otpComponent)
//        waitForFragmentResumed()
    }

    private fun setupHomeAccountUserActivity(
        intentModifier: (Intent) -> Unit = {}
    ) {
        val intent = Intent()
        intentModifier(intent)
        activityTestRule.activity
        activityTestRule.launchActivity(intent)
        activity = activityTestRule.activity
    }

    companion object {
        lateinit var homeAccountUserComponents: HomeAccountUserComponentsStub
    }
}