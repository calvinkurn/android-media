package com.tokopedia.play.broadcaster.pusher.state


/**
 * Created by mzennis on 27/05/20.
 */
sealed class PlayPusherNetworkState {
    object Poor : PlayPusherNetworkState()
    object Loss : PlayPusherNetworkState()
    object Recover : PlayPusherNetworkState()
    object ConnectFailed : PlayPusherNetworkState()
    object ReconnectFailed : PlayPusherNetworkState()
    object ReConnectStart : PlayPusherNetworkState()
    object ReConnectSucceed : PlayPusherNetworkState()
}