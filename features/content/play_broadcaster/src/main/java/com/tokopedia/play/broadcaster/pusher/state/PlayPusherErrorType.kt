package com.tokopedia.play.broadcaster.pusher.state


/**
 * Created by mzennis on 17/06/20.
 */
sealed class PlayPusherErrorType {
    object UnSupportedDevice: PlayPusherErrorType()
    object ReachMaximumPauseDuration: PlayPusherErrorType()
    data class Throwable(val message: String): PlayPusherErrorType()
}