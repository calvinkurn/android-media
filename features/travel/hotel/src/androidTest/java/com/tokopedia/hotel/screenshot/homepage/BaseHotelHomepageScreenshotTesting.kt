package com.tokopedia.hotel.screenshot.homepage

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.core.widget.NestedScrollView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.common.travel.widget.TravelVideoBannerWidget
import com.tokopedia.hotel.R
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity
import com.tokopedia.hotel.homepage.presentation.activity.HotelHomepageActivity
import com.tokopedia.hotel.homepage.presentation.activity.mock.HotelHomepageMockResponseConfig
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.espresso_component.CommonMatcher
import com.tokopedia.test.application.util.setupDarkModeTest
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.core.AllOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author by astidhiyaa on 27/04/21
 */
abstract class BaseHotelHomepageScreenshotTesting {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    var activityRule: IntentsTestRule<HotelHomepageActivity> = object : IntentsTestRule<HotelHomepageActivity>(HotelHomepageActivity::class.java) {

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupDarkModeTest(forceDarkMode())
            setupGraphqlMockResponse(HotelHomepageMockResponseConfig())
        }

        override fun getActivityIntent(): Intent {
            return HotelHomepageActivity.getCallingIntent(context)
        }
    }

    @Before
    fun setUp() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @Test
    fun screenShot() {
        val activity = activityRule.activity
        turnOffDynamicCarousel()

        Thread.sleep(3000)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            CommonActions.takeScreenShotVisibleViewInScreen(activity.window.decorView, filePrefix(), "top")
        }

        //SS testing for bottom sheet menu
        Espresso.onView(ViewMatchers.withId(R.id.action_overflow_menu)).perform(ViewActions.click())
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            CommonActions.takeScreenShotVisibleViewInScreen(activity.window.decorView, filePrefix(), "bs-menu")
        }
        Espresso.onView(ViewMatchers.withId(R.id.bottom_sheet_close)).perform(ViewActions.click())

        CommonActions.findViewAndScreenShot(R.id.hotel_search_form_container, filePrefix(), "search-container")

        //SS testing destination
        Intents.intending(IntentMatchers.hasComponent(HotelDestinationActivity::class.java.name)).respondWith(createDummyDestination())
        Espresso.onView(CommonMatcher.withTagStringValue(R.id.tv_hotel_homepage_destination.toString())).perform(ViewActions.click())
        Intents.intended(AllOf.allOf(IntentMatchers.hasComponent(HotelDestinationActivity::class.java.name)))
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            CommonActions.takeScreenShotVisibleViewInScreen(activity.window.decorView, filePrefix(), "destination")
        }

        //SS testing guest and room
        Espresso.onView(CommonMatcher.withTagStringValue(R.id.tv_hotel_homepage_guest_info.toString())).perform(ViewActions.click())
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            CommonActions.takeScreenShotVisibleViewInScreen(activity.window.decorView, filePrefix(), "bs-guest-info")
        }
        Espresso.onView(ViewMatchers.withId(R.id.bottom_sheet_close)).perform(ViewActions.click())

        //SS testing calendar
        Espresso.onView(CommonMatcher.withTagStringValue(R.id.tv_hotel_homepage_checkout_date.toString())).perform(ViewActions.click())
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            CommonActions.takeScreenShotVisibleViewInScreen(activity.window.decorView, filePrefix(), "bs-guest-info")
        }
        Espresso.onView(ViewMatchers.withId(R.id.bottom_sheet_close)).perform(ViewActions.click())

        CommonActions.findViewAndScreenShot(R.id.hotel_container_last_search, filePrefix(), "recent-search")

        activityRule.runOnUiThread {
            scrollToBottom()
        }

        CommonActions.findViewAndScreenShot(R.id.hotel_container_promo, filePrefix(), "promo-container")

        CommonActions.findViewAndScreenShot(R.id.hotel_homepage_video_banner, filePrefix(), "video-banner")

//            CommonActions.findViewAndScreenShot(com.tokopedia.hotel.R.id.widget_hotel_homepage_popular_cities, filePrefix(), "widget-popular-cities")

        activityRule.activity.finishAndRemoveTask()
    }

    private fun turnOffDynamicCarousel() {
        val carousel = activityRule.activity.findViewById<CarouselUnify>(com.tokopedia.hotel.R.id.banner_hotel_homepage_promo)
        carousel.apply {
            autoplay = false
        }
    }

    private fun scrollToBottom() {
        val bottomView = activityRule.activity.findViewById<TravelVideoBannerWidget>(com.tokopedia.hotel.R.id.hotel_homepage_video_banner)
        val hotelHomepage = activityRule.activity.findViewById<NestedScrollView>(com.tokopedia.hotel.R.id.hotelHomepageScrollView)
        hotelHomepage.requestChildFocus(bottomView, bottomView)
    }

    private fun createDummyDestination(): Instrumentation.ActivityResult {
        val resultData = Intent()
        resultData.putExtra(HotelDestinationActivity.HOTEL_DESTINATION_NAME, "Jakarta")
        resultData.putExtra(HotelDestinationActivity.HOTEL_DESTINATION_SEARCH_TYPE, "regionOrigin")
        resultData.putExtra(HotelDestinationActivity.HOTEL_DESTINATION_SEARCH_ID, "835")
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }

    abstract fun filePrefix(): String

    abstract fun forceDarkMode(): Boolean
}