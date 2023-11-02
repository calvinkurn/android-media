package com.tokopedia.topads.common.view.adapter.createedit.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.topads.common.R
import com.tokopedia.topads.common.databinding.TopadsCreateApplyItemAdGroupBinding
import com.tokopedia.topads.common.domain.model.createedit.CreateApplyAdGroupItemUiModel
import com.tokopedia.topads.common.R as topadscommonR

class CreateApplyAdGroupItemViewHolder(private val viewBinding: TopadsCreateApplyItemAdGroupBinding) : AbstractViewHolder<CreateApplyAdGroupItemUiModel>(viewBinding.root) {

    override fun bind(element: CreateApplyAdGroupItemUiModel) {
        viewBinding.createProductAdButton.text = element.title
        viewBinding.captionOne.text = MethodChecker.fromHtml(viewBinding.root.context.getString(topadscommonR.string.top_ads_create_text_caption_one))
        viewBinding.captionTwo.text = MethodChecker.fromHtml(viewBinding.root.context.getString(topadscommonR.string.top_ads_create_text_caption_two))
        viewBinding.createProductAdButton?.setOnClickListener {
            element.clickListener.invoke()
        }

    }

    companion object {
        val LAYOUT = R.layout.topads_create_apply_item_ad_group
    }

}
