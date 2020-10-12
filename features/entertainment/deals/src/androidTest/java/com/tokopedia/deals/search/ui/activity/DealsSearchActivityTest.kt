package com.tokopedia.deals.search.ui.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.deals.R
import com.tokopedia.deals.search.ui.activity.mock.DealsSearchMockResponse
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author by abrar on 30/09/20
 */
class DealsSearchActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDbSource = GtmLogDBSource(context)

    @get: Rule
    var activtyRule: IntentsTestRule<DealsSearchActivity> = object : IntentsTestRule<DealsSearchActivity>(DealsSearchActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            gtmLogDbSource.deleteAll().subscribe()
            setupGraphqlMockResponse(DealsSearchMockResponse())
        }

        override fun getActivityIntent(): Intent {
            return DealsSearchActivity.getCallingIntent(context)
        }
    }

    @Before
    fun setUp() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @Test
    fun testSearchLayout() {
        impressionOnSearchResult()
        impressionOnSearchResulNotFound()
        onClickSearchResultBrand()
        onClickOnSearchResultProduct()
        clickChipsLastSeen()

        Assert.assertThat(getAnalyticsWithQuery(gtmLogDbSource, context, ANALYTIC_VALIDATOR_QUERY_DEALS_SEARHPAGE),
                hasAllSuccess())
    }

    private fun impressionOnSearchResult() {
        Thread.sleep(2000)
        onView(withId(R.id.search_bar)).check(matches(isDisplayed())).perform(click()).perform(typeText("tes"))

        Thread.sleep(3000)
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed())).perform(RecyclerViewActions
                .scrollToPosition<RecyclerView.ViewHolder>(BRAND_POSITION))

        Thread.sleep(2000)
        val recyclerView = activtyRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(PRODUCT_POSITION)
        viewHolder?.let {
            CommonActions.clickOnEachItemRecyclerView(it.itemView, R.id.rv_search_results, 0)
        }
    }

    private fun onClickSearchResultBrand() {
        Thread.sleep(2000)
        onView(withId(R.id.search_bar)).check(matches(isDisplayed())).perform(click()).perform(typeText("tes"))

        Thread.sleep(2000)
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed())).perform(RecyclerViewActions
                .scrollToPosition<RecyclerView.ViewHolder>(BRAND_POSITION))

        Thread.sleep(2000)
        val recyclerView = activtyRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(BRAND_POSITION)
        viewHolder?.let {
            CommonActions.clickOnEachItemRecyclerView(it.itemView, R.id.rv_brands, 0)
        }
        Thread.sleep(2000)
    }

    private fun onClickOnSearchResultProduct() {
        Thread.sleep(2000)
        onView(withId(R.id.search_bar)).check(matches(isDisplayed())).perform(click()).perform(typeText("tes"))

        Thread.sleep(2000)
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed())).perform(RecyclerViewActions
                .scrollToPosition<RecyclerView.ViewHolder>(PRODUCT_POSITION))

        Thread.sleep(2000)
        val recyclerView = activtyRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(PRODUCT_POSITION)
        viewHolder?.let {
            CommonActions.clickOnEachItemRecyclerView(it.itemView, R.id.rv_search_results, 0)
        }
    }


    private fun impressionOnSearchResulNotFound() {
        Thread.sleep(2000)
        onView(withId(R.id.search_bar)).check(matches(isDisplayed())).perform(typeText("tesasjsagsja"))

        Thread.sleep(2000)
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed())).perform(RecyclerViewActions
                .scrollToPosition<RecyclerView.ViewHolder>(EMPTY_VIEW_POSITION))

        Thread.sleep(2000)
        onView(withId(R.id.empty_state_content_container_id)).check(matches(isDisplayed())).perform(click())
    }

    private fun impressionOnChips() {
        Thread.sleep(3000)
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed())).perform(RecyclerViewActions
                .scrollToPosition<RecyclerView.ViewHolder>(CHIPS_POSITION))

        Thread.sleep(2000)
        val recyclerView = activtyRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(CHIPS_POSITION)
        viewHolder?.let {
            CommonActions.clickOnEachItemRecyclerView(it.itemView, R.id.rv_search_results, 0)
        }
    }

    private fun clickChipsLastSeen() {
        Thread.sleep(3000)
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed())).perform(RecyclerViewActions
                .scrollToPosition<RecyclerView.ViewHolder>(CHIPS_POSITION))

        Thread.sleep(2000)
        val recyclerView = activtyRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(CHIPS_POSITION)
        viewHolder?.let {
            CommonActions.clickChildViewWithId(R.id.chip_green_item)
        }
    }

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_DEALS_SEARHPAGE = "tracker/entertainment/deals/deals_search_tracking.json"
        const val BRAND_POSITION = 0
        const val PRODUCT_POSITION = 1
        const val EMPTY_VIEW_POSITION = 0
        const val CHIPS_POSITION = 0
    }
}