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
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.unifycomponents.ImageUnify


class YoutubeViewViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {

    private var youtubeWebview: WebView = itemView.findViewById(R.id.youtube_webview)
    private val shimmerView: ImageUnify = itemView.findViewById(R.id.shimmer_view)
    private lateinit var youTubeViewViewModel: YouTubeViewViewModel
    private var videoId: String = ""
    private var videoName: String = ""


    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        youTubeViewViewModel = discoveryBaseViewModel as YouTubeViewViewModel
        setUpObserver()
    }

    private fun setUpObserver() {
        youTubeViewViewModel.getVideoId().observe(fragment.viewLifecycleOwner, Observer {
            videoId = it.videoId ?: ""
            videoName = it.name ?: ""
            if (shimmerView.visibility == View.VISIBLE) {
                showVideoInWebView()
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
        youtubeWebview.addJavascriptInterface(YoutubeWebViewInterface(fragment, videoId, videoName), "jsInterface")
        val mimeType = "text/html"
        val encoding = "UTF-8"
        youtubeWebview.webChromeClient = object : WebChromeClient() {}
        val html: String = getHTML()
        youtubeWebview.loadData(html, mimeType, encoding)
        shimmerView.hide()
    }

    private fun getHTML(): String {
        return "<html>\n" +
                "  <body>\n" +
                "    <div id=\"player\"></div>\n" +
                "\n" +
                "    <script>\n" +
                "      var tag = document.createElement('script');\n" +
                "\n" +
                "      tag.src = \"https://www.youtube.com/iframe_api\";\n" +
                "      var firstScriptTag = document.getElementsByTagName('script')[0];\n" +
                "      firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);\n" +
                "\n" +
                "      var player;\n" +
                "      function onYouTubeIframeAPIReady() {\n" +
                "        player = new YT.Player('player', {\n" +
                "          height: '100%',\n" +
                "          width: '100%',\n" +
                "          videoId: '$videoId',\n" +
                "          events: {\n" +
                "            'onStateChange': onPlayerStateChange\n" +
                "          }\n" +
                "        });\n" +
                "      }\n" +
                "\n" +
                "      function onPlayerStateChange(event) {\n" +
                "        if (event.data == YT.PlayerState.PLAYING) {\n" +
                "          jsInterface.onStateChanged(player.getCurrentTime());\n" +
                "        }\n" +
                "        else if (event.data == YT.PlayerState.PAUSED) {\n" +
                "          jsInterface.onStateChanged(player.getCurrentTime());\n" +
                "        }\n" +
                "      }\n" +
                "    </script>\n" +
                "  </body>\n" +
                "</html>"
    }

}