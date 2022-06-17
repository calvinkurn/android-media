package com.tokopedia.homenav.component

import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import org.hamcrest.MatcherAssert

/**
 * Created by dhaba
 */

const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_SHOP_AFFILIATE = "tracker/home_nav/shop_affiliate.json"

fun mainNavCassavaTest(func: MainNavCassavaHelper.() -> Unit) =
    MainNavCassavaHelper().apply(func)

class MainNavCassavaHelper {

    fun waitForData() {
        Thread.sleep(10000)
    }

    fun waitForData2() {
        Thread.sleep(5000)
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
//fun waitForData() {
//    Thread.sleep(4000)
//}
//
//fun addDebugEnd() {
//    Thread.sleep(2000)
//}

fun hasPassedAnalytics(cassavaTestRule: CassavaTestRule, queryFileName: String) {
    MatcherAssert.assertThat(cassavaTestRule.validate(queryFileName), hasAllSuccess())
}

fun waitForLoadCassavaAssert() {
    Thread.sleep(2000)
}