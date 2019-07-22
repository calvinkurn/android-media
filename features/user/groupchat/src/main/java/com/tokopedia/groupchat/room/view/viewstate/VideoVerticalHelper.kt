package com.tokopedia.groupchat.room.view.viewstate

import android.support.v4.app.FragmentManager
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.viewpagerindicator.CirclePageIndicator
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel
import com.tokopedia.groupchat.common.analytics.GroupChatAnalytics
import com.tokopedia.groupchat.room.view.viewmodel.VideoStreamViewModel
import com.tokopedia.kotlin.extensions.view.debug
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.videoplayer.state.*
import com.tokopedia.videoplayer.utils.sendViewToBack
import com.tokopedia.videoplayer.view.player.TkpdVideoPlayer
import com.tokopedia.videoplayer.view.player.VideoPlayerListener

/**
 * @author : Steven 28/05/19
 */
class VideoVerticalHelper constructor (
        model: ChannelInfoViewModel?,
        var bufferContainer: View,
        var fragmentManager: FragmentManager,
        var playerView: FrameLayout,
        var rootView: View,
        var setChatListHasSpaceOnTop: (Int) -> Unit,
        var backgroundHelper: PlayBackgroundHelper,
        var analytics: GroupChatAnalytics,
        var gradientBackground: View,
        var liveIndicator: View
): PlayBaseHelper(model) {

    companion object {
        var VIDEO_480 = 480
        var VIDEO_720 = 720
        var VERTICAL_WITH_VIDEO = 200
        var VERTICAL_WITHOUT_VIDEO = 542

        var TAG = "playvertical"
    }

    var videoSource = 0
    var videoStreamViewModel = VideoStreamViewModel()
    var player: TkpdVideoPlayer? = null
    private var bufferLoading = bufferContainer.findViewById<ProgressBar>(R.id.buffer_progress_bar)
    private var bufferText = bufferContainer.findViewById<TextView>(R.id.buffer_text)

    private var startTime = 0L
    private var endTime = 0L

    init {
        hideContainer()
        bufferText.text = getSpannable(R.string.buffer_text_long, R.string.buffer_text_retry)
        bufferText.setOnClickListener {
            showLoadingOnly()
            playVideo(videoSource)
        }
    }

    fun showLoadingOnly() {
        showContainer()
        bufferLoading.show()
        bufferText.hide()
    }

    fun showRetryOnly() {
        showContainer()
        bufferLoading.hide()
        bufferText.show()
    }

    fun showContainer() {
        bufferContainer.show()
    }

    fun hideContainer() {
        bufferContainer.hide()
    }

    private fun getSpannable(sourceStringRes: Int, hyperlinkStringRes: Int): Spannable {
        val context = bufferContainer.context
        val sourceString = context.resources.getString(sourceStringRes)
        val hyperlinkString = context.resources.getString(hyperlinkStringRes)
        val spannable = SpannableString(sourceString)

        spannable.setSpan(object : ClickableSpan() {
            override fun onClick(view: View) {

            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = MethodChecker.getColor(context, R.color.Green_G300)
            }
        }, sourceString.indexOf(hyperlinkString), sourceString.length, 0)

        return spannable
    }

    private fun playVideoSource(sourceMedia: String) {
        debug(TAG, "play video source $sourceMedia")
        sendViewToBack(playerView)
        val width = rootView.layoutParams.width
        val height = rootView.layoutParams.height
        val layoutParams = playerView.layoutParams
        layoutParams.width = width
        layoutParams.height = height

        playerView.layoutParams = layoutParams

        player = TkpdVideoPlayer.Builder()
                .transaction(R.id.playerView, fragmentManager)
                .videoSource(sourceMedia)
                /* preventing seekTo, declare videoPlayer with live_stream mode */
                .type(PlayerType.LIVE_STREAM)
                /* if you have custom controller, turn it off */
                .controller(PlayerController.OFF)
                /* repeat video mode after finished */
                .repeatMode(RepeatMode.REPEAT_MODE_OFF)
                /* handle video player listener */
                .listener(object : VideoPlayerListener {
                    override fun onPlayerStateChanged(playbackState: Int) {
                        when(playbackState) {
                            Player.STATE_BUFFERING -> {
                                showLoadingOnly()
                                debug(TAG, "buffering vertical video")
                            }
                            Player.STATE_READY -> {
                                hideContainer()
                                debug(TAG, "ready vertical video")
                            }
                            Player.STATE_ENDED -> {
                                if(videoStreamViewModel.isActive) {
                                    showLoadingOnly()
                                    playVideo(videoSource)
                                    debug(TAG, "ended vertical video active")
                                }else {
                                    debug(TAG, "ended vertical video non-active")
//                                    showHasEnded()
                                }

                            }
                            Player.STATE_IDLE -> {
                                debug(TAG, "idle vertical video")
                            }
                        }
                    }

                    override fun onPlayerError(error: PlayerException) {
                        debug(TAG, "error vertical video $error")
                        showRetryOnly()
                    }
                })
                .build()
        playerView.show()
        gradientBackground.show()
        showLoadingOnly()
        analytics.eventVerticalVideoPlayed(viewModel?.channelId)
        startTime = System.currentTimeMillis()
    }

    fun stopVideo() {
        player?.stop()
        playerView.hide()
        gradientBackground.show()
        backgroundHelper.resetBackground()
        hideContainer()
        endTime = System.currentTimeMillis()
        analytics.eventWatchVerticalVideo(viewModel?.channelId, (endTime - startTime).toString())
        liveIndicator.hide()
    }

    fun setData(it: VideoStreamViewModel) {
        videoStreamViewModel = it
    }

    fun playVideo(videoQuality: Int) {
        videoSource = videoQuality
        val video: String = when (videoQuality) {
            VIDEO_480 -> videoStreamViewModel.androidStreamSD
            VIDEO_720 -> videoStreamViewModel.androidStreamHD
            else -> videoStreamViewModel.androidStreamSD
        }
        playVideoSource(video)
        backgroundHelper.setEmptyBackground()
        liveIndicator.showWithCondition(videoStreamViewModel.isActive)
    }

    fun isVideoShown(): Boolean {
        return playerView.isShown && videoStreamViewModel?.isActive
    }
}