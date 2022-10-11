package com.tokopedia.search.result.product.banner

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.searchComponentTracking
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory


data class BannerDataView(
    val position: Int = DEFAULT_POSITION,
    val text: String = "",
    val applink: String = "",
    val imageUrl: String = "",
    val componentId: String = "",
    val trackingOption: Int = 0,
    val dimension90: String = "",
    val keyword: String = "",
    val pageTitle: String = "",
) : Visitable<ProductListTypeFactory>, ImpressHolder(),
    SearchComponentTracking by searchComponentTracking(
        trackingOption = trackingOption,
        keyword = keyword,
        valueName = pageTitle,
        componentId = componentId,
        applink = applink,
        dimension90 = dimension90,
    ) {

    override fun type(typeFactory: ProductListTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }

    companion object {
        private const val DEFAULT_POSITION = -2

        fun create(
            bannerModel: SearchProductModel.Banner,
            keyword: String,
            dimension90: String,
            pageTitle: String,
        ): BannerDataView {
            return BannerDataView(
                bannerModel.position,
                bannerModel.text,
                bannerModel.applink,
                bannerModel.imageUrl,
                bannerModel.componentId,
                bannerModel.trackingOption,
                dimension90,
                keyword,
                pageTitle,
            )
        }
    }
}