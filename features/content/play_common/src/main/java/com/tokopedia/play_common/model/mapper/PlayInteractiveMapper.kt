package com.tokopedia.play_common.model.mapper

import com.tokopedia.play_common.domain.model.interactive.GetCurrentInteractiveResponse
import com.tokopedia.play_common.domain.model.interactive.GiveawayResponse
import com.tokopedia.play_common.domain.model.interactive.QuizResponse
import com.tokopedia.play_common.model.dto.interactive.InteractiveUiModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 12/04/22
 */
class PlayInteractiveMapper @Inject constructor() {

    fun mapInteractive(data: GetCurrentInteractiveResponse.Data): InteractiveUiModel {
        return when (data.meta.active) {
            TYPE_GIVEAWAY -> mapGiveaway(data.giveaway)
            TYPE_QUIZ -> mapQuiz(data.quiz)
            else -> InteractiveUiModel.Unknown
        }
    }

    private fun mapGiveaway(data: GiveawayResponse): InteractiveUiModel.Giveaway {
        return InteractiveUiModel.Giveaway(
            id = data.interactiveID,
            title = data.title,
            status = when (data.status) {
                STATUS_SCHEDULED -> InteractiveUiModel.Giveaway.Status.Upcoming(
                    timeToStartInMs = TimeUnit.SECONDS.toMillis(
                        data.countdownStart.toLong()
                    ),
                    durationInMs = TimeUnit.SECONDS.toMillis(
                        data.countdownEnd.toLong() -
                                data.countdownStart.toLong()
                    )
                )
                STATUS_LIVE -> InteractiveUiModel.Giveaway.Status.Ongoing(
                    durationInMs = TimeUnit.SECONDS.toMillis(data.countdownEnd.toLong())
                )
                STATUS_FINISHED -> InteractiveUiModel.Giveaway.Status.Finished
                else -> InteractiveUiModel.Giveaway.Status.Unknown
            }
        )
    }

    private fun mapQuiz(data: QuizResponse): InteractiveUiModel.Quiz {
        return InteractiveUiModel.Quiz(
            id = data.interactiveID,
            title = data.question,
            status = when (data.status) {
                STATUS_LIVE -> InteractiveUiModel.Quiz.Status.Ongoing(
                    durationInMs = TimeUnit.SECONDS.toMillis(data.countdownEnd.toLong())
                )
                STATUS_FINISHED -> InteractiveUiModel.Quiz.Status.Finished
                else -> InteractiveUiModel.Quiz.Status.Unknown
            }
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