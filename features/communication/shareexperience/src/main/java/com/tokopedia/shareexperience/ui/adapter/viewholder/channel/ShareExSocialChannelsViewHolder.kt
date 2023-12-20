package com.tokopedia.shareexperience.ui.adapter.viewholder.channel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shareexperience.R
import com.tokopedia.shareexperience.databinding.ShareexperienceSocialChannelsItemBinding
import com.tokopedia.shareexperience.ui.model.channel.ShareExSocialChannelUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShareExSocialChannelsViewHolder(
    itemView: View
) : AbstractViewHolder<ShareExSocialChannelUiModel>(itemView) {

    private val binding: ShareexperienceSocialChannelsItemBinding? by viewBinding()

    override fun bind(element: ShareExSocialChannelUiModel) {
        binding?.shareexRvChannelSocial?.updateData(element.socialChannel)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.shareexperience_social_channels_item
    }
}
