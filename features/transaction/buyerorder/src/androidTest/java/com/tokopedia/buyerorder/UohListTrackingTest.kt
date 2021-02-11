package com.tokopedia.buyerorder

import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.IdlingRegistry
import com.tokopedia.buyerorder.unifiedhistory.list.view.activity.UohListActivity
import com.tokopedia.buyerorder.test.R
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.Utils.getJsonDataFromAsset
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohIdlingResource
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationMockHelper
import org.junit.*

/**
 * Created by fwidjaja on 06/11/20.
 */
class UohListTrackingTest {

    companion object {
        private const val QUERY_SUMMARY_UOH = "tracker/transaction/uoh_summary.json"
        private const val KEY_UOH_ORDERS = "GetOrderHistory"
    }

    @get:Rule
    var activityRule = ActivityTestRule(UohListActivity::class.java, false, false)

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

        val query = getJsonDataFromAsset(context, QUERY_SUMMARY_UOH)
                ?: throw AssertionError("Validator Query not found")

        runBot {
            loading()
            clickPrimaryButton()
            clickThreeDotsMenu()
            clickBeliLagi()
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
            hasPassedAnalytics(gtmLogDBSource, query)
        }
    }
}