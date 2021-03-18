package com.tokopedia.smartbills

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.smartbills.presentation.activity.SmartBillsActivity
import com.tokopedia.smartbills.presentation.fragment.SmartBillsFragment
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.ResourcePathUtil
import org.hamcrest.core.AllOf
import org.hamcrest.core.AllOf.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SmartBillsActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)
    private val graphqlCacheManager = GraphqlCacheManager()

    @get:Rule
    var activityRule =
            ActivityTestRule(SmartBillsActivity::class.java, false, false)

    @Before
    fun setup() {
        Intents.init()
        graphqlCacheManager.deleteAll()
        gtmLogDBSource.deleteAll().subscribe()
        setupGraphqlMockResponse {
            addMockResponse(
                    KEY_STATEMENT_MONTHS,
                    ResourcePathUtil.getJsonFromResource(PATH_STATEMENT_MONTHS),
                    MockModelConfig.FIND_BY_CONTAINS)

            addMockResponse(
                    KEY_STATEMENT_BILLS,
                    ResourcePathUtil.getJsonFromResource(PATH_STATEMENT_BILLS),
                    MockModelConfig.FIND_BY_CONTAINS)
        }

        InstrumentationAuthHelper.loginInstrumentationTestUser1()

        LocalCacheHandler(context, SmartBillsFragment.SMART_BILLS_PREF).also {
            it.putBoolean(SmartBillsFragment.SMART_BILLS_VIEWED_ONBOARDING_COACH_MARK, true)
            it.applyEditor()
        }

        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(targetContext, SmartBillsActivity::class.java)
        activityRule.launchActivity(intent)
    }

    @Test
    fun validateSmartBills() {
        Thread.sleep(3000)
        validate_onboarding()
        validate_bill_selection()
        validate_bill_detail()
        validate_tooltip()

        ViewMatchers.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, SMART_BILLS_VALIDATOR_QUERY), hasAllSuccess())
    }

    private fun validate_onboarding() {
        Thread.sleep(2000)
        val localCacheHandler = LocalCacheHandler(context, SmartBillsFragment.SMART_BILLS_PREF)
        if (!localCacheHandler.getBoolean(SmartBillsFragment.SMART_BILLS_VIEWED_ONBOARDING_COACH_MARK, false)) {
            onView(ViewMatchers.withText(R.string.smart_bills_onboarding_title_1)).check(ViewAssertions.matches(isDisplayed()))
            onView(withId(R.id.text_next)).perform(click())
            onView(ViewMatchers.withText(R.string.smart_bills_onboarding_title_2)).check(ViewAssertions.matches(isDisplayed()))
            onView(withId(R.id.text_next)).perform(click())
            onView(ViewMatchers.withText(R.string.smart_bills_onboarding_title_3)).check(ViewAssertions.matches(isDisplayed()))
            onView(withId(R.id.text_next)).perform(click())
        }
    }

    private fun validate_tooltip(){
        Intents.intending(IntentMatchers.isInternal()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        Thread.sleep(2000)
        onView(withId(R.id.action_menu_tooltip)).check(ViewAssertions.matches(isDisplayed()))
                .perform(click())
        Thread.sleep(2000)
        onView(allOf(withId(R.id.btn_tooltip_sbm), withText("Pelajari Selengkapnya"))).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.bottom_sheet_close)).perform(click())
    }

    private fun validate_bill_selection() {
        onView(ViewMatchers.withText("Air PDAM - ATB BATAM")).check(ViewAssertions.matches(isDisplayed()))
        val billCheckbox = onView(AllOf.allOf(
                withId(R.id.cb_smart_bills_item),
                ViewMatchers.hasSibling(ViewMatchers.withChild(ViewMatchers.withText("Air PDAM - ATB BATAM")))
        ))
        billCheckbox.perform(click())
        Thread.sleep(2000)
        billCheckbox.perform(click())
        Thread.sleep(2000)
        val billToggleAllCheckbox = onView(withId(R.id.cb_smart_bills_select_all))
        billToggleAllCheckbox.perform(click())
        Thread.sleep(2000)
        billToggleAllCheckbox.perform(click())
        Thread.sleep(2000)
    }

    private fun validate_bill_detail() {
        onView(AllOf.allOf(
                withId(R.id.tv_smart_bills_item_detail),
                ViewMatchers.hasSibling(ViewMatchers.withText("Air PDAM - ATB BATAM"))
        )).perform(click())
        Thread.sleep(2000)
        onView(ViewMatchers.withText("Detail Air PDAM")).check(ViewAssertions.matches(isDisplayed()))
        onView(ViewMatchers.withText("13111516111")).check(ViewAssertions.matches(isDisplayed()))
        onView(ViewMatchers.withText("Rp 102.500")).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.bottom_sheet_close)).perform(click())
    }

    @After
    fun cleanUp() {
        Intents.release()
    }


    companion object {
        private const val KEY_STATEMENT_MONTHS = "rechargeStatementMonths"
        private const val KEY_STATEMENT_BILLS = "rechargeStatementBills"

        private const val PATH_STATEMENT_MONTHS = "statement_months.json"
        private const val PATH_STATEMENT_BILLS = "statement_bills.json"

        private const val SMART_BILLS_VALIDATOR_QUERY = "tracker/recharge/smart_bills_management_test.json"
    }
}
