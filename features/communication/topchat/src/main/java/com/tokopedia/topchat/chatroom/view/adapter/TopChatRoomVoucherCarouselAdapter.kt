package com.tokopedia.topchat.chatroom.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.topchat.chatroom.view.adapter.diffutil.TopChatRoomVoucherCarouselItemCallback
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.voucher.TopChatRoomVoucherItemViewHolder
import com.tokopedia.topchat.chatroom.view.listener.TopChatRoomVoucherListener
import com.tokopedia.topchat.chatroom.view.uimodel.TopChatRoomBroadcastUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.voucher.TopChatRoomVoucherUiModel
import com.tokopedia.topchat.databinding.TopchatChatroomVoucherItemBinding

class TopChatRoomVoucherCarouselAdapter(
    private val broadcastUiModel: TopChatRoomBroadcastUiModel,
    private val listener: TopChatRoomVoucherListener
): ListAdapter<TopChatRoomVoucherUiModel, TopChatRoomVoucherItemViewHolder>(
    TopChatRoomVoucherCarouselItemCallback()
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TopChatRoomVoucherItemViewHolder {
        val binding = TopchatChatroomVoucherItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TopChatRoomVoucherItemViewHolder(binding.root, listener)
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: TopChatRoomVoucherItemViewHolder, position: Int) {
        holder.bind(currentList[position], broadcastUiModel)
    }

    fun updateData(newList: List<TopChatRoomVoucherUiModel>) {
        this.submitList(newList)
    }

    override fun onViewRecycled(holder: TopChatRoomVoucherItemViewHolder) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }
}

