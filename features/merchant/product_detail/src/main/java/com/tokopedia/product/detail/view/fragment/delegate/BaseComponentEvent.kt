package com.tokopedia.product.detail.view.fragment.delegate

import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.view.componentization.ComponentEvent

interface BaseComponentEvent : ComponentEvent

data class OnImpressComponent(val trackData: ComponentTrackDataModel) : BaseComponentEvent
data class GoToApplink(val applink: String) : BaseComponentEvent
