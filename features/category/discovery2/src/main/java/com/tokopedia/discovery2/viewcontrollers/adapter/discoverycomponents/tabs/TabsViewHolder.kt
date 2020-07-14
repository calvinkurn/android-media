package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.customview.CustomViewCreator
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.unifycomponents.TabsUnify

private const val DEFAULT_TAB_POSITION = 0

class TabsViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private val tabsHolder: TabsUnify = itemView.findViewById(R.id.discovery_tabs_holder)
    private lateinit var tabsViewModel: TabsViewModel
    private var firstSelectedTab = true

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        tabsViewModel = discoveryBaseViewModel as TabsViewModel
        tabsHolder.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                trackTabsGTMStatus(tab)
                if (tab.customView != null && tab.customView is CustomViewCreator) {
                    ((tab.customView as CustomViewCreator).viewModel as TabsItemViewModel).setSelectionTabItem(true)
                }
                tabsViewModel.setSelectedState(tab.position, true)
                tabsViewModel.onTabClick()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tabsViewModel.setSelectedState(tab.position, false)
                if (tab.customView == null || !(tab.customView is CustomViewCreator)) return
                ((tab.customView as CustomViewCreator).viewModel as TabsItemViewModel).setSelectionTabItem(false)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
    }

    private fun trackTabsGTMStatus(tab: TabLayout.Tab) {
        if (firstSelectedTab && tab.position == DEFAULT_TAB_POSITION) {
            firstSelectedTab = false
            sendTabTrackingData(tab)
        } else if (tab.position > DEFAULT_TAB_POSITION) {
            firstSelectedTab = true
            sendTabTrackingData(tab)
        }
    }

    private fun sendTabTrackingData(tab: TabLayout.Tab) {
        tabsViewModel.components.data?.let {
            if (it.size >= tab.position)
                (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackTabsClick(tabsViewModel.components.id, tabsViewModel.components.position,
                        it[tab.position], tab.position)
        }
    }


    override fun onViewAttachedToWindow() {
        super.onViewAttachedToWindow()
        tabsViewModel.getUnifyTabLiveData().observe(fragment.viewLifecycleOwner, Observer {
            tabsHolder.tabLayout.removeAllTabs()
            tabsHolder.getUnifyTabLayout().setSelectedTabIndicator(tabsHolder.getUnifyTabLayout().tabSelectedIndicator)
            it.forEachIndexed { index, tabItem ->
                if (tabItem.data?.isNotEmpty() == true) {
                    tabItem.data?.get(0)?.name?.let { tabTitle ->
                        tabsHolder.addNewTab(tabTitle)
                        if (tabItem.data?.get(0)?.isSelected == true)
                            tabsHolder.getUnifyTabLayout().getTabAt(index)?.select()
                    }
                }
            }
        })
        tabsViewModel.getColorTabComponentLiveData().observe(fragment.viewLifecycleOwner, Observer {
            tabsHolder.tabLayout.removeAllTabs()
            tabsHolder.getUnifyTabLayout().setSelectedTabIndicator(null)
            it.forEach {
                var tab = tabsHolder.tabLayout.newTab()
                tab.customView = CustomViewCreator.getCustomViewObject(itemView.context, ComponentsList.TabsItem, it, fragment)
                tabsHolder.tabLayout.addTab(tab, it.data?.get(0)?.isSelected ?: false)
            }
        })
    }

    override fun onViewDetachedToWindow() {
        super.onViewDetachedToWindow()
        tabsViewModel.getColorTabComponentLiveData().removeObservers(fragment.viewLifecycleOwner)
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        tabsViewModel.getSyncPageLiveData().observe(fragment.viewLifecycleOwner, Observer { needResync ->
            if (needResync) {
                (fragment as DiscoveryFragment).reSync()
            }
        })
    }
}