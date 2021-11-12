package com.tokopedia.play.broadcaster.ui.model.pusher

import com.tokopedia.play.broadcaster.pusher.PlayLivePusherMediatorState


/**
 * Created by mzennis on 20/06/21.
 */
sealed class PlayLiveLogState {
    data class Init(
        val ingestUrl: String,
        val videoWidth: Int,
        val videoHeight: Int,
        val fps: Float,
        val bitrate: Int,
    ): PlayLiveLogState()
    data class Changed(val state: PlayLivePusherMediatorState): PlayLiveLogState()
}


