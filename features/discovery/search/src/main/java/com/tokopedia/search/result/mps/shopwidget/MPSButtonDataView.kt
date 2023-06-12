package com.tokopedia.search.result.mps.shopwidget

import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.searchComponentTracking
import com.tokopedia.search.result.mps.domain.model.MPSModel.SearchShopMPS.Shop.Button

data class MPSButtonDataView(
    val name: String = "",
    val text: String = "",
    val applink: String = "",
    val componentId: String = "",
    val trackingOption: Int = 0,
    val keywords: String = "",
    val componentValueId: String = "",
    val componentValueName: String = "",
): SearchComponentTracking by searchComponentTracking(
    trackingOption = trackingOption,
    componentId = componentId,
    keyword = keywords,
    valueId = componentValueId,
    valueName = componentValueName,
    applink = applink,
) {

    fun isPrimary() = name == PRIMARY

    fun isSecondary() = name == SECONDARY

    companion object {
        const val PRIMARY = "primary"
        const val SECONDARY = "secondary"

        fun create(
            button: Button,
            keywords: String,
            componentValueId: String,
            componentValueName: String,
        ): MPSButtonDataView = MPSButtonDataView(
            name = button.name,
            text = button.text,
            applink = button.applink,
            componentId = button.componentId,
            trackingOption = button.trackingOption,
            keywords = keywords,
            componentValueId = componentValueId,
            componentValueName = componentValueName,
        )
    }
}
