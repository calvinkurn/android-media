package com.tokopedia.home.topads

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
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
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationAdapter
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecycleAdapter
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation.HomeRecommendationTypeFactoryImpl
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation.HomeRecommendationFeedViewHolder
import com.tokopedia.home.component.disableCoachMark
import com.tokopedia.home.environment.InstrumentationHomeRevampTestActivity
import com.tokopedia.home.util.HomeInstrumentationTestHelper.deleteHomeDatabase
import com.tokopedia.home.util.HomeRecyclerViewIdlingResource
import com.tokopedia.home.util.getRecyclerViewInfo
import com.tokopedia.home.util.scrollAndCheckRecyclerViewByType
import com.tokopedia.home_component.viewholders.FeaturedShopViewHolder
import com.tokopedia.home_component.viewholders.FlashSaleViewHolder
import com.tokopedia.home_component.viewholders.Lego4ProductViewHolder
import com.tokopedia.home_component.viewholders.MixLeftComponentViewHolder
import com.tokopedia.home_component.viewholders.MixTopComponentViewHolder
import com.tokopedia.home_component.visitable.FeaturedShopDataModel
import com.tokopedia.home_component.visitable.FlashSaleDataModel
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.home_component.visitable.Lego4ProductDataModel
import com.tokopedia.home_component.visitable.MixLeftDataModel
import com.tokopedia.home_component.visitable.MixTopDataModel
import com.tokopedia.recommendation_widget_common.widget.bestseller.BestSellerViewHolder
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import com.tokopedia.test.application.annotations.TopAdsTest
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.espresso_component.CommonActions.clickOnEachItemRecyclerView
import com.tokopedia.test.application.util.InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser
import com.tokopedia.test.application.util.setupTopAdsDetector
import org.junit.*
import com.tokopedia.carouselproductcard.R as carouselproductcardR
import com.tokopedia.recommendation_widget_common.R as recommendation_widget_commonR
import com.tokopedia.home_component.R as home_componentR

/**
 * Created by DevAra
 * Instrumented test, which will execute on an Android device.
 * Verify Topads for HomePage
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@TopAdsTest
class V2HomeTopAdsVerificationTest {
    companion object {
        private const val LIMIT_COUNT_TO_IDLE = 10

        // min item 3 : blank space item, product item, and see all card item
        private const val MIX_LEFT_ITEM_COUNT_THRESHOLD = 3
    }

    private var homeRecyclerViewIdlingResource: HomeRecyclerViewIdlingResource? = null
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private var topAdsCount = 0

    private var topAdsAssertion = TopAdsAssertion(context) { topAdsCount }

    @get:Rule
    var grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @get:Rule
    var activityRule = object : IntentsTestRule<InstrumentationHomeRevampTestActivity>(InstrumentationHomeRevampTestActivity::class.java) {
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
        val recyclerView: RecyclerView =
            activityRule.activity.findViewById(R.id.home_fragment_recycler_view)
        homeRecyclerViewIdlingResource = HomeRecyclerViewIdlingResource(
            recyclerView = recyclerView,
            limitCountToIdle = LIMIT_COUNT_TO_IDLE
        )
        IdlingRegistry.getInstance().register(homeRecyclerViewIdlingResource)
        activityRule.deleteHomeDatabase()
    }

    @After
    fun tearDown() {
        topAdsAssertion.after()
        IdlingRegistry.getInstance().unregister(homeRecyclerViewIdlingResource)
    }

    @Test
    fun testMixTopDataModel() {
        val targetId = home_componentR.id.dc_banner_rv
        val info = getRecyclerViewInfo(activityRule)

        activityRule.scrollAndCheckRecyclerViewByType<MixTopDataModel, MixTopComponentViewHolder>(
            info = info,
            targetId = targetId
        )

        val data = info.data.filterIsInstance<MixTopDataModel>()
        val topAdsCount = calculateTopAdsCount(data)

        topAdsAssertion.setTopAdsCount { topAdsCount }
        topAdsAssertion.assert()
    }

//    @Test
//    fun testMixLeftDataModel() {
//        val targetId = home_componentR.id.rv_product
//        val info = getRecyclerViewInfo(activityRule)
//
//        val data = info.data.filterIsInstance<MixLeftDataModel>()
//        val topAdsCount = calculateTopAdsCount(data)
//
//        topAdsAssertion.setTopAdsCount { topAdsCount }
//
//        activityRule.scrollAndCheckRecyclerViewByType<MixLeftDataModel, MixLeftComponentViewHolder>(
//            info = info,
//            targetId = targetId,
//            condition = {
//                val childRecyclerView: RecyclerView = it.itemView.findViewById(targetId)
//                val childItemCount = childRecyclerView.adapter?.itemCount ?: 0
//
//                childItemCount >= MIX_LEFT_ITEM_COUNT_THRESHOLD
//            }
//        )
//
//        topAdsAssertion.assert()
//    }
//
//    @Test
//    fun testFeaturedShopDataModel() {
//        val targetId = home_componentR.id.dc_banner_rv
//        val info = getRecyclerViewInfo(activityRule)
//
//        val data = info.data.filterIsInstance<FeaturedShopDataModel>()
//        val topAdsCount = calculateTopAdsCount(data)
//
//        topAdsAssertion.setTopAdsCount { topAdsCount }
//
//        activityRule.scrollAndCheckRecyclerViewByType<FeaturedShopDataModel, FeaturedShopViewHolder>(
//            info = info,
//            targetId = targetId
//        )
//
//        topAdsAssertion.assert()
//    }

//    private fun calculateAllTopAdsCount(recyclerView: RecyclerView) {
//        val itemList = recyclerView.getItemList()
//        topAdsCount += calculateTopAdsCount(itemList)
//    }

    private fun calculateTopAdsCount(itemList: List<Visitable<*>>): Int {
        var count = 0
        for (item in itemList) {
            count += countTopAdsInItem(item)
        }
        return count
    }

    private fun countTopAdsInItem(item: Visitable<*>): Int {
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
            is FeaturedShopDataModel -> {
                for (grid in item.channelModel.channelGrids)
                    if (grid.isTopads) count++
            }
            is BestSellerDataModel -> {
                for (recom in item.recommendationItemList) {
                    if (recom.isTopAds) count++
                }
            }
            is Lego4ProductDataModel -> {
                for (grid in item.channelModel.channelGrids) {
                    if (grid.isTopads) count++
                }
            }
            is FlashSaleDataModel -> {
                for (grid in item.channelModel.channelGrids) {
                    if (grid.isTopads) count++
                }
            }
        }
        return count
    }

    private fun calculateTopAdsRecomFeedCount(viewHolder: HomeRecommendationFeedViewHolder) {
        val recomFeedViewPager = viewHolder.itemView.findViewById<ViewPager>(R.id.view_pager_home_feeds)

        val recomFeedRecyclerView = (recomFeedViewPager.adapter as? FragmentStatePagerAdapter)?.getItem(0)?.view?.findViewById<RecyclerView>(R.id.home_feed_fragment_recycler_view)

        val itemList = recomFeedRecyclerView?.getRecomItemList().orEmpty()

        val count = itemList.count { it is HomeRecommendationItemDataModel && it.recommendationProductItem?.isTopAds == true }
        topAdsCount += count
    }

    private fun checkProductOnDynamicChannel(homeRecyclerView: RecyclerView, i: Int) {
        when (val viewHolder = homeRecyclerView.findViewHolderForAdapterPosition(i)) {
            is MixTopComponentViewHolder -> {
                waitForData()
                clickOnEachItemRecyclerView(viewHolder.itemView, home_componentR.id.dc_banner_rv, 0)
            }
            is MixLeftComponentViewHolder -> {
                val childRecyclerView: RecyclerView = viewHolder.itemView.findViewById(home_componentR.id.rv_product)
                val childItemCount = childRecyclerView.adapter?.itemCount ?: 0
                if (childItemCount >= MIX_LEFT_ITEM_COUNT_THRESHOLD) {
                    clickOnEachItemRecyclerView(viewHolder.itemView, home_componentR.id.rv_product, 0)
                }
            }
            is FeaturedShopViewHolder -> {
                waitForData()
                clickOnEachItemRecyclerView(viewHolder.itemView, home_componentR.id.dc_banner_rv, 0)
            }
            is HomeRecommendationFeedViewHolder -> {
                waitForData()
                calculateTopAdsRecomFeedCount(viewHolder)
                clickOnEachItemRecyclerView(viewHolder.itemView, R.id.home_feed_fragment_recycler_view, 0)
            }
            is BestSellerViewHolder -> {
                waitForData()
                clickOnEachItemRecyclerView(viewHolder.itemView, recommendation_widget_commonR.id.best_seller_recommendation_recycler_view, 0)
            }
            is Lego4ProductViewHolder -> {
                clickOnEachItemRecyclerView(viewHolder.itemView, R.id.recycleList, 0)
            }
            is FlashSaleViewHolder -> {
                clickOnEachItemRecyclerView(viewHolder.itemView, carouselproductcardR.id.carouselProductCardRecyclerView, 0)
            }
        }
    }

    private fun waitForData() {
        Thread.sleep(8000)
    }

    private fun scrollHomeRecyclerViewToPosition(homeRecyclerView: RecyclerView, position: Int) {
        val layoutManager = homeRecyclerView.layoutManager as LinearLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
    }

    private fun RecyclerView.getItemList(): List<Visitable<*>> {
        val homeAdapter = this.adapter as? HomeRecycleAdapter

        if (homeAdapter == null) {
            val detailMessage = "Adapter is not ${HomeRecycleAdapter::class.java.simpleName}"
            throw AssertionError(detailMessage)
        }

        return homeAdapter.currentList
    }

    private fun RecyclerView.getRecomItemList(): List<Visitable<HomeRecommendationTypeFactoryImpl>> {
        val homeRecomAdapter = this.adapter as? HomeRecommendationAdapter

        if (homeRecomAdapter == null) {
            val detailMessage = "Adapter is not ${HomeRecommendationAdapter::class.java.simpleName}"
            throw AssertionError(detailMessage)
        }

        return homeRecomAdapter.currentList
    }
}
