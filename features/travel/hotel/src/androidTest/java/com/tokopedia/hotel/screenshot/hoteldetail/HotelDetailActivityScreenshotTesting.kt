package com.tokopedia.hotel.screenshot.hoteldetail

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.widget.LinearLayout
import androidx.core.widget.NestedScrollView
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.hotel.hoteldetail.presentation.activity.HotelDetailActivity
import com.tokopedia.hotel.hoteldetail.presentation.activity.mock.HotelDetailMockResponseConfig
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.tokopedia.hotel.R

/**
 * @author by astidhiyaa on 28/04/21
 */
class HotelDetailActivityScreenshotTesting {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    lateinit var appBar: AppBarLayout
    lateinit var hotelDetail: NestedScrollView

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
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        appBar = activityRule.activity.findViewById(R.id.app_bar_layout)
        hotelDetail = activityRule.activity.findViewById(R.id.hotelDetailNestedScrollView)
    }

    @Test
    fun screenShotDetail(){
        val activity = activityRule.activity

        Thread.sleep(3000)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            CommonActions.takeScreenShotVisibleViewInScreen(activity.window.decorView, filePrefix(), "top")
        }

        CommonActions.findViewAndScreenShot(R.id.container_bottom, filePrefix(), "bottom-widget")

        activityRule.runOnUiThread {
            scrollToMiddle()
        }

        CommonActions.findViewAndScreenShot(R.id.container_hotel_review, filePrefix(), "review")
        CommonActions.findViewAndScreenShot(R.id.container_hotel_address, filePrefix(), "address")

        activityRule.runOnUiThread {
            scrollToBottom()
        }

        CommonActions.findViewAndScreenShot(R.id.container_facilities, filePrefix(), "facilities")
        CommonActions.findViewAndScreenShot(R.id.container_hotel_info, filePrefix(), "info")
        CommonActions.findViewAndScreenShot(R.id.container_important_info, filePrefix(), "important-info")
        CommonActions.findViewAndScreenShot(R.id.container_hotel_description, filePrefix(), "description")
    }

    private fun filePrefix(): String = "hotel-detail"

    private fun scrollToMiddle(){
        val midPoint = activityRule.activity.findViewById<LinearLayout>(R.id.container_hotel_review)
        appBar.setExpanded(false)
        hotelDetail.scrollTo(0, midPoint.y.toInt())
    }

    private fun scrollToBottom(){
        val bottomPoint = activityRule.activity.findViewById<LinearLayout>(R.id.container_facilities)
        appBar.setExpanded(false)
        hotelDetail.scrollTo(0, bottomPoint.y.toInt())
    }
}