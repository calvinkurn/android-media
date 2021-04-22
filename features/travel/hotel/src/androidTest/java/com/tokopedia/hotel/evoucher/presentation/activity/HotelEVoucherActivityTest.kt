package com.tokopedia.hotel.evoucher.presentation.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.hotel.R
import com.tokopedia.hotel.evoucher.presentation.activity.mock.HotelEVoucherMockResponseConfig
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HotelEVoucherActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val orderId = "QPN-12Q58-VHSO"

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
        intending(anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @Test
    fun testActivity() {
        clickShareButton()
        clickShareAsImagePopupShown()
    }

    private fun clickShareButton() {
        onView(withId(R.id.action_share)).perform(click())
        onView(withText(R.string.hotel_share_as_image)).check(matches(isDisplayed()))
        onView(withText(R.string.hotel_share_as_pdf)).check(matches(isDisplayed()))
        onView(withText(R.string.hotel_save_as_image)).check(matches(isDisplayed()))
    }

    private fun clickShareAsImagePopupShown() {
        onView(withText(R.string.hotel_save_as_image)).perform(click())
        onView(withText(R.string.hotel_save_as_image_success)).check(matches(isDisplayed()))
    }
}