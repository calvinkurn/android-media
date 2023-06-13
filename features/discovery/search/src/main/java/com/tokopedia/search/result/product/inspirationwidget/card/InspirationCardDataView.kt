package com.tokopedia.search.result.product.inspirationwidget.card

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.model.SearchProductModel.InspirationWidgetOption
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.search.result.product.inspirationwidget.InspirationWidgetDataView
import com.tokopedia.search.result.product.inspirationwidget.InspirationWidgetVisitable
import com.tokopedia.search.result.product.separator.VerticalSeparable
import com.tokopedia.search.result.product.separator.VerticalSeparator

data class InspirationCardDataView(
    override val data: InspirationWidgetDataView = InspirationWidgetDataView(),
    val optionCardData: List<InspirationCardOptionDataView> = listOf(),
    override val verticalSeparator: VerticalSeparator = VerticalSeparator.None,
) : InspirationWidgetVisitable {

    override fun addTopSeparator(): VerticalSeparable = this
    override fun addBottomSeparator(): VerticalSeparable = this

    override fun type(typeFactory: ProductListTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun isRelated() = data.type == SearchConstant.InspirationCard.TYPE_RELATED

    companion object {
        fun create(data: SearchProductModel.InspirationWidgetData): InspirationCardDataView {
            return InspirationCardDataView(
                data = InspirationWidgetDataView.create(data),
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
