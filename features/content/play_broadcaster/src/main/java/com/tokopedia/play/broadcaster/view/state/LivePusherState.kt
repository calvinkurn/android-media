package com.tokopedia.play.broadcaster.view.state

import com.tokopedia.play.broadcaster.pusher.apsara.ApsaraLivePusherErrorStatus


/**
 * Created by mzennis on 22/09/20.
 */
sealed class LivePusherState {
    object Connecting : LivePusherState()
    object Started : LivePusherState()
    object Recovered : LivePusherState()
    data class Stopped(val shouldNavigate: Boolean) : LivePusherState()
    data class Error(val errorStatus: ApsaraLivePusherErrorStatus) : LivePusherState()
}