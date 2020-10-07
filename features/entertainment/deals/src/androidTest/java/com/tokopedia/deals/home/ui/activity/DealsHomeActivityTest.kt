package com.tokopedia.deals.home.ui.activity

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
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.deals.R
import com.tokopedia.deals.home.ui.activity.mock.DealsHomeMockResponse
import com.tokopedia.deals.location_picker.model.response.Location
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author by jessica on 28/09/20
 */
class DealsHomeActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)
    private lateinit var localCacheHandler: LocalCacheHandler

    @get:Rule
    var activityRule: IntentsTestRule<DealsHomeActivity> = object : IntentsTestRule<DealsHomeActivity>(DealsHomeActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            gtmLogDBSource.deleteAll().subscribe()
            localCacheHandler = LocalCacheHandler(context, PREFERENCES_NAME)
            localCacheHandler.apply {
                putBoolean(SHOW_COACH_MARK_KEY, false)
                applyEditor()
            }
            setupGraphqlMockResponse(DealsHomeMockResponse())
        }

        override fun getActivityIntent(): Intent {
            return DealsHomeActivity.getCallingIntent(context)
        }
    }

    @Before
    fun setUp() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @Test
    fun testHomeLayout() {
        impressionOnLayout()
        clickOnOrderList()
        clickOnHomepageBanner()
        actionOnCategoryViewHolder()
        actionOnBrandsViewHolder()
        actionOnProductViewHolder()
        actionOnPopularLandmarkViewHolder()
        actionOnFavouriteCategoryViewHolder()
        changeLocation()

        Assert.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_DEALS_HOMEPAGE),
                hasAllSuccess())
    }

    private fun changeLocation() {
        Thread.sleep(2000)
        activityRule.activity.setCurrentLocation(Location())

        Thread.sleep(2000)
    }

    private fun actionOnPopularLandmarkViewHolder() {
        Thread.sleep(2000)
        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(POPULAR_LANDMARKS_POSITION))

        Thread.sleep(2000)
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(POPULAR_LANDMARKS_POSITION)
        viewHolder?.let {
            CommonActions.clickOnEachItemRecyclerView(it.itemView, R.id.lst_voucher_popular_place_card, 1)
        }
        Thread.sleep(2000)
    }

    private fun actionOnFavouriteCategoryViewHolder() {
        Thread.sleep(2000)
        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(FAVOURITE_CATEGORY_POSITION))

        Thread.sleep(2000)
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(FAVOURITE_CATEGORY_POSITION)
        viewHolder?.let {
            onView(secondView(withId(R.id.lst_voucher_popular_place_card)))
                    .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))
        }
        Thread.sleep(2000)
    }

    private fun <T> secondView(matcher: Matcher<T>): Matcher<T> {
        return object : BaseMatcher<T>() {
            var counter = 0
            override fun matches(item: Any): Boolean {
                if (matcher.matches(item)) {
                    if (counter == 1) return true
                    else counter++

                    return false
                }
                return false
            }

            override fun describeTo(description: Description) {
                description.appendText("should return second matching item")
            }
        }
    }

    private fun actionOnProductViewHolder() {
        Thread.sleep(2000)
        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(PRODUCT_POSITION))

        Thread.sleep(2000)
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(PRODUCT_POSITION)
        viewHolder?.let {
            CommonActions.clickOnEachItemRecyclerView(it.itemView, R.id.lst_curated_product_category_card, 1)
        }
        Thread.sleep(2000)
        onView(withId(R.id.recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(PRODUCT_POSITION,
                        CommonActions.clickChildViewWithId(R.id.btn_curated_product_category_see_all))
        )
    }

    private fun actionOnBrandsViewHolder() {
        Thread.sleep(2000)
        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(BRAND_POSITION))

        Thread.sleep(2000)
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(BRAND_POSITION)
        viewHolder?.let {
            CommonActions.clickOnEachItemRecyclerView(it.itemView, R.id.rvDealsBrandPopular, 1)
        }
        Thread.sleep(2000)
        onView(withId(R.id.recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(BRAND_POSITION,
                        CommonActions.clickChildViewWithId(R.id.txtDealsPopularBrandSeeAll))
        )
    }

    private fun actionOnCategoryViewHolder() {
        Thread.sleep(2000)
        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(CATEGORY_POSITION))

        Thread.sleep(2000)
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(CATEGORY_POSITION)
        viewHolder?.let {
            CommonActions.clickOnEachItemRecyclerView(it.itemView, R.id.rvDealsHomeCategories, 1)
        }
        Thread.sleep(2000)
    }

    private fun clickOnHomepageBanner() {
        Thread.sleep(2000)
        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(BANNER_POSITION))

        Thread.sleep(2000)
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(BANNER_POSITION)
        viewHolder?.let {
            CommonActions.clickOnEachItemRecyclerView(it.itemView, R.id.banner_recyclerview, 0)
        }
        Thread.sleep(2000)
        onView(withId(R.id.recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(BANNER_POSITION,
                        CommonActions.clickChildViewWithId(R.id.banner_see_all))
        )
    }

    private fun impressionOnLayout() {
        Thread.sleep(3000)
        onView(withId(R.id.btn_ok_dialog)).perform(click())
        Thread.sleep(3000)
    }

    private fun clickOnOrderList() {
        Thread.sleep(2000)
        onView(withId(R.id.imgDealsOrderListMenu)).check(matches(isDisplayed())).perform(click())
    }

    companion object {
        private const val PREFERENCES_NAME = "deals_home_preferences"
        private const val SHOW_COACH_MARK_KEY = "show_coach_mark_key"

        const val BANNER_POSITION = 0
        const val CATEGORY_POSITION = 1
        const val BRAND_POSITION = 2
        const val PRODUCT_POSITION = 3

        const val POPULAR_LANDMARKS_POSITION = 5
        const val FAVOURITE_CATEGORY_POSITION = 6

        private const val ANALYTIC_VALIDATOR_QUERY_DEALS_HOMEPAGE = "tracker/entertainment/deals/deals_homepage.json"
    }
}
