package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.sellerhome.WidgetType
import com.tokopedia.sellerhome.view.model.*
import com.tokopedia.sellerhome.view.widget.ShopScorePMWidget
import com.tokopedia.usecase.coroutines.UseCase

/**
 * Created By @ilhamsuaib on 2020-01-15
 */

class GetLayoutUseCase(
        graphqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<List<BaseWidgetUiModel<*>>>() {

    override suspend fun executeOnBackground(): List<BaseWidgetUiModel<*>> {

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
                ),
                CarouselWidgetUiModel(
                        widgetType = WidgetType.CAROUSEL,
                        title = "Caruosel",
                        subTitle = "",
                        tooltip = TooltipUiModel("", "", false),
                        url = "",
                        appLink = "",
                        dataKey = "",
                        ctaText = "",
                        data = CarouselDataUiModel(
                                data = listOf(
                                        CarouselDataModel(
                                                id = "",
                                                url = "http://placekitten.com/300/100",
                                                applink = "",
                                                featuredMediaURL = "http://placekitten.com/300/101"
                                        ),
                                        CarouselDataModel(
                                                id = "",
                                                url = "http://placekitten.com/300/101",
                                                applink = "",
                                                featuredMediaURL = "http://placekitten.com/300/102"
                                        ),
                                        CarouselDataModel(
                                                id = "",
                                                url = "http://placekitten.com/300/102",
                                                applink = "",
                                                featuredMediaURL = "http://placekitten.com/300/103"
                                        )
                                )
                        )
                ),
                ProgressUiModel(
                        widgetType = WidgetType.PROGRESS,
                        title = "Power Merchant (Aktif)",
                        subTitle = "",
                        tooltip = TooltipUiModel("Skor", "", false),
                        url = "",
                        appLink = "",
                        dataKey = "",
                        ctaText = "",
                        data = ProgressDataUiModel(
                                barTitle = "Skor",
                                valueTxt = "80jt",
                                maxValueTxt = "100jt",
                                value = 80,
                                maxValue = 100,
                                colorState = ShopScorePMWidget.State.GREEN,
                                subtitle = "Please maintain your shop score"
                        )
                ),
                ListUiModel(
                        widgetType = WidgetType.LIST,
                        title = "Info Seller",
                        subTitle = "",
                        tooltip = TooltipUiModel("Skor", "", false),
                        url = "",
                        appLink = "",
                        dataKey = "",
                        ctaText = "",
                        data = ListDataUiModel(
                                dataKey = "articles",
                                items = listOf(
                                        ListItemUiModel(
                                                title = "Lihat Siapa Aja yang Udah Promosiin Produkmu Lewat Affiliate Marketing!",
                                                appLink = "",
                                                url = "",
                                                featuredMediaURL = "https://i1.wp.com/ecs7.tokopedia.net/img/blog/seller/2019/12/Seller-Center-6.jpg?fit=1024%2C439&ssl=1",
                                                subtitle = "FITUR <i> 12 SEP 19"
                                        ),
                                        ListItemUiModel(
                                                title = "Lihat Siapa Aja yang Udah Promosiin Produkmu Lewat Affiliate Marketing!",
                                                appLink = "",
                                                url = "",
                                                featuredMediaURL = "https://i1.wp.com/ecs7.tokopedia.net/img/blog/seller/2019/12/Seller-Center-6.jpg?fit=1024%2C439&ssl=1",
                                                subtitle = "FITUR <i> 12 SEP 20"
                                        ),
                                        ListItemUiModel(
                                                title = "Lihat Siapa Aja yang Udah Promosiin Produkmu Lewat Affiliate Marketing!",
                                                appLink = "",
                                                url = "",
                                                featuredMediaURL = "https://i1.wp.com/ecs7.tokopedia.net/img/blog/seller/2019/12/Seller-Center-6.jpg?fit=1024%2C439&ssl=1",
                                                subtitle = "FITUR <i> 12 SEP 21"
                                        )
                                )
                        )
                )
        )
    }
}