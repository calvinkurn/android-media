package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.automatecoupon

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.data.automatecoupon.AutomateCouponCtaState
import com.tokopedia.discovery2.data.automatecoupon.AutomateCouponUiModel
import com.tokopedia.discovery2.databinding.SingleAutomateCouponLayoutBinding
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery_component.widgets.automatecoupon.AutomateCouponListView
import com.tokopedia.discovery_component.widgets.automatecoupon.ButtonState
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.Toaster
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
                    binding?.couponView?.show()
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

            viewModel?.shouldShowErrorClaimCouponToaster()?.observe(it) { message ->
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

    private fun SingleAutomateCouponLayoutBinding?.renderCoupon(model: AutomateCouponUiModel) {
        val handler = CtaActionHandler(
            model.ctaState,
            object : CtaActionHandler.Listener {

                override fun claim() {
                    viewModel?.claim()
                }

                override fun redirect(properties: AutomateCouponCtaState.Properties) {
                    val target = properties.appLink.ifEmpty { properties.url }
                    val intent = RouteManager.getIntent(itemView.context, target)

                    itemView.context.startActivity(intent)
                }
            }
        )

        this?.couponView?.apply {
            setModel(model.data)
            setState(handler)
            setClickAction(model.redirectAppLink)
        }
    }

    private fun AutomateCouponListView.setClickAction(redirectAppLink: String) {
        if (redirectAppLink.isEmpty()) return

        onClick {
            RouteManager.route(itemView.context, redirectAppLink)
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        if (lifecycleOwner == null) return

        viewModel?.getComponentList()?.removeObservers(lifecycleOwner)
        super.removeObservers(lifecycleOwner)
    }
}
