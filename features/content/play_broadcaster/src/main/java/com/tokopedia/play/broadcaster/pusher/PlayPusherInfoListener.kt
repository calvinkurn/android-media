package com.tokopedia.play.broadcaster.pusher

import com.tokopedia.play.broadcaster.pusher.apsara.ApsaraLivePusherActiveStatus
import com.tokopedia.play.broadcaster.pusher.apsara.ApsaraLivePusherErrorStatus


/**
 * Created by mzennis on 22/09/20.
 */
interface PlayPusherInfoListener {
    fun onStarted()
    fun onPushed(activeStatus: ApsaraLivePusherActiveStatus)
    fun onPaused()
    fun onStop()
    fun onRestarted()
    fun onError(errorStatus: ApsaraLivePusherErrorStatus)
}