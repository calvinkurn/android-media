package com.tokopedia.play.view.wrapper

/**
 * Created by jegul on 18/12/19
 */
sealed class LoginStateEvent {

    data class InteractionAllowed(val event: InteractionEvent) : LoginStateEvent()
    object NeedLoggedIn : LoginStateEvent()
}