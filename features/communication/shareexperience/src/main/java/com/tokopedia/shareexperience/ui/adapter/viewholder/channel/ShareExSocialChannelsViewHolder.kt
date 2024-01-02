package com.tokopedia.shareexperience.ui.adapter.viewholder.channel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.shareexperience.R
import com.tokopedia.shareexperience.databinding.ShareexperienceSocialChannelsItemBinding
import com.tokopedia.shareexperience.ui.listener.ShareExChannelListener
import com.tokopedia.shareexperience.ui.model.channel.ShareExSocialChannelUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShareExSocialChannelsViewHolder(
    itemView: View,
    channelListener: ShareExChannelListener
) : AbstractViewHolder<ShareExSocialChannelUiModel>(itemView) {

    private val binding: ShareexperienceSocialChannelsItemBinding? by viewBinding()

    init {
        binding?.shareexRvChannelSocial?.setChannelListener(channelListener)
    }

    override fun bind(element: ShareExSocialChannelUiModel) {
        bindSocialChannel(element)
        bindDescription(element)
    }

    private fun bindSocialChannel(element: ShareExSocialChannelUiModel) {
        binding?.shareexRvChannelSocial?.updateData(element.socialChannel.listChannel)
    }

    private fun bindDescription(element: ShareExSocialChannelUiModel) {
        binding?.shareexTvChannelSocial?.text = element.socialChannel.description
        binding?.shareexTvChannelSocial?.showWithCondition(
            element.socialChannel.description.isNotBlank()
        )
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.shareexperience_social_channels_item
    }
}
