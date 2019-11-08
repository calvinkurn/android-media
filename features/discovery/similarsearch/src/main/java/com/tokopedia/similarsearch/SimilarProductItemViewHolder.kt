package com.tokopedia.similarsearch

import android.view.View
import com.tokopedia.productcard.v2.ProductCardModel
import kotlinx.android.synthetic.main.similar_search_product_card_layout.view.*

internal class SimilarProductItemViewHolder(itemView: View): BaseViewHolder<ProductCardModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.similar_search_product_card_layout
    }

    override fun bind(productCardModel: ProductCardModel) {
        itemView.productCardView?.setProductModel(productCardModel)
    }
}