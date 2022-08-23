package com.tokopedia.search.robot

import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import org.hamcrest.MatcherAssert.assertThat

private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME = "tracker/search/search_product.json"

internal class GTMTrackingResultRobot {

    fun allP0TrackingSuccess(cassavaRule: CassavaTestRule) {
        assertThat(
                cassavaRule.validate(ANALYTIC_VALIDATOR_QUERY_FILE_NAME),
                hasAllSuccess()
        )
    }
}