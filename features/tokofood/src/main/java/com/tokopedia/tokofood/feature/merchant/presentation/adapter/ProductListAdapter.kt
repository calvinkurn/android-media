package com.tokopedia.tokofood.feature.merchant.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokofood.databinding.TokofoodCategoryHeaderLayoutBinding
import com.tokopedia.tokofood.databinding.TokofoodProductCardLayoutBinding
import com.tokopedia.tokofood.feature.merchant.presentation.enums.ProductListItemType.CATEGORY_HEADER
import com.tokopedia.tokofood.feature.merchant.presentation.enums.ProductListItemType.PRODUCT_CARD
import com.tokopedia.tokofood.feature.merchant.presentation.enums.ProductListItemType.values
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductListItem
import com.tokopedia.tokofood.feature.merchant.presentation.viewholder.CategoryHeaderViewHolder
import com.tokopedia.tokofood.feature.merchant.presentation.viewholder.ProductCardViewHolder
import com.tokopedia.tokofood.feature.merchant.presentation.viewholder.ProductCardViewHolder.OnProductCardItemClickListener

class ProductListAdapter(private val clickListener: OnProductCardItemClickListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var productListItems: MutableList<ProductListItem> = mutableListOf()

    fun getProductListItems() = productListItems

    override fun getItemViewType(position: Int): Int {
        return when (productListItems[position].listItemType) {
            CATEGORY_HEADER -> CATEGORY_HEADER.type
            PRODUCT_CARD -> PRODUCT_CARD.type
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (values().first { it.type == viewType }) {
            CATEGORY_HEADER -> {
                val binding = TokofoodCategoryHeaderLayoutBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                CategoryHeaderViewHolder(binding)
            }
            PRODUCT_CARD -> {
                val binding = TokofoodProductCardLayoutBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                ProductCardViewHolder(binding, clickListener)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            CATEGORY_HEADER.type -> {
                val viewHolder = holder as CategoryHeaderViewHolder
                val categoryName = productListItems[position].productCategory.title
                viewHolder.bindData(categoryName)
            }
            PRODUCT_CARD.type -> {
                val viewHolder = holder as ProductCardViewHolder
                val productUiModel = productListItems[position].productUiModel
                viewHolder.bindData(productUiModel, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return productListItems.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setProductListItems(productListItems: List<ProductListItem>) {
        this.productListItems = productListItems.toMutableList()
        notifyDataSetChanged()
    }

    fun updateOrderNote(orderNote: String, cardPositions: Pair<Int, Int>) {
        val dataSetPosition = cardPositions.first
        val adapterPosition = cardPositions.second
        productListItems[dataSetPosition].productUiModel.orderNote = orderNote
        notifyItemChanged(adapterPosition)
    }

    fun updateAtcStatus(isAtc: Boolean, cardPositions: Pair<Int, Int>) {
        val dataSetPosition = cardPositions.first
        val adapterPosition = cardPositions.second
        productListItems[dataSetPosition].productUiModel.isAtc = isAtc
        notifyItemChanged(adapterPosition)
    }
}