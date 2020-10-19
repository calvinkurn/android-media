package com.tokopedia.deals.search.ui.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
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
import com.tokopedia.deals.search.ui.activity.mock.DealsSearchMockResponse
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.espresso_component.CommonMatcher
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

    private val query = "tes"

    @get: Rule
    var activityRule: IntentsTestRule<DealsSearchActivity> = object : IntentsTestRule<DealsSearchActivity>(DealsSearchActivity::class.java) {
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
        clickChipsLastSeen()
        clickSearch()
        clickOnSearchBar()
        actionOnVoucherViewHolder()
        actionOnMerchantViewHolder()
        impressionNotFoundViewHolder()

        Assert.assertThat(getAnalyticsWithQuery(gtmLogDbSource, context, ANALYTIC_VALIDATOR_QUERY_DEALS_SEARHPAGE),
                hasAllSuccess())
    }

    private fun clickSearch() {
        Thread.sleep(2000)
        onView(withId(R.id.search_bar)).perform(click())
    }

    private fun clickOnSearchBar() {
        Thread.sleep(2000)
        onView(withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield)).perform(click())
        Thread.sleep(2000)
        onView(withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield)).perform(click())
    }


    private fun actionOnVoucherViewHolder() {
        Thread.sleep(2000)
        onView(withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield)).perform(click()).perform(typeText(query), ViewActions.closeSoftKeyboard())

        Thread.sleep(2000)
        onView(CommonMatcher.getElementFromMatchAtPosition(withId(R.id.voucher_deals_layout), 1)).perform(click())
    }

    private fun actionOnMerchantViewHolder() {
        Thread.sleep(2000)
        onView(withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield)).perform(click()).perform(typeText(query), ViewActions.closeSoftKeyboard())

        Thread.sleep(2000)
        onView(CommonMatcher.getElementFromMatchAtPosition(withId(R.id.brand_view_holder_layout), 1)).perform(click())
    }

    private fun actionOnCuratedViewHolder() {
        Thread.sleep(2000)
        onView(CommonMatcher.getElementFromMatchAtPosition(withId(R.id.chip_green_item), 0)).perform(click())
    }


    private fun impressionNotFoundViewHolder() {
        Thread.sleep(2000)
        onView(withId(R.id.searchbar_textfield)).check(matches(isDisplayed())).perform(typeText("tesasjsagsja"))

        Thread.sleep(2000)
        onView(withId(R.id.tv_not_found_title))
    }

    private fun clickChipsLastSeen() {
        Thread.sleep(3000)
        onView(withId(R.id.rv_search_results)).check(matches(isDisplayed())).perform(RecyclerViewActions
                .scrollToPosition<RecyclerView.ViewHolder>(CHIPS_POSITION))

        Thread.sleep(2000)
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.rv_search_results)
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(CHIPS_POSITION)
        viewHolder?.let {
            CommonActions.clickChildViewWithId(R.id.chip_green_item)
        }
    }


    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_DEALS_SEARHPAGE = "tracker/entertainment/deals/deals_search_tracking.json"
        const val CHIPS_POSITION = 0
    }
}