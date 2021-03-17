package com.tokopedia.play.broadcaster.pusher.listener

import com.alivc.live.pusher.AlivcLivePushError
import com.alivc.live.pusher.AlivcLivePushErrorListener
import com.alivc.live.pusher.AlivcLivePusher
import com.tokopedia.play.broadcaster.pusher.ApsaraLivePusherWrapper
import com.tokopedia.play.broadcaster.pusher.ApsaraLivePusherWrapper.Companion.PLAY_PUSHER_ERROR_SYSTEM_ERROR


/**
 * Created by mzennis on 16/03/21.
 */
class ApsaraLivePusherErrorListenerImpl(
        private val listener: ApsaraLivePusherWrapper.Listener?
) : AlivcLivePushErrorListener {

    override fun onSystemError(pusher: AlivcLivePusher?, pusherError: AlivcLivePushError?) {
        listener?.onError(
                PLAY_PUSHER_ERROR_SYSTEM_ERROR,
                IllegalStateException("System Error")
        )
    }

    override fun onSDKError(pusher: AlivcLivePusher?, pusherError: AlivcLivePushError?) {
        listener?.onError(
                PLAY_PUSHER_ERROR_SYSTEM_ERROR,
                IllegalStateException("on SDK Error")
        )
    }
}