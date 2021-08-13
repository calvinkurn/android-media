package com.tokopedia.play.broadcaster.pusher.state


/**
 * Created by mzennis on 02/07/20.
 */
sealed class ApsaraLivePusherState {
    object Idle : ApsaraLivePusherState()
    object Connecting : ApsaraLivePusherState()
    object Start : ApsaraLivePusherState()
    object Resume : ApsaraLivePusherState()
    object Pause : ApsaraLivePusherState()
    object Stop : ApsaraLivePusherState()
    object Restart : ApsaraLivePusherState()
    object Recovered : ApsaraLivePusherState()
}