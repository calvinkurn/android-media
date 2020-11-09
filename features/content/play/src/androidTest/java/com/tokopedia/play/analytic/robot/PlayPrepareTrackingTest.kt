package com.tokopedia.play.analytic.robot

import android.content.Intent
import android.net.Uri
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.play.view.activity.PlayActivity
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.setupGraphqlMockResponse


/**
 * Created by mzennis on 14/09/20.
 */
internal class PlayPrepareTrackingTest {

    private lateinit var intentsTestRule: IntentsTestRule<PlayActivity>

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    /**
     * database that holds all the analytics every user journey
     */
    private val gtmLogDbSource = GtmLogDBSource(targetContext)

    private lateinit var fileName: String

    fun setup(testRule: IntentsTestRule<PlayActivity>) {
        this.intentsTestRule = testRule

        // delete all data in the database
        gtmLogDbSource.deleteAll().subscribe()
    }

    /**
     * setup mock response
     */
    fun setMockModel(mockModelConfig: MockModelConfig) {
        setupGraphqlMockResponse(mockModelConfig)
    }

    fun launch(channelId: String) {
        intentsTestRule.launchActivity(Intent(targetContext, PlayActivity::class.java).apply {
            data = Uri.parse("${ApplinkConstInternalContent.INTERNAL_PLAY}/$channelId")
        })
    }

    /**
     * json file which holds valid trackers for Live Channel
     * directory: libraries/analytics_debugger/src/main/assets/tracker/content/play/
     */
    fun setJsonAbsolutePath(fileName: String) {
        this.fileName = fileName
    }

    infix fun test(action: PlayTrackingTest.() -> Unit) = PlayTrackingTest(
            targetContext,
            gtmLogDbSource,
            fileName
    ).apply { action() }
}

internal fun prepare(action: PlayPrepareTrackingTest.() -> Unit) = PlayPrepareTrackingTest().apply { action() }
