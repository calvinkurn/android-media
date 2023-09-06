package com.tokopedia.dilayanitokopedia

import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import org.hamcrest.MatcherAssert

/**
 * Created by irpan on 22/05/23.
 */
object ResultRobot {
    fun hasPassedAnalytics(cassavaTestRule: CassavaTestRule, queryId: String) {
        MatcherAssert.assertThat(cassavaTestRule.validate(queryId), hasAllSuccess())
    }
}
