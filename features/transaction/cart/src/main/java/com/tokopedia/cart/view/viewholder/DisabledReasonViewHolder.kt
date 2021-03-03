package com.tokopedia.cart.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartDisabledReasonBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.DisabledReasonHolderData
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show

class DisabledReasonViewHolder(private val binding: ItemCartDisabledReasonBinding, val actionListener: ActionListener?) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        val LAYOUT = R.layout.item_cart_disabled_reason
    }

    fun bind(data: DisabledReasonHolderData) {
        binding.textDisabledTitle.text = data.title
        if (data.subTitle.isNotBlank()) {
            binding.textDisabledSubTitle.text = data.subTitle
            binding.textDisabledSubTitle.show()
        } else {
            binding.textDisabledSubTitle.gone()
        }
    }

}