package com.tokopedia.travelhomepage.homepage.presentation.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig.Companion.FIND_BY_CONTAINS
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.travelhomepage.R
import org.junit.After
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * @author by jessica on 26/11/20
 */
class TravelHomepageActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)
    private val graphqlCacheManager = GraphqlCacheManager()

    @get:Rule
    var activityRule: IntentsTestRule<TravelHomepageActivity> = object : IntentsTestRule<TravelHomepageActivity>(TravelHomepageActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            graphqlCacheManager.deleteAll()
            gtmLogDBSource.deleteAll().toBlocking().first()
            setupGraphqlMockResponse {
                addMockResponse(KEY_TRAVEL_SUBHOME_GET_LAYOUT, getRawString(context,
                        com.tokopedia.travelhomepage.test.R.raw.response_mock_travel_homepage_layout), FIND_BY_CONTAINS)
                addMockResponse(KEY_TRAVEL_SUBHOME_GET_ITEM, getRawString(context,
                        com.tokopedia.travelhomepage.test.R.raw.response_mock_travel_homepage_item), FIND_BY_CONTAINS)
            }
        }

        override fun getActivityIntent(): Intent {
            return TravelHomepageActivity.getCallingIntent(context)
        }
    }

    @Test
    fun mainFlow() {

        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        actionsOnBannerViewHolder()
        actionsOnCategoryViewHolder()
        actionsOnSliderProductCardViewHolder()
        actionsOnLegoBannerViewHolder()
        actionsOnSquareProductCardViewHolder()
        actionsOnDynamicBannerViewHolder()

        Thread.sleep(3000)
        Assert.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_TRAVEL_HOMEPAGE),
                hasAllSuccess())
    }

    private fun actionsOnBannerViewHolder() {

        Thread.sleep(3000)
        Espresso.onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(BANNER_POSITION))

        Thread.sleep(2000)
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(BANNER_POSITION)
        viewHolder?.let {
            CommonActions.clickOnEachItemRecyclerView(it.itemView, R.id.banner_recyclerview, 0)
        }
        Thread.sleep(2000)
        Espresso.onView(withId(R.id.recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(BANNER_POSITION,
                        CommonActions.clickChildViewWithId(R.id.banner_see_all))
        )
    }

    private fun actionsOnCategoryViewHolder() {
        Thread.sleep(3000)
        Espresso.onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(CATEGORY_POSITION))

        Thread.sleep(2000)
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(CATEGORY_POSITION)
        viewHolder?.let {
            CommonActions.clickOnEachItemRecyclerView(it.itemView, R.id.category_recycler_view, 0)
        }
    }

    private fun actionsOnSliderProductCardViewHolder() {
        Thread.sleep(3000)
        Espresso.onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(SLIDER_PRODUCT_CARD_POSITION))

        Thread.sleep(2000)
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(SLIDER_PRODUCT_CARD_POSITION)
        viewHolder?.let {
            CommonActions.clickOnEachItemRecyclerView(it.itemView, R.id.list_recycler_view, 0)
        }
        Thread.sleep(2000)
        Espresso.onView(withId(R.id.recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(SLIDER_PRODUCT_CARD_POSITION,
                        CommonActions.clickChildViewWithId(R.id.section_see_all))
        )
    }

    private fun actionsOnLegoBannerViewHolder() {
        Thread.sleep(3000)
        Espresso.onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(LEGO_BANNER_POSITION))

        Thread.sleep(2000)
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(LEGO_BANNER_POSITION)
        viewHolder?.let {
            CommonActions.clickOnEachItemRecyclerView(it.itemView, R.id.travel_homepage_lego_banner_rv, 0)
        }
    }

    private fun actionsOnSquareProductCardViewHolder() {
        Thread.sleep(3000)
        Espresso.onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(SQUARE_PRODUCT_CARD_POSITION))

        Thread.sleep(2000)
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(SQUARE_PRODUCT_CARD_POSITION)
        viewHolder?.let {
            CommonActions.clickOnEachItemRecyclerView(it.itemView, R.id.travel_homepage_product_rv, 0)
        }
        Thread.sleep(2000)
        Espresso.onView(withId(R.id.recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(SQUARE_PRODUCT_CARD_POSITION,
                        CommonActions.clickChildViewWithId(R.id.travel_homepage_product_widget_see_all))
        )
    }

    private fun actionsOnDynamicBannerViewHolder() {
        Thread.sleep(3000)
        Espresso.onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(DYNAMIC_BANNER_POSITION))

        Thread.sleep(2000)
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(DYNAMIC_BANNER_POSITION)
        viewHolder?.let {
            CommonActions.clickOnEachItemRecyclerView(it.itemView, R.id.list_recycler_view, 0)
        }
    }

    @After
    fun tearDown() {
        gtmLogDBSource.deleteAll().subscribe()
    }

    companion object {
        const val ANALYTIC_VALIDATOR_QUERY_TRAVEL_HOMEPAGE = "tracker/travel/homepage/travel_homepage.json"

        const val KEY_TRAVEL_SUBHOME_GET_LAYOUT = "travelLayoutSubhomepage"
        const val KEY_TRAVEL_SUBHOME_GET_ITEM = "TravelGetDynamicSubhomepage"

        const val BANNER_POSITION = 0
        const val CATEGORY_POSITION = 1
        const val SLIDER_PRODUCT_CARD_POSITION = 2
        const val LEGO_BANNER_POSITION = 3
        const val SQUARE_PRODUCT_CARD_POSITION = 4
        const val DYNAMIC_BANNER_POSITION = 5
    }
}