package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs

import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import com.tokopedia.applink.RouteManager
import com.tokopedia.category.navbottomsheet.view.CategoryNavBottomSheet
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils.Companion.preSelectedTab
import com.tokopedia.discovery2.datamapper.updateComponentsQueryParams
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.customview.CustomViewCreator
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.TabsUnify

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
    private val tabsHolder: TabsUnify = itemView.findViewById(R.id.discovery_tabs_holder)
    private var tabsViewModel: TabsViewModel? = null
    private var selectedTab: TabLayout.Tab? = null
    private var isParentUnifyTab: Boolean = true

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
            tabsViewModel.getUnifyTabLiveData().observe(fragment.viewLifecycleOwner) {
                isParentUnifyTab = false
                tabsHolder.hasRightArrow = tabsViewModel.getArrowVisibilityStatus()
                tabsHolder.tabLayout.removeAllTabs()
                if (tabsViewModel.isFromCategory()) {
                    tabsHolder.customTabMode = TabLayout.MODE_SCROLLABLE
                }
                tabsHolder.getUnifyTabLayout()
                    .setSelectedTabIndicator(
                        tabsHolder.getUnifyTabLayout().tabSelectedIndicator
                    )
                var selectedPosition = 0
                it.forEachIndexed { index, tabItem ->
                    if (tabItem.data?.isNotEmpty() == true) {
                        tabItem.data?.firstOrNull()?.name?.let { tabTitle ->
                            if (tabItem.data?.firstOrNull()?.isSelected == true) {
                                selectedPosition = index
                            }
                            tabsHolder.addNewTab(
                                tabTitle,
                                tabItem.data?.firstOrNull()?.isSelected
                                    ?: false
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

            tabsViewModel.getTabMargin().observe(fragment.viewLifecycleOwner) {
                if (!tabsViewModel.isFromCategory()) {
                    if (it) {
                        val params = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        params.setMargins(0, 0, 0, 16)
                        tabsHolder.layoutParams = params
                    } else {
                        val params = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        params.setMargins(0, 0, 0, 0)
                        tabsHolder.layoutParams = params
                    }
                }
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
        tabsHandler.removeCallbacks(tabsRunnable)
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        tabsViewModel?.getSyncPageLiveData()?.observe(
            fragment.viewLifecycleOwner,
            Observer { needReSync ->
                if (needReSync) {
                    (fragment as DiscoveryFragment).reSync()
                }
            }
        )
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        tabsViewModel?.let { tabsViewModel ->
            selectedTab = tab
            if (tabsViewModel.setSelectedState(tab.position, true)) {
                if (tabsViewModel.isFromCategory()) {
                    tabsViewModel.components.getComponentsItem()?.get(tab.position).apply {
                        (fragment as DiscoveryFragment).getDiscoveryAnalytics()
                            .setOldTabPageIdentifier(this?.data?.firstOrNull()?.id ?: "")
                        fragment.discoveryViewModel.pageIdentifier =
                            this?.data?.firstOrNull()?.id ?: ""
                    }
                }
                tabsViewModel.onTabClick()
                (fragment as DiscoveryFragment).currentTabPosition = tab.position + 1
                trackTabsGTMStatus(tab)
            }
            if (tab.customView != null && tab.customView is CustomViewCreator) {
                setSelectedTabItem(tabsViewModel, tab, true)
            }
        }
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
        if (tabsViewModel.components.name == ComponentsList.Tabs.componentName) {
            ((tab.customView as CustomViewCreator).viewModel as TabsItemViewModel).setSelectionTabItem(
                isCurrentTabSelected
            )
        } else if (tabsViewModel.components.name == ComponentsList.TabsIcon.componentName) {
            ((tab.customView as CustomViewCreator).viewModel as TabsItemIconViewModel).setSelectionTabItem(
                isCurrentTabSelected
            )
        }
    }

    override fun onTabReselected(tab: TabLayout.Tab) {
        selectedTab = tab
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
