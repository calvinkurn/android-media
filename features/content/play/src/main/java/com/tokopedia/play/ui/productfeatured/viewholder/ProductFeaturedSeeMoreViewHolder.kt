package com.tokopedia.play.ui.productfeatured.viewholder

import android.view.View
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

    private val icProduct: ProductIconView = itemView.findViewById(R.id.ic_play_featured_product)

    init {
        itemView.setOnClickListener { listener.onSeeMoreClicked() }
    }

    fun bind(item: PlayProductUiModel.SeeMore) {
        icProduct.setTotalProduct(item.total)
        itemView.scaleX = item.scale
        itemView.scaleY = item.scale
    }

    companion object {
        val LAYOUT = R.layout.item_play_product_featured_more_action
    }

    interface Listener {
        fun onSeeMoreClicked()
    }
}