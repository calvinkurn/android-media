package com.tokopedia.search

import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.search.result.presentation.view.activity.SearchActivity
import com.tokopedia.search.robot.prepare
import org.junit.Rule
import org.junit.Test

// Experimental test class
// Original class: SearchProductTrackingTest
internal class SearchProductTrackingTestRobot {

    @get:Rule
    val activityRule = IntentsTestRule(SearchActivity::class.java, false, false)

    @get:Rule
    var cassavaRule = CassavaTestRule()

    @Test
    fun testTracking() {
        prepare {
            usingRule(activityRule)
            mockResponse()
            disableOnBoarding()
        } search {
            withKeyword("samsung")
        } interact {
            clickNextOrganicProduct()
            clickNextTopAdsProduct()
            finish()
        } checkGTMTracking {
            allP0TrackingSuccess(cassavaRule)
        }
    }
}