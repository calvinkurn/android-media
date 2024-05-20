package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchergrid

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.analytics.merchantvoucher.MerchantVoucherTrackingMapper.dataToMvcTrackingProperties
import com.tokopedia.discovery2.analytics.merchantvoucher.MvcTrackingProperties
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.automatecoupon.AutomateCouponCtaState
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.automatecoupon.CtaActionHandler
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.discovery_component.widgets.automatecoupon.AutomateCouponGridView
import com.tokopedia.discovery_component.widgets.automatecoupon.ButtonState
import com.tokopedia.kotlin.extensions.view.orZero

class MerchantVoucherGridItemViewHolder(
    itemView: View,
    val fragment: Fragment
) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private val merchantVoucherGrid = itemView.findViewById<AutomateCouponGridView>(R.id.merchant_voucher_grid)

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
                trackImpression()
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let { viewModel?.getComponentData()?.removeObservers(it) }
    }

    private fun renderVoucher(model: MerchantVoucherGridModel) {
        merchantVoucherGrid.run {
            setModel(model.automateCouponModel)

            val handler = mapToCTAHandler(
                AutomateCouponCtaState.Redirect(
                    AutomateCouponCtaState.Properties(
                        text = model.buttonText,
                        appLink = model.appLink,
                        url = model.url
                    )
                )
            )
            setState(handler)
            setClickAction(model.appLink)
        }
    }

    private fun AutomateCouponGridView.setClickAction(redirectAppLink: String) {
        if (redirectAppLink.isEmpty()) return
        onClick {
            trackClickEvent()
            RouteManager.route(itemView.context, redirectAppLink)
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

    private fun trackClickEvent() {
        val trackingProperties = viewModel?.components?.asImpressionClickProperties()

        trackingProperties?.run {
            getAnalytics()?.trackMvcClickEvent(this, false)
        }
    }

    private fun trackClickCTAEvent(ctaText: String) {
        val trackingProperties = viewModel?.components?.asClickCTAProperties(ctaText)

        trackingProperties?.run {
            getAnalytics()?.trackMvcClickEvent(this, true)
        }
    }

    private fun trackImpression() {
        val trackingProperties = viewModel?.components?.asImpressionClickProperties()

        trackingProperties?.run {
            getAnalytics()?.trackMvcImpression(listOf(this))
        }
    }

    private fun ComponentsItem?.asClickCTAProperties(ctaText: String): MvcTrackingProperties? {
        return this?.data?.firstOrNull()
            ?.dataToMvcTrackingProperties(
                ctaText = ctaText,
                creativeName = creativeName.orEmpty(),
                compId = parentComponentId,
                position = position.orZero()
            )
    }

    private fun ComponentsItem?.asImpressionClickProperties(): MvcTrackingProperties? {
        return this?.data?.firstOrNull()
            ?.dataToMvcTrackingProperties(
                creativeName = creativeName.orEmpty(),
                compId = parentComponentId,
                position = position.orZero()
            )
    }

    private fun getAnalytics() = (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()
}
