package com.tokopedia.youtube_player

import android.annotation.SuppressLint
import android.content.Context
import android.net.http.SslError
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient

private const val MIME_TYPE = "text/html"
private const val ENCODING = "UTF-8"
private const val BASE_URL_YOUTUBE = "https://www.youtube.com"

class YoutubeWebView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    WebView(context, attrs, defStyleAttr) {
    var videoId: String? = null
    var youtubeJSInterface: YoutubeWebViewInterface? = null
    private var jsInterface: String = "jsInterface"
    var isPlayerReady: Boolean = false
    var customViewInterface: YoutubeCustomViewListener? = null
    private val mainThread: Handler = Handler(Looper.getMainLooper())

    @SuppressLint("SetJavaScriptEnabled")
    fun initialize(
        youtubeEventVideoEnded: YoutubeWebViewEventListener.EventVideoEnded? = null,
        youtubeEventVideoPlaying: YoutubeWebViewEventListener.EventVideoPlaying? = null,
        youtubeEventVideoPaused: YoutubeWebViewEventListener.EventVideoPaused? = null,
        youtubeEventVideoBuffering: YoutubeWebViewEventListener.EventVideoBuffering? = null,
        youtubeEventVideoCued: YoutubeWebViewEventListener.EventVideoCued? = null,
        youtubeEventError: YoutubeWebViewEventListener.EventError? = null,
        playerReady: YoutubeWebViewEventListener.EventPlayerReady? = null,
        options: PlayerOptions = PlayerOptions()
    ) {
        settings.apply {
            javaScriptEnabled = true
            mediaPlaybackRequiresUserGesture = false
            cacheMode = WebSettings.LOAD_DEFAULT
        }
        val youtubeJSInterface = YoutubeWebViewInterface(
            youtubeEventVideoEnded,
            youtubeEventVideoPlaying,
            youtubeEventVideoPaused,
            youtubeEventVideoBuffering,
            youtubeEventVideoCued,
            youtubeEventError,
            playerReady
        )
        this.youtubeJSInterface = youtubeJSInterface
        addJavascriptInterface(youtubeJSInterface, jsInterface)
        loadDataWithBaseURL(BASE_URL_YOUTUBE, getYoutubePlayerHtml(options), MIME_TYPE, ENCODING, null)
        setUpWebViewClient()
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

        webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest,
                error: WebResourceError
            ) {}

            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
            ) {}

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {}
        }
    }

    fun loadVideo(videoId: String, startSeconds: Int = 0) {
        this.videoId = videoId
        invokeFunction("loadVideo", "'$videoId'", "$startSeconds")
    }

    fun mute() {
        invokeFunction("mute")
    }

    fun unMute() {
        invokeFunction("unMute")
    }

    fun play() {
        invokeFunction("playVideo")
    }

    fun pause() {
        invokeFunction("pauseVideo")
    }

    private fun invokeFunction(function: String, vararg stringArgs: String) {
        if (isPlayerReady) {
            mainThread.post { loadUrl("javascript:$function(${stringArgs.joinToString(",")})") }
        }
    }

    private fun getYoutubePlayerHtml(options: PlayerOptions): String {
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
                    playerVars: {
                        'fs': ${if (options.enableFullScreen) 1 else 0}
                    },
                    events: {
                        onReady: function(event) { jsInterface.onReady() },
                        onStateChange: function(event) { jsInterface.onStateChanged(event.data, player.getCurrentTime()) },
                        onError: function(error) { 
                            jsInterface.onError(error.data)
                            console.log(error)
                        }
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

    private fun release() {
        mainThread.removeCallbacksAndMessages(null)
    }

    override fun destroy() {
        release()
        super.destroy()
    }

    fun releaseWebView() {
        removeAllViews()
        destroy()
        webChromeClient = null
    }

    data class PlayerOptions(
        val enableFullScreen: Boolean = true
    )
}
