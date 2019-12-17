package com.tokopedia.play.ui.toolbar.interaction

import com.tokopedia.play.component.ComponentEvent
import com.tokopedia.play.ui.toolbar.model.PartnerType

/**
 * Created by jegul on 09/12/19
 */
sealed class PlayToolbarInteractionEvent : ComponentEvent {

    object BackButtonClicked : PlayToolbarInteractionEvent()
    object MoreButtonClicked : PlayToolbarInteractionEvent()
    object FollowButtonClicked : PlayToolbarInteractionEvent()
    object UnFollowButtonClicked : PlayToolbarInteractionEvent()
    data class PartnerNameClicked(val partnerId: Long, val type: PartnerType) : PlayToolbarInteractionEvent()
}