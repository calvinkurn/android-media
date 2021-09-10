package com.tokopedia.discovery2.viewcontrollers.customview

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.youtubeview.YoutubeCustomViewListener

private const val MIME_TYPE = "text/html"
private const val ENCODING= "UTF-8"
private const val ASPECT_RATIO = 0.5625 //9:16
private const val DELAY_TO_MIMIC_CLICK = 1000

class YoutubeWebView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : WebView(context, attrs, defStyleAttr) {
    var videoId: String? = null
    var youtubeJSInterface: YoutubeWebViewInterface? = null
    private var dispatchDownEvent:Boolean = false
    private var userDownEvent:Boolean = false
    private var jsInterface: String = "jsInterface"
    var isPlayerReady:Boolean = false
    var customViewInterface: YoutubeCustomViewListener? = null

    init {
        setupTouchListener()
        setUpWebViewClient()
        settings.javaScriptEnabled = true
        settings.mediaPlaybackRequiresUserGesture = false
    }

    private fun setUpWebViewClient() {
        webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                val uri = Uri.parse(url)
                if(uri.host == "www.youtube.com" || uri.host == "m.youtube.com") {
                    context?.let { openYoutubeVideo(it, uri) }
                    return true
                }
                return false
            }

            override fun onRenderProcessGone(view: WebView, detail: RenderProcessGoneDetail): Boolean {
                customViewInterface?.renderProcessKilled()
                return true
            }
        }
        webChromeClient = object : WebChromeClient() {
            override fun onShowCustomView(view: View, callback: CustomViewCallback?) {
                super.onShowCustomView(view, callback)
                customViewInterface?.onShowCustomView(view)
            }

            override fun onHideCustomView() {
                super.onHideCustomView()
                customViewInterface?.onHideCustomView()
            }
        }
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
                        val tempEvent = MotionEvent.obtain(event.downTime + DELAY_TO_MIMIC_CLICK,
                            event.eventTime + DELAY_TO_MIMIC_CLICK,
                                event.actionMasked, event.x, event.y, event.metaState)
                        val downEvent = MotionEvent.obtain(event.downTime, event.eventTime,
                                MotionEvent.ACTION_DOWN, event.x, event.y, event.metaState)
                        dispatchTouchEvent(downEvent)
                        dispatchTouchEvent(tempEvent)
                        v.performClick()
                        return@setOnTouchListener true
                    }else if(event.actionMasked == MotionEvent.ACTION_CANCEL) {
                        userDownEvent = false
                    }
                }
            }
            return@setOnTouchListener false
        }
    }

    fun setUpEventListeners(youtubeEventVideoEnded: YoutubeWebViewEventListener.EventVideoEnded? = null,
                            youtubeEventVideoPlaying: YoutubeWebViewEventListener.EventVideoPlaying? = null,
                            youtubeEventVideoPaused: YoutubeWebViewEventListener.EventVideoPaused? = null,
                            youtubeEventVideoBuffering: YoutubeWebViewEventListener.EventVideoBuffering? = null,
                            youtubeEventVideoCued: YoutubeWebViewEventListener.EventVideoCued? = null,
                            playerReady: YoutubeWebViewEventListener.EventPlayerReady? = null) {
        val youtubeJSInterface = YoutubeWebViewInterface(youtubeEventVideoEnded, youtubeEventVideoPlaying,
            youtubeEventVideoPaused, youtubeEventVideoBuffering, youtubeEventVideoCued,playerReady)
        this.youtubeJSInterface = youtubeJSInterface
        addJavascriptInterface(youtubeJSInterface, jsInterface)
    }

    fun loadVideo(videoId: String,width: Int) {
        this.videoId = videoId
        if (isPlayerReady) {
            loadUrl("javascript:cueVideo('$videoId', 0)")
        } else
            loadData(getYoutubePlayerHtml(videoId, width), MIME_TYPE, ENCODING)
    }

    fun mute(){
        if(isPlayerReady)
            loadUrl("javascript:mute()")
    }

    fun unMute(){
        if(isPlayerReady)
            loadUrl("javascript:unMute()")
    }

    fun play() {
        if(isPlayerReady)
            loadUrl("javascript:playVideo()")
    }

    fun pause() {
        if(isPlayerReady)
            loadUrl("javascript:pauseVideo()")
    }

    private fun getYoutubePlayerHtml(videoId: String, width: Int): String {
        val height = (ASPECT_RATIO*width).toInt()
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
                "          height: ${height},\n" +
                "          width: '${width}',\n" +
                "          videoId: '${videoId}',\n" +
                "          playerVars: {\n" +
                "            'rel': 0,\n" +
                "            'modestbranding': 1\n" +
                "          }," +
                "          events: {\n" +
                "            'onReady': onPlayerReady,\n"+
                "            'onStateChange': onPlayerStateChange\n" +
                "          }\n" +
                "        });\n" +
                "      }\n" +
                "\n" +
                "      function onPlayerStateChange(event) {\n" +
                "          jsInterface.onStateChanged(event.data, player.getCurrentTime());\n" +
                "      }\n" +
                "      function onPlayerReady(event) {\n" +
                "          jsInterface.onReady();\n" +
                "      }\n" +
                "      function cueVideo(videoId, startSeconds) {\n" +
                "          player.cueVideoById(videoId, startSeconds);\n" +
                "      }\n" +
                "      function loadVideo(videoId, startSeconds) {\n" +
                "          player.loadVideoById(videoId, startSeconds);\n" +
                "      }\n" +
                "      function playVideo() {\n" +
                "         player.playVideo();\n" +
                "      }\n"+
                "      function pauseVideo() {\n" +
                "         player.pauseVideo();\n" +
                "      }\n"+
                "      function mute() {\n" +
                "         player.mute();\n" +
                "      }\n" +
                "      function unMute() {\n" +
                "         player.unMute();\n" +
                "      }\n"+
                "    </script>\n" +
                "  </body>\n" +
                "</html>"
    }

    fun openYoutubeVideo(context: Context, uri: Uri) {
        val webIntent = Intent(
            Intent.ACTION_VIEW,
            uri
        )
        context.startActivity(webIntent)
    }


}
