package com.tokopedia.shop.common.data.mapper

import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toFloatOrZero
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.shop.campaign.view.model.ShopCampaignWidgetCarouselProductUiModel
import com.tokopedia.shop.campaign.view.model.ShopWidgetDisplaySliderBannerHighlightUiModel
import com.tokopedia.shop.common.data.model.DynamicRule
import com.tokopedia.shop.common.data.model.ShopPageWidgetUiModel
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.home.data.model.ShopLayoutWidget
import com.tokopedia.shop.home.data.model.ShopPageWidgetRequestModel
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.model.showcase_navigation.appearance.CarouselAppearance
import com.tokopedia.shop.home.view.model.showcase_navigation.appearance.LeftMainBannerAppearance
import com.tokopedia.shop.home.view.model.banner_product_group.ShopWidgetComponentBannerProductGroupUiModel
import com.tokopedia.shop.home.view.model.showcase_navigation.ShowcaseNavigationUiModel
import com.tokopedia.shop.home.view.model.ShopWidgetDisplayBannerProductHotspotUiModel
import com.tokopedia.shop.home.view.model.ShopWidgetDisplayBannerTimerUiModel
import com.tokopedia.shop.home.view.model.ShopWidgetVoucherSliderUiModel
import com.tokopedia.shop.home.view.model.showcase_navigation.ShowcaseNavigationBannerWidgetStyle
import com.tokopedia.shop.home.view.model.StatusCampaign
import com.tokopedia.shop.home.view.model.banner_product_group.ShopWidgetComponentBannerProductGroupUiModel.Tab.ComponentList.ComponentType
import com.tokopedia.shop.home.view.model.banner_product_group.ShopWidgetComponentBannerProductGroupUiModel.Tab.ComponentList.Data.LinkType
import com.tokopedia.shop.home.view.model.showcase_navigation.Showcase
import com.tokopedia.shop.home.view.model.showcase_navigation.ShowcaseCornerShape
import com.tokopedia.shop.home.view.model.showcase_navigation.ShowcaseTab
import com.tokopedia.shop.home.view.model.showcase_navigation.appearance.TopMainBannerAppearance

//TODO need to migrate all other shop widget mapper on home mapper to this mapper
object ShopPageWidgetMapper {

    fun mapToBannerTimerWidget(
        widgetResponse: ShopLayoutWidget.Widget,
        widgetLayout: ShopPageWidgetUiModel?,
        isOverrideTheme: Boolean,
        colorSchema: ShopPageColorSchema
    )= ShopWidgetDisplayBannerTimerUiModel(
        widgetId = widgetResponse.widgetID,
        layoutOrder = widgetResponse.layoutOrder,
        name = widgetResponse.name,
        type = widgetResponse.type,
        header = ShopPageHomeMapper.mapToHeaderModel(widgetResponse.header, widgetLayout, isOverrideTheme, colorSchema),
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
        header = ShopPageHomeMapper.mapToHeaderModel(widgetResponse.header, widgetLayout, false, ShopPageColorSchema()),
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
        header = ShopPageHomeMapper.mapToHeaderModel(widgetResponse.header, widgetLayout, false, ShopPageColorSchema()),
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
        ShopPageHomeMapper.mapToHeaderModel(widgetResponse.header, widgetLayout, false, ShopPageColorSchema()),
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

    fun mapToHomeShowcaseNavigationWidget(
        response: ShopLayoutWidget.Widget,
        isOverrideTheme: Boolean,
        widgetLayout: ShopPageWidgetUiModel?,
        colorSchema: ShopPageColorSchema
    ): ShowcaseNavigationUiModel {
        val tabs = response.data.map { tab ->
            val showcases = tab.showcaseList.map { showcase ->
                Showcase(
                    showcase.showcaseID,
                    showcase.name,
                    showcase.imageURL,
                    showcase.ctaLink,
                    showcase.isMainBanner
                )
            }

            ShowcaseTab(text = tab.text, imageUrl = tab.imageURL, showcases = showcases)
        }

        val showcases = tabs.firstOrNull()?.showcases ?: emptyList()

        val appearance = when (response.header.widgetStyle) {
            ShowcaseNavigationBannerWidgetStyle.TOP_ROUNDED_CORNER.id -> {
                TopMainBannerAppearance(response.header.title, showcases, response.header.ctaLink, ShowcaseCornerShape.ROUNDED_CORNER)
            }
            ShowcaseNavigationBannerWidgetStyle.TOP_CIRCLE.id -> {
                TopMainBannerAppearance(response.header.title, showcases, response.header.ctaLink, ShowcaseCornerShape.CIRCLE)
            }
            ShowcaseNavigationBannerWidgetStyle.LEFT_ROUNDED_CORNER.id -> {
                LeftMainBannerAppearance(tabs, response.header.title, response.header.ctaLink, ShowcaseCornerShape.ROUNDED_CORNER)
            }
            ShowcaseNavigationBannerWidgetStyle.LEFT_CIRCLE.id -> {
                LeftMainBannerAppearance(tabs, response.header.title, response.header.ctaLink, ShowcaseCornerShape.CIRCLE)
            }
            ShowcaseNavigationBannerWidgetStyle.CAROUSEL_ROUNDED_CORNER.id -> {
                CarouselAppearance(response.header.title, showcases, response.header.ctaLink, ShowcaseCornerShape.CIRCLE)
            }
            ShowcaseNavigationBannerWidgetStyle.CAROUSEL_CIRCLE.id -> {
                CarouselAppearance(response.header.title, showcases, response.header.ctaLink, ShowcaseCornerShape.CIRCLE)
            }
            else -> TopMainBannerAppearance(response.header.title, showcases, response.header.ctaLink, ShowcaseCornerShape.ROUNDED_CORNER)
        }


        return ShowcaseNavigationUiModel(
            appearance = appearance,
            widgetId = response.widgetID,
            header = ShopPageHomeMapper.mapToHeaderModel(response.header, widgetLayout, isOverrideTheme, colorSchema),
            layoutOrder = response.layoutOrder,
            name = response.name,
            type = response.type
        )
    }
    fun mapToHomeBannerProductGroupWidget(
        response: ShopLayoutWidget.Widget,
        widgetLayout: ShopPageWidgetUiModel?,
        isOverrideTheme: Boolean,
        colorSchema: ShopPageColorSchema
    ): ShopWidgetComponentBannerProductGroupUiModel {
        val tabs = response.data.map { tab ->
            val componentList = tab.componentList.map { component ->

                val componentType = when (component.componentType) {
                    ComponentType.PRODUCT.id -> ComponentType.PRODUCT
                    ComponentType.DISPLAY_SINGLE_COLUMN.id -> ComponentType.DISPLAY_SINGLE_COLUMN
                    else -> ComponentType.DISPLAY_SINGLE_COLUMN
                }

                val data = component.data.map { data ->
                    val linkType = when (data.linkType) {
                        LinkType.PRODUCT.id -> LinkType.PRODUCT
                        LinkType.SHOWCASE.id -> LinkType.SHOWCASE
                        LinkType.FEATURED_PRODUCT.id -> LinkType.FEATURED_PRODUCT
                        else -> LinkType.PRODUCT
                    }

                    ShopWidgetComponentBannerProductGroupUiModel.Tab.ComponentList.Data(
                        data.imageUrl,
                        data.ctaLink,
                        data.linkID,
                        linkType,
                        data.isShowProductInfo
                    )
                }

                ShopWidgetComponentBannerProductGroupUiModel.Tab.ComponentList(
                    component.componentID,
                    component.componentName,
                    componentType,
                    data
                )
            }

            ShopWidgetComponentBannerProductGroupUiModel.Tab(tab.tabLabel, tab.tabName, componentList)
        }

        val viewAllChevronAppLink = response.header.ctaLink

        return ShopWidgetComponentBannerProductGroupUiModel(
            widgetId = response.widgetID,
            layoutOrder = response.layoutOrder,
            title = response.header.title,
            header = ShopPageHomeMapper.mapToHeaderModel(response.header, widgetLayout, isOverrideTheme, colorSchema),
            tabs = tabs,
            name = response.name,
            type = response.type,
            viewAllChevronAppLink = viewAllChevronAppLink,
            widgetStyle = response.header.widgetStyle
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
        widgetLayout: ShopPageWidgetUiModel?,
        isOverrideTheme: Boolean,
        colorSchema: ShopPageColorSchema
    )= ShopWidgetDisplayBannerProductHotspotUiModel(
        widgetId = widgetResponse.widgetID,
        layoutOrder = widgetResponse.layoutOrder,
        name = widgetResponse.name,
        type = widgetResponse.type,
        header = ShopPageHomeMapper.mapToHeaderModel(widgetResponse.header, widgetLayout, isOverrideTheme, colorSchema),
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
