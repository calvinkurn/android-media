package com.tokopedia.find_native.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.find_native.R
import com.tokopedia.common_category.adapter.QuickFilterAdapter
import com.tokopedia.common_category.model.productModel.ProductsItem
import com.tokopedia.unifyprinciples.Typography

class FindPriceListAdapter(var productList: ArrayList<ProductsItem>, private var priceListClickListener: PriceListClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_PRICE = 0
        const val VIEW_SHIMMER = 1
        const val SHIMMER_LAYOUT_COUNT = 4
        const val PRICE_COUNT = 10
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_PRICE -> {
                val v = LayoutInflater.from(parent.context).inflate(PriceViewHolder.LAYOUT, parent, false)
                PriceViewHolder(v)
            }
            else -> {
                val v = LayoutInflater.from(parent.context).inflate(QuickFilterAdapter.ShimmerViewHolder.Layout, parent, false)
                return QuickFilterAdapter.ShimmerViewHolder(v)
            }
        }
    }

    class PriceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object {
            val LAYOUT = R.layout.related_price_rv_item
        }

        val findItem: Typography = itemView.findViewById(R.id.find_item)
        val findItemPrice: Typography = itemView.findViewById(R.id.find_item_price)
    }

    override fun getItemCount(): Int {
        return if (productList.size <= 0) {
            SHIMMER_LAYOUT_COUNT
        } else {
            minOf(PRICE_COUNT, productList.size)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            VIEW_PRICE -> setPriceListData(holder as PriceViewHolder, position)
            else -> {
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (productList.size > 0) {
            VIEW_PRICE
        } else {
            VIEW_SHIMMER
        }
    }

    private fun setPriceListData(viewHolder: PriceViewHolder, position: Int) {
        val product = productList[position]
        viewHolder.findItem.text = product.name.trim()
        viewHolder.findItemPrice.text = product.price
        viewHolder.findItem.setOnClickListener {
            priceListClickListener.onPriceListClick(product, viewHolder.adapterPosition)
        }
    }

    interface PriceListClickListener {
        fun onPriceListClick(product: ProductsItem, adapterPosition: Int)
    }
}