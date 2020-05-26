package com.tokopedia.play.broadcaster.pusher

import android.content.Context


/**
 * Created by mzennis on 25/05/20.
 */
class PlayPusherCountDownTimerNoop(val context: Context,
                                   private val maxLiveStreamDuration: Long = 0) {

    fun addCallback(callback: PlayPusherCountDownTimerCallback) {
    }

    fun start() {

    }

    fun stop() {

    }

}