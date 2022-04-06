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

    data class Result(
        val isCorrect: Boolean
    ): PlayQuizOptionState()

    object Loading: PlayQuizOptionState()
    object Unknown: PlayQuizOptionState()
}

