package com.tokopedia.search.result.product.inspirationwidget

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchConstant.InspirationCard.TYPE_SIZE_PERSO
import com.tokopedia.search.result.domain.model.SearchProductModel.SearchInspirationWidget
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.search.result.product.inspirationwidget.card.InspirationCardDataView
import com.tokopedia.search.result.product.inspirationwidget.size.InspirationSizeDataView

interface InspirationWidgetVisitable: Visitable<ProductListTypeFactory> {

    val data: InspirationWidgetDataView

    val hasTopSeparator: Boolean

    val hasBottomSeparator: Boolean

    companion object {
        fun create(
            searchInspirationWidget: SearchInspirationWidget,
            keyword: String,
            dimension90: String,
        ): List<InspirationWidgetVisitable> {
            val widgetData = searchInspirationWidget.data

            val inspirationCardList = widgetData
                .filter { it.type != TYPE_SIZE_PERSO }
                .map { data -> InspirationCardDataView.create(data) }

            val inspirationSizeList = widgetData
                .filter { it.type == TYPE_SIZE_PERSO }
                .map { data -> InspirationSizeDataView.create(data, keyword, dimension90) }

            return inspirationCardList + inspirationSizeList
        }
    }
}