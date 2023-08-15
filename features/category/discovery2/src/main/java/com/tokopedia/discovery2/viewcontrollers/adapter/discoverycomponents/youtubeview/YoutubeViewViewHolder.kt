package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.youtubeview

import android.content.res.Resources
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.customview.VIDEO_PAUSED
import com.tokopedia.discovery2.viewcontrollers.customview.VIDEO_PLAYING
import com.tokopedia.discovery2.viewcontrollers.customview.YoutubeWebView
import com.tokopedia.discovery2.viewcontrollers.customview.YoutubeWebViewEventListener
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.unifycomponents.ImageUnify

class YoutubeViewViewHolder(itemView: View, private val fragment: Fragment) :
    AbstractViewHolder(itemView),
    YoutubeWebViewEventListener.EventVideoPaused,
    YoutubeWebViewEventListener.EventVideoPlaying,
    YoutubeWebViewEventListener.EventVideoEnded,
    YoutubeWebViewEventListener.EventVideoCued,
    YoutubeCustomViewListener,
    YoutubeWebViewEventListener.EventPlayerReady {

    private var youtubeWebView: YoutubeWebView? = itemView.findViewById(R.id.youtube_webview)
    private val shimmerView: ImageUnify = itemView.findViewById(R.id.shimmer_view)
    private val parent: ConstraintLayout = itemView.findViewById(R.id.parent)
    private var youTubeViewViewModel: YouTubeViewViewModel? = null
    private var videoId: String = ""
    private var videoName: String = ""
    private val widthOfPlayer: Int =
        (
            (
                Resources.getSystem().displayMetrics.widthPixels - (
                    2 * itemView.context.resources.getDimensionPixelSize(
                        R.dimen.disco_youtube_horizontal_padding
                    )
                    )
                ) / Resources.getSystem().displayMetrics.density
            ).toInt()
    private val mainThreadHandler: Handler = Handler(Looper.getMainLooper())

    private val autoPlayObserver = Observer<String> {
        if (youtubeWebView?.youtubeJSInterface?.currentState == VIDEO_PLAYING &&
            youTubeViewViewModel?.shouldPause() == true
        ) {
            youTubeViewViewModel?.disableAutoplay()
            youtubeWebView?.pause()
        } else {
            onVideoCued()
        }
    }

    init {
        youtubeWebView?.customViewInterface = this
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        if (youtubeWebView == null) {
            addWebView()
        }
        youTubeViewViewModel = discoveryBaseViewModel as YouTubeViewViewModel
        setUpObserver()
    }

    private fun addWebView() {
        fragment.context?.let {
            val webView = YoutubeWebView(it)
            val set = ConstraintSet()
            webView.id = View.generateViewId()
            parent.addView(webView)
            set.clone(parent)
            val params = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
            webView.layoutParams = params
            set.connect(webView.id, ConstraintSet.TOP, parent.id, ConstraintSet.TOP)
            set.connect(webView.id, ConstraintSet.START, parent.id, ConstraintSet.START)
            set.applyTo(parent)
            webView.customViewInterface = this
            youtubeWebView = webView
        }
    }

    private fun setUpObserver() {
        youTubeViewViewModel?.getVideoId()?.observe(
            fragment.viewLifecycleOwner,
            Observer {
                videoId = it.videoId ?: ""
                videoName = it.name ?: ""
                showVideoInWebView()
            }
        )
        youTubeViewViewModel?.currentlyAutoPlaying()?.observe(fragment.viewLifecycleOwner, autoPlayObserver)
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let { it ->
            youTubeViewViewModel?.getVideoId()?.removeObservers(it)
            youTubeViewViewModel?.currentlyAutoPlaying()?.removeObserver(autoPlayObserver)
        }
    }

    private fun showVideoInWebView() {
        if (youtubeWebView?.youtubeJSInterface == null) {
            youtubeWebView?.setUpEventListeners(
                youtubeEventVideoPlaying = this,
                youtubeEventVideoPaused = this,
                youtubeEventVideoEnded = this,
                youtubeEventVideoCued = this,
                playerReady = this
            )
        }
        youtubeWebView?.loadVideo(videoId, widthOfPlayer)
        shimmerView.hide()
    }

    override fun onVideoPaused(time: Int) {
        (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()
            ?.trackClickVideo(videoId, videoName, time.toString())
        if (youTubeViewViewModel?.isAutoPlayEnabled() == true) {
            val videoID = videoId
//            this check is required because video gets paused for a very
//            short time during seek as well
            mainThreadHandler.postDelayed({
                if (videoID != youtubeWebView?.videoId ||
                    youtubeWebView?.youtubeJSInterface?.currentState == VIDEO_PAUSED
                ) {
                    youTubeViewViewModel?.disableAutoplay()
                }
            }, DELAY_FOR_STATE_CHANGE)
        }
    }

    override fun onVideoPlaying(time: Int) {
        (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()
            ?.trackClickVideo(videoId, videoName, time.toString())
        mainThreadHandler.post {
            if (youTubeViewViewModel?.isAutoPlayEnabled() == true) {
                if (youTubeViewViewModel?.pauseOtherVideos() == true) {
                    youtubeWebView?.unMute()
                }
            } else {
                youtubeWebView?.unMute()
            }
        }
    }

    override fun onShowCustomView(view: View) {
        (fragment as? DiscoveryFragment)?.showCustomContent(view)
        if (youTubeViewViewModel?.isAutoPlayEnabled() == true) {
            mainThreadHandler.post {
                youtubeWebView?.unMute()
            }
        }
    }

    override fun onHideCustomView() {
        (fragment as? DiscoveryFragment)?.hideCustomContent()
    }

    override fun renderProcessKilled() {
        val parent = youtubeWebView?.parent as? ConstraintLayout
        parent?.removeView(youtubeWebView)
        youtubeWebView?.destroy()
        youtubeWebView = null
        addWebView()
        showVideoInWebView()
    }

    override fun onPlayerReady() {
        youtubeWebView?.isPlayerReady = true
        onVideoCued()
    }

    override fun onVideoEnded(time: Int) {
        mainThreadHandler.post {
            youTubeViewViewModel?.autoPlayNext()
        }
    }

    override fun onVideoCued() {
        mainThreadHandler.post {
            if (youTubeViewViewModel?.shouldAutoPlay() == true) {
                youtubeWebView?.play()
                youtubeWebView?.mute()
            }
        }
    }

    companion object {
        const val DELAY_FOR_STATE_CHANGE: Long = 1000
    }
}
