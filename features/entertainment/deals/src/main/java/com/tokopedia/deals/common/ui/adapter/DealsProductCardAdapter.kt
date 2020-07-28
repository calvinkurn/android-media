package com.tokopedia.deals.common.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.deals.common.listener.ProductCardListener
import com.tokopedia.deals.common.ui.adapter.viewholder.ProductCardViewHolder
import com.tokopedia.deals.common.ui.dataview.ProductCardDataView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener

class DealsProductCardAdapter(private val productCardListener: ProductCardListener) :
    RecyclerView.Adapter<ProductCardViewHolder>() {
    var page = 0
    var productCards: List<ProductCardDataView> = mutableListOf()
        set(value) {
            val diff = DiffUtil.calculateDiff(ProductCardDiffCallback(field, value))
            field = value
            diff.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductCardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(ProductCardViewHolder.LAYOUT, parent, false)
        return ProductCardViewHolder(itemView, productCardListener)
    }

    override fun getItemCount(): Int = productCards.size

    override fun onBindViewHolder(holder: ProductCardViewHolder, position: Int) {
        val item = productCards[position]
        holder.bindData(productCards[position])
        holder.itemView.addOnImpressionListener(item) {
            productCardListener.onImpressionProduct(item, position,page)
        }
    }

    private class ProductCardDiffCallback(
        private val oldProductCards: List<ProductCardDataView>,
        private val newProductCards: List<ProductCardDataView>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldProductCards.size

        override fun getNewListSize(): Int = newProductCards.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldProductCards[oldItemPosition].id == newProductCards[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldProductCards[oldItemPosition] == newProductCards[newItemPosition]
        }
    }
}