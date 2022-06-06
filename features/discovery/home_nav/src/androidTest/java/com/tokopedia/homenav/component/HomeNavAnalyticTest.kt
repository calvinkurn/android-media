package com.tokopedia.homenav.component

import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.homenav.environment.InstrumentationHomeMainTestActivity
import com.tokopedia.test.application.annotations.CassavaTest
import org.junit.Rule

/**
 * Created by dhaba
 */
private const val TAG = "HomeNavAnalyticsTest"
@CassavaTest
class HomeNavAnalyticTest {
    @get:Rule
    var activityRule = object: IntentsTestRule<InstrumentationHomeMainTestActivity>(InstrumentationHomeMainTestActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
        }
    }


}