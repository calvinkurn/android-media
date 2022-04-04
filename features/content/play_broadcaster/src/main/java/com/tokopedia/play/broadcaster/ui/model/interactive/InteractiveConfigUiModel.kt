package com.tokopedia.play.broadcaster.ui.model.interactive

import com.tokopedia.play.broadcaster.ui.model.game.GameType

/**
 * Created by jegul on 07/07/21
 */
data class GameConfigUiModel(
        val tapTapConfig: TapTapConfigUiModel,
        val quizConfig: QuizConfigUiModel,
) {

        fun isNoGameActive(): Boolean = !tapTapConfig.isActive && !quizConfig.isActive

        fun generateGameTypeList(): List<GameType> {
                return mutableListOf<GameType>().apply {
                        if(tapTapConfig.isActive) add(GameType.Taptap)
                        if(quizConfig.isActive) add(GameType.Quiz)
                }
        }

        companion object {
                fun empty() = GameConfigUiModel(
                        tapTapConfig = TapTapConfigUiModel.empty(),
                        quizConfig = QuizConfigUiModel.empty()
                )
        }
}

data class TapTapConfigUiModel(
        val isActive: Boolean,
        val nameGuidelineHeader: String,
        val nameGuidelineDetail: String,
        val timeGuidelineHeader: String,
        val timeGuidelineDetail: String,
        val durationInMs: Long,
        val availableStartTimeInMs: List<Long>,
) {
        companion object {
                fun empty() = TapTapConfigUiModel(
                        isActive = false,
                        nameGuidelineHeader = "",
                        nameGuidelineDetail = "",
                        timeGuidelineHeader = "",
                        timeGuidelineDetail = "",
                        durationInMs = 0,
                        availableStartTimeInMs = emptyList(),
                )
        }
}

data class QuizConfigUiModel(
        val isActive: Boolean,
        val maxTitleLength: Int,
        val maxChoicesCount: Int,
        val minChoicesCount: Int,
        val maxRewardLength: Int,
        val maxChoiceLength: Int,
        val availableStartTimeInMs: List<Long>,
        val eligibleStartTimeInMs: List<Long>,
) {
        companion object {
                fun empty() = QuizConfigUiModel(
                        isActive = false,
                        maxTitleLength = 0,
                        maxChoicesCount = 0,
                        minChoicesCount = 0,
                        maxRewardLength = 0,
                        maxChoiceLength = 0,
                        availableStartTimeInMs = emptyList(),
                        eligibleStartTimeInMs =  emptyList(),
                )
        }
}