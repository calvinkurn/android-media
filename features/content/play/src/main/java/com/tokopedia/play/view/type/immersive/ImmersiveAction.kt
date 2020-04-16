package com.tokopedia.play.view.type.immersive

/**
 * Created by jegul on 15/04/20
 */
sealed class ImmersiveAction {

    abstract val durationInMs: Long

    data class Enter(override val durationInMs: Long) : ImmersiveAction()
    data class Exit(override val durationInMs: Long, val delayBeforeFadeOutInMs: Long) : ImmersiveAction()
}