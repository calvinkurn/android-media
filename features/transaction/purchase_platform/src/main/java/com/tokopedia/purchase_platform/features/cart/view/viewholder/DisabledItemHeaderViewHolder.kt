package com.tokopedia.purchase_platform.features.cart.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.cart.view.ActionListener
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.DisabledItemHeaderHolderData
import kotlinx.android.synthetic.main.item_cart_disabled_header.view.*

/**
 * Created by Irfan Khoirul on 2019-10-16.
 */

class DisabledItemHeaderViewHolder(itemView: View, val actionListener: ActionListener) : RecyclerView.ViewHolder(itemView) {

    companion object {
        val LAYOUT = R.layout.item_cart_disabled_header
    }

    fun bind(data: DisabledItemHeaderHolderData) {
        itemView.text_disabled_item_count.text = "${data.disabledItemCount} barang tidak dapat dibeli"
        itemView.text_delete.setOnClickListener { actionListener.onDeleteAllDisabledProduct() }
    }

}