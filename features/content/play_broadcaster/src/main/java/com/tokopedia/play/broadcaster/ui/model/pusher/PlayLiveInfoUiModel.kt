package com.tokopedia.play.broadcaster.ui.model.pusher


/**
 * Created by mzennis on 20/06/21.
 */
data class PlayLiveInfoUiModel(
    val ingestUrl: String,
    val videoWidth: Int,
    val videoHeight: Int,
    val fps: Float,
    val initialBitrate: Int
)
