package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.image.ImageLoader
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.Sticker

class StickerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var stickerImage: ImageView? = itemView.findViewById(R.id.iv_sticker)

    fun bind(sticker: Sticker) {
        bindStickerImage(sticker)
    }

    private fun bindStickerImage(sticker: Sticker) {
        ImageLoader.LoadImage(stickerImage, sticker.imageUrl)
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_sticker
        fun create(parent: ViewGroup): StickerViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(LAYOUT, parent, false)
            return StickerViewHolder(view)
        }
    }
}