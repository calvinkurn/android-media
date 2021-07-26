package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.youtubeview

import android.content.res.Resources
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.customview.YoutubeWebView
import com.tokopedia.discovery2.viewcontrollers.customview.YoutubeWebViewEventListener
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.unifycomponents.ImageUnify

class YoutubeViewViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView), YoutubeWebViewEventListener.EventVideoPaused, YoutubeWebViewEventListener.EventVideoPlaying,YoutubeCustomViewListener, YoutubeWebViewEventListener.EventPlayerReady {

    private var youtubeWebView: YoutubeWebView? = itemView.findViewById(R.id.youtube_webview)
    private val shimmerView: ImageUnify = itemView.findViewById(R.id.shimmer_view)
    private val parent:ConstraintLayout = itemView.findViewById(R.id.parent)
    private lateinit var youTubeViewViewModel: YouTubeViewViewModel
    private var videoId: String = ""
    private var videoName: String = ""
    private val widthOfPlayer:Int = ((Resources.getSystem().displayMetrics.widthPixels- (2*itemView.context.resources.getDimensionPixelSize(R.dimen.disco_youtube_horizontal_padding)))/ Resources.getSystem().displayMetrics.density).toInt()

    init {
        youtubeWebView?.customViewInterface = this
    }


    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        if(youtubeWebView == null){
            addWebView()
        }
        youTubeViewViewModel = discoveryBaseViewModel as YouTubeViewViewModel
        setUpObserver()
    }

    private fun addWebView() {
        fragment.context?.let {
            val webView  = YoutubeWebView(it)
            val set = ConstraintSet()
            webView.id = View.generateViewId()
            parent.addView(webView)
            set.clone(parent)
            val params = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
            webView.layoutParams = params
            set.connect(webView.id,ConstraintSet.TOP,parent.id,ConstraintSet.TOP)
            set.connect(webView.id,ConstraintSet.START,parent.id,ConstraintSet.START)
            set.applyTo(parent)
            webView.customViewInterface = this
            youtubeWebView = webView
        }
    }

    private fun setUpObserver() {
        youTubeViewViewModel.getVideoId().observe(fragment.viewLifecycleOwner, Observer {
            videoId = it.videoId ?: ""
            videoName = it.name ?: ""
            showVideoInWebView()
        })
    }
    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let { it ->
            youTubeViewViewModel.getVideoId().removeObservers(it)
        }
    }

    private fun showVideoInWebView() {
        youtubeWebView?.setUpEventListeners(youtubeEventVideoPlaying =this, youtubeEventVideoPaused = this,playerReady = this)
        youtubeWebView?.loadVideo(videoId,widthOfPlayer)
        shimmerView.hide()
    }

    override fun onVideoPaused(time: Int) {
        (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackClickVideo(videoId, videoName, time.toString())
    }

    override fun onVideoPlaying(time: Int) {
        (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackClickVideo(videoId, videoName, time.toString())
    }

    override fun onShowCustomView(view: View) {
        (fragment as? DiscoveryFragment)?.showCustomContent(view)
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
    }

}