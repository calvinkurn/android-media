package com.tokopedia.search.result.product.coupon

import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery_component.widgets.automatecoupon.AutomateCouponGridView
import com.tokopedia.discovery_component.widgets.automatecoupon.AutomateCouponModel
import com.tokopedia.discovery_component.widgets.automatecoupon.ButtonState
import com.tokopedia.discovery_component.widgets.utils.HexColorParser
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultProductCouponLayoutBinding
import com.tokopedia.search.result.domain.model.SearchCouponModel
import com.tokopedia.search.result.presentation.model.CouponDataView
import com.tokopedia.search.result.product.inspirationcarousel.TYPE_INSPIRATION_CAROUSEL_COUPON_IMAGE
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.utils.view.binding.viewBinding

internal class CouponGridViewHolder(
    itemView: View,
    private val couponListener: CouponListener
) : AbstractViewHolder<CouponDataView>(itemView) {
    private var binding: SearchResultProductCouponLayoutBinding? by viewBinding()
    override fun bind(element: CouponDataView?) {
        if (element == null) return
        binding?.run {
            renderCouponLayout(element)

            if (element.shouldShowLoading()) {
                coupon1.visibility = View.INVISIBLE
                coupon2.visibility = View.INVISIBLE
                coupon1Loader.visibility = View.VISIBLE
                coupon2Loader.visibility = View.VISIBLE
                return
            }
            
            //Handle impress
            cvMain.addOnImpressionListener(element) {
                couponListener.onCouponImpressed(element)
            }

            couponListener.onCouponImpressed(element)
            coupon1Loader.visibility = View.GONE
            coupon2Loader.visibility = View.GONE
            coupon1.renderCoupon(element, element.couponModel1, element.couponWidgetData1)
            coupon2.renderCoupon(element, element.couponModel2, element.couponWidgetData2)
        }
    }

    private fun renderCouponLayout(element: CouponDataView?) {
        binding?.run {
            val item = element?.carouselItem?.options?.get(0) ?: return
            if (element.carouselItem.type == TYPE_INSPIRATION_CAROUSEL_COUPON_IMAGE) {
                renderCouponImageTitle(element)
            } else {
                renderCouponTitle(element)
            }
            tvFooter.text = item.subtitle

            item.bannerImageUrl.let {
                ivBackground.loadImage(it)
            }
            HexColorParser.parse(item.chipImageUrl) {
                tvTitle.setTextColor(it)
                tvFooter.setTextColor(it)
            }
        }
    }

    private fun renderCouponTitle(element: CouponDataView) {
        binding?.run {
            tvTitle.text = element.carouselItem.options.getOrNull(0)?.title ?: ""

            tvTitle.visible()
            ivTitle.gone()
        }
    }

    private fun renderCouponImageTitle(element: CouponDataView) {
        binding?.run {
            element.carouselItem.options.getOrNull(0)?.title?.let {
                ivTitle.loadImage(it)
            }
            tvTitle.gone()
            ivTitle.visible()
        }
    }

    private fun AutomateCouponGridView.renderCoupon(
        element: CouponDataView,
        couponModel: AutomateCouponModel?,
        widget: SearchCouponModel.CouponListWidget?
    ) {
        if (couponModel == null) {
            visibility = View.GONE
            return
        }
        visibility = View.VISIBLE
        setModel(couponModel)
        if (widget == null) return
        when(widget.widgetInfo?.ctaList?.getOrNull(0)?.type){
            CouponDataView.CTA_TYPE_CLAIM -> {
                setState(
                    ButtonState.Claim {
                        couponListener.claimCoupon(element, widget)
                    }
                )
            }
            CouponDataView.CTA_TYPE_REDIRECT -> {
                setState(
                    ButtonState.Redirection {
                        couponListener.claimCoupon(element, widget)
                    }
                )
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.search_result_product_coupon_layout
        private const val LAYOUT_HEIGHT_MINIMUM = 290
    }
}
