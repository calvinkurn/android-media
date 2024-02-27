package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchercarousel

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Constant.MultipleShopMVCCarousel.CAROUSEL_ITEM_DESIGN
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.analytics.merchantvoucher.MerchantVoucherTrackingMapper.dataToMvcTrackingProperties
import com.tokopedia.discovery2.analytics.merchantvoucher.MvcTrackingProperties
import com.tokopedia.discovery2.data.automatecoupon.AutomateCouponCtaState
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.automatecoupon.CtaActionHandler
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.discovery_component.widgets.automatecoupon.AutomateCouponListView
import com.tokopedia.discovery_component.widgets.automatecoupon.ButtonState
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show

const val RATIO_FOR_CAROUSEL = 0.85

class MerchantVoucherCarouselItemViewHolder(itemView: View, val fragment: Fragment) :
    AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private val mvcMultiShopView: AutomateCouponListView =
        itemView.findViewById(R.id.mvc_multi_view)
    private val parentView: ConstraintLayout = itemView.findViewById(R.id.multishop_parent_view)
    private var merchantVoucherCarouselItemViewModel: MerchantVoucherCarouselItemViewModel? = null

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        merchantVoucherCarouselItemViewModel =
            discoveryBaseViewModel as MerchantVoucherCarouselItemViewModel
        merchantVoucherCarouselItemViewModel?.let {
            setupView(it.components.design)
            setupMargins(it.components.name)
        }
    }

    private fun setupView(design: String) {
        val params = parentView.layoutParams
        if (design == CAROUSEL_ITEM_DESIGN) {
            val width = Resources.getSystem().displayMetrics.widthPixels
            params.width = (width * RATIO_FOR_CAROUSEL).toInt()
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        } else {
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
        parentView.layoutParams = params
    }

    private fun setupMargins(componentName: String?) {
        if (ComponentNames.MerchantVoucherListItem.componentName == componentName) {
            fragment.context?.let {
                parentView.setMargin(
                    it.resources.getDimensionPixelOffset(R.dimen.dp_12),
                    0,
                    it.resources.getDimensionPixelOffset(R.dimen.dp_12),
                    0
                )
            }
        } else {
            parentView.setMargin(0, 0, 0, 0)
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let { lifecycle ->
            merchantVoucherCarouselItemViewModel?.multiShopModel?.observe(lifecycle) {
                merchantVoucherCarouselItemViewModel?.syncParentPosition()
                renderCoupon(it)
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
        mvcMultiShopView.apply {
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
        merchantVoucherCarouselItemViewModel?.components?.data?.firstOrNull().let { data ->
            val analytics = (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()
            val properties = data?.dataToMvcTrackingProperties(
                ctaText = ctaText,
                creativeName = merchantVoucherCarouselItemViewModel?.components?.creativeName.orEmpty()
            )
            properties?.let { analytics?.trackMvcCtaClickEvent(it) }
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
        merchantVoucherCarouselItemViewModel?.components?.data?.firstOrNull().let { data ->
            val analytics = (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()
            val properties = data?.dataToMvcTrackingProperties(
                creativeName = merchantVoucherCarouselItemViewModel?.components?.creativeName.orEmpty()
            )
            properties?.let { analytics?.trackMvcClickEvent(it) }
        }
    }

    private fun trackImpression() {
        val properties = mutableListOf<MvcTrackingProperties>()
        merchantVoucherCarouselItemViewModel?.components?.data?.firstOrNull()
            ?.dataToMvcTrackingProperties(
                creativeName = merchantVoucherCarouselItemViewModel?.components?.creativeName.orEmpty()
            )
            ?.let { properties.add(it) }
        val analytics = (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()
        if (properties.isNotEmpty()) analytics?.trackMvcImpression(properties)
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            merchantVoucherCarouselItemViewModel?.multiShopModel?.removeObservers(
                it
            )
        }
    }
}
