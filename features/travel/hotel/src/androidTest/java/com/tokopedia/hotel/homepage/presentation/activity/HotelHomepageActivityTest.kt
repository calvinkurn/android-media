package com.tokopedia.hotel.homepage.presentation.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.banner.BannerViewPagerAdapter
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.hotel.R
import com.tokopedia.hotel.cancellation.presentation.activity.mock.HotelCancellationMockResponseConfig
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity.Companion.HOTEL_DESTINATION_NAME
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity.Companion.HOTEL_DESTINATION_SEARCH_ID
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity.Companion.HOTEL_DESTINATION_SEARCH_TYPE
import com.tokopedia.hotel.homepage.presentation.activity.mock.HotelHomepageMockResponseConfig
import com.tokopedia.hotel.homepage.presentation.adapter.viewholder.HotelLastSearchViewHolder
import com.tokopedia.test.application.espresso_component.CommonMatcher.withTagStringValue
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf
import org.junit.*
import java.util.*


/**
 * @author by jessica on 04/08/20
 */

class HotelHomepageActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @get:Rule
    var activityRule: IntentsTestRule<HotelHomepageActivity> = object : IntentsTestRule<HotelHomepageActivity>(HotelHomepageActivity::class.java) {

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            gtmLogDBSource.deleteAll().subscribe()
            setupGraphqlMockResponse(HotelHomepageMockResponseConfig())
        }

        override fun getActivityIntent(): Intent {
            return HotelHomepageActivity.getCallingIntent(context)
        }
    }

    @Before
    fun setUp() {
        intending(anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    private fun createDummyDestination(): Instrumentation.ActivityResult {
        val resultData = Intent()
        resultData.putExtra(HOTEL_DESTINATION_NAME, "Jakarta")
        resultData.putExtra(HOTEL_DESTINATION_SEARCH_TYPE, "regionOrigin")
        resultData.putExtra(HOTEL_DESTINATION_SEARCH_ID, "835")
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }

    @Test
    fun testHomeLayout() {
        clickOnChangeDestination()
        changeDate()
        modifyGuestAndRoomCount()
        clickSubmitButton()

        slidePromoBanner()
        clickPromoBanner()

        clickRecentSearchWidget()

        Assert.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_HOTEL_HOMEPAGE),
                hasAllSuccess())
    }

    private fun clickSubmitButton() {
        onView(withId(R.id.btn_hotel_homepage_search)).check(matches(isDisplayed())).perform(ViewActions.click())
        Thread.sleep(3000)
    }

    private fun clickOnChangeDestination() {
        Thread.sleep(4000)
        intending(hasComponent(HotelDestinationActivity::class.java.name)).respondWith(createDummyDestination())
        onView(withTagStringValue(R.id.tv_hotel_homepage_destination.toString())).perform(ViewActions.click())
        intended(AllOf.allOf(hasComponent(HotelDestinationActivity::class.java.name)))

        onView(withTagStringValue(R.id.tv_hotel_homepage_destination.toString())).check(matches(withText("Jakarta")))

        Thread.sleep(2000)
    }

    private fun modifyGuestAndRoomCount() {
        Thread.sleep(1000)
        onView(withTagStringValue(R.id.tv_hotel_homepage_guest_info.toString())).perform(ViewActions.click())
        Thread.sleep(1000)

        onView(AllOf.allOf(withId(R.id.image_button_plus),
                isDescendantOfA(AllOf.allOf(withId(R.id.spv_hotel_room),
                        hasDescendant(AllOf.allOf(withId(R.id.textview_title), withText("Kamar")))))))
                .perform(click())
                .check(matches(isDisplayed()))
        Thread.sleep(1000)

        onView(AllOf.allOf(withId(R.id.image_button_plus),
                isDescendantOfA(AllOf.allOf(withId(R.id.spv_hotel_adult),
                        hasDescendant(AllOf.allOf(withId(R.id.textview_title), withText("Tamu")))))))
                .perform(click())
                .check(matches(isDisplayed()))
        Thread.sleep(1000)

        onView(AllOf.allOf(withId(R.id.btn_hotel_save_guest))).perform(ViewActions.click())
        Thread.sleep(2000)
    }

    private fun slidePromoBanner() {
        Thread.sleep(2000)
        onView(withId(R.id.hotelHomepageScrollView)).perform(swipeUp())
        Thread.sleep(2000)
        if (getBannerItemCount() > 0) {
            onView(withId(R.id.banner_hotel_homepage_promo)).check(matches(isDisplayed()))
            Thread.sleep(1000)
        } else {
            Thread.sleep(1000)
            onView(withId(R.id.banner_hotel_homepage_promo)).check(matches(org.hamcrest.Matchers.not(isDisplayed())))
        }
    }

    private fun getBannerItemCount(): Int {
        val carouselUnify: CarouselUnify = activityRule.activity.findViewById(R.id.banner_hotel_homepage_promo)
        return carouselUnify.indicatorCount.toInt()
    }

    private fun getLastSearchCount(): Int {
        val recyclerView: RecyclerView = activityRule.activity.findViewById(R.id.rv_hotel_homepage_last_search)
        return recyclerView.adapter?.itemCount ?: 0
    }

    private fun clickPromoBanner() {
        Thread.sleep(2000)

        if (getBannerItemCount() > 0) {
            onView(withId(R.id.banner_hotel_homepage_promo)).perform(click())
        }
    }

    private fun clickRecentSearchWidget() {
        Thread.sleep(4000)

        if (getLastSearchCount() > 0) {
            onView(withId(R.id.rv_hotel_homepage_last_search)).perform(RecyclerViewActions
                    .actionOnItemAtPosition<HotelLastSearchViewHolder>(0, click()))
        }
    }

    private fun changeDate() {
        Thread.sleep(3000)
        onView(withTagStringValue(R.id.tv_hotel_homepage_checkout_date.toString())).perform(click())

        Thread.sleep(3000)
        val cal = Calendar.getInstance()
        cal.time = TravelDateUtil.addTimeToSpesificDate(TravelDateUtil.getCurrentCalendar().time,
                Calendar.DATE, 2)
        var tomorrowDate = cal[Calendar.DATE]

        if (tomorrowDate > 1) {
            try {
                onView(getElementFromMatchAtPosition(withText(tomorrowDate.toString()), 0)).perform(click())
            } catch (e: Exception) {
                onView(getElementFromMatchAtPosition(withText(tomorrowDate.toString()), 2)).perform(click())
            }
        } else {
            onView(getElementFromMatchAtPosition(withText(tomorrowDate.toString()), 2)).perform(click())
        }
    }

    fun getElementFromMatchAtPosition(
            matcher: Matcher<View>,
            position: Int
    ): Matcher<View?>? {
        return object : BaseMatcher<View?>() {
            var counter = 0
            override fun matches(item: Any): Boolean {
                if (matcher.matches(item)) {
                    if (counter == position) {
                        counter++
                        return true
                    }
                    counter++
                }
                return false
            }

            override fun describeTo(description: Description) {
                description.appendText("Element at hierarchy position $position")
            }
        }
    }

    @After
    fun tearDown() {
        gtmLogDBSource.deleteAll().subscribe()
    }

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_HOTEL_HOMEPAGE = "tracker/travel/hotel/hotel_homepage.json"
    }
}