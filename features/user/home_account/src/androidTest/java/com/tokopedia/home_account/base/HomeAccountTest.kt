package com.tokopedia.home_account.base

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.home_account.common.idling.FragmentTransactionIdle
import com.tokopedia.home_account.di.*
import com.tokopedia.home_account.stub.data.GraphqlRepositoryStub
import com.tokopedia.home_account.stub.di.user.*
import com.tokopedia.home_account.stub.view.activity.HomeAccountUserActivityStub
import com.tokopedia.home_account.view.activity.HomeAccountUserActivity
import com.tokopedia.home_account.view.fragment.HomeAccountUserFragment
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    lateinit var fragment: HomeAccountUserFragment

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext

    @ExperimentalCoroutinesApi
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
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
    }

    @After
    open fun tearDown() {
        activityTestRule.finishActivity()
    }

    fun runTest(test: () -> Unit) {
        launchDefaultFragment()
        test.invoke()
    }

    fun Unit.validate(query: List<Map<String, String>>) {
        Thread.sleep(5000)
        val queryMatcher = cassavaTestRule.validate(
                query,  CassavaTestRule.MODE_SUBSET)
        ViewMatchers.assertThat(queryMatcher, hasAllSuccess())
    }

    protected fun launchDefaultFragment() {
        setupHomeAccountUserActivity {
            it.putExtras(Intent(context, HomeAccountUserActivityStub::class.java))
        }
    }

    private fun setupHomeAccountUserActivity(
        intentModifier: (Intent) -> Unit = {}
    ) {
        val intent = Intent()
        intentModifier(intent)
        activityTestRule.activity
        activityTestRule.launchActivity(intent)
        activity = activityTestRule.activity
        fragmentTransactionIdling = FragmentTransactionIdle(
                activity.supportFragmentManager,
                HomeAccountUserActivity.TAG
        )
        fragment = activity.fragment

    }

    companion object {
        lateinit var homeAccountUserComponents: HomeAccountUserComponentsStub
    }
}