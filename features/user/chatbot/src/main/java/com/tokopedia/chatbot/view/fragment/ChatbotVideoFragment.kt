package com.tokopedia.chatbot.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.view.activity.ChatbotVideoActivity
import com.tokopedia.chatbot.view.widget.ChatbotExoPlayer
import com.tokopedia.chatbot.view.widget.ChatbotVideoControlView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.Toaster.LENGTH_SHORT
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR

class ChatbotVideoFragment : BaseDaggerFragment(), ChatbotExoPlayer.ChatbotVideoStateListener{

    private var videoUrl = ""
    private lateinit var videoPlayerView : PlayerView
    private lateinit var progressLoader : LoaderUnify
    private lateinit var errorImage : ImageView
    private lateinit var chatbotExoPlayer: ChatbotExoPlayer
    private lateinit var chatbotVideoControl: ChatbotVideoControlView

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_chatbot_video, container, false)
        videoUrl = arguments?.getString(ChatbotVideoActivity.PARAM_VIDEO_URL, "") ?: ""
        initViews(view)

        return view
    }

    private fun initViews(view : View) {
        chatbotVideoControl = view.findViewById(R.id.video_control)
        chatbotExoPlayer = ChatbotExoPlayer(view.context, chatbotVideoControl)
        videoPlayerView = view.findViewById(R.id.video_player)
        progressLoader = view.findViewById(R.id.loader)
        errorImage = view.findViewById(R.id.error_image)
        initVideoPlayer()
    }

    private fun initVideoPlayer() {
        videoPlayerView.player = chatbotExoPlayer.getExoPlayer()
        chatbotExoPlayer.start(videoUrl)
        chatbotExoPlayer.videoStateListener = this
        initListenerForVideoPlayer()
    }

    private fun initListenerForVideoPlayer() {
        videoPlayerView.videoSurfaceView?.setOnClickListener {
            if (chatbotExoPlayer.getExoPlayer().isPlaying) {
                chatbotExoPlayer.pause()
            } else {
                chatbotExoPlayer.resume()
            }
        }
    }

    private fun onErrorVideoLoad(){
        errorImage.visible()
        errorImage.setImageResource(R.drawable.chatbot_video_error)
        progressLoader.gone()
        context?.let {
            Toaster.build(
                videoPlayerView, getString(R.string.chatbot_video_can_not_be_played), LENGTH_SHORT,
                TYPE_ERROR, getString(R.string.chatbot_video_refresh)
            ) {
                chatbotExoPlayer.start(videoUrl)
                errorImage.gone()
            }.show()
        }

    }

    companion object {

        fun getInstance(bundle: Bundle): ChatbotVideoFragment {
            val fragment = ChatbotVideoFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onInitialStateLoading() {
        progressLoader.visible()
        progressLoader.bringToFront()
    }

    override fun onVideoReadyToPlay() {
        progressLoader.gone()
    }

    override fun onVideoPause() {
        progressLoader.gone()
    }

    override fun onVideoStateChange(stopDuration: Long, videoDuration: Long) {
        //nope
    }

    override fun onVideoPlayerError(e: ExoPlaybackException) {
        onErrorVideoLoad()
    }

    override fun onVideoEnded() {
        progressLoader.gone()
    }

    override fun onPause() {
        super.onPause()
        chatbotExoPlayer.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        chatbotExoPlayer.destroy()
    }

    override fun onStop() {
        super.onStop()
        chatbotExoPlayer.stop()
    }

}