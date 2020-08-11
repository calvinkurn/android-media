package com.tokopedia.flight.homepage.presentation.activity

import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test

/**
 * @author by furqan on 04/08/2020
 */
class FlightHomepageActivityTest {

    @get:Rule
    var activityRule: ActivityTestRule<FlightHomepageActivity> = ActivityTestRule(FlightHomepageActivity::class.java)

    @Test
    fun testFlightHomepageLayout() {
        Thread.sleep(1000)
    }
}