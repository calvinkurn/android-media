package com.tokopedia.search.result.product.inspirationwidget.filter

import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst
import com.tokopedia.discovery.common.analytics.searchComponentTracking
import com.tokopedia.filter.common.data.Option

class InspirationFilterOptionDataView(
    val text: String = "",
    val img: String = "",
    val url: String = "",
    val hexColor: String = "",
    val applink: String = "",
    val optionList: List<Option> = listOf(Option()),
    val inspirationCardType: String = "",
    val componentId: String = "",
    val keyword: String = "",
    val valueName: String = "",
    val dimension90: String = "",
    val trackingOption: Int = SearchComponentTrackingConst.Options.IMPRESSION_AND_CLICK
) : SearchComponentTracking by searchComponentTracking(
    componentId = componentId,
    valueName = valueName,
    valueId = VALUE_ID,
    keyword = keyword,
    dimension90 = dimension90,
    trackingOption = trackingOption
) {
    companion object {
        private const val VALUE_ID = "0"
    }
}
