package com.tokopedia.buyerorder

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by fwidjaja on 06/11/20.
 */
class UohListTrackingTest {
    @get:Rule
    var activityRule = ActivityTestRule<UohListActivity>(UohListActivity::class.java, false, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun setup() {
        gtmLogDBSource.deleteAll().toBlocking().first()
        setupGraphqlMockResponse {
            addMockResponse(KEY_UOH_ORDERS, InstrumentationMockHelper.getRawString(context, R.raw.response_mock_uoh_orders_with_selesai_buttons), MockModelConfig.FIND_BY_CONTAINS)
        }
        // do this need to logged in?
        // InstrumentationAuthHelper.loginToAnUser(targetContext.applicationContext as Application, idlingResource)
        IdlingRegistry.getInstance().register(UohIdlingResource.countingIdlingResource)
    }

    @After
    fun cleanup() {
        IdlingRegistry.getInstance().unregister(UohIdlingResource.countingIdlingResource)
        activityRule.finishActivity()
    }

    @Test
    fun test_openScreen() {
        runBot { launchFrom(activityRule) }
        waitForData()

        val query = getJsonDataFromAsset(context, "tracker/transaction/uoh_list.json")
                ?: throw AssertionError("Validator Query not found")
        submit { hasPassedAnalytics(gtmLogDBSource, query) }
    }

    /*private fun performUserJourney() {
        *//*onView(withId(recyclerViewId)).check(matches(isDisplayed()))

        activityRule.activity.finish()*//*

        val uohRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.rv_order_list)
        val itemCount = uohRecyclerView.adapter?.itemCount?:0

        for (i in 0 until itemCount) {
            scrollUohRecyclerViewToPosition(uohRecyclerView, i)
            // checkProduct(uohRecyclerView, i)
        }
        waitForData()
    }*/

    private fun scrollUohRecyclerViewToPosition(uohRecyclerView: RecyclerView, position: Int) {
        val layoutManager = uohRecyclerView.layoutManager as GridLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
    }

    private fun login() {
        InstrumentationAuthHelper.loginToAnUser(activityRule.activity.application)
    }

    private fun waitForData() {
        Thread.sleep(5000)
    }

    companion object {
        const val IDLING_RESOURCE = "uoh_fake_login"
        const val KEY_UOH_ORDERS = "GetOrderHistory"
    }
}