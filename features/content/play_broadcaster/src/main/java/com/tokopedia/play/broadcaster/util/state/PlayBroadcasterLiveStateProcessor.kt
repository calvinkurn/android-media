package com.tokopedia.play.broadcaster.util.state

import com.tokopedia.play.broadcaster.pusher.ApsaraLivePusherWrapper
import com.tokopedia.play_common.util.coroutine.CoroutineDispatcherProvider
import kotlinx.coroutines.CoroutineScope


/**
 * Created by mzennis on 16/03/21.
 */
class PlayBroadcasterLiveStateProcessor(
        private val apsaraLivePusherWrapper: ApsaraLivePusherWrapper,
        private val dispatcher: CoroutineDispatcherProvider,
        private val scope: CoroutineScope
) {


}