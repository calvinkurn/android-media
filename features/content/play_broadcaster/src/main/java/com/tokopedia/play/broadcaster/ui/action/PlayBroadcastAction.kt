package com.tokopedia.play.broadcaster.ui.action

import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_UNKNOWN
import com.tokopedia.play.broadcaster.pusher.state.PlayBroadcasterState
import com.tokopedia.play.broadcaster.ui.model.beautification.FaceFilterUiModel
import com.tokopedia.play.broadcaster.ui.model.beautification.PresetFilterUiModel
import com.tokopedia.content.product.picker.seller.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.game.GameType
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizFormDataUiModel
import com.tokopedia.play.broadcaster.ui.model.livetovod.TickerBottomSheetPage
import com.tokopedia.play.broadcaster.ui.model.livetovod.TickerBottomSheetType
import com.tokopedia.content.product.picker.seller.model.product.ProductUiModel
import com.tokopedia.play_common.model.ui.QuizChoicesUiModel
import java.util.*

/**
 * Created by jegul on 12/10/21
 */
sealed interface PlayBroadcastAction {

    object EditPinnedMessage : PlayBroadcastAction
    data class SetPinnedMessage(val message: String) : PlayBroadcastAction
    object CancelEditPinnedMessage : PlayBroadcastAction

    data class SetProduct(val productTagSectionList: List<ProductTagSectionUiModel>) :
        PlayBroadcastAction

    data class SetSchedule(val date: Date) : PlayBroadcastAction
    object DeleteSchedule : PlayBroadcastAction
    object SuccessOnBoardingUGC : PlayBroadcastAction
    data class GetConfiguration(val selectedType: String = TYPE_UNKNOWN) : PlayBroadcastAction
    data class SwitchAccount(val needLoading: Boolean = true) : PlayBroadcastAction

    data class GetTickerBottomSheetConfig(val page: TickerBottomSheetPage) : PlayBroadcastAction
    data class SetLiveToVodPref(val type: TickerBottomSheetType, val page: TickerBottomSheetPage) : PlayBroadcastAction

    /** Game */
    data class ClickGameOption(val gameType: GameType) : PlayBroadcastAction

    /**
     * Giveaway
     */
    object GiveawayUpcomingEnded : PlayBroadcastAction
    object GiveawayOngoingEnded : PlayBroadcastAction
    data class CreateGiveaway(
        val title: String,
        val durationInMs: Long,
    ) : PlayBroadcastAction

    /** Quiz */
    object ClickBackOnQuiz : PlayBroadcastAction
    object ClickNextOnQuiz : PlayBroadcastAction
    data class InputQuizTitle(val title: String) : PlayBroadcastAction
    data class InputQuizOption(val order: Int, val text: String) : PlayBroadcastAction
    data class SelectQuizOption(val order: Int) : PlayBroadcastAction
    data class SelectQuizDuration(val duration: Long) : PlayBroadcastAction
    data class SaveQuizData(val quizFormData: QuizFormDataUiModel) : PlayBroadcastAction
    object SubmitQuizForm : PlayBroadcastAction
    object QuizEnded : PlayBroadcastAction
    object ClickOngoingWidget : PlayBroadcastAction
    object ClickRefreshQuizDetailBottomSheet : PlayBroadcastAction
    object ClickBackOnChoiceDetail : PlayBroadcastAction
    object ClickRefreshQuizOption : PlayBroadcastAction
    object DismissQuizDetailBottomSheet : PlayBroadcastAction
    data class ClickQuizChoiceOption(val choice: QuizChoicesUiModel) : PlayBroadcastAction
    object LoadMoreCurrentChoiceParticipant : PlayBroadcastAction
    object ClickGameResultWidget : PlayBroadcastAction
    object Ignore : PlayBroadcastAction

    /** Pin Product*/
    data class ClickPinProduct(val product: ProductUiModel) : PlayBroadcastAction

    data class BroadcastStateChanged(val state: PlayBroadcasterState) : PlayBroadcastAction

    data class SetCoverUploadedSource(
        val source: Int
    ) : PlayBroadcastAction

    object SetShowSetupCoverCoachMark : PlayBroadcastAction
    object ResetUploadState : PlayBroadcastAction

    /** Beautification */
    object ResetBeautification : PlayBroadcastAction

    data class SelectFaceFilterOption(val faceFilter: FaceFilterUiModel) : PlayBroadcastAction
    data class ChangeFaceFilterValue(val newValue: Int) : PlayBroadcastAction

    data class SelectPresetOption(val preset: PresetFilterUiModel) : PlayBroadcastAction
    data class ChangePresetValue(val newValue: Int) : PlayBroadcastAction
    object RemoveBeautificationMenu : PlayBroadcastAction

    data class SendErrorLog(
        val throwable: Throwable,
    ) : PlayBroadcastAction
}
