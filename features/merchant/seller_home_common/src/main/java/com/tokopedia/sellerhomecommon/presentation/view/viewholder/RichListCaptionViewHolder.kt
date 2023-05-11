package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.text.method.LinkMovementMethod
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gm.common.utils.PowerMerchantSpannableUtil
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcRichListCaptionItemBinding
import com.tokopedia.sellerhomecommon.presentation.model.BaseRichListItem
import com.tokopedia.sellerhomecommon.presentation.view.viewhelper.URLSpanNoUnderline

/**
 * Created by @ilhamsuaib on 06/05/23.
 */

class RichListCaptionViewHolder(
    itemView: View,
    private val onCtaClicked: (appLink: String) -> Unit
) : AbstractViewHolder<BaseRichListItem.CaptionItemUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_rich_list_caption_item
    }

    private val binding by lazy { ShcRichListCaptionItemBinding.bind(itemView) }

    override fun bind(element: BaseRichListItem.CaptionItemUiModel) {
        showCaption(element)
    }

    private fun showCaption(element: BaseRichListItem.CaptionItemUiModel) {
        with(binding) {
            if (element.ctaText.isNotBlank() && element.url.isNotBlank()) {
                val ctaTextColor = com.tokopedia.unifyprinciples.R.color.Unify_GN500
                val caption = PowerMerchantSpannableUtil.createSpannableString(
                    text = element.caption.parseAsHtml(),
                    highlightText = element.ctaText,
                    colorId = root.context.getResColor(ctaTextColor),
                    isBold = true
                ) {
                    onCtaClicked(element.url)
                }
                tvShcCaption.apply {
                    movementMethod = LinkMovementMethod.getInstance()
                    text = caption
                }
            } else {
                tvShcCaption.text = element.caption.parseAsHtml()
                URLSpanNoUnderline.stripUnderlines(tvShcCaption)
            }
        }
    }
}