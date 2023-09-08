package com.tokopedia.topads.common.view.adapter.createedit.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.common.databinding.TopadsCreateEditItemEditAdGroupBinding
import com.tokopedia.topads.common.domain.model.createedit.CreateEditAdGroupItemUiModel
import com.tokopedia.topads.common.R

class CreateEditAdGroupItemViewHolder(private val viewBinding: TopadsCreateEditItemEditAdGroupBinding) : AbstractViewHolder<CreateEditAdGroupItemUiModel>(viewBinding.root) {

    override fun bind(element: CreateEditAdGroupItemUiModel) {
        viewBinding.editAdItemTitle.text = element.title
        if(element.subtitle.isEmpty()){
            viewBinding.editAdItemShimmer.show()
            viewBinding.editAdItemSubtitle.invisible()
        }else {
            viewBinding.editAdItemSubtitle.text = element.subtitle
            viewBinding.editAdItemSubtitle.show()
            viewBinding.editAdItemShimmer.hide()
        }
        viewBinding.root.setOnClickListener {
            element.clickListener()
        }
    }

    companion object {
        val LAYOUT = R.layout.topads_create_edit_item_edit_ad_group
    }

}
