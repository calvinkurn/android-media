package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.sellerhome.WidgetType
import com.tokopedia.sellerhome.view.model.*
import com.tokopedia.usecase.coroutines.UseCase

/**
 * Created By @ilhamsuaib on 2020-01-15
 */

class GetLayoutUseCase(
        graphqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<List<BaseWidgetUiModel<*>>>() {

    override suspend fun executeOnBackground(): List<BaseWidgetUiModel<*>> {

        val strings: ArrayList<String> = ArrayList()
        strings.add("http://placekitten.com/100/50")
        strings.add("http://placekitten.com/100/51")
        strings.add("http://placekitten.com/100/53")
        strings.add("http://placekitten.com/100/54")

        //handle request here
        return listOf(
                SectionWidgetUiModel(
                        widgetType = WidgetType.SECTION,
                        title = "Performa Toko, tes jika textnya kepanjangan akan seperti apa hasilnya",
                        subTitle = "",
                        tooltip = TooltipUiModel("", "", false),
                        url = "",
                        appLink = "",
                        dataKey = "",
                        ctaText = "",
                        data = SectionDataUiModel()
                ),
                CardWidgetUiModel(
                        widgetType = WidgetType.CARD,
                        title = "Card 1",
                        subTitle = "",
                        tooltip = TooltipUiModel("", "", false),
                        url = "",
                        appLink = "",
                        dataKey = "",
                        ctaText = "",
                        data = CardDataUiModel()
                ),
                CardWidgetUiModel(
                        widgetType = WidgetType.CARD,
                        title = "Card 2",
                        subTitle = "",
                        tooltip = TooltipUiModel("", "", false),
                        url = "",
                        appLink = "",
                        dataKey = "",
                        ctaText = "",
                        data = CardDataUiModel()
                ),
                CardWidgetUiModel(
                        widgetType = WidgetType.CARD,
                        title = "Card 3",
                        subTitle = "",
                        tooltip = TooltipUiModel("", "", false),
                        url = "",
                        appLink = "",
                        dataKey = "",
                        ctaText = "",
                        data = CardDataUiModel()
                ),
                LineGraphWidgetUiModel(
                        widgetType = WidgetType.LINE_GRAPH,
                        title = "Total Pendapatan",
                        subTitle = "",
                        tooltip = TooltipUiModel("", "", false),
                        url = "",
                        appLink = "",
                        dataKey = "",
                        ctaText = "",
                        data = LineGraphDataUiModel(
                                dataKey = "",
                                description = "<span style=color:#EF144A;><b>-87%</b></span>",
                                error = "",
                                header = "Rp2.000.000",
                                yLabels = emptyList(),
                                list = listOf(
                                        XYAxisUiModel(
                                                xLabel = "29 des",
                                                yLabel = "",
                                                yVal = 1000
                                        ),
                                        XYAxisUiModel(
                                                xLabel = "30 ",
                                                yLabel = "",
                                                yVal = 200
                                        ),
                                        XYAxisUiModel(
                                                xLabel = "31",
                                                yLabel = "",
                                                yVal = 600
                                        ),
                                        XYAxisUiModel(
                                                xLabel = "1 jan",
                                                yLabel = "",
                                                yVal = 1200
                                        ),
                                        XYAxisUiModel(
                                                xLabel = "2",
                                                yLabel = "",
                                                yVal = 700
                                        ),
                                        XYAxisUiModel(
                                                xLabel = "3",
                                                yLabel = "",
                                                yVal = 1100
                                        ),
                                        XYAxisUiModel(
                                                xLabel = "4",
                                                yLabel = "",
                                                yVal = 1500
                                        )
                                )
                        )
                )
        )
    }
}