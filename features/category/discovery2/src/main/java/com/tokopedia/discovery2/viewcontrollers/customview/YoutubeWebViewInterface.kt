package com.tokopedia.discovery2.viewcontrollers.customview

import android.util.Log
import android.webkit.JavascriptInterface

private const val VIDEO_ENDED = 0
private const val VIDEO_PLAYING = 1
private const val VIDEO_PAUSED = 2
private const val VIDEO_BUFFERING = 3
private const val VIDEO_CUED = 5

class YoutubeWebViewInterface(private val youtubeEventVideoEnded: YoutubeWebViewEventListener.EventVideoEnded?,
                              private val youtubeEventVideoPlaying: YoutubeWebViewEventListener.EventVideoPlaying?,
                              private val youtubeEventVideoPaused: YoutubeWebViewEventListener.EventVideoPaused?,
                              private val youtubeEventVideoBuffering: YoutubeWebViewEventListener.EventVideoBuffering?,
                              private val youtubeEventVideoCued: YoutubeWebViewEventListener.EventVideoCued?,
                              private val playerReady: YoutubeWebViewEventListener.EventPlayerReady?) {

    @JavascriptInterface
    fun onStateChanged(event: Int, time: Int) {
        when (event) {
            VIDEO_ENDED -> {
                youtubeEventVideoEnded?.onVideoEnded(time)
            }
            VIDEO_PLAYING -> {
                youtubeEventVideoPlaying?.onVideoPlaying(time)
            }
            VIDEO_PAUSED -> {
                youtubeEventVideoPaused?.onVideoPaused(time)
            }
            VIDEO_BUFFERING -> {
                youtubeEventVideoBuffering?.onVideoBuffering()
            }
            VIDEO_CUED -> {
                youtubeEventVideoCued?.onVideoCued()
            }
        }
    }

    @JavascriptInterface
    fun onReady(){
        playerReady?.onPlayerReady()
    }

}