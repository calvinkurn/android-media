package com.tokopedia.topchat.chatroom.view.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.Sticker
import com.tokopedia.topchat.chatroom.view.adapter.util.StickerDiffUtil
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.StickerViewHolder

class StickerAdapter : RecyclerView.Adapter<StickerViewHolder>() {

    var stickerListener: StickerViewHolder.Listener? = null
    var stickers: List<Sticker> = emptyList()
        set(value) {
            val diffUtil = StickerDiffUtil(field, value)
            val diffResult = DiffUtil.calculateDiff(diffUtil)
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    override fun getItemCount(): Int = stickers.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StickerViewHolder {
        return StickerViewHolder.create(parent, stickerListener)
    }

    override fun onBindViewHolder(holder: StickerViewHolder, position: Int) {
        holder.bind(stickers[position])
    }

}