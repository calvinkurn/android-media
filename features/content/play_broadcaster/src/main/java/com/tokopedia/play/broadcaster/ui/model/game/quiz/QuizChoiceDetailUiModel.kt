package com.tokopedia.play.broadcaster.ui.model.game.quiz

import com.tokopedia.play.broadcaster.ui.model.game.GameParticipantUiModel
import com.tokopedia.play_common.model.ui.QuizChoicesUiModel

data class QuizChoiceDetailUiModel(
    val choice: QuizChoicesUiModel,
    val cursor: String,
    val winners: List<GameParticipantUiModel> = emptyList(),
    val participants: List<GameParticipantUiModel> = emptyList(),
    )




