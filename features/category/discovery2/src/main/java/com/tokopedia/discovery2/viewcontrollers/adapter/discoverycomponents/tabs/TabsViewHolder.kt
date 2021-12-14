package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs

import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
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
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.customview.CustomViewCreator
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.TabsUnify

private const val TAB_START_PADDING = 20
private const val SHOULD_HIDE_L1 = false
private const val SOURCE = "best-seller"

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
            if((fragment.activity as DiscoveryActivity).isFromCategory())
                tabsHolder.customTabMode = TabLayout.MODE_SCROLLABLE
            tabsHolder.getUnifyTabLayout().setSelectedTabIndicator(tabsHolder.getUnifyTabLayout().tabSelectedIndicator)
            var selectedPosition = 0
            it.forEachIndexed { index, tabItem ->
                if (tabItem.data?.isNotEmpty() == true) {
                    tabItem.data?.firstOrNull()?.name?.let { tabTitle ->
                        if(tabItem.data?.firstOrNull()?.isSelected == true)
                            selectedPosition = index
                        tabsHolder.addNewTab(tabTitle, tabItem.data?.firstOrNull()?.isSelected ?: false)
                    }
                }
            }
            tabsHolder.viewTreeObserver
                .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        if(selectedPosition>=0 && (fragment.activity as DiscoveryActivity).isFromCategory()) {
                            tabsHolder.gone()
                            tabsHolder.tabLayout.getTabAt(selectedPosition)?.select()
                            Handler().postDelayed({
                                tabsHolder.show()
                            },400)
                            selectedPosition = -1
                        }
                    }
                })
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
            if ((fragment.activity as DiscoveryActivity).isFromCategory()) {
                CategoryNavBottomSheet.getInstance(
                    tabsViewModel.components.data?.get(it)?.id ?: tabsViewModel.components.pageEndPoint,
                    this,
                    this,
                    true).show(fragment.childFragmentManager, "")

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
            (fragment.activity as DiscoveryActivity).let {
                if (it.isFromCategory()) {
                    tabsViewModel.components.getComponentsItem()?.get(tab.position).apply {
                        (fragment as DiscoveryFragment).getDiscoveryAnalytics().setOldTabPageIdentifier(this?.data?.firstOrNull()?.id ?: "")
                        it.getViewModel().pageIdentifier =
                            this?.data?.firstOrNull()?.id ?: ""
                    }
                }
            }
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
        (fragment.activity as DiscoveryActivity).let {
            if (it.isFromCategory()) {
                RouteManager.route(itemView.context, appLink)
                it.finish()
            }
        }
    }

    override fun onL2Expanded(id: String?, name: String?) {
        (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackClickExpandNavigationAccordion(id)
    }

    override fun onL2Collapsed(id: String?, name: String?) {
        (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackClickCollapseNavigationAccordion(id)
    }

    override fun onL3Clicked(id: String?, name: String?) {
        (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackClickCategoryOption(id)
    }

    override fun onL2Clicked(id: String?, name: String?) {
        (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackClickCategoryOption(id)
    }

    override fun onBottomSheetClosed() {
        (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackCategoryTreeCloseClick(tabsViewModel.isUserLoggedIn())
    }
}