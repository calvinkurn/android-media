package com.tokopedia.home.topads

import android.Manifest
import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecycleAdapter
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.DynamicChannelSprintViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation.HomeRecommendationFeedViewHolder
import com.tokopedia.home.environment.InstrumentationHomeRevampTestActivity
import com.tokopedia.home_component.viewholders.FeaturedShopViewHolder
import com.tokopedia.home_component.viewholders.MixLeftComponentViewHolder
import com.tokopedia.home_component.viewholders.MixTopComponentViewHolder
import com.tokopedia.home_component.visitable.FeaturedShopDataModel
import com.tokopedia.home_component.visitable.MixLeftDataModel
import com.tokopedia.home_component.visitable.MixTopDataModel
import com.tokopedia.recommendation_widget_common.widget.bestseller.BestSellerViewHolder
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import com.tokopedia.searchbar.navigation_component.NavConstant
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.environment.callback.TopAdsVerificatorInterface
import com.tokopedia.test.application.espresso_component.CommonActions.clickOnEachItemRecyclerView
import com.tokopedia.test.application.util.InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser
import com.tokopedia.test.application.util.setupTopAdsDetector
import org.junit.*

/**
 * Created by DevAra
 * Instrumented test, which will execute on an Android device.
 * Verify Topads for HomePage
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class HomeTopAdsVerificationTest {

    @get:Rule
    var grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @get:Rule
    var activityRule = object: ActivityTestRule<InstrumentationHomeRevampTestActivity>(InstrumentationHomeRevampTestActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            disableCoachMark()
            loginInstrumentationTestTopAdsUser()
            setupTopAdsDetector()
        }
    }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private var topAdsCount = 0
    private val topAdsAssertion = TopAdsAssertion(context, TopAdsVerificatorInterface { topAdsCount })

    @After
    fun deleteDatabase() {
        topAdsAssertion.after()
    }

    @Test
    fun testTopAdsHome() {
        waitForData()

        val homeRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)
        val itemCount = homeRecyclerView.adapter?.itemCount?:0

        val itemList = homeRecyclerView.getItemList()
        topAdsCount = calculateTopAdsCount(itemList)

        for (i in 0 until itemCount) {
            scrollHomeRecyclerViewToPosition(homeRecyclerView, i)
            checkProductOnDynamicChannel(homeRecyclerView, i)
        }
        topAdsAssertion.assert()
    }

    private fun disableCoachMark(){
        val sharedPrefs = InstrumentationRegistry
                .getInstrumentation().context
                .getSharedPreferences(NavConstant.KEY_FIRST_VIEW_NAVIGATION, Context.MODE_PRIVATE)
        sharedPrefs.edit().putBoolean(
                NavConstant.KEY_FIRST_VIEW_NAVIGATION_ONBOARDING, false).apply()
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
            is DynamicChannelDataModel -> {
                item.channel?.grids?.let {
                    for (grid in it)
                        if (grid.isTopads) count++
                }
            }
            is FeaturedShopDataModel -> {
                for (grid in item.channelModel.channelGrids)
                    if (grid.isTopads) count++
            }
            is BestSellerDataModel -> {
                for (recom in item.recommendationItemList){
                    if(recom.isTopAds) count++
                }
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
            is DynamicChannelSprintViewHolder -> {
                clickOnEachItemRecyclerView(viewHolder.itemView, R.id.recycleList, 0)
            }
            is FeaturedShopViewHolder -> {
                clickOnEachItemRecyclerView(viewHolder.itemView, R.id.dc_banner_rv, 0)
            }
            is HomeRecommendationFeedViewHolder -> {
                waitForData()
                clickOnEachItemRecyclerView(viewHolder.itemView, R.id.home_feed_fragment_recycler_view, 0)
            }
            is BestSellerViewHolder -> {
                waitForData()
                clickOnEachItemRecyclerView(viewHolder.itemView, R.id.best_seller_recommendation_recycler_view, 0)
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
}
