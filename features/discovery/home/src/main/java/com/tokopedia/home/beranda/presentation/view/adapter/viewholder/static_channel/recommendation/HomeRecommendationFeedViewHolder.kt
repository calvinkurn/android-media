package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.collapsing.tab.layout.CollapsingTabLayout
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.listener.HomeTabFeedListener
import com.tokopedia.home.beranda.presentation.view.adapter.HomeFeedPagerAdapter
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.RecommendationTabDataModel
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeRecommendationFeedDataModel
import java.util.*

/**
 * Created by henrypriyono on 22/03/18.
 */

class HomeRecommendationFeedViewHolder(itemView: View,
                                       private val listener: HomeCategoryListener) : AbstractViewHolder<HomeRecommendationFeedDataModel>(itemView), HomeTabFeedListener {
    private val homeFeedsViewPager: ViewPager = itemView.findViewById(R.id.view_pager_home_feeds)
    private val homeFeedsTabLayout: CollapsingTabLayout = itemView.findViewById(R.id.tab_layout_home_feeds)
    private val homeFeedsTabShadow: View = itemView.findViewById(R.id.view_feed_shadow)
    private val container: View = itemView.findViewById(R.id.home_recommendation_feed_container)
    private val context: Context = itemView.context
    private var homeFeedPagerAdapter: HomeFeedPagerAdapter? = null
    private var recommendationTabDataModelList: List<RecommendationTabDataModel>? = null

    override fun bind(homeRecommendationFeedDataModel: HomeRecommendationFeedDataModel) {
        val layoutParams = container.layoutParams

        //we must specify height for viewpager, so it can't scroll up anymore and create
        //sticky effect

        layoutParams.height = listener.windowHeight - listener.homeMainToolbarHeight + context.resources.getDimensionPixelSize(R.dimen.dp_8)
        container.layoutParams = layoutParams

        recommendationTabDataModelList = homeRecommendationFeedDataModel.recommendationTabDataModel

        homeFeedsTabLayout.visibility = View.VISIBLE
        homeFeedsViewPager.visibility = View.VISIBLE

        initViewPagerAndTablayout()
        homeRecommendationFeedDataModel.isNewData = false
    }

    private fun initViewPagerAndTablayout() {
        homeFeedPagerAdapter = HomeFeedPagerAdapter(
                listener,
                listener.eggListener,
                this,
                listener.childsFragmentManager,
                recommendationTabDataModelList,
                listener.parentPool)

        homeFeedsViewPager.offscreenPageLimit = DEFAULT_FEED_PAGER_OFFSCREEN_LIMIT
        homeFeedsViewPager.adapter = homeFeedPagerAdapter
        homeFeedsTabLayout.setup(homeFeedsViewPager, convertToTabItemDataList(recommendationTabDataModelList!!))
        homeFeedsTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position < recommendationTabDataModelList!!.size) {
                    val selectedFeedTabModel = recommendationTabDataModelList!![tab.position]
                    HomePageTracking.eventClickOnHomePageRecommendationTab(
                            context,
                            selectedFeedTabModel
                    )
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                val homeFeedFragment = homeFeedPagerAdapter!!.getRegisteredFragment(tab.position)
                homeFeedFragment?.scrollToTop()
                homeFeedsTabLayout.resetCollapseState()
            }
        })
    }


    private fun convertToTabItemDataList(recommendationTabDataModelList: List<RecommendationTabDataModel>): List<CollapsingTabLayout.TabItemData> {
        val tabItemDataList = ArrayList<CollapsingTabLayout.TabItemData>()
        for (feedTabModel in recommendationTabDataModelList) {
            tabItemDataList.add(CollapsingTabLayout.TabItemData(feedTabModel.name, feedTabModel.imageUrl))
        }
        return tabItemDataList
    }

    override fun onFeedContentScrolled(dy: Int, totalScrollY: Int) {
        homeFeedsTabLayout.adjustTabCollapseOnScrolled(dy, totalScrollY)
    }

    override fun onFeedContentScrollStateChanged(newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
            homeFeedsTabLayout.scrollActiveTabToLeftScreen()
        } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            homeFeedsTabLayout.snapCollapsingTab()
        }
    }

    fun showFeedTabShadow(show: Boolean?) {
        if (show!!) {
            homeFeedsTabShadow.visibility = View.VISIBLE
        } else {
            homeFeedsTabShadow.visibility = View.INVISIBLE
        }
    }

    fun scrollByVelocity(velocity: Int) {
        val fragment = homeFeedPagerAdapter?.getRegisteredFragment(
                homeFeedsViewPager.currentItem
        )
        fragment?.let {
            fragment.smoothScrollRecyclerViewByVelocity(velocity)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_recommendation_feed_viewholder
        private val TAG = HomeRecommendationFeedViewHolder::class.java.simpleName
        private const val DEFAULT_FEED_PAGER_OFFSCREEN_LIMIT = 10
    }
}
