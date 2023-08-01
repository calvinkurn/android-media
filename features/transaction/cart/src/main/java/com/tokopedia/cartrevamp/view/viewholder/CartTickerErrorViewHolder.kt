package com.tokopedia.cartrevamp.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.HolderItemCartTickerErrorBinding
import com.tokopedia.cartrevamp.view.ActionListener
import com.tokopedia.cartrevamp.view.uimodel.CartItemTickerErrorHolderData

class CartTickerErrorViewHolder(private val binding: HolderItemCartTickerErrorBinding, private val actionListener: ActionListener?) : RecyclerView.ViewHolder(binding.root) {

    fun bindData(data: CartItemTickerErrorHolderData) {
        val errorInfoTemplate = itemView.context?.getString(R.string.cart_error_message) ?: ""
        val errorInfo = String.format(errorInfoTemplate, data.errorProductCount)

        binding.tickerError.setTextDescription(errorInfo)
        binding.tickerAction.setOnClickListener { actionListener?.onSeeErrorProductsClicked() }
    }

    companion object {
        val TYPE_VIEW_TICKER_CART_ERROR = R.layout.holder_item_cart_ticker_error
    }
}
