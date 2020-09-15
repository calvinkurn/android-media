package com.tokopedia.play.analytic

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.core.getAnalyticsWithQuery
import com.tokopedia.analyticsdebugger.validator.core.hasAllSuccess
import com.tokopedia.play.util.TextViewIdlingResource
import com.tokopedia.play.view.activity.PlayActivity
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule


/**
 * Created by mzennis on 14/09/20.
 */
abstract class BaseCavPlayTrackingTest {

    /**
     * json file which holds valid trackers for Live Channel
     * directory: libraries/analytics_debugger/src/main/assets/tracker/content/play/
     **/
    abstract fun getFileName(): String

    abstract fun mockModelConfig() : MockModelConfig

    /**
     * This class is an
     * extension of {@link ActivityTestRule}, which initializes Espresso-Intents before each test.
     * This rule also provides functional testing of a single Activity
     * the Activity under test will be launched before each test annotated with @Test
     * and before methods annotated with @Before
     *
     * @param initialTouchMode false because the Activity should not be placed into "touch mode" when started
     * @param launchActivity false because the Activity will be launched manually with intent extras
     **/
    @get:Rule
    val intentsTestRule = IntentsTestRule(PlayActivity::class.java, false, false)

    // context for the target application being instrumented
    protected val targetContext: Context = InstrumentationRegistry.getInstrumentation().targetContext

    // database that holds all the analytics every user journey
    private val gtmLogDbSource = GtmLogDBSource(targetContext)

    private var idlingResources: Array<IdlingResource> = emptyArray()

    @Before
    fun prepare() {
        // delete all data in the database
        gtmLogDbSource.deleteAll().subscribe()

        // setup mock response
        setupGraphqlMockResponse(mockModelConfig())

        IdlingRegistry.getInstance().register(*idlingResources)
    }

    protected fun validateTracker() {
        MatcherAssert.assertThat(getAnalyticsWithQuery(gtmLogDbSource, targetContext, getFileName()),
                hasAllSuccess())
    }


    @After
    fun clear() {
        IdlingRegistry.getInstance().unregister(*idlingResources)
    }
}
