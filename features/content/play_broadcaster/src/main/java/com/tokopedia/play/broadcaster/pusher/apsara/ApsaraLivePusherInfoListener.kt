package com.tokopedia.play.broadcaster.pusher.apsara


/**
 * Created by mzennis on 22/09/20.
 */
interface ApsaraLivePusherInfoListener {
    fun onPushed(activeStatus: ApsaraLivePusherActiveStatus)
    fun onPaused()
    fun onStop()
    fun onRestarted()
    fun onError(errorStatus: ApsaraLivePusherErrorStatus)
}