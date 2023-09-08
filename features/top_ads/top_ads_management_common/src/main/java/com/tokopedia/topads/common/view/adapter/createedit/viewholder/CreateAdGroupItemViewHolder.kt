package com.tokopedia.topads.common.view.adapter.createedit.viewholder

import android.util.TypedValue
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.common.R
import com.tokopedia.topads.common.databinding.TopadsCreateItemAdGroupBinding
import com.tokopedia.topads.common.domain.model.createedit.CreateAdGroupItemUiModel

class CreateAdGroupItemViewHolder(private val viewBinding: TopadsCreateItemAdGroupBinding)
    : AbstractViewHolder<CreateAdGroupItemUiModel>(viewBinding.root) {

    override fun bind(element: CreateAdGroupItemUiModel) {
        viewBinding.adItemTitle.text = element.title
        if (element.subtitle.isEmpty()) viewBinding.adItemSubtitle.hide() else {
            viewBinding.adItemSubtitle.show()
            viewBinding.adItemSubtitle.text = element.subtitle
        }
        if (element.isItemClickable) viewBinding.iconUnify.show() else viewBinding.iconUnify.hide()
        if (element.isManualAdBid) {
            viewBinding.adItemSubtitleOne.show()
            viewBinding.adItemSubtitleOneValue.show()
            viewBinding.adItemSubtitleOne.text = element.subtitleOne
            viewBinding.adItemSubtitleOneValue.text = element.subtitleOneValue
            if (element.subtitleTwo.isEmpty() || element.subtitleTwoValue.isEmpty()) {
                viewBinding.adItemSubtitleTwo.hide()
                viewBinding.adItemSubtitleTwoValue.hide()
            } else {
                viewBinding.adItemSubtitleTwo.show()
                viewBinding.adItemSubtitleTwoValue.show()
                viewBinding.adItemSubtitleTwo.text = element.subtitleTwo
                viewBinding.adItemSubtitleTwoValue.text = element.subtitleTwoValue
            }
        } else {
            viewBinding.adItemSubtitleOne.hide()
            viewBinding.adItemSubtitleOneValue.hide()
            viewBinding.adItemSubtitleTwo.hide()
            viewBinding.adItemSubtitleTwoValue.hide()
        }
        if (element.isEditable) {
            viewBinding.iconUnify.setImage(IconUnify.EDIT)
            viewBinding.iconUnify.rotation = 0f
        }

        viewBinding.iconUnify.setOnClickListener {
            element.clickListener.invoke(element.tag)
        }

    }

    companion object {
        val LAYOUT = R.layout.topads_create_daily_budget_item_ad_group
    }

}
