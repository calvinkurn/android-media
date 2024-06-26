package com.tokopedia.hotel.screenshot.cancellation

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
import com.tokopedia.test.application.util.setupDarkModeTest
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author by astidhiyaa on 29/04/21
 */
abstract class BaseHotelCancellationScreenshotTesting {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    lateinit var hotelCancellation : ScrollView

    @get:Rule
    var activityRule: IntentsTestRule<HotelCancellationActivity> = object : IntentsTestRule<HotelCancellationActivity>(HotelCancellationActivity::class.java) {

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupDarkModeTest(forceDarkMode())
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

    abstract fun filePrefix(): String

    abstract fun forceDarkMode(): Boolean

    private fun scrollToBottom(){
        val bottomPoint = activityRule.activity.findViewById<AppCompatTextView>(R.id.hotel_cancellation_page_footer)
        hotelCancellation.scrollTo(0, bottomPoint.y.toInt())
    }
}