package com.tokopedia.search.result.product.inspirationwidget.filter

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.model.SearchProductModel.InspirationWidgetOption
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.search.result.product.inspirationwidget.InspirationWidgetDataView
import com.tokopedia.search.result.product.inspirationwidget.InspirationWidgetVisitable
import com.tokopedia.search.result.product.separator.VerticalSeparable
import com.tokopedia.search.result.product.separator.VerticalSeparator

class InspirationFilterDataView(
    override val data: InspirationWidgetDataView = InspirationWidgetDataView(),
    val optionFilterData: List<InspirationFilterOptionDataView> = listOf(),
    override val verticalSeparator: VerticalSeparator = VerticalSeparator.Both,
    inputType: String = "",
): InspirationWidgetVisitable {

    override fun addTopSeparator(): VerticalSeparable = this
    override fun addBottomSeparator(): VerticalSeparable = this

    override fun type(typeFactory: ProductListTypeFactory): Int {
        return typeFactory.type(this)
    }

    val isTypeRadio: Boolean = SearchConstant.InspirationCard.INPUT_TYPE_RADIO == inputType

    companion object {
        fun create(
            data: SearchProductModel.InspirationWidgetData,
            keyword: String,
            dimension90: String,
        ): InspirationFilterDataView {
            return InspirationFilterDataView(
                data = InspirationWidgetDataView.create(data),
                optionFilterData = data.inspirationWidgetOptions.mapToInspirationFilterOptionDataView(
                    data.type,
                    keyword,
                    dimension90,
                    data.title,
                    data.trackingOption.toIntOrZero(),
                ),
                inputType = data.inputType,
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
                optionList = optionModel.asOptionList(),
                inspirationCardType = inspirationCardType,
                componentId = optionModel.componentId,
                keyword = keyword,
                dimension90 = dimension90,
                valueName = title + " - " +optionModel.text,
                trackingOption = trackingOption
            )
        }
    }
}
