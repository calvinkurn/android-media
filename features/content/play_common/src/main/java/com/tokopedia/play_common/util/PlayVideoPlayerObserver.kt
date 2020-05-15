package com.tokopedia.play_common.util

import android.app.Activity
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.play_common.player.PlayVideoManager

/**
 * Created by jegul on 11/12/19
 */
class PlayVideoPlayerObserver(private val context: Context) : LifecycleObserver {

    private val playVideoManager: PlayVideoManager
        get() = PlayVideoManager.getInstance(context.applicationContext)

    @Volatile
    private var isVideoPlaying = playVideoManager.isPlaying()

    private val isChangingConfig: Boolean
        get() {
            return try {
                val activity = context as Activity?
                activity?.isChangingConfigurations ?: throw IllegalStateException("Activity is null")
            } catch (e: Exception) {
                false
            }
        }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        synchronized(this) {
            if (!isChangingConfig) {
                isVideoPlaying = playVideoManager.isPlaying()

                playVideoManager.pause(true)
                playVideoManager.mute(true)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        synchronized(this) {
            if (!isChangingConfig) {
                playVideoManager.resumeOrPlayPreviousVideo(isVideoPlaying)
                playVideoManager.mute(false)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        synchronized(this) {
            if (!isChangingConfig) {
                playVideoManager.stop()
            }
        }
    }
}