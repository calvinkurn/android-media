package com.tokopedia.play.broadcaster.pusher.apsara


/**
 * Created by mzennis on 22/09/20.
 */
interface ApsaraLivePusherInfoListener {
    fun onStarted()
    fun onResumed()
    fun onPaused()
    fun onStop()
    fun onRestarted()
    fun onRecovered()
    fun onError(errorStatus: ApsaraLivePusherErrorStatus)
}