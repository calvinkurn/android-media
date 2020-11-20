package com.tokopedia.buyerorder

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.buyerorder.unifiedhistory.list.view.activity.UohListActivity
import com.tokopedia.buyerorder.test.R
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohIdlingResource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
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
        private const val QUERY_CLICK_ITEM_LIST = "tracker/transaction/uoh_click_item_list.json"
        private const val QUERY_CLICK_PRIMARY_BUTTON = "tracker/transaction/uoh_click_primary_button.json"
        private const val QUERY_CLICK_THREE_DOTS_MENU = "tracker/transaction/uoh_click_three_dots_menu.json"
        private const val QUERY_ADD_TO_CART = "tracker/transaction/uoh_add_to_cart.json"
        private const val QUERY_CLICK_FILTER_DATE = "tracker/transaction/uoh_click_filter_date_item.json"
        private const val QUERY_APPLY_FILTER_DATE = "tracker/transaction/uoh_apply_filter_date.json"
        private const val QUERY_CLICK_FILTER_STATUS = "tracker/transaction/uoh_click_filter_status_item.json"
        private const val QUERY_APPLY_FILTER_STATUS = "tracker/transaction/uoh_apply_filter_status.json"
        private const val QUERY_CLICK_FILTER_CATEGORY = "tracker/transaction/uoh_click_filter_category_item.json"
        private const val QUERY_APPLY_FILTER_CATEGORY = "tracker/transaction/uoh_apply_filter_category.json"
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
        runBot { login(activityRule) }
        waitForData()

        val uohRecyclerView = activityRule.activity.findViewById<RecyclerView>(com.tokopedia.buyerorder.R.id.rv_order_list)

        // click primary button
        onView(withId(R.id.rv_order_list)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, clickOnViewChild(com.tokopedia.buyerorder.R.id.uoh_btn_action)))
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, QUERY_CLICK_PRIMARY_BUTTON), hasAllSuccess())

        // click three dots menu
        onView(withId(R.id.rv_order_list)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, clickOnViewChild(com.tokopedia.buyerorder.R.id.iv_kebab_menu)))
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, QUERY_CLICK_THREE_DOTS_MENU), hasAllSuccess())

        // click beli lagi
        onView(withId(com.tokopedia.buyerorder.R.id.rv_kebab)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, ViewActions.click()))
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, QUERY_ADD_TO_CART), hasAllSuccess())

        // click order card
        onView(withId(R.id.rv_order_list)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, clickOnViewChild(com.tokopedia.buyerorder.R.id.cl_data_product)))
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, QUERY_CLICK_ITEM_LIST), hasAllSuccess())
        pressBack()

        val itemCount = uohRecyclerView.adapter?.itemCount?:0
        for (i in 0 until itemCount) {
            scrollUohRecyclerViewToPosition(uohRecyclerView, i)
        }
        waitForData()
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, QUERY_VIEW_LIST), hasAllSuccess())

        // search
        onView(withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield)).perform(ViewActions.typeText("product 17"))
        waitForData()
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, QUERY_SEARCH), hasAllSuccess())

        // click filter date
        onView(nthChildOf(withId(com.tokopedia.sortfilter.R.id.sort_filter_items),0)).perform(ViewActions.click())
        onView(withId(com.tokopedia.buyerorder.R.id.rv_option)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, ViewActions.click()))
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, QUERY_CLICK_FILTER_DATE), hasAllSuccess())

        // click apply filter date
        onView(withId(com.tokopedia.buyerorder.R.id.btn_apply)).perform(ViewActions.click())
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, QUERY_APPLY_FILTER_DATE), hasAllSuccess())
        waitForData()

        // click filter status
        onView(nthChildOf(withId(com.tokopedia.sortfilter.R.id.sort_filter_items),1)).perform(ViewActions.click())
        onView(withId(com.tokopedia.buyerorder.R.id.rv_option)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, ViewActions.click()))
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, QUERY_CLICK_FILTER_STATUS), hasAllSuccess())

        // click apply filter status
        onView(withId(com.tokopedia.buyerorder.R.id.btn_apply)).perform(ViewActions.click())
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, QUERY_APPLY_FILTER_STATUS), hasAllSuccess())
        waitForData()

        // click filter category
        onView(nthChildOf(withId(com.tokopedia.sortfilter.R.id.sort_filter_items),2)).perform(scrollTo(), ViewActions.click())
        onView(withId(com.tokopedia.buyerorder.R.id.rv_option)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, ViewActions.click()))
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, QUERY_CLICK_FILTER_CATEGORY), hasAllSuccess())

        // click apply filter category
        onView(withId(com.tokopedia.buyerorder.R.id.btn_apply)).perform(ViewActions.click())
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, QUERY_APPLY_FILTER_CATEGORY), hasAllSuccess())
        waitForData()
    }

    private fun waitForData() {
        Thread.sleep(5000)
    }

    private fun scrollUohRecyclerViewToPosition(uohRecyclerView: RecyclerView, position: Int) {
        val layoutManager = uohRecyclerView.layoutManager as GridLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
    }
}