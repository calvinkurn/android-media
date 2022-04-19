package com.tokopedia.search

import android.Manifest
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.search.result.presentation.view.activity.SearchActivity
import com.tokopedia.search.robot.prepare
import org.junit.Rule
import org.junit.Test

// Experimental test class
// Original class: SearchProductTopAdsVerficationTest
internal class SearchProductTopAdsVerficationTestRobot {

    @get:Rule
    var grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @get:Rule
    val activityRule = IntentsTestRule(SearchActivity::class.java, false, false)

    @Test
    fun testTopAdsUrlTracking() {
        prepare {
            usingRule(activityRule)
            loginAsTopAdsUser()
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