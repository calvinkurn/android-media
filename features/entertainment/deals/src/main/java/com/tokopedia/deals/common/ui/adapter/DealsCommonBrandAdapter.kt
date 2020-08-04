package com.tokopedia.deals.common.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.deals.common.listener.DealsBrandActionListener
import com.tokopedia.deals.common.ui.adapter.viewholder.DealsBrandViewHolder
import com.tokopedia.deals.common.ui.dataview.DealsBrandsDataView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener

/**
 * @author by jessica on 17/06/20
 */

class DealsCommonBrandAdapter(private val brandActionListener: DealsBrandActionListener, private val layoutType: Int) :
        RecyclerView.Adapter<DealsBrandViewHolder>() {

    var brandList: List<DealsBrandsDataView.Brand> = mutableListOf()
        set(value) {
            val diff = DiffUtil.calculateDiff(ProductCardDiffCallback(field, value))
            field = value
            diff.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealsBrandViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutType, parent, false)
        return DealsBrandViewHolder(view, brandActionListener, layoutType)
    }

    override fun getItemCount(): Int = brandList.size

    override fun onBindViewHolder(holder: DealsBrandViewHolder, position: Int) {
        val item = brandList[position]
        holder.bind(brandList[position])
        holder.itemView.addOnImpressionListener(item) {
            brandActionListener.onImpressionBrand(item,position)
        }
    }

    private class ProductCardDiffCallback(
            private val oldProductCards: List<DealsBrandsDataView.Brand>,
            private val newProductCards: List<DealsBrandsDataView.Brand>
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
