package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.ImageUploadViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageUploadListener
import com.tokopedia.topchat.R

class TopchatImageUploadViewHolder(itemView: View?, listener: ImageUploadListener?)
    : ImageUploadViewHolder(itemView, listener) {

    override fun bind(element: ImageUploadViewModel?) {
        if (element == null) return
        super.bind(element)
        bindChatReadStatus(element)
    }

    override fun bindImageAttachment(element: ImageUploadViewModel) {
        if (element.isDummy) {
            setVisibility(progressBarSendImage, View.VISIBLE)
            ImageHandler.loadImageBlur(
                    itemView.context,
                    attachment,
                    element.imageUrl
            )
        } else {
            setVisibility(progressBarSendImage, View.GONE)
            ImageHandler.loadImageBlur(
                    itemView.context,
                    attachment,
                    element.imageUrl
            )
        }
    }

    override fun alignHour(alignment: Int, hour: TextView?) {}

    override fun alwaysShowTime(): Boolean {
        return true
    }

    override fun getDateId(): Int {
        return R.id.tvDate
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_chat_image_upload
    }
}