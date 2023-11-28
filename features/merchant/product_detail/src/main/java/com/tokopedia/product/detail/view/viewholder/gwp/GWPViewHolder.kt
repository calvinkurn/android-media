package com.tokopedia.product.detail.view.viewholder.gwp

import android.view.View
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.ItemDynamicProductBmgmBinding
import com.tokopedia.product.detail.databinding.ItemDynamicProductGwpBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.viewholder.ProductDetailPageViewHolder

/**
 * Created by yovi.putra on 27/07/23"
 * Project name: android-tokopedia-core
 **/

class GWPViewHolder(
    private val view: View,
    private val listener: DynamicProductDetailListener
) : ProductDetailPageViewHolder<GWPUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_product_gwp
    }

    private val binding by lazyThreadSafetyNone {
        ItemDynamicProductGwpBinding.bind(view)
    }

    override fun bind(element: GWPUiModel) {
        /*binding.pdpBmgmWidget.setData(
            uiState = element.state,
            router = object : BMGMWidgetRouter {
                override fun goToAppLink(url: String) {
                    listener.goToApplink(url)
                }

                override fun goToWebView(url: String) {
                    listener.goToWebView(url)
                }
            },
            tracker = object : BMGMWidgetTracker {
                override fun getImpressionHolder(): ImpressHolder = element.impressHolder

                override fun getImpressionHolders() = listener.getImpressionHolders()

                override fun onImpressed() {
                    listener.onImpressComponent(getComponentTrackData(element))
                }

                override fun onClick(data: BMGMWidgetUiModel) {
                    listener.onBMGMClicked(
                        title = data.title,
                        offerId = data.offerId,
                        component = getComponentTrackData(element)
                    )
                }

                override fun isCacheable() = listener.isRemoteCacheableActive()
            }
        )*/
    }
}
