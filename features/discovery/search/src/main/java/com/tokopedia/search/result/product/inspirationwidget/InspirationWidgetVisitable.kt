package com.tokopedia.search.result.product.inspirationwidget

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchConstant.InspirationCard.TYPE_ANNOTATION
import com.tokopedia.discovery.common.constants.SearchConstant.InspirationCard.TYPE_CATEGORY
import com.tokopedia.discovery.common.constants.SearchConstant.InspirationCard.TYPE_CURATED
import com.tokopedia.discovery.common.constants.SearchConstant.InspirationCard.TYPE_GUIDED
import com.tokopedia.discovery.common.constants.SearchConstant.InspirationCard.TYPE_RELATED
import com.tokopedia.search.result.domain.model.SearchProductModel.SearchInspirationWidget
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.search.result.product.inspirationwidget.card.InspirationCardDataView
import com.tokopedia.search.result.product.inspirationwidget.filter.InspirationFilterDataView
import com.tokopedia.search.result.product.separator.VerticalSeparable

interface InspirationWidgetVisitable: Visitable<ProductListTypeFactory>, VerticalSeparable {

    val data: InspirationWidgetDataView

    companion object {
        private val showInspirationCardLayout = listOf(
            TYPE_ANNOTATION,
            TYPE_CATEGORY,
            TYPE_GUIDED,
            TYPE_CURATED,
            TYPE_RELATED,
        )

        fun create(
            searchInspirationWidget: SearchInspirationWidget,
            keyword: String,
            dimension90: String,
        ): List<InspirationWidgetVisitable> {
            val inspirationCardList = searchInspirationWidget.data
                .filter { showInspirationCardLayout.contains(it.layout) }
                .map { data -> InspirationCardDataView.create(data) }

            val inspirationFilterList = searchInspirationWidget.filterWidgetList()
                .map { data -> InspirationFilterDataView.create(data, keyword, dimension90) }

            return inspirationCardList + inspirationFilterList
        }
    }
}
