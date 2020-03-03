package com.tokopedia.play_common.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.play_common.player.TokopediaPlayManager

/**
 * Created by jegul on 11/12/19
 */
class PlayLifecycleObserver(
        private val playManager: TokopediaPlayManager
) : LifecycleObserver {

    @Volatile
    private var isVideoPlaying = playManager.isVideoPlaying()

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        synchronized(this) {
            isVideoPlaying = playManager.isVideoPlaying()
            if (isVideoPlaying) playManager.pauseCurrentVideo()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        synchronized(this) {
            if (isVideoPlaying) playManager.resumeCurrentVideo()
        }
    }
}