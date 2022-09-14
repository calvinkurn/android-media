package com.tokopedia.chatbot.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.view.activity.ChatbotActivity
import com.tokopedia.chatbot.view.activity.ChatbotVideoActivity
import com.tokopedia.chatbot.view.customview.videoheader.VideoScreenHeader
import com.tokopedia.chatbot.view.widget.ChatbotExoPlayer
import com.tokopedia.chatbot.view.widget.ChatbotVideoControlView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.Toaster.LENGTH_SHORT
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR

class ChatbotVideoFragment : BaseDaggerFragment(), ChatbotExoPlayer.ChatbotVideoStateListener, VideoScreenHeader.OnClickBackButton{

    private var videoUrl = ""
    private lateinit var videoPlayerView : PlayerView
    private lateinit var progressLoader : LoaderUnify
    private lateinit var errorImage : ImageView
    private lateinit var chatbotExoPlayer: ChatbotExoPlayer
    private lateinit var chatbotVideoControl: ChatbotVideoControlView
    private lateinit var parentLayout : ConstraintLayout
    private lateinit var videoScreenHeader: VideoScreenHeader

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
        initListenerForNavigation()
        var defaultStatusBarHeight = 50
        if (context != null) {
            defaultStatusBarHeight = getStatusBarHeight(requireContext())
        }
        setMarginForHeader(defaultStatusBarHeight)
        return view
    }

    private fun setMarginForHeader(defaultStatusBarHeight : Int) {
        val param = videoScreenHeader.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(0,defaultStatusBarHeight,0,0)
        videoScreenHeader.layoutParams = param
    }

    fun getStatusBarHeight(context: Context): Int {
        var result =
            (DEFAULT_STATUS_BAR_HEIGHT * context.resources.displayMetrics.density + 0.5f).toInt()
        val resourceId = context.resources.getIdentifier(
            STATUS_BAR_HEIGHT_ID,
            "dimen",
            "android"
        )
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    private fun initViews(view : View) {
        parentLayout = view.findViewById(R.id.parent_view)
        parentLayout.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        videoScreenHeader = view.findViewById(R.id.video_screen_header)
        chatbotVideoControl = view.findViewById(R.id.video_control)
        chatbotExoPlayer = ChatbotExoPlayer(view.context, chatbotVideoControl)
        videoPlayerView = view.findViewById(R.id.video_player)
        progressLoader = view.findViewById(R.id.loader)
        errorImage = view.findViewById(R.id.error_image)
        videoScreenHeader.bringToFront()
        initVideoPlayer()

    }

    private fun initListenerForNavigation(){
        videoScreenHeader.backClickListener = this
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

    override fun onInitialStateLoading() {
        progressLoader.visible()
        progressLoader.bringToFront()
    }

    override fun onVideoReadyToPlay() {
        progressLoader.gone()
    }

    override fun onVideoStateChange(stopDuration: Long, videoDuration: Long) {
        //nope
    }

    override fun onVideoPlayerError(e: ExoPlaybackException) {
        onErrorVideoLoad()
    }


    override fun onPause() {
        super.onPause()
        chatbotExoPlayer.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        chatbotExoPlayer.destroy()
    }

    override fun onResume() {
        super.onResume()
        chatbotExoPlayer.resume()
    }

    override fun onStop() {
        super.onStop()
        chatbotExoPlayer.stop()
    }

    override fun navigateToChatbotActivity() {
        val intent = Intent(activity, ChatbotActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    companion object {

        fun getInstance(bundle: Bundle): ChatbotVideoFragment {
            val fragment = ChatbotVideoFragment()
            fragment.arguments = bundle
            return fragment
        }

        const val DEFAULT_STATUS_BAR_HEIGHT = 24
        const val STATUS_BAR_HEIGHT_ID = "status_bar_height"
    }

}