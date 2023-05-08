package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcRichListCaptionItemBinding
import com.tokopedia.sellerhomecommon.presentation.model.BaseRichListItemUiModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by @ilhamsuaib on 06/05/23.
 */

class RichListCaptionViewHolder(
    itemView: View
) : AbstractViewHolder<BaseRichListItemUiModel.CaptionItemUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_rich_list_caption_item
    }

    private val binding: ShcRichListCaptionItemBinding? by viewBinding()

    override fun bind(element: BaseRichListItemUiModel.CaptionItemUiModel) {
        binding?.run {
            tvShcCaption.text = element.caption.parseAsHtml()
        }
    }
}