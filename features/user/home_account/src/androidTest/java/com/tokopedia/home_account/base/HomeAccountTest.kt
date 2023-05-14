package com.tokopedia.home_account.base

import android.content.Context
import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.home_account.di.ActivityComponentFactory
import com.tokopedia.home_account.stub.data.GraphqlRepositoryStub
import com.tokopedia.home_account.stub.di.ActivityComponentFactoryStub
import com.tokopedia.home_account.stub.di.user.HomeAccountUserComponentsStub
import com.tokopedia.home_account.stub.view.activity.HomeAccountUserActivityStub
import com.tokopedia.home_account.view.activity.HomeAccountUserActivity
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.user.session.UserSessionInterface
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

abstract class HomeAccountTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        HomeAccountUserActivity::class.java,
        false,
        false
    )

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    @Inject
    lateinit var repo: GraphqlRepositoryStub

    @Inject
    lateinit var userSession: UserSessionInterface

    protected open lateinit var activity: HomeAccountUserActivity
    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    open fun before() {
        val stub = ActivityComponentFactoryStub()
        ActivityComponentFactory.instance = stub
        stub.component.inject(this)
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
        Thread.sleep(3000)
        val queryMatcher = cassavaTestRule.validate(
            query,
            CassavaTestRule.MODE_SUBSET
        )
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
        activityTestRule.launchActivity(intent)
    }

    companion object {
        lateinit var homeAccountUserComponents: HomeAccountUserComponentsStub
    }
}
