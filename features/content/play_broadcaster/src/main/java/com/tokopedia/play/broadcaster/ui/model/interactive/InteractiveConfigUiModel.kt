package com.tokopedia.play.broadcaster.ui.model.interactive

import com.tokopedia.play.broadcaster.ui.model.game.GameType

/**
 * Created by jegul on 07/07/21
 */
data class InteractiveConfigUiModel(
    val giveawayConfig: GiveawayConfigUiModel,
    val quizConfig: QuizConfigUiModel,
) {

    fun isNoGameActive(): Boolean = !giveawayConfig.isActive && !quizConfig.isActive

    @OptIn(ExperimentalStdlibApi::class)
    fun availableGameList(): List<GameType> {
        return buildList {
            if (quizConfig.isActive && quizConfig.availableStartTimeInMs.isNotEmpty()) add(GameType.Quiz)
            if (giveawayConfig.isActive && giveawayConfig.availableStartTimeInMs.isNotEmpty()) add(GameType.Giveaway)
        }
    }

    companion object {
        fun empty() = InteractiveConfigUiModel(
            giveawayConfig = GiveawayConfigUiModel.empty(),
            quizConfig = QuizConfigUiModel.empty(),
        )
    }
}

data class GiveawayConfigUiModel(
    val isActive: Boolean,
    val nameGuidelineHeader: String,
    val nameGuidelineDetail: String,
    val timeGuidelineHeader: String,
    val timeGuidelineDetail: String,
    val durationInMs: Long,
    val availableStartTimeInMs: List<Long>,
) {
    companion object {
        fun empty() = GiveawayConfigUiModel(
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
    val isGiftActive: Boolean,
    val maxTitleLength: Int,
    val maxChoicesCount: Int,
    val minChoicesCount: Int,
    val maxChoiceLength: Int,
    val availableStartTimeInMs: List<Long>,
    val eligibleStartTimeInMs: List<Long>,
) {
    companion object {
        fun empty() = QuizConfigUiModel(
            isActive = false,
            isGiftActive = false,
            maxTitleLength = 0,
            maxChoicesCount = 0,
            minChoicesCount = 0,
            maxChoiceLength = 0,
            availableStartTimeInMs = emptyList(),
            eligibleStartTimeInMs =  emptyList(),
        )
    }
}
