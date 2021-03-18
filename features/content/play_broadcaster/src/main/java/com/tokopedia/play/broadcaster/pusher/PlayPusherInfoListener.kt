package com.tokopedia.play.broadcaster.pusher


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

    fun onError() {
        // optional body
    }
}