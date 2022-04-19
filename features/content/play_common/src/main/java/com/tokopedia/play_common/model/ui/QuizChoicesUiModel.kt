package com.tokopedia.play_common.model.ui

import com.tokopedia.play_common.view.game.quiz.PlayQuizOptionState

/**
 * @author by astidhiyaa on 08/04/22
 */
data class QuizChoicesUiModel(
    val id: String,
    val text: String,
    val type: PlayQuizOptionState,
    val isLoading: Boolean = false,
)

