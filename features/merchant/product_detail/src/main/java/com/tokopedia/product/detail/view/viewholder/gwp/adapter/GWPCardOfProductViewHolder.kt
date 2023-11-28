package com.tokopedia.product.detail.view.viewholder.gwp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.product.detail.databinding.GwpCardItemBinding
import com.tokopedia.product.detail.view.viewholder.gwp.model.GWPWidgetUiModel
import com.tokopedia.product.detail.R as productdetailR

/**
 * Created by yovi.putra on 27/07/23"
 * Project name: android-tokopedia-core
 **/

class GWPCardOfProductViewHolder(
    private val binding: GwpCardItemBinding
) : GWPCardViewHolder<GWPWidgetUiModel.Card.Product>(binding.root) {

    override fun bind(data: GWPWidgetUiModel.Card.Product) {
    }

    // override fun bind(product: GWPWidgetUiModel.Card.Product) {
        /*binding.bmgmProductImage.loadImage(product.imageUrl)

        if (product.loadMoreText.isNotBlank()) {
            overlayBinding.root.show()
            overlayBinding.bmgmShowMoreText.text = product.loadMoreText
        } else if (binding.bmgmProductShowMoreStub.isInflated()) {
            overlayBinding.root.hide()
        }*/
    // }

    companion object {
        val ID = productdetailR.layout.gwp_card_item

        fun create(parent: ViewGroup): GWPCardOfProductViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = GwpCardItemBinding.inflate(inflater)
            return GWPCardOfProductViewHolder(view)
        }
    }
}
