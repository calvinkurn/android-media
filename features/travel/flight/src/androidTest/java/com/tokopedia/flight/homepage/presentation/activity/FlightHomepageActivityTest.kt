package com.tokopedia.flight.homepage.presentation.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.core.getAnalyticsWithQuery
import com.tokopedia.analyticsdebugger.validator.core.hasAllSuccess
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
        intending(anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @Test
    fun validateFlightHomepage() {
        validateFlightHomepageBannerDisplayedAndScrollable()
        validateFlightHomepageBannerClickableAndPerformClick()
        validateFlightHomepageSearchClick()

        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_P1),
                hasAllSuccess())
    }

    private fun validateFlightHomepageSearchClick() {
        Thread.sleep(2000)
        onView(withId(R.id.btnFlightSearch)).perform(click())
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