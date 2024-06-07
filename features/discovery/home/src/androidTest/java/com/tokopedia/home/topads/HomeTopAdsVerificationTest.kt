package com.tokopedia.home.topads

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.view.View
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.GlobalHomeRecommendationAdapter
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecycleAdapter
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation.HomeRecommendationFeedViewHolder
import com.tokopedia.home.beranda.presentation.view.uimodel.HomeRecommendationFeedDataModel
import com.tokopedia.home.component.disableCoachMark
import com.tokopedia.home.environment.InstrumentationHomeRevampTestActivity
import com.tokopedia.home.util.HomeInstrumentationTestHelper.deleteHomeDatabase
import com.tokopedia.home.util.HomeRecyclerViewIdlingResource
import com.tokopedia.home_component.viewholders.FeaturedShopViewHolder
import com.tokopedia.home_component.viewholders.FlashSaleViewHolder
import com.tokopedia.home_component.viewholders.Lego4ProductViewHolder
import com.tokopedia.home_component.viewholders.MixLeftComponentViewHolder
import com.tokopedia.home_component.viewholders.MixTopComponentViewHolder
import com.tokopedia.home_component.visitable.FeaturedShopDataModel
import com.tokopedia.home_component.visitable.FlashSaleDataModel
import com.tokopedia.home_component.visitable.HasChannelModel
import com.tokopedia.home_component.visitable.Lego4ProductDataModel
import com.tokopedia.home_component.visitable.MixLeftDataModel
import com.tokopedia.home_component.visitable.MixTopDataModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.ForYouRecommendationVisitable
import com.tokopedia.recommendation_widget_common.infinite.foryou.recom.RecommendationCardModel
import com.tokopedia.recommendation_widget_common.widget.bestseller.BestSellerViewHolder
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import com.tokopedia.test.application.annotations.TopAdsTest
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.espresso_component.CommonActions.clickOnEachItemRecyclerViewWithIdle
import com.tokopedia.test.application.util.InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser
import com.tokopedia.test.application.util.setupTopAdsDetector
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.tokopedia.carouselproductcard.R as carouselproductcardR
import com.tokopedia.home_component.R as home_componentR
import com.tokopedia.recommendation_widget_common.R as recommendation_widget_commonR

/**
 * Created by DevAra
 * Instrumented test, which will execute on an Android device.
 * Verify Topads for HomePage
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@TopAdsTest
class HomeTopAdsVerificationTest {

    private var homeRecyclerViewIdlingResource: HomeRecyclerViewIdlingResource? = null
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private var topAdsAssertion = TopAdsAssertion(context) { topAdsCount }
    private var topAdsCount = 0

    @get:Rule
    var grantPermissionRule: GrantPermissionRule = GrantPermissionRule
        .grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @get:Rule
    var activityRule = object : IntentsTestRule<InstrumentationHomeRevampTestActivity>(
        InstrumentationHomeRevampTestActivity::class.java
    ) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            disableCoachMark(context)
            loginInstrumentationTestTopAdsUser()
            setupTopAdsDetector()
        }
    }

    @Before
    fun setUp() {
        Intents.intending(IntentMatchers.isInternal()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        val recyclerView: RecyclerView = activityRule.activity.findViewById(R.id.home_fragment_recycler_view)
        homeRecyclerViewIdlingResource = HomeRecyclerViewIdlingResource(
            recyclerView = recyclerView,
            limitCountToIdle = LIMIT_COUNT_TO_IDLE
        )
        IdlingRegistry.getInstance().register(homeRecyclerViewIdlingResource)
        activityRule.deleteHomeDatabase()
    }

    @Test
    fun testMixTopDataModel() {
        shouldDisplayRecyclerView()

        // Given
        val homeRecyclerView = getCurrentHomeRecyclerView()
        val (indexes, topAdsCount) = calculateTopAdsCountByModel<MixTopDataModel>(
            homeRecyclerView.getItemList()
        )

        // When
        homeRecyclerView.scrollAndInvokeByViewHolder<MixTopComponentViewHolder>(indexes) {
            waitForData()
            clickOnEachItemRecyclerViewWithIdle(it, home_componentR.id.dc_banner_rv, 0)
        }

        // skip assertion if there's no topAds data exist
        if (topAdsCount == 0) return

        // Then
        topAdsAssertion.setTopAdsCount { topAdsCount }
        topAdsAssertion.assert()
    }

    @Test
    fun testMixLeftDataModel() {
        shouldDisplayRecyclerView()

        // Given
        val homeRecyclerView = getCurrentHomeRecyclerView()
        val (indexes, topAdsCount) = calculateTopAdsCountByModel<MixLeftDataModel>(
            homeRecyclerView.getItemList()
        )

        // When
        homeRecyclerView.scrollAndInvokeByViewHolder<MixLeftComponentViewHolder>(indexes) {
            waitForData()
            val childRecyclerView: RecyclerView = it.findViewById(home_componentR.id.rv_product)
            val childItemCount = childRecyclerView.adapter?.itemCount ?: 0

            if (childItemCount >= MIX_LEFT_ITEM_COUNT_THRESHOLD) {
                clickOnEachItemRecyclerViewWithIdle(it, home_componentR.id.rv_product, 0)
            }
        }

        // skip assertion if there's no topAds data exist
        if (topAdsCount == 0) return

        // Then
        topAdsAssertion.setTopAdsCount { topAdsCount }
        topAdsAssertion.assert()
    }

    @Test
    fun testFeaturedShopDataModel() {
        shouldDisplayRecyclerView()

        // Given
        val homeRecyclerView = getCurrentHomeRecyclerView()
        val (indexes, topAdsCount) = calculateTopAdsCountByModel<FeaturedShopDataModel>(
            homeRecyclerView.getItemList()
        )

        // When
        homeRecyclerView.scrollAndInvokeByViewHolder<FeaturedShopViewHolder>(indexes) {
            waitForData()
            clickOnEachItemRecyclerViewWithIdle(it, home_componentR.id.dc_banner_rv, 0)
        }

        // skip assertion if there's no topAds data exist
        if (topAdsCount == 0) return

        // Then
        topAdsAssertion.setTopAdsCount { topAdsCount }
        topAdsAssertion.assert()
    }

    @Test
    fun testBestSellerDataModel() {
        shouldDisplayRecyclerView()

        // Given
        val homeRecyclerView = getCurrentHomeRecyclerView()
        val (indexes, topAdsCount) = calculateTopAdsCountByModel<BestSellerDataModel>(
            homeRecyclerView.getItemList()
        )

        // When
        homeRecyclerView.scrollAndInvokeByViewHolder<BestSellerViewHolder>(indexes) {
            waitForData()
            clickOnEachItemRecyclerViewWithIdle(it, recommendation_widget_commonR.id.best_seller_recommendation_recycler_view, 0)
        }

        // skip assertion if there's no topAds data exist
        if (topAdsCount == 0) return

        // Then
        topAdsAssertion.setTopAdsCount { topAdsCount }
        topAdsAssertion.assert()
    }

    @Test
    fun testHomeRecommendationFeedDataModel() {
        shouldDisplayRecyclerView()

        // Given
        val homeRecyclerView = getCurrentHomeRecyclerView()
        val (indexes, topAdsCount) = calculateTopAdsCountByModel<HomeRecommendationFeedDataModel>(
            homeRecyclerView.getItemList()
        )

        var topAdsTotal = topAdsCount

        // When
        homeRecyclerView.scrollAndInvokeByViewHolder<HomeRecommendationFeedViewHolder>(indexes) {
            waitForData()

            // calculate nested recom widget [view-pager]
            topAdsTotal += calculateTopAdsRecomFeedCount(it)

            // since the recom widget has endless data,
            // hence we have to limit the threshold based on that shown on the screen only.
            val limitRecomDataCount = 4

            clickOnEachItemRecyclerViewWithIdle(it, R.id.home_feed_fragment_recycler_view, limitRecomDataCount)
        }

        // skip assertion if there's no topAds data exist
        if (topAdsTotal == 0) return

        // Then
        topAdsAssertion.setTopAdsCount { topAdsTotal }
        topAdsAssertion.assert()
    }

    @Test
    fun testLego4ProductDataModel() {
        shouldDisplayRecyclerView()

        // Given
        val homeRecyclerView = getCurrentHomeRecyclerView()
        val (indexes, topAdsCount) = calculateTopAdsCountByModel<Lego4ProductDataModel>(
            homeRecyclerView.getItemList()
        )

        // When
        homeRecyclerView.scrollAndInvokeByViewHolder<Lego4ProductViewHolder>(indexes) {
            clickOnEachItemRecyclerViewWithIdle(it, R.id.recycleList, 0)
        }

        // skip assertion if there's no topAds data exist
        if (topAdsCount == 0) return

        // Then
        topAdsAssertion.setTopAdsCount { topAdsCount }
        topAdsAssertion.assert()
    }

    @Test
    fun testFlashSaleDataModel() {
        shouldDisplayRecyclerView()

        // Given
        val homeRecyclerView = getCurrentHomeRecyclerView()
        val (indexes, topAdsCount) = calculateTopAdsCountByModel<FlashSaleDataModel>(
            homeRecyclerView.getItemList()
        )

        // When
        homeRecyclerView.scrollAndInvokeByViewHolder<FlashSaleViewHolder>(indexes) {
            clickOnEachItemRecyclerViewWithIdle(it, carouselproductcardR.id.carouselProductCardRecyclerView, 0)
        }

        // skip assertion if there's no topAds data exist
        if (topAdsCount == 0) return

        // Then
        topAdsAssertion.setTopAdsCount { topAdsCount }
        topAdsAssertion.assert()
    }

    @After
    fun tearDown() {
        topAdsAssertion.after()
        IdlingRegistry.getInstance().unregister(homeRecyclerViewIdlingResource)
    }

    private inline fun <reified T> calculateTopAdsCountByModel(itemList: List<Visitable<*>>): Pair<List<Int>, Int> {
        val indexes = mutableListOf<Int>()
        var topAdsCount = 0

        for ((index, item) in itemList.withIndex()) {
            if (item is T) {
                topAdsCount += countTopAdsInItem(item)
                indexes.add(index)
            }
        }

        return Pair(indexes, topAdsCount)
    }

    private inline fun <reified VH : ViewHolder> RecyclerView.scrollAndInvokeByViewHolder(
        indexes: List<Int>,
        invoke: (View) -> Unit
    ) {
        indexes.forEach {
            scrollHomeRecyclerViewToPosition(this, it)

            val viewHolder = this.findViewHolderForAdapterPosition(it)
            if (viewHolder is VH) invoke(viewHolder.itemView)
        }
    }

    private fun countTopAdsInItem(item: Visitable<*>): Int {
        var count = 0

        when (item) {
            is MixTopDataModel,
            is MixLeftDataModel,
            is FeaturedShopDataModel,
            is Lego4ProductDataModel,
            is FlashSaleDataModel -> {
                val channelGrids = (item as HasChannelModel).model.channelGrids
                count += channelGrids.count { it.isTopads }
            }
            is BestSellerDataModel -> {
                val recommendationItemList = item.recommendationItemList
                count += recommendationItemList.count { it.isTopAds }
            }
        }

        return count
    }

    private fun calculateTopAdsRecomFeedCount(view: View): Int {
        val recomFeedViewPager = view.findViewById<ViewPager>(R.id.view_pager_home_feeds)

        val recomFeedRecyclerView = (recomFeedViewPager.adapter as? FragmentStatePagerAdapter)
            ?.getItem(0)
            ?.view
            ?.findViewById<RecyclerView>(R.id.home_feed_fragment_recycler_view)

        val itemList = recomFeedRecyclerView?.getRecomItemList().orEmpty()

        return itemList.count {
            it is RecommendationCardModel && it.recommendationProductItem.isTopAds
        }
    }

    private fun waitForData() {
        Thread.sleep(8000)
    }

    private fun scrollHomeRecyclerViewToPosition(homeRecyclerView: RecyclerView, position: Int) {
        val layoutManager = homeRecyclerView.layoutManager as LinearLayoutManager

        activityRule.runOnUiThread {
            layoutManager.scrollToPositionWithOffset(position, 0)
        }

        // ensuring the scroll position is done
        Thread.sleep(2000)
    }

    private fun RecyclerView.getItemList(): List<Visitable<*>> {
        val homeAdapter = this.adapter as? HomeRecycleAdapter

        if (homeAdapter == null) {
            val detailMessage = "Adapter is not ${HomeRecycleAdapter::class.java.simpleName}"
            throw AssertionError(detailMessage)
        }

        return homeAdapter.currentList
    }

    private fun RecyclerView.getRecomItemList(): List<ForYouRecommendationVisitable> {
        val homeRecomAdapter = this.adapter as? GlobalHomeRecommendationAdapter

        if (homeRecomAdapter == null) {
            val detailMessage = "Adapter is not ${GlobalHomeRecommendationAdapter::class.java.simpleName}"
            throw AssertionError(detailMessage)
        }

        return homeRecomAdapter.currentList
    }

    private fun shouldDisplayRecyclerView() {
        Espresso.onView(
            ViewMatchers.withId(R.id.home_fragment_recycler_view)
        ).check(ViewAssertions.matches(isDisplayed()))
    }

    private fun getCurrentHomeRecyclerView(): RecyclerView {
        return activityRule.activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)
    }

    companion object {
        private const val LIMIT_COUNT_TO_IDLE = 10

        // min item 3 : blank space item, product item, and see all card item
        private const val MIX_LEFT_ITEM_COUNT_THRESHOLD = 3
    }
}
