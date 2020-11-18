package com.tokopedia.buyerorder

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.PerformException
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.buyerorder.unifiedhistory.list.view.activity.UohListActivity
import com.tokopedia.buyerorder.test.R
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohIdlingResource
import com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.viewholder.UohOrderListViewHolder
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationMockHelper
import org.junit.*
import org.junit.Assert.assertThat

/**
 * Created by fwidjaja on 06/11/20.
 */
class UohListTrackingTest {

    companion object {
        private const val QUERY_VIEW_LIST = "tracker/transaction/uoh_view_list.json"
        private const val QUERY_SEARCH = "tracker/transaction/uoh_search.json"
        private const val QUERY_CLICK_ITEM = "tracker/transaction/uoh_click_item.json"
        private const val QUERY_CLICK_FILTER_DATE = "tracker/transaction/uoh_click_filter_date_item.json"
        private const val QUERY_APPLY_FILTER_DATE = "tracker/transaction/uoh_apply_filter_date.json"
        const val KEY_UOH_ORDERS = "GetOrderHistory"
    }

    @get:Rule
    var activityRule = ActivityTestRule<UohListActivity>(UohListActivity::class.java, false, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun setup() {
        gtmLogDBSource.deleteAll().subscribe()
        setupGraphqlMockResponse {
            addMockResponse(KEY_UOH_ORDERS, InstrumentationMockHelper.getRawString(context, R.raw.response_mock_uoh_orders_succeed_manual), MockModelConfig.FIND_BY_CONTAINS)
        }
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

        // need for order detail
        // login()
        // waitForData()

        val uohRecyclerView = activityRule.activity.findViewById<RecyclerView>(com.tokopedia.buyerorder.R.id.rv_order_list)
        val itemCount = uohRecyclerView.adapter?.itemCount?:0

        for (i in 0 until itemCount) {
            scrollUohRecyclerViewToPosition(uohRecyclerView, i)
            // clickOrderItem(uohRecyclerView, i)
        }
        waitForData()
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, QUERY_VIEW_LIST), hasAllSuccess())
        // assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, QUERY_CLICK_ITEM), hasAllSuccess())

        // search
        onView(withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield)).perform(ViewActions.typeText("product 17"))
        waitForData()
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, QUERY_SEARCH), hasAllSuccess())

        // click filter date
        onView(withId(com.tokopedia.buyerorder.R.id.uoh_sort_filter)).perform(ViewActions.click())
        onView(withId(com.tokopedia.buyerorder.R.id.rv_option)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, ViewActions.click()))
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, QUERY_CLICK_FILTER_DATE), hasAllSuccess())


        // click apply filter date
        onView(withId(com.tokopedia.buyerorder.R.id.btn_apply)).perform(ViewActions.click())
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, QUERY_APPLY_FILTER_DATE), hasAllSuccess())
    }

    private fun waitForData() {
        Thread.sleep(5000)
    }

    private fun scrollUohRecyclerViewToPosition(uohRecyclerView: RecyclerView, position: Int) {
        val layoutManager = uohRecyclerView.layoutManager as GridLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
    }

    private fun login() {
        InstrumentationAuthHelper.loginToAnUser(activityRule.activity.application)
    }

    private fun clickOrderItem(uohRecyclerView: RecyclerView, i: Int) {
        when (uohRecyclerView.findViewHolderForAdapterPosition(i)) {
            is UohOrderListViewHolder -> {
                try {
                    onView(withId(uohRecyclerView.id))
                            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(i, ViewActions.click()))
                } catch (e: PerformException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun clickFilterOptionItem(uohRecyclerView: RecyclerView, i: Int) {
        try {
            onView(withId(uohRecyclerView.id))
                    .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(i, ViewActions.click()))
        } catch (e: PerformException) {
            e.printStackTrace()
        }
    }
}