package com.tokopedia.shop.home.view.adapter.viewholder.showcase_navigation.left

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
import com.tokopedia.shop.databinding.ItemShopHomeShowcaseNavigationLeftMainBannerBinding
import com.tokopedia.shop.home.util.ShopHomeShowcaseNavigationDependencyProvider
import com.tokopedia.shop.home.view.fragment.ShopShowcaseNavigationTabWidgetFragment
import com.tokopedia.shop.home.view.listener.ShopHomeShowcaseNavigationListener
import com.tokopedia.shop.home.view.model.showcase_navigation.appearance.LeftMainBannerAppearance
import com.tokopedia.shop.home.view.model.showcase_navigation.ShopHomeShowcaseNavigationUiModel
import com.tokopedia.shop.home.view.model.showcase_navigation.ShowcaseTab
import com.tokopedia.unifycomponents.TabsUnifyMediator
import com.tokopedia.unifycomponents.setCustomText
import com.tokopedia.utils.view.binding.viewBinding

class ShopHomeShowCaseNavigationLeftMainBannerViewHolder(
    itemView: View,
    private val listener: ShopHomeShowcaseNavigationListener,
    private val provider: ShopHomeShowcaseNavigationDependencyProvider
) : AbstractViewHolder<ShopHomeShowcaseNavigationUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_showcase_navigation_left_main_banner
        private const val SHOW_VIEW_ALL_SHOWCASE_THRESHOLD = 5
        private const val ONE_TAB = 1
    }

    private val viewBinding: ItemShopHomeShowcaseNavigationLeftMainBannerBinding? by viewBinding()


    override fun bind(model: ShopHomeShowcaseNavigationUiModel) {
        val tabs = if (model.appearance is LeftMainBannerAppearance) model.appearance.tabs else emptyList()
        setupShowcaseHeader(model.appearance.title, model.appearance.viewAllCtaAppLink, tabs)
        setupTabs(tabs)
    }

    private fun setupShowcaseHeader(title: String, viewAllCtaAppLink: String, tabs: List<ShowcaseTab>) {
        viewBinding?.tpgTitle?.text = title
        viewBinding?.tpgTitle?.isVisible = title.isNotEmpty() && tabs.isNotEmpty()

        viewBinding?.iconChevron?.setOnClickListener {
            listener.onNavigationBannerViewAllShowcaseClick(viewAllCtaAppLink)
        }

        val showcases = tabs.getOrNull(0)?.showcases ?: emptyList()
        viewBinding?.iconChevron?.isVisible = showcases.size > SHOW_VIEW_ALL_SHOWCASE_THRESHOLD
    }

    private fun setupTabs(tabs: List<ShowcaseTab>) {
        val fragments = createFragments(tabs)
        val pagerAdapter = TabPagerAdapter(provider.currentFragment, fragments)

        viewBinding?.run {
            viewPager.adapter = pagerAdapter

            tabsUnify.tabLayout.isTabIndicatorFullWidth = false
            tabsUnify.tabLayout.setBackgroundColor(Color.TRANSPARENT)
            tabsUnify.whiteShadeLeft.gone()
            tabsUnify.whiteShadeRight.gone()

            when {
                tabs.isEmpty() -> tabsUnify.gone()
                tabs.size == ONE_TAB -> tabsUnify.gone()
                else -> {
                    tabsUnify.visible()
                    tabsUnify.customTabMode = TabLayout.MODE_FIXED
                    tabsUnify.customTabGravity = TabLayout.GRAVITY_FILL
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

    private fun createFragments(tabs: List<ShowcaseTab>): List<Pair<String, Fragment>> {
        val pages = mutableListOf<Pair<String, Fragment>>()

        tabs.forEachIndexed { _, currentTab ->
            val fragment = ShopShowcaseNavigationTabWidgetFragment.newInstance(currentTab.showcases)
            fragment.setOnShowcaseClick { selectedShowcase ->
                listener.onNavigationBannerShowcaseClick(selectedShowcase)
            }

            val displayedTabName = currentTab.text
            pages.add(Pair(displayedTabName, fragment))
        }

        return pages
    }

}
