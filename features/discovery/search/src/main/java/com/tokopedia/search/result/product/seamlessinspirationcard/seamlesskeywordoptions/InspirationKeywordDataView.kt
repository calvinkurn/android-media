package com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.searchComponentTracking
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.typefactory.InspirationKeywordsTypeFactory

data class InspirationKeywordDataView(
    val keyword: String = "",
    val imageKeyword: String = "",
    val url: String = "",
    val applink: String = "",
    val isAppendTitleInTokopedia: Boolean = false,
    val dimension90: String = "",
    val componentId: String = "",
    val trackingOption: Int = 0,
    val actualKeyword: String = "",
    val carouselTitle: String = ""
) : ImpressHolder(),
    Visitable<InspirationKeywordsTypeFactory>,
    SearchComponentTracking by searchComponentTracking(
        trackingOption = trackingOption,
        keyword = actualKeyword,
        valueName = keyword,
        componentId = componentId,
        applink = applink,
        dimension90 = dimension90
    ) {
    companion object {

        fun create(
            option: InspirationCarouselDataView.Option
        ) = InspirationKeywordDataView(
            keyword = option.title,
            imageKeyword = option.bannerImageUrl,
            applink = option.applink,
            trackingOption = option.trackingOption,
            carouselTitle = option.carouselTitle,
            componentId = option.componentId
        )
    }

    override fun type(typeFactory: InspirationKeywordsTypeFactory): Int {
        return typeFactory.type(this)
    }
}
