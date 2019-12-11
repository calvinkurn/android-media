package com.tokopedia.play.ui.toolbar.interaction

import com.tokopedia.play.component.ComponentEvent

/**
 * Created by jegul on 09/12/19
 */
sealed class PlayToolbarInteractionEvent : ComponentEvent {

    object BackButtonClicked : PlayToolbarInteractionEvent()
    object MoreButtonClicked : PlayToolbarInteractionEvent()
    object FollowButtonClicked : PlayToolbarInteractionEvent()
    object UnFollowButtonClicked : PlayToolbarInteractionEvent()
}