package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.flashsaletoko

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.discovery2.databinding.DiscoveryFlashSaleTokoTabsBinding
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.flashsaletoko.FlashSaleTokoTabMapper.mapToShopTabDataModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.home_component.widget.shop_tab.ShopTabDataModel
import com.tokopedia.home_component.widget.shop_tab.ShopTabListener

class FlashSaleTokoTabViewHolder(
    itemView: View,
    val fragment: Fragment
) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner), ShopTabListener {

    private var viewModel: FlashSaleTokoTabViewModel? = null

    private val binding = DiscoveryFlashSaleTokoTabsBinding.bind(itemView)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = discoveryBaseViewModel as FlashSaleTokoTabViewModel

        viewModel?.let {
            getSubComponent().inject(it)
        }
    }

    override fun onViewAttachedToWindow() {
        super.onViewAttachedToWindow()

        viewModel?.getTabLiveData()?.observe(fragment.viewLifecycleOwner) { components ->
            binding.tab.setShopTabs(components.mapToShopTabDataModel(), this)
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        viewModel?.getSyncPageLiveData()?.observe(fragment.viewLifecycleOwner) { needReSync ->
            if (needReSync) (fragment as? DiscoveryFragment)?.reSync()
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        viewModel?.getTabLiveData()?.removeObservers(fragment.viewLifecycleOwner)
        super.removeObservers(lifecycleOwner)
    }

    override fun onShopTabClick(element: ShopTabDataModel) {
        viewModel?.onTabClick(element.id)

        val position = viewModel?.getTabLiveData()?.value
            ?.indexOfFirst { it.data?.first()?.filterValue == element.id } ?: -1

        (fragment as DiscoveryFragment).currentTabPosition = position + 1
    }
}
