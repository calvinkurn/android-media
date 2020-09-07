package com.tokopedia.play.analytic

import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.core.getAnalyticsWithQuery
import com.tokopedia.analyticsdebugger.validator.core.hasAllSuccess
import com.tokopedia.play.data.PlayErrorMockModelConfig
import com.tokopedia.play.view.activity.PlayActivity
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/**
 * Created by mzennis on 07/09/20.
 * Instrumentation Test for checking Analytics Validator in Play Viewer Module
 *
 * Channel Type: Error
 */
class CavPlayTrackingErrorTest {

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
    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    // database that holds all the analytics every user journey
    private val gtmLogDbSource = GtmLogDBSource(targetContext)

    /**
     * json file which holds valid trackers for Live Channel
     * file: libraries/analytics_debugger/src/main/assets/tracker/content/play/play_error_analytic.json
     **/
    private val fileName = "tracker/content/play/play_error_analytic.json"

    @Before
    fun setUp() {
        // delete all data in the database
        gtmLogDbSource.deleteAll().subscribe()

        // setup mock response
        setupGraphqlMockResponse(PlayErrorMockModelConfig())
    }

    @Test
    fun runValidateTracking() {
        // launch play activity with dummy channel id
        intentsTestRule.launchActivity(PlayActivity.createIntent(targetContext, "43215"))

        // idling resource: add artificial delays to the tests
        Thread.sleep(8000)

        // finish / close activity
        intentsTestRule.finishActivity()

        Thread.sleep(2000)

        MatcherAssert.assertThat(getAnalyticsWithQuery(gtmLogDbSource, targetContext, fileName),
                hasAllSuccess())
    }
}