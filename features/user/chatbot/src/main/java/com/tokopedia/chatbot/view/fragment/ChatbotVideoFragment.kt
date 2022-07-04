package com.tokopedia.chatbot.view.fragment

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.MediaController
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.view.activity.ChatbotVideoActivity
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.videoplayer.utils.Video
import com.tokopedia.videoplayer.view.widget.VideoPlayerView

class ChatbotVideoFragment : BaseDaggerFragment(), MediaPlayer.OnPreparedListener {

    private var videoUrl = ""
    private lateinit var videoPlayerView : VideoPlayerView
    private lateinit var progressLoader : LoaderUnify
    private lateinit var errorImage : ImageView

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
        videoPlayerView = view.findViewById(R.id.video_player)
        progressLoader = view.findViewById(R.id.loader)
        errorImage = view.findViewById(R.id.error_image)
        progressLoader.visible()
        progressLoader.bringToFront()
        initVideoPlayer()
    }

    private fun initVideoPlayer() {
        videoPlayerView.setVideoURI(Uri.parse(videoUrl))
        videoPlayerView.setOnErrorListener { _, problem, _ ->
            when(problem) {
                MediaPlayer.MEDIA_ERROR_UNKNOWN -> {
                    Toaster.build(
                        videoPlayerView,
                        getString(com.tokopedia.videoplayer.R.string.error_unknown),
                        Toaster.LENGTH_SHORT,
                        Toaster.TYPE_ERROR
                    ).show()
                    onErrorVideoLoad()
                    true
                }
                MediaPlayer.MEDIA_ERROR_SERVER_DIED -> {
                    Toaster.build(
                        videoPlayerView,
                        getString(com.tokopedia.abstraction.R.string.default_request_error_internal_server),
                        Toaster.LENGTH_SHORT,
                        Toaster.TYPE_ERROR
                    ).show()
                    onErrorVideoLoad()
                    true
                }
                MediaPlayer.MEDIA_ERROR_IO -> {
                    Toaster.build(
                        videoPlayerView,
                        getString(R.string.chatbot_video_can_not_be_played),
                        Toaster.LENGTH_SHORT,
                        Toaster.TYPE_ERROR
                    ).show()
                    onErrorVideoLoad()
                    true
                }

                else -> {
                    Toaster.build(
                        videoPlayerView,
                        getString(com.tokopedia.abstraction.R.string.default_request_error_timeout),
                        Toaster.LENGTH_SHORT,
                        Toaster.TYPE_ERROR
                    ).show()
                    onErrorVideoLoad()
                    true
                }
            }
        }
        videoPlayerView.setOnPreparedListener(this)
    }

    private fun onErrorVideoLoad(){
        progressLoader.gone()
        errorImage.visible()
        errorImage.setImageResource(R.drawable.chatbot_video_error)
    }

    companion object {

        fun getInstance(bundle: Bundle): ChatbotVideoFragment {
            val fragment = ChatbotVideoFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onPrepared(mediaPlayer: MediaPlayer?) {
        mediaPlayer?.let { player ->

            activity?.let { it ->

                val videoSize = Video.resize(it, player.videoWidth, player.videoHeight)
                videoPlayerView.setSize(videoSize.videoWidth, videoSize.videoHeight)
                videoPlayerView.holder.setFixedSize(videoSize.videoWidth, videoSize.videoHeight)


                val mediaController = MediaController(it)
                videoPlayerView.setMediaController(mediaController)
                mediaController.setAnchorView(videoPlayerView)
            }
            progressLoader.gone()
            player.start()
        }
    }
}