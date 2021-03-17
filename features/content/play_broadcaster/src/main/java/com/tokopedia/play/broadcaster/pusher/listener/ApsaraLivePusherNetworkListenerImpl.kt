package com.tokopedia.play.broadcaster.pusher.listener

import com.alivc.live.pusher.AlivcLivePushNetworkListener
import com.alivc.live.pusher.AlivcLivePusher
import com.tokopedia.play.broadcaster.pusher.ApsaraLivePusherWrapper
import com.tokopedia.play.broadcaster.pusher.ApsaraLivePusherWrapper.Companion.PLAY_PUSHER_ERROR_CONNECTION_FAILED
import com.tokopedia.play.broadcaster.pusher.ApsaraLivePusherWrapper.Companion.PLAY_PUSHER_ERROR_NETWORK_LOSS
import com.tokopedia.play.broadcaster.pusher.ApsaraLivePusherWrapper.Companion.PLAY_PUSHER_ERROR_NETWORK_POOR
import com.tokopedia.play.broadcaster.pusher.ApsaraLivePusherWrapper.Companion.PLAY_PUSHER_ERROR_RECONNECTION_FAILED
import com.tokopedia.play.broadcaster.pusher.state.ApsaraLivePusherState


/**
 * Created by mzennis on 16/03/21.
 */
class ApsaraLivePusherNetworkListenerImpl(
        private val listener: ApsaraLivePusherWrapper.Listener?
) : AlivcLivePushNetworkListener {

    override fun onNetworkRecovery(pusher: AlivcLivePusher?) {
        // Indicates that the network is recovered.
    }

    override fun onSendMessage(pusher: AlivcLivePusher?) {
    }

    override fun onReconnectFail(pusher: AlivcLivePusher?) {
        listener?.onError(
                PLAY_PUSHER_ERROR_RECONNECTION_FAILED,
                IllegalStateException("Reconnect Failed")
        )
    }

    override fun onConnectionLost(pusher: AlivcLivePusher?) {
        listener?.onError(
                PLAY_PUSHER_ERROR_NETWORK_LOSS,
                IllegalStateException("Connection Loss")
        )
    }

    override fun onSendDataTimeout(pusher: AlivcLivePusher?) {
        listener?.onError(
                PLAY_PUSHER_ERROR_NETWORK_LOSS,
                IllegalStateException("Send Data Timeout")
        )
    }

    override fun onConnectFail(pusher: AlivcLivePusher?) {
        listener?.onError(
                PLAY_PUSHER_ERROR_CONNECTION_FAILED,
                IllegalStateException("Connect Failed")
        )
    }

    override fun onPacketsLost(pusher: AlivcLivePusher?) {
        listener?.onError(
                PLAY_PUSHER_ERROR_NETWORK_LOSS,
                IllegalStateException("Packets Failed")
        )
    }

    override fun onReconnectStart(pusher: AlivcLivePusher?) {
        // Indicates that a reconnection starts.
    }

    override fun onReconnectSucceed(pusher: AlivcLivePusher?) {
        listener?.onConnectionStateChanged(ApsaraLivePusherState.Recovered)
    }

    override fun onPushURLAuthenticationOverdue(pusher: AlivcLivePusher?): String {
        return ""
    }

    override fun onNetworkPoor(pusher: AlivcLivePusher?) {
        listener?.onError(
                PLAY_PUSHER_ERROR_NETWORK_POOR,
                IllegalStateException("Network Poor")
        )
    }
}