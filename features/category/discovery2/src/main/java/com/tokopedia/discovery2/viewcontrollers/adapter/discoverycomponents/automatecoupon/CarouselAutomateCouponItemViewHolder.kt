package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.automatecoupon

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.analytics.CouponTrackingMapper.toTrackingProperties
import com.tokopedia.discovery2.data.automatecoupon.AutomateCouponCtaState
import com.tokopedia.discovery2.data.automatecoupon.AutomateCouponUiModel
import com.tokopedia.discovery2.data.automatecoupon.ClaimFailure
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.discovery_component.widgets.automatecoupon.AutomateCouponListView
import com.tokopedia.discovery_component.widgets.automatecoupon.ButtonState
import com.tokopedia.unifycomponents.Toaster

class CarouselAutomateCouponItemViewHolder(
    itemView: View,
    val fragment: Fragment
) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private val couponView = itemView.findViewById<AutomateCouponListView>(R.id.couponView)

    private var viewModel: ListAutomateCouponItemViewModel? = null
    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = discoveryBaseViewModel as? ListAutomateCouponItemViewModel

        viewModel?.let { getSubComponent().inject(it) }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)

        lifecycleOwner?.let { lifeCycle ->
            viewModel?.getCouponModel()?.observe(lifeCycle) {
                renderCoupon(it)
            }

            viewModel?.getCTAState()?.observe(lifeCycle) { ctaState ->
                couponView.setState(mapToCTAHandler(ctaState))
            }

            viewModel?.shouldShowErrorClaimCouponToaster()?.observe(lifeCycle) { reason ->
                showErrorMessage(reason)
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            viewModel?.getCouponModel()?.removeObservers(it)
            viewModel?.shouldShowErrorClaimCouponToaster()?.removeObservers(it)
            viewModel?.getCTAState()?.removeObservers(it)
        }
    }

    private fun renderCoupon(model: AutomateCouponUiModel) {
        val handler = mapToCTAHandler(model.ctaState)

        couponView.apply {
            setModel(model.data)
            setState(handler)
            setClickAction(model.redirectAppLink)
        }
    }

    private fun mapToCTAHandler(ctaState: AutomateCouponCtaState): ButtonState {
        val handler = CtaActionHandler(
            ctaState,
            object : CtaActionHandler.Listener {

                override fun claim() {
                    val ctaText = (ctaState as? AutomateCouponCtaState.Claim)
                        ?.properties?.text.orEmpty()

                    trackClickCTAEvent(ctaText)

                    val catalogId = (ctaState as? AutomateCouponCtaState.Claim)?.catalogId
                    viewModel?.claim(catalogId)
                }

                override fun redirect(properties: AutomateCouponCtaState.Properties) {
                    trackClickCTAEvent(properties.text)

                    val target = properties.appLink.ifEmpty { properties.url }
                    val intent = RouteManager.getIntent(itemView.context, target)

                    itemView.context.startActivity(intent)
                }
            }
        )
        return handler
    }

    private fun AutomateCouponListView.setClickAction(redirectAppLink: String) {
        if (redirectAppLink.isEmpty()) return

        onClick {
            trackClickEvent()
            RouteManager.route(itemView.context, redirectAppLink)
        }
    }

    private fun showErrorMessage(reason: ClaimFailure) {
        fragment.activity?.let { activity ->
            SnackbarManager.getContentView(activity)
        }?.let { contentView ->
            when (reason) {
                ClaimFailure.Unauthorized -> {
                    showUnauthorizedToaster(contentView)
                }

                is ClaimFailure.Ineligible -> {
                    showIneligibleToaster(contentView, reason)
                }
            }
        }
    }

    private fun showIneligibleToaster(
        contentView: View,
        reason: ClaimFailure.Ineligible
    ) {
        Toaster.build(
            contentView,
            reason.message.ifEmpty { contentView.context.getString(R.string.discovery_default_claim_message_toaster) },
            Toast.LENGTH_SHORT,
            Toaster.TYPE_ERROR
        ).show()
    }

    private fun showUnauthorizedToaster(contentView: View) {
        Toaster.build(
            contentView,
            contentView.context.getString(R.string.discovery_unauthorized_claim_message_toaster),
            Toast.LENGTH_SHORT,
            Toaster.TYPE_NORMAL,
            contentView.context.getString(R.string.discovery_unauthorized_claim_cta_toaster)
        ).show()
    }

    private fun trackClickEvent() {
        viewModel?.component?.let { component ->

            val analytics = (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()
            val properties = component.toTrackingProperties()

            analytics?.trackCouponClickEvent(properties)
        }
    }

    private fun trackClickCTAEvent(ctaText: String) {
        viewModel?.component?.let { component ->

            val analytics = (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()
            val properties = component.toTrackingProperties(ctaText)

            analytics?.trackCouponCTAClickEvent(properties)
        }
    }
}
