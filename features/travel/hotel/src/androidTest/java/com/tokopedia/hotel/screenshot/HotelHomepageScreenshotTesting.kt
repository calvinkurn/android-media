package com.tokopedia.hotel.screenshot

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.core.widget.NestedScrollView
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.common.travel.widget.TravelVideoBannerWidget
import com.tokopedia.hotel.homepage.presentation.activity.HotelHomepageActivity
import com.tokopedia.hotel.homepage.presentation.activity.mock.HotelHomepageMockResponseConfig
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.setupDarkModeTest
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author by astidhiyaa on 27/04/21
 */
class HotelHomepageScreenshotTesting {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    var activityRule: IntentsTestRule<HotelHomepageActivity> = object : IntentsTestRule<HotelHomepageActivity>(HotelHomepageActivity::class.java) {

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupGraphqlMockResponse(HotelHomepageMockResponseConfig())
            setupDarkModeTest(false)
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

        CommonActions.findViewAndScreenShot(com.tokopedia.hotel.R.id.hotel_search_form_container, filePrefix(), "search-container")

        CommonActions.findViewAndScreenShot(com.tokopedia.hotel.R.id.hotel_container_last_search, filePrefix(), "recent-search")

        activityRule.runOnUiThread {
            scrollToBottom()
        }

        CommonActions.findViewAndScreenShot(com.tokopedia.hotel.R.id.hotel_container_promo, filePrefix(), "promo-container")

        CommonActions.findViewAndScreenShot(com.tokopedia.hotel.R.id.hotel_homepage_video_banner, filePrefix(), "video-banner")

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

    private fun filePrefix(): String = "hotel-homepage"
}