package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.graphics.Color
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.ImageUploadViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageUploadListener
import com.tokopedia.topchat.R

class TopchatImageUploadViewHolder(itemView: View?, listener: ImageUploadListener)
    : ImageUploadViewHolder(itemView, listener) {

    override fun alwaysShowTime() = true
    override fun useWhiteReadStatus() = true
    override fun getDateId() = R.id.tvDate
    override fun getImageId() = R.id.image
    override fun getChatNameId() = R.id.name
    override fun getLabelId() = R.id.label
    override fun getDotId() = R.id.dot
    override fun getProgressBarSendImageId() = R.id.progress_bar
    override fun getLeftActionId() = R.id.left_action
    override fun getReadStatusId() = R.id.chat_status
    override fun getChatBalloonId() = R.id.fl_image_container

    override fun bind(element: ImageUploadViewModel?) {
        if (element == null) return
        super.bind(element)
        bindChatReadStatus(element)
    }

    override fun bindImageAttachment(element: ImageUploadViewModel) {
        changeHourColor(Color.WHITE)
        if (element.isDummy) {
            setVisibility(progressBarSendImage, View.VISIBLE)
            ImageHandler.loadImageRounded2(
                    itemView.context,
                    attachment,
                    element.imageUrl
            )
        } else {
            setVisibility(progressBarSendImage, View.GONE)
            ImageHandler.loadImageRounded2(
                    itemView.context,
                    attachment,
                    element.imageUrl
            )
        }
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_chat_image_upload
    }
}