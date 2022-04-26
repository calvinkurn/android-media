package com.tokopedia.play.broadcaster.ui.state

import com.tokopedia.play.broadcaster.ui.model.BroadcastScheduleUiModel
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.TermsAndConditionUiModel
import com.tokopedia.play.broadcaster.ui.model.game.GameType
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizFormDataUiModel
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizFormStateUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.InteractiveConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.InteractiveSetupUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.QuizConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.TapTapConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.pinnedmessage.PinnedMessageEditStatus
import com.tokopedia.play.broadcaster.ui.model.result.NetworkState
import com.tokopedia.play_common.model.dto.interactive.InteractiveUiModel
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
    val quizForm: QuizFormUiState,
    val interactive: InteractiveUiModel,
    val interactiveConfig: InteractiveConfigUiModel,
    val interactiveSetup: InteractiveSetupUiModel,
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
                quizForm = QuizFormUiState.Empty,
                interactive = InteractiveUiModel.Unknown,
                interactiveConfig = InteractiveConfigUiModel.empty(),
                interactiveSetup = InteractiveSetupUiModel.Empty,
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

data class QuizFormUiState(
    val quizFormData: QuizFormDataUiModel,
    val quizFormState: QuizFormStateUiModel,
    val isNeedToUpdateUI: Boolean,
) {
    companion object {
        val Empty: QuizFormUiState
            get() {
                return QuizFormUiState(
                    quizFormData = QuizFormDataUiModel(),
                    quizFormState = QuizFormStateUiModel.Nothing,
                    isNeedToUpdateUI = false,
                )
            }
    }
}