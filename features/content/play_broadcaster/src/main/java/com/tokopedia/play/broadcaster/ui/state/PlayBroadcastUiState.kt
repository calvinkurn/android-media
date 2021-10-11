package com.tokopedia.play.broadcaster.ui.state

import com.tokopedia.play.broadcaster.ui.model.TermsAndConditionUiModel

/**
 * Created by jegul on 04/10/21
 */
internal data class PlayBroadcastUiState(
    val channel: PlayChannelUiState,
)

internal data class PlayChannelUiState(
    val canStream: Boolean,
    val tnc: List<TermsAndConditionUiModel>,
)