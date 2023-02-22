package com.tokopedia.search.result.product.inspirationwidget.filter

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.model.SearchProductModel.InspirationWidgetFilter
import com.tokopedia.search.result.domain.model.SearchProductModel.InspirationWidgetOption
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.search.result.product.inspirationwidget.InspirationWidgetDataView
import com.tokopedia.search.result.product.inspirationwidget.InspirationWidgetVisitable
import com.tokopedia.search.result.product.separator.VerticalSeparable
import com.tokopedia.search.result.product.separator.VerticalSeparator

class InspirationFilterDataView(
    override val data: InspirationWidgetDataView = InspirationWidgetDataView(),
    val optionSizeData: List<InspirationFilterOptionDataView> = listOf(),
    override val verticalSeparator: VerticalSeparator = VerticalSeparator.Both,
): InspirationWidgetVisitable {

    override fun addTopSeparator(): VerticalSeparable = this
    override fun addBottomSeparator(): VerticalSeparable = this

    override fun type(typeFactory: ProductListTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        fun create(
            data: SearchProductModel.InspirationWidgetData,
            keyword: String,
            dimension90: String,
        ): InspirationFilterDataView {
            return InspirationFilterDataView(
                data = InspirationWidgetDataView.create(data),
                optionSizeData = data.inspirationWidgetOptions.mapToInspirationFilterOptionDataView(
                    data.type,
                    keyword,
                    dimension90,
                    data.title,
                    data.trackingOption.toIntOrZero(),
                ),
            )
        }

        private fun List<InspirationWidgetOption>.mapToInspirationFilterOptionDataView(
            inspirationCardType: String,
            keyword: String,
            dimension90: String,
            title: String,
            trackingOption: Int
        ) = this.map { optionModel ->
            InspirationFilterOptionDataView(
                text = optionModel.text,
                img = optionModel.img,
                url = optionModel.url,
                hexColor = optionModel.color,
                applink = optionModel.applink,
                filters = optionModel.filters.toInspirationFilterOptionFiltersDataView(),
                inspirationCardType = inspirationCardType,
                componentId = optionModel.componentId,
                keyword = keyword,
                dimension90 = dimension90,
                valueName = title + " - " +optionModel.text,
                trackingOption = trackingOption
            )
        }

        private fun InspirationWidgetFilter.toInspirationFilterOptionFiltersDataView() =
            InspirationFilterOptionFiltersDataView(
                key = this.key,
                name = this.name,
                value = this.value
            )
    }
}
