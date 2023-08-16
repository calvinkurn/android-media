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
import com.tokopedia.shop.databinding.ItemShopHomeProductCarouselViewpagerBinding
import com.tokopedia.shop.home.util.ShopHomeProductCarouselTabDataProvider
import com.tokopedia.shop.home.view.fragment.ShopProductCarouselTabFragment
import com.tokopedia.shop.home.view.listener.ShopHomeProductCarouselListener
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselUiModel
import com.tokopedia.unifycomponents.TabsUnifyMediator
import com.tokopedia.unifycomponents.setCustomText
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselUiModel.Tab.ComponentList.Data.BannerType

class ShopHomeProductCarouselViewPagerViewHolder(
    itemView: View,
    private val listener: ShopHomeProductCarouselListener,
    private val provider: ShopHomeProductCarouselTabDataProvider
) : AbstractViewHolder<ShopHomeProductCarouselUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_product_carousel_viewpager
        private const val ONE_TAB = 1
        private const val TWO_TAB = 2
    }

    private val viewBinding: ItemShopHomeProductCarouselViewpagerBinding? by viewBinding()


    override fun bind(model: ShopHomeProductCarouselUiModel) {
        setupTitle(model)
        setupViewAllChevron(model)
        setupTabs(model.tabs)
    }

    private fun setupViewAllChevron(model: ShopHomeProductCarouselUiModel) {
        val hasVerticalBanner = hasVerticalBanner(model)

        viewBinding?.iconChevron?.isVisible = hasVerticalBanner
        viewBinding?.iconChevron?.setOnClickListener {
            listener.onProductCarouselChevronViewAllClick(model.viewAllChevronAppLink)
        }
    }

    private fun hasVerticalBanner(model: ShopHomeProductCarouselUiModel): Boolean {
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

    private fun setupTitle(model: ShopHomeProductCarouselUiModel) {
        viewBinding?.tpgTitle?.text = model.title
        viewBinding?.tpgTitle?.isVisible = model.title.isNotEmpty() && model.tabs.isNotEmpty()
    }

    private fun setupTabs(tabs: List<ShopHomeProductCarouselUiModel.Tab>) {
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

    private fun createFragments(tabs: List<ShopHomeProductCarouselUiModel.Tab>): List<Pair<String, Fragment>> {
        val pages = mutableListOf<Pair<String, Fragment>>()

        tabs.forEachIndexed { _, currentTab ->
            val fragment = ShopProductCarouselTabFragment.newInstance(provider.currentShopId, currentTab.componentList)
            fragment.setOnMainBannerClick { mainBanner -> listener.onProductCarouselMainBannerClick(mainBanner) }
            fragment.setOnProductClick { selectedShowcase -> listener.onProductCarouselProductClick(selectedShowcase) }
            fragment.setOnVerticalBannerClick { verticalBanner -> listener.onProductCarouselVerticalBannerClick(verticalBanner) }

            val displayedTabName = currentTab.name
            pages.add(Pair(displayedTabName, fragment))
        }

        return pages
    }

}
