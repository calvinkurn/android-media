package com.tokopedia.recharge_credit_card

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.supportsInputMethods
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RechargeCCInstrumentTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @get:Rule
    var activityRule = ActivityTestRule(RechargeCCActivity::class.java)

    @Before
    fun setUp() {
        gtmLogDBSource.deleteAll().subscribe()
    }

    private fun typeCreditCardNumber(ccNumber: String) {
        onView(allOf(supportsInputMethods(), isDescendantOfA(withId(R.id.cc_widget_client_number))))
                .perform(typeText(ccNumber))
    }

    private fun openConfirmationDialogThenClickNext() {
        onView(withId(R.id.cc_button_next)).perform(click())
        onView(withText(R.string.cc_title_dialog))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))

        onView(allOf(withText(R.string.cc_cta_btn_primary), isDisplayed()))
                .inRoot(isDialog())
                .perform(click())
    }

    private fun openConfirmationDialogThenClickBack() {
        onView(withId(R.id.cc_button_next)).perform(click())
        onView(withText(R.string.cc_title_dialog))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))

        onView(allOf(withText(R.string.cc_cta_btn_secondary), isDisplayed()))
                .inRoot(isDialog())
                .perform(click())
    }

    @Test
    fun next_button_is_enabled_on_inserted_valid_credit_card_number() {
        onView(withId(R.id.cc_button_next)).check(matches(not(isEnabled())))
        typeCreditCardNumber(VALID_CC_NUMBER)
        onView(withId(R.id.cc_button_next)).check(matches(isEnabled()))
    }


    @Test
    fun next_button_is_disabled_on_inserted_invalid_credit_card_number() {
        onView(withId(R.id.cc_button_next)).check(matches(not(isEnabled())))
        typeCreditCardNumber(INVALID_CC_NUMBER)
        onView(withId(R.id.cc_button_next)).check(matches(not(isEnabled())))
        onView(withText(R.string.cc_error_invalid_number)).check(matches(isDisplayed()))
    }

    @Test
    fun validate_credit_card_user_journey() {
        typeCreditCardNumber(VALID_CC_NUMBER)
        openConfirmationDialogThenClickBack()
        openConfirmationDialogThenClickNext()

        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY), hasAllSuccess())
    }

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY = "tracker/recharge/recharge_credit_card.json"
        private const val VALID_CC_NUMBER = "4111111111111111"
        private const val INVALID_CC_NUMBER = "4141414141414141"
    }
}