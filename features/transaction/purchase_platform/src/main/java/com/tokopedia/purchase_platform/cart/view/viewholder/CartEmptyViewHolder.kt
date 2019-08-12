package com.tokopedia.purchase_platform.cart.view.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.cart.view.ActionListener
import com.tokopedia.purchase_platform.cart.view.viewmodel.CartEmptyHolderData
import kotlinx.android.synthetic.main.item_empty_cart_placeholder.view.*

/**
 * Created by Irfan Khoirul on 2019-05-29.
 */

class CartEmptyViewHolder(val view: View, val listener: ActionListener): RecyclerView.ViewHolder(view) {

    companion object {
        val LAYOUT = R.layout.item_empty_cart_placeholder
    }

    fun bind(element: CartEmptyHolderData) {
        itemView.btn_shopping_now.setOnClickListener {
            listener.onClickShopNow()
        }
    }

}