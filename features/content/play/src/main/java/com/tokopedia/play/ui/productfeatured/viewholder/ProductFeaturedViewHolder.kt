package com.tokopedia.play.ui.productfeatured.viewholder

import android.view.View
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.play.R
import com.tokopedia.play.ui.product.ProductBasicViewHolder
import com.tokopedia.play.view.uimodel.PlayProductUiModel

/**
 * Created by jegul on 23/02/21
 */
class ProductFeaturedViewHolder(
        itemView: View,
        private val listener: Listener,
) : ProductBasicViewHolder(itemView, listener) {

    fun sendImpression(item: PlayProductUiModel.Product) {
        itemView.addOnImpressionListener(item.impressHolder) {
            listener.onProductCardImpressed(item, adapterPosition)
        }
    }

    interface Listener : ProductBasicViewHolder.Listener {
        fun onProductCardImpressed(product: PlayProductUiModel.Product, position: Int)
    }

    companion object {
        val LAYOUT = R.layout.item_play_product_featured
    }
}