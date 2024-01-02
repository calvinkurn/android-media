package com.tokopedia.shareexperience.ui.adapter.viewholder.channel

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shareexperience.databinding.ShareexperienceChannelItemBinding
import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelItemModel
import com.tokopedia.shareexperience.ui.listener.ShareExChannelListener
import com.tokopedia.utils.view.binding.viewBinding

class ShareExChannelViewHolder(
    itemView: View,
    channelListener: ShareExChannelListener
): RecyclerView.ViewHolder(itemView) {

    private val binding: ShareexperienceChannelItemBinding? by viewBinding()
    private var element: ShareExChannelItemModel? = null

    init {
        binding?.root?.setOnClickListener {
            element?.let {
                channelListener.onChannelClicked(it)
            }
        }
    }

    fun bind(element: ShareExChannelItemModel) {
        this.element = element
        binding?.shareexTvChannelName?.text = element.title
        binding?.shareexIconChannel?.setImage(newIconId = element.icon)
    }
}
