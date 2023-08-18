package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.anchortabs

import android.app.Application
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.usecase.AnchorTabsUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import javax.inject.Inject

class AnchorTabsItemViewModel(
    val application: Application,
    val components: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel() {

    @JvmField
    @Inject
    var anchorTabsUseCase: AnchorTabsUseCase? = null
    private var dataItem: DataItem? = null

    init {
        dataItem = components.data?.firstOrNull()
    }

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        components.shouldRefreshComponent = null
    }

    fun getTitle(): String {
        return dataItem?.name ?: ""
    }

    fun getImageUrl(): String {
        return dataItem?.imageUrlMobile ?: ""
    }

    fun isSelected(): Boolean {
        return (dataItem?.targetSectionID == anchorTabsUseCase?.selectedId)
    }

    fun getSectionID(): String {
        return dataItem?.targetSectionID ?: ""
    }

    fun getImageURLForView(isHorizontalTab: Boolean, shouldShowIcon: Boolean): String {
        return if (isHorizontalTab || shouldShowIcon) {
            getImageUrl()
        } else {
            ""
        }
    }

    fun parentPosition(): Int {
        return getComponent(
            componentId = components.parentComponentId,
            components.pageEndPoint
        )?.position ?: components.parentComponentPosition
    }
}
