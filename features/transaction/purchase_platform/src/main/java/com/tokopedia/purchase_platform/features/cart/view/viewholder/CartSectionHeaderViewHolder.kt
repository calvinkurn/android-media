package com.tokopedia.purchase_platform.features.cart.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.cart.view.ActionListener
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartSectionHeaderHolderData
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