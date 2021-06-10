package com.tokopedia.cart.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.HolderItemCartTickerErrorBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.CartItemTickerErrorHolderData

/**
 * @author anggaprasetiyo on 13/03/18.
 */
class CartTickerErrorViewHolder(private val binding: HolderItemCartTickerErrorBinding, private val actionListener: ActionListener?) : RecyclerView.ViewHolder(binding.root) {

    fun bindData(data: CartItemTickerErrorHolderData) {
        binding.tickerError.setTextDescription(data.cartTickerErrorData?.errorInfo ?: "")
        binding.tickerAction.setOnClickListener { v -> actionListener?.onSeeErrorProductsClicked() }
    }

    companion object {
        val TYPE_VIEW_TICKER_CART_ERROR = R.layout.holder_item_cart_ticker_error
    }
}
