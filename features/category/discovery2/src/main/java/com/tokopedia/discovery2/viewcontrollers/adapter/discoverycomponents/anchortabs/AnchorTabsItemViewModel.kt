package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.anchortabs

import android.app.Application
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class AnchorTabsItemViewModel(
    val application: Application,
    val components: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel() {

    fun getTitle(): String {
        return components.data?.firstOrNull()?.name ?: ""
    }

    fun getImageUrl(): String {
        return components.data?.firstOrNull()?.imageUrlMobile ?: ""
    }

    fun isSelected(): Boolean {
        return (components.data?.firstOrNull()?.isSelected ?: false)
    }

    fun getSectionID(): String {
        return components.data?.firstOrNull()?.targetSectionID ?: ""
    }

    fun parentPosition(): Int {
        return getComponent(
            componentId = components.parentComponentId,
            components.pageEndPoint
        )?.position ?: components.parentComponentPosition
    }
}