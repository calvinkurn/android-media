package com.tokopedia.home_recom.tracker

import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import org.hamcrest.MatcherAssert

/**
 * Created by yfsx on 10/11/21.
 */
fun RecommendationCassavaTest(func: RecommendationAnalyticsHelper.() -> Unit) =
    RecommendationAnalyticsHelper().apply(func)

class RecommendationAnalyticsHelper {

    fun waitForData() {
        Thread.sleep(10000)
    }

    fun waitForData2() {
        Thread.sleep(5000)
    }

    fun addDebugEnd() {
        Thread.sleep(5000)
    }


    infix fun validateAnalytics(func: RecommendationCassavaResult.() -> Unit): RecommendationCassavaResult {
        return RecommendationCassavaResult().apply(func)
    }

}

class RecommendationCassavaResult {

    fun hasPassedAnalytics(cassavaTestRule: CassavaTestRule, queryFileName: String) {
        MatcherAssert.assertThat(cassavaTestRule.validate(queryFileName), hasAllSuccess())
    }

}