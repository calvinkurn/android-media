package com.tokopedia.cart.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartDisabledReasonRevampBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.DisabledReasonHolderData
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show

class DisabledReasonViewHolder(private val binding: ItemCartDisabledReasonRevampBinding, val actionListener: ActionListener?) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        val LAYOUT = R.layout.item_cart_disabled_reason_revamp
    }

    fun bind(data: DisabledReasonHolderData) {
        binding.textDisabledTitle.text = String.format(
            itemView.context.getString(R.string.cart_label_disabled_reason_with_counter),
            data.title,
            data.productsCount
        )
        binding.textChangeAddress.isVisible = data.isShowOutOfCoverageAction
        binding.textChangeAddress.text = data.showOutOfCoverageTitle
        binding.textChangeAddress.setOnClickListener { actionListener?.onChangeAddressClicked() }
        if (data.subTitle.isNotBlank()) {
            binding.textDisabledSubTitle.text = data.subTitle
            binding.textDisabledSubTitle.show()
        } else {
            binding.textDisabledSubTitle.gone()
        }
    }
}
