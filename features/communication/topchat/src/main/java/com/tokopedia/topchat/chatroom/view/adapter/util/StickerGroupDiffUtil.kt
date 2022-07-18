package com.tokopedia.topchat.chatroom.view.adapter.util

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.StickerGroup

class StickerGroupDiffUtil(
        private val oldGroup: List<StickerGroup>,
        private val newGroup: List<StickerGroup>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldGroup.size

    override fun getNewListSize(): Int = newGroup.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldGroup = oldGroup[oldItemPosition]
        val newGroup = newGroup[newItemPosition]
        return oldGroup.groupUUID == newGroup.groupUUID && oldGroup.lastUpdate == newGroup.lastUpdate
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldGroup = oldGroup[oldItemPosition]
        val newGroup = newGroup[newItemPosition]
        return oldGroup.groupUUID == newGroup.groupUUID && oldGroup.lastUpdate == newGroup.lastUpdate
    }
}