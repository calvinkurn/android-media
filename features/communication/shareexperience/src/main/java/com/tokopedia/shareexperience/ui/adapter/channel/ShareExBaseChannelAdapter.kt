package com.tokopedia.shareexperience.ui.adapter.channel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.shareexperience.databinding.ShareexperienceChannelItemBinding
import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelItemModel
import com.tokopedia.shareexperience.ui.adapter.diffutil.ShareExChannelItemCallback
import com.tokopedia.shareexperience.ui.adapter.viewholder.channel.ShareExChannelViewHolder

abstract class ShareExBaseChannelAdapter(
    //TODO: Listener
): ListAdapter<ShareExChannelItemModel, ShareExChannelViewHolder>(ShareExChannelItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareExChannelViewHolder {
        return ShareExChannelViewHolder(
            ShareexperienceChannelItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root
        )
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: ShareExChannelViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    fun updateData(listModel: List<ShareExChannelItemModel>) {
        this.submitList(listModel)
    }
}
