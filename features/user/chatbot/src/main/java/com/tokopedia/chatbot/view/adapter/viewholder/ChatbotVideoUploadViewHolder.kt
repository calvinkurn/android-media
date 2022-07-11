package com.tokopedia.chatbot.view.adapter.viewholder

import android.net.Uri
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.videoupload.VideoUploadUiModel
import com.tokopedia.chatbot.util.ChatBotTimeConverter
import com.tokopedia.chatbot.util.ViewUtil
import com.tokopedia.chatbot.view.adapter.viewholder.listener.VideoUploadListener
import com.tokopedia.chatbot.view.widget.ChatbotExoPlayer
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifyprinciples.Typography
import java.util.concurrent.TimeUnit


class ChatbotVideoUploadViewHolder(
    itemView: View?,
    private val listener: VideoUploadListener,
) : BaseChatViewHolder<VideoUploadUiModel>(itemView) {

    override fun alwaysShowTime() = true
    override fun useWhiteReadStatus() = true
    private fun getVideoPlayerId() = R.id.video_player
    private fun getProgressBarSendVideoId() = R.id.progress_bar
    private fun getLeftActionId() = R.id.left_action
    private fun getReadStatusId() = com.tokopedia.chat_common.R.id.chat_status
    private fun getChatBalloonId() = R.id.card_group_chat_message
    private fun getVideoTotalLengthId() = R.id.video_length
    private val datContainer: CardView? = itemView?.findViewById(R.id.dateContainer)
    private val chatBalloon: View? = itemView?.findViewById(getChatBalloonId())
    private val videoTotalLength: Typography? = itemView?.findViewById(getVideoTotalLengthId())
    private val cancelUpload = itemView?.findViewById<ImageView>(R.id.progress_cross)
    private val chatStatus: ImageView? = itemView?.findViewById(getReadStatusId())
    private val action: ImageView? = itemView?.findViewById(getLeftActionId())
    private val progressBarSendVideo: View? = itemView?.findViewById(getProgressBarSendVideoId())
    private val videoPlayerView: PlayerView? = itemView?.findViewById(getVideoPlayerId())
    private lateinit var chatbotExoPlayer: ChatbotExoPlayer

    override val dateId: Int
        get() = R.id.date

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

    override fun bind(element: VideoUploadUiModel) {
        if (element == null) return
        super.bind(element)
        prerequisiteUISetup()
        setupChatBubbleAlignment(chatBalloon, element)
        bindClickListener(element)
        bindVideoAttachment(element)
        bindRetry(element)
        setDefaultVideoDuration()
        setUpChatReadStatus(element)
        bindBackground()
        setHeaderDate(element)
        setUpChatbotExoPlayer(element)
        setUpExoPlayerListener()
    }

    private fun setUpChatReadStatus(element: VideoUploadUiModel) {
        chatStatus?.let {
            bindChatReadStatus(element, it)
        }
    }

    private fun setUpExoPlayerListener() {
        chatbotExoPlayer.getExoPlayer().addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)
                if (playbackState == Player.STATE_READY) {
                    var duration = chatbotExoPlayer.getExoPlayer().duration
                    setTimeData(duration)
                }
            }
        })
    }

    private fun setUpChatbotExoPlayer(element: VideoUploadUiModel) {
        chatbotExoPlayer = ChatbotExoPlayer(itemView.context)
        videoPlayerView?.player = chatbotExoPlayer.getExoPlayer()
        val mediaSource =
            chatbotExoPlayer.getMediaSourceBySource(itemView.context, Uri.parse(element.videoUrl))
        chatbotExoPlayer?.getExoPlayer().prepare(mediaSource)
    }

    private fun setDefaultVideoDuration() {
        videoTotalLength?.text = itemView.context.getString(R.string.chatbot_default_video_duration)
    }

    private fun setTimeData(duration: Long) {
        var totalLengthInString = convertVideoLength(duration)
        videoTotalLength?.text = totalLengthInString
    }

    private fun convertVideoLength(totalLength: Long): String {
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
        if (element.isDummy) {
            progressBarSendVideo?.show()
        } else {
            progressBarSendVideo?.gone()
        }
    }

    fun getStrokeWidthSenderDimenRes(): Int {
        return R.dimen.dp_chatbot_3
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
        cancelUpload?.setOnClickListener {
            listener.onVideoUploadCancelClicked(element)
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
        chatStatus?.gone()
    }

    private fun setChatRight(chatBalloon: View?) {
        setAlignParent(RelativeLayout.ALIGN_PARENT_RIGHT, chatBalloon)
        alignHour(RelativeLayout.ALIGN_PARENT_RIGHT, hour)
        chatStatus?.visible()
    }

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
        action?.visible()
        hour?.gone()
        chatStatus?.gone()
        progressBarSendVideo?.gone()
        action?.setOnClickListener {
            listener.onRetrySendVideo(element)
        }
    }

    private fun prerequisiteUISetup() {
        action?.visibility = View.GONE
        progressBarSendVideo?.visibility = View.GONE
    }

    override fun onViewRecycled() {
        super.onViewRecycled()
        chatbotExoPlayer.destroy()
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_chatbot_chat_video_upload
    }
}