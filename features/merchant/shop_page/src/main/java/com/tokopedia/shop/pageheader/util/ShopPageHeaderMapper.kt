package com.tokopedia.shop.pageheader.util

import com.tokopedia.feedcomponent.data.pojo.whitelist.Whitelist
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.common.data.model.ShopPageGetDynamicTabResponse
import com.tokopedia.shop.common.data.model.ShopPageGetHomeType
import com.tokopedia.shop.common.graphql.data.isshopofficial.GetIsShopOfficialStore
import com.tokopedia.shop.common.graphql.data.isshoppowermerchant.GetIsShopPowerMerchant
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.pageheader.ShopPageHeaderConstant.SHOP_PAGE_POWER_MERCHANT_ACTIVE
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderLayoutResponse
import com.tokopedia.shop.pageheader.presentation.uimodel.ShopPageHeaderLayoutUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.ShopPageHeaderP1HeaderData
import com.tokopedia.shop.pageheader.presentation.uimodel.component.*
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopPageHeaderComponentUiModel.ComponentName.BUTTON_FOLLOW
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopPageHeaderComponentUiModel.ComponentName.BUTTON_PLAY
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopPageHeaderComponentUiModel.ComponentName.SHOP_LOGO
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopPageHeaderComponentUiModel.ComponentName.SHOP_NAME
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopPageHeaderWidgetUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopPageHeaderWidgetUiModel.WidgetType.SHOP_BASIC_INFO

object ShopPageHeaderMapper {

    fun mapToShopPageP1HeaderData(
        shopInfoOsData: GetIsShopOfficialStore,
        shopInfoGoldData: GetIsShopPowerMerchant,
        shopPageHomeTypeData: ShopPageGetHomeType,
        feedWhitelistData: Whitelist,
        shopPageHeaderLayoutData: ShopPageHeaderLayoutResponse
    ): ShopPageHeaderP1HeaderData {
        val listShopHeaderWidget = mapToShopPageHeaderLayoutWidgetUiModel(shopPageHeaderLayoutData)
        val shopName = getShopHeaderWidgetComponentData<ShopPageHeaderBadgeTextValueComponentUiModel>(
            listShopHeaderWidget,
            SHOP_BASIC_INFO,
            SHOP_NAME
        )?.text?.getOrNull(0)?.textHtml.orEmpty()
        val shopAvatar = getShopHeaderWidgetComponentData<ShopPageHeaderImageOnlyComponentUiModel>(
            listShopHeaderWidget,
            SHOP_BASIC_INFO,
            SHOP_LOGO
        )?.image.orEmpty()
        return ShopPageHeaderP1HeaderData(
            shopInfoOsData.data.isOfficial,
            shopInfoGoldData.data.powerMerchant.status == SHOP_PAGE_POWER_MERCHANT_ACTIVE,
            shopInfoGoldData.data.powerMerchant.pmTier,
            shopPageHomeTypeData.shopHomeType,
            shopName,
            shopAvatar,
            "",
            feedWhitelistData.isWhitelist,
            feedWhitelistData.url,
            listShopHeaderWidget
        )
    }

    fun mapToNewShopPageP1HeaderData(
        shopInfoCoreData: ShopInfo,
        shopPageGetDynamicTabResponse: ShopPageGetDynamicTabResponse,
        feedWhitelistData: Whitelist,
        shopPageHeaderLayoutData: ShopPageHeaderLayoutResponse
    ): ShopPageHeaderP1HeaderData {
        val listShopHeaderWidget = mapToShopPageHeaderLayoutWidgetUiModel(shopPageHeaderLayoutData)
        val shopName = getShopHeaderWidgetComponentData<ShopPageHeaderBadgeTextValueComponentUiModel>(
            listShopHeaderWidget,
            SHOP_BASIC_INFO,
            SHOP_NAME
        )?.text?.getOrNull(0)?.textHtml.orEmpty()
        val shopAvatar = getShopHeaderWidgetComponentData<ShopPageHeaderImageOnlyComponentUiModel>(
            listShopHeaderWidget,
            SHOP_BASIC_INFO,
            SHOP_LOGO
        )?.image.orEmpty()
        val shopPageHeaderLayoutUiModel = mapToShopPageHeaderLayoutUiModel(shopPageHeaderLayoutData)
        return ShopPageHeaderP1HeaderData(
            isOfficial = shopInfoCoreData.goldOS.shopTier == ShopPageConstant.ShopTierType.OFFICIAL_STORE,
            isGoldMerchant = shopInfoCoreData.goldOS.shopTier == ShopPageConstant.ShopTierType.POWER_MERCHANT_PRO,
            pmTier = shopInfoCoreData.goldOS.shopTier,
            shopHomeType = "",
            shopName = shopName,
            shopAvatar = shopAvatar,
            shopDomain = "",
            isWhitelist = feedWhitelistData.isWhitelist,
            feedUrl = feedWhitelistData.url,
            listShopPageHeaderWidget = listShopHeaderWidget,
            listDynamicTabData = shopPageGetDynamicTabResponse.shopPageGetDynamicTab.tabData,
            shopHeaderLayoutData = shopPageHeaderLayoutUiModel
        )
    }

    private fun mapToShopPageHeaderLayoutUiModel(shopPageHeaderLayoutData: ShopPageHeaderLayoutResponse): ShopPageHeaderLayoutUiModel {
        return ShopPageHeaderLayoutUiModel(
            mapToShopPageHeaderLayoutListConfig(shopPageHeaderLayoutData.shopPageGetHeaderLayout.generalComponentConfigList),
            shopPageHeaderLayoutData.shopPageGetHeaderLayout.isOverrideTheme
        )
    }

    private fun mapToShopPageHeaderLayoutListConfig(generalComponentConfigList: List<ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.GeneralComponentConfigList>): List<ShopPageHeaderLayoutUiModel.Config> {
        return generalComponentConfigList.map {
            ShopPageHeaderLayoutUiModel.Config(
                it.name,
                it.type,
                it.data.patternColorType,
                it.data.listBackgroundColor,
                mapToListBackgroundObject(it.data.listBackgroundObject),
                mapToListColorSchema(it.data.listColorSchema),
            )
        }
    }

    private fun mapToListColorSchema(listColorSchema: List<ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.GeneralComponentConfigList.DataComponentConfig.ColorSchema>): ShopPageColorSchema {
        return ShopPageColorSchema(
            listColorSchema = listColorSchema.map {
                ShopPageColorSchema.ColorSchema(
                    it.name,
                    it.type,
                    it.value
                )
            }
        )
    }

    private fun mapToListBackgroundObject(listBackgroundObject: List<ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.GeneralComponentConfigList.DataComponentConfig.BackgroundObject>): List<ShopPageHeaderLayoutUiModel.Config.BackgroundObject> {
        return listBackgroundObject.map {
            ShopPageHeaderLayoutUiModel.Config.BackgroundObject(
                it.url,
                it.type,
            )
        }
    }

    private inline fun <reified T> getShopHeaderWidgetComponentData(
        listShopPageHeaderWidgetData: List<ShopPageHeaderWidgetUiModel>,
        widgetName: String,
        componentName: String
    ): T? {
        return listShopPageHeaderWidgetData.firstOrNull {
            it.name == widgetName
        }?.componentPages?.firstOrNull {
            it.name == componentName
        } as T?
    }

    private var headerComponentPosition: Int = -1
    private fun mapToShopPageHeaderLayoutWidgetUiModel(
        shopPageHeaderLayoutResponseData: ShopPageHeaderLayoutResponse
    ): List<ShopPageHeaderWidgetUiModel> {
        return mutableListOf<ShopPageHeaderWidgetUiModel>().apply {
            headerComponentPosition = 0
            shopPageHeaderLayoutResponseData.shopPageGetHeaderLayout.widgets.forEach { widgetResponseData ->
                add(mapShopHeaderWidget(widgetResponseData))
            }
        }
    }

    private fun mapShopHeaderWidget(
        widgetResponseData: ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.Widget
    ): ShopPageHeaderWidgetUiModel {
        return ShopPageHeaderWidgetUiModel(
            widgetResponseData.widgetID,
            widgetResponseData.name,
            widgetResponseData.type,
            mutableListOf<BaseShopPageHeaderComponentUiModel>().apply {
                widgetResponseData.listComponent.forEachIndexed { index, componentResponseData ->
                    if (shouldIncrementHeaderComponentPosition(widgetResponseData, index)) {
                        headerComponentPosition++
                    }
                    mapShopHeaderComponent(headerComponentPosition, componentResponseData)?.let {
                        add(it)
                    }
                }
            }
        )
    }

    private fun shouldIncrementHeaderComponentPosition(
        widgetResponseData: ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.Widget,
        index: Int
    ): Boolean {
        return if (widgetResponseData.type.equals(SHOP_BASIC_INFO, true)) {
            index < 1
        } else {
            true
        }
    }

    private fun mapShopHeaderComponent(
        componentPosition: Int,
        component: ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.Widget.Component
    ): BaseShopPageHeaderComponentUiModel? {
        return when (component.type.toLowerCase()) {
            BaseShopPageHeaderComponentUiModel.ComponentType.IMAGE_ONLY.toLowerCase() -> mapShopHeaderImageOnlyComponent(component)
            BaseShopPageHeaderComponentUiModel.ComponentType.BADGE_TEXT_VALUE.toLowerCase() -> mapShopHeaderBadgeTextValueComponent(component)
            BaseShopPageHeaderComponentUiModel.ComponentType.BUTTON.toLowerCase() -> mapShopHeaderButtonComponent(component)
            BaseShopPageHeaderComponentUiModel.ComponentType.IMAGE_TEXT.toLowerCase() -> mapShopHeaderImageTextComponent(component)
            else -> null
        }?.apply {
            this.componentPosition = componentPosition
        }
    }

    private fun mapShopHeaderImageTextComponent(
        component: ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.Widget.Component
    ) = ShopPageHeaderImageTextComponentUiModel(
        component.name,
        component.type,
        mapShopHeaderImageTextComponentImagesData(component.data.images),
        mapShopHeaderImageTextComponentTextData(component.data.textComponent)
    )

    private fun mapShopHeaderImageTextComponentTextData(
        textComponent: ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.Widget.Component.TextComponent
    ) = ShopPageHeaderImageTextComponentUiModel.TextComponent(
        ShopPageHeaderImageTextComponentUiModel.TextComponent.Data(
            textComponent.data.icon,
            textComponent.data.isBottomSheet,
            textComponent.data.textHtml,
            textComponent.data.textLink
        ),
        textComponent.style
    )

    private fun mapShopHeaderImageTextComponentImagesData(
        images: ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.Widget.Component.Images
    ) = ShopPageHeaderImageTextComponentUiModel.Images(
        images.data.map {
            ShopPageHeaderImageTextComponentUiModel.Images.Data(
                it.image,
                it.imageLink,
                it.isBottomSheet
            )
        },
        images.style
    )

    private fun mapShopHeaderButtonComponent(
        component: ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.Widget.Component
    ) = when (component.name) {
        BUTTON_PLAY -> {
            mapToPlayButtonComponent(component)
        }
        BUTTON_FOLLOW -> {
            mapToFollowButtonComponent(component)
        }
        else -> {
            mapToGeneralButtonComponent(component)
        }
    }

    private fun mapToFollowButtonComponent(
        component: ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.Widget.Component
    ) = ShopPageHeaderActionWidgetFollowButtonComponentUiModel().apply {
        mapComponentModel(component)
    }

    private fun mapToPlayButtonComponent(
        component: ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.Widget.Component
    ) = ShopPageHeaderPlayWidgetButtonComponentUiModel().apply {
        mapComponentModel(component)
    }

    private fun mapToGeneralButtonComponent(
        component: ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.Widget.Component
    ) = ShopPageHeaderButtonComponentUiModel().apply {
        mapComponentModel(component)
    }

    private fun mapShopHeaderImageOnlyComponent(
        component: ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.Widget.Component
    ) = ShopPageHeaderImageOnlyComponentUiModel(
        component.name,
        component.type,
        component.data.image,
        component.data.imageLink,
        component.data.isBottomSheet
    )

    private fun mapShopHeaderBadgeTextValueComponent(
        component: ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.Widget.Component
    ) = ShopPageHeaderBadgeTextValueComponentUiModel(
        component.name,
        component.type,
        component.data.ctaText,
        component.data.ctaLink,
        component.data.ctaIcon,
        mutableListOf<ShopPageHeaderBadgeTextValueComponentUiModel.Text>().apply {
            component.data.listText.forEach {
                add(
                    ShopPageHeaderBadgeTextValueComponentUiModel.Text(
                        it.icon,
                        it.textLink,
                        it.textHtml,
                        it.isBottomSheet
                    )
                )
            }
        }
    )
}
