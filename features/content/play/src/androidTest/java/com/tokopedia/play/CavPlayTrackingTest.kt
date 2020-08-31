package com.tokopedia.play

import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.core.getAnalyticsWithQuery
import com.tokopedia.analyticsdebugger.validator.core.hasAllSuccess
import com.tokopedia.play.view.activity.PlayActivity
import com.tokopedia.play.view.activity.PlayActivity.Companion.createIntent
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/**
 * Created by mzennis on 31/08/20.
 */
class CavPlayTrackingTest {

    @get:Rule
    val activityRule = IntentsTestRule(PlayActivity::class.java, false, false)

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDbSource = GtmLogDBSource(targetContext)

    private val fileName = "tracker/content/play/play_analytic.json"

    @Before
    fun setUp() {
        // clear all existing db tracking
        gtmLogDbSource.deleteAll().subscribe()

        // setup mock response
        setupGraphqlMockResponse(PlayMockModelConfig())

        activityRule.launchActivity(createIntent(targetContext, "10708"))
    }

    @Test
    fun runTrackingTest() {
        performJourney()

        assertThat(getAnalyticsWithQuery(gtmLogDbSource, targetContext, fileName),
                hasAllSuccess())
    }

    // TODO create the journey for vod vertical
    private fun performJourney() {

    }
}