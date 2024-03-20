package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.text.method.LinkMovementMethod
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gm.common.utils.SpannableUtil
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcRichListCaptionItemBinding
import com.tokopedia.sellerhomecommon.presentation.model.BaseRichListItem
import com.tokopedia.sellerhomecommon.presentation.view.viewhelper.URLSpanNoUnderline
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

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
        private const val BOLD_TEXT_REGEX = "<b>(.*?)<\\/b>|<b\\s.*?>(.*?)<\\/b>"
    }

    private val binding by lazy { ShcRichListCaptionItemBinding.bind(itemView) }

    override fun bind(element: BaseRichListItem.CaptionItemUiModel) {
        showCaption(element)
    }

    private fun showCaption(element: BaseRichListItem.CaptionItemUiModel) {
        with(binding) {
            if (element.ctaText.isNotBlank() && element.url.isNotBlank()) {
                val ctaTextColor = unifyprinciplesR.color.Unify_GN500
                val caption = SpannableUtil.createSpannableString(
                    text = element.caption.parseAsHtml().toString(),
                    highlightText = element.ctaText,
                    colorId = root.context.getResColor(ctaTextColor),
                    boldTextList = getBoldText(element.caption),
                    isBold = true
                ) {
                    onCtaClicked(element.url)
                }
                tvShcCaption.run {
                    text = caption
                    movementMethod = LinkMovementMethod.getInstance()
                }
            } else {
                tvShcCaption.text = element.caption.parseAsHtml()
                tvShcCaption.setOnClickListener {
                    onCtaClicked(element.url)
                }
                URLSpanNoUnderline.stripUnderlines(tvShcCaption)
            }
        }
    }

    private fun getBoldText(caption: String): List<String> {
        val regex = BOLD_TEXT_REGEX.toRegex()
        val matches = regex.findAll(caption)
        val results = matches.toList().map { match ->
            val values = match.groupValues
            values.lastOrNull { it.isNotBlank() }.orEmpty()
        }
        return results
    }
}
