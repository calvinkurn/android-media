package com.tokopedia.cart.view.viewholder.now

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartCollapsedListBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.adapter.collapsedproduct.CartCollapsedProductAdapter
import com.tokopedia.cart.view.decorator.CartHorizontalItemDecoration
import com.tokopedia.cart.view.uimodel.now.CartCollapsedProductListHolderData
import kotlin.math.min

class CartCollapsedProductListViewHolder(val viewBinding: ItemCartCollapsedListBinding, val actionListener: ActionListener) : RecyclerView.ViewHolder(viewBinding.root) {

    val paddingLeft = itemView.context?.resources?.getDimension(R.dimen.dp_40)?.toInt() ?: 0
    val paddingRight = itemView.context?.resources?.getDimension(R.dimen.dp_16)?.toInt() ?: 0
    var cartCartCollapsedProductAdapter = CartCollapsedProductAdapter(actionListener)
    var cartHorizontalItemDecoration = CartHorizontalItemDecoration(paddingLeft, paddingRight)

    companion object {
        var LAYOUT = R.layout.item_cart_collapsed_list

        const val MAXIMUM_ITEM = 10
    }

    fun bind(cartCollapsedProductListHolderData: CartCollapsedProductListHolderData) {
        val maxIndex = min(MAXIMUM_ITEM, cartCollapsedProductListHolderData.cartCollapsedProductHolderDataList.size)
        cartCartCollapsedProductAdapter.parentPosition = adapterPosition
        cartCartCollapsedProductAdapter.cartCollapsedProductHolderDataList = cartCollapsedProductListHolderData.cartCollapsedProductHolderDataList.subList(0, maxIndex)
        val layoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
        viewBinding.recyclerView.layoutManager = layoutManager
        viewBinding.recyclerView.adapter = cartCartCollapsedProductAdapter
        val itemDecorationCount = viewBinding.recyclerView.itemDecorationCount
        if (itemDecorationCount > 0) {
            viewBinding.recyclerView.removeItemDecorationAt(0)
        }
        viewBinding.recyclerView.addItemDecoration(cartHorizontalItemDecoration)
    }

}