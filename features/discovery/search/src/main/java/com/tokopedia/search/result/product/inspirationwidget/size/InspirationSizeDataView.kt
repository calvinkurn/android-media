package com.tokopedia.search.result.product.inspirationwidget.size

import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.model.SearchProductModel.InspirationWidgetOption
import com.tokopedia.search.result.domain.model.SearchProductModel.InspirationWidgetFilter
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.search.result.product.inspirationwidget.InspirationWidgetDataView
import com.tokopedia.search.result.product.inspirationwidget.InspirationWidgetVisitable

class InspirationSizeDataView(
    override val data: InspirationWidgetDataView = InspirationWidgetDataView(),
    val optionSizeData: List<InspirationSizeOptionDataView> = listOf(),
): InspirationWidgetVisitable {

    override val hasTopSeparator = true

    override val hasBottomSeparator = true

    override fun type(typeFactory: ProductListTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        fun create(
            data: SearchProductModel.InspirationWidgetData,
            keyword: String,
            dimension90: String,
        ): InspirationSizeDataView {
            return InspirationSizeDataView(
                data = InspirationWidgetDataView(
                    title = data.title,
                    type = data.type,
                    position = data.position,
                ),
                optionSizeData = data.inspirationWidgetOptions.mapToInspirationSizeOptionDataView(
                    data.type,
                    keyword,
                    dimension90,
                    data.title,
                    data.trackingOption
                ),
            )
        }

        private fun List<InspirationWidgetOption>.mapToInspirationSizeOptionDataView(
            inspirationCardType: String,
            keyword: String,
            dimension90: String,
            title: String,
            trackingOption: Int
        ) = this.map { optionModel ->
            InspirationSizeOptionDataView(
                text = optionModel.text,
                img = optionModel.img,
                url = optionModel.url,
                hexColor = optionModel.color,
                applink = optionModel.applink,
                filters = optionModel.filters.toInspirationSizeOptionFiltersDataView(),
                inspirationCardType = inspirationCardType,
                componentId = optionModel.componentId,
                keyword = keyword,
                dimension90 = dimension90,
                valueName = title + " - " +optionModel.text,
                trackingOption = trackingOption
            )
        }

        private fun InspirationWidgetFilter.toInspirationSizeOptionFiltersDataView() =
            InspirationSizeOptionFiltersDataView(
                key = this.key,
                name = this.name,
                value = this.value
            )
    }
}