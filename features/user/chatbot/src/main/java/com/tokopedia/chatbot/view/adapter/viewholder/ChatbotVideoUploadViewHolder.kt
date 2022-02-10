package com.tokopedia.chatbot.view.adapter.viewholder

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.videoupload.VideoUploadUiModel
import com.tokopedia.chatbot.util.ChatBotTimeConverter
import com.tokopedia.chatbot.util.ViewUtil
import com.tokopedia.chatbot.view.adapter.viewholder.listener.VideoUploadListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import java.util.concurrent.TimeUnit

class ChatbotVideoUploadViewHolder(
    itemView: View?,
    private val listener: VideoUploadListener,
    private val userSession: UserSessionInterface
) : BaseChatViewHolder<VideoUploadUiModel>(itemView) {

    override fun alwaysShowTime() = true
    override fun useWhiteReadStatus() = true
    fun getImageId() = R.id.image
    fun getProgressBarSendImageId() = R.id.progress_bar
    fun getLeftActionId() = R.id.left_action

    //TODO change this
    fun getReadStatusId() = com.tokopedia.chat_common.R.id.chat_status
    fun getChatBalloonId() = R.id.card_group_chat_message
    fun getVideoTotalLengthId() = R.id.video_length
    private val datContainer: CardView? = itemView?.findViewById(R.id.dateContainer)
    protected val chatBalloon: View? = itemView?.findViewById(getChatBalloonId())
    private val videoTotalLength: Typography? = itemView?.findViewById(getVideoTotalLengthId())

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

    private val imageRadius =
        itemView?.context?.resources?.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)
            ?: 0f

    override fun bind(element: VideoUploadUiModel?) {
        if (element == null) return
        super.bind(element)
        prerequisiteUISetup(element)
        setupChatBubbleAlignment(chatBalloon, element)
        bindClickListener(element)
        bindVideoAttachment(element)
        bindRetry(element)
        setVisibility(attachment, View.VISIBLE)
        chatStatus?.let { bindChatReadStatus(element, it) }
        bindBackground()
        cancelUpload?.setOnClickListener { listener.onVideoUploadCancelClicked(element) }
        setHeaderDate(element)
        setTimeData(element)
    }

    private fun setTimeData(element: VideoUploadUiModel) {
        var totalLength = element.length
        var totalLengthInString = convertDate(totalLength)
        videoTotalLength?.text = totalLengthInString

    }

    private fun convertDate(totalLength: Long): String {
        var hours = TimeUnit.MILLISECONDS.toHours(totalLength) % 24
        var minutes = TimeUnit.MILLISECONDS.toMinutes(totalLength) % 60
        var seconds = TimeUnit.MILLISECONDS.toSeconds(totalLength) % 60

        return when {
//            hours == 0L && minutes == 0L -> String.format(
//                "%02d", seconds
//            )

            hours == 0L -> String.format(
                "%02d:%02d", minutes, seconds
            )

            else ->
                String.format("%02d:%02d:%02d", hours, minutes, seconds)

        }

    }

    private fun bindBackground() {
        chatBalloon?.background = bgSender
    }

    fun bindVideoAttachment(element: VideoUploadUiModel) {
        changeHourColor(
            MethodChecker.getColor(
                itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_N0
            )
        )
        attachment?.scaleType = ImageView.ScaleType.CENTER_CROP
        if (element.isDummy) {
            setVisibility(progressBarSendImage, View.VISIBLE)
        } else {
            setVisibility(progressBarSendImage, View.GONE)
        }

        if (element.videoUrlThumbnail?.isNullOrEmpty() == true) {
            attachmentUnify?.let { attachementUnify ->
                loadPlaceHolderWhenUploadingImageWithPNG(
                    attachementUnify,
                    R.drawable.chatbot_video_placeholder
                )
            }
        } else {
            attachmentUnify?.let { attachementUnify ->
                loadThumbnail(attachementUnify, element.videoUrlThumbnail)
            }
        }
    }

    fun loadPlaceHolderWhenUploadingImageWithPNG(imageview: ImageView, placeHolder: Int) {
        try {
            if (imageview != null) {
                Glide.with(imageview.context)
                    .load(placeHolder)
                    .fitCenter()
                    .dontAnimate()
                    .placeholder(com.tokopedia.resources.common.R.drawable.chatbot_video_placeholder)
                    .error(com.tokopedia.abstraction.R.drawable.error_drawable)
                    .into(imageview)
            }
        } catch (e: Exception) {
            if (imageview.context != null) {
                imageview.setImageDrawable(
                    ContextCompat.getDrawable(
                        imageview.context,
                        com.tokopedia.resources.common.R.drawable.chatbot_video_placeholder
                    )
                )
            }
        }

    }

    fun loadThumbnail(imageview: ImageView, videoUrl: String?) {
        try {
            if (imageview != null) {
                Glide.with(imageview.context)
                    .load(videoUrl)
                    .fitCenter()
                    .dontAnimate()
                    .placeholder(com.tokopedia.resources.common.R.drawable.chatbot_video_placeholder)
                    .error(com.tokopedia.abstraction.R.drawable.error_drawable)
                    .into(imageview)
            }
        } catch (e: Exception) {
            if (imageview.context != null) {
                imageview.setImageDrawable(
                    ContextCompat.getDrawable(
                        imageview.context,
                        com.tokopedia.resources.common.R.drawable.chatbot_video_placeholder
                    )
                )
            }
        }

    }

    fun getStrokeWidthSenderDimenRes(): Int {
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
                    .placeholder(com.tokopedia.resources.common.R.drawable.chatbot_video_placeholder)
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
        return GlideUrl(url)
    }


    private fun bindChatReadStatus(element: VideoUploadUiModel, checkMark: ImageView) {
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

    override fun setHeaderDate(element: BaseChatUiModel?) {
        if (date == null) return
        val time = element?.replyTime?.let {
            ChatBotTimeConverter.getDateIndicatorTime(
                it,
                itemView.context.getString(com.tokopedia.chat_common.R.string.chat_today_date),
                itemView.context.getString(com.tokopedia.chat_common.R.string.chat_yesterday_date)
            )
        }
        date.text = time
        if (date != null && element?.isShowDate == true
            && !TextUtils.isEmpty(time)
        ) {
            datContainer?.show()
        } else if (date != null) {
            datContainer?.hide()
        }
    }

    override fun getDateId(): Int = R.id.date

    protected val chatStatus: ImageView? = itemView?.findViewById(getReadStatusId())

    //    private val name: TextView? = itemView?.findViewById(getChatNameId())
//    private val label: TextView? = itemView?.findViewById(getLabelId())
//    private val dot: TextView? = itemView?.findViewById(getDotId())
    private val action: ImageView? = itemView?.findViewById(getLeftActionId())
    protected val progressBarSendImage: View? = itemView?.findViewById(getProgressBarSendImageId())
    protected val attachment: ImageView? = itemView?.findViewById(getImageId())
    //ADD the video length and text


    private fun bindRetry(element: VideoUploadUiModel) {
        if (element.isRetry) {
            setRetryView(element)
        }
    }

    protected open fun bindClickListener(element: VideoUploadUiModel) {
        view.setOnClickListener { view ->
            if (element.videoUrl != null && element.replyTime != null) {
                //  listener.onVideoUploadClicked(element.videoUrl!!, element.replyTime!!)
            }
        }
    }

    private fun setupChatBubbleAlignment(chatBalloon: View?, element: VideoUploadUiModel) {
        if (element.isSender) {
            setChatRight(chatBalloon)
            bindChatReadStatus(element)
        } else {
            setChatLeft(chatBalloon)
        }
    }

    protected open fun setChatLeft(chatBalloon: View?) {
        setAlignParent(RelativeLayout.ALIGN_PARENT_LEFT, chatBalloon)
        alignHour(RelativeLayout.ALIGN_PARENT_LEFT, hour)
        setVisibility(chatStatus, View.GONE)
//        setVisibility(name, View.GONE)
//        setVisibility(label, View.GONE)
//        setVisibility(dot, View.GONE)
    }

    protected open fun setChatRight(chatBalloon: View?) {
        setAlignParent(RelativeLayout.ALIGN_PARENT_RIGHT, chatBalloon)
        alignHour(RelativeLayout.ALIGN_PARENT_RIGHT, hour)
        setVisibility(chatStatus, View.VISIBLE)
    }

    protected open fun alignHour(alignment: Int, hour: TextView) {
        setAlignParent(alignment, hour)
    }

    private fun setAlignParent(alignment: Int, view: View?) {
        if (view?.layoutParams !is RelativeLayout.LayoutParams) return
        val params = view.layoutParams as RelativeLayout.LayoutParams
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0)
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0)
        params.addRule(alignment)
        view.layoutParams = params
    }

    private fun setRetryView(element: VideoUploadUiModel) {
        setVisibility(action, View.VISIBLE)
        setVisibility(hour, View.GONE)
        setVisibility(chatStatus, View.GONE)
        setVisibility(progressBarSendImage, View.GONE)
        action?.setOnClickListener {
            listener.onRetrySendVideo(element)
        }
    }

    @SuppressLint("DefaultLocale")
    protected fun prerequisiteUISetup(element: VideoUploadUiModel) {
        action?.visibility = View.GONE
        progressBarSendImage?.visibility = View.GONE
        if (!TextUtils.isEmpty(element.fromRole)
            && element.fromRole.toLowerCase() != ROLE_USER.toLowerCase()
            && element.isSender
            && !element.isDummy
            && element.isShowRole
        ) {
//            if (name != null) name.text = element.from
//            if (label != null) label.text = element.fromRole
//            setVisibility(name, View.VISIBLE)
//            setVisibility(dot, View.VISIBLE)
//            setVisibility(label, View.VISIBLE)
        } else {
//            setVisibility(name, View.GONE)
//            setVisibility(dot, View.GONE)
//            setVisibility(label, View.GONE)
        }
    }

    protected fun setVisibility(view: View?, visibility: Int) {
        if (view != null) {
            view.visibility = visibility
        }
    }

    override fun onViewRecycled() {
        super.onViewRecycled()
        if (attachment != null) {
            ImageHandler.clearImage(attachment)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_chatbot_chat_video_upload
        private val ROLE_USER = "User"
        private val BLUR_WIDTH = 30
        private val BLUR_HEIGHT = 30
    }
}