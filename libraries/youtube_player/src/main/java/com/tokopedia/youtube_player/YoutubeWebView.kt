package com.tokopedia.youtube_player

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient

private const val MIME_TYPE = "text/html"
private const val ENCODING= "UTF-8"
private const val DELAY_TO_MIMIC_CLICK = 1000
private const val BASE_URL_YOUTUBE = "https://www.youtube.com"

class YoutubeWebView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : WebView(context, attrs, defStyleAttr) {
    var videoId: String? = null
    var youtubeJSInterface: YoutubeWebViewInterface? = null
    private var dispatchDownEvent:Boolean = false
    private var userDownEvent:Boolean = false
    private var jsInterface: String = "jsInterface"
    var isPlayerReady:Boolean = false
    var customViewInterface: YoutubeCustomViewListener? = null
    private val mainThread: Handler = Handler(Looper.getMainLooper())

    @SuppressLint("SetJavaScriptEnabled")
    fun initialize(
        youtubeEventVideoEnded: YoutubeWebViewEventListener.EventVideoEnded? = null,
        youtubeEventVideoPlaying: YoutubeWebViewEventListener.EventVideoPlaying? = null,
        youtubeEventVideoPaused: YoutubeWebViewEventListener.EventVideoPaused? = null,
        youtubeEventVideoBuffering: YoutubeWebViewEventListener.EventVideoBuffering? = null,
        youtubeEventVideoCued: YoutubeWebViewEventListener.EventVideoCued? = null,
        playerReady: YoutubeWebViewEventListener.EventPlayerReady? = null
    ) {
        settings.apply {
            javaScriptEnabled = true
            mediaPlaybackRequiresUserGesture = false
        }
        clearCache(true)
        clearHistory()
        setupTouchListener()
        val youtubeJSInterface = YoutubeWebViewInterface(
            youtubeEventVideoEnded, youtubeEventVideoPlaying,
            youtubeEventVideoPaused, youtubeEventVideoBuffering, youtubeEventVideoCued, playerReady
        )
        this.youtubeJSInterface = youtubeJSInterface
        addJavascriptInterface(youtubeJSInterface, jsInterface)
        setUpWebViewClient()
        mainThread.post {
            loadDataWithBaseURL(BASE_URL_YOUTUBE, getYoutubePlayerHtml(), MIME_TYPE, ENCODING, null)
        }
    }

    private fun setUpWebViewClient() {
        webChromeClient = object : WebChromeClient() {
            override fun onShowCustomView(view: View, callback: CustomViewCallback?) {
                super.onShowCustomView(view, callback)
                customViewInterface?.onEnterFullScreen(view)
            }

            override fun onHideCustomView() {
                super.onHideCustomView()
                customViewInterface?.onExitFullScreen()
            }


        }

        webViewClient = object : WebViewClient(){
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                val asd = -2
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

    fun loadVideo(videoId: String, startSeconds: Int = 0) {
        this.videoId = videoId
        invokeFunction("loadVideo", "'$videoId'", "$startSeconds")
    }

    fun mute(){
        invokeFunction("mute")
    }

    fun unMute(){
        invokeFunction("unMute")
    }

    fun play() {
        invokeFunction("playVideo")
    }

    fun pause() {
        invokeFunction("pauseVideo")
    }

    private fun invokeFunction(function: String, vararg stringArgs: String) {
        if (isPlayerReady)
            mainThread.post { loadUrl("javascript:$function(${stringArgs.joinToString(",")})") }
    }

    private fun getYoutubePlayerHtml(): String {
        return """
            <!DOCTYPE html>
            <html>
              <style type="text/css">
                    html, body {
                        height: 100%;
                        width: 100%;
                        margin: 0;
                        padding: 0;
                        background-color: #000000;
                        overflow: hidden;
                        position: fixed;
                    }
                </style>

              <head>
                <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
                <!-- defer forces the library to execute after the html page is fully parsed. -->
                <!-- This is needed to avoid race conditions, where the library executes and calls `onYouTubeIframeAPIReady` before the page is fully parsed. -->
                <!-- See #873 on GitHub -->
                <script defer src="https://www.youtube.com/iframe_api"></script>
              </head>

              <body>
                <div id="player"></div>
              </body>

              <script type="text/javascript">
                var player;

                function onYouTubeIframeAPIReady() {

                	player = new YT.Player('player', {
                			
                    height: '100%',
                	width: '100%',

                    events: {
                	    onReady: function(event) { jsInterface.onReady() },
                		  onStateChange: function(event) { jsInterface.onStateChanged(event.data, player.getCurrentTime()) },
                		  onError: function(error) { console.log(error) }
                	  },

                  });
                }

                // JAVA to WEB functions

                function seekTo(startSeconds) {
                  player.seekTo(startSeconds, true);
                }

                function pauseVideo() {
                  player.pauseVideo();
                }

                function playVideo() {
                  player.playVideo();
                }

                function loadVideo(videoId, startSeconds) {
                  player.loadVideoById(videoId, startSeconds);
                }

                function cueVideo(videoId, startSeconds) {
                  player.cueVideoById(videoId, startSeconds);
                }

                function mute() {
                  player.mute();
                }

                function unMute() {
                  player.unMute();
                }

                function toggleFullscreen() {
                  player.toggleFullscreen();
                }

              </script>
            </html>
        """.trimIndent()
    }

    fun release() {
        mainThread.removeCallbacksAndMessages(null)
        destroy()
    }

}
