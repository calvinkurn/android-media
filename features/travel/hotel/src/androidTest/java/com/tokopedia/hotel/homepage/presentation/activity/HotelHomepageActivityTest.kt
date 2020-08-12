package com.tokopedia.hotel.homepage.presentation.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.hotel.R
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity.Companion.HOTEL_DESTINATION_NAME
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity.Companion.HOTEL_DESTINATION_SEARCH_ID
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity.Companion.HOTEL_DESTINATION_SEARCH_TYPE
import com.tokopedia.hotel.homepage.presentation.activity.HotelHomepageActivity.Companion.getCallingIntent
import com.tokopedia.hotel.homepage.presentation.activity.mock.HotelMockResponseConfig
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.test.application.util.setupGraphqlMockResponseWithCheck
import org.hamcrest.core.AllOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/**
 * @author by jessica on 04/08/20
 */

class HotelHomepageActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @get:Rule
    var activityRule: ActivityTestRule<HotelHomepageActivity> = object : IntentsTestRule<HotelHomepageActivity>(HotelHomepageActivity::class.java) {
        override fun getActivityIntent(): Intent {
            val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
            return Intent(targetContext, HotelHomepageActivity::class.java)
        }
    }

    @Before
    fun setUp() {
        gtmLogDBSource.deleteAll().subscribe()
        setupGraphqlMockResponse(HotelMockResponseConfig())
    }

    private fun createDummyDestination(): Instrumentation.ActivityResult {
        val resultData = Intent()
        resultData.putExtra(HOTEL_DESTINATION_NAME, "Jakarta")
        resultData.putExtra(HOTEL_DESTINATION_SEARCH_TYPE, "regionOrigin")
        resultData.putExtra(HOTEL_DESTINATION_SEARCH_ID, "835")
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }

    @Test
    fun testHotelHomepageBanner() {
        slidePromoBanner()
        clickSeeAllPromoBanner()
        clickPromoBanner()
    }

    @Test
    fun testClickRecentSearchWidget() {
        clickRecentSearchWidget()
    }

    @Test
    fun testHomeLayout() {
        clickOnChangeDestination()
        modifyCheckInCheckOutDate()
        modifyCheckOutDate()
        modifyGuestAndRoomCount()
        clickSubmitButton()
    }

    private fun clickSubmitButton() {
        onView(withId(R.id.btn_hotel_homepage_search)).check(matches(isDisplayed())).perform(ViewActions.click())
        Thread.sleep(3000)
    }

    private fun clickOnChangeDestination() {
        Thread.sleep(2000)
        intending(hasComponent(HotelDestinationActivity::class.java.name)).respondWith(createDummyDestination())
        onView(withId(R.id.tv_hotel_homepage_destination)).perform(ViewActions.click())
        intended(AllOf.allOf(hasComponent(HotelDestinationActivity::class.java.name)))

        onView(withId(R.id.tv_hotel_homepage_destination)).check(matches(withText("Jakarta")))

        Thread.sleep(2000)
    }

    private fun modifyGuestAndRoomCount() {
        Thread.sleep(1000)
        onView(withId(R.id.tv_hotel_homepage_guest_info)).perform(ViewActions.click())
        Thread.sleep(1000)

        onView(AllOf.allOf(withId(R.id.image_button_plus),
                isDescendantOfA(AllOf.allOf(withId(R.id.spv_hotel_room),
                        hasDescendant(AllOf.allOf(withId(R.id.textview_title), withText("Kamar")))))))
                .perform(ViewActions.click())
                .check(matches(isDisplayed()))
        Thread.sleep(1000)

        onView(AllOf.allOf(withId(R.id.image_button_plus),
                isDescendantOfA(AllOf.allOf(withId(R.id.spv_hotel_adult),
                        hasDescendant(AllOf.allOf(withId(R.id.textview_title), withText("Tamu")))))))
                .perform(ViewActions.click())
                .check(matches(isDisplayed()))
        Thread.sleep(1000)

        onView(AllOf.allOf(withId(R.id.btn_hotel_save_guest))).perform(ViewActions.click())
        Thread.sleep(2000)
    }

    private fun modifyCheckInCheckOutDate() {

    }

    private fun modifyCheckOutDate() {

    }

    private fun slidePromoBanner() {

    }

    private fun clickSeeAllPromoBanner() {

    }

    private fun clickPromoBanner() {

    }

    private fun clickRecentSearchWidget() {

    }

    @After
    fun tearDown() {
        gtmLogDBSource.deleteAll().subscribe()
    }
}