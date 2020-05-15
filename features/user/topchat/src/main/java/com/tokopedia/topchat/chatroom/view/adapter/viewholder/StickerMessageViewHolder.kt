package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.transition.Slide
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.attr.StickerProfile
import com.tokopedia.topchat.chatroom.view.uimodel.StickerUiModel

class StickerMessageViewHolder(itemView: View?) : BaseChatViewHolder<StickerUiModel>(itemView) {

    private var statusFooter: LinearLayout? = itemView?.findViewById(R.id.ll_footer)
    private var stickerImage: ImageView? = itemView?.findViewById(R.id.iv_sticker_message)

    override fun alwaysShowTime(): Boolean = true

    override fun bind(message: StickerUiModel?) {
        if (message == null) return
        super.bind(message)
        bindStickerImage(message.sticker)
        bindChatReadStatus(message)
        alignLayout(message)
    }

    private fun bindStickerImage(sticker: StickerProfile) {
        ImageHandler.LoadImage(stickerImage, sticker.imageUrl)
    }

    private fun alignLayout(message: StickerUiModel) {
        if (message.isSender) {
            setLayoutGravity(stickerImage, Gravity.END)
            setLayoutGravity(statusFooter, Gravity.END)
        } else {
            setLayoutGravity(stickerImage, Gravity.START)
            setLayoutGravity(statusFooter, Gravity.START)
        }
    }

    private fun setLayoutGravity(view: View?, @Slide.GravityFlag gravity: Int) {
        view?.layoutParams?.let { lp ->
            (lp as LinearLayout.LayoutParams).apply {
                this.gravity = gravity
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_sticker_message
    }
}