package com.tokopedia.discovery2.viewcontrollers.customview

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.tokopedia.unifycomponents.toPx

private const val mimeType = "text/html"
private const val encoding = "UTF-8"

class YoutubeWebView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : WebView(context, attrs, defStyleAttr) {

    private var jsInterface: String = "jsInterface"

    init {
        setUpWebViewClient()
        settings.javaScriptEnabled = true
    }

    private fun setUpWebViewClient() {
        webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return false
            }
        }
        webChromeClient = object : WebChromeClient() {}
    }

    fun setUpEventListeners(youtubeEventVideoEnded: YoutubeWebViewEventListener.EventVideoEnded? = null,
                            youtubeEventVideoPlaying: YoutubeWebViewEventListener.EventVideoPlaying? = null,
                            youtubeEventVideoPaused: YoutubeWebViewEventListener.EventVideoPaused? = null,
                            youtubeEventVideoBuffering: YoutubeWebViewEventListener.EventVideoBuffering? = null,
                            youtubeEventVideoCued: YoutubeWebViewEventListener.EventVideoCued? = null) {
        addJavascriptInterface(YoutubeWebViewInterface(youtubeEventVideoEnded, youtubeEventVideoPlaying,
                youtubeEventVideoPaused, youtubeEventVideoBuffering, youtubeEventVideoCued), jsInterface)
    }

    fun loadVideo(videoId: String,width: Int) {
        loadData(getYoutubePlayerHtml(videoId,width), mimeType, encoding)
    }

    private fun getYoutubePlayerHtml(videoId: String, width: Int): String {
        return "<html>\n" +
                "  <body>\n" +
                "    <div id=\"player\"" +
                "style =\"margin-top : -2%;margin-left : -2%;\"></div>\n" +
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
                "          height: 200,\n" +
                "          width: '${width}',\n" +
                "          videoId: '${videoId}',\n" +
                "          playerVars: {\n" +
                "            'rel': 0\n" +
                "          }," +
                "          events: {\n" +
                "            'onStateChange': onPlayerStateChange\n" +
                "          }\n" +
                "        });\n" +
                "      }\n" +
                "\n" +
                "      function onPlayerStateChange(event) {\n" +
                "          jsInterface.onStateChanged(event.data, player.getCurrentTime());\n" +
                "      }\n" +
                "    </script>\n" +
                "  </body>\n" +
                "</html>"
    }

}
