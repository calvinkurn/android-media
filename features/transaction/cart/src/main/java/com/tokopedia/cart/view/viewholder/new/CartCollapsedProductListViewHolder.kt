package com.tokopedia.cart.view.viewholder.new

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartCollapsedListBinding
import com.tokopedia.cart.view.adapter.collapsedproduct.CartCollapsedProductAdapter
import com.tokopedia.cart.view.decorator.CartHorizontalItemDecoration
import com.tokopedia.cart.view.uimodel.new.CartCollapsedProductListHolderData

class CartCollapsedProductListViewHolder(val viewBinding: ItemCartCollapsedListBinding) : RecyclerView.ViewHolder(viewBinding.root) {

    var cartCartCollapsedProductAdapter = CartCollapsedProductAdapter()
    var cartHorizontalItemDecoration = CartHorizontalItemDecoration()

    companion object {
        var LAYOUT = R.layout.item_cart_collapsed_list

        const val MAXIMUM_ITEM = 10
    }

    fun bind(cartCollapsedProductListHolderData: CartCollapsedProductListHolderData) {
        renderCollapsedItems(cartCollapsedProductListHolderData)
        renderAccordion(cartCollapsedProductListHolderData)
    }

    private fun renderCollapsedItems(cartCollapsedProductListHolderData: CartCollapsedProductListHolderData) {
        cartCartCollapsedProductAdapter.cartCollapsedProductHolderDataList = cartCollapsedProductListHolderData.cartCollapsedProductHolderDataList.subList(0, 9)
        val layoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
        viewBinding.recyclerView.layoutManager = layoutManager
        viewBinding.recyclerView.adapter = cartCartCollapsedProductAdapter
        val itemDecorationCount = viewBinding.recyclerView.itemDecorationCount
        if (itemDecorationCount > 0) {
            viewBinding.recyclerView.removeItemDecorationAt(0)
        }
        viewBinding.recyclerView.addItemDecoration(cartHorizontalItemDecoration)
    }

    private fun renderAccordion(cartCollapsedProductListHolderData: CartCollapsedProductListHolderData) {
        if (cartCollapsedProductListHolderData.cartCollapsedProductHolderDataList.size > MAXIMUM_ITEM) {
            val exceedItemCount = cartCollapsedProductListHolderData.cartCollapsedProductHolderDataList.size - MAXIMUM_ITEM
            val accordionText = "+$exceedItemCount lainnya"
            viewBinding.textAccordion.text = accordionText
        } else {
            val accordionText = "Lihat selengkapnya"
            viewBinding.textAccordion.text = accordionText
        }
    }

}