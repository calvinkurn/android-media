package com.tokopedia.similarsearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard.v2.ProductCardModel

internal class SimilarProductItemAdapterDelegate: BaseAdapterDelegate<ProductCardModel, SimilarProductItemViewHolder>() {

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is ProductCardModel
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.similar_search_product_card_layout, parent, false)
        return SimilarProductItemViewHolder(itemView)
    }
}