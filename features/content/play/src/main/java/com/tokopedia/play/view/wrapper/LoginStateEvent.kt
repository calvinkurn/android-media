package com.tokopedia.play.view.wrapper

/**
 * Created by jegul on 18/12/19
 */
sealed class LoginStateEvent {

    abstract val event: InteractionEvent

    data class InteractionAllowed(override val event: InteractionEvent) : LoginStateEvent()
    data class NeedLoggedIn(override val event: InteractionEvent) : LoginStateEvent()
}