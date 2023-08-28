package com.tokopedia.product.detail.view.viewholder.bmgm

import android.view.View
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.ItemDynamicProductBmgmBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.viewholder.ProductDetailPageViewHolder
import com.tokopedia.product.detail.view.viewholder.bmgm.model.BMGMWidgetUiModel
import com.tokopedia.product.detail.view.viewholder.bmgm.widget.BMGMWidgetRouter
import com.tokopedia.product.detail.view.viewholder.bmgm.widget.BMGMWidgetTracker

/**
 * Created by yovi.putra on 27/07/23"
 * Project name: android-tokopedia-core
 **/

class BMGMViewHolder(
    private val view: View,
    private val listener: DynamicProductDetailListener
) : ProductDetailPageViewHolder<BMGMDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_product_bmgm
    }

    private val binding by lazyThreadSafetyNone {
        ItemDynamicProductBmgmBinding.bind(view)
    }

    override fun bind(element: BMGMDataModel) {
        binding.pdpBmgmWidget.setData(
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

                override fun onImpressed() {
                    listener.onImpressComponent(getComponentTrackData(element))
                }

                override fun onClick(data: BMGMWidgetUiModel) {
                    listener.onBMGMClicked(
                        title = data.title,
                        component = getComponentTrackData(element)
                    )
                }
            }
        )
    }
}
