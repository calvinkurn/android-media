package com.tokopedia.cart.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.CartItemTickerErrorHolderData
import kotlinx.android.synthetic.main.holder_item_cart_ticker_error.view.*

/**
 * @author anggaprasetiyo on 13/03/18.
 */
class CartTickerErrorViewHolder(itemView: View, private val actionListener: ActionListener?) : RecyclerView.ViewHolder(itemView) {

    fun bindData(data: CartItemTickerErrorHolderData, position: Int) {
        itemView.ticker_description.text = data.cartTickerErrorData?.errorInfo
        itemView.ticker_action.setOnClickListener { v -> actionListener?.onSeeErrorProductsClicked() }
    }

    companion object {
        val TYPE_VIEW_TICKER_CART_ERROR = R.layout.holder_item_cart_ticker_error
    }
}
