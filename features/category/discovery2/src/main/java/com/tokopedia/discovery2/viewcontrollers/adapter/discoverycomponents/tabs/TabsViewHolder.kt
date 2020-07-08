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

class TabsViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private val tabsHolder: TabsUnify = itemView.findViewById(R.id.tabs_holder)
    private lateinit var tabsViewModel: TabsViewModel
    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        tabsViewModel = discoveryBaseViewModel as TabsViewModel
        tabsHolder.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if(tab.customView != null && tab.customView is CustomViewCreator) {
                    ((tab.customView as CustomViewCreator).viewModel as TabsItemViewModel).setSelectionTabItem(true)
                }
                tabsViewModel.setSelectedState(tab.position,true)
                tabsViewModel.onTabClick()
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {
                tabsViewModel.setSelectedState(tab.position,false)
                if(tab.customView == null || !(tab.customView is CustomViewCreator)) return
                ((tab.customView as CustomViewCreator).viewModel as TabsItemViewModel).setSelectionTabItem(false)
            }
            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
    }
    override fun onViewAttachedToWindow() {
        super.onViewAttachedToWindow()
        tabsViewModel.getUnifyTabLiveData().observe(fragment.viewLifecycleOwner, Observer {
            tabsHolder.tabLayout.removeAllTabs()
            tabsHolder.getUnifyTabLayout().setSelectedTabIndicator(tabsHolder.getUnifyTabLayout().tabSelectedIndicator)
            it.forEachIndexed { index, it ->
                it?.data?.get(0)?.name?.let {tabTitle->
                    tabsHolder.addNewTab(tabTitle)
                    if(it?.data?.get(0)?.isSelected?:false)
                        tabsHolder.getUnifyTabLayout().getTabAt(index)?.select()
                }
            }
        })
        tabsViewModel.getColorTabComponentLiveData().observe(fragment.viewLifecycleOwner, Observer {
            tabsHolder.tabLayout.removeAllTabs()
            tabsHolder.getUnifyTabLayout().setSelectedTabIndicator(null)
            it.forEach {
                var tab = tabsHolder.tabLayout.newTab()
                tab.customView = CustomViewCreator.getCustomViewObject(itemView.context, ComponentsList.TabsItem, it, fragment)
                tabsHolder.tabLayout.addTab(tab,it.data?.get(0)?.isSelected?:false)
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