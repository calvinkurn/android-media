package com.tokopedia.unifyorderhistory

import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.unifyorderhistory.util.UohIdlingResource
import com.tokopedia.unifyorderhistory.view.activity.UohListActivity
import com.tokopedia.unifyorderhistory.test.R
import com.tokopedia.trackingoptimizer.repository.TrackRepository
import com.tokopedia.trackingoptimizer.sendTrack
import kotlinx.coroutines.GlobalScope
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
    var activityRule = IntentsTestRule(UohListActivity::class.java, false, false)

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        setupGraphqlMockResponse {
            addMockResponse(KEY_UOH_ORDERS, InstrumentationMockHelper.getRawString(context,
                    R.raw.response_mock_uoh_orders_succeed_manual), MockModelConfig.FIND_BY_CONTAINS)
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
            hideKeyboard()
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
            // Force TrackingQueue to send trackers
            sendTrack(GlobalScope, TrackRepository(context)) {
                /* no-op */
            }
            // Wait for TrackingQueue to finish
            Thread.sleep(5_000)
        } submit {
            hasPassedAnalytics(cassavaTestRule, query)
        }
    }
}