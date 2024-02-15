package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.automatecoupon

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.databinding.SingleAutomateCouponLayoutBinding
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery_component.widgets.automatecoupon.AutomateCouponModel
import com.tokopedia.discovery_component.widgets.automatecoupon.ButtonState
import com.tokopedia.utils.view.binding.viewBinding

class SingleAutomateCouponViewHolder(
    itemView: View,
    val fragment: Fragment
) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private val binding: SingleAutomateCouponLayoutBinding?
        by viewBinding()

    private var viewModel: SingleAutomateCouponViewModel? = null

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = discoveryBaseViewModel as? SingleAutomateCouponViewModel

        viewModel?.let {
            getSubComponent().inject(it)
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)

        lifecycleOwner?.let {
            viewModel?.getComponentList()?.observe(it) { items ->
                items.firstOrNull()?.automateCoupons?.firstOrNull()?.let { model ->
                    binding?.renderCoupon(model)
                }
            }

            viewModel?.getRedirectLink()?.observe(it) {
                binding?.couponView?.setState(
                    ButtonState.Redirection {
                        RouteManager.route(fragment.requireContext(), it)
                    }
                )
            }
        }
    }

    private fun SingleAutomateCouponLayoutBinding?.renderCoupon(model: AutomateCouponModel) {
        this?.couponView?.apply {
            setModel(model)

            setState(
                ButtonState.Claim {
                    viewModel?.claim()
                }
            )
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        if (lifecycleOwner == null) return

        viewModel?.getComponentList()?.removeObservers(lifecycleOwner)
        super.removeObservers(lifecycleOwner)
    }
}
