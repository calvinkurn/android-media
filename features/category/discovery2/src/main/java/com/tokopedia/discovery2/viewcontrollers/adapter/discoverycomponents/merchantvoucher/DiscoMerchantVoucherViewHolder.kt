package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvoucher

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.analytics.merchantvoucher.MerchantVoucherTrackingMapper.componentToMvcTrackingProperties
import com.tokopedia.discovery2.analytics.merchantvoucher.MvcTrackingProperties
import com.tokopedia.discovery2.data.automatecoupon.AutomateCouponCtaState
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.automatecoupon.CtaActionHandler
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchercarousel.MerchantVoucherCarouselModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.discovery_component.widgets.automatecoupon.AutomateCouponListView
import com.tokopedia.discovery_component.widgets.automatecoupon.ButtonState
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.user.session.UserSession

class DiscoMerchantVoucherViewHolder(itemView: View, val fragment: Fragment) :
    AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private var discoMerchantVoucherViewModel: DiscoMerchantVoucherViewModel? = null
    private val mvcView: AutomateCouponListView = itemView.findViewById(R.id.disco_single_mv)
    private val shimmer: LoaderUnify = itemView.findViewById(R.id.shimmer_view)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        discoMerchantVoucherViewModel = discoveryBaseViewModel as DiscoMerchantVoucherViewModel
        discoMerchantVoucherViewModel?.let {
            getSubComponent().inject(it)
        }
        if (UserSession(fragment.context).isLoggedIn) {
            shimmer.show()
            discoveryBaseViewModel.fetchDataForCoupons()
        } else {
            shimmer.hide()
            mvcView.hide()
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let { owner ->
            discoMerchantVoucherViewModel?.let { discoMerchantVoucherViewModel ->
                discoMerchantVoucherViewModel.coupon.observe(owner) {
                    shimmer.hide()
                    renderCoupon(it)
                }
                discoMerchantVoucherViewModel.loadError.observe(owner) {
                    if (it) {
                        handleErrorState()
                    }
                }
            }
        }
    }

    private fun renderCoupon(model: MerchantVoucherCarouselModel) {
        val handler = mapToCTAHandler(
            AutomateCouponCtaState.Redirect(
                AutomateCouponCtaState.Properties(
                    text = model.buttonText,
                    appLink = model.appLink,
                    url = model.url
                )
            )
        )
        mvcView.apply {
            show()
            setClickAction(model.appLink)
            setModel(model.automateCouponModel)
            setState(handler)
        }
        trackImpression()
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

    private fun trackClickCTAEvent(ctaText: String) {
        discoMerchantVoucherViewModel?.components?.let { component ->
            val analytics = (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()
            val properties = component.getComponentsItem()?.firstOrNull()
                ?.componentToMvcTrackingProperties(
                    ctaText = ctaText,
                    compId = discoMerchantVoucherViewModel?.components?.id.orEmpty(),
                    creativeName = discoMerchantVoucherViewModel?.components?.creativeName.orEmpty()
                )
            properties?.let { analytics?.trackMvcClickEvent(it, true) }
        }
    }

    private fun AutomateCouponListView.setClickAction(redirectAppLink: String) {
        if (redirectAppLink.isEmpty()) return
        onClick {
            trackClickEvent()
            RouteManager.route(itemView.context, redirectAppLink)
        }
    }

    private fun trackClickEvent() {
        discoMerchantVoucherViewModel?.components?.let { component ->
            val analytics = (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()
            val properties =
                component.getComponentsItem()?.firstOrNull()?.componentToMvcTrackingProperties(
                    compId = discoMerchantVoucherViewModel?.components?.id.orEmpty(),
                    creativeName = discoMerchantVoucherViewModel?.components?.creativeName.orEmpty()
                )
            properties?.let { analytics?.trackMvcClickEvent(it, false) }
        }
    }

    private fun trackImpression() {
        val properties = mutableListOf<MvcTrackingProperties>()
        discoMerchantVoucherViewModel?.components?.getComponentsItem()?.firstOrNull()
            ?.componentToMvcTrackingProperties(
                compId = discoMerchantVoucherViewModel?.components?.id.orEmpty(),
                creativeName = discoMerchantVoucherViewModel?.components?.creativeName.orEmpty()
            )
            ?.let { properties.add(it) }
        val analytics = (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()
        if (properties.isNotEmpty()) analytics?.trackMvcImpression(properties)
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            discoMerchantVoucherViewModel?.loadError?.removeObservers(it)
            discoMerchantVoucherViewModel?.coupon?.removeObservers(it)
        }
    }

    private fun handleErrorState() {
        mvcView.hide()
        shimmer.hide()
    }
}
