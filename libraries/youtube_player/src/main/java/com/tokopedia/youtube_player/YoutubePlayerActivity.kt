package com.tokopedia.youtube_player

import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.youtube_player.databinding.ActivityYoutubePlayerBinding

class YoutubePlayerActivity : AppCompatActivity(),
    YoutubeWebViewEventListener.EventVideoPaused, YoutubeWebViewEventListener.EventVideoPlaying,
    YoutubeWebViewEventListener.EventVideoEnded, YoutubeWebViewEventListener.EventVideoCued,
    YoutubeCustomViewListener, YoutubeWebViewEventListener.EventPlayerReady
{

    companion object {
        private const val ROTATION = 90f
    }

    private val viewBinding: ActivityYoutubePlayerBinding? by viewBinding()
    private var videoId: String = ""
    private val youtubeWebView: YoutubeWebView? by lazy {
        viewBinding?.youtubeView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_player)
        getIntentData()
        setupYoutubeVideo()
    }

    private fun setupYoutubeVideo() {
        youtubeWebView?.let {
            it.customViewInterface = this
            it.loadVideo(videoId)
        }
    }

    private fun getIntentData() {
        intent.data?.let {
            videoId = it.pathSegments.getOrNull(Int.ONE).toString()
        }
    }
    override fun onDestroy() {
        destroyYoutubeWebView()
        super.onDestroy()
    }

    private fun destroyYoutubeWebView() {
        youtubeWebView?.destroy()
    }

    override fun onEnterFullScreen(view: View) {
        hideSystemUi()
        viewBinding?.videoContainer?.hide()
        view.rotation = ROTATION
        val width = Resources.getSystem().displayMetrics.widthPixels
        val height = this.let {
            val metrics = DisplayMetrics()
            windowManager?.defaultDisplay?.getRealMetrics(metrics)
            metrics.heightPixels
        }
        val offset = width - height
        view.translationX = offset.toFloat() / 2
        view.translationY = -offset.toFloat() / 2

        val layoutParams = FrameLayout.LayoutParams(height, width)
        view.layoutParams = layoutParams
        viewBinding?.videoFullscreenContainer?.addView(view)
        viewBinding?.videoFullscreenContainer?.show()
        viewBinding?.videoFullscreenContainer?.requestFocus()
    }

    private fun hideSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, viewBinding?.activityYoutubePlayer!!).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    override fun onExitFullScreen() {
        showSystemUi()
        viewBinding?.videoContainer?.show()
        viewBinding?.videoFullscreenContainer?.hide()
        viewBinding?.videoFullscreenContainer?.removeAllViews()
    }

    private fun showSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, viewBinding?.activityYoutubePlayer!!).show(WindowInsetsCompat.Type.systemBars())
    }

    override fun renderProcessKilled() {
        destroyYoutubeWebView()
        showVideoInWebView()
    }

    private fun showVideoInWebView() {
        if (youtubeWebView?.youtubeJSInterface == null) {
            youtubeWebView?.setUpEventListeners(
                youtubeEventVideoPlaying = this, youtubeEventVideoPaused = this,
                youtubeEventVideoEnded = this, youtubeEventVideoCued = this, playerReady = this
            )
        }
        youtubeWebView?.loadVideo(videoId)
    }

    override fun onVideoEnded(time: Int) {
    }

    override fun onVideoPlaying(time: Int) {
    }

    override fun onVideoPaused(time: Int) {
    }

    override fun onVideoCued() {
    }

    override fun onPlayerReady() {
        youtubeWebView?.isPlayerReady = true
    }

}
