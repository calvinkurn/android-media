package com.tokopedia.play_common.view.game.quiz

/**
 * @author by astidhiyaa on 06/04/22
 */
sealed class PlayQuizOptionState {

    data class Default(
        val alphabet: Char
    ): PlayQuizOptionState()

    data class Answered(
        val isCorrect: Boolean
    ): PlayQuizOptionState()

    data class Other(
        val isCorrect: Boolean
    ): PlayQuizOptionState()

    data class Participant(
        val alphabet : Char,
        val isCorrect : Boolean,
        val count : String,
        val showArrow: Boolean,
    ): PlayQuizOptionState()

    object Unknown: PlayQuizOptionState()
}

