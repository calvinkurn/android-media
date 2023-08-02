package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.bmgm.ui.BMGMRouter
import com.tokopedia.product.detail.common.bmgm.ui.model.BMGMUiState
import com.tokopedia.product.detail.data.model.datamodel.ProductBMGMDataModel
import com.tokopedia.product.detail.databinding.ItemDynamicProductBmgmBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener

/**
 * Created by yovi.putra on 27/07/23"
 * Project name: android-tokopedia-core
 **/

class ProductBMGMViewHolder(
    private val view: View,
    private val listener: DynamicProductDetailListener
) : ProductDetailPageViewHolder<ProductBMGMDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_product_bmgm
    }

    private val binding by lazyThreadSafetyNone {
        ItemDynamicProductBmgmBinding.bind(view)
    }

    override fun bind(element: ProductBMGMDataModel) {
        binding.pdpBmgmWidget.setData(
            uiState = element.state,
            router = object : BMGMRouter {
                override fun goToAppLink(url: String) {
                    listener.goToApplink(url)
                }

                override fun goToWebView(url: String) {
                    listener.goToWebView(url)
                }
            }
        )

        if (element.state is BMGMUiState.Show) {
            binding.root.addOnImpressionListener(element.impressHolder) {
                listener.onImpressComponent(getComponentTrackData(element))
            }
        }
    }
}
