package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.automatecoupon

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.discovery2.databinding.GridAutomateCouponItemLayoutBinding
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery_component.widgets.automatecoupon.AutomateCouponModel

class GridAutomateCouponItemViewHolder(
    itemView: View,
    val fragment: Fragment
) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private val binding = GridAutomateCouponItemLayoutBinding.bind(itemView)

    private var viewModel: ListAutomateCouponItemViewModel? = null

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = discoveryBaseViewModel as? ListAutomateCouponItemViewModel

        viewModel?.let { getSubComponent().inject(it) }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)

        lifecycleOwner?.let { lifeCycle ->
            viewModel?.getCouponModel()?.observe(lifeCycle) {
                binding.renderCoupon(it)
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let { viewModel?.getCouponModel()?.removeObservers(it) }
    }

    private fun GridAutomateCouponItemLayoutBinding.renderCoupon(model: AutomateCouponModel) {
        couponGrid.setModel(model)
    }
}
