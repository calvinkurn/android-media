package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.flashsaletoko

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.flashsaletoko.FlashSaleTokoTabMapper.mapToShopTabDataModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs.CLICK_UNIFY_TAB
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.home_component.widget.shop_tab.ShopTabDataModel
import com.tokopedia.home_component.widget.shop_tab.ShopTabListener
import com.tokopedia.home_component.widget.shop_tab.ShopTabView

class FlashSaleTokoTabViewHolder(
    itemView: View,
    val fragment: Fragment
) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner), ShopTabListener {

    private var viewModel: FlashSaleTokoTabViewModel? = null

    private val tab = itemView.findViewById<ShopTabView>(R.id.tab)

    private val discoveryRecycleAdapter: DiscoveryRecycleAdapter by lazy { DiscoveryRecycleAdapter(fragment) }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = discoveryBaseViewModel as FlashSaleTokoTabViewModel

        viewModel?.let {
            getSubComponent().inject(it)
        }
    }

    override fun onViewAttachedToWindow() {
        super.onViewAttachedToWindow()

        viewModel?.getTabLiveData()?.observe(fragment.viewLifecycleOwner) { components ->
            val isFestiveApplied = viewModel?.component?.isBackgroundPresent ?: false
            tab.setShopTabs(components.mapToShopTabDataModel(isFestiveApplied), this)
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        viewModel?.getSyncPageLiveData()?.observe(fragment.viewLifecycleOwner) { needReSync ->
            if (needReSync) (fragment as? DiscoveryFragment)?.reSync()
        }

        viewModel?.notifyTargetInFestiveSection()?.observe(fragment.viewLifecycleOwner) { (sectionId, selectedFilterValue) ->
            discoveryRecycleAdapter.notifyFestiveSectionId(sectionId, selectedFilterValue)
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        viewModel?.getTabLiveData()?.removeObservers(fragment.viewLifecycleOwner)
        viewModel?.notifyTargetInFestiveSection()?.removeObservers(fragment.viewLifecycleOwner)
        super.removeObservers(lifecycleOwner)
    }

    override fun onShopTabClick(element: ShopTabDataModel) {
        viewModel?.onTabClick(element.id)

        val position = updateCurrentTabPosition(element)
        trackTabClick(position)
    }

    override fun onShopTabImpressed(element: ShopTabDataModel) {
    }

    private fun updateCurrentTabPosition(element: ShopTabDataModel): Int {
        val position = viewModel?.getTabLiveData()?.value
            ?.indexOfFirst { it.data?.first()?.filterValue == element.id } ?: -1

        (fragment as? DiscoveryFragment)?.currentTabPosition = position + 1

        return position
    }

    private fun trackTabClick(tabPosition: Int) {
        val analytics = (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics() ?: return

        viewModel?.run {
            val parentPosition = position

            component.data?.let {
                analytics.trackUnifyTabsClick(
                    component.id,
                    parentPosition,
                    it[tabPosition],
                    tabPosition,
                    CLICK_UNIFY_TAB
                )
            }
        }
    }
}
