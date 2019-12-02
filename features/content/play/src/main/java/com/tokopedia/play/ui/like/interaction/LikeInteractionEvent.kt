package com.tokopedia.play.ui.like.interaction

import com.tokopedia.play.component.ComponentEvent

/**
 * Created by jegul on 02/12/19
 */
sealed class LikeInteractionEvent : ComponentEvent {

    object LikeClicked : LikeInteractionEvent()
}