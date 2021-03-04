package com.tokopedia.cart.view.viewholder

import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.CartSectionHeaderHolderData
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import kotlinx.android.synthetic.main.item_cart_section_header.view.*

/**
 * Created by Irfan Khoirul on 2019-05-29.
 */

class CartSectionHeaderViewHolder(val view: View, val listener: ActionListener?) : RecyclerView.ViewHolder(view) {

    companion object {
        val LAYOUT = R.layout.item_cart_section_header
    }

    fun bind(element: CartSectionHeaderHolderData) {
        itemView.label_title.text = element.title
        if (TextUtils.isEmpty(element.showAllAppLink)) {
            itemView.label_show_all.gone()
            itemView.ic_show_all.gone()
        } else {
            itemView.label_show_all.setOnClickListener {
                listener?.onShowAllItem(element.showAllAppLink)
            }
            itemView.ic_show_all.setOnClickListener {
                listener?.onShowAllItem(element.showAllAppLink)
            }
            itemView.label_show_all.visible()
            itemView.ic_show_all.visible()
        }
    }

}