package com.tokopedia.vouchercreation.instrumenttest

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.test.application.espresso_component.CommonMatcher
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.TokopediaGraphqlInstrumentationTestHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.activity.CreateMerchantVoucherStepsActivity
import com.tokopedia.vouchercreation.mock.MerchantVoucherCreationMockModelConfig
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MerchantVoucherCreationAnalyticTest {

    companion object {
        private const val VOUCHER_TARGET_PAGE_OPEN = "tracker/merchant/voucher_creation/mvc_voucher_target_page_open.json"
        private const val VOUCHER_TYPE_AND_BUDGET_PAGE_OPEN = "tracker/merchant/voucher_creation/mvc_voucher_type_and_budget_page_open.json"
        private const val VOUCHER_PERIOD_PAGE_OPEN = "tracker/merchant/voucher_creation/mvc_voucher_period_page_open.json"
        private const val VOUCHER_REVIEW_PAGE_OPEN = "tracker/merchant/voucher_creation/mvc_voucher_review_page_open.json"
        private const val VOUCHER_REVIEW_PAGE_ADD_BUTTON_CLICK = "tracker/merchant/voucher_creation/mvc_voucher_review_page_add_button_click.json"
    }

    @get:Rule
    var activityRule: IntentsTestRule<CreateMerchantVoucherStepsActivity> = IntentsTestRule(CreateMerchantVoucherStepsActivity::class.java, false, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun beforeTest() {
        gtmLogDBSource.deleteAll().toBlocking().first()
        setupGraphqlMockResponse(MerchantVoucherCreationMockModelConfig())
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        activityRule.launchActivity(Intent())
    }

    @After
    fun afterTest() {
        gtmLogDBSource.deleteAll().toBlocking().first()
        TokopediaGraphqlInstrumentationTestHelper.deleteAllDataInDb()
    }

    @Test
    fun validateMerchantVoucherCreationJourneyTrackers() {
        // swipe the recycler view up to make the next button visible to espresso
        onView(CommonMatcher.withTagStringValue("voucher_target_recycler_view")).perform(swipeUp())
        // input the voucher name
        onView(Matchers.allOf(withId(R.id.text_field_input), isDescendantOfA(withId(R.id.fillVoucherNameTextfield))))
                .perform(ViewActions.typeText("Nama Voucher"), ViewActions.closeSoftKeyboard())
        // click the next button
        onView(CommonMatcher.withTagStringValue("voucher_target_next_step_button"))
                .perform(click())
        // scroll to the next button and click the button
        onView(withId(R.id.rvMvcVoucherType))
                .perform(actionOnItem<RecyclerView.ViewHolder>(hasDescendant(allOf(withId(R.id.nextButton))), scrollTo()))
                .perform(actionOnItem<RecyclerView.ViewHolder>(hasDescendant(allOf(withId(R.id.nextButton))), click()))
        // click the next button to proceed to the review page
        onView(withId(R.id.setDateNextButton)).perform(click())
//        Need further investigation what is the root cause of AppNotIdleException that occurred during the voucher creation process
//        Thread.sleep(1000)
//        // scroll to and click the add voucher button
//        onView(CommonMatcher.withTagStringValue("voucher_creation_review_recycler_view"))
//                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(10, scrollTo()))
//                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(10, click()))
        // validate open voucher target page tracker
        doAnalyticDebuggerTest(VOUCHER_TARGET_PAGE_OPEN)
        // validate open voucher type and budget page tracker
        doAnalyticDebuggerTest(VOUCHER_TYPE_AND_BUDGET_PAGE_OPEN)
        // validate open voucher period page tracker
        doAnalyticDebuggerTest(VOUCHER_PERIOD_PAGE_OPEN)
        // validate open voucher review page tracker
        doAnalyticDebuggerTest(VOUCHER_REVIEW_PAGE_OPEN)
    }

    private fun doAnalyticDebuggerTest(fileName: String) {
        MatcherAssert.assertThat(
                getAnalyticsWithQuery(gtmLogDBSource, context, fileName),
                hasAllSuccess()
        )
    }
}