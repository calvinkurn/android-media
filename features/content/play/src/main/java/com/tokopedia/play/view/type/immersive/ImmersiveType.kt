package com.tokopedia.play.view.type.immersive

/**
 * Created by jegul on 15/04/20
 */
sealed class ImmersiveType {

    abstract val action: ImmersiveAction

    data class Full(override val action: ImmersiveAction) : ImmersiveType()
    data class Partial(override val action: ImmersiveAction) : ImmersiveType()
}