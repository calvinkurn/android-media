package com.tokopedia.hotel.homepage.presentation.activity

import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test

/**
 * @author by jessica on 04/08/20
 */

class HotelHomepageActivityTest {

    @get:Rule
    var activityRule: ActivityTestRule<HotelHomepageActivity> = ActivityTestRule(HotelHomepageActivity::class.java)

    @Test
    fun testHomeLayout() {
        Thread.sleep(1000)
    }
}