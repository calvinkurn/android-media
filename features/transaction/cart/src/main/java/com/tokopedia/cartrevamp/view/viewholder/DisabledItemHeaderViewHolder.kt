package com.tokopedia.cartrevamp.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartDisabledHeaderRevampBinding
import com.tokopedia.cartrevamp.view.ActionListener
import com.tokopedia.cartrevamp.view.uimodel.DisabledItemHeaderHolderData
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible

/**
 * Created by Irfan Khoirul on 2019-10-16.
 */
class DisabledItemHeaderViewHolder(
    private val binding: ItemCartDisabledHeaderRevampBinding,
    val actionListener: ActionListener?
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        val LAYOUT = R.layout.item_cart_disabled_header_revamp
    }

    fun bind(
        data: DisabledItemHeaderHolderData
    ) {
        val disabledLabelText = String.format(
            itemView.context.getString(R.string.label_delete_with_amount),
            data.disabledItemCount
        )
        binding.textDisabledItemCount.text =
            itemView.context.getString(R.string.label_cannot_be_processed_no_value)
        binding.textDelete.text = disabledLabelText
        binding.textDelete.setOnClickListener { actionListener?.onDeleteAllDisabledProduct() }

        if (data.isDividerShown) {
            binding.headerDivider.visible()
        } else {
            binding.headerDivider.gone()
        }
    }
}
