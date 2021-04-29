package com.tokopedia.hotel.screenshot

import android.content.Intent
import android.widget.ScrollView
import androidx.appcompat.widget.AppCompatTextView
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.applink.RouteManager
import com.tokopedia.hotel.R
import com.tokopedia.hotel.cancellation.presentation.activity.HotelCancellationActivity
import com.tokopedia.hotel.cancellation.presentation.activity.mock.HotelCancellationMockResponseConfig
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HotelCancellationScreenshotTesting {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    lateinit var hotelCancellation : ScrollView

    @get:Rule
    var activityRule: IntentsTestRule<HotelCancellationActivity> = object : IntentsTestRule<HotelCancellationActivity>(HotelCancellationActivity::class.java) {

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupGraphqlMockResponse(HotelCancellationMockResponseConfig())
        }

        override fun getActivityIntent(): Intent {
            return RouteManager.getIntent(context, "tokopedia://hotel/cancel/ABC")
        }
    }

    @Before
    fun setUp(){
        hotelCancellation = activityRule.activity.findViewById(R.id.hotelCancellationScrollView)
    }

    @Test
    fun screenShot(){
        Thread.sleep(3000)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            CommonActions.takeScreenShotVisibleViewInScreen(activityRule.activity.window.decorView, filePrefix(), "top")
        }

        activityRule.runOnUiThread {
            scrollToBottom()
        }

        Thread.sleep(3000)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            CommonActions.takeScreenShotVisibleViewInScreen(activityRule.activity.window.decorView, filePrefix(), "bottom")
        }

        activityRule.activity.finishAndRemoveTask()
    }

    private fun filePrefix(): String = "hotel-cancellation"

    private fun scrollToBottom(){
        val bottomPoint = activityRule.activity.findViewById<AppCompatTextView>(R.id.hotel_cancellation_page_footer)
        hotelCancellation.scrollTo(0, bottomPoint.y.toInt())
    }
}