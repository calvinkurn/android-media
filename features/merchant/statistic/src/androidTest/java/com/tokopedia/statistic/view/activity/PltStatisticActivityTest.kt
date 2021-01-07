package com.tokopedia.statistic.view.activity

import androidx.test.rule.ActivityTestRule
import com.tokopedia.statistic.mock.StatisticMockResponseConfig
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.test.application.util.setupGraphqlMockResponseWithCheck
import org.junit.Rule
import org.junit.Test

/**
 * Created By @ilhamsuaib on 07/01/21
 */

class PltStatisticActivityTest {

    @get:Rule
    var activityRule = object : ActivityTestRule<StatisticActivity>(StatisticActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupGraphqlMockResponseWithCheck(StatisticMockResponseConfig())
        }
    }

    @Test
    fun testHomeLayout() {
        Thread.sleep(5000L)
    }
}