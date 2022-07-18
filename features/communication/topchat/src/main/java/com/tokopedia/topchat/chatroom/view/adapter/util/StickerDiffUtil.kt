package com.tokopedia.topchat.chatroom.view.adapter.util

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.Sticker

class StickerDiffUtil(
        private val oldStickers: List<Sticker>,
        private val newStickers: List<Sticker>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldStickers.size

    override fun getNewListSize(): Int = newStickers.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldStickers[oldItemPosition]
        val new = newStickers[newItemPosition]
        return old.imageUrl == new.imageUrl
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldStickers[oldItemPosition]
        val new = newStickers[newItemPosition]
        return old.imageUrl == new.imageUrl
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }

}