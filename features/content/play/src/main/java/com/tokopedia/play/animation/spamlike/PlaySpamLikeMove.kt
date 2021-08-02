package com.tokopedia.play.animation.spamlike

/**
 * Created By : Jonathan Darwin on August 02, 2021
 */

sealed class PlaySpamLikeMove {
    val threshold = 5

    object Right: PlaySpamLikeMove()
    object Left: PlaySpamLikeMove()
    data class Center(val next: PlaySpamLikeMove, var counter: Int = 0): PlaySpamLikeMove()
}