package com.tokopedia.product.detail.view.viewholder.bmgm

import android.view.View
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.ItemDynamicProductBmgmBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.viewholder.ProductDetailPageViewHolder
import com.tokopedia.product.detail.view.viewholder.bmgm.model.BMGMWidgetUiState
import com.tokopedia.product.detail.view.viewholder.bmgm.widget.BMGMWidgetRouter

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
            }
        )

        if (element.state is BMGMWidgetUiState.Show) {
            binding.root.addOnImpressionListener(element.impressHolder) {
                listener.onImpressComponent(getComponentTrackData(element))
            }
        }
    }
}
