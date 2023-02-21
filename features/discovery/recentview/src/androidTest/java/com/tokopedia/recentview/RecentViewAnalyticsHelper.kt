package com.tokopedia.recentview

import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import org.hamcrest.MatcherAssert

/**
 * Created by yfsx on 10/11/21.
 */
fun RecentViewCassavaTest(func: RecentViewAnalyticsHelper.() -> Unit) =
    RecentViewAnalyticsHelper().apply(func)

class RecentViewAnalyticsHelper {

    fun waitForData() {
        Thread.sleep(10000)
    }

    fun waitForData2() {
        Thread.sleep(5000)
    }

    fun addDebugEnd() {
        Thread.sleep(5000)
    }


    infix fun validateAnalytics(func: RecentViewCassavaResult.() -> Unit): RecentViewCassavaResult {
        return RecentViewCassavaResult().apply(func)
    }

}

class RecentViewCassavaResult {

    fun hasPassedAnalytics(cassavaTestRule: CassavaTestRule, queryFileName: String) {
        MatcherAssert.assertThat(cassavaTestRule.validate(queryFileName), hasAllSuccess())
    }

}