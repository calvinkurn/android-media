package com.tokopedia.homenav.component

import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import org.hamcrest.MatcherAssert

/**
 * Created by dhaba
 */

const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_SHOP_AFFILIATE = "tracker/home_nav/shop_affiliate.json"
const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_FAVORITE_SHOP = "tracker/home_nav/favorite_shop.json"

fun waitForData() {
    Thread.sleep(4000)
}

fun addDebugEnd() {
    Thread.sleep(2000)
}

fun hasPassedAnalytics(cassavaTestRule: CassavaTestRule, queryFileName: String) {
    MatcherAssert.assertThat(cassavaTestRule.validate(queryFileName), hasAllSuccess())
}

fun waitForLoadCassavaAssert() {
    Thread.sleep(2000)
}