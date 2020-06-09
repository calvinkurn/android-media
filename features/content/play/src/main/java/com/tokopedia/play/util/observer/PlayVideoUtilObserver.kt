package com.tokopedia.play.util.observer

import android.app.Activity
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.play.util.video.PlayVideoUtil

/**
 * Created by jegul on 24/04/20
 */
class PlayVideoUtilObserver(
        private val context: Context,
        private val videoUtil: PlayVideoUtil
) : LifecycleObserver {

    private val isChangingConfig: Boolean
        get() {
            return try {
                val activity = context as Activity?
                activity?.isChangingConfigurations ?: throw IllegalStateException("Activity is null")
            } catch (e: Exception) {
                false
            }
        }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        if (!isChangingConfig) {
            videoUtil.clearImage()
        }
    }
}