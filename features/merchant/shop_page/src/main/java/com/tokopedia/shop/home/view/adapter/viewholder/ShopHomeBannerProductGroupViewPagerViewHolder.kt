package com.tokopedia.shop.home.view.adapter.viewholder

import android.graphics.Color
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.ItemShopHomeBannerProductGroupViewpagerBinding
import com.tokopedia.shop.home.util.ShopBannerProductGroupWidgetTabDependencyProvider
import com.tokopedia.shop.home.view.fragment.ShopBannerProductGroupWidgetTabFragment
import com.tokopedia.shop.home.view.listener.ShopBannerProductGroupListener
import com.tokopedia.shop.home.view.model.ShopWidgetComponentBannerProductGroupUiModel
import com.tokopedia.unifycomponents.TabsUnifyMediator
import com.tokopedia.unifycomponents.setCustomText
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.shop.home.view.model.ShopWidgetComponentBannerProductGroupUiModel.Tab.ComponentList.Data.BannerType

class ShopHomeBannerProductGroupViewPagerViewHolder(
    itemView: View,
    private val listener: ShopBannerProductGroupListener,
    private val provider: ShopBannerProductGroupWidgetTabDependencyProvider
) : AbstractViewHolder<ShopWidgetComponentBannerProductGroupUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_banner_product_group_viewpager
        private const val ONE_TAB = 1
        private const val TWO_TAB = 2
    }

    private val viewBinding: ItemShopHomeBannerProductGroupViewpagerBinding? by viewBinding()

    init {
        disableTabSwipeBehavior()
    }

    override fun bind(model: ShopWidgetComponentBannerProductGroupUiModel) {
        setupTitle(model)
        setupViewAllChevron(model)
        setupTabs(model.tabs)
    }

    private fun setupViewAllChevron(model: ShopWidgetComponentBannerProductGroupUiModel) {
        val hasVerticalBanner = hasVerticalBanner(model)

        viewBinding?.iconChevron?.isVisible = hasVerticalBanner
        viewBinding?.iconChevron?.setOnClickListener {
            listener.onBannerProductGroupViewAllClick(model.viewAllChevronAppLink)
        }
    }

    private fun hasVerticalBanner(model: ShopWidgetComponentBannerProductGroupUiModel): Boolean {
        model.tabs.forEach { tab ->
            tab.componentList.forEach { component ->
                component.data.forEach { data ->
                    val hasVerticalBanner = data.bannerType == BannerType.VERTICAL
                    if (hasVerticalBanner) {
                        return true
                    }
                }
            }
        }

        return false
    }

    private fun setupTitle(model: ShopWidgetComponentBannerProductGroupUiModel) {
        viewBinding?.tpgTitle?.text = model.title
        viewBinding?.tpgTitle?.isVisible = model.title.isNotEmpty() && model.tabs.isNotEmpty()
    }

    private fun setupTabs(tabs: List<ShopWidgetComponentBannerProductGroupUiModel.Tab>) {
        val fragments = createFragments(tabs)
        val pagerAdapter = TabPagerAdapter(provider.fragment, fragments)

        viewBinding?.run {
            viewPager.adapter = pagerAdapter

            tabsUnify.tabLayout.isTabIndicatorFullWidth = false
            tabsUnify.tabLayout.setBackgroundColor(Color.TRANSPARENT)
            tabsUnify.whiteShadeLeft.gone()
            tabsUnify.whiteShadeRight.gone()

            when {
                tabs.isEmpty() -> tabsUnify.gone()
                tabs.size == ONE_TAB -> tabsUnify.gone()
                tabs.size == TWO_TAB -> {
                    tabsUnify.visible()
                    tabsUnify.customTabMode = TabLayout.MODE_FIXED
                    tabsUnify.customTabGravity = TabLayout.GRAVITY_FILL
                }
                else -> {
                    tabsUnify.visible()
                    tabsUnify.customTabMode = TabLayout.MODE_SCROLLABLE
                    tabsUnify.customTabGravity = TabLayout.GRAVITY_CENTER
                }
            }


            val centeredTabIndicator = ContextCompat.getDrawable(tabsUnify.tabLayout.context, R.drawable.shape_showcase_tab_indicator_color)
            tabsUnify.tabLayout.setSelectedTabIndicator(centeredTabIndicator)

            TabsUnifyMediator(tabsUnify, viewPager) { tab, currentPosition ->
                tab.setCustomText(fragments[currentPosition].first)
            }
        }

    }

    private class TabPagerAdapter(
        fragment: Fragment,
        private val fragments: List<Pair<String, Fragment>>
    ) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = fragments.size
        override fun createFragment(position: Int) = fragments[position].second
    }

    private fun createFragments(
        tabs: List<ShopWidgetComponentBannerProductGroupUiModel.Tab>
    ): List<Pair<String, Fragment>> {
        val pages = mutableListOf<Pair<String, Fragment>>()

        tabs.forEachIndexed { _, currentTab ->
            val fragment = ShopBannerProductGroupWidgetTabFragment.newInstance(provider.currentShopId, currentTab.componentList)
            fragment.setOnMainBannerClick { mainBanner -> listener.onBannerProductGroupMainBannerClick(mainBanner) }
            fragment.setOnProductClick { selectedShowcase -> listener.onBannerProductGroupProductClick(selectedShowcase) }
            fragment.setOnVerticalBannerClick { verticalBanner -> listener.onBannerProductGroupVerticalBannerClick(verticalBanner) }

            val displayedTabName = currentTab.label
            pages.add(Pair(displayedTabName, fragment))
        }

        return pages
    }

    private fun disableTabSwipeBehavior() {
        viewBinding?.viewPager?.isUserInputEnabled = false
    }
}
