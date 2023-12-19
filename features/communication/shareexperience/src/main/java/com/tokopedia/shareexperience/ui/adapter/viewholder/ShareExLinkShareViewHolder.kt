package com.tokopedia.shareexperience.ui.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shareexperience.ui.model.ShareExLinkShareUiModel
import com.tokopedia.shareexperience.R
import com.tokopedia.shareexperience.databinding.ShareexperienceShareLinkItemBinding
import com.tokopedia.utils.view.binding.viewBinding

class ShareExLinkShareViewHolder(
    itemView: View
): AbstractViewHolder<ShareExLinkShareUiModel>(itemView) {

    private val binding: ShareexperienceShareLinkItemBinding? by viewBinding()

    override fun bind(element: ShareExLinkShareUiModel) {
        binding?.shareexTvTitleLink?.text = element.title
        binding?.shareexTvCommisionLink?.text = element.commissionText
        binding?.shareexTvLink?.text = element.link
        binding?.shareexIvImageThumbnailLink?.loadImage(element.imageThumbnailUrl)
        binding?.shareexLabelLink?.text = element.label
        binding?.shareexTvDate?.text = element.date
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.shareexperience_share_link_item
    }
}
