package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.Sticker

class StickerViewHolder(
        itemView: View,
        private val listener: Listener?
) : RecyclerView.ViewHolder(itemView) {

    private var stickerImage: ImageView? = itemView.findViewById(R.id.iv_sticker)

    interface Listener {
        fun onClickSticker(sticker: Sticker)
    }

    fun bind(sticker: Sticker) {
        bindStickerImage(sticker)
        bindStickerImageClick(sticker)
    }

    private fun bindStickerImage(sticker: Sticker) {
        ImageHandler.loadImage(itemView.context, stickerImage, sticker.imageUrl, R.drawable.ic_loading_image)
    }

    private fun bindStickerImageClick(sticker: Sticker) {
        stickerImage?.setOnClickListener {
            listener?.onClickSticker(sticker)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_sticker
        fun create(parent: ViewGroup, listener: Listener?): StickerViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(LAYOUT, parent, false)
            return StickerViewHolder(view, listener)
        }
    }
}