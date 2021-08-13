package com.tokopedia.buyerorder

import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.cassava.validator.Utils.getJsonDataFromAsset
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
class UohRecommendationTrackingTest {

    companion object {
        private const val KEY_UOH_ORDERS = "GetOrderHistory"
        private const val KEY_UOH_RECOMMENDATION = "productRecommendation"
    }

    @get:Rule
    var activityRule = IntentsTestRule(UohListActivity::class.java, false, false)

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun setup() {
        gtmLogDBSource.deleteAll().subscribe()

        IdlingRegistry.getInstance().register(UohIdlingResource.countingIdlingResource)

        setupGraphqlMockResponse {
            addMockResponse(KEY_UOH_ORDERS, InstrumentationMockHelper.getRawString(context, R.raw.response_uoh_orders_empty), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(KEY_UOH_RECOMMENDATION, InstrumentationMockHelper.getRawString(context, R.raw.response_uoh_recommendation_items), MockModelConfig.FIND_BY_CONTAINS)
        }

        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        onIdle()

        activityRule.launchActivity(null)
        onIdle()
    }

    @After
    fun cleanup() {
        IdlingRegistry.getInstance().unregister(UohIdlingResource.countingIdlingResource)
        activityRule.finishActivity()
    }

    @Test
    fun test_uoh_recommendation_summary() {
        val query = "tracker/transaction/uoh_recommendation_summary.json"

        runBot {
            loading()
            scrollToRecommendationList()
            clickAtcRecommendation()
            clickRecommendationCard()
        } submit {
            hasPassedAnalytics(cassavaTestRule, query)
        }
    }
}