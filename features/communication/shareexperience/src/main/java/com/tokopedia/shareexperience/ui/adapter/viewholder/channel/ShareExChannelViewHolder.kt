package com.tokopedia.shareexperience.ui.adapter.viewholder.channel

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shareexperience.databinding.ShareexperienceChannelItemBinding
import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelItemModel
import com.tokopedia.utils.view.binding.viewBinding

class ShareExChannelViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val binding: ShareexperienceChannelItemBinding? by viewBinding()

    fun bind(model: ShareExChannelItemModel) {
        binding?.shareexTvChannelName?.text = model.title
        binding?.shareexIconChannel?.setImage(newIconId = model.icon)
    }
}
