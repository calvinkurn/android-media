package com.tokopedia.product.detail.view.viewholder.promo_price.ui

import android.view.View
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.utils.extensions.addOnPdpImpressionListener
import com.tokopedia.product.detail.databinding.ProductPromoPriceViewHolderBinding
import com.tokopedia.product.detail.view.fragment.delegate.BasicComponentEvent
import com.tokopedia.product.detail.view.viewholder.ProductDetailPageViewHolder
import com.tokopedia.product.detail.view.viewholder.promo_price.delegate.ProductPriceCallback
import com.tokopedia.product.detail.view.viewholder.promo_price.event.ProductPriceEvent

class ProductPriceViewHolder(
    view: View,
    private val callback: ProductPriceCallback
) : ProductDetailPageViewHolder<ProductPriceUiModel>(view) {

    private val binding by lazyThreadSafetyNone {
        ProductPromoPriceViewHolderBinding.bind(view)
    }

    companion object {
        val LAYOUT = R.layout.product_promo_price_view_holder
    }

    override fun bind(element: ProductPriceUiModel) {
        binding.pdpPromoPriceComposeView.setContent {
            ProductDetailPriceComponent(
                promoPriceData = element.promoPriceData,
                normalPromoUiModel = element.normalPromoUiModel,
                priceComponentType = element.priceComponentType,
                normalPriceBoUrl = element.normalPriceBoUrl,
                onPromoPriceClicked = {
                    onPromoPriceClicked(element)
                }
            )
        }
        setComponentImpression(element)
    }

    private fun onPromoPriceClicked(element: ProductPriceUiModel) {
        val applink = element.promoPriceData?.applink.orEmpty()
        val subtitle = element.promoPriceData?.promoSubtitle.orEmpty()
        val slashPriceFmt = element.promoPriceData?.slashPriceFmt.orEmpty()
        val bottomSheetParams = element.promoPriceData?.bottomSheetParam.orEmpty()
        val promoPriceFmt = element.promoPriceData?.promoPriceFmt.orEmpty()
        val defaultPriceFmt = element.normalPromoUiModel?.priceFmt.orEmpty()
        val promoIds = element.promoIdsString

        callback.event(
            ProductPriceEvent.OnPromoPriceClicked(
                url = applink,
                subtitle = subtitle,
                defaultPriceFmt = defaultPriceFmt,
                promoPriceFmt = promoPriceFmt,
                slashPriceFmt = slashPriceFmt,
                promoId = promoIds,
                bottomSheetParams = bottomSheetParams,
                trackerData = getComponentTrackData(element)
            )
        )
    }

    private fun setComponentImpression(element: ProductPriceUiModel) {
        binding.root.addOnPdpImpressionListener(
            holders = callback.impressionHolders,
            name = element.impressionKey()
        ) {
            val trackerData = getComponentTrackData(element = element)
            callback.event(BasicComponentEvent.OnImpressComponent(trackData = trackerData))
        }
    }
}
