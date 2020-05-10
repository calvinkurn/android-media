package com.tokopedia.notifcenter.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.notifcenter.data.viewbean.ProductHighlightViewBean
import com.tokopedia.notifcenter.presentation.adapter.viewholder.ProductHighlightViewHolder

class ProductHighlightAdapter(
        private val items: List<ProductHighlightViewBean>,
        private val onAtcClick: () -> Unit
): RecyclerView.Adapter<ProductHighlightViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHighlightViewHolder {
        return ProductHighlightViewHolder.builder(parent, onAtcClick)
    }

    override fun onBindViewHolder(holder: ProductHighlightViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

}