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
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR

class ChatbotVideoFragment : BaseDaggerFragment(){

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

        progressLoader.visible()
        progressLoader.bringToFront()
        initVideoPlayer()
    }

    private fun initVideoPlayer() {
        videoPlayerView.player = chatbotExoPlayer.getExoPlayer()
        chatbotExoPlayer.start(videoUrl)
        chatbotExoPlayer.setVideoStateListener(object : ChatbotExoPlayer.VideoStateListener{
            override fun onInitialStateLoading() {
                progressLoader.visible()
                progressLoader.bringToFront()
            }

            override fun onVideoReadyToPlay() {
                progressLoader.gone()
            }

            override fun onVideoStateChange(stopDuration: Long, videoDuration: Long) {

            }

            override fun onVideoPlayerError(e: ExoPlaybackException) {
                onErrorVideoLoad()
            }
        })
    }

    private fun onErrorVideoLoad(){
        progressLoader.gone()
        errorImage.visible()
        errorImage.setImageResource(R.drawable.chatbot_video_error)
        context?.getString(R.string.chatbot_video_can_not_be_played)
            ?.let { Toaster.build(videoPlayerView, it, type = TYPE_ERROR) }
    }

    companion object {

        fun getInstance(bundle: Bundle): ChatbotVideoFragment {
            val fragment = ChatbotVideoFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}