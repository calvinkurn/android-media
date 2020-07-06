package com.tokopedia.play.broadcaster.util.error

import com.tokopedia.play.broadcaster.pusher.state.PlayPusherErrorType


/**
 * Created by mzennis on 06/07/20.
 */
class PusherErrorThrowable(val mErrorType: PlayPusherErrorType? = null): Throwable()