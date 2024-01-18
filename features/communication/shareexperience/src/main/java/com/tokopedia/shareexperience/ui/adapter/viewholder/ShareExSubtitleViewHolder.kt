package com.tokopedia.shareexperience.ui.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shareexperience.databinding.ShareexperienceSubtitleItemBinding
import com.tokopedia.shareexperience.ui.model.ShareExSubtitleUiModel
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.shareexperience.R

class ShareExSubtitleViewHolder(
    itemView: View
) : AbstractViewHolder<ShareExSubtitleUiModel>(itemView) {

    private val binding by viewBinding<ShareexperienceSubtitleItemBinding>()
    override fun bind(element: ShareExSubtitleUiModel) {
        binding?.shareexTvSubtitle?.text = element.text
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.shareexperience_subtitle_item
    }
}
