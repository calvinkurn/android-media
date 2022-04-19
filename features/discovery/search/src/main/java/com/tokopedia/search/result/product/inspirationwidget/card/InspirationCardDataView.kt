package com.tokopedia.search.result.product.inspirationwidget.card

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.model.SearchProductModel.InspirationWidgetOption
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.search.result.product.inspirationwidget.InspirationWidgetDataView
import com.tokopedia.search.result.product.inspirationwidget.InspirationWidgetVisitable

data class InspirationCardDataView(
    override val data: InspirationWidgetDataView = InspirationWidgetDataView(),
    val optionCardData: List<InspirationCardOptionDataView> = listOf(),
) : InspirationWidgetVisitable {

    override val hasBottomSeparator = false

    override val hasTopSeparator = false

    override fun type(typeFactory: ProductListTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun isRelated() = data.type == SearchConstant.InspirationCard.TYPE_RELATED

    companion object {
        fun create(data: SearchProductModel.InspirationWidgetData): InspirationCardDataView {
            return InspirationCardDataView(
                data = InspirationWidgetDataView(
                    title = data.title,
                    type = data.type,
                    position = data.position,
                ),
                optionCardData = data
                    .inspirationWidgetOptions
                    .mapToInspirationCardOptionDataView(data.type),
            )
        }

        private fun List<InspirationWidgetOption>.mapToInspirationCardOptionDataView(
            inspirationCardType: String
        ) = this.map { optionModel ->
            InspirationCardOptionDataView(
                text = optionModel.text,
                img = optionModel.img,
                url = optionModel.url,
                hexColor = optionModel.color,
                applink = optionModel.applink,
                inspirationCardType = inspirationCardType,
            )
        }
    }
}