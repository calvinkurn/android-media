package com.tokopedia.product.detail.tracking

import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel

data class GeneralInfoTracker(
    val isTokoNow: Boolean,
    private val componentData: ComponentTrackDataModel
) {
    val componentName = componentData.componentName
    val componentType = componentData.componentType
    val componentPosition = componentData.adapterPosition
}
