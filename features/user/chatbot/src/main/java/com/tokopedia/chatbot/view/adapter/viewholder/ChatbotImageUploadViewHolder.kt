package com.tokopedia.chatbot.view.adapter.viewholder

import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.data.BaseChatViewModel
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.ImageUploadViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageUploadListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.util.ChatBotTimeConverter
import com.tokopedia.chatbot.util.ViewUtil
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.ImageUnify

class ChatbotImageUploadViewHolder(itemView: View?,
                                   private val listener: ImageUploadListener)
    : ImageUploadViewHolder(itemView, listener) {

    override fun alwaysShowTime() = true
    override fun useWhiteReadStatus() = true
    override fun getImageId() = R.id.image
    override fun getProgressBarSendImageId() = R.id.progress_bar
    override fun getLeftActionId() = R.id.left_action
    override fun getChatBalloonId() = R.id.fl_image_container
    override fun getReadStatusId() = com.tokopedia.chat_common.R.id.chat_status
    private val datContainer:CardView? = itemView?.findViewById(R.id.dateContainer)

    private val cancelUpload = itemView?.findViewById<ImageView>(R.id.progress_cross)

    private val bgSender = ViewUtil.generateBackgroundWithShadow(
            chatBalloon,
            com.tokopedia.unifyprinciples.R.color.Unify_G200,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
            com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
            R.dimen.dp_chatbot_2,
            R.dimen.dp_chatbot_1,
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
        chatStatus?.let { bindChatReadStatus(element, it) }
        bindBackground()
        cancelUpload?.setOnClickListener { listener.onImageUploadCancelClicked(element) }
        setHeaderDate(element)
    }

    private fun bindBackground() {
        chatBalloon?.background = bgSender
    }

    override fun bindImageAttachment(element: ImageUploadViewModel) {
        changeHourColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
        attachment?.scaleType = ImageView.ScaleType.CENTER_CROP
        if (element.isDummy) {
            setVisibility(progressBarSendImage, View.VISIBLE)
        } else {
            setVisibility(progressBarSendImage, View.GONE)
        }
        element.imageUrl?.let { imageUrl ->
            attachmentUnify?.let { attachementUnify -> LoadImage(attachementUnify, imageUrl) }
        }
    }

    fun getStrokeWidthSenderDimenRes(): Int {
        return R.dimen.dp_chatbot_3
    }

    fun LoadImage(imageview: ImageView, url: String?) {
        try {
            if (imageview.context != null) {
                Glide.with(imageview.context)
                        .load(url)
                        .fitCenter()
                        .dontAnimate()
                        .placeholder(R.drawable.chatbot_image_placeloader)
                        .error(com.tokopedia.abstraction.R.drawable.error_drawable)
                        .into(imageview)
            }
        } catch (e: Exception) {
            if (imageview.context != null) {
                Glide.with(imageview.context)
                        .asBitmap()
                        .load(R.drawable.chatbot_image_placeloader)
                        .dontAnimate()
                        .into(imageview)
            }
        }
    }

    private fun bindChatReadStatus(element: ImageUploadViewModel, checkMark: ImageView) {
        if (element.isShowTime && element.isSender) {
            checkMark.show()
            val imageResource = when {
                element.isDummy -> com.tokopedia.chat_common.R.drawable.ic_chatcommon_check_rounded_grey
                else -> com.tokopedia.chat_common.R.drawable.ic_chatcommon_check_read_rounded_green
            }
            val drawable = MethodChecker.getDrawable(checkMark.context, imageResource)
            checkMark.setImageDrawable(drawable)
        } else {
            checkMark.gone()
        }
    }

    override fun setHeaderDate(element: BaseChatViewModel?) {
        if (date == null) return
        val time = element?.replyTime?.let {
            ChatBotTimeConverter.getDateIndicatorTime(
                    it,
                    itemView.context.getString(com.tokopedia.chat_common.R.string.chat_today_date),
                    itemView.context.getString(com.tokopedia.chat_common.R.string.chat_yesterday_date))
        }
        date.text = time
        if (date != null && element?.isShowDate ==true
                && !TextUtils.isEmpty(time)) {
            datContainer?.show()
        } else if (date != null) {
            datContainer?.hide()
        }
    }

    override fun getDateId(): Int = R.id.date

    companion object {
        val LAYOUT = R.layout.item_chatbot_chat_image_upload
    }
}