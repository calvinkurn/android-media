package com.tokopedia.officialstore.topads

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.viewholders.FeaturedShopViewHolder
import com.tokopedia.home_component.viewholders.MixLeftComponentViewHolder
import com.tokopedia.home_component.viewholders.MixTopComponentViewHolder
import com.tokopedia.home_component.visitable.FeaturedShopDataModel
import com.tokopedia.home_component.visitable.MixLeftDataModel
import com.tokopedia.home_component.visitable.MixTopDataModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.environment.InstrumentationOfficialStoreTestFullActivity
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapter
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.ProductRecommendationDataModel
import com.tokopedia.officialstore.official.presentation.adapter.viewholder.OfficialProductRecommendationViewHolder
import com.tokopedia.officialstore.official.presentation.dynamic_channel.DynamicChannelMixTopViewHolder
import com.tokopedia.officialstore.util.OSRecyclerViewIdlingResource
import com.tokopedia.officialstore.util.preloadRecomOnOSPage
import com.tokopedia.officialstore.util.removeProgressBarOnOsPage
import com.tokopedia.recommendation_widget_common.widget.bestseller.BestSellerViewHolder
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.environment.callback.TopAdsVerificatorInterface
import com.tokopedia.test.application.espresso_component.CommonActions.clickOnEachItemRecyclerView
import com.tokopedia.test.application.espresso_component.CommonMatcher.firstView
import com.tokopedia.test.application.matcher.RecyclerViewMatcher
import com.tokopedia.test.application.util.InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/**
 * Created by DevAra
 * Instrumented test, which will execute on an Android device.
 * Verify Topads for OSPage
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class OSTopAdsVerificationTest {

    @get:Rule
    var grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @get:Rule
    var activityRule = object: IntentsTestRule<InstrumentationOfficialStoreTestFullActivity>(
        InstrumentationOfficialStoreTestFullActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            loginInstrumentationTestTopAdsUser()
        }
    }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private var topAdsCount = 0
    private var countFeaturedShop = 0
    private val topAdsAssertion = TopAdsAssertion(context, TopAdsVerificatorInterface { topAdsCount })
    private var osRecyclerViewIdlingResource: OSRecyclerViewIdlingResource? = null

    @Before
    fun setUp() {
        Intents.intending(IntentMatchers.isInternal()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        osRecyclerViewIdlingResource = OSRecyclerViewIdlingResource(
            activity = activityRule.activity,
            limitCountToIdle = 3
        )
        IdlingRegistry.getInstance().register(osRecyclerViewIdlingResource)
    }

    @After
    fun deleteDatabase() {
        topAdsAssertion.after()
        IdlingRegistry.getInstance().unregister(osRecyclerViewIdlingResource)
    }

    @Test
    fun testTopAdsHome() {
        Espresso.onView(firstView(withId(R.id.os_child_recycler_view))).check(matches(isDisplayed()))

        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.os_child_recycler_view)
        val itemAdapter: OfficialHomeAdapter = recyclerView.adapter as OfficialHomeAdapter

        /**
         * This function needed to remove any loading view, because any infinite loop rendered view such as loading view,
         * shimmering, progress bar, etc can block instrumentation test
         */
        removeProgressBarOnOsPage(recyclerView, activityRule.activity)

        /**
         * This function needed to trigger product recommendation usecase in official store,
         * official store page only hit recommendation usecase on scroll in the end of current list
         */
        preloadRecomOnOSPage(recyclerView)
        Espresso.onView(firstView(withId(R.id.os_child_recycler_view))).perform(ViewActions.swipeUp())

        waitForData()

        val itemList = itemAdapter.currentList
        topAdsCount = calculateTopAdsCount(itemList)

        val itemCount = itemList.size

        for (i in 0 until itemCount) {
            val checkLoadingView: View? = activityRule.activity.findViewById<View>(R.id.loading_view)
            checkLoadingView?.let { checkLoadingView.gone() }
            scrollHomeRecyclerViewToPosition(recyclerView, i)
            checkProductOnDynamicChannel(recyclerView, i)
        }
        topAdsAssertion.assert()
    }

    private fun calculateTopAdsCount(itemList: List<Visitable<*>>) : Int {
        var count = 0
        for (item in itemList) {
            count += countTopAdsInItem(item)
        }
        return count
    }

    private fun countTopAdsInItem(item: Visitable<*>) : Int {
        var count = 0

        when (item) {
            is MixTopDataModel -> {
                for (grid in item.channelModel.channelGrids)
                    if (grid.isTopads) count++
            }
            is MixLeftDataModel -> {
                for (grid in item.channelModel.channelGrids)
                    if (grid.isTopads) count++
            }
            is ProductRecommendationDataModel -> {
                if (item.productItem.isTopAds) count++
            }
            is BestSellerDataModel -> {
                for (recom in item.recommendationItemList)
                    if (recom.isTopAds) count++
            }
            is FeaturedShopDataModel -> {
                for (grid in item.channelModel.channelGrids)
                    if (grid.isTopads) count++
            }
        }
        return count
    }

    private fun checkProductOnDynamicChannel(homeRecyclerView: RecyclerView, i: Int) {
        when (val viewHolder = homeRecyclerView.findViewHolderForAdapterPosition(i)) {
            is MixTopComponentViewHolder -> {
                clickOnEachItemRecyclerView(viewHolder.itemView, R.id.dc_banner_rv, 0)
            }
            is MixLeftComponentViewHolder -> {
                clickOnEachItemRecyclerView(viewHolder.itemView, R.id.rv_product, 0)
            }
            is DynamicChannelMixTopViewHolder -> {
                clickOnEachItemRecyclerView(viewHolder.itemView, R.id.dc_banner_rv, 0)
            }
            is OfficialProductRecommendationViewHolder -> {
                Espresso.onView(firstView(withId(R.id.os_child_recycler_view)))
                        .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(i, ViewActions.click()))
            }
            is BestSellerViewHolder -> {
                val recyclerView: View? = activityRule.activity.findViewById<View>(R.id.best_seller_recommendation_recycler_view)
                recyclerView?.let {
                    clickOnEachItemRecyclerView(viewHolder.itemView, R.id.best_seller_recommendation_recycler_view, 0)
                }
            }
            is FeaturedShopViewHolder -> {
                val checkLoadingView: View? =
                    activityRule.activity.findViewById<View>(R.id.loading_view)
                checkLoadingView?.let { checkLoadingView.gone() }
                val backgroundView: View? =
                    activityRule.activity.findViewById<View>(R.id.featured_shop_background)
                backgroundView?.let { backgroundView.gone() }
                try {
                    val recyclerView: View? =
                        activityRule.activity.findViewById<View>(R.id.dc_banner_rv)
//                    Espresso.onView(withRecyclerView(R.id.dc_banner_rv)?.atPosition(atPosition3)).perform(click())
//                        .perform(RecyclerViewActions.actionOnItemAtPosition(9, click()))
//                    Espresso.onView(withRecyclerView(R.id.dc_banner_rv)?.atPosition(0)).perform(click())
//                    Espresso.onView(withId(R.id.dc_banner_rv))
//                        .perform(scrollToPosition<RecyclerView.ViewHolder>(0))

                    tapRecyclerViewItem(R.id.dc_banner_rv,0)
                    waitForData()
                    tapRecyclerViewItem(R.id.dc_banner_rv,1)
                    waitForData()
                    tapRecyclerViewItem(R.id.dc_banner_rv,2)
                    waitForData()
                    tapRecyclerViewItem(R.id.dc_banner_rv,3)
                    waitForData()
                    tapRecyclerViewItem(R.id.dc_banner_rv,4)
                    waitForData()
                    tapRecyclerViewItem(R.id.dc_banner_rv,5)
                    waitForData()
                    tapRecyclerViewItem(R.id.dc_banner_rv,6)
                    waitForData()
                    tapRecyclerViewItem(R.id.dc_banner_rv,7)
                    waitForData()
                    tapRecyclerViewItem(R.id.dc_banner_rv,8)
                    waitForData()
                    tapRecyclerViewItem(R.id.dc_banner_rv,9)
                    waitForData()

//                    recyclerView?.let {
//                        clickOnEachItemRecyclerView(viewHolder.itemView, R.id.dc_banner_rv, 0)
//                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher? {
        return RecyclerViewMatcher(recyclerViewId)
    }

    fun tapRecyclerViewItem(recyclerViewId: Int, position: Int) {
        onView(withId(recyclerViewId)).perform(scrollToPosition<RecyclerView.ViewHolder>(position))
        onView(withRecyclerView(recyclerViewId)!!.atPosition(position)).perform(click())
    }

    private fun waitForData() {
        Thread.sleep(5000)
    }

    private fun scrollHomeRecyclerViewToPosition(homeRecyclerView: RecyclerView, position: Int) {
        val layoutManager = homeRecyclerView.layoutManager as StaggeredGridLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
    }
}
