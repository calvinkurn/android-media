package com.tokopedia.addongifting.view.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.addongifting.R
import com.tokopedia.addongifting.databinding.ItemAddOnBinding
import com.tokopedia.addongifting.view.AddOnActionListener
import com.tokopedia.addongifting.view.uimodel.AddOnUiModel

class AddOnViewHolder(private val viewBinding: ItemAddOnBinding, private val listener: AddOnActionListener)
    : AbstractViewHolder<AddOnUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_add_on
    }

    override fun bind(element: AddOnUiModel) {

    }

}