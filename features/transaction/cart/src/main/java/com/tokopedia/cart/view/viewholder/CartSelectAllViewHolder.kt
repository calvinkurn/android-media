package com.tokopedia.cart.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.tokopedia.cart.R
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.cart.view.ActionListener
import kotlinx.android.synthetic.main.item_select_all.view.*

class CartSelectAllViewHolder(itemView: View, val actionListener: ActionListener?): RecyclerView.ViewHolder(itemView) {

    fun bind(isAllSelected: Boolean) {
        itemView.ll_header.invisible()
    }

    companion object {
        val LAYOUT = R.layout.item_select_all
    }
}