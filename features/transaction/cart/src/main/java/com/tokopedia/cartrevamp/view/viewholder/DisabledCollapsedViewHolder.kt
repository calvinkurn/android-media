package com.tokopedia.cartrevamp.view.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartDisabledCollapsedBinding
import com.tokopedia.cartrevamp.view.ActionListener
import com.tokopedia.cartrevamp.view.adapter.collapsedproduct.CartCollapsedProductAdapter
import com.tokopedia.cartrevamp.view.decorator.CartHorizontalItemDecoration
import com.tokopedia.cartrevamp.view.uimodel.CartItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.DisabledCollapsedHolderData
import com.tokopedia.kotlin.extensions.view.dpToPx
import kotlin.math.min

class DisabledCollapsedViewHolder(
    private val binding: ItemCartDisabledCollapsedBinding,
    val actionListener: ActionListener
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        val LAYOUT = R.layout.item_cart_disabled_collapsed

        private const val COLLAPSED_PRODUCTS_LIMIT = 10

        private const val ITEM_DECORATION_PADDING = 16
    }

    fun bind(data: DisabledCollapsedHolderData) {
        val cartItemDataList = data.productUiModelList.distinctBy {
            if (it.isBundlingItem) it.bundleGroupId else it.productId
        }
        val maxIndex = min(COLLAPSED_PRODUCTS_LIMIT, cartItemDataList.size)
        val cartCartCollapsedProductAdapter = CartCollapsedProductAdapter(actionListener)
        cartCartCollapsedProductAdapter.cartCollapsedProductHolderDataList =
            cartItemDataList.subList(0, maxIndex)
        val layoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
        binding.rvCartItem.layoutManager = layoutManager
        binding.rvCartItem.adapter = cartCartCollapsedProductAdapter

        setCollapsedRecyclerViewHeight()

        val itemDecorationCount = binding.rvCartItem.itemDecorationCount
        if (itemDecorationCount > 0) {
            binding.rvCartItem.removeItemDecorationAt(0)
        }
        val paddingLeft = ITEM_DECORATION_PADDING.dpToPx(itemView.resources.displayMetrics)
        val paddingRight = ITEM_DECORATION_PADDING.dpToPx(itemView.resources.displayMetrics)
        binding.rvCartItem.addItemDecoration(
            CartHorizontalItemDecoration(
                paddingLeft,
                paddingRight
            )
        )
    }

    private fun setCollapsedRecyclerViewHeight() {
        binding.rvCartItem.layoutParams.height =
            itemView.context.resources.getDimensionPixelSize(R.dimen.dp_72)
        binding.rvCartItem.requestLayout()
    }
}
