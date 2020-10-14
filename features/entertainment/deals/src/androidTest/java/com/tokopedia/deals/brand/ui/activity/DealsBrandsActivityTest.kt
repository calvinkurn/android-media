package com.tokopedia.deals.brand.ui.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.deals.R
import com.tokopedia.deals.brand.ui.activity.mock.DealsBrandsMockResponse
import com.tokopedia.test.application.espresso_component.CommonMatcher.withTagStringValue
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.core.AllOf
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DealsBrandsActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDbSource = GtmLogDBSource(context)

    private val TAB_NAME = "Fashion"
    private val QUERY_BY_USER = "sadjgh"

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
        clickOnSearchBar()
        actionOnDealsBrandViewHolder()
        impressionDealsEmptyViewHolder()
        eventClickSearchBrandPage()
        clickOnRelaksasiTab()
        eventClickCategoryTabBrandPage()

        Assert.assertThat(getAnalyticsWithQuery(gtmLogDbSource, context, ANALYTIC_VALIDATOR_QUERY_DEALS_BRANDPAGE),
                hasAllSuccess())
    }

    private fun impressionDealsEmptyViewHolder() {
        Thread.sleep(3000)
        onView(withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield)).perform(typeText(QUERY_BY_USER))

        Thread.sleep(3000)
        onData(withId(R.id.deals_brand_recycler_view)).check(matches(isDisplayed())).perform(RecyclerViewActions
                .scrollToPosition<RecyclerView.ViewHolder>(EMPTY_POSITION)).perform(click())
    }

    private fun clickOnSearchBar() {
        Thread.sleep(2000)
        onView(withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield)).perform(click())
        Thread.sleep(2000)
        onView(withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield)).perform(click())
    }

    private fun actionOnDealsBrandViewHolder() {
        Thread.sleep(1000)
        val recyclerView = onView(AllOf.allOf(withId(R.id.deals_brand_recycler_view), withTagStringValue(TAB_NAME)))
        recyclerView.perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
    }

    private fun eventClickSearchBrandPage() {
        Thread.sleep(2000)
        onView(withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield)).perform(click())
        Thread.sleep(2000)
        onView(withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield)).perform(click())
    }

    private fun clickOnRelaksasiTab() {
        Thread.sleep(5000)
        onView(AllOf.allOf(withId(R.id.tab_item_text_id), ViewMatchers.withText(TAB_NAME))).perform(click())
    }

    private fun eventClickCategoryTabBrandPage() {
        Thread.sleep(3000)
        onView(AllOf.allOf(withId(R.id.deals_brand_recycler_view), withTagStringValue(TAB_NAME))).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(2))
    }


    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_DEALS_BRANDPAGE = "tracker/entertainment/deals/deals_brand_tracking.json"
        private const val EMPTY_POSITION = 0
        private const val BRAND_POSITION = 0
    }
}