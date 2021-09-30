package com.tokopedia.play.animation.multiplelikes

/**
 * Created By : Jonathan Darwin on August 02, 2021
 */

sealed class PlayLikeBubbleMove {
    val threshold = 5

    object Right: PlayLikeBubbleMove()
    object Left: PlayLikeBubbleMove()
    data class Center(val next: PlayLikeBubbleMove, var counter: Int = 0): PlayLikeBubbleMove()
}