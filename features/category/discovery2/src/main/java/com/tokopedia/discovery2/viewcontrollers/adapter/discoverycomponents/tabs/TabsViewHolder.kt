package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs

import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
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
import com.tokopedia.unifycomponents.TabsUnify

private const val TAB_START_PADDING = 20
private const val SHOULD_HIDE_L1 = false

class TabsViewHolder(itemView: View, private val fragment: Fragment) :
        AbstractViewHolder(itemView, fragment.viewLifecycleOwner),
        TabLayout.OnTabSelectedListener, CategoryNavBottomSheet.CategorySelected,
        CategoryNavBottomSheet.GtmProviderListener {
    private val tabsHolder: TabsUnify = itemView.findViewById(R.id.discovery_tabs_holder)
    private lateinit var tabsViewModel: TabsViewModel
    private var selectedTab: TabLayout.Tab? = null

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        tabsViewModel = discoveryBaseViewModel as TabsViewModel
        getSubComponent().inject(tabsViewModel)
    }

    override fun onViewAttachedToWindow() {
        super.onViewAttachedToWindow()
        tabsHolder.tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        tabsHolder.tabLayout.addOnTabSelectedListener(this)
        tabsHolder.arrowView.setOnClickListener {
            openCategoryBottomSheet()
        }
        tabsViewModel.getUnifyTabLiveData().observe(fragment.viewLifecycleOwner, {
            tabsHolder.hasRightArrow = tabsViewModel.getArrowVisibilityStatus()
            tabsHolder.tabLayout.removeAllTabs()
            tabsHolder.getUnifyTabLayout().setSelectedTabIndicator(tabsHolder.getUnifyTabLayout().tabSelectedIndicator)
            it.forEachIndexed { index, tabItem ->
                if (tabItem.data?.isNotEmpty() == true) {
                    tabItem.data?.firstOrNull()?.name?.let { tabTitle ->
                        tabsHolder.addNewTab(tabTitle)
                        if (tabItem.data?.firstOrNull()?.isSelected == true)
                            tabsHolder.getUnifyTabLayout().getTabAt(index)?.select()
                    }
                }
            }
        })

        tabsViewModel.getColorTabComponentLiveData().observe(fragment.viewLifecycleOwner, Observer {
            tabsHolder.tabLayout.apply {
                layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                layoutParams.height = tabsHolder.context.resources.getDimensionPixelSize(R.dimen.dp_56)
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
                tab.customView = CustomViewCreator.getCustomViewObject(itemView.context, ComponentsList.TabsItem, it, fragment)
                tabsHolder.tabLayout.addTab(tab, it.data?.get(0)?.isSelected ?: false)
            }
        })
    }

    private fun openCategoryBottomSheet() {
        (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackCategoryTreeDropDownClick(tabsViewModel.isUserLoggedIn())
        selectedTab?.position?.let { it ->
            CategoryNavBottomSheet.getInstance(tabsViewModel
                    .getTabItemData(it)?.filterValue ?: "",
                    this,
                    this,
                    SHOULD_HIDE_L1)
                    .show(fragment.childFragmentManager, "")
        }
    }

    override fun onViewDetachedToWindow() {
        super.onViewDetachedToWindow()
        tabsHolder.tabLayout.removeOnTabSelectedListener(this)
        tabsViewModel.getColorTabComponentLiveData().removeObservers(fragment.viewLifecycleOwner)
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        tabsViewModel.getSyncPageLiveData().observe(fragment.viewLifecycleOwner, Observer { needReSync ->
            if (needReSync) {
                (fragment as DiscoveryFragment).reSync()
            }
        })
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        selectedTab = tab
        if (tabsViewModel.setSelectedState(tab.position, true)) {
            tabsViewModel.onTabClick()
            trackTabsGTMStatus(tab)
        }
        if (tab.customView != null && tab.customView is CustomViewCreator) {
            ((tab.customView as CustomViewCreator).viewModel as TabsItemViewModel).setSelectionTabItem(true)
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {
        tabsViewModel.setSelectedState(tab.position, false)
        if (tab.customView == null || !(tab.customView is CustomViewCreator)) return
        ((tab.customView as CustomViewCreator).viewModel as TabsItemViewModel).setSelectionTabItem(false)
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
        tabsViewModel.components.data?.let {
            if (it.size >= tab.position)
                (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()
                        ?.trackTabsClick(tabsViewModel.components.id,
                                tabsViewModel.components.position,
                                it[tab.position],
                                tab.position)
        }
    }

    override fun onCategorySelected(catId: String, appLink: String?, depth: Int, catName: String) {
        (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackCategoryOptionClick(
                tabsViewModel.isUserLoggedIn(), catId, appLink, depth, catName)
        updateComponentsQueryParams(catId)
        tabsViewModel.reInitTabTargetComponents()
        tabsViewModel.reInitTabComponentData()
        tabsViewModel.fetchDynamicTabData()
    }

    override fun onBottomSheetClosed() {
        (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackCategoryTreeCloseClick(tabsViewModel.isUserLoggedIn())
    }
}