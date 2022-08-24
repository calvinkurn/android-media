package com.tokopedia.play.broadcaster.ui.action

import com.tokopedia.play.broadcaster.pusher.state.PlayBroadcasterState
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import java.util.*
import com.tokopedia.play.broadcaster.ui.model.game.GameType
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizFormDataUiModel
import com.tokopedia.play_common.model.ui.LeaderboardGameUiModel
import com.tokopedia.play_common.model.ui.QuizChoicesUiModel

/**
 * Created by jegul on 12/10/21
 */
sealed interface PlayBroadcastAction {

    object EditPinnedMessage : PlayBroadcastAction
    data class SetPinnedMessage(val message: String) : PlayBroadcastAction
    object CancelEditPinnedMessage : PlayBroadcastAction

    data class SetCover(val cover: PlayCoverUiModel) : PlayBroadcastAction
    data class SetProduct(val productTagSectionList: List<ProductTagSectionUiModel>) :
        PlayBroadcastAction

    data class SetSchedule(val date: Date) : PlayBroadcastAction
    object DeleteSchedule : PlayBroadcastAction

    object ExitLive : PlayBroadcastAction

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
    data class InputQuizGift(val text: String) : PlayBroadcastAction
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
}