package com.tokopedia.topads.edit.view.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.edit.databinding.TopadsEditItemEditAdGroupBinding
import com.tokopedia.topads.edit.view.model.edit.EditAdGroupItemUiModel
import com.tokopedia.topads.edit.R

class EditAdGroupItemViewHolder(private val viewBinding: TopadsEditItemEditAdGroupBinding) : AbstractViewHolder<EditAdGroupItemUiModel>(viewBinding.root) {

    override fun bind(element: EditAdGroupItemUiModel) {
        viewBinding.editAdItemTitle.text = element.title
        viewBinding.editAdItemSubtitle.text = element.subtitle
        viewBinding.root.setOnClickListener {
            element.clickListener()
        }
    }

    companion object {
        val LAYOUT = R.layout.topads_edit_item_edit_ad_group
    }

}
