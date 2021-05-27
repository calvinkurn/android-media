package com.tokopedia.buyerorder

import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.IdlingRegistry
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.buyerorder.test.R
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohIdlingResource
import com.tokopedia.buyerorder.unifiedhistory.list.view.activity.UohListActivity
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by fwidjaja on 06/11/20.
 */
class UohListTrackingTest {

    companion object {
        private const val KEY_UOH_ORDERS = "GetOrderHistory"
    }

    @get:Rule
    var activityRule = ActivityTestRule(UohListActivity::class.java, false, false)

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun setup() {
        gtmLogDBSource.deleteAll().subscribe()

        setupGraphqlMockResponse {
            addMockResponse(KEY_UOH_ORDERS, InstrumentationMockHelper.getRawString(context, R.raw.response_mock_uoh_orders_succeed_manual), MockModelConfig.FIND_BY_CONTAINS)
        }

        InstrumentationAuthHelper.loginInstrumentationTestUser1()
    }

    @After
    fun cleanup() {
        IdlingRegistry.getInstance().unregister(UohIdlingResource.countingIdlingResource)
        activityRule.finishActivity()
    }

    @Test
    fun test_uoh_summary() {
        IdlingRegistry.getInstance().register(UohIdlingResource.countingIdlingResource)
        activityRule.launchActivity(null)
        onIdle()

        val query = "tracker/transaction/uoh_summary.json"

        runBot {
            loading()
            clickPrimaryButton()
            clickThreeDotsMenu()
            clickBeliLagi()
            loading()
            clickOrderCard()
            doSearch("product 17")
            clickFilterStatus()
            selectFilterStatus()
            doApplyFilter()
            clickFilterCategory()
            selectFilterCategory()
            doApplyFilter()
            clickFilterDate()
            selectFilterDate()
            doApplyFilter()
        } submit {
            hasPassedAnalytics(cassavaTestRule, query)
        }
    }
}