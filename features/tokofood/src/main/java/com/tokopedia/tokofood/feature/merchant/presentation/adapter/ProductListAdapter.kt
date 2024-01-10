package com.tokopedia.tokofood.feature.merchant.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.tokofood.common.domain.response.CartListCartGroupCart
import com.tokopedia.tokofood.common.presentation.listener.TokofoodScrollChangedListener
import com.tokopedia.tokofood.common.presentation.mapper.CustomOrderDetailsMapper
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

class ProductListAdapter(
    private var clickListener: OnProductCardItemClickListener?,
    private var tokofoodScrollChangedListener: TokofoodScrollChangedListener?
) :
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

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder is ProductCardViewHolder) {
            holder.removeListeners()
        }
        super.onViewRecycled(holder)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setProductListItems(productListItems: List<ProductListItem>) {
        this.productListItems = productListItems.toMutableList()
        notifyDataSetChanged()
    }

    fun getProductUiModel(dataSetPosition: Int): ProductUiModel? {
        return productListItems.getOrNull(dataSetPosition)?.productUiModel
    }

    fun getProductUiModel(cartId: String): ProductUiModel? {
        return productListItems.find {
            it.productUiModel.cartId == cartId ||
                it.productUiModel.customOrderDetails.any { customOrderDetail ->
                    customOrderDetail.cartId == cartId
                }
        }?.productUiModel
    }

    fun getProductUiModelPositions(productId: String): List<Int> {
        val dataSetPositions: MutableList<Int> = mutableListOf()
        productListItems.forEachIndexed { index, productListItem ->
            if (productListItem.productUiModel.id == productId) {
                dataSetPositions.add(index)
            }
        }
        return dataSetPositions
    }

    fun updateProductUiModel(
        cartTokoFood: CartListCartGroupCart,
        dataSetPosition: Int,
        adapterPosition: Int,
        customOrderDetail: CustomOrderDetail? = null
    ) {
        productListItems.getOrNull(dataSetPosition)?.productUiModel?.run {
            val sameCustomProductExist: Boolean
            val sameCustomProduct = this.customOrderDetails.firstOrNull { it.cartId == cartTokoFood.cartId }
            sameCustomProductExist = sameCustomProduct != null
            if (sameCustomProductExist) {
                if (!isCustomizable) cartId = cartTokoFood.cartId
                orderQty = cartTokoFood.quantity
                orderNote = cartTokoFood.metadata.notes
                isAtc = cartTokoFood.quantity.isMoreThanZero()
                sameCustomProduct?.apply { qty += 1 }
            } else {
                if (!isCustomizable) cartId = cartTokoFood.cartId
                orderQty = cartTokoFood.quantity
                orderNote = cartTokoFood.metadata.notes
                isAtc = cartTokoFood.quantity.isMoreThanZero()
                customOrderDetail?.let { customOrderDetails.add(it) }
            }
            notifyItemChanged(adapterPosition)
        }
    }

    fun updateProductUiModel(
        dataSetPosition: Int,
        adapterPosition: Int,
        quantity: Int,
        currentCartId: String
    ) {
        productListItems.getOrNull(dataSetPosition)?.productUiModel?.run {
            orderQty = quantity
            cartId = currentCartId
            isAtc = quantity.isMoreThanZero()
        }
        notifyItemChanged(adapterPosition)
    }

    fun updateCartProductUiModel(
        cartTokoFood: CartListCartGroupCart? = null,
        cartTokoFoodList: List<CartListCartGroupCart>,
        dataSetPosition: Int,
        adapterPosition: Int
    ) {
        productListItems.getOrNull(dataSetPosition)?.productUiModel?.apply {
            if (!isCustomizable) cartId = cartTokoFood?.cartId.orEmpty()
            orderQty = cartTokoFoodList.size
            orderNote = cartTokoFood?.metadata?.notes.orEmpty()
            isAtc = cartTokoFoodList.sumOf { it.quantity }.isMoreThanZero()

            customOrderDetails = CustomOrderDetailsMapper.mapTokoFoodProductsToCustomOrderDetails(cartTokoFoodList)
            notifyItemChanged(adapterPosition)
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

    fun removeListeners() {
        clickListener = null
        tokofoodScrollChangedListener = null
    }
}
