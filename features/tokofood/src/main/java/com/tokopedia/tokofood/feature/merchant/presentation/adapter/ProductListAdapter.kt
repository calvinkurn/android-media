package com.tokopedia.tokofood.feature.merchant.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.tokofood.common.domain.response.CartTokoFood
import com.tokopedia.tokofood.databinding.TokofoodCategoryHeaderLayoutBinding
import com.tokopedia.tokofood.databinding.TokofoodProductCardLayoutBinding
import com.tokopedia.tokofood.feature.merchant.presentation.enums.ProductListItemType.*
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductListItem
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.viewholder.CategoryHeaderViewHolder
import com.tokopedia.tokofood.feature.merchant.presentation.viewholder.ProductCardViewHolder
import com.tokopedia.tokofood.feature.merchant.presentation.viewholder.ProductCardViewHolder.OnProductCardItemClickListener

class ProductListAdapter(private val clickListener: OnProductCardItemClickListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var productListItems: MutableList<ProductListItem> = mutableListOf()

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

    fun getProductUiModel(dataSetPosition: Int): ProductUiModel {
        return productListItems[dataSetPosition].productUiModel
    }

    fun updateProductUiModel(cartTokoFood: CartTokoFood, dataSetPosition: Int, adapterPosition: Int) {
        productListItems[dataSetPosition].productUiModel.apply {
            cartId = cartTokoFood.cartId
            orderQty = cartTokoFood.quantity
            orderNote = cartTokoFood.getMetadata().notes
            isAtc = cartTokoFood.quantity.isMoreThanZero()
        }
        notifyItemChanged(adapterPosition)
    }

    fun updateAtcStatus(isAtc: Boolean, dataSetPosition: Int, adapterPosition: Int) {
        productListItems[dataSetPosition].productUiModel.isAtc = isAtc
        notifyItemChanged(adapterPosition)
    }

    fun updateOrderQty(orderQty: Int, dataSetPosition: Int) {
        productListItems[dataSetPosition].productUiModel.orderQty = orderQty
    }

    fun updateCustomOrderQty(cartId: String, orderQty: Int, dataSetPosition: Int) {
        productListItems[dataSetPosition].productUiModel.customOrderDetails.firstOrNull { it.cartId == cartId }?.qty = orderQty
    }

    fun removeCustomOrder(cartId: String, dataSetPosition: Int) {
        productListItems[dataSetPosition].productUiModel.customOrderDetails.removeFirst { it.cartId == cartId }
    }
}