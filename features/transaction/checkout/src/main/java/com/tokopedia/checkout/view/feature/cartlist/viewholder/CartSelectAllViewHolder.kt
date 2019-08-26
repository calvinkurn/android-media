package com.tokopedia.checkout.view.feature.cartlist.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.feature.cartlist.ActionListener
import kotlinx.android.synthetic.main.item_select_all.view.*

class CartSelectAllViewHolder(itemView: View, val actionListener: ActionListener): RecyclerView.ViewHolder(itemView) {

    fun bind(isAllSelected: Boolean) {
        itemView.cb_select_all.isChecked = isAllSelected
        itemView.cb_select_all.setOnClickListener {
            actionListener.onSelectAllClicked()
        }
    }

    companion object {
        val LAYOUT = R.layout.item_select_all
    }
}