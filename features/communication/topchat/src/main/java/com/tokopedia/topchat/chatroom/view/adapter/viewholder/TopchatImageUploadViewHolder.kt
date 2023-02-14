package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_IMAGE_UPLOAD_SECURE
import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.chat_common.data.BaseChatUiModel.Companion.SENDING_TEXT
import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.chat_common.view.adapter.viewholder.ImageUploadViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageUploadListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadSecureImage
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.util.LongClickMenuItemGenerator
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.CommonViewHolderListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.Payload
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.getStrokeWidthSenderDimenRes
import com.tokopedia.topchat.chatroom.view.custom.message.ReplyBubbleAreaMessage
import com.tokopedia.topchat.common.util.ViewUtil
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.user.session.UserSessionInterface

class TopchatImageUploadViewHolder(
    itemView: View?,
    private val listener: ImageUploadListener,
    private val replyBubbleListener: ReplyBubbleAreaMessage.Listener,
    private val commonListener: CommonViewHolderListener,
    private val userSession: UserSessionInterface
) : ImageUploadViewHolder(itemView, listener) {

    private val viewContainer: LinearLayout? = itemView?.findViewById(R.id.ll_image_container)
    private var replyBubbleArea: ReplyBubbleAreaMessage? = itemView?.findViewById(
        R.id.rba_image
    )
    private var msgOffsetLine: View? = itemView?.findViewById(
        R.id.v_image_offset
    )

    override fun alwaysShowTime() = true
    override fun useWhiteReadStatus() = true
    override fun getImageId() = R.id.image
    override fun getProgressBarSendImageId() = R.id.progress_bar
    override fun getLeftActionId() = R.id.left_action
    override fun getChatBalloonId() = R.id.fl_image_container

    private val bgOpposite = ViewUtil.generateBackgroundWithShadow(
        view = chatBalloon,
        backgroundColor = com.tokopedia.unifyprinciples.R.color.Unify_Background,
        topLeftRadius = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        topRightRadius = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        bottomLeftRadius = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        bottomRightRadius = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        shadowColor = com.tokopedia.unifyprinciples.R.color.Unify_NN600,
        elevation = R.dimen.dp_topchat_2,
        shadowRadius = R.dimen.dp_topchat_1,
        shadowGravity = Gravity.CENTER,
        strokeColor = com.tokopedia.unifyprinciples.R.color.Unify_Background,
        strokeWidth = getStrokeWidthSenderDimenRes()
    )
    private val bgSender = ViewUtil.generateBackgroundWithShadow(
        view = chatBalloon,
        backgroundColor = com.tokopedia.unifyprinciples.R.color.Unify_GN50,
        topLeftRadius = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        topRightRadius = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        bottomLeftRadius = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        bottomRightRadius = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        shadowColor = com.tokopedia.unifyprinciples.R.color.Unify_NN600,
        elevation = R.dimen.dp_topchat_2,
        shadowRadius = R.dimen.dp_topchat_1,
        shadowGravity = Gravity.CENTER,
        strokeColor = com.tokopedia.unifyprinciples.R.color.Unify_GN50,
        strokeWidth = getStrokeWidthSenderDimenRes()
    )

    private val attachmentUnify get() = attachment as? ImageUnify

    override fun bind(element: ImageUploadUiModel?, payloads: MutableList<Any>) {
        if (payloads.isEmpty() || element == null) return
        if (payloads.first() == Payload.REBIND) {
            bind(element)
        }
    }

    override fun bind(element: ImageUploadUiModel) {
        super.bind(element)
        hideReadStatusIfRetry(element)
        bindBackground(element)
        bindLoadingAnimation(element)
        bindReplyBubbleListener()
        bindReplyReference(element)
        bindMsgOffsetLine(element)
        bindLongClick(element)
    }

    override fun setBottomHour(element: BaseChatUiModel) {
        if (element is ImageUploadUiModel && element.isDummy) {
            hour?.text = SENDING_TEXT
            hour?.show()
        } else {
            super.setBottomHour(element)
        }
    }

    private fun bindLongClick(element: ImageUploadUiModel) {
        attachmentUnify?.setOnLongClickListener {
            if (element.isSender) {
                val menus = LongClickMenuItemGenerator.createLongClickMenuUploadImageBubble()
                commonListener.showMsgMenu(
                    element, "", menus
                )
            }
            true
        }
    }

    override fun bindClickListener(element: ImageUploadUiModel) {
        attachmentUnify?.setOnClickListener { view ->
            if (element.replyTime != null) {
                val imageSecureUrl = element.imageSecureUrl.toEmptyStringIfNull()
                val imageUrl = element.imageUrl.toEmptyStringIfNull()
                val replyTime = element.replyTime.toEmptyStringIfNull()
                if (element.attachmentType == TYPE_IMAGE_UPLOAD_SECURE
                    && imageSecureUrl.isNotEmpty()) {
                    listener.onImageUploadClicked(
                        imageSecureUrl, replyTime, true
                    )
                } else if (imageUrl.isNotEmpty()) {
                    listener.onImageUploadClicked(
                        imageUrl, replyTime, false
                    )
                }
            }
        }
    }

    override fun setChatLeft(chatBalloon: View?) {
        replyBubbleArea?.alignLeft()
        viewContainer?.gravity = Gravity.START
    }

    override fun setChatRight(chatBalloon: View?) {
        replyBubbleArea?.alignRight()
        viewContainer?.gravity = Gravity.END
    }

    private fun bindMsgOffsetLine(element: ImageUploadUiModel) {
        val msgOffsetLp = msgOffsetLine?.layoutParams as? ViewGroup.MarginLayoutParams
        val bottomMargin: Int = if (element.hasReplyBubble()) {
            itemView.context.resources.getDimension(R.dimen.dp_topchat_20).toInt()
        } else {
            0
        }
        msgOffsetLp?.bottomMargin = bottomMargin
        msgOffsetLine?.layoutParams = msgOffsetLp
    }

    private fun bindReplyBubbleListener() {
        replyBubbleArea?.setReplyListener(replyBubbleListener)
    }

    private fun bindReplyReference(element: ImageUploadUiModel) {
        replyBubbleArea?.bindReplyData(element)
    }

    private fun hideReadStatusIfRetry(element: ImageUploadUiModel) {
        if (element.isRetry) {
            chatReadStatus?.hide()
        }
    }

    private fun bindLoadingAnimation(element: ImageUploadUiModel) {
        if (ViewUtil.areSystemAnimationsEnabled(itemView.context) && element.isDummy) {
            progressBarSendImage?.show()
        } else {
            progressBarSendImage?.hide()
        }
    }

    private fun bindBackground(element: ImageUploadUiModel) {
        if (element.isSender) {
            chatBalloon?.background = bgSender
        } else {
            chatBalloon?.background = bgOpposite
        }
    }

    override fun bindImageAttachment(element: ImageUploadUiModel) {
        changeHourColor(
            MethodChecker.getColor(
                itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_Background
            )
        )
        attachment?.scaleType = ImageView.ScaleType.CENTER_CROP
        if (element.isDummy) {
            setVisibility(progressBarSendImage, View.VISIBLE)
        } else {
            setVisibility(progressBarSendImage, View.GONE)
        }
        if (element.attachmentType == TYPE_IMAGE_UPLOAD_SECURE) {
            attachmentUnify?.loadSecureImage(element.imageSecureUrl, userSession)
        } else {
            attachmentUnify?.loadImage(element.imageUrl)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_chat_image_upload
    }
}
