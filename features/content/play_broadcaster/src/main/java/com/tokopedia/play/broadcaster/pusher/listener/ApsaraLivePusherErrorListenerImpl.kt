package com.tokopedia.play.broadcaster.pusher.listener

import com.alivc.live.pusher.AlivcLivePushError
import com.alivc.live.pusher.AlivcLivePushErrorListener
import com.alivc.live.pusher.AlivcLivePusher
import com.tokopedia.play.broadcaster.pusher.ApsaraLivePusherWrapper.Companion.PLAY_PUSHER_ERROR_SYSTEM_ERROR
import com.tokopedia.play.broadcaster.pusher.error.ApsaraFatalException
import com.tokopedia.play.broadcaster.pusher.state.ApsaraLivePusherStateProcessor


/**
 * Created by mzennis on 16/03/21.
 */
class ApsaraLivePusherErrorListenerImpl(
        private val livePusherStateProcessor: ApsaraLivePusherStateProcessor
) : AlivcLivePushErrorListener {

    override fun onSystemError(pusher: AlivcLivePusher?, pusherError: AlivcLivePushError?) {
        livePusherStateProcessor.onError(
                PLAY_PUSHER_ERROR_SYSTEM_ERROR,
                ApsaraFatalException("Something went wrong, please restart application")
        )
    }

    override fun onSDKError(pusher: AlivcLivePusher?, pusherError: AlivcLivePushError?) {
        livePusherStateProcessor.onError(
                PLAY_PUSHER_ERROR_SYSTEM_ERROR,
                ApsaraFatalException("Something went wrong, please restart application")
        )
    }
}