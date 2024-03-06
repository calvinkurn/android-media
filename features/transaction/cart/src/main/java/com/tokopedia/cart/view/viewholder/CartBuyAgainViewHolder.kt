package com.tokopedia.cart.view.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartBuyAgainBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.adapter.buyagain.CartBuyAgainAdapter
import com.tokopedia.cart.view.decorator.CartHorizontalItemDecoration
import com.tokopedia.cart.view.uimodel.CartBuyAgainHolderData
import com.tokopedia.cart.view.uimodel.CartBuyAgainItemHolderData
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.unifycomponents.UnifyButton

class CartBuyAgainViewHolder(
    private val binding: ItemCartBuyAgainBinding,
    private val listener: ActionListener?
) : RecyclerView.ViewHolder(binding.root) {

    private var buyAgainAdapter: CartBuyAgainAdapter? = null

    companion object {
        val LAYOUT = R.layout.item_cart_buy_again
    }

    fun bind(element: CartBuyAgainHolderData) {
        if (element.buyAgainList.size == 1) {
            binding.productCardView.apply {
                val data = element.buyAgainList[0] as CartBuyAgainItemHolderData
                setProductModel(
                    data.recommendationItem.toProductCardModel(true, UnifyButton.Type.MAIN)
                        .copy(isInBackground = true)
                )
                setOnClickListener {
                    listener?.onBuyAgainProductClicked(data)
                }
                setAddToCartOnClickListener {
                    listener?.onBuyAgainButtonAddToCartClicked(data)
                }
                visible()
            }
            binding.rvBuyAgain.gone()
        } else {
            if (buyAgainAdapter == null) {
                buyAgainAdapter = CartBuyAgainAdapter(listener)
            }
            buyAgainAdapter?.buyAgainList = element.buyAgainList
            val layoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
            binding.rvBuyAgain.layoutManager = layoutManager
            binding.rvBuyAgain.adapter = buyAgainAdapter
            val itemDecorationCount = binding.rvBuyAgain.itemDecorationCount
            if (itemDecorationCount > 0) {
                binding.rvBuyAgain.removeItemDecorationAt(0)
            }
            binding.rvBuyAgain.addItemDecoration(
                CartHorizontalItemDecoration(
                    12.dpToPx(itemView.resources.displayMetrics),
                    16.dpToPx(itemView.resources.displayMetrics)
                )
            )
            binding.productCardView.gone()
            binding.rvBuyAgain.visible()
        }
        if (!element.hasSentImpressionAnalytics) {
            listener?.onBuyAgainImpression(element.buyAgainList)
            element.hasSentImpressionAnalytics = true
        }
    }
}

