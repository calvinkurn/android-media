package com.tokopedia.search.result.mps.shopwidget

import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.searchComponentTracking
import com.tokopedia.search.result.mps.domain.model.MPSModel.SearchShopMPS.Shop.Button

data class MPSShopWidgetViewAllCardDataView(
    val text: String = "",
    val applink: String = "",
    val componentId: String = "",
    val trackingOption: Int = 0,
    val keywords: String = "",
): SearchComponentTracking by searchComponentTracking(
    trackingOption = trackingOption,
    componentId = componentId,
    keyword = keywords,
    applink = applink,
) {
    companion object {
        fun create(
            button: Button?,
            keywords: String,
        ): MPSShopWidgetViewAllCardDataView =
            button?.let {
                MPSShopWidgetViewAllCardDataView(
                    text = button.text,
                    applink = button.applink,
                    componentId = button.componentId,
                    trackingOption = button.trackingOption,
                    keywords = keywords,
                )
            } ?: MPSShopWidgetViewAllCardDataView()
    }
}
