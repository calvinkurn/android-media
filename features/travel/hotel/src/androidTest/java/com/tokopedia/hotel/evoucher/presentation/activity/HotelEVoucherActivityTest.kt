package com.tokopedia.hotel.evoucher.presentation.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.hotel.R
import com.tokopedia.hotel.evoucher.presentation.activity.mock.HotelEVoucherMockResponseConfig
import com.tokopedia.hotel.evoucher.presentation.adapter.HotelShareAsPdfAdapter
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.Matcher
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
        clickPhoneNumber()
        clickShareAsPdfPopUpShown()
    }

    private fun clickShareButton() {
        onView(withId(R.id.action_share)).perform(click())
        Thread.sleep(2000)
        onView(withText(R.string.hotel_share_as_image)).check(matches(isDisplayed()))
        onView(withText(R.string.hotel_share_as_pdf)).check(matches(isDisplayed()))
        onView(withText(R.string.hotel_save_as_image)).check(matches(isDisplayed()))
    }

    private fun clickShareAsImagePopupShown() {
        onView(withText(R.string.hotel_save_as_image)).perform(click())
        Thread.sleep(500)
        onView(withText(R.string.hotel_save_as_image_success)).check(matches(isDisplayed()))
        Thread.sleep(1000)
    }

    private fun clickShareAsPdfPopUpShown(){
        onView(withId(R.id.action_share)).perform(click())
        Thread.sleep(2000)
        onView(withText(R.string.hotel_share_as_pdf)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.ev_email)).perform(typeText("te.digital@tokopedia.com"))
        onView(withId(R.id.btn_send_email)).check(matches(isEnabled()))
        Thread.sleep(2000)
        onView(withId(R.id.container_add_email)).perform(click())
        onView(withId(R.id.ev_email)).perform(typeText("te1.digital@tokopedia.com"))
        onView(withId(R.id.btn_send_email)).check(matches(isEnabled()))
        Thread.sleep(2000)
        onView(withId(R.id.container_add_email)).perform(click())
        onView(withId(R.id.rv_email_list)).perform(
            RecyclerViewActions.actionOnItemAtPosition<HotelShareAsPdfAdapter.HotelShareAsPdfViewHolder>(0, clickChildViewWithId(R.id.iv_delete))
        )
        onView(withId(R.id.btn_send_email)).perform(click())
    }

    private fun clickPhoneNumber(){
        onView(withId(R.id.btn_nha_phone)).perform(click())
        Thread.sleep(3000L)
        intended((hasAction(Intent.ACTION_DIAL)))
    }

    private fun clickChildViewWithId(id: Int): ViewAction {
        return object : ViewAction{
            override fun getConstraints(): Matcher<View>? {
                return null
            }

            override fun getDescription(): String {
                return "Click on a child view with specified id."
            }

            override fun perform(uiController: UiController?, view: View?) {
                val v = view?.findViewById<View>(id)
                v?.performClick()
            }
        }
    }
}