package com.tokopedia.hotel.cancellation.presentation.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.applink.RouteManager
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.hotel.R
import com.tokopedia.hotel.cancellation.presentation.activity.mock.HotelCancellationMockResponseConfig
import com.tokopedia.hotel.cancellation.presentation.adapter.HotelCancellationReasonAdapter
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.core.AllOf
import org.junit.After
import org.junit.Assert
import org.junit.Rule
import org.junit.Test


/**
 * @author by jessica on 22/09/20
 */
class HotelCancellationActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @get:Rule
    var activityRule: IntentsTestRule<HotelCancellationActivity> = object : IntentsTestRule<HotelCancellationActivity>(HotelCancellationActivity::class.java) {

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            gtmLogDBSource.deleteAll().subscribe()
            setupGraphqlMockResponse(HotelCancellationMockResponseConfig())
        }

        override fun getActivityIntent(): Intent {
            return RouteManager.getIntent(context, "tokopedia://hotel/cancel/ABC")
        }
    }

    @Test
    fun cancellationMainFlowTest() {
        viewStepOneCancellation()
        selectCancellationReason()
        clickOnDoneButton()

        Assert.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_HOTEL_CANCELLATION),
                hasAllSuccess())
    }

    private fun viewStepOneCancellation() {
        Thread.sleep(3000)
        onView(withId(R.id.hotelCancellationScrollView)).perform(ViewActions.swipeUp())

        Thread.sleep(3000)
        onView(withId(R.id.hotel_cancellation_button_next)).perform(click())
    }

    private fun selectCancellationReason() {
        Thread.sleep(3000)
        onView(withId(R.id.hotel_cancellation_reason_rv)).perform(
                RecyclerViewActions.actionOnItemAtPosition<HotelCancellationReasonAdapter.ViewHolder>(1,
                        CommonActions.clickChildViewWithId(R.id.hotel_cancellation_reason_radio_button)))

        Thread.sleep(3000)
        onView(withId(R.id.hotel_cancellation_button_next)).perform(click())

        Thread.sleep(3000)
        onView(withId(R.id.dialog_btn_secondary)).perform(click())
    }

    private fun clickOnDoneButton() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        Thread.sleep(5000)
        onView(withTagStringValue("Cari hotel lainnya")).check(matches(isDisplayed())).perform(forceClick())
    }

    private fun withTagStringValue(tagStringValue: String): Matcher<View> {
        return withTagValue(Matchers.`is`(tagStringValue))
    }

    private fun forceClick(): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return AllOf.allOf(isClickable(), isEnabled(), isDisplayed())
            }

            override fun getDescription(): String {
                return "force click"
            }

            override fun perform(uiController: UiController, view: View) {
                view.performClick() // perform click without checking view coordinates.
                uiController.loopMainThreadUntilIdle()
            }
        }
    }

    @After
    fun tearDown() {
        gtmLogDBSource.deleteAll().subscribe()
    }

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_HOTEL_CANCELLATION = "tracker/travel/hotel/hotel_cancellation_page.json"
    }
}