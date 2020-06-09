package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.youtubeview

import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.unifycomponents.ImageUnify


class YoutubeViewViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {

    private var youtubeWebview: WebView = itemView.findViewById(R.id.youtube_webview)
    private val shimmerView: ImageUnify = itemView.findViewById(R.id.shimmer_view)
    private lateinit var youTubeViewViewModel: YouTubeViewViewModel
    private lateinit var videoId: String


    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        youTubeViewViewModel = discoveryBaseViewModel as YouTubeViewViewModel
        setUpObserver()
    }

    private fun setUpObserver() {
        youTubeViewViewModel.getVideoId().observe(fragment.viewLifecycleOwner, Observer {
            videoId = it.videoId ?: ""
            if (shimmerView.visibility == View.VISIBLE) {
                showVideoInWebView()
                (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackClickVideo(it.videoId
                        ?: "", it.name ?: "", "")
            }
        })
    }

    private fun showVideoInWebView() {
        youtubeWebview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return false
            }
        }
        val webSettings: WebSettings = youtubeWebview.settings
        webSettings.javaScriptEnabled = true
        val mimeType = "text/html"
        val encoding = "UTF-8"
        youtubeWebview.webChromeClient = object : WebChromeClient() {}
        val html: String = getHTML()
        youtubeWebview.loadData(html, mimeType, encoding)
        shimmerView.hide()
    }

    private fun getHTML(): String {
        return "<html><body><iframe width=100% height=100% src=\"https://www.youtube.com/embed/$videoId\" frameborder=\"0\"></iframe></body></html>"
    }


}