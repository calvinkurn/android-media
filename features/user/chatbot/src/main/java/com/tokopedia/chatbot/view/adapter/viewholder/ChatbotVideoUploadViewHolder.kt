package com.tokopedia.chatbot.view.adapter.viewholder

import android.R.attr.path
import android.graphics.drawable.Drawable
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
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
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
import com.tokopedia.unifycomponents.LoaderUnify
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
    private fun getImageId() = R.id.image
    private fun getProgressBarSendImageId() = R.id.progress_bar
    private fun getLeftActionId() = R.id.left_action

    //TODO change this
    private fun getReadStatusId() = com.tokopedia.chat_common.R.id.chat_status
    private fun getChatBalloonId() = R.id.card_group_chat_message
    private fun getVideoTotalLengthId() = R.id.video_length
    private val datContainer: CardView? = itemView?.findViewById(R.id.dateContainer)
    private val chatBalloon: View? = itemView?.findViewById(getChatBalloonId())
    private val videoTotalLength: Typography? = itemView?.findViewById(getVideoTotalLengthId())
    private val videoUploadProgressBar: LoaderUnify? =
        itemView?.findViewById(R.id.progress_bar_loding)
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

    override fun bind(element: VideoUploadUiModel) {
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

        element.videoUrl?.let { videoUrl ->
            attachmentUnify?.let { attachementUnify ->
                loadImage(
                    attachementUnify,
                    videoUrl,
                )
            }
        }
    }

    fun getStrokeWidthSenderDimenRes(): Int {
        return R.dimen.dp_chatbot_3
    }

    private fun loadImage(
        imageview: ImageView,
        url: String?
    ) {
        try {
            if (imageview.context != null) {
                Glide.with(imageview.context)
                    .load(url)
                    .fitCenter()
                    .dontAnimate()
                    .placeholder(R.drawable.chatbot_video_placeholder)
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

    override fun setHeaderDate(element: BaseChatUiModel) {
        if (date == null) return
        val time = element?.replyTime?.let {
            ChatBotTimeConverter.getDateIndicatorTime(
                it,
                itemView.context.getString(com.tokopedia.chat_common.R.string.chat_today_date),
                itemView.context.getString(com.tokopedia.chat_common.R.string.chat_yesterday_date)
            )
        }
        date?.text = time
        if (date != null && element?.isShowDate && !TextUtils.isEmpty(time)
        ) {
            datContainer?.show()
        } else if (date != null) {
            datContainer?.hide()
        }
    }

    override val dateId: Int
        get() = R.id.date

    private val chatStatus: ImageView? = itemView?.findViewById(getReadStatusId())

    //    private val name: TextView? = itemView?.findViewById(getChatNameId())
//    private val label: TextView? = itemView?.findViewById(getLabelId())
//    private val dot: TextView? = itemView?.findViewById(getDotId())
    private val action: ImageView? = itemView?.findViewById(getLeftActionId())
    private val progressBarSendImage: View? = itemView?.findViewById(getProgressBarSendImageId())
    private val attachment: ImageView? = itemView?.findViewById(getImageId())
    //ADD the video length and text


    private fun bindRetry(element: VideoUploadUiModel) {
        if (element.isRetry) {
            setRetryView(element)
        }
    }

    private fun bindClickListener(element: VideoUploadUiModel) {
        view?.setOnClickListener { view ->
            if (element.videoUrl != null && element.replyTime != null) {
                listener.onUploadedVideoClicked(element.videoUrl ?: "")
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

    private fun setChatLeft(chatBalloon: View?) {
        setAlignParent(RelativeLayout.ALIGN_PARENT_LEFT, chatBalloon)
        alignHour(RelativeLayout.ALIGN_PARENT_LEFT, hour)
        setVisibility(chatStatus, View.GONE)
    }

    private fun setChatRight(chatBalloon: View?) {
        setAlignParent(RelativeLayout.ALIGN_PARENT_RIGHT, chatBalloon)
        alignHour(RelativeLayout.ALIGN_PARENT_RIGHT, hour)
        setVisibility(chatStatus, View.VISIBLE)
    }

    //TODO check
    private fun alignHour(alignment: Int, hour: TextView?) {
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

    private fun prerequisiteUISetup(element: VideoUploadUiModel) {
        action?.visibility = View.GONE
        progressBarSendImage?.visibility = View.GONE
    }

    private fun setVisibility(view: View?, visibility: Int) {
        if (view != null) {
            view.visibility = visibility
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_chatbot_chat_video_upload
    }
}