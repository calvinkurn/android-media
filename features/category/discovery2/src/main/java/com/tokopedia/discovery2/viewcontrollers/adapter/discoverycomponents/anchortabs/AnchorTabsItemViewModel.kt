package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.anchortabs

import android.app.Application
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.usecase.AnchorTabsUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import javax.inject.Inject

class AnchorTabsItemViewModel(
    val application: Application,
    val components: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel() {

    @Inject
    lateinit var anchorTabsUseCase: AnchorTabsUseCase

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        components.shouldRefreshComponent = null
    }

    fun getTitle(): String {
        return components.data?.firstOrNull()?.name ?: ""
    }

    fun getImageUrl(): String {
        return components.data?.firstOrNull()?.imageUrlMobile ?: ""
    }

    fun isSelected(): Boolean {
        return (components.data?.firstOrNull()?.targetSectionID == anchorTabsUseCase.selectedId)
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