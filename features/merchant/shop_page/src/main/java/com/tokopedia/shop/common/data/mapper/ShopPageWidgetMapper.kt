package com.tokopedia.shop.common.data.mapper

import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toFloatOrZero
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.shop.campaign.view.model.ShopCampaignWidgetCarouselProductUiModel
import com.tokopedia.shop.campaign.view.model.ShopWidgetDisplaySliderBannerHighlightUiModel
import com.tokopedia.shop.common.data.model.DynamicRule
import com.tokopedia.shop.common.data.model.ShopPageWidgetUiModel
import com.tokopedia.shop.home.ComponentType
import com.tokopedia.shop.home.data.model.ShopLayoutWidget
import com.tokopedia.shop.home.data.model.ShopPageWidgetRequestModel
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselUiModel
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseNavigationUiModel
import com.tokopedia.shop.home.view.model.ShopWidgetDisplayBannerProductHotspotUiModel
import com.tokopedia.shop.home.view.model.ShopWidgetDisplayBannerTimerUiModel
import com.tokopedia.shop.home.view.model.ShopWidgetVoucherSliderUiModel
import com.tokopedia.shop.home.view.model.ShowcaseNavigationBannerWidgetStyle
import com.tokopedia.shop.home.view.model.StatusCampaign

//TODO need to migrate all other shop widget mapper on home mapper to this mapper
object ShopPageWidgetMapper {

    fun mapToBannerTimerWidget(
        widgetResponse: ShopLayoutWidget.Widget,
        widgetLayout: ShopPageWidgetUiModel?
    )= ShopWidgetDisplayBannerTimerUiModel(
        widgetId = widgetResponse.widgetID,
        layoutOrder = widgetResponse.layoutOrder,
        name = widgetResponse.name,
        type = widgetResponse.type,
        header = ShopPageHomeMapper.mapToHeaderModel(widgetResponse.header, widgetLayout),
        isFestivity = widgetLayout?.isFestivity.orFalse(),
        data = mapToBannerItemWidget(widgetResponse.data.firstOrNull())
    )

    private fun mapToBannerItemWidget(
        data: ShopLayoutWidget.Widget.Data?,
    ): ShopWidgetDisplayBannerTimerUiModel.Data {
        val statusCampaign = data?.timeInfo?.status?.mapToStatusCampaign()
        val isUpcomingCampaign = statusCampaign == StatusCampaign.UPCOMING
        val isRemindMe = if (isUpcomingCampaign) {
            false
        } else {
            null
        }
        return ShopWidgetDisplayBannerTimerUiModel.Data(
            appLink =  data?.appLink.orEmpty(),
            imageUrl =  data?.imageUrl.orEmpty(),
            linkType =  data?.linkType.orEmpty(),
            timeDescription = data?.timeInfo?.timeDescription.orEmpty(),
            timeCounter = data?.timeInfo?.timeCounter.orZero(),
            startDate = data?.timeInfo?.startDate.orEmpty(),
            endDate = data?.timeInfo?.endDate.orEmpty(),
            bgColor = data?.timeInfo?.bgColor.orEmpty(),
            textColor = data?.timeInfo?.textColor.orEmpty(),
            status = data?.timeInfo?.status.mapToStatusCampaign(),
            totalNotify = data?.totalNotify.orZero(),
            totalNotifyWording = data?.totalNotifyWording.orEmpty(),
            dynamicRule = mapToDynamicRule(data?.dynamicRule),
            isRemindMe = isRemindMe
        )
    }

    fun mapToSliderBannerHighlightWidget(
        widgetResponse: ShopLayoutWidget.Widget,
        widgetLayout: ShopPageWidgetUiModel?
    )= ShopWidgetDisplaySliderBannerHighlightUiModel(
        widgetId = widgetResponse.widgetID,
        layoutOrder = widgetResponse.layoutOrder,
        name = widgetResponse.name,
        type = widgetResponse.type,
        header = ShopPageHomeMapper.mapToHeaderModel(widgetResponse.header, widgetLayout),
        isFestivity = widgetLayout?.isFestivity.orFalse(),
        listHighlightProductData = mapToListHighlightProductData(widgetResponse.data)
    )

    private fun mapToListHighlightProductData(data: List<ShopLayoutWidget.Widget.Data>): List<ShopWidgetDisplaySliderBannerHighlightUiModel.ProductHighlightData> {
        return data.map {
            ShopWidgetDisplaySliderBannerHighlightUiModel.ProductHighlightData(
                appLink = it.appLink,
                imageUrl = it.imageUrl,
            )
        }
    }

    fun mapToCampaignProductCarouselWidgetUiModel(
        widgetResponse: ShopLayoutWidget.Widget,
        widgetLayout: ShopPageWidgetUiModel?
    ) =  ShopCampaignWidgetCarouselProductUiModel(
        widgetId = widgetResponse.widgetID,
        layoutOrder = widgetResponse.layoutOrder,
        name = widgetResponse.name,
        type = widgetResponse.type,
        header = ShopPageHomeMapper.mapToHeaderModel(widgetResponse.header, widgetLayout),
        isFestivity = widgetLayout?.isFestivity.orFalse(),
        productList = ShopPageHomeMapper.mapCampaignCarouselListProduct(
            widgetResponse.data.firstOrNull()?.listProduct.orEmpty()
        ),
        statusCampaign = widgetResponse.data.firstOrNull()?.statusCampaign.orEmpty()
    )

    fun mapToCampaignVoucherSliderUiModel(
        widgetResponse: ShopLayoutWidget.Widget,
        widgetLayout: ShopPageWidgetUiModel?
    ) = ShopWidgetVoucherSliderUiModel(
        widgetResponse.widgetID,
        widgetResponse.layoutOrder,
        widgetResponse.name,
        widgetResponse.type,
        ShopPageHomeMapper.mapToHeaderModel(widgetResponse.header, widgetLayout),
        widgetLayout?.isFestivity.orFalse()
    )

    fun mapToShopPageWidgetRequest(listWidgetLayout: List<ShopPageWidgetUiModel>): List<ShopPageWidgetRequestModel> {
        return listWidgetLayout.map {
            ShopPageWidgetRequestModel(
                it.widgetId,
                it.widgetMasterId,
                it.widgetType,
                it.widgetName,
                ShopPageWidgetRequestModel.HeaderInput(
                    it.header.title,
                    it.header.subtitle,
                    it.header.ctaText,
                    it.header.ctaLink,
                    it.header.isAtc,
                    it.header.etalaseId,
                    it.header.isShowEtalaseName,
                    it.header.data.map { dataHeader ->
                        ShopPageWidgetRequestModel.HeaderInput.DataOnHeaderInput(
                            dataHeader.linkID.toString(),
                            dataHeader.linkType
                        )
                    }
                )
            )
        }
    }

    fun mapToDynamicRule(dynamicRule: ShopLayoutWidget.Widget.Data.DynamicRule?): DynamicRule {
        return DynamicRule(
            dynamicRule?.descriptionHeader.orEmpty(),
            dynamicRule?.dynamicRoleData?.map {
                DynamicRule.DynamicRoleData(
                    ruleID = it.ruleID,
                    isActive = it.isActive
                )
            }.orEmpty()
        )
    }


    fun mapToPlayWidgetTypeSellerApp(shopId: String): PlayWidgetUseCase.WidgetType.SellerApp{
        return PlayWidgetUseCase.WidgetType.SellerApp(
            shopId = shopId
        )
    }

    fun mapToPlayWidgetTypeShopPage(shopId: String): PlayWidgetUseCase.WidgetType.ShopPage{
        return PlayWidgetUseCase.WidgetType.ShopPage(
            shopId = shopId
        )
    }

    fun mapToPlayWidgetTypeExclusiveLaunch(shopId: String, campaignId: String): PlayWidgetUseCase.WidgetType.ShopPageExclusiveLaunch{
        return PlayWidgetUseCase.WidgetType.ShopPageExclusiveLaunch(
            shopId = shopId,
            campaignId = campaignId
        )
    }

    fun mapToHomeShowcaseWidget(response: ShopLayoutWidget.Widget): ShopHomeShowcaseNavigationUiModel {
        val widgetStyle = if (response.header.widgetStyle == ShowcaseNavigationBannerWidgetStyle.ROUNDED_CORNER.id) {
            ShopHomeShowcaseNavigationUiModel.WidgetStyle.ROUNDED_CORNER
        } else {
            ShopHomeShowcaseNavigationUiModel.WidgetStyle.CIRCLE
        }

        val tabs = response.data.map { tab ->
            val showcases = tab.showcaseList.map { showcase ->
                ShopHomeShowcaseNavigationUiModel.Tab.Showcase(
                    showcase.showcaseID,
                    showcase.name,
                    showcase.imageURL,
                    showcase.ctaLink,
                    showcase.isMainBanner
                )
            }

            val mainBannerPosition = if (tab.mainBannerPosition == ShopHomeShowcaseNavigationUiModel.MainBannerPosition.TOP.id) {
                ShopHomeShowcaseNavigationUiModel.MainBannerPosition.TOP
            } else {
                ShopHomeShowcaseNavigationUiModel.MainBannerPosition.LEFT
            }

            ShopHomeShowcaseNavigationUiModel.Tab(
                text = tab.text,
                imageUrl = tab.imageURL,
                mainBannerPosition = mainBannerPosition,
                showcases = showcases
            )
        }

        return ShopHomeShowcaseNavigationUiModel(
            showcaseHeader = ShopHomeShowcaseNavigationUiModel.ShowcaseHeader(
                title = response.header.title,
                ctaLink = response.header.ctaLink,
                widgetStyle = widgetStyle
            ),
            tabs = tabs,
            widgetId = response.widgetID,
            layoutOrder = response.layoutOrder,
            name = response.name,
            type = response.type
        )
    }
    fun mapToHomeProductCarouselWidget(response: ShopLayoutWidget.Widget): ShopHomeProductCarouselUiModel {
        val tabs = response.data.map { tab ->
            val tabComponentLists = tab.componentList.map { component ->
                val componentType = when (component.componentType) {
                    ComponentType.BANNER_SINGLE -> {
                        ShopHomeProductCarouselUiModel.ComponentType.BANNER_SINGLE
                    }
                    ComponentType.PRODUCT_CARD_WITH_INFO -> {
                        ShopHomeProductCarouselUiModel.ComponentType.PRODUCT_CARD_WITH_PRODUCT_INFO
                    }
                    ComponentType.PRODUCT_CARD_WITHOUT_INFO -> {
                        ShopHomeProductCarouselUiModel.ComponentType.PRODUCT_CARD_WITHOUT_PRODUCT_INFO
                    }
                    else -> {
                        ShopHomeProductCarouselUiModel.ComponentType.BANNER_SINGLE
                    }
                }

                val componentListChild = component.componentChild.map { componentChild ->
                    ShopHomeProductCarouselUiModel.Tab.ComponentList.Data(
                        componentChild.imageID,
                        componentChild.imageUrl,
                        componentChild.ctaLink,
                        componentChild.linkID,
                        componentChild.linkType,
                        componentChild.isShowProductInfo,
                        componentChild.bannerType
                    )
                }

                ShopHomeProductCarouselUiModel.Tab.ComponentList(
                    component.componentID,
                    component.componentName,
                    componentType,
                    component.ratio,
                    componentListChild
                )
            }

            ShopHomeProductCarouselUiModel.Tab(
                tab.tabLabel,
                tab.tabName,
                tabComponentLists
            )
        }

        return ShopHomeProductCarouselUiModel(
            title = response.header.title,
            tabs = tabs
        )
    }

    private fun Int?.mapToStatusCampaign(): StatusCampaign {
        return when (this) {
            0 -> StatusCampaign.UPCOMING
            1 -> StatusCampaign.ONGOING
            2 -> StatusCampaign.FINISHED
            else -> StatusCampaign.UPCOMING
        }
    }

    fun mapToBannerProductHotspotWidget(
        widgetResponse: ShopLayoutWidget.Widget,
        widgetLayout: ShopPageWidgetUiModel?
    )= ShopWidgetDisplayBannerProductHotspotUiModel(
        widgetId = widgetResponse.widgetID,
        layoutOrder = widgetResponse.layoutOrder,
        name = widgetResponse.name,
        type = widgetResponse.type,
        header = ShopPageHomeMapper.mapToHeaderModel(widgetResponse.header, widgetLayout),
        isFestivity = widgetLayout?.isFestivity.orFalse(),
        data = mapToBannerProductHotspotItem(widgetResponse.data).toMutableList().apply {  }
    )

    private fun mapToBannerProductHotspotItem(
        listData: List<ShopLayoutWidget.Widget.Data>
    ): List<ShopWidgetDisplayBannerProductHotspotUiModel.Data> {
        return listData.map {data ->
            ShopWidgetDisplayBannerProductHotspotUiModel.Data(
                appLink = data.appLink,
                imageUrl = data.imageUrl,
                linkType = data.linkType,
                listProductHotspot = data.productHotspot.map {
                    ShopWidgetDisplayBannerProductHotspotUiModel.Data.ProductHotspot(
                        productId = it.productID,
                        name = it.name,
                        imageUrl = it.imageUrl,
                        productUrl = it.productUrl,
                        displayedPrice = it.displayPrice,
                        isSoldOut = it.isSoldOut,
                        hotspotCoordinate = ShopWidgetDisplayBannerProductHotspotUiModel.Data.ProductHotspot.Coordinate(
                            x = it.coordinate.x.toFloatOrZero(),
                            y = it.coordinate.y.toFloatOrZero()
                        )
                    )
                }
            )
        }
    }
}
