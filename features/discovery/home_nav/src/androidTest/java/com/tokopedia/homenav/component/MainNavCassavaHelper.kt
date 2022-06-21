package com.tokopedia.homenav.component

import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import org.hamcrest.MatcherAssert

/**
 * Created by dhaba
 */

fun mainNavCassavaTest(func: MainNavCassavaHelper.() -> Unit) =
    MainNavCassavaHelper().apply(func)

class MainNavCassavaHelper {
    fun waitForData() {
        Thread.sleep(10000)
    }

    infix fun validateAnalytics(func: MainNavCassavaResult.() -> Unit): MainNavCassavaResult {
        return MainNavCassavaResult().apply(func)
    }
}
class MainNavCassavaResult {
    fun addDebugEnd() {
        Thread.sleep(5000)
    }

    fun hasPassedAnalytics(cassavaTestRule: CassavaTestRule, queryFileName: String) {
        MatcherAssert.assertThat(cassavaTestRule.validate(queryFileName), hasAllSuccess())
    }
}

fun waitForLoadCassavaAssert() {
    Thread.sleep(2000)
}

fun waitForData() {
    Thread.sleep(10000)
}

fun hasPassedAnalytics(cassavaTestRule: CassavaTestRule, queryFileName: String) {
    MatcherAssert.assertThat(cassavaTestRule.validate(queryFileName), hasAllSuccess())
}