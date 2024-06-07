package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
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
import com.tokopedia.home.beranda.presentation.view.helper.HomeRollenceController
import com.tokopedia.home.beranda.presentation.view.uimodel.HomeRecommendationFeedDataModel
import com.tokopedia.home.databinding.HomeRecommendationFeedViewholderBinding
import com.tokopedia.home.util.HomeRefreshType
import com.tokopedia.home.util.HomeServerLogger
import com.tokopedia.kotlin.extensions.view.onTabReselected
import com.tokopedia.kotlin.extensions.view.onTabSelected
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.remoteconfig.RemoteConfig

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

    override fun bind(model: HomeRecommendationFeedDataModel) {
        with(binding) {
            val layoutParams = homeRecommendationFeedContainer.layoutParams

            // we must specify height for viewpager, so it can't scroll up anymore and create
            // sticky effect

            // 1st dp8 comes from N0 divider in home recommendation feed viewholder
            // 2nd dp8 comes from N50 divider in home recommendation feed viewholder
            // 3rd dp8 comes from N0 divider in home recommendation feed viewholder
            layoutParams?.height = listener.windowHeight - listener.homeMainToolbarHeight +
                HEIGHT_8.toDpInt() +
                HEIGHT_8.toDpInt()
            homeRecommendationFeedContainer.layoutParams = layoutParams

            recommendationTabDataModelList = model.recommendationTabDataModel

            initViewPager()

            if (isMegaTabEnabled()) {
                initMegaTabLayout()
            } else {
                initCarouselTabLayout()
            }

            model.isNewData = false
        }
    }

    private fun initViewPager() {
        binding.viewPagerHomeFeeds.show()

        homeFeedPagerAdapter = HomeFeedPagerAdapter(
            listener,
            listener.eggListener,
            this,
            listener.childsFragmentManager,
            recommendationTabDataModelList,
            listener.parentPool,
            listener.getHomeRefreshType()
        )

        binding.viewPagerHomeFeeds.offscreenPageLimit = DEFAULT_FEED_PAGER_OFFSCREEN_LIMIT

        try {
            binding.viewPagerHomeFeeds.adapter = homeFeedPagerAdapter
        } catch (e: IllegalStateException) {
            HomeServerLogger.logWarning(
                type = HomeServerLogger.TYPE_RECOM_SET_ADAPTER_ERROR,
                throwable = e,
                reason = e.message.toString()
            )
        }
    }

    private fun initMegaTabLayout() {
        with(binding) {
            recommendationTabDataModelList
                ?.map { it.toMegaTabItem() }
                ?.let { tabRecommendation.set(it, viewPagerHomeFeeds) }

            tabRecommendation.onTabSelected(::onTabLayoutSelected)
            tabRecommendation.onTabReselected(::onTabLayoutReselected)
            tabRecommendation.show()
            megatabStroke.show()
        }
    }

    private fun initCarouselTabLayout() {
        with(binding) {
            recommendationTabDataModelList
                ?.map { it.toCarouselTabItem() }
                ?.let { tabLayoutHomeFeeds.setup(viewPagerHomeFeeds, it, cardInteraction) }

            tabLayoutHomeFeeds.onTabSelected(::onTabLayoutSelected)
            tabLayoutHomeFeeds.onTabReselected(::onTabLayoutReselected)
            tabLayoutHomeFeeds.show()
        }
    }

    private fun onTabLayoutSelected(tab: TabLayout.Tab) {
        homeFeedPagerAdapter
            ?.getRegisteredFragment(tab.position)
            ?.setRefreshType(listener.getHomeRefreshType())

        if (tab.position < recommendationTabDataModelList!!.size) {
            val selectedFeedTabModel = recommendationTabDataModelList!![tab.position]
            HomePageTracking.eventClickOnHomePageRecommendationTab(
                selectedFeedTabModel
            )
        }
    }

    private fun onTabLayoutReselected(tab: TabLayout.Tab) {
        homeFeedPagerAdapter
            ?.getRegisteredFragment(tab.position)
            ?.scrollToTop()

        if (isMegaTabEnabled().not()) {
            binding.tabLayoutHomeFeeds.resetCollapseState()
        }
    }

    fun selectJumperTab() {
        val tabIndex = recommendationTabDataModelList?.indexOfFirst { it.isJumperTab }
            ?: RecyclerView.NO_POSITION
        if (tabIndex != RecyclerView.NO_POSITION) {
            val tabToSelect = if (isMegaTabEnabled().not()) {
                binding.tabLayoutHomeFeeds.getTabAt(tabIndex)
            } else {
                binding.tabRecommendation.getTabAt(tabIndex)
            }

            tabToSelect?.select()
        }
    }

    override fun onFeedContentScrolled(dy: Int, totalScrollY: Int) {
        binding.tabLayoutHomeFeeds.ifVisible { adjustTabCollapseOnScrolled(dy, totalScrollY) }
    }

    override fun onFeedContentScrollStateChanged(newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
            binding.tabLayoutHomeFeeds.ifVisible { scrollActiveTabToLeftScreen() }
        } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            binding.tabLayoutHomeFeeds.ifVisible { snapCollapsingTab() }
        }
    }

    fun showFeedTabShadow(show: Boolean?) {
        if (show == true) {
            binding.viewFeedShadow.visibility = View.VISIBLE
        } else {
            binding.viewFeedShadow.visibility = View.INVISIBLE
        }
    }

    private fun isMegaTabEnabled() = HomeRollenceController.isMegaTabEnabled

    private inline fun <T : CollapsingTabLayout> T.ifVisible(invoke: T.() -> Unit) {
        if (visibility == View.VISIBLE && isMegaTabEnabled()) invoke()
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.home_recommendation_feed_viewholder

        private const val DEFAULT_FEED_PAGER_OFFSCREEN_LIMIT = 10
        private const val HEIGHT_8 = 8f
    }
}
