package com.tokopedia.shop.home.view.adapter.viewholder.showcase_navigation.left

import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.shop.R
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.databinding.ItemShopHomeShowcaseNavigationLeftMainBannerBinding
import com.tokopedia.shop.home.util.ShopHomeReimagineShowcaseNavigationDependencyProvider
import com.tokopedia.shop.home.view.fragment.ShopShowcaseNavigationTabWidgetFragment
import com.tokopedia.shop.home.view.listener.ShopHomeReimagineShowcaseNavigationListener
import com.tokopedia.shop.home.view.model.showcase_navigation.ShowcaseNavigationUiModel
import com.tokopedia.shop.home.view.model.showcase_navigation.ShowcaseTab
import com.tokopedia.shop.home.view.model.showcase_navigation.appearance.LeftMainBannerAppearance
import com.tokopedia.unifycomponents.TabsUnify
import com.tokopedia.unifycomponents.TabsUnifyMediator
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.unifycomponents.setCustomText
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.tokopedia.unifycomponents.R as unifycomponentsR

class ShopHomeShowCaseNavigationLeftMainBannerViewHolder(
    itemView: View,
    private val listener: ShopHomeReimagineShowcaseNavigationListener,
    private val provider: ShopHomeReimagineShowcaseNavigationDependencyProvider
) : AbstractViewHolder<ShowcaseNavigationUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_showcase_navigation_left_main_banner
        private const val ONE_TAB = 1
        private const val MARGIN_16_DP = 16f
        private const val MINIMAL_SHOWCASE_COUNT_ON_A_TAB = 5
        private const val THREE_TAB = 3
        private const val EXTRA_WIDTH_TAB_TITLE_DP = 8
    }

    private var tabTotalWidth = 0
    private val viewBinding: ItemShopHomeShowcaseNavigationLeftMainBannerBinding? by viewBinding()

    override fun bind(model: ShowcaseNavigationUiModel) {
        val tabs = if (model.appearance is LeftMainBannerAppearance) model.appearance.tabs else emptyList()

        // Render tab only if it has 5 showcase or more than 5 showcase
        val validatedTabs = tabs.filter {
            val showcaseCount = it.showcases.size
            showcaseCount >= MINIMAL_SHOWCASE_COUNT_ON_A_TAB
        }

        setupTitle(model, validatedTabs)
        setupTabs(validatedTabs, model)
        setupChevronViewAll(
            model,
            validatedTabs,
            model.header.isOverrideTheme,
            model.header.colorSchema
        )

        autoScrollTabByLastSelectedTabPosition(model)
    }

    private fun autoScrollTabByLastSelectedTabPosition(model: ShowcaseNavigationUiModel) {
        viewBinding?.viewPager?.setCurrentItem(model.lastTabIndexSelected, false)

        val selectedTab = viewBinding?.tabsUnify?.getUnifyTabLayout()?.getTabAt(model.lastTabIndexSelected)
        selectedTab?.select()
        selectedTab?.selectedTextColor(model)
    }

    private fun setupChevronViewAll(
        model: ShowcaseNavigationUiModel,
        tabs: List<ShowcaseTab>,
        overrideTheme: Boolean,
        colorSchema: ShopPageColorSchema
    ) {
        viewBinding?.iconChevron?.setOnClickListener {
            listener.onNavigationBannerViewAllShowcaseClick(
                model.appearance.viewAllCtaAppLink,
                model.appearance,
                tabs.firstOrNull()?.showcases?.firstOrNull()?.id.orEmpty()
            )
        }

        val chevronColor = if (overrideTheme && colorSchema.listColorSchema.isNotEmpty()) {
            colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.ICON_ENABLED_HIGH_COLOR)
        } else {
            ContextCompat.getColor(viewBinding?.iconChevron?.context ?: return, unifycomponentsR.color.Unify_NN950)
        }

        viewBinding?.iconChevron?.setImage(
            newIconId = IconUnify.CHEVRON_RIGHT,
            newLightEnable = chevronColor,
            newDarkEnable = chevronColor
        )

        val hasTab = tabs.size > ONE_TAB
        val showChevronViewAll = if (hasTab) {
            // Left main banner have more than 1 tab
            model.appearance.viewAllCtaAppLink.isNotEmpty() && model.appearance.title.isNotEmpty() && tabs.isNotEmpty()
        } else {
            // Left main banner have only 1 tab
            model.appearance.viewAllCtaAppLink.isNotEmpty() && model.appearance.title.isNotEmpty()
        }

        viewBinding?.iconChevron?.isVisible = showChevronViewAll
    }

    private fun setupTitle(model: ShowcaseNavigationUiModel, tabs: List<ShowcaseTab>) {
        viewBinding?.tpgTitle?.text = model.appearance.title
        viewBinding?.tpgTitle?.isVisible = model.appearance.title.isNotEmpty() && tabs.isNotEmpty()

        val highEmphasizeColor = if (model.header.isOverrideTheme && model.header.colorSchema.listColorSchema.isNotEmpty()) {
            model.header.colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS)
        } else {
            ContextCompat.getColor(viewBinding?.tpgTitle?.context ?: return, unifycomponentsR.color.Unify_NN950)
        }
        viewBinding?.tpgTitle?.setTextColor(highEmphasizeColor)
    }

    private fun setupTabs(
        tabs: List<ShowcaseTab>,
        uiModel: ShowcaseNavigationUiModel
    ) {
        val fragments = createFragments(tabs, uiModel)
        val pagerAdapter = TabPagerAdapter(provider.currentFragment, fragments)

        viewBinding?.run {
            viewPager.adapter = pagerAdapter

            tabsUnify.tabLayout.isTabIndicatorFullWidth = false
            tabsUnify.tabLayout.setBackgroundColor(Color.TRANSPARENT)
            tabsUnify.whiteShadeLeft.gone()
            tabsUnify.whiteShadeRight.gone()

            val centeredTabIndicator = ContextCompat.getDrawable(tabsUnify.tabLayout.context, R.drawable.shape_showcase_tab_indicator_color)
            tabsUnify.tabLayout.setSelectedTabIndicator(centeredTabIndicator)

            TabsUnifyMediator(tabsUnify, viewPager) { tab, currentPosition ->
                tab.setCustomText(tabs[currentPosition].text)
                tab.normalTextColor(uiModel)

                val tabTitle = tabs[currentPosition].text
                changeTabTitleAppearance(tab, tabTitle)

                //Measure tab width
                tab.view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                val tabWidth = (tab.view.measuredWidth + MARGIN_16_DP.dpToPx() + MARGIN_16_DP.dpToPx()).toInt()
                tabTotalWidth += tabWidth
            }

            handleTabChange(tabsUnify, uiModel, tabs)
            applyTabRuleWidth(tabs, tabsUnify)
        }
    }

    private fun changeTabTitleAppearance(
        tab: TabLayout.Tab,
        tabTitle: String
    ) {
        val tabTitleTextView = tab.tabTitleTextView()
        tabTitleTextView?.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            tabTitleTextView.context.resources.getDimension(R.dimen.tab_name_font_size)
        )
        tabTitleTextView?.text = tabTitle

        tabTitleTextView?.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        tabTitleTextView?.layoutParams?.width = tabTitleTextView?.measuredWidth.orZero() + EXTRA_WIDTH_TAB_TITLE_DP.toPx()
    }

    private fun handleTabChange(
        tabsUnify: TabsUnify,
        model: ShowcaseNavigationUiModel,
        tabs: List<ShowcaseTab>
    ) {
        tabsUnify.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val tabPosition = tab?.position.orZero()
                val tabTitle = tabs.getOrNull(tabPosition)?.text.orEmpty()
                listener.onNavigationBannerTabClick(tabTitle)

                CoroutineScope(Dispatchers.Main).launch {
                    tab?.selectedTextColor(model)

                    if (tabPosition.isMoreThanZero()) {
                        model.lastTabIndexSelected = tabPosition
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                CoroutineScope(Dispatchers.Main).launch {
                    tab?.normalTextColor(model)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun TabLayout.Tab?.selectedTextColor(model: ShowcaseNavigationUiModel) {
        val tabTitle = this?.tabTitleTextView()

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

    private fun TabLayout.Tab?.tabTitleTextView(): TextView? {
        return this?.customView?.findViewById<TextView?>(unifycomponentsR.id.tab_item_text_id)
    }

    private fun TabLayout.Tab?.normalTextColor(model: ShowcaseNavigationUiModel) {
        val tabTitle = this?.tabTitleTextView()

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

    private fun applyTabRuleWidth(tabs: List<ShowcaseTab>, tabsUnify: TabsUnify) {
        tabsUnify.post {
            when {
                tabs.isEmpty() -> tabsUnify.gone()
                tabs.size == ONE_TAB -> tabsUnify.gone()
                else -> {
                    tabsUnify.visible()

                    val screenWidth = getScreenWidth()
                    if (tabTotalWidth < screenWidth && tabs.size <= THREE_TAB) {
                        tabsUnify.customTabMode = TabLayout.MODE_AUTO
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
        fragment: Fragment,
        private val fragments: List<Pair<String, Fragment>>
    ) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = fragments.size
        override fun createFragment(position: Int) = fragments[position].second
    }

    private fun createFragments(
        tabs: List<ShowcaseTab>,
        uiModel: ShowcaseNavigationUiModel
    ): List<Pair<String, Fragment>> {
        val pages = mutableListOf<Pair<String, Fragment>>()
        tabs.forEachIndexed { _, currentTab ->
            val fragment = ShopShowcaseNavigationTabWidgetFragment.newInstance(
                currentTab.text,
                currentTab.showcases,
                uiModel.header.isOverrideTheme,
                uiModel.header.colorSchema
            )
            fragment.setOnShowcaseClick { selectedShowcase, tabName ->
                listener.onNavigationBannerShowcaseClick(
                    selectedShowcase,
                    uiModel,
                    tabs.size,
                    tabName
                )
            }

            fragment.setOnShowcaseVisible { showcaseId, tabName ->
                listener.onNavigationBannerImpression(
                    uiModel = uiModel,
                    tabCount = tabs.size,
                    tabName = tabName,
                    showcaseId = showcaseId
                )
            }
            val displayedTabName = currentTab.text
            pages.add(Pair(displayedTabName, fragment))
        }

        return pages
    }
}
