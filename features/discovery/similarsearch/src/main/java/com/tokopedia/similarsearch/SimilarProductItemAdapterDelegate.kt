package com.tokopedia.similarsearch

import android.view.View
import androidx.recyclerview.widget.RecyclerView

internal class SimilarProductItemAdapterDelegate(
        private val similarProductItemListener: SimilarProductItemListener
): BaseAdapterDelegate<Product, SimilarProductItemViewHolder>() {

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is Product
    }

    override fun getViewHolderLayout(): Int {
        return SimilarProductItemViewHolder.LAYOUT
    }

    override fun onCreateViewHolder(inflatedView: View): RecyclerView.ViewHolder {
        return SimilarProductItemViewHolder(inflatedView, similarProductItemListener)
    }
}