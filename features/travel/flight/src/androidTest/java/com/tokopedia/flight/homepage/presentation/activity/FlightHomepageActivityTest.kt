package com.tokopedia.flight.homepage.presentation.activity

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.banner.BannerViewPagerAdapter
import com.tokopedia.flight.R
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author by furqan on 04/08/2020
 */
@RunWith(AndroidJUnit4::class)
class FlightHomepageActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @get:Rule
    var activityRule: IntentsTestRule<FlightHomepageActivity> = object : IntentsTestRule<FlightHomepageActivity>(FlightHomepageActivity::class.java) {
        override fun getActivityIntent(): Intent =
                Intent(context, FlightHomepageActivity::class.java)
    }

    @Before
    fun setup() {
        gtmLogDBSource.deleteAll().subscribe()
    }

    @Test
    fun validateFlightHomepage() {
        validateFlightHomepageBannerDisplayedAndScrollable()
        validateFlightHomepageBannerClickableAndPerformClick()
    }

    private fun validateFlightHomepageBannerDisplayedAndScrollable() {
        Thread.sleep(4000)
        if (getBannerItemCount() > 0) {
            onView(withId(R.id.banner_recyclerview)).check(matches(isDisplayed()))
            Thread.sleep(1000)
            if (getBannerItemCount() > 1)
                onView(withId(R.id.banner_recyclerview))
                        .perform(RecyclerViewActions.scrollToPosition<BannerViewPagerAdapter.BannerViewHolder>(
                                getBannerItemCount() - 1))
        } else {
            Thread.sleep(1000)
            onView(withId(R.id.banner_recyclerview)).check(matches(Matchers.not(isDisplayed())))
        }
    }

    private fun validateFlightHomepageBannerClickableAndPerformClick() {
        Thread.sleep(2000)

        if (getBannerItemCount() > 0) {
            onView(withId(R.id.banner_recyclerview)).perform(RecyclerViewActions
                    .actionOnItemAtPosition<BannerViewPagerAdapter.BannerViewHolder>(0, click()))
        }
    }

    private fun getBannerItemCount(): Int {
        val recyclerView: RecyclerView = activityRule.activity.findViewById(R.id.banner_recyclerview) as RecyclerView
        return recyclerView.adapter?.itemCount ?: 0
    }

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_P1 = "tracker/travel/flight/flight_homepage_p1.json"
    }
}