package com.tokopedia.search.result.product.suggestion

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.searchComponentTracking
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.separator.VerticalSeparable
import com.tokopedia.search.result.product.separator.VerticalSeparator

@Suppress("LongParameterList")
data class SuggestionDataView(
    val suggestionText: String = "",
    val suggestedQuery: String = "",
    val suggestion: String = "",
    val componentId: String = "",
    val trackingOption: Int = 0,
    val keyword: String = "",
    val dimension90: String = "",
    val trackingValue: String = "",
    override val verticalSeparator: VerticalSeparator = VerticalSeparator.None,
) : ImpressHolder(),
    VerticalSeparable,
    Visitable<ProductListTypeFactory?>,
    SearchComponentTracking by searchComponentTracking(
        trackingOption = trackingOption,
        keyword = keyword,
        dimension90 = dimension90,
        valueName = trackingValue,
        componentId = componentId,
        applink = suggestedQuery,
    ) {

    override fun type(typeFactory: ProductListTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }

    override fun addTopSeparator(): VerticalSeparable =
        this.copy(verticalSeparator = VerticalSeparator.Top)

    override fun addBottomSeparator(): VerticalSeparable = this

    companion object {

        fun create(inspirationCarouselDataView: InspirationCarouselDataView): SuggestionDataView =
            SuggestionDataView(
                suggestionText = inspirationCarouselDataView.title,
                verticalSeparator = VerticalSeparator.Top
            )
    }
}
