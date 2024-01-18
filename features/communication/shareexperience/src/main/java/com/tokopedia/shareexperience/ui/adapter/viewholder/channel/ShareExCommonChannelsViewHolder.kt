package com.tokopedia.shareexperience.ui.adapter.viewholder.channel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.shareexperience.R
import com.tokopedia.shareexperience.databinding.ShareexperienceCommonChannelsItemBinding
import com.tokopedia.shareexperience.ui.listener.ShareExChannelListener
import com.tokopedia.shareexperience.ui.model.channel.ShareExCommonChannelUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShareExCommonChannelsViewHolder(
    itemView: View,
    channelListener: ShareExChannelListener
) : AbstractViewHolder<ShareExCommonChannelUiModel>(itemView) {

    private val binding: ShareexperienceCommonChannelsItemBinding? by viewBinding()

    init {
        binding?.shareexRvChannelCommon?.setChannelListener(channelListener)
    }

    override fun bind(element: ShareExCommonChannelUiModel) {
        bindCommonChannel(element)
        bindDescription(element)
    }

    private fun bindCommonChannel(element: ShareExCommonChannelUiModel) {
        binding?.shareexRvChannelCommon?.updateData(element.commonChannel.listChannel)
    }

    private fun bindDescription(element: ShareExCommonChannelUiModel) {
        binding?.shareexTvChannelCommon?.text = element.commonChannel.description
        binding?.shareexTvChannelCommon?.showWithCondition(
            element.commonChannel.description.isNotBlank()
        )
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.shareexperience_common_channels_item
    }
}
