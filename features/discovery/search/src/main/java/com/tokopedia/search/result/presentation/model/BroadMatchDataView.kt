package com.tokopedia.search.result.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.searchComponentTracking
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory

data class BroadMatchDataView(
    val keyword: String = "",
    val url: String = "",
    val applink: String = "",
    val isAppendTitleInTokopedia: Boolean = false,
    val broadMatchItemDataViewList: List<BroadMatchItemDataView> = listOf(),
    val dimension90: String = "",
    val carouselOptionType: CarouselOptionType,
    val componentId: String = "",
    val trackingOption: Int = 0,
    val actualKeyword: String = "",
) : ImpressHolder(),
    Visitable<ProductListTypeFactory>,
    SearchComponentTracking by searchComponentTracking(
        trackingOption = trackingOption,
        keyword = actualKeyword,
        valueName = keyword,
        componentId = componentId,
        applink = applink,
        dimension90 = dimension90,
    ) {

    override fun type(typeFactory: ProductListTypeFactory): Int {
        return typeFactory.type(this)
    }
}