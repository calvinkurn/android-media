package com.tokopedia.hotel.cancellation.presentation.activity

import android.content.Intent
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.core.getAnalyticsWithQuery
import com.tokopedia.analyticsdebugger.validator.core.hasAllSuccess
import com.tokopedia.hotel.R
import com.tokopedia.hotel.cancellation.presentation.activity.mock.HotelCancellationMockResponseConfig
import com.tokopedia.hotel.cancellation.presentation.adapter.HotelCancellationReasonAdapter
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.Matchers.anything
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
            return HotelCancellationActivity.getCallingIntent(context)
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
        Thread.sleep(5000)
        onData(anything()).inAdapterView(withId(R.id.hotel_cancellation_confirmation_button_layout))
                .atPosition(0)
                .onChildView(withText("Cari hotel lainnya"))
                .check(matches(isDisplayed()))
    }

    @After
    fun tearDown() {
        gtmLogDBSource.deleteAll().subscribe()
    }

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_HOTEL_CANCELLATION = "tracker/travel/hotel/hotel_cancellation_page.json"
    }
}