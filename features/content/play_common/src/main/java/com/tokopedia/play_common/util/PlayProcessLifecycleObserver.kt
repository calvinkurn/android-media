package com.tokopedia.play_common.util

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.play_common.player.PlayVideoManager

/**
 * Created by jegul on 26/03/20
 */
class PlayProcessLifecycleObserver(private val context: Context) : LifecycleObserver {

    private val playVideoManager: PlayVideoManager
        get() = PlayVideoManager.getInstance(context.applicationContext)

    private var isFirstTime = true

    private var isPreviouslyPlaying = true

    /**
     * Called when app is going to background
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        isPreviouslyPlaying = playVideoManager.isVideoPlaying()
        playVideoManager.stopPlayer(false)
    }

    /**
     * Called when app is going to foreground
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        if (!isFirstTime && isPreviouslyPlaying) {
            playVideoManager.resumeOrPlayPreviousVideo(isPreviouslyPlaying)
        }

        if (isFirstTime) isFirstTime = false
    }
}