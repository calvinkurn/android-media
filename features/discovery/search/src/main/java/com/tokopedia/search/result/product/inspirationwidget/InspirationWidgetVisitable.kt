package com.tokopedia.search.result.product.inspirationwidget

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchConstant.InspirationCard.TYPE_ANNOTATION
import com.tokopedia.discovery.common.constants.SearchConstant.InspirationCard.TYPE_CATEGORY
import com.tokopedia.discovery.common.constants.SearchConstant.InspirationCard.TYPE_CURATED
import com.tokopedia.discovery.common.constants.SearchConstant.InspirationCard.TYPE_GUIDED
import com.tokopedia.discovery.common.constants.SearchConstant.InspirationCard.TYPE_RELATED
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
        private val showInspirationCardType = listOf(
            TYPE_ANNOTATION,
            TYPE_CATEGORY,
            TYPE_GUIDED,
            TYPE_CURATED,
            TYPE_RELATED,
            TYPE_SIZE_PERSO,
        )

        fun create(
            searchInspirationWidget: SearchInspirationWidget,
            keyword: String,
            dimension90: String,
        ): List<InspirationWidgetVisitable> {
            val validWidgetList = searchInspirationWidget.data
                .filter { showInspirationCardType.contains(it.type) }

            val inspirationCardList = validWidgetList
                .filter { it.type != TYPE_SIZE_PERSO }
                .map { data -> InspirationCardDataView.create(data) }

            val inspirationSizeList = validWidgetList
                .filter { it.type == TYPE_SIZE_PERSO }
                .map { data -> InspirationSizeDataView.create(data, keyword, dimension90) }

            return inspirationCardList + inspirationSizeList
        }
    }
}