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

        //handle request here
        return listOf(
                SectionWidgetUiModel(
                        widgetType = WidgetType.SECTION,
                        title = "Performa Toko",
                        subtitle = "",
                        tooltip = TooltipUiModel("", "", false),
                        url = "",
                        appLink = "",
                        dataKey = "",
                        ctaText = "",
                        data = SectionDataUiModel()
                ),
                CardWidgetUiModel(
                        widgetType = WidgetType.CARD,
                        title = "New Order",
                        subtitle = "",
                        tooltip = TooltipUiModel("", "", false),
                        url = "",
                        appLink = "",
                        dataKey = "newOrder",
                        ctaText = "",
                        data = null
                ),
                CardWidgetUiModel(
                        widgetType = WidgetType.CARD,
                        title = "Ready To Ship",
                        subtitle = "",
                        tooltip = TooltipUiModel("", "", false),
                        url = "",
                        appLink = "",
                        dataKey = "readyToShipOrder",
                        ctaText = "",
                        data = null
                ),
                CardWidgetUiModel(
                        widgetType = WidgetType.CARD,
                        title = "Complaint",
                        subtitle = "",
                        tooltip = TooltipUiModel("", "", false),
                        url = "",
                        appLink = "",
                        dataKey = "complaint",
                        ctaText = "",
                        data = null
                ),
                CardWidgetUiModel(
                        widgetType = WidgetType.CARD,
                        title = "Unread Chat",
                        subtitle = "",
                        tooltip = TooltipUiModel("", "", false),
                        url = "",
                        appLink = "",
                        dataKey = "unreadChat",
                        ctaText = "",
                        data = null
                ),
                CardWidgetUiModel(
                        widgetType = WidgetType.CARD,
                        title = "Discussion",
                        subtitle = "",
                        tooltip = TooltipUiModel("", "", false),
                        url = "",
                        appLink = "",
                        dataKey = "discussion",
                        ctaText = "",
                        data = null
                ),
                CardWidgetUiModel(
                        widgetType = WidgetType.CARD,
                        title = "Product View",
                        subtitle = "",
                        tooltip = TooltipUiModel("", "", false),
                        url = "",
                        appLink = "",
                        dataKey = "productViewStatistic",
                        ctaText = "",
                        data = null
                ),
                CardWidgetUiModel(
                        widgetType = WidgetType.CARD,
                        title = "Shop Total Revenue",
                        subtitle = "",
                        tooltip = TooltipUiModel("", "", false),
                        url = "",
                        appLink = "",
                        dataKey = "shopTotalRevenueStatistic",
                        ctaText = "",
                        data = null
                ),
                CardWidgetUiModel(
                        widgetType = WidgetType.CARD,
                        title = "Product Sold",
                        subtitle = "",
                        tooltip = TooltipUiModel("", "", false),
                        url = "",
                        appLink = "",
                        dataKey = "productSoldStatistic",
                        ctaText = "",
                        data = null
                ),
                LineGraphWidgetUiModel(
                        widgetType = WidgetType.LINE_GRAPH,
                        title = "Total Pendapatan",
                        subtitle = "",
                        tooltip = TooltipUiModel("", "", false),
                        url = "",
                        appLink = "",
                        dataKey = "grossIncome",
                        ctaText = "",
                        data = null
                ),
                CarouselWidgetUiModel(
                        widgetType = WidgetType.CAROUSEL,
                        title = "Caruosel",
                        subtitle = "",
                        tooltip = TooltipUiModel("", "", false),
                        url = "",
                        appLink = "",
                        dataKey = "",
                        ctaText = "",
                        data = CarouselDataUiModel(
                                data = listOf(
                                        CarouselDataModel(
                                                id = "asasd",
                                                url = "http://tokopedia.com/blablablaal",
                                                applink = "https://seller.tokopedia.com/edu/cara-melihat-promosi-affiliate/",
                                                featuredMediaURL = "https://i1.wp.com/ecs7.tokopedia.net/img/blog/seller/2019/12/Seller-Center-6.jpg"
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
                                ),
                                state = CarouselState.NORMAL
                        )
                ),
                ProgressWidgetUiModel(
                        widgetType =  WidgetType.PROGRESS,
                        title = "Progress",
                        subtitle = "",
                        tooltip = TooltipUiModel("", "", false),
                        url = "",
                        appLink = ",",
                        dataKey = "shopScore",
                        ctaText = "",
                        data = null
                )
        )
    }
}