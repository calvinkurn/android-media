package com.tokopedia.hotel.screenshot.booking

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.widget.RelativeLayout
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.hotel.booking.presentation.activity.HotelBookingActivity
import com.tokopedia.hotel.booking.presentation.activity.mock.HotelBookingMockResponseConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.user.session.UserSession
import com.tokopedia.hotel.R
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.setupDarkModeTest
import kotlinx.android.synthetic.main.fragment_hotel_booking.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author by astidhiyaa on 29/04/21
 */
abstract class BaseHotelBookingScreenshotTesting {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    var activityRule: IntentsTestRule<HotelBookingActivity> = object : IntentsTestRule<HotelBookingActivity>(HotelBookingActivity::class.java) {

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupDarkModeTest(forceDarkMode())
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
    }

    @Test
    fun screenShot(){
        Thread.sleep(3000)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            CommonActions.takeScreenShotVisibleViewInScreen(activityRule.activity.window.decorView, filePrefix(), "top")
        }

        //SS testing form permintaan khusus
        Espresso.onView(ViewMatchers.withId(R.id.add_request_container)).perform(ViewActions.click())
        CommonActions.findViewAndScreenShot(R.id.room_request_form_container, filePrefix(), "special-request-container")

        CommonActions.findViewAndScreenShot(R.id.hotel_detail_container, filePrefix(), "detail-container")
        CommonActions.findViewAndScreenShot(R.id.booking_room_duration_info, filePrefix(), "duration-info")
        CommonActions.findViewAndScreenShot(R.id.room_request_container, filePrefix(), "room-request-container")

        Espresso.onView(ViewMatchers.withId(R.id.hotelBookingView)).perform(ViewActions.swipeUp())

        CommonActions.takeScreenShotVisibleViewInScreen(activityRule.activity.window.decorView, filePrefix(), "middle")

        Thread.sleep(6000)

        CommonActions.findViewAndScreenShot(R.id.contact_detail_container, filePrefix(), "contact-detail")
        CommonActions.findViewAndScreenShot(R.id.booking_pay_now_promo_container, filePrefix(), "promo-container")

        Espresso.onView(ViewMatchers.withId(R.id.hotelBookingView)).perform(ViewActions.swipeUp())

        Thread.sleep(3000)

        CommonActions.takeScreenShotVisibleViewInScreen(activityRule.activity.window.decorView, filePrefix(), "bottom")

        CommonActions.findViewAndScreenShot(R.id.invoice_summary_container, filePrefix(), "invoice-summary")
        CommonActions.findViewAndScreenShot(R.id.booking_button, filePrefix(), "next-button")

        activityRule.activity.finishAndRemoveTask()
    }

    abstract fun filePrefix(): String

    abstract fun forceDarkMode(): Boolean

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