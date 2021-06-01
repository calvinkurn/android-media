package com.tokopedia.discovery2.viewcontrollers.customview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient

private const val mimeType = "text/html"
private const val encoding = "UTF-8"

class YoutubeWebView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : WebView(context, attrs, defStyleAttr) {
    private val delayToMimicClick = 1000
    private var dispatchDownEvent:Boolean = false
    private var userDownEvent:Boolean = false
    private var jsInterface: String = "jsInterface"

    init {
        setupTouchListener()
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

    private fun setupTouchListener(){
        setOnTouchListener { v, event ->
            if(event.actionMasked == MotionEvent.ACTION_DOWN){
                if(dispatchDownEvent) {
                    dispatchDownEvent = false
                    userDownEvent = false
                    return@setOnTouchListener false
                }
                userDownEvent = true
                return@setOnTouchListener true
            }else{
                if(userDownEvent){
                    if(event.actionMasked == MotionEvent.ACTION_UP) {
                        dispatchDownEvent = true
                        val tempEvent = MotionEvent.obtain(event.downTime + delayToMimicClick, event.eventTime + delayToMimicClick,
                                event.actionMasked, event.x, event.y, event.metaState)
                        val downEvent = MotionEvent.obtain(event.downTime, event.eventTime,
                                MotionEvent.ACTION_DOWN, event.x, event.y, event.metaState)
                        dispatchTouchEvent(downEvent)
                        dispatchTouchEvent(tempEvent)
                        v.performClick()
                        return@setOnTouchListener true
                    }
                    userDownEvent = false
                }
            }
            return@setOnTouchListener false
        }
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
