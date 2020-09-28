package com.tokopedia.deals.home.ui.activity

import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test

/**
 * @author by jessica on 28/09/20
 */
class DealsHomeActivityTest {
    @get:Rule
    var activityRule: ActivityTestRule<DealsHomeActivity> = ActivityTestRule(DealsHomeActivity::class.java)

    @Test
    fun testHomeLayout() {
        Thread.sleep(1000)
    }
}
