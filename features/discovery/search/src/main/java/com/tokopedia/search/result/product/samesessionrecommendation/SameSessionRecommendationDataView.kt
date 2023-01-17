package com.tokopedia.search.result.product.samesessionrecommendation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.searchComponentTracking
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView

class SameSessionRecommendationDataView(
    val title: String = "",
    val componentId: String = "",
    val trackingOption: Int = 0,
    val products: List<InspirationCarouselDataView.Option.Product> = emptyList(),
    val feedback: Feedback = Feedback(),
    val anchorProductId: String = "",
    val keyword: String = "",
    val dimension90: String = "",
) : ImpressHolder(), Visitable<ProductListTypeFactory>,
    SearchComponentTracking by searchComponentTracking(
        trackingOption = trackingOption,
        keyword = keyword,
        valueId = anchorProductId,
        componentId = componentId,
        dimension90 = dimension90,
    ) {
    override fun type(typeFactory: ProductListTypeFactory): Int {
        return typeFactory.type(this)
    }

    data class Feedback(
        val title: String = "",
        val componentId: String = "",
        val trackingOption: Int = 0,
        val items: List<FeedbackItem> = emptyList(),
        val keyword: String = "",
        val anchorProductId: String = "",
        val dimension90: String = "",
        var selectedFeedbackItem: FeedbackItem? = null,
    ) : ImpressHolder(), SearchComponentTracking by searchComponentTracking(
        trackingOption = trackingOption,
        componentId = componentId,
        keyword = keyword,
        valueId = anchorProductId,
        dimension90 = dimension90,
    ) {
        data class FeedbackItem(
            val name: String = "",
            val applink: String = "",
            val url: String = "",
            val imageUrl: String = "",
            val componentId: String = "",
            val titleOnClick: String = "",
            val messageOnClick: String = "",
            val trackingOption: Int = 0,
            val keyword: String = "",
            val valueName: String = "",
            val anchorProductId: String = "",
            val dimension90: String = "",
        ) : ImpressHolder(), SearchComponentTracking by searchComponentTracking(
            trackingOption = trackingOption,
            componentId = componentId,
            keyword = keyword,
            valueId = anchorProductId,
            valueName = valueName,
            dimension90 = dimension90,
        )
    }
}
