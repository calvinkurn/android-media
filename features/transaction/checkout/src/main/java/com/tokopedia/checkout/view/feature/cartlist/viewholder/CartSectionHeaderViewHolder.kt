package com.tokopedia.checkout.view.feature.cartlist.viewholder

import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.feature.cartlist.ActionListener
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartSectionHeaderHolderData
import kotlinx.android.synthetic.main.item_cart_section_header.view.*

/**
 * Created by Irfan Khoirul on 2019-05-29.
 */

class CartSectionHeaderViewHolder(val view: View, val listener: ActionListener) : RecyclerView.ViewHolder(view) {

    companion object {
        val LAYOUT = R.layout.item_cart_section_header
    }

    fun bind(element: CartSectionHeaderHolderData) {
        itemView.label_title.text = element.title
        if (TextUtils.isEmpty(element.showAllAppLink)) {
            itemView.label_show_all.visibility = View.GONE
        } else {
            itemView.label_show_all.setOnClickListener {
                listener.onShowAllItem(element.showAllAppLink)
            }
            itemView.label_show_all.visibility = View.VISIBLE
        }
    }

}