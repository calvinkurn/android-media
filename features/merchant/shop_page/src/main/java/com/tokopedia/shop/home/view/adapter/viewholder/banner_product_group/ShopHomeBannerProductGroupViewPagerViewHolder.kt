package com.tokopedia.shop.home.view.adapter.viewholder.banner_product_group

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.shop.R
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.databinding.ItemShopHomeBannerProductGroupViewpagerBinding
import com.tokopedia.shop.home.util.ShopBannerProductGroupWidgetTabDependencyProvider
import com.tokopedia.shop.home.view.fragment.ShopBannerProductGroupWidgetTabFragment
import com.tokopedia.shop.home.view.listener.ShopBannerProductGroupListener
import com.tokopedia.shop.home.view.model.banner_product_group.BannerProductGroupUiModel
import com.tokopedia.unifycomponents.TabsUnify
import com.tokopedia.unifycomponents.TabsUnifyMediator
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifycomponents.R as unifycomponentsR

class ShopHomeBannerProductGroupViewPagerViewHolder(
    itemView: View,
    private val listener: ShopBannerProductGroupListener,
    private val provider: ShopBannerProductGroupWidgetTabDependencyProvider
) : AbstractViewHolder<BannerProductGroupUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_banner_product_group_viewpager
        private const val ONE_TAB = 1
        private const val MARGIN_16_DP = 16f
    }

    private val viewBinding: ItemShopHomeBannerProductGroupViewpagerBinding? by viewBinding()
    private var isProductSuccessfullyLoaded = false
    private var tabTotalWidth = 0

    init {
        disableTabSwipeBehavior()
    }

    override fun bind(model: BannerProductGroupUiModel) {
        if (!isProductSuccessfullyLoaded){
            setupTitle(model)
            setupViewAllChevron(model)
            setupTabs(model)
            setupColors(model.header.isOverrideTheme, model.header.colorSchema)
        }
    }

    private fun setupViewAllChevron(model: BannerProductGroupUiModel) {
        val hasVerticalBanner = model.widgetStyle == BannerProductGroupUiModel.WidgetStyle.VERTICAL.id

        viewBinding?.iconChevron?.isVisible = hasVerticalBanner && model.viewAllChevronAppLink.isNotEmpty()
        viewBinding?.iconChevron?.setOnClickListener {
            listener.onBannerProductGroupViewAllClick(model.viewAllChevronAppLink)
        }
    }

    private fun setupTitle(model: BannerProductGroupUiModel) {
        viewBinding?.tpgTitle?.text = model.title
        viewBinding?.tpgTitle?.isVisible = model.title.isNotEmpty() && model.tabs.isNotEmpty()
    }

    private fun setupTabs(model: BannerProductGroupUiModel) {
        val fragments = createFragments(model)
        val pagerAdapter = TabPagerAdapter(provider.productCarouselHostFragmentManager, provider.productCarouselHostLifecycle, fragments)

        viewBinding?.run {
            viewPager.adapter = pagerAdapter
            tabsUnify.tabLayout.isTabIndicatorFullWidth = false
            tabsUnify.tabLayout.setBackgroundColor(Color.TRANSPARENT)
            tabsUnify.whiteShadeLeft.gone()
            tabsUnify.whiteShadeRight.gone()

            val centeredTabIndicator = ContextCompat.getDrawable(tabsUnify.tabLayout.context, R.drawable.shape_showcase_tab_indicator_color)
            tabsUnify.tabLayout.setSelectedTabIndicator(centeredTabIndicator)

            TabsUnifyMediator(tabsUnify, viewPager) { tab, currentPosition ->
                val tabView = LayoutInflater.from(tabsUnify.context).inflate(R.layout.item_viewpager_showcase_navigation_tab, tabsUnify, false)
                tab.customView = tabView

                val tabTitle : Typography? = tabView.findViewById(R.id.tpgTabTitle)
                tabTitle?.text = fragments[currentPosition].first

                if (currentPosition == 0) tab.select(model) else tab.unselect(model)

                tab.view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                val tabWidth = (tab.view.measuredWidth + MARGIN_16_DP.dpToPx() + MARGIN_16_DP.dpToPx()).toInt()
                tabTotalWidth += tabWidth
            }

            handleTabChange(tabsUnify, model)
            applyTabRuleWidth(model.tabs, tabsUnify)
        }

    }

    private fun handleTabChange(tabsUnify: TabsUnify, model: BannerProductGroupUiModel) {
        tabsUnify.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.select(model)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab.unselect(model)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })
    }

    private fun TabLayout.Tab?.select(model: BannerProductGroupUiModel) {
        val tabTitle = this?.customView?.findViewById<Typography>(R.id.tpgTabTitle)

        val highEmphasizeColor = if (model.header.isOverrideTheme && model.header.colorSchema.listColorSchema.isNotEmpty()) {
            model.header.colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS)
        } else {
            ContextCompat.getColor(tabTitle?.context ?: return, unifycomponentsR.color.Unify_NN950)
        }

        tabTitle?.apply {
            setTypeface(Typography.getFontType(context, true, Typography.DISPLAY_3))
            setTextColor(highEmphasizeColor)
            invalidate()
        }
    }

    private fun TabLayout.Tab?.unselect(model: BannerProductGroupUiModel) {
        val tabTitle = this?.customView?.findViewById<Typography>(R.id.tpgTabTitle)

        val disabledTextColor = if (model.header.isOverrideTheme && model.header.colorSchema.listColorSchema.isNotEmpty()) {
            model.header.colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.DISABLED_TEXT_COLOR)
        } else {
            ContextCompat.getColor(tabTitle?.context ?: return, unifycomponentsR.color.Unify_NN950)
        }

        tabTitle?.apply {
            setTypeface(Typography.getFontType(context, false, Typography.DISPLAY_3))
            setTextColor(disabledTextColor)
            invalidate()
        }
    }

    private fun applyTabRuleWidth(tabs: List<BannerProductGroupUiModel.Tab>, tabsUnify: TabsUnify) {
        tabsUnify.post {
            when {
                tabs.isEmpty() -> tabsUnify.gone()
                tabs.size == ONE_TAB -> tabsUnify.gone()
                else -> {
                    tabsUnify.visible()

                    val screenWidth = DeviceScreenInfo.getScreenWidth(tabsUnify.context) - MARGIN_16_DP.dpToPx() - MARGIN_16_DP.dpToPx()
                    if (tabTotalWidth < screenWidth) {
                        tabsUnify.customTabMode = TabLayout.MODE_FIXED
                        tabsUnify.customTabGravity = TabLayout.GRAVITY_FILL
                    } else {
                        tabsUnify.customTabMode = TabLayout.MODE_SCROLLABLE
                        tabsUnify.customTabGravity = TabLayout.GRAVITY_FILL
                    }
                }
            }
        }
    }

    private class TabPagerAdapter(
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle,
        private val fragments: List<Pair<String, Fragment>>
    ) : FragmentStateAdapter(fragmentManager, lifecycle) {

        override fun getItemCount(): Int = fragments.size
        override fun createFragment(position: Int) = fragments[position].second
    }

    private fun createFragments(model: BannerProductGroupUiModel): List<Pair<String, Fragment>> {
        val pages = mutableListOf<Pair<String, Fragment>>()

        model.tabs.forEachIndexed { _, currentTab ->
            val fragment = ShopBannerProductGroupWidgetTabFragment.newInstance(
                provider.currentShopId,
                currentTab.componentList,
                model.widgetStyle,
                model.header.isOverrideTheme,
                model.header.colorSchema
            )
            fragment.setOnMainBannerClick { mainBanner -> listener.onBannerProductGroupMainBannerClick(mainBanner) }
            fragment.setOnProductClick { selectedShowcase -> listener.onBannerProductGroupProductClick(selectedShowcase) }
            fragment.setOnVerticalBannerClick { verticalBanner -> listener.onBannerProductGroupVerticalBannerClick(verticalBanner) }
            fragment.setOnProductSuccessfullyLoaded { isProductSuccessfullyLoaded ->
                this.isProductSuccessfullyLoaded = isProductSuccessfullyLoaded
            }

            val displayedTabName = currentTab.label
            pages.add(Pair(displayedTabName, fragment))
        }

        return pages
    }

    private fun disableTabSwipeBehavior() {
        viewBinding?.viewPager?.isUserInputEnabled = false
    }

    private fun setupColors(overrideTheme: Boolean, colorSchema: ShopPageColorSchema) {
        val chevronColor = if (overrideTheme) {
            colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.ICON_CTA_LINK_COLOR)
        } else {
            ContextCompat.getColor(viewBinding?.iconChevron?.context ?: return, unifycomponentsR.color.Unify_NN950)
        }

        val highEmphasizeColor = if (overrideTheme) {
            colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS)
        } else {
            ContextCompat.getColor(viewBinding?.tpgTitle?.context ?: return, unifycomponentsR.color.Unify_NN950)
        }

        viewBinding?.apply {
            iconChevron.setImage(
                newIconId = IconUnify.CHEVRON_RIGHT,
                newLightEnable = chevronColor,
                newDarkEnable = chevronColor
            )
            tpgTitle.setTextColor(highEmphasizeColor)
        }
    }
}
