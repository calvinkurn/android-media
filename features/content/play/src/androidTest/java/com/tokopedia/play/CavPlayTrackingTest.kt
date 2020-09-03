package com.tokopedia.play

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.core.getAnalyticsWithQuery
import com.tokopedia.analyticsdebugger.validator.core.hasAllSuccess
import com.tokopedia.play.data.PlayLiveMockModelConfig
import com.tokopedia.play.data.PlayVodMockModelConfig
import com.tokopedia.play.view.activity.PlayActivity
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/**
 * Created by mzennis on 31/08/20.
 * Instrumentation Test for checking Analytics Validator in Play Viewer Module
 */
class CavPlayTrackingTest {

    /**
     * This rule provides functional testing of a single Activity
     * the Activity under test will be launched before each test annotated with @Test
     * and before methods annotated with @Before
     *
     * @param initialTouchMode false because the Activity should not be placed into "touch mode" when started
     * @param launchActivity false because the Activity will be launched manually with intent extras
     **/
    @get:Rule
    val activityRule = ActivityTestRule(PlayActivity::class.java, false, false)

    // context for the target application being instrumented
    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    // database that holds all the analytics every user journey
    private val gtmLogDbSource = GtmLogDBSource(targetContext)

    /**
     * json file which holds all valid trackers of the Play
     * file: libraries/analytics_debugger/src/main/assets/tracker/content/play/play_v1_analytic.json
     **/
    private val fileName = "tracker/content/play/play_v1_analytic.json"

    @Before
    fun setUp() {
        // delete all data in the database
        gtmLogDbSource.deleteAll().subscribe()
    }

    @Test
    fun validateAllTracking() {

        performJourneyLiveVertical()

//        performJourneyVodHorizontal()
//        performJourneyError()

        assertAllJourney()
    }

    private fun performJourneyLiveVertical() {
        setupGraphqlMockResponse(PlayLiveMockModelConfig())

        activityRule.launchActivity(PlayActivity.createIntent(targetContext, "10708"))

        Thread.sleep(10000)

        activityRule.finishActivity()

        Thread.sleep(3000)
    }

//    private fun performJourneyVodHorizontal() {
//        setupGraphqlMockResponse(PlayVodMockModelConfig())
//
//        activityRule.launchActivity(PlayActivity.createIntent(targetContext, "10708"))
//
//        Thread.sleep(3000)
//
//        activityRule.finishActivity()
//    }

//    private fun performJourneyError() {
//        setupGraphqlMockResponse(PlayVodMockModelConfig())
//
//        activityRule.launchActivity(PlayActivity.createIntent(targetContext, "10708"))
//
//        Thread.sleep(3000)
//
//        activityRule.finishActivity()
//    }

    private fun assertAllJourney() {
        assertThat(getAnalyticsWithQuery(gtmLogDbSource, targetContext, fileName),
                hasAllSuccess())
    }
}