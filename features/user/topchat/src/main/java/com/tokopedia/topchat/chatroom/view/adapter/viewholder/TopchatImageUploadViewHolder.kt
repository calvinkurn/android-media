package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.Gravity
import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.ImageUploadViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageUploadListener
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.getStrokeWidthSenderDimenRes
import com.tokopedia.topchat.common.util.ViewUtil
import com.tokopedia.unifycomponents.ImageUnify

class TopchatImageUploadViewHolder(itemView: View?, listener: ImageUploadListener)
    : ImageUploadViewHolder(itemView, listener) {

    override fun alwaysShowTime() = true
    override fun useWhiteReadStatus() = true
    override fun getImageId() = R.id.image
    override fun getProgressBarSendImageId() = R.id.progress_bar
    override fun getLeftActionId() = R.id.left_action
    override fun getChatBalloonId() = R.id.fl_image_container

    private val bgOpposite = ViewUtil.generateBackgroundWithShadow(
            chatBalloon,
            com.tokopedia.unifyprinciples.R.color.Unify_N0,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
            R.dimen.dp_topchat_2,
            R.dimen.dp_topchat_1,
            Gravity.CENTER,
            com.tokopedia.unifyprinciples.R.color.Unify_N0,
            getStrokeWidthSenderDimenRes()
    )
    private val bgSender = ViewUtil.generateBackgroundWithShadow(
            chatBalloon,
            com.tokopedia.unifyprinciples.R.color.Unify_G200,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
            R.dimen.dp_topchat_2,
            R.dimen.dp_topchat_1,
            Gravity.CENTER,
            com.tokopedia.unifyprinciples.R.color.Unify_G200,
            getStrokeWidthSenderDimenRes()
    )

    private val attachmentUnify get() = attachment as? ImageUnify

    private val imageRadius = itemView?.context?.resources?.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)
            ?: 0f

    override fun bind(element: ImageUploadViewModel?) {
        if (element == null) return
        super.bind(element)
        bindChatReadStatus(element)
        bindBackground(element)
    }

    private fun bindBackground(element: ImageUploadViewModel) {
        if (element.isSender) {
            chatBalloon?.background = bgSender
        } else {
            chatBalloon?.background = bgOpposite
        }
    }

    override fun bindImageAttachment(element: ImageUploadViewModel) {
        changeHourColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
        attachment?.scaleType = ImageView.ScaleType.CENTER_CROP
        if (element.isDummy) {
            setVisibility(progressBarSendImage, View.VISIBLE)
        } else {
            setVisibility(progressBarSendImage, View.GONE)
        }
        element.imageUrl?.let {
            ImageHandler.LoadImage(attachmentUnify, it)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_chat_image_upload
    }
}