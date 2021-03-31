package com.tokopedia.play.broadcaster.pusher.observer

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.play.broadcaster.pusher.ApsaraLivePusherWrapper
import com.tokopedia.play.broadcaster.pusher.state.ApsaraLivePusherState


/**
 * Created by mzennis on 18/03/21.
 */
class PlayLivePusherObserver(
        private val apsaraLivePusherWrapper: ApsaraLivePusherWrapper
) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        if (apsaraLivePusherWrapper.pusherState is ApsaraLivePusherState.Pause) return
        apsaraLivePusherWrapper.resume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        apsaraLivePusherWrapper.pause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        apsaraLivePusherWrapper.destroy()
    }
}