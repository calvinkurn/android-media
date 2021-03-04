package com.tokopedia.play.broadcaster.pusher

import com.tokopedia.play.broadcaster.pusher.apsara.ApsaraLivePusherErrorStatus


/**
 * Created by mzennis on 22/09/20.
 */
interface PlayPusherInfoListener {

    fun onStarted() {
        // optional body
    }

    fun onResumed() {
        // optional body
    }

    fun onPaused() {
        // optional body
    }

    fun onStop() {
        // optional body
    }

    fun onRecovered() {
        // optional body
    }

    fun onError(errorStatus: ApsaraLivePusherErrorStatus) {
        // optional body
    }
}