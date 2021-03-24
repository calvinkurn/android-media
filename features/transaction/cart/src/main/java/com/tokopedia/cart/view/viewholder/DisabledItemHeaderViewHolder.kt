package com.tokopedia.cart.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartDisabledHeaderBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.DisabledItemHeaderHolderData

/**
 * Created by Irfan Khoirul on 2019-10-16.
 */

class DisabledItemHeaderViewHolder(private val binding: ItemCartDisabledHeaderBinding, val actionListener: ActionListener?) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        val LAYOUT = R.layout.item_cart_disabled_header
    }

    fun bind(data: DisabledItemHeaderHolderData) {
        val disabledLabelText = String.format(itemView.context.getString(R.string.label_cannot_be_processed), data.disabledItemCount)
        binding.textDisabledItemCount.text = disabledLabelText
        binding.textDelete.setOnClickListener { actionListener?.onDeleteAllDisabledProduct() }
    }

}