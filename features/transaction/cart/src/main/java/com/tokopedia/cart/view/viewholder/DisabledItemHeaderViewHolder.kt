package com.tokopedia.cart.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.DisabledItemHeaderHolderData
import kotlinx.android.synthetic.main.item_cart_disabled_header.view.*

/**
 * Created by Irfan Khoirul on 2019-10-16.
 */

class DisabledItemHeaderViewHolder(itemView: View, val actionListener: ActionListener?) : RecyclerView.ViewHolder(itemView) {

    companion object {
        val LAYOUT = R.layout.item_cart_disabled_header
    }

    fun bind(data: DisabledItemHeaderHolderData) {
        itemView.text_disabled_item_count.text = "${data.disabledItemCount} barang tidak dapat dibeli"
        itemView.text_delete.setOnClickListener { actionListener?.onDeleteAllDisabledProduct() }
    }

}