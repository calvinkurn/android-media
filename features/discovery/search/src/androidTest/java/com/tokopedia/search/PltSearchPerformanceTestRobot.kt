package com.tokopedia.search

import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.search.result.presentation.view.activity.SearchActivity
import com.tokopedia.search.robot.prepare
import com.tokopedia.test.application.TestRepeatRule
import org.junit.Rule
import org.junit.Test

// Experimental test class
// Original class: PltSearchPerformanceTest
// Still require more testing and data comparison using jenkins performance test
class PltSearchPerformanceTestRobot {

    @get:Rule
    var activityRule = IntentsTestRule(SearchActivity::class.java, false, false)

    @get:Rule
    var testRepeatRule: TestRepeatRule = TestRepeatRule()

    @Test
    fun testPageLoadTimePerformance() {
        prepare {
            usingRule(activityRule)
            mockResponseWithCheck()
        } search {
            withKeyword("samsung")
        } interact {
            savePLTPerformanceResultData()
            finish()
        }
    }
}