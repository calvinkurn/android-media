package com.tokopedia.cart.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.tokopedia.cart.R
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.CartEmptyHolderData
import kotlinx.android.synthetic.main.item_empty_cart.view.*

/**
 * Created by Irfan Khoirul on 2019-05-29.
 */

class CartEmptyViewHolder(val view: View, val listener: ActionListener?): RecyclerView.ViewHolder(view) {

    companion object {
        val LAYOUT = R.layout.item_empty_cart
    }

    fun bind(data: CartEmptyHolderData) {
        itemView.cart_empty_state.apply {
            setTitle(data.title)
            setDescription(data.desc)
            setImageUrl(data.imgUrl)
            setPrimaryCTAText(data.btnText)
            setPrimaryCTAClickListener { listener?.onClickShopNow() }
        }
    }

}