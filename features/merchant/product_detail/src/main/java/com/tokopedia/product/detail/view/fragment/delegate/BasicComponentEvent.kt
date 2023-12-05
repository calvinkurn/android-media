package com.tokopedia.product.detail.view.fragment.delegate

import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.view.componentization.ComponentEvent

/**
 * Each event in BasicComponentEvent will always directly extend ComponentEvent,
 * aiming to allow features implementing ComponentEvent to send events existing in BasicComponentEvent.
 * This is due to the fact that a sealed interface/class cannot extend its subclass to children of BasicComponentEvent.
 */
sealed interface BasicComponentEvent : ComponentEvent {
    data class OnImpresseComponent(val trackData: ComponentTrackDataModel) : ComponentEvent
    data class GoToAppLink(val appLink: String) : ComponentEvent
    data class GoToWebView(val link: String) : ComponentEvent
}
