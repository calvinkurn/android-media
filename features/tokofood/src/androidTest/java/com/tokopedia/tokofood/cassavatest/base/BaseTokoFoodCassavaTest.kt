package com.tokopedia.tokofood.cassavatest.base

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.tokofood.stub.base.presentation.activity.BaseTokofoodActivityStub
import org.hamcrest.MatcherAssert
import org.junit.Rule

open class BaseTokoFoodCassavaTest {

    @get:Rule
    var activityRule = IntentsTestRule(BaseTokofoodActivityStub::class.java, true, false)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    protected val applicationContext: Context = ApplicationProvider.getApplicationContext()

    private val targetContext: Context = InstrumentationRegistry.getInstrumentation().targetContext

    protected fun launchActivity(page: String = BaseTokofoodActivityStub.PURCHASE_PAGE) {
        activityRule.launchActivity(
            BaseTokofoodActivityStub.createIntent(targetContext, page)
        )
    }

    protected fun validateTracker(fileName: String) {
        MatcherAssert.assertThat(cassavaTestRule.validate(fileName), hasAllSuccess())
    }

    protected fun dismissPage() {
        activityRule.activity.finish()
    }

}
