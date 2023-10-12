package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchergrid

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.databinding.MerchantVoucherGridItemLayoutBinding
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder

class MerchantVoucherGridItemViewHolder(
    itemView: View,
    val fragment: Fragment
): AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private val binding = MerchantVoucherGridItemLayoutBinding.bind(itemView)

    private var viewModel: MerchantVoucherGridItemViewModel? = null

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = discoveryBaseViewModel as? MerchantVoucherGridItemViewModel

        viewModel?.let {
            getSubComponent().inject(it)
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        lifecycleOwner?.let { lifeCycle ->
            viewModel?.getComponentData()?.observe(lifeCycle) {
                renderVoucher(it)
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let { viewModel?.getComponentData()?.removeObservers(it) }
    }

    private fun renderVoucher(item: DataItem) {
        binding.merchantVoucherGrid.run {
            setData(item)
            onClick {
                RouteManager.route(itemView.context, item.shopInfo?.appLink)
            }
        }
    }
}
