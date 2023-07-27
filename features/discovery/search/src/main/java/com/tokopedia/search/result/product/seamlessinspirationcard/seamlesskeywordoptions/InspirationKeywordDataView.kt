package com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions

import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.searchComponentTracking
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.seamlessinspirationcard.utils.INDEX_IMAGE_PRODUCT_FOR_IMAGE_KEYWORD

data class InspirationKeywordDataView(
    val keyword: String = "",
    val imageKeyword: String = "",
    val url: String = "",
    val applink: String = "",
    val isAppendTitleInTokopedia: Boolean = false,
    val dimension90: String = "",
    val componentId: String = "",
    val trackingOption: Int = 0,
    val actualKeyword: String = ""
) : ImpressHolder(),
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
            option: InspirationCarouselDataView.Option,
            type: String
        ) = InspirationKeywordDataView(
            keyword = option.title,
            imageKeyword = option.getFirstProductItemImage(),
            applink = option.applink,
            trackingOption = option.trackingOption
        )
        private fun InspirationCarouselDataView.Option.getFirstProductItemImage() = this.product[INDEX_IMAGE_PRODUCT_FOR_IMAGE_KEYWORD].imgUrl
    }
}
