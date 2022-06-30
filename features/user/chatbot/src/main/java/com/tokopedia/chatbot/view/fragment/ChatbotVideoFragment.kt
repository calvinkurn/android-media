package com.tokopedia.chatbot.view.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.view.activity.ChatbotVideoActivity
import com.tokopedia.videoplayer.view.widget.VideoPlayerView

class ChatbotVideoFragment : BaseDaggerFragment() {

    private var videoUrl = ""

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
        val videoPlayer = view.findViewById<VideoPlayerView>(R.id.video_player)
        videoPlayer.setVideoURI(Uri.parse(videoUrl))
        videoPlayer.start()
    }

    companion object {

        fun getInstance(bundle: Bundle): ChatbotVideoFragment {
            val fragment = ChatbotVideoFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}