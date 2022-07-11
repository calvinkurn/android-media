package com.tokopedia.play_common.model.mapper

import com.tokopedia.play_common.domain.model.interactive.GetCurrentInteractiveResponse
import com.tokopedia.play_common.domain.model.interactive.GiveawayResponse
import com.tokopedia.play_common.domain.model.interactive.QuizResponse
import com.tokopedia.play_common.model.dto.interactive.InteractiveUiModel
import com.tokopedia.play_common.model.ui.QuizChoicesUiModel
import com.tokopedia.play_common.transformer.HtmlTextTransformer
import com.tokopedia.play_common.view.game.quiz.PlayQuizOptionState
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 12/04/22
 */
class PlayInteractiveMapper @Inject constructor(private val decodeHtml : HtmlTextTransformer) {

    fun mapInteractive(data: GetCurrentInteractiveResponse.Data): InteractiveUiModel {
        val waitingDuration = TimeUnit.SECONDS.toMillis(data.meta.waitingDuration.toLong())
        return when (data.meta.active) {
            TYPE_GIVEAWAY -> mapGiveaway(data.giveaway, waitingDuration)
            TYPE_QUIZ -> mapQuiz(data.quiz, waitingDuration)
            else -> InteractiveUiModel.Unknown
        }
    }

    fun mapGiveaway(data: GiveawayResponse, waitingDurationInMillis: Long): InteractiveUiModel.Giveaway {
        return InteractiveUiModel.Giveaway(
            id = data.interactiveID,
            title = decodeHtml.transform(data.title),
            status = when (data.status) {
                STATUS_SCHEDULED -> InteractiveUiModel.Giveaway.Status.Upcoming(
                    startTime = Calendar.getInstance().apply {
                        add(Calendar.SECOND, data.countdownStart)
                    },
                    endTime = Calendar.getInstance().apply {
                        add(Calendar.SECOND, data.countdownEnd)
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

    fun mapQuiz(data: QuizResponse, waitingDurationInMillis: Long): InteractiveUiModel.Quiz {
        return InteractiveUiModel.Quiz(
            id = data.interactiveID,
            title = decodeHtml.transform(data.question),
            status = when (data.status) {
                STATUS_LIVE -> InteractiveUiModel.Quiz.Status.Ongoing(
                    endTime = Calendar.getInstance().apply {
                        add(Calendar.SECOND, data.countdownEnd)
                    }
                )
                STATUS_FINISHED -> InteractiveUiModel.Quiz.Status.Finished
                else -> InteractiveUiModel.Quiz.Status.Unknown
            },
            listOfChoices = data.choices.mapIndexed { index: Int, item: QuizResponse.Choice ->
                QuizChoicesUiModel(
                    index,
                    item.id,
                    decodeHtml.transform(item.text),
                    when {
                        item.id == data.userChoice -> PlayQuizOptionState.Answered(isCorrect = item.isCorrect ?: false) //if user has already answered, the option that user chose
                        data.userChoice == "0" || data.userChoice.isEmpty() -> PlayQuizOptionState.Default(alphabet = generateAlphabet(index)) //if user has not choose answer
                        else -> PlayQuizOptionState.Other(isCorrect = item.isCorrect ?: false) //if user has already answered but the other options
                    }
                )
            },
            reward = decodeHtml.transform(data.prize),
            waitingDuration = waitingDurationInMillis,
        )
    }

    private fun generateAlphabet(index: Int) : Char = arrayOfAlphabet[index]
    private val arrayOfAlphabet = ('A'..'Z').toMutableList()

    companion object {
        private const val TYPE_GIVEAWAY = "QUICK_TAP"
        private const val TYPE_QUIZ = "QUIZ"

        private const val STATUS_SCHEDULED = 0
        private const val STATUS_LIVE = 1
        private const val STATUS_FINISHED = 2
    }
}