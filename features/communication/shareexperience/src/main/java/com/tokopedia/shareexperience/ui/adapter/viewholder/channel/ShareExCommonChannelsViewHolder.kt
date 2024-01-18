package com.tokopedia.shareexperience.ui.adapter.viewholder.channel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shareexperience.R
import com.tokopedia.shareexperience.databinding.ShareexperienceCommonChannelsItemBinding
import com.tokopedia.shareexperience.ui.model.channel.ShareExCommonChannelUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShareExCommonChannelsViewHolder(
    itemView: View
) : AbstractViewHolder<ShareExCommonChannelUiModel>(itemView) {

    private val binding: ShareexperienceCommonChannelsItemBinding? by viewBinding()

    override fun bind(element: ShareExCommonChannelUiModel) {
        binding?.shareexRvChannelCommon?.updateData(element.commonChannel)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.shareexperience_common_channels_item
    }
}
