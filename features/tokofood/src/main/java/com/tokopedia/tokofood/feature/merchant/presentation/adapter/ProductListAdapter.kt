package com.tokopedia.tokofood.feature.merchant.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.tokofood.common.domain.response.CartTokoFood
import com.tokopedia.tokofood.common.presentation.listener.TokofoodScrollChangedListener
import com.tokopedia.tokofood.databinding.TokofoodCategoryHeaderLayoutBinding
import com.tokopedia.tokofood.databinding.TokofoodProductCardLayoutBinding
import com.tokopedia.tokofood.feature.merchant.presentation.enums.ProductListItemType.CATEGORY_HEADER
import com.tokopedia.tokofood.feature.merchant.presentation.enums.ProductListItemType.PRODUCT_CARD
import com.tokopedia.tokofood.feature.merchant.presentation.enums.ProductListItemType.values
import com.tokopedia.tokofood.feature.merchant.presentation.model.CustomOrderDetail
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductListItem
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.viewholder.CategoryHeaderViewHolder
import com.tokopedia.tokofood.feature.merchant.presentation.viewholder.ProductCardViewHolder
import com.tokopedia.tokofood.feature.merchant.presentation.viewholder.ProductCardViewHolder.OnProductCardItemClickListener

class ProductListAdapter(private val clickListener: OnProductCardItemClickListener,
                         private val tokofoodScrollChangedListener: TokofoodScrollChangedListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
                ProductCardViewHolder(binding, clickListener, tokofoodScrollChangedListener)
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
                val productListItem = productListItems[position]
                viewHolder.bindData(productListItem, productUiModel, position)
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

    fun getProductUiModel(dataSetPosition: Int): ProductUiModel? {
        return productListItems.getOrNull(dataSetPosition)?.productUiModel
    }

    fun updateProductUiModel(
        cartTokoFood: CartTokoFood,
        dataSetPosition: Int,
        adapterPosition: Int,
        customOrderDetail: CustomOrderDetail? = null
    ) {
        productListItems.getOrNull(dataSetPosition)?.productUiModel?.run {
            var sameCustomProductExist = false
            val sameCustomProduct = this.customOrderDetails.firstOrNull { it.cartId == cartTokoFood.cartId }
            sameCustomProductExist = sameCustomProduct != null
            if (sameCustomProductExist) {
                if (!isCustomizable) cartId = cartTokoFood.cartId
                orderQty = cartTokoFood.quantity
                orderNote = cartTokoFood.getMetadata()?.notes.orEmpty()
                isAtc = cartTokoFood.quantity.isMoreThanZero()
                sameCustomProduct?.apply { qty += 1 }
            } else {
                if (!isCustomizable) cartId = cartTokoFood.cartId
                orderQty = cartTokoFood.quantity
                orderNote = cartTokoFood.getMetadata()?.notes.orEmpty()
                isAtc = cartTokoFood.quantity.isMoreThanZero()
                customOrderDetail?.let { customOrderDetails.add(it) }
            }
            notifyItemChanged(adapterPosition)
        }
    }

    fun updateCartProductUiModel(
        cartTokoFood: CartTokoFood,
        dataSetPosition: Int,
        adapterPosition: Int,
        customOrderDetail: CustomOrderDetail? = null
    ) {
        productListItems.getOrNull(dataSetPosition)?.productUiModel?.apply {
            if (customOrderDetail != null) {
                val position = customOrderDetails.indexOfFirst { it.cartId == customOrderDetail.cartId }
                if (position > RecyclerView.NO_POSITION) {

                    if (!isCustomizable) cartId = cartTokoFood.cartId
                    orderQty = cartTokoFood.quantity
                    orderNote = cartTokoFood.getMetadata()?.notes.orEmpty()
                    isAtc = cartTokoFood.quantity.isMoreThanZero()

                    customOrderDetails[position] = customOrderDetail
                    notifyItemChanged(adapterPosition)
                }
            }
        }
    }

    fun updateCustomOrderQty(cartId: String, orderQty: Int, dataSetPosition: Int) {
        productListItems.getOrNull(dataSetPosition)?.productUiModel?.customOrderDetails?.firstOrNull { it.cartId == cartId }?.qty =
            orderQty
    }

    fun removeCustomOrder(cartId: String, dataSetPosition: Int, adapterPosition: Int) {
        productListItems.getOrNull(dataSetPosition)?.productUiModel?.apply {
            customOrderDetails.removeFirst { it.cartId == cartId }
            isAtc = customOrderDetails.isNotEmpty()
        }
        notifyItemChanged(adapterPosition)
    }

    fun resetProductUiModel(dataSetPosition: Int, adapterPosition: Int) {
        productListItems.getOrNull(dataSetPosition)?.productUiModel?.run {
            isAtc = false
            cartId = ""
            orderQty = Int.ONE
            orderNote = ""
            customOrderDetails = mutableListOf()
        }
        notifyItemChanged(adapterPosition)
    }
}
