package com.tokopedia.buyerorder

import android.app.Application
import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.intent.rule.IntentsTestRule
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
class UohRecommendationTrackingTest {

    companion object {
        private const val QUERY_SUMMARY_RECOMMENDATION_UOH = "tracker/transaction/uoh_recommendation_summary.json"
        private const val KEY_UOH_ORDERS = "GetOrderHistory"
        private const val KEY_UOH_RECOMMENDATION = "productRecommendation"
        private const val IDLING_RESOURCE = "uoh_fake_login"
    }

    @get:Rule
    var activityRule = IntentsTestRule(UohListActivity::class.java, false, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val idlingResourceLogin: CountingIdlingResource = CountingIdlingResource(IDLING_RESOURCE)
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun setup() {
        gtmLogDBSource.deleteAll().subscribe()

        IdlingRegistry.getInstance().register(idlingResourceLogin)
        IdlingRegistry.getInstance().register(UohIdlingResource.countingIdlingResource)

        setupGraphqlMockResponse {
            addMockResponse(KEY_UOH_ORDERS, InstrumentationMockHelper.getRawString(context, R.raw.response_uoh_orders_empty), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(KEY_UOH_RECOMMENDATION, InstrumentationMockHelper.getRawString(context, R.raw.response_uoh_recommendation_items), MockModelConfig.FIND_BY_CONTAINS)
        }

        InstrumentationAuthHelper.loginToAnUser(context.applicationContext as Application, idlingResourceLogin)
        onIdle()

        activityRule.launchActivity(null)
        onIdle()
    }

    @After
    fun cleanup() {
        IdlingRegistry.getInstance().unregister(UohIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(idlingResourceLogin)
        activityRule.finishActivity()
    }

    @Test
    fun test_uoh_recommendation_summary() {
        val query = getJsonDataFromAsset(context, QUERY_SUMMARY_RECOMMENDATION_UOH)
                ?: throw AssertionError("Validator Query not found")

        runBot {
            loading()
            scrollToRecommendationList()
            clickAtcRecommendation()
            clickRecommendationCard()
        } submit {
            hasPassedAnalytics(gtmLogDBSource, query)
        }
    }
}