package com.tokopedia.play.broadcaster.pusher

import com.tokopedia.play.broadcaster.pusher.apsara.ApsaraLivePusherErrorStatus


/**
 * Created by mzennis on 22/09/20.
 */
interface PlayPusherInfoListener {
    fun onStarted()
    fun onPushed()
    fun onPaused()
    fun onStop()
    fun onRestarted()
    fun onRecovered()
    fun onError(errorStatus: ApsaraLivePusherErrorStatus)
}