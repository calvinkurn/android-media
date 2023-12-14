package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.collapsing.tab.layout.CollapsingTabLayout
import com.tokopedia.discovery.common.utils.toDpInt
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.listener.HomeTabFeedListener
import com.tokopedia.home.beranda.presentation.view.adapter.HomeFeedPagerAdapter
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.RecommendationTabDataModel
import com.tokopedia.home.beranda.presentation.view.uimodel.HomeRecommendationFeedDataModel
import com.tokopedia.home.databinding.HomeRecommendationFeedViewholderBinding
import com.tokopedia.home.util.HomeServerLogger
import java.util.*

/**
 * Created by henrypriyono on 22/03/18.
 */

class HomeRecommendationFeedViewHolder(
    view: View,
    private val listener: HomeCategoryListener,
    private val cardInteraction: Boolean = false
) : AbstractViewHolder<HomeRecommendationFeedDataModel>(view), HomeTabFeedListener {

    private val binding = HomeRecommendationFeedViewholderBinding.bind(itemView)

    private var homeFeedPagerAdapter: HomeFeedPagerAdapter? = null
    private var recommendationTabDataModelList: List<RecommendationTabDataModel>? = null

    override fun bind(homeRecommendationFeedDataModel: HomeRecommendationFeedDataModel) {
        with(binding) {
            val layoutParams = homeRecommendationFeedContainer.layoutParams

            // we must specify height for viewpager, so it can't scroll up anymore and create
            // sticky effect

            // 1st dp8 comes from N0 divider in home recommendation feed viewholder
            // 2nd dp8 comes from N50 divider in home recommendation feed viewholder
            // 3rd dp8 comes from N0 divider in home recommendation feed viewholder
            layoutParams?.height = listener.windowHeight - listener.homeMainToolbarHeight +
                HEIGHT_8.toDpInt() +
                HEIGHT_8.toDpInt() +
                HEIGHT_8.toDpInt()
            homeRecommendationFeedContainer.layoutParams = layoutParams

            recommendationTabDataModelList =
                homeRecommendationFeedDataModel.recommendationTabDataModel

            tabLayoutHomeFeeds.visibility = View.VISIBLE
            viewPagerHomeFeeds.visibility = View.VISIBLE

            initViewPagerAndTablayout(
                homeFeedsViewPager = viewPagerHomeFeeds,
                homeFeedsTabLayout = tabLayoutHomeFeeds
            )
            homeRecommendationFeedDataModel.isNewData = false
        }
    }

    private fun initViewPagerAndTablayout(
        homeFeedsViewPager: ViewPager?,
        homeFeedsTabLayout: CollapsingTabLayout?
    ) {
        homeFeedPagerAdapter = HomeFeedPagerAdapter(
            listener,
            listener.eggListener,
            this,
            listener.childsFragmentManager,
            recommendationTabDataModelList,
            listener.parentPool
        )

        homeFeedsViewPager?.offscreenPageLimit = DEFAULT_FEED_PAGER_OFFSCREEN_LIMIT
        try {
            homeFeedsViewPager?.adapter = homeFeedPagerAdapter
        } catch (e: IllegalStateException) {
            HomeServerLogger.logWarning(
                type = HomeServerLogger.TYPE_RECOM_SET_ADAPTER_ERROR,
                throwable = e,
                reason = e.message.toString()
            )
        }
        homeFeedsTabLayout?.setup(
            homeFeedsViewPager,
            convertToTabItemDataList(recommendationTabDataModelList!!),
            cardInteraction
        )
        homeFeedsTabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position < recommendationTabDataModelList!!.size) {
                    val selectedFeedTabModel = recommendationTabDataModelList!![tab.position]
                    HomePageTracking.eventClickOnHomePageRecommendationTab(
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

    fun selectJumperTab() {
        val tabIndex = recommendationTabDataModelList?.indexOfFirst { it.isJumperTab }
            ?: RecyclerView.NO_POSITION
        if (tabIndex != RecyclerView.NO_POSITION) {
            val tabToSelect = binding.tabLayoutHomeFeeds.getTabAt(tabIndex)
            tabToSelect?.select()
        }
    }

    private fun convertToTabItemDataList(recommendationTabDataModelList: List<RecommendationTabDataModel>): List<CollapsingTabLayout.TabItemData> {
        val tabItemDataList = ArrayList<CollapsingTabLayout.TabItemData>()
        for (feedTabModel in recommendationTabDataModelList) {
            tabItemDataList.add(
                CollapsingTabLayout.TabItemData(
                    feedTabModel.name,
                    feedTabModel.imageUrl
                )
            )
        }
        return tabItemDataList
    }

    override fun onFeedContentScrolled(dy: Int, totalScrollY: Int) {
        binding.tabLayoutHomeFeeds.adjustTabCollapseOnScrolled(dy, totalScrollY)
    }

    override fun onFeedContentScrollStateChanged(newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
            binding.tabLayoutHomeFeeds.scrollActiveTabToLeftScreen()
        } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            binding.tabLayoutHomeFeeds.snapCollapsingTab()
        }
    }

    fun showFeedTabShadow(show: Boolean?) {
        if (show == true) {
            binding.viewFeedShadow.visibility = View.VISIBLE
        } else {
            binding.viewFeedShadow.visibility = View.INVISIBLE
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_recommendation_feed_viewholder
        private val TAG = HomeRecommendationFeedViewHolder::class.java.simpleName
        private const val DEFAULT_FEED_PAGER_OFFSCREEN_LIMIT = 10
        private const val HEIGHT_8 = 8f
    }
}
