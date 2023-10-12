package com.tokopedia.chatbot.chatbot2.view.fragment

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
import com.tokopedia.chatbot.chatbot2.view.activity.ChatbotVideoActivity
import com.tokopedia.chatbot.chatbot2.view.customview.videoheader.VideoScreenHeader
import com.tokopedia.chatbot.chatbot2.view.widget.ChatbotExoPlayer
import com.tokopedia.chatbot.chatbot2.view.widget.ChatbotVideoControlView
import com.tokopedia.chatbot.databinding.FragmentChatbotVideo2Binding
import com.tokopedia.chatbot.view.activity.ChatbotActivity
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.Toaster.LENGTH_SHORT
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR

class ChatbotVideoFragment :
    BaseDaggerFragment(),
    ChatbotExoPlayer.ChatbotVideoStateListener,
    VideoScreenHeader.OnClickBackButton {

    private var videoUrl = ""
    private var videoPlayerView: PlayerView? = null
    private var progressLoader: LoaderUnify? = null
    private var errorImage: ImageView? = null
    private var chatbotExoPlayer: ChatbotExoPlayer? = null
    private var chatbotVideoControl: ChatbotVideoControlView? = null
    private var parentLayout: ConstraintLayout? = null
    private var videoScreenHeader: VideoScreenHeader? = null

    private var _viewBinding: FragmentChatbotVideo2Binding? = null
    private fun getBindingView() = _viewBinding!!

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() = Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentChatbotVideo2Binding.inflate(inflater, container, false)
        return getBindingView().root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        videoUrl = arguments?.getString(ChatbotVideoActivity.PARAM_VIDEO_URL, "") ?: ""
        initViews(view)
        initListenerForNavigation()
        var defaultStatusBarHeight = DEFAULT_HEIGHT
        if (context != null) {
            defaultStatusBarHeight = getStatusBarHeight(requireContext())
        }
        setMarginForHeader(defaultStatusBarHeight)
    }

    private fun setMarginForHeader(defaultStatusBarHeight: Int) {
        val param = videoScreenHeader?.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(0, defaultStatusBarHeight, 0, 0)
        videoScreenHeader?.layoutParams = param
    }

    private fun getStatusBarHeight(context: Context): Int {
        var result =
            (DEFAULT_STATUS_BAR_HEIGHT * context.resources.displayMetrics.density + DEFAULT_RATIO).toInt()
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

    private fun initViews(view: View) {
        parentLayout = getBindingView().parentView
        parentLayout?.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        videoScreenHeader = getBindingView().videoScreenHeader
        chatbotVideoControl = getBindingView().videoControl
        chatbotExoPlayer = ChatbotExoPlayer(view.context, chatbotVideoControl)
        videoPlayerView = getBindingView().videoPlayer
        progressLoader = getBindingView().loader
        errorImage = getBindingView().errorImage
        videoScreenHeader?.bringToFront()
        initVideoPlayer()
    }

    private fun initListenerForNavigation() {
        videoScreenHeader?.backClickListener = this
    }

    private fun initVideoPlayer() {
        videoPlayerView?.player = chatbotExoPlayer?.getExoPlayer()
        chatbotExoPlayer?.start(videoUrl)
        chatbotExoPlayer?.videoStateListener = this
        initListenerForVideoPlayer()
    }

    private fun initListenerForVideoPlayer() {
        videoPlayerView?.videoSurfaceView?.setOnClickListener {
            if (chatbotExoPlayer?.getExoPlayer()?.isPlaying == true) {
                chatbotExoPlayer?.pause()
            } else {
                chatbotExoPlayer?.resume()
            }
        }
    }

    private fun onErrorVideoLoad() {
        errorImage?.visible()
        errorImage?.setImageResource(R.drawable.chatbot_video_error)
        progressLoader?.gone()
        context?.let {
            videoPlayerView?.let { videoPlayer ->
                Toaster.build(
                    videoPlayer,
                    getString(R.string.chatbot_video_can_not_be_played),
                    LENGTH_SHORT,
                    TYPE_ERROR,
                    getString(R.string.chatbot_video_refresh)
                ) {
                    chatbotExoPlayer?.start(videoUrl)
                    errorImage?.gone()
                }.show()
            }
        }
    }

    override fun onInitialStateLoading() {
        progressLoader?.visible()
        progressLoader?.bringToFront()
    }

    override fun onVideoReadyToPlay() {
        progressLoader?.gone()
    }

    override fun onVideoStateChange(stopDuration: Long, videoDuration: Long) {
        // nope
    }

    override fun onVideoPlayerError(e: ExoPlaybackException) {
        onErrorVideoLoad()
    }

    override fun onPause() {
        super.onPause()
        chatbotExoPlayer?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        chatbotExoPlayer?.destroy()
    }

    override fun onResume() {
        super.onResume()
        chatbotExoPlayer?.resume()
    }

    override fun onStop() {
        super.onStop()
        chatbotExoPlayer?.stop()
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
        const val DEFAULT_RATIO = 0.5f
        const val DEFAULT_HEIGHT = 50
    }
}
