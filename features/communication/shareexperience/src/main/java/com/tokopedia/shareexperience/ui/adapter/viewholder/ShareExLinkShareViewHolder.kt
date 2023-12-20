package com.tokopedia.shareexperience.ui.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shareexperience.ui.model.ShareExLinkShareUiModel
import com.tokopedia.shareexperience.R
import com.tokopedia.shareexperience.databinding.ShareexperienceShareLinkItemBinding
import com.tokopedia.utils.htmltags.HtmlUtil
import com.tokopedia.utils.view.binding.viewBinding

class ShareExLinkShareViewHolder(
    itemView: View
): AbstractViewHolder<ShareExLinkShareUiModel>(itemView) {

    private val binding: ShareexperienceShareLinkItemBinding? by viewBinding()

    override fun bind(element: ShareExLinkShareUiModel) {
        bindTitle(element)
        bindCommissionHtml(element)
        bindLink(element)
        bindImage(element)
        bindLabel(element)
        bindDate(element)
    }

    private fun bindTitle(element: ShareExLinkShareUiModel) {
        binding?.shareexTvTitleLink?.text = element.title
    }

    private fun bindImage(element: ShareExLinkShareUiModel) {
        binding?.shareexIvImageThumbnailLink?.loadImage(element.imageThumbnailUrl)
    }

    private fun bindLink(element: ShareExLinkShareUiModel) {
        binding?.shareexTvLink?.text = element.link
    }

    private fun bindCommissionHtml(element: ShareExLinkShareUiModel) {
        val htmlCommissionText = HtmlUtil.fromHtml(element.commissionText)
        binding?.shareexTvCommisionLink?.text = htmlCommissionText
        binding?.shareexTvCommisionLink?.showWithCondition(htmlCommissionText.isNotBlank())
    }

    private fun bindLabel(element: ShareExLinkShareUiModel) {
        binding?.shareexLabelLink?.text = element.label
        binding?.shareexLabelLink?.showWithCondition(element.label.isNotBlank())
    }

    private fun bindDate(element: ShareExLinkShareUiModel) {
        binding?.shareexTvDate?.text = element.date
        binding?.shareexTvDate?.showWithCondition(element.date.isNotBlank())
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.shareexperience_share_link_item
    }
}
