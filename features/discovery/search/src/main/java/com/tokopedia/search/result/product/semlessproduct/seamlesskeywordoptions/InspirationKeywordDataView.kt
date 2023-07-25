package com.tokopedia.search.result.product.semlessproduct.seamlesskeywordoptions

import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.searchComponentTracking
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.search.result.product.broadmatch.CarouselOptionType
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.semlessproduct.utils.INDEX_IMAGE_PRODUCT_FOR_IMAGE_KEYWORD

data class InspirationKeywordDataView(
    val keyword: String = "",
    val subtitle: String = "",
    val imageKeyword: String = "",
    val url: String = "",
    val applink: String = "",
    val isAppendTitleInTokopedia: Boolean = false,
    val dimension90: String = "",
    val carouselOptionType: CarouselOptionType,
    val componentId: String = "",
    val trackingOption: Int = 0,
    val actualKeyword: String = "",
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
            type: String,
        ) = InspirationKeywordDataView(
            keyword = option.title,
            subtitle = option.subtitle,
            imageKeyword = option.getFirstProductItemImage(),
            applink = option.applink,
            carouselOptionType = CarouselOptionType.of(type, option),
            trackingOption = option.trackingOption,
        )
        private fun InspirationCarouselDataView.Option.getFirstProductItemImage() = this.product[INDEX_IMAGE_PRODUCT_FOR_IMAGE_KEYWORD].imgUrl
    }
}
