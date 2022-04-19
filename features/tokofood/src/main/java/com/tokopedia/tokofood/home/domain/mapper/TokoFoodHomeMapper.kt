package com.tokopedia.tokofood.home.domain.mapper

import com.tokopedia.home_component.model.ChannelBanner
import com.tokopedia.home_component.model.ChannelBenefit
import com.tokopedia.home_component.model.ChannelConfig
import com.tokopedia.home_component.model.ChannelCtaData
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelHeader
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.LabelGroup
import com.tokopedia.home_component.model.TrackingAttributionModel
import com.tokopedia.home_component.util.ServerTimeOffsetUtil
import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.home_component.visitable.CategoryWidgetV2DataModel
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodHomeLayoutItemState
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodHomeLayoutState
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodHomeLayoutType.Companion.BANNER_CAROUSEL
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodHomeLayoutType.Companion.CATEGORY_WIDGET
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodHomeLayoutType.Companion.ICON_TOKOFOOD
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodHomeLayoutType.Companion.LEGO_6_IMAGE
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodHomeLayoutType.Companion.TABS_TOKOFOOD
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodHomeLayoutType.Companion.USP_TOKOFOOD
import com.tokopedia.tokofood.home.domain.data.HomeLayoutResponse
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodFakeTab
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeFakeTabUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeIconsUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeItemUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeLoadingStateUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeUSPUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodIcon
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodUSPModel

object TokoFoodHomeMapper {

    val SUPPORTED_LAYOUT_TYPE = listOf(
        BANNER_CAROUSEL,
        LEGO_6_IMAGE,
        CATEGORY_WIDGET,
        TABS_TOKOFOOD,
        USP_TOKOFOOD,
        ICON_TOKOFOOD
    )

    fun MutableList<TokoFoodHomeItemUiModel>.addLoadingIntoList() {
        val loadingLayout = TokoFoodHomeLoadingStateUiModel(id = "")
        add(TokoFoodHomeItemUiModel(loadingLayout, TokoFoodHomeLayoutItemState.LOADED))
    }

    fun MutableList<TokoFoodHomeItemUiModel>.mapHomeLayoutList(
        responses: List<HomeLayoutResponse>
    ){
         responses .filter { SUPPORTED_LAYOUT_TYPE.contains(it.layout) }.forEach { homeLayoutResponse ->
             mapToHomeUiModel(homeLayoutResponse)?.let { item ->
                 add(item)
             }
         }
    }

    private fun mapToHomeUiModel(
        response: HomeLayoutResponse
    ): TokoFoodHomeItemUiModel? {
        val loadedState = TokoFoodHomeLayoutItemState.LOADED
        val notLoadedState = TokoFoodHomeLayoutItemState.NOT_LOADED

        return when (response.layout) {
            // region data got from dynamic channel direcly
            LEGO_6_IMAGE -> mapLego6DataModel(response, loadedState)
            BANNER_CAROUSEL -> mapBannerCarouselModel(response, loadedState)
            CATEGORY_WIDGET -> mapCategoryWidgetModel(response, loadedState)
            // endregion

            // region data got from other gql
            TABS_TOKOFOOD -> mapFakeTabModel(response, notLoadedState)
            USP_TOKOFOOD -> mapUSPWidgetModel(response, notLoadedState)
            ICON_TOKOFOOD -> mapDynamicIconModel(response, notLoadedState)
            // endregion

            else -> null
        }
    }

    private fun mapLego6DataModel(response: HomeLayoutResponse, state: TokoFoodHomeLayoutItemState): TokoFoodHomeItemUiModel {
        val channelModel = mapToChannelModel(response)
        val dynamicLego6Data = DynamicLegoBannerDataModel(channelModel)
        return TokoFoodHomeItemUiModel(dynamicLego6Data, state)
    }

    private fun mapBannerCarouselModel(response: HomeLayoutResponse, state: TokoFoodHomeLayoutItemState): TokoFoodHomeItemUiModel {
        val channelModel = mapToChannelModel(response)
        val bannerDataModel = BannerDataModel(channelModel)
        return TokoFoodHomeItemUiModel(bannerDataModel, state)
    }

    private fun mapCategoryWidgetModel(response: HomeLayoutResponse, state: TokoFoodHomeLayoutItemState): TokoFoodHomeItemUiModel {
        val channelModel = mapToChannelModel(response)
        val categoryWidgetV2DataModel = CategoryWidgetV2DataModel(channelModel)
        return TokoFoodHomeItemUiModel(categoryWidgetV2DataModel, state)
    }

    private fun mapUSPWidgetModel(response: HomeLayoutResponse, state: TokoFoodHomeLayoutItemState): TokoFoodHomeItemUiModel {
        val uspWidgetDataModel = TokoFoodHomeUSPUiModel("id", TokoFoodUSPModel(), TokoFoodHomeLayoutState.LOADING)
        return TokoFoodHomeItemUiModel(uspWidgetDataModel, state)
    }

    private fun mapDynamicIconModel(response: HomeLayoutResponse, state: TokoFoodHomeLayoutItemState): TokoFoodHomeItemUiModel {
        val dynamicIconModel = TokoFoodHomeIconsUiModel("id", listOf(
            TokoFoodIcon("", "https://images.tokopedia.net/img/SnKlQx/2022/2/2/459e236c-b66a-484d-82dc-461ea8a18c63.png", "Terdekat"),
            TokoFoodIcon("", "https://images.tokopedia.net/img/SnKlQx/2022/2/2/459e236c-b66a-484d-82dc-461ea8a18c63.png", "Terlaris"),
            TokoFoodIcon("", "https://images.tokopedia.net/img/SnKlQx/2022/2/2/459e236c-b66a-484d-82dc-461ea8a18c63.png", "Promo"),
            TokoFoodIcon("", "https://images.tokopedia.net/img/SnKlQx/2022/2/2/459e236c-b66a-484d-82dc-461ea8a18c63.png", "Sehat"),
            TokoFoodIcon("", "https://images.tokopedia.net/img/SnKlQx/2022/2/2/459e236c-b66a-484d-82dc-461ea8a18c63.png", "Most Loved"),
            TokoFoodIcon("", "https://images.tokopedia.net/img/SnKlQx/2022/2/2/459e236c-b66a-484d-82dc-461ea8a18c63.png", "24 Jam"),
            TokoFoodIcon("", "https://images.tokopedia.net/img/SnKlQx/2022/2/2/459e236c-b66a-484d-82dc-461ea8a18c63.png", "Bugdet"),
            TokoFoodIcon("", "https://images.tokopedia.net/img/SnKlQx/2022/2/2/459e236c-b66a-484d-82dc-461ea8a18c63.png", "Sehat"),
        ), TokoFoodHomeLayoutState.LOADING)
        return TokoFoodHomeItemUiModel(dynamicIconModel, state)
    }

    private fun mapFakeTabModel(response: HomeLayoutResponse, state: TokoFoodHomeLayoutItemState): TokoFoodHomeItemUiModel {
        val fakeTabModel = TokoFoodHomeFakeTabUiModel("id", TokoFoodFakeTab(), TokoFoodHomeLayoutState.LOADING)
        return TokoFoodHomeItemUiModel(fakeTabModel, state)
    }

    fun mapToChannelModel(response: HomeLayoutResponse): ChannelModel {
        return ChannelModel(
            id = response.id,
            groupId = response.groupId,
            type = response.type,
            layout = response.layout,
            pageName = response.pageName,
            channelHeader = ChannelHeader(
                response.header.id,
                response.header.name,
                response.header.subtitle,
                response.header.expiredTime,
                response.header.serverTimeUnix,
                response.header.applink,
                response.header.url,
                response.header.backColor,
                response.header.backImage,
                response.header.textColor
            ),
            channelBanner = ChannelBanner(
                id = response.banner.id,
                title = response.banner.title,
                description = response.banner.description,
                backColor = response.banner.backColor,
                url = response.banner.url,
                applink = response.banner.applink,
                textColor = response.banner.textColor,
                imageUrl = response.banner.imageUrl,
                attribution = response.banner.attribution,
                cta = ChannelCtaData(
                    response.banner.cta.type,
                    response.banner.cta.mode,
                    response.banner.cta.text,
                    response.banner.cta.couponCode
                ),
                gradientColor = response.banner.gradientColor
            ),
            channelConfig = ChannelConfig(
                response.layout,
                response.showPromoBadge,
                response.hasCloseButton,
                if (response.header.serverTimeUnix != 0L) ServerTimeOffsetUtil.getServerTimeOffsetFromUnix(response.header.serverTimeUnix) else 0,
                response.timestamp,
                response.isAutoRefreshAfterExpired
            ),
            trackingAttributionModel = TrackingAttributionModel(
                galaxyAttribution = response.galaxyAttribution,
                persona = response.persona,
                brandId = response.brandId,
                categoryPersona = response.categoryPersona,
                categoryId = response.categoryID,
                persoType = response.persoType,
                campaignCode = response.campaignCode,
                homeAttribution = response.homeAttribution
            ),
            channelGrids = response.grids.map {
                ChannelGrid(
                    id = it.id,
                    warehouseId = it.warehouseId,
                    parentProductId = it.parentProductId,
                    recommendationType = it.recommendationType,
                    minOrder = it.minOrder,
                    stock = it.maxOrder,
                    price = it.price,
                    imageUrl = it.imageUrl,
                    name = it.name,
                    applink = it.applink,
                    url = it.url,
                    discount = it.discount,
                    slashedPrice = it.slashedPrice,
                    label = it.label,
                    soldPercentage = it.soldPercentage,
                    attribution = it.attribution,
                    impression = it.impression,
                    cashback = it.cashback,
                    productClickUrl = it.productClickUrl,
                    isTopads = it.isTopads,
                    productViewCountFormatted = it.productViewCountFormatted,
                    isOutOfStock = it.isOutOfStock,
                    isFreeOngkirActive = it.freeOngkir.isActive,
                    freeOngkirImageUrl = it.freeOngkir.imageUrl,
                    shopId = it.shop.shopId,
                    hasBuyButton = it.hasBuyButton,
                    labelGroup = it.labelGroup.map { label ->
                        LabelGroup(
                            title = label.title,
                            position = label.position,
                            type = label.type
                        )
                    },
                    rating = it.rating,
                    ratingFloat = it.ratingFloat,
                    countReview = it.countReview,
                    backColor = it.backColor,
                    benefit = ChannelBenefit(
                        it.benefit.type,
                        it.benefit.value
                    ),
                    textColor = it.textColor,
                    param = it.param
                )
            }
        )
    }
}