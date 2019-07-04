package com.tokopedia.groupchat.room.view.viewstate

import android.support.v4.app.FragmentManager
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.room.view.viewmodel.VideoStreamViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.videoplayer.state.PlayerController
import com.tokopedia.videoplayer.state.PlayerException
import com.tokopedia.videoplayer.state.PlayerType
import com.tokopedia.videoplayer.state.RepeatMode
import com.tokopedia.videoplayer.utils.sendViewToBack
import com.tokopedia.videoplayer.view.player.TkpdVideoPlayer
import com.tokopedia.videoplayer.view.player.VideoPlayerListener

/**
 * @author : Steven 28/05/19
 */
class VideoVerticalHelper constructor(
        var bufferContainer: View,
        var bufferDimContainer: View,
        var fragmentManager: FragmentManager,
        var playerView: FrameLayout,
        var rootView: RelativeLayout
) {

    companion object {
        var VIDEO_480 = 480
        var VIDEO_720 = 720
    }

    var videoSource = 0
    var videoStreamViewModel = VideoStreamViewModel()
    var player: TkpdVideoPlayer? = null
    private var bufferLoading = bufferContainer.findViewById<ProgressBar>(R.id.buffer_progress_bar)
    private var bufferText = bufferContainer.findViewById<TextView>(R.id.buffer_text)

    init {
        hideContainer()
        bufferDimContainer.setOnClickListener {}
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
        bufferDimContainer.show()
    }

    fun hideContainer() {
        bufferContainer.hide()
        bufferDimContainer.hide()
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
                .repeatMode(RepeatMode.REPEAT_MODE_ALL)
                /* handle video player listener */
                .listener(object : VideoPlayerListener {
                    override fun onPlayerStateChanged(playbackState: Int) {

                    }

                    override fun onPlayerError(error: PlayerException) {
                        showRetryOnly()
                        Log.d("exoplayert", error.toString())
                    }
                })
                .build()
        playerView.show()
        hideContainer()
    }

    fun hideVideo() {
        player?.pause()
        playerView.hide()
    }

    fun setData(it: VideoStreamViewModel) {
        videoStreamViewModel = it
    }


    fun playVideo(videoQuality: Int) {
        videoSource = videoQuality
        val video: String = when (videoQuality) {
            VIDEO_480 -> videoStreamViewModel.rtmpStandard
            VIDEO_720 -> videoStreamViewModel.rtmpHigh
            else -> videoStreamViewModel.rtmpStandard
        }
        playVideoSource(video)
    }

    fun showVideo() {
        playerView.show()
        player?.resume()
    }
}