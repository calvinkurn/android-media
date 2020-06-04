package com.tokopedia.play.broadcaster.view.event


/**
 * Created by mzennis on 28/05/20.
 */
sealed class ScreenStateEvent {

    object ShowLoading: ScreenStateEvent()

    object ShowSetupPage: ScreenStateEvent()

    data class ShowUserInteractionPage(val channelId: String): ScreenStateEvent()

    data class ShowDialogError(val title: String, val message: String): ScreenStateEvent()
}