package com.tokopedia.similarsearch.productitem

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.similarsearch.getsimilarproducts.model.Product
import com.tokopedia.similarsearch.abstraction.BaseAdapterDelegate

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