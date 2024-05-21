package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs

import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.tabs.TabLayout
import com.tokopedia.applink.RouteManager
import com.tokopedia.category.navbottomsheet.view.CategoryNavBottomSheet
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.Utils.Companion.preSelectedTab
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.datamapper.updateComponentsQueryParams
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.customview.CustomViewCreator
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.TabsUnify
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

private const val TAB_START_PADDING = 20
private const val DELAY_400: Long = 400
private const val SHOULD_HIDE_L1 = false
private const val SOURCE = "best-seller"
const val CLICK_UNIFY_TAB = "click section tab"
const val CLICK_MEGA_TAB = "click mega tab"

class TabsViewHolder(itemView: View, private val fragment: Fragment) :
    AbstractViewHolder(itemView, fragment.viewLifecycleOwner),
    TabLayout.OnTabSelectedListener,
    CategoryNavBottomSheet.CategorySelected,
    CategoryNavBottomSheet.GtmProviderListener {
    companion object{
        // WORKAROUND, global variable to access tabName and tabIndex, for tracking purpose.
        var CURRENT_TAB_NAME = ""
        var CURRENT_TAB_INDEX = 0
    }
    private val tabsHolder: TabsUnify = itemView.findViewById(R.id.discovery_tabs_holder)
    private var tabsViewModel: TabsViewModel? = null
    private var selectedTab: TabLayout.Tab? = null
    private var isParentUnifyTab: Boolean = true
    private val scrollToCurrentTabPositionHandler = Handler(Looper.getMainLooper())
    private var scrollToCurrentTabPositionRunnable: Runnable? = null

    private val mFragment: DiscoveryFragment
        by lazy { fragment as DiscoveryFragment }

    private val tabsHandler = Handler(Looper.getMainLooper())
    private val tabsRunnable = Runnable {
        tabsHolder.show()
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        tabsViewModel = discoveryBaseViewModel as TabsViewModel
        tabsViewModel?.let {
            getSubComponent().inject(it)
        }
    }

    override fun onViewAttachedToWindow() {
        super.onViewAttachedToWindow()
        tabsHolder.tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        tabsHolder.tabLayout.addOnTabSelectedListener(this)
        tabsHolder.arrowView.setOnClickListener {
            openCategoryBottomSheet()
        }
        tabsViewModel?.let { tabsViewModel ->
            tabsViewModel.getColorTabComponentLiveData().observe(
                fragment.viewLifecycleOwner
            ) {
                isParentUnifyTab = true
                tabsHolder.tabLayout.apply {
                    layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                    layoutParams.height =
                        tabsHolder.context.resources.getDimensionPixelSize(R.dimen.dp_55)
                    tabMode = TabLayout.MODE_SCROLLABLE
                    removeAllTabs()
                    setBackgroundResource(0)
                }
                tabsHolder.apply {
                    whiteShadeLeft.setBackgroundResource(0)
                    whiteShadeRight.setBackgroundResource(0)
                    getUnifyTabLayout().setSelectedTabIndicator(null)
                }
                it.forEach {
                    val tab = tabsHolder.tabLayout.newTab()
                    ViewCompat.setPaddingRelative(tab.view, TAB_START_PADDING, 0, 0, 0)
                    tab.customView = CustomViewCreator.getCustomViewObject(
                        itemView.context,
                        ComponentsList.TabsItem,
                        it,
                        fragment
                    )
                    tabsHolder.tabLayout.addTab(tab, it.data?.get(0)?.isSelected ?: false)
                }
            }

            tabsViewModel.getIconTabLiveData().observe(
                fragment.viewLifecycleOwner
            ) {
                isParentUnifyTab = false
                tabsHolder.getUnifyTabLayout()
                    .setSelectedTabIndicator(tabsHolder.getUnifyTabLayout().tabSelectedIndicator)
                tabsHolder.getUnifyTabLayout().apply {
                    layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                    layoutParams.height =
                        tabsHolder.context.resources.getDimensionPixelSize(R.dimen.dp_60)
                    tabMode = TabLayout.MODE_SCROLLABLE
                    removeAllTabs()
                }
                var selectedPosition = 0
                it.forEachIndexed { index, tabItem ->
                    if (tabItem.data?.isNotEmpty() == true) {
                        tabItem.data?.firstOrNull()?.name?.let { tabTitle ->
                            if (tabItem.data?.firstOrNull()?.isSelected == true) {
                                selectedPosition = index
                            }
                            val tab = tabsHolder.tabLayout.newTab()
                            ViewCompat.setPaddingRelative(tab.view, TAB_START_PADDING, 0, 0, 0)
                            tab.customView = CustomViewCreator.getCustomViewObject(
                                itemView.context,
                                ComponentsList.TabsIconItem,
                                tabItem,
                                fragment
                            )
                            tabsHolder.tabLayout.addTab(
                                tab,
                                tabItem.data?.firstOrNull()?.isSelected ?: false
                            )
                        }
                    }
                }

                tabsHolder.viewTreeObserver
                    .addOnGlobalLayoutListener {
                        fragment.activity?.let { _ ->
                            if (selectedPosition >= 0 && tabsViewModel.isFromCategory()) {
                                tabsHolder.gone()
                                tabsHolder.tabLayout.getTabAt(selectedPosition)?.select()
                                tabsHandler.postDelayed(tabsRunnable, DELAY_400)
                                selectedPosition = -1
                            }
                        }
                    }
            }

            tabsViewModel.getUnifyTabLiveData().observe(
                fragment.viewLifecycleOwner
            ) {
                isParentUnifyTab = false

                tabsHolder.apply {
                    hasRightArrow = tabsViewModel.getArrowVisibilityStatus()
                    tabLayout.removeAllTabs()
                    if (tabsViewModel.isFromCategory()) {
                        customTabMode = TabLayout.MODE_SCROLLABLE
                    }
                }

                tabsHolder.getUnifyTabLayout().apply {
                    layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                    layoutParams.height =
                        tabsHolder.context.resources.getDimensionPixelSize(R.dimen.dp_60)
                    removeAllTabs()
                }

                setTabsHolderShade()

                var selectedPosition = 0
                it.forEachIndexed { index, tabItem ->
                    if (tabItem.data?.isNotEmpty() == true) {
                        val isSelected = tabItem.data?.firstOrNull()?.isSelected ?: false
                        if (isSelected) {
                            selectedPosition = index
                        }

                        val tab = tabsHolder.tabLayout.newTab()
                        ViewCompat.setPaddingRelative(tab.view, TAB_START_PADDING, 0, 0, 0)
                        val customViewObject = CustomViewCreator.getCustomViewObject(
                            itemView.context,
                            ComponentsList.PlainTabItem,
                            tabItem,
                            fragment
                        )

                        tab.customView = customViewObject

                        tabsHolder.tabLayout.addTab(tab, isSelected)

                        if (!isSelected) {
                            val selectedTabItem = it[selectedPosition]
                            selectedTabItem.setUnselectedTabColor(customViewObject)
                        }
                    }
                }

                scrollToCurrentTabPosition(
                    selectedPosition = selectedPosition,
                    isFromCategory = tabsViewModel.isFromCategory()
                )

                tabsHolder.viewTreeObserver
                    .addOnGlobalLayoutListener {
                        fragment.activity?.let { _ ->
                            if (selectedPosition >= 0 && tabsViewModel.isFromCategory()) {
                                tabsHolder.gone()
                                tabsHolder.tabLayout.getTabAt(selectedPosition)?.select()
                                tabsHandler.postDelayed(tabsRunnable, DELAY_400)
                                selectedPosition = -1
                            }
                        }
                    }
            }

            tabsViewModel.getTabMargin().observe(fragment.viewLifecycleOwner) {
                if (!tabsViewModel.isFromCategory()) {
                    if (it) {
                        tabsHolder.setMargin(0, 0, 0, 16)
                    }
                }
            }
        }
    }

    private fun ComponentsItem.setUnselectedTabColor(customViewObject: CustomViewCreator) {
        val inactiveColor = data?.firstOrNull()?.inactiveFontColor

        (customViewObject.viewModel as? PlainTabItemViewModel)
            ?.setUnselectedTabColor(inactiveColor)
    }

    private fun setTabsHolderShade() {
        tabsViewModel?.let {
            if (it.isBackgroundAvailable()) {
                tabsHolder.apply {
                    removeView(whiteShadeLeft)
                    removeView(whiteShadeRight)
                }
            }
        }
    }

    private fun scrollToCurrentTabPosition(
        selectedPosition: Int,
        isFromCategory: Boolean
    ) {
        if (!isFromCategory) {
            scrollToCurrentTabPositionRunnable = Runnable {
                tabsHolder.tabLayout.getTabAt(selectedPosition)?.select()
                tabsViewModel?.switchThematicHeaderData(selectedPosition.inc())
            }
            scrollToCurrentTabPositionRunnable?.apply {
                scrollToCurrentTabPositionHandler.removeCallbacks(this)
                scrollToCurrentTabPositionHandler.postDelayed(
                    this,
                    DELAY_400
                )
            }
        }
    }

    private fun openCategoryBottomSheet() {
        tabsViewModel?.let { tabsViewModel ->
            (fragment as DiscoveryFragment).getDiscoveryAnalytics()
                .trackCategoryTreeDropDownClick(
                    tabsViewModel.isUserLoggedIn()
                )
            selectedTab?.position?.let { it ->
                if (tabsViewModel.isFromCategory()) {
                    CategoryNavBottomSheet.getInstance(
                        tabsViewModel.components.data?.get(it)?.id
                            ?: tabsViewModel.components.pageEndPoint,
                        this,
                        this,
                        true
                    ).show(fragment.childFragmentManager, "")
                } else {
                    CategoryNavBottomSheet.getInstance(
                        tabsViewModel
                            .getTabItemData(it)?.filterValue ?: "",
                        this,
                        this,
                        SHOULD_HIDE_L1,
                        source = SOURCE
                    )
                        .show(fragment.childFragmentManager, "")
                }
            }
        }
    }

    override fun onViewDetachedToWindow() {
        super.onViewDetachedToWindow()
        tabsHolder.tabLayout.removeOnTabSelectedListener(this)
        tabsViewModel?.getColorTabComponentLiveData()?.removeObservers(fragment.viewLifecycleOwner)
        tabsViewModel?.redirectToOther()?.removeObservers(fragment.viewLifecycleOwner)
        tabsHandler.removeCallbacks(tabsRunnable)
        scrollToCurrentTabPositionRunnable?.apply {
            scrollToCurrentTabPositionHandler.removeCallbacks(this)
            scrollToCurrentTabPositionRunnable = null
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        tabsViewModel?.getSyncPageLiveData()?.observe(
            fragment.viewLifecycleOwner
        ) { needReSync ->
            if (needReSync) {
                (fragment as DiscoveryFragment).reSync()
            }
        }

        lifecycleOwner?.run {
            tabsViewModel?.redirectToOther()?.observe(this) {
                selectOtherTab(it)
            }
        }
    }

    private fun selectOtherTab(position: Int) {
        val tab = tabsHolder.tabLayout.getTabAt(position)
        tabsHolder.tabLayout.selectTab(tab)
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        tabsViewModel?.let { tabsViewModel ->
            selectedTab = tab
            CURRENT_TAB_NAME = tabsViewModel.components.data?.get(tab.position)?.name ?: ""
            CURRENT_TAB_INDEX = tab.position
            if (tabsViewModel.setSelectedState(tab.position, true)) {
                if (tabsViewModel.isFromCategory()) {
                    tabsViewModel.components.getComponentsItem()?.get(tab.position).apply {
                        mFragment.getDiscoveryAnalytics().setOldTabPageIdentifier(this?.data?.firstOrNull()?.id ?: "")
                        mFragment.discoveryViewModel.pageIdentifier = this?.data?.firstOrNull()?.id ?: ""
                    }
                }
                tabsViewModel.onTabClick()
                val tabPosition = tab.position.inc()
                mFragment.setCurrentTabPosition(tabPosition)
                trackTabsGTMStatus(tab)
                tabsViewModel.switchThematicHeaderData(tabPosition)
            }
            if (tab.customView != null && tab.customView is CustomViewCreator) {
                setSelectedTabItem(tabsViewModel, tab, true)
            }

            tabsViewModel.components.data?.apply {
                setSelectedTabBackground(tab.position)
                setSelectedTabIndicator(tab.position)
            }

            tabsViewModel.components.getComponentsItem()?.let {
                resetUnselectedTabColor(tab.position, it.map { component -> component.id })
            }
        }
    }

    private fun resetUnselectedTabColor(
        selectedTabPosition: Int,
        tabsComponentId: List<String>
    ) {
        val selectedComponent = (getTabView(selectedTabPosition)?.viewModel as? PlainTabItemViewModel)
            ?.components

        selectedComponent?.let {
            tabsComponentId.forEachIndexed { index, id ->
                if (id != it.id) {
                    val customViewCreator = getTabView(index)
                    (customViewCreator?.viewModel as? PlainTabItemViewModel)
                        ?.setUnselectedTabColor(selectedComponent.data?.firstOrNull()?.inactiveFontColor)
                }
            }
        }
    }

    private fun getTabView(index: Int): CustomViewCreator? {
        return tabsHolder.tabLayout.getTabAt(index)?.customView as? CustomViewCreator
    }

    private fun List<DataItem>?.setSelectedTabBackground(selectedPosition: Int) {
        val hexColor = this?.get(selectedPosition)?.boxColor

        if (hexColor.isNullOrEmpty()) {
            tabsHolder.tabLayout.setBackgroundColor(
                ContextCompat.getColor(itemView.context, unifyprinciplesR.color.Unify_NN0)
            )
        } else {
            renderTabBackground(hexColor)
        }
    }

    private fun List<DataItem>?.setSelectedTabIndicator(selectedPosition: Int) {
        val hexColor = this?.get(selectedPosition)?.fontColor
        val validHexCode = Utils.getValidHexCode(itemView.context, hexColor)

        val validColor = if (hexColor.isNullOrEmpty()) {
            ContextCompat.getColor(itemView.context, unifyprinciplesR.color.Unify_GN500)
        } else {
            Color.parseColor(validHexCode)
        }

        tabsHolder.setIndicator(
            TabsUnify.NestTabIndicatorType.INDICATOR_SMALL,
            validColor
        )
    }

    private fun renderTabBackground(hexColor: String?) {
        if (hexColor.isNullOrEmpty()) return

        val validHexCode = Utils.getValidHexCode(itemView.context, hexColor)
        tabsHolder.tabLayout.setBackgroundColor(Color.parseColor(validHexCode))
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {
        tabsViewModel?.let { tabsViewModel ->
            tabsViewModel.setSelectedState(tab.position, false)
            if (tab.customView == null || !(tab.customView is CustomViewCreator)) return
            setSelectedTabItem(tabsViewModel, tab, false)
        }
        tabsViewModel?.shouldAddSpace(false)
    }

    private fun setSelectedTabItem(
        tabsViewModel: TabsViewModel,
        tab: TabLayout.Tab,
        isCurrentTabSelected: Boolean
    ) {
        when (tabsViewModel.components.name) {
            ComponentsList.Tabs.componentName -> {
                ((tab.customView as CustomViewCreator).viewModel as TabsItemViewModel)
                    .setSelectionTabItem(isCurrentTabSelected)
            }
            ComponentsList.TabsIcon.componentName -> {
                ((tab.customView as CustomViewCreator).viewModel as TabsItemIconViewModel)
                    .setSelectionTabItem(isCurrentTabSelected)
            }
            ComponentsList.PlainTab.componentName -> {
                ((tab.customView as CustomViewCreator).viewModel as PlainTabItemViewModel)
                    .setSelectionTabItem(isCurrentTabSelected)
            }
        }
    }

    override fun onTabReselected(tab: TabLayout.Tab) {
        selectedTab = tab
    }

    private fun TabsViewModel.switchThematicHeaderData(
        tabPosition: Int
    ) {
        if (components.name == ComponentNames.PlainTab.componentName) {
            mFragment.setTabPosition(tabPosition)
        }
    }

    private fun trackTabsGTMStatus(tab: TabLayout.Tab) {
        if (preSelectedTab != tab.position) {
            preSelectedTab = tab.position
            sendTabTrackingData(tab)
        }
    }

    private fun sendTabTrackingData(tab: TabLayout.Tab) {
        tabsViewModel?.let { tabsViewModel ->
            tabsViewModel.components.data?.let { it ->
                if (it.size >= tab.position) {
                    (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()
                        ?.let { discoAnalytics ->
                            if (isParentUnifyTab) {
                                discoAnalytics.trackTabsClick(
                                    tabsViewModel.components.id,
                                    tabsViewModel.position,
                                    it[tab.position],
                                    tab.position,
                                    CLICK_MEGA_TAB
                                )
                            } else {
                                discoAnalytics.trackUnifyTabsClick(
                                    tabsViewModel.components.id,
                                    tabsViewModel.position,
                                    it[tab.position],
                                    tab.position,
                                    CLICK_UNIFY_TAB
                                )
                            }
                        }
                }
            }
        }
    }

    override fun onCategorySelected(catId: String, appLink: String?, depth: Int, catName: String) {
        tabsViewModel?.let {
            (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackCategoryOptionClick(
                it.isUserLoggedIn(),
                catId,
                appLink,
                depth,
                catName
            )
        }
        updateComponentsQueryParams(catId)
        tabsViewModel?.reInitTabTargetComponents()
        tabsViewModel?.reInitTabComponentData()
        tabsViewModel?.fetchDynamicTabData()
        (fragment.activity)?.let {
            if (tabsViewModel?.isFromCategory() == true) {
                RouteManager.route(itemView.context, appLink)
                it.finish()
            }
        }
    }

    override fun onL2Expanded(id: String?, name: String?) {
        (fragment as DiscoveryFragment).getDiscoveryAnalytics()
            .trackClickExpandNavigationAccordion(
                id
            )
    }

    override fun onL2Collapsed(id: String?, name: String?) {
        (fragment as DiscoveryFragment).getDiscoveryAnalytics()
            .trackClickCollapseNavigationAccordion(
                id
            )
    }

    override fun onL3Clicked(id: String?, name: String?) {
        (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackClickCategoryOption(id)
    }

    override fun onL2Clicked(id: String?, name: String?) {
        (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackClickCategoryOption(id)
    }

    override fun onBottomSheetClosed() {
        tabsViewModel?.isUserLoggedIn()?.let {
            (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackCategoryTreeCloseClick(it)
        }
    }
}
