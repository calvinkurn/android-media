package com.tokopedia.product.detail.view.viewholder.dynamic_oneliner.event

import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.view.componentization.ComponentEvent

sealed interface DynamicOneLinerEvent : ComponentEvent {
    data class OnDynamicOneLinerClicked(
        val title: String,
        val url: String,
        val trackDataModel: ComponentTrackDataModel
    ) : DynamicOneLinerEvent
}
