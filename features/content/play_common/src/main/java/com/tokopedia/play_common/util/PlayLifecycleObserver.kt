package com.tokopedia.play_common.util

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.play_common.player.PlayVideoManager

/**
 * Created by jegul on 11/12/19
 */
class PlayLifecycleObserver(private val context: Context) : LifecycleObserver {

    private val playVideoManager: PlayVideoManager
        get() = PlayVideoManager.getInstance(context.applicationContext)

    @Volatile
    private var isVideoPlaying = playVideoManager.isVideoPlaying()

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        synchronized(this) {
            isVideoPlaying = playVideoManager.isVideoPlaying()

            playVideoManager.pauseCurrentVideo(true)
            playVideoManager.muteVideo(true)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        synchronized(this) {
            if (isVideoPlaying) playVideoManager.resumeCurrentVideo()
            playVideoManager.muteVideo(false)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        synchronized(this) {
            playVideoManager.stopPlayer()
        }
    }
}