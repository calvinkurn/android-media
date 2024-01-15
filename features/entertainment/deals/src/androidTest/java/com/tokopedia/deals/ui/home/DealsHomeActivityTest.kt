package com.tokopedia.deals.ui.home

import android.Manifest
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
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.deals.DealsDummyResponseString.DUMMY_LOCATION_ONE_STRING
import com.tokopedia.deals.DealsDummyResponseString.DUMMY_LOCATION_TWO_STRING
import com.tokopedia.deals.R
import com.tokopedia.deals.ui.home.ui.activity.DealsHomeActivity
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.espresso_component.CommonMatcher.firstView
import com.tokopedia.test.application.espresso_component.CommonMatcher.getElementFromMatchAtPosition
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Rule
import org.junit.Test

/**
 * @author by jessica on 28/09/20
 */
class DealsHomeActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private lateinit var localCacheHandler: LocalCacheHandler

    @get:Rule
    var permissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    @get:Rule
    var activityRule: IntentsTestRule<DealsHomeActivity> = object : IntentsTestRule<DealsHomeActivity>(DealsHomeActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
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

    @get:Rule
    var cassavaRule = CassavaTestRule()

    @Test
    fun testHomeLayout() {
        changeLocation()

        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        impressionOnLayout()
        clickOnSearchBar()
        clickOnOrderList()
        clickOnOrderList()
        clickOnHomepageBanner()
        actionOnCategoryViewHolder()
        actionOnBrandsViewHolder()
        actionOnProductViewHolder()
        actionOnPopularLandmarkViewHolder()
        actionOnFavouriteCategoryViewHolder()

        assertThat(cassavaRule.validate(ANALYTIC_VALIDATOR_QUERY_DEALS_HOMEPAGE), hasAllSuccess())
    }

    private fun changeLocation() {
        onView(withId(R.id.txtDealsBaseLocationTitle)).perform(click())
        Thread.sleep(2000)
        onView(firstView(withText(DUMMY_LOCATION_ONE_STRING))).perform(click())
        Thread.sleep(2000)

        onView(withId(R.id.txtDealsBaseLocationTitle)).perform(click())
        Thread.sleep(2000)
        onView(firstView(withText(DUMMY_LOCATION_TWO_STRING))).perform(click())
        Thread.sleep(2000)
    }

    private fun clickOnSearchBar() {
        Thread.sleep(2000)
        onView(withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield)).perform(click())
        Thread.sleep(2000)
        onView(withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield)).perform(click())
    }

    private fun actionOnPopularLandmarkViewHolder() {
        Thread.sleep(2000)
        onView(withId(R.id.recycler_view))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                POPULAR_LANDMARKS_POSITION
            ))

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
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                FAVOURITE_CATEGORY_POSITION
            ))

        Thread.sleep(2000)
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(FAVOURITE_CATEGORY_POSITION)
        viewHolder?.let {
            onView(getElementFromMatchAtPosition(withId(R.id.lst_voucher_popular_place_card), 1))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))
        }
        Thread.sleep(2000)
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
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                PRODUCT_POSITION,
                CommonActions.clickChildViewWithId(R.id.btn_curated_product_category_see_all)
            )
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
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                BRAND_POSITION,
                CommonActions.clickChildViewWithId(R.id.txtDealsPopularBrandSeeAll)
            )
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
            CommonActions.clickOnEachItemRecyclerView(it.itemView, com.tokopedia.banner.R.id.banner_recyclerview, 0)
        }
        Thread.sleep(2000)
        onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                BANNER_POSITION,
                CommonActions.clickChildViewWithId(com.tokopedia.banner.R.id.banner_see_all)
            )
        )
    }

    private fun impressionOnLayout() {
        Thread.sleep(3000)
        try {
            onView(withId(com.tokopedia.design.R.id.btn_ok_dialog)).perform(click())
        } catch (e: Exception) {
        }
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
