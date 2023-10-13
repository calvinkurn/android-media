package com.tokopedia.chatbot.view.adapter.viewholder

import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.tokopedia.abstraction.common.utils.network.AuthUtil
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.chat_common.view.adapter.viewholder.ImageUploadViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageUploadListener
import com.tokopedia.chatbot.ChatbotConstant.AttachmentType.TYPE_SECURE_IMAGE_UPLOAD
import com.tokopedia.chatbot.ChatbotConstant.SecureImageUpload.AUTHORIZATION
import com.tokopedia.chatbot.ChatbotConstant.SecureImageUpload.CONTENT_TYPE
import com.tokopedia.chatbot.ChatbotConstant.SecureImageUpload.DATE_FORMAT
import com.tokopedia.chatbot.ChatbotConstant.SecureImageUpload.POST
import com.tokopedia.chatbot.ChatbotConstant.SecureImageUpload.TKPD_USERID
import com.tokopedia.chatbot.ChatbotConstant.SecureImageUpload.X_APP_VERSION
import com.tokopedia.chatbot.ChatbotConstant.SecureImageUpload.X_DEVICE
import com.tokopedia.chatbot.ChatbotConstant.SecureImageUpload.X_USER_ID
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.util.ChatBotTimeConverter
import com.tokopedia.chatbot.util.SecureImageUploadUrl
import com.tokopedia.chatbot.util.ViewUtil
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.network.authentication.AuthHelper
import com.tokopedia.network.utils.ThemeUtils
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.user.session.UserSessionInterface

class ChatbotImageUploadViewHolder(
    itemView: View?,
    private val listener: ImageUploadListener,
    private val userSession: UserSessionInterface
) :
    ImageUploadViewHolder(itemView, listener) {

    override fun alwaysShowTime() = true
    override fun useWhiteReadStatus() = true
    override fun getImageId() = R.id.image
    override fun getProgressBarSendImageId() = R.id.progress_bar
    override fun getLeftActionId() = R.id.left_action
    override fun getChatBalloonId() = R.id.fl_image_container
    override fun getReadStatusId() = com.tokopedia.chat_common.R.id.chat_status
    private val datContainer: CardView? = itemView?.findViewById(R.id.dateContainer)

    private val cancelUpload = itemView?.findViewById<ImageView>(R.id.progress_cross)

    private val bgSender = ViewUtil.generateBackgroundWithShadow(
        chatBalloon,
        com.tokopedia.unifyprinciples.R.color.Unify_GN100,
        com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        com.tokopedia.unifyprinciples.R.color.Unify_NN950_20,
        R.dimen.dp_chatbot_2,
        R.dimen.dp_chatbot_1,
        Gravity.CENTER,
        R.color.chatbot_dms_stroke,
        getStrokeWidthSenderDimenRes()
    )
    private val bgOpposite = ViewUtil.generateBackgroundWithShadow(
        view = chatBalloon,
        backgroundColor = com.tokopedia.unifyprinciples.R.color.Unify_NN0,
        topLeftRadius = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        topRightRadius = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        bottomLeftRadius = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        bottomRightRadius = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        shadowColor = com.tokopedia.unifyprinciples.R.color.Unify_NN950_20,
        elevation = R.dimen.dp_chatbot_2,
        shadowRadius = R.dimen.dp_chatbot_1,
        shadowGravity = Gravity.CENTER,
        strokeColor = com.tokopedia.unifyprinciples.R.color.Unify_NN0,
        strokeWidth = getStrokeWidthSenderDimenRes()
    )

    private val attachmentUnify get() = attachment as? ImageUnify

    override fun bind(element: ImageUploadUiModel) {
        super.bind(element)
        chatStatus?.let { bindChatReadStatus(element, it) }
        bindBackground(element.isSender)
        cancelUpload?.setOnClickListener { listener.onImageUploadCancelClicked(element) }
        setHeaderDate(element)
    }

    private fun bindBackground(sender: Boolean) {
        if (sender) {
            chatBalloon?.background = bgSender
        } else {
            chatBalloon?.background = bgOpposite
        }
    }

    override fun bindClickListener(element: ImageUploadUiModel) {
        chatBalloon?.setOnClickListener { view ->
            val imageUrl = element.imageUrl.toEmptyStringIfNull()
            val replyTime = element.replyTime.toEmptyStringIfNull()
            if (imageUrl.isNotEmpty() && replyTime.isNotEmpty()) {
                listener.onImageUploadClicked(
                    imageUrl,
                    replyTime,
                    false
                )
            }
        }
    }

    override fun bindImageAttachment(element: ImageUploadUiModel) {
        changeHourColor(
            MethodChecker.getColor(
                itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN0
            )
        )
        attachment?.scaleType = ImageView.ScaleType.CENTER_CROP
        if (element.isDummy) {
            setVisibility(progressBarSendImage, View.VISIBLE)
        } else {
            setVisibility(progressBarSendImage, View.GONE)
        }
        element.imageUrl?.let { imageUrl ->
            attachmentUnify?.let { attachementUnify ->
                loadImage(
                    attachementUnify,
                    imageUrl,
                    element.attachmentType,
                    element.messageId
                )
            }
        }
    }

    private fun getStrokeWidthSenderDimenRes(): Int {
        return R.dimen.dp_chatbot_3
    }

    private fun loadImage(
        imageview: ImageView,
        url: String?,
        attachmentType: String,
        messageId: String
    ) {
        try {
            if (imageview.context != null) {
                Glide.with(imageview.context)
                    .load(getGlideUrl(messageId, attachmentType, url, userSession))
                    .fitCenter()
                    .dontAnimate()
                    .placeholder(com.tokopedia.resources.common.R.drawable.chatbot_image_placeloader)
                    .error(com.tokopedia.abstraction.R.drawable.error_drawable)
                    .into(imageview)
            }
        } catch (e: Exception) {
            if (imageview.context != null) {
                imageview.setImageDrawable(
                    ContextCompat.getDrawable(
                        imageview.context,
                        com.tokopedia.resources.common.R.drawable.chatbot_image_placeloader
                    )
                )
            }
        }
    }

    private fun getGlideUrl(
        messageId: String,
        attachmentType: String,
        url: String?,
        userSession: UserSessionInterface
    ): GlideUrl {
        val map = AuthHelper.getDefaultHeaderMap(
            path = SecureImageUploadUrl.getUploadSecureUrl(),
            strParam = messageId,
            method = POST,
            contentType = CONTENT_TYPE,
            authKey = AuthUtil.KEY.KEY_WSV4,
            dateFormat = DATE_FORMAT,
            userSession = userSession,
            theme = ThemeUtils.getHeader(itemView.context)
        )
        return if (attachmentType == TYPE_SECURE_IMAGE_UPLOAD) {
            GlideUrl(
                url,
                LazyHeaders.Builder()
                    .addHeader(AUTHORIZATION, map[AUTHORIZATION] ?: "")
                    .addHeader(TKPD_USERID, map[X_USER_ID] ?: "")
                    .addHeader(X_APP_VERSION, map[X_APP_VERSION] ?: "")
                    .addHeader(X_DEVICE, map[X_DEVICE] ?: "")
                    .build()
            )
        } else {
            GlideUrl(url)
        }
    }

    private fun bindChatReadStatus(element: ImageUploadUiModel, checkMark: ImageView) {
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

    override fun setHeaderDate(element: BaseChatUiModel) {
        if (date == null) return
        val time = element.replyTime?.let {
            ChatBotTimeConverter.settingDateIndicatorTimeAtLeastToday(
                it,
                itemView.context.getString(com.tokopedia.chat_common.R.string.chat_today_date),
                itemView.context.getString(com.tokopedia.chat_common.R.string.chat_yesterday_date)
            )
        }
        date?.text = time
        if (date != null && element.isShowDate && !TextUtils.isEmpty(time)
        ) {
            datContainer?.show()
        } else if (date != null) {
            datContainer?.hide()
        }
    }

    override val dateId: Int
        get() = R.id.date

    companion object {
        val LAYOUT = R.layout.item_chatbot_image_upload
    }
}
