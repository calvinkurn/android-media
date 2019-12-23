package com.tokopedia.purchase_platform.features.cart.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.cart.view.ActionListener
import kotlinx.android.synthetic.main.item_select_all.view.*

class CartSelectAllViewHolder(itemView: View, val actionListener: ActionListener?): RecyclerView.ViewHolder(itemView) {

    fun bind(isAllSelected: Boolean) {
        itemView.ll_header.invisible()

//        itemView.cb_select_all.setOnClickListener {
//            actionListener?.onSelectAllClicked()
//        }
    }

    companion object {
        val LAYOUT = R.layout.item_select_all
    }
}