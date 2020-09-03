package com.tokopedia.search

import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.search.result.presentation.view.activity.SearchActivity
import com.tokopedia.search.robot.prepare
import org.junit.Rule
import org.junit.Test

// Experimental test class
// Original class: SearchProductTopAdsVerficationTest
internal class SearchProductTopAdsVerficationTestRobot {

    @get:Rule
    val activityRule = IntentsTestRule(SearchActivity::class.java, false, false)

    @Test
    fun testTopAdsUrlTracking() {
        prepare {
            usingRule(activityRule)
            disableOnBoarding()
        } search {
            withKeyword("samsung")
        } interact {
            clickAllTopAdsProduct()
            finish()
        } checkTopAdsTracking {
            allSuccess()
        }
    }
}