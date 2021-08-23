package com.tokopedia.play.ui.productfeatured.viewholder

import android.view.View
import android.widget.TextView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.R
import com.tokopedia.play.view.custom.ProductIconView
import com.tokopedia.play.view.uimodel.PlayProductUiModel

/**
 * Created by jegul on 24/02/21
 */
class ProductFeaturedSeeMoreViewHolder(
        itemView: View,
        listener: Listener
) : BaseViewHolder(itemView) {

    init {
        itemView.setOnClickListener { listener.onSeeMoreClicked() }
    }

    fun bind(item: PlayProductUiModel.SeeMore) {
        val icProduct = itemView.findViewById<ProductIconView>(R.id.ic_product)
        icProduct.setTotalProduct(item.total)
    }

    companion object {
        val LAYOUT = R.layout.item_play_product_featured_more_action
    }

    interface Listener {

        fun onSeeMoreClicked()
    }
}