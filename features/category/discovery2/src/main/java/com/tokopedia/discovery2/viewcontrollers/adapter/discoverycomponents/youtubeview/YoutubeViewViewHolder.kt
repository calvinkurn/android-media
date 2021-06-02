package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.youtubeview

import android.content.res.Resources
import android.view.View
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

class YoutubeViewViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView), YoutubeWebViewEventListener.EventVideoPaused, YoutubeWebViewEventListener.EventVideoPlaying {

    private var youtubeWebView: YoutubeWebView = itemView.findViewById(R.id.youtube_webview)
    private val shimmerView: ImageUnify = itemView.findViewById(R.id.shimmer_view)
    private lateinit var youTubeViewViewModel: YouTubeViewViewModel
    private var videoId: String = ""
    private var videoName: String = ""
    private val widthOfPlayer:Int = ((Resources.getSystem().displayMetrics.widthPixels- (2*itemView.context.resources.getDimensionPixelSize(R.dimen.disco_youtube_view_padding)))/ Resources.getSystem().displayMetrics.density).toInt()


    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        youTubeViewViewModel = discoveryBaseViewModel as YouTubeViewViewModel
        setUpObserver()
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
        youtubeWebView.setUpEventListeners(youtubeEventVideoPlaying = this, youtubeEventVideoPaused = this)
        youtubeWebView.loadVideo(videoId,widthOfPlayer)
        shimmerView.hide()
    }

    override fun onVideoPaused(time: Int) {
        (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackClickVideo(videoId, videoName, time.toString())
    }

    override fun onVideoPlaying(time: Int) {
        (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackClickVideo(videoId, videoName, time.toString())
    }

}