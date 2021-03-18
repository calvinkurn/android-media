package com.tokopedia.play.broadcaster.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.play.broadcaster.pusher.ApsaraLivePusherWrapper


/**
 * Created by mzennis on 18/03/21.
 */
class PlayLivePusherObserver(private val livePusher: ApsaraLivePusherWrapper) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        livePusher.resume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        livePusher.pause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        livePusher.destroy()
    }
}