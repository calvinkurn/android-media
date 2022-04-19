package com.tokopedia.play_common.model.mapper

import com.tokopedia.play_common.domain.model.interactive.GetCurrentInteractiveResponse
import com.tokopedia.play_common.domain.model.interactive.GiveawayResponse
import com.tokopedia.play_common.domain.model.interactive.QuizResponse
import com.tokopedia.play_common.model.dto.interactive.InteractiveUiModel
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 12/04/22
 */
class PlayInteractiveMapper @Inject constructor() {

    fun mapInteractive(data: GetCurrentInteractiveResponse.Data): InteractiveUiModel {
        val waitingDuration = TimeUnit.SECONDS.toMillis(data.meta.waitingDuration.toLong()),
        return when (data.meta.active) {
            TYPE_GIVEAWAY -> mapGiveaway(data.giveaway, waitingDuration)
            TYPE_QUIZ -> mapQuiz(data.quiz, waitingDuration)
            else -> InteractiveUiModel.Unknown
        }
    }

    fun mapGiveaway(data: GiveawayResponse, waitingDurationInMillis: Long): InteractiveUiModel.Giveaway {
        return InteractiveUiModel.Giveaway(
            id = data.interactiveID,
            title = data.title,
            status = when (data.status) {
                STATUS_SCHEDULED -> InteractiveUiModel.Giveaway.Status.Upcoming(
                    startTime = Calendar.getInstance().apply {
                        add(Calendar.SECOND, data.countdownStart)
                    },
                    endTime = Calendar.getInstance().apply {
                        add(Calendar.SECOND, data.countdownStart + data.countdownEnd)
                    }
                )
                STATUS_LIVE -> InteractiveUiModel.Giveaway.Status.Ongoing(
                    endTime = Calendar.getInstance().apply {
                        add(Calendar.SECOND, data.countdownEnd)
                    }
                )
                STATUS_FINISHED -> InteractiveUiModel.Giveaway.Status.Finished
                else -> InteractiveUiModel.Giveaway.Status.Unknown
            },
            waitingDuration = waitingDurationInMillis,
        )
    }

    private fun mapQuiz(data: QuizResponse, waitingDurationInMillis: Long): InteractiveUiModel.Quiz {
        return InteractiveUiModel.Quiz(
            id = data.interactiveID,
            title = data.question,
            status = when (data.status) {
                STATUS_LIVE -> InteractiveUiModel.Quiz.Status.Ongoing(
                    endTime = Calendar.getInstance().apply {
                        add(Calendar.SECOND, data.countdownEnd)
                    }
                )
                STATUS_FINISHED -> InteractiveUiModel.Quiz.Status.Finished
                else -> InteractiveUiModel.Quiz.Status.Unknown
            },
            waitingDuration = waitingDurationInMillis,
        )
    }

    companion object {
        private const val TYPE_GIVEAWAY = "QUICK_TAP"
        private const val TYPE_QUIZ = "QUIZ"

        private const val STATUS_SCHEDULED = 0
        private const val STATUS_LIVE = 1
        private const val STATUS_FINISHED = 2
    }
}