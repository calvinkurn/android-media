package com.tokopedia.officialstore.cassava

import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import org.hamcrest.MatcherAssert

/**
 * Created by yfsx on 25/10/21.
 */

fun OSCassavaTest(func: OfficialStoreCassavaHelper.() -> Unit) =
    OfficialStoreCassavaHelper().apply(func)

class OfficialStoreCassavaHelper {

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