package com.tokopedia.play.broadcaster.ui.state

import com.tokopedia.play.broadcaster.ui.model.TermsAndConditionUiModel
import com.tokopedia.play.broadcaster.ui.model.etalase.EtalaseProductListMap
import com.tokopedia.play.broadcaster.ui.model.pinnedmessage.PinnedMessageEditStatus

/**
 * Created by jegul on 04/10/21
 */
internal data class PlayBroadcastUiState(
    val channel: PlayChannelUiState,
    val pinnedMessage: PinnedMessageUiState,
    val selectedProduct: EtalaseProductListMap,
    val isExiting: Boolean,
)

internal data class PlayChannelUiState(
    val canStream: Boolean,
    val tnc: List<TermsAndConditionUiModel>,
)

internal data class PinnedMessageUiState(
	val message: String,
	val editStatus: PinnedMessageEditStatus,
)

