package com.tokopedia.home.component

import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import org.hamcrest.MatcherAssert

/**
 * Created by yfsx on 01/11/21.
 */
fun HomeDCCassavaTest(func: HomeDynamicChannelCassavaHelper.() -> Unit) =
    HomeDynamicChannelCassavaHelper().apply(func)

class HomeDynamicChannelCassavaHelper {

    fun waitForData() {
        Thread.sleep(10000)
    }

    fun waitForData2() {
        Thread.sleep(5000)
    }

    fun addDebugEnd() {
        Thread.sleep(5000)
    }


    infix fun validateAnalytics(func: OsCassavaResult.() -> Unit): OsCassavaResult {
        return OsCassavaResult().apply(func)
    }

}

class OsCassavaResult {

    fun hasPassedAnalytics(cassavaTestRule: CassavaTestRule, queryFileName: String) {
        MatcherAssert.assertThat(cassavaTestRule.validate(queryFileName), hasAllSuccess())
    }

}