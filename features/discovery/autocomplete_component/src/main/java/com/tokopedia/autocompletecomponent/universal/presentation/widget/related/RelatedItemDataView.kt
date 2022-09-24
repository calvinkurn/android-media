package com.tokopedia.autocompletecomponent.universal.presentation.widget.related

import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.searchComponentTracking
import com.tokopedia.kotlin.model.ImpressHolder

class RelatedItemDataView(
    val id: String = "",
    val applink: String = "",
    val imageUrl: String = "",
    val title: String = "",
    val componentId: String = "",
    val trackingOption: Int = 0,
    val campaignCode: String = "",
    val keyword: String = "",
    val dimension90: String = "",
): ImpressHolder(), SearchComponentTracking by searchComponentTracking(
    trackingOption = trackingOption,
    keyword = keyword,
    valueName = title,
    componentId = componentId,
    applink = applink,
    dimension90 = dimension90,
)