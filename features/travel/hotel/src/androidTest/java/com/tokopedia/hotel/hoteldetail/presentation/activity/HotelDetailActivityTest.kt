package com.tokopedia.hotel.hoteldetail.presentation.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.hotel.R
import com.tokopedia.hotel.hoteldetail.presentation.activity.mock.HotelDetailMockResponseConfig
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import kotlinx.android.synthetic.main.fragment_hotel_detail.*
import org.junit.*

/**
 * @author by jessica on 16/09/20
 */
class HotelDetailActivityTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @get:Rule
    var activityRule: IntentsTestRule<HotelDetailActivity> = object : IntentsTestRule<HotelDetailActivity>(HotelDetailActivity::class.java) {

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupGraphqlMockResponse(HotelDetailMockResponseConfig())
        }

        override fun getActivityIntent(): Intent {
            return HotelDetailActivity.getCallingIntent(context, "2023-10-10",
                    "2023-10-11", 11, 1, 1, "region",
                    "Jakarta", true, "HOMEPAGE")
        }
    }

    @Before
    fun setUp() {
        gtmLogDBSource.deleteAll().subscribe()
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @Test
    fun checkOnHotelDetailTracking() {
        clickOnSeePhoto()
        clickOnSeeAllReview()
        clickSeeRoomButton()

        Assert.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_HOTEL_PDP),
                hasAllSuccess())
    }

    private fun clickOnSeePhoto() {
        Thread.sleep(1000)
        activityRule.activity.hotelDetailNestedScrollView.scrollTo(0, 100)

        Thread.sleep(3000)
        Espresso.onView(ViewMatchers.withId(R.id.iv_first_photo_preview)).perform(click())

        Thread.sleep(3000)
        Espresso.onView(ViewMatchers.withId(R.id.btn_arrow_back)).perform(click())
    }

    private fun clickOnSeeAllReview() {
        Thread.sleep(3000)
        Espresso.onView(ViewMatchers.withId(R.id.tv_hotel_detail_all_reviews)).perform(click())
    }

    private fun clickSeeRoomButton() {
        Thread.sleep(3000)
        Espresso.onView(ViewMatchers.withId(R.id.btn_see_room)).perform(click())
    }

    @After
    fun tearDown() {
        gtmLogDBSource.deleteAll().subscribe()
    }

    companion object {
        const val ANALYTIC_VALIDATOR_QUERY_HOTEL_PDP = "tracker/travel/hotel/hotel_pdp.json"
    }

}