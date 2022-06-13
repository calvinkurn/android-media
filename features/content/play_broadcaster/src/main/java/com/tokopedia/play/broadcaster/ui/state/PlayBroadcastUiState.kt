package com.tokopedia.play.broadcaster.ui.state

import com.tokopedia.play.broadcaster.ui.model.BroadcastScheduleUiModel
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.TermsAndConditionUiModel
import com.tokopedia.play.broadcaster.ui.model.pinnedmessage.PinnedMessageEditStatus
import com.tokopedia.play.broadcaster.ui.model.result.NetworkState
import java.util.*

/**
 * Created by jegul on 04/10/21
 */
data class PlayBroadcastUiState(
    val channel: PlayChannelUiState,
    val pinnedMessage: PinnedMessageUiState,
    val selectedProduct: List<ProductTagSectionUiModel>,
    val schedule: ScheduleUiModel,
    val isExiting: Boolean,
) {
    companion object {
        val Empty: PlayBroadcastUiState
            get() = PlayBroadcastUiState(
                channel = PlayChannelUiState(
                    canStream = true,
                    tnc = emptyList(),
                ),
                pinnedMessage = PinnedMessageUiState(
                    message = "",
                    editStatus = PinnedMessageEditStatus.Nothing,
                ),
                selectedProduct = emptyList(),
                schedule = ScheduleUiModel.Empty,
                isExiting = false,
            )
    }
}

data class PlayChannelUiState(
    val canStream: Boolean,
    val tnc: List<TermsAndConditionUiModel>,
)

data class PinnedMessageUiState(
	val message: String,
	val editStatus: PinnedMessageEditStatus,
)

data class ScheduleUiModel(
    val schedule: BroadcastScheduleUiModel,
    val state: NetworkState,
    val config: ScheduleConfigUiModel,
    val canSchedule: Boolean,
) {
    companion object {
        val Empty: ScheduleUiModel
            get() = ScheduleUiModel(
                schedule = BroadcastScheduleUiModel.NoSchedule,
                state = NetworkState.Success,
                config = ScheduleConfigUiModel.Empty,
                canSchedule = false,
            )
    }
}

data class ScheduleConfigUiModel(
    val maxDate: Date,
    val minDate: Date,
    val defaultDate: Date,
) {
    companion object {
        val Empty: ScheduleConfigUiModel
            get() {
                val defaultDate = Date()
                return ScheduleConfigUiModel(
                    maxDate = defaultDate,
                    minDate = defaultDate,
                    defaultDate = defaultDate,
                )
            }
    }
}
