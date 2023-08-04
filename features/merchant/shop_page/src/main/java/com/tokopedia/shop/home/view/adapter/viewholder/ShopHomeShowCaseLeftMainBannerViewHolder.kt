package com.tokopedia.shop.home.view.adapter.viewholder

import android.graphics.Color
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.ItemShopHomeShowcaseLeftMainBannerBinding
import com.tokopedia.shop.home.view.fragment.ShopShowcaseFragment
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseUiModel
import com.tokopedia.unifycomponents.TabsUnifyMediator
import com.tokopedia.unifycomponents.setCustomText
import com.tokopedia.utils.view.binding.viewBinding

class ShopHomeShowCaseLeftMainBannerViewHolder(
    itemView: View,
    private val fragment: Fragment
) : AbstractViewHolder<ShopHomeShowcaseUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_showcase_left_main_banner
        private const val SHOW_VIEW_ALL_SHOWCASE_THRESHOLD = 5
    }

    private val viewBinding: ItemShopHomeShowcaseLeftMainBannerBinding? by viewBinding()


    override fun bind(model: ShopHomeShowcaseUiModel) {
        setupShowcaseHeader(model)
        setupTabs(model.tabs)
    }

    private fun setupShowcaseHeader(model: ShopHomeShowcaseUiModel) {
        viewBinding?.tpgTitle?.text = model.showcaseHeader.title
        val showcases = model.tabs.getOrNull(0)?.showcases ?: emptyList()
        viewBinding?.iconChevron?.isVisible = showcases.size > SHOW_VIEW_ALL_SHOWCASE_THRESHOLD
    }

    private fun setupTabs(tabs: List<ShopHomeShowcaseUiModel.ShopHomeShowCaseTab>) {
        val fragments = createFragments(tabs)
        val pagerAdapter = TabPagerAdapter(fragment, fragments)

        viewBinding?.run {
            viewPager.adapter = pagerAdapter
            tabsUnify.isVisible = tabs.size > 1
            tabsUnify.customTabMode = TabLayout.MODE_SCROLLABLE
            tabsUnify.tabLayout.isTabIndicatorFullWidth = false
            tabsUnify.tabLayout.setBackgroundColor(Color.TRANSPARENT)

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
        tabs: List<ShopHomeShowcaseUiModel.ShopHomeShowCaseTab>
    ): List<Pair<String, Fragment>> {
        val pages = mutableListOf<Pair<String, Fragment>>()

        tabs.forEachIndexed { _, currentTab ->
            val fragment = ShopShowcaseFragment.newInstance(currentTab.showcases)

            val displayedTabName = currentTab.text
            pages.add(Pair(displayedTabName, fragment))
        }

        return pages
    }

}
