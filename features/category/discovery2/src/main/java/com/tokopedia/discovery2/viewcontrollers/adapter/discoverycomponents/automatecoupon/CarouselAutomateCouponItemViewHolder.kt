package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.automatecoupon

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.data.automatecoupon.AutomateCouponCtaState
import com.tokopedia.discovery2.data.automatecoupon.AutomateCouponUiModel
import com.tokopedia.discovery2.databinding.CarouselAutomateCouponItemLayoutBinding
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery_component.widgets.automatecoupon.AutomateCouponModel
import com.tokopedia.discovery_component.widgets.automatecoupon.ButtonState
import com.tokopedia.unifycomponents.Toaster

class CarouselAutomateCouponItemViewHolder(
    itemView: View,
    val fragment: Fragment
) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private val binding = CarouselAutomateCouponItemLayoutBinding.bind(itemView)

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

            viewModel?.shouldShowErrorClaimCouponToaster()?.observe(lifeCycle) { message ->
                if (message.isNotEmpty()) {
                    fragment.activity?.let { activity ->
                        SnackbarManager.getContentView(activity)
                    }?.let { contentView ->
                        Toaster.build(
                            contentView,
                            message,
                            Toast.LENGTH_SHORT,
                            Toaster.TYPE_ERROR
                        ).show()
                    }
                }
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let { viewModel?.getCouponModel()?.removeObservers(it) }
    }

    private fun CarouselAutomateCouponItemLayoutBinding.renderCoupon(model: AutomateCouponUiModel) {
        val handler = CtaActionHandler(model.ctaState, object : CtaActionHandler.Listener {

            override fun claim() {
                viewModel?.claim()
            }

            override fun redirect(properties: AutomateCouponCtaState.Properties) {
                val target = properties.appLink.ifEmpty { properties.url }
                val intent = RouteManager.getIntent(itemView.context, target)

                itemView.context.startActivity(intent)
            }
        })

        couponView.setModel(model.data)
        couponView.setState(handler)
    }
}
