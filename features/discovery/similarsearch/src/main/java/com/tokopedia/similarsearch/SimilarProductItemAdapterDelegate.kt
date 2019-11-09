package com.tokopedia.similarsearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

internal class SimilarProductItemAdapterDelegate: BaseAdapterDelegate<Product, SimilarProductItemViewHolder>() {

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is Product
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(SimilarProductItemViewHolder.LAYOUT, parent, false)
        return SimilarProductItemViewHolder(itemView)
    }
}