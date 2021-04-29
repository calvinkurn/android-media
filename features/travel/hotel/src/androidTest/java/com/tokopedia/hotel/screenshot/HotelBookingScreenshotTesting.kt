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
import com.tokopedia.hotel.booking.presentation.activity.HotelBookingActivity
import com.tokopedia.hotel.booking.presentation.activity.mock.HotelBookingMockResponseConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.user.session.UserSession
import com.tokopedia.hotel.R
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.unifycomponents.UnifyButton
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HotelBookingScreenshotTesting {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    lateinit var hotelBooking : RelativeLayout

    @get:Rule
    var activityRule: IntentsTestRule<HotelBookingActivity> = object : IntentsTestRule<HotelBookingActivity>(HotelBookingActivity::class.java) {

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupGraphqlMockResponse(HotelBookingMockResponseConfig())
            login()
        }

        override fun getActivityIntent(): Intent {
            return HotelBookingActivity.getCallingIntent(context, "123")
        }
    }

    @Before
    fun setUp() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        hotelBooking = activityRule.activity.findViewById(R.id.hotelBookingView)
    }

    @Test
    fun screenShot(){
        Thread.sleep(3000)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            CommonActions.takeScreenShotVisibleViewInScreen(activityRule.activity.window.decorView, filePrefix(), "top")
        }
        Thread.sleep(3000)
//        CommonActions.findViewAndScreenShot(R.id.hotel_detail_container, filePrefix(), "detail-container")
//        CommonActions.findViewAndScreenShot(R.id.booking_room_duration_info, filePrefix(), "duration-info")
//        CommonActions.findViewAndScreenShot(R.id.room_request_container, filePrefix(), "room-request-container")

        activityRule.runOnUiThread {
            scrollToMiddle()
        }

        CommonActions.takeScreenShotVisibleViewInScreen(activityRule.activity.window.decorView, filePrefix(), "middle")

        Thread.sleep(3000)

//        CommonActions.findViewAndScreenShot(R.id.contact_detail_container, filePrefix(), "contact-detail")
//        CommonActions.findViewAndScreenShot(R.id.booking_pay_now_promo_container, filePrefix(), "promo-container")

        activityRule.runOnUiThread {
            scrollToBottom()
        }

        Thread.sleep(3000)

        CommonActions.takeScreenShotVisibleViewInScreen(activityRule.activity.window.decorView, filePrefix(), "bottom")

//        CommonActions.findViewAndScreenShot(R.id.invoice_summary_container, filePrefix(), "invoice-summary")
//        CommonActions.findViewAndScreenShot(R.id.hotel_booking_important_notes, filePrefix(), "important-notes")
//        CommonActions.findViewAndScreenShot(R.id.booking_button, filePrefix(), "next-button")

        activityRule.activity.finishAndRemoveTask()
    }

    private fun scrollToMiddle(){
        val middlePoint = activityRule.activity.findViewById<LinearLayout>(R.id.booking_pay_now_promo_container)
        hotelBooking.scrollTo(0, middlePoint.y.toInt())
    }

    private fun scrollToBottom(){
        val bottomPoint = activityRule.activity.findViewById<UnifyButton>(R.id.booking_button)
        hotelBooking.scrollTo(0, bottomPoint.y.toInt())
    }

    private fun filePrefix(): String = "hotel-booking"

    private fun login() {
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        val userSession = UserSession(context)
        userSession.setLoginSession(
                true,
                userSession.userId,
                userSession.name,
                userSession.shopId,
                true,
                userSession.shopName,
                userSession.email,
                userSession.isGoldMerchant,
                userSession.phoneNumber
        )
    }
}