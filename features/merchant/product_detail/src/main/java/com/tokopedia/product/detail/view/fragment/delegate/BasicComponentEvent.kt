package com.tokopedia.product.detail.view.fragment.delegate

import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.view.componentization.ComponentEvent

sealed interface BasicComponentEvent : ComponentEvent {
    data class OnImpressed(val trackData: ComponentTrackDataModel) : BasicComponentEvent
    data class GoToAppLink(val applink: String) : BasicComponentEvent
    data class GoToWebView(val link: String) : BasicComponentEvent
}
