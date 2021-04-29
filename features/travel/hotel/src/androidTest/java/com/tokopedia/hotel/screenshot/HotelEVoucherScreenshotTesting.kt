package com.tokopedia.hotel.screenshot

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.hotel.evoucher.presentation.activity.HotelEVoucherActivity
import com.tokopedia.hotel.evoucher.presentation.activity.mock.HotelEVoucherMockResponseConfig
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.tokopedia.hotel.R

class HotelEVoucherScreenshotTesting {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val orderId = "QPN-12Q58-VHSO"
    lateinit var hotelEVoucher: RelativeLayout

    @get:Rule
    var activityRule: IntentsTestRule<HotelEVoucherActivity> = object : IntentsTestRule<HotelEVoucherActivity>(HotelEVoucherActivity::class.java) {

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupGraphqlMockResponse(HotelEVoucherMockResponseConfig())
        }
        override fun getActivityIntent(): Intent {
            return HotelEVoucherActivity.getCallingIntent(context, orderId)
        }
    }

    @Before
    fun setUp() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        hotelEVoucher = activityRule.activity.findViewById(R.id.container_root)
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

    private fun scrollToBottom(){
        val bottomPoint = activityRule.activity.findViewById<LinearLayout>(R.id.container_bottom)
        hotelEVoucher.scrollTo(0, bottomPoint.y.toInt())
    }

    private fun filePrefix(): String = "hotel-evoucher"
}