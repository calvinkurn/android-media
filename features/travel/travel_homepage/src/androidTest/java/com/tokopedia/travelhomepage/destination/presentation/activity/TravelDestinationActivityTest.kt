package com.tokopedia.travelhomepage.destination.presentation.activity

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.travelhomepage.R
import com.tokopedia.travelhomepage.destination.presentation.adapter.viewholder.TravelDestinationSummaryViewHolder
import com.tokopedia.travelhomepage.homepage.presentation.activity.TravelHomepageActivityTest
import kotlinx.android.synthetic.main.layout_travel_destination_article.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.junit.*


/**
 * @author by jessica on 26/11/20
 */
class TravelDestinationActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)
    private val graphqlCacheManager = GraphqlCacheManager()

    @get:Rule
    var activityRule: IntentsTestRule<TravelDestinationActivity> = object : IntentsTestRule<TravelDestinationActivity>(TravelDestinationActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            graphqlCacheManager.deleteAll()
            gtmLogDBSource.deleteAll().toBlocking().first()
            setupGraphqlMockResponse {
                addMockResponse(KEY_TRAVEL_DESTINATION_CITY_DATA, InstrumentationMockHelper.getRawString(context,
                        com.tokopedia.travelhomepage.test.R.raw.response_mock_travel_destination_city_data), MockModelConfig.FIND_BY_CONTAINS)

                addMockResponse(KEY_TRAVEL_DESTINATION_RECOMMENDATION, InstrumentationMockHelper.getRawString(context,
                        com.tokopedia.travelhomepage.test.R.raw.response_mock_travel_destination_recommendation), MockModelConfig.FIND_BY_CONTAINS)

                addMockResponse(KEY_TRAVEL_DESTINATION_SUMMARY, InstrumentationMockHelper.getRawString(context,
                        com.tokopedia.travelhomepage.test.R.raw.response_mock_travel_destination_summary), MockModelConfig.FIND_BY_CONTAINS)

                addMockResponse(KEY_TRAVEL_DESTINATION_ORDER_LIST, InstrumentationMockHelper.getRawString(context,
                        com.tokopedia.travelhomepage.test.R.raw.response_mock_travel_destination_order_list), MockModelConfig.FIND_BY_CONTAINS)

                addMockResponse(KEY_TRAVEL_DESTINATION_ARTICLE, InstrumentationMockHelper.getRawString(context,
                        com.tokopedia.travelhomepage.test.R.raw.response_mock_travel_destination_city_article), MockModelConfig.FIND_BY_CONTAINS)
            }
        }

        override fun getActivityIntent(): Intent {
            return TravelDestinationActivity.createInstance(context, DUMMY_WEB_URL)
        }
    }

    @Before
    fun setUp() {
    }

    @Test
    fun mainFlow() {
        disableAnimation()
        collapseToolbar()

        actionsOnBookingViewHolder()
        actionsOnCrossSellViewHolder()
        actionsOnEventViewHolder()
        actionsOnDealsViewHolder()
        actionsOnBlogViewHolder()

        Assert.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_TRAVEL_DESTINATION),
                hasAllSuccess())
    }

    private fun collapseToolbar() {
        Thread.sleep(2000)
        val appBarLayout = activityRule.activity.findViewById<AppBarLayout>(R.id.app_bar_layout)
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                appBarLayout.setExpanded(false)
            }
        }
    }

    private fun disableAnimation() {
        Thread.sleep(2000)
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(SUMMARY_POSITION)
        (viewHolder as TravelDestinationSummaryViewHolder).disableAnimation()
    }

    private fun actionsOnBookingViewHolder() {
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.recycler_view))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(BOOKING_POSITION))

        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(BOOKING_POSITION)
        viewHolder?.let {
            CommonActions.clickOnEachItemRecyclerView(it.itemView, R.id.list_recycler_view, 0)
        }
    }

    private fun actionsOnCrossSellViewHolder() {
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.recycler_view))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(CROSS_SELL_POSITION))

        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(CROSS_SELL_POSITION)
        viewHolder?.let {
            CommonActions.clickOnEachItemRecyclerView(it.itemView, R.id.rv_destination_recommendation, 0)
        }
    }

    private fun actionsOnDealsViewHolder() {
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.recycler_view))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(DEALS_VIEW_HOLDER_POSITION))

        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(DEALS_VIEW_HOLDER_POSITION)
        viewHolder?.let {
            CommonActions.clickOnEachItemRecyclerView(it.itemView, R.id.list_recycler_view, 0)
        }

        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(DEALS_VIEW_HOLDER_POSITION,
                        CommonActions.clickChildViewWithId(R.id.section_see_all))
        )
    }

    private fun actionsOnEventViewHolder() {
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.recycler_view))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(EVENT_VIEW_HOLDER_POSITION))

        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(EVENT_VIEW_HOLDER_POSITION)
        viewHolder?.let {
            CommonActions.clickOnEachItemRecyclerView(it.itemView, R.id.list_recycler_view, 0)
        }

        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(EVENT_VIEW_HOLDER_POSITION,
                        CommonActions.clickChildViewWithId(R.id.section_see_all))
        )
    }

    private fun actionsOnBlogViewHolder() {
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.recycler_view))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(ARTICLE_VIEW_HOLDER_POSITION))

        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(ARTICLE_VIEW_HOLDER_POSITION)
        viewHolder?.let {
            CommonActions.clickOnEachItemRecyclerView(it.itemView, R.id.rv_travel_destination_article_item, 0)
        }

        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(ARTICLE_VIEW_HOLDER_POSITION,
                        CommonActions.clickChildViewWithId(R.id.tv_travel_destination_article_see_all))
        )
    }

    @After
    fun tearDown() {
    }

    companion object {
        const val KEY_TRAVEL_DESTINATION_RECOMMENDATION = "TravelCollectiveRecommendation"
        const val KEY_TRAVEL_DESTINATION_SUMMARY = "TravelDestinationSummary"
        const val KEY_TRAVEL_DESTINATION_CITY_DATA = "TravelDestinationCityData"
        const val KEY_TRAVEL_DESTINATION_ORDER_LIST = "TravelCollectiveOrderList"
        const val KEY_TRAVEL_DESTINATION_ARTICLE = "TravelArticle"

        const val DUMMY_WEB_URL = "https://m.tokopedia.com/travel-entertainment/bandung/"

        const val ANALYTIC_VALIDATOR_QUERY_TRAVEL_DESTINATION = "tracker/travel/homepage/travel_destination.json"

        const val SUMMARY_POSITION = 0
        const val BOOKING_POSITION = 1
        const val CROSS_SELL_POSITION = 2
        const val EVENT_VIEW_HOLDER_POSITION =  3
        const val DEALS_VIEW_HOLDER_POSITION = 4
        const val ARTICLE_VIEW_HOLDER_POSITION = 5
    }
}