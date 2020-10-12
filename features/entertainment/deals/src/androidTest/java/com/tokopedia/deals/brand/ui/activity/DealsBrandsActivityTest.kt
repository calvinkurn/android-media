package com.tokopedia.deals.brand.ui.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.deals.R
import com.tokopedia.deals.brand.ui.activity.mock.DealsBrandsMockResponse
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DealsBrandsActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDbSource = GtmLogDBSource(context)

    @get: Rule
    var activtyRule: IntentsTestRule<DealsBrandActivity> = object : IntentsTestRule<DealsBrandActivity>(DealsBrandActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            gtmLogDbSource.deleteAll().subscribe()
            setupGraphqlMockResponse(DealsBrandsMockResponse())
        }

        override fun getActivityIntent(): Intent {
            return DealsBrandActivity.getCallingIntent(context, "tes")
        }
    }

    @Before
    fun setUp() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @Test
    fun testSearchLayout() {
        impressionDealsEmptyViewHolder()
        actionOnDealsBrandViewHolder()
        eventClickCategoryTabBrandPage()

        Assert.assertThat(getAnalyticsWithQuery(gtmLogDbSource, context, ANALYTIC_VALIDATOR_QUERY_DEALS_BRANDPAGE),
                hasAllSuccess())
    }

    private fun impressionDealsEmptyViewHolder() {
        Thread.sleep(3000)
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed())).perform(RecyclerViewActions
                .scrollToPosition<RecyclerView.ViewHolder>(EMPTY_POSITION)).perform(click())
    }

    private fun actionOnDealsBrandViewHolder() {
        Thread.sleep(3000)
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(BRAND_POSITION))

        Thread.sleep(3000)
        val recyclerView = activtyRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(BRAND_POSITION)
        viewHolder?.let {
            CommonActions.clickOnEachItemRecyclerView(it.itemView, R.id.recycler_view, 0)
        }
        Thread.sleep(2000)
    }

    private fun eventClickCategoryTabBrandPage() {
        Thread.sleep(3000)
        activtyRule.activity.tabAnalytics("Kecantikan", 3)
    }


    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_DEALS_BRANDPAGE = "tracker/entertainment/deals/deals_brand_tracking.json"
        private const val EMPTY_POSITION = 0
        private const val BRAND_POSITION = 0
    }
}