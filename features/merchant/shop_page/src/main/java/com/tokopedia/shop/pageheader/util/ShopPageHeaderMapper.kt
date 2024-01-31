package com.tokopedia.shop.pageheader.util

import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.constant.ShopPageConstant.FORMAT_CONVERT_PERCENTAGE_TO_HEX
import com.tokopedia.shop.common.data.model.ShopPageGetDynamicTabResponse
import com.tokopedia.shop.common.data.model.ShopPageGetHomeType
import com.tokopedia.shop.common.graphql.data.isshopofficial.GetIsShopOfficialStore
import com.tokopedia.shop.common.graphql.data.isshoppowermerchant.GetIsShopPowerMerchant
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
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
        shopPageHeaderLayoutData: ShopPageHeaderLayoutResponse
    ): ShopPageHeaderP1HeaderData {
        val listShopHeaderWidget = mapToShopPageHeaderLayoutWidgetUiModel(shopPageHeaderLayoutData, shopInfoGoldData.data.shopId)
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
            isOfficial = shopInfoOsData.data.isOfficial,
            isGoldMerchant = shopInfoGoldData.data.powerMerchant.status == SHOP_PAGE_POWER_MERCHANT_ACTIVE,
            pmTier = shopInfoGoldData.data.powerMerchant.pmTier,
            shopHomeType = shopPageHomeTypeData.shopHomeType,
            shopName = shopName,
            shopAvatar = shopAvatar,
            shopDomain = "",
            listShopPageHeaderWidget = listShopHeaderWidget
        )
    }

    fun mapToNewShopPageP1HeaderData(
        shopInfoCoreData: ShopInfo,
        shopPageGetDynamicTabResponse: ShopPageGetDynamicTabResponse,
        shopPageHeaderLayoutData: ShopPageHeaderLayoutResponse,
        shopPageColorSchemaDefaultConfigColor: Map<ShopPageColorSchema.ColorSchemaName, String>,
        isEnableShopReimagined: Boolean
    ): ShopPageHeaderP1HeaderData {
        val listShopHeaderWidget = mapToShopPageHeaderLayoutWidgetUiModel(shopPageHeaderLayoutData, shopInfoCoreData.shopCore.shopID)
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
        val shopPageHeaderLayoutUiModel = mapToShopPageHeaderLayoutUiModel(
            shopPageHeaderLayoutData,
            shopPageColorSchemaDefaultConfigColor,
            isEnableShopReimagined
        )
        return ShopPageHeaderP1HeaderData(
            isOfficial = shopInfoCoreData.goldOS.shopTier == ShopPageConstant.ShopTierType.OFFICIAL_STORE,
            isGoldMerchant = shopInfoCoreData.goldOS.shopTier == ShopPageConstant.ShopTierType.POWER_MERCHANT_PRO,
            pmTier = shopInfoCoreData.goldOS.shopTier,
            shopHomeType = "",
            shopName = shopName,
            shopAvatar = shopAvatar,
            shopDomain = "",
            listShopPageHeaderWidget = listShopHeaderWidget,
            listDynamicTabData = shopPageGetDynamicTabResponse.shopPageGetDynamicTab.tabData,
            shopHeaderLayoutData = shopPageHeaderLayoutUiModel
        )
    }

    private fun mapToShopPageHeaderLayoutUiModel(
        shopPageHeaderLayoutData: ShopPageHeaderLayoutResponse,
        shopPageColorSchemaDefaultConfigColor: Map<ShopPageColorSchema.ColorSchemaName, String>,
        isEnableShopReimagined: Boolean
    ): ShopPageHeaderLayoutUiModel {
        return ShopPageHeaderLayoutUiModel(
            mapToShopPageHeaderLayoutListConfig(
                shopPageHeaderLayoutData.shopPageGetHeaderLayout.generalComponentConfigList,
                shopPageColorSchemaDefaultConfigColor
            ),
            shopPageHeaderLayoutData.shopPageGetHeaderLayout.isOverrideTheme && isEnableShopReimagined
        )
    }

    private fun mapToShopPageHeaderLayoutListConfig(
        generalComponentConfigList: List<ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.GeneralComponentConfigList>,
        shopPageColorSchemaDefaultConfigColor: Map<ShopPageColorSchema.ColorSchemaName, String>
    ): List<ShopPageHeaderLayoutUiModel.Config> {
        return generalComponentConfigList.map {
            ShopPageHeaderLayoutUiModel.Config(
                it.name,
                it.type,
                it.data.patternColorType.lowercase(),
                it.data.listBackgroundColor,
                mapToListBackgroundObject(it.data.listBackgroundObject),
                mapToListColorSchema(it.data.listColorSchema, shopPageColorSchemaDefaultConfigColor)
            )
        }
    }

    private fun mapToListColorSchema(
        listColorSchema: List<ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.GeneralComponentConfigList.DataComponentConfig.ColorSchema>,
        shopPageColorSchemaDefaultConfigColor: Map<ShopPageColorSchema.ColorSchemaName, String>
    ): ShopPageColorSchema {
        return ShopPageColorSchema(
            listColorSchema = listColorSchema.map {
                ShopPageColorSchema.ColorSchema(
                    it.name,
                    it.type,
                    getHexColor(it.name, it.value, shopPageColorSchemaDefaultConfigColor)
                )
            }
        )
    }

    private fun getHexColor(
        name: String,
        hexStringValue: String,
        shopPageColorSchemaDefaultConfigColor: Map<ShopPageColorSchema.ColorSchemaName, String>
    ): String {
        return if (hexStringValue.isEmpty()) {
            shopPageColorSchemaDefaultConfigColor.getOrDefault(ShopPageColorSchema.ColorSchemaName.fromValue(name), "")
        } else {
            parseFormattedHexString(hexStringValue)
        }
    }

    /**
     * This function check whether hexStringValue is formatted like #212121-20
     * If it's match the format, then it will convert it to string hex with opacity (e.g. #33212121)
     * otherwise it will return current hexStringValue
     */
    private fun parseFormattedHexString(hexStringValue: String): String {
        val splitHexColor = hexStringValue.split("-")
        return if (splitHexColor.size > Int.ONE) {
            try {
                val hexString = splitHexColor.getOrNull(Int.ZERO).orEmpty()
                val opacityPercentage = splitHexColor.getOrNull(Int.ONE).toIntOrZero()
                val opacityHexString = convertPercentageToOpacityHexString(opacityPercentage)
                hexString.substring(
                    Int.ZERO,
                    Int.ONE
                ) + opacityHexString + hexString.substring(Int.ONE)
            } catch (_: Exception) {
                ""
            }
        } else {
            hexStringValue
        }
    }

    private fun convertPercentageToOpacityHexString(opacityPercentage: Int): String {
        return if (opacityPercentage in 0 until 100) {
            val decimalOpacity = opacityPercentage / 100.0
            val roundedValue = (decimalOpacity * 255).toInt()
            String.format(FORMAT_CONVERT_PERCENTAGE_TO_HEX, roundedValue)
        } else {
            ""
        }
    }

    private fun mapToListBackgroundObject(listBackgroundObject: List<ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.GeneralComponentConfigList.DataComponentConfig.BackgroundObject>): List<ShopPageHeaderLayoutUiModel.Config.BackgroundObject> {
        return listBackgroundObject.map {
            ShopPageHeaderLayoutUiModel.Config.BackgroundObject(
                it.url,
                it.type
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
        shopPageHeaderLayoutResponseData: ShopPageHeaderLayoutResponse,
        shopId: String
    ): List<ShopPageHeaderWidgetUiModel> {
        return mutableListOf<ShopPageHeaderWidgetUiModel>().apply {
            headerComponentPosition = 0
            shopPageHeaderLayoutResponseData.shopPageGetHeaderLayout.widgets.forEach { widgetResponseData ->
                add(mapShopHeaderWidget(widgetResponseData, shopId))
            }
        }
    }

    private fun mapShopHeaderWidget(
        widgetResponseData: ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.Widget,
        shopId: String
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
                    mapShopHeaderComponent(headerComponentPosition, componentResponseData, shopId)?.let {
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
        component: ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.Widget.Component,
        shopId: String
    ): BaseShopPageHeaderComponentUiModel? {
        return when (component.type.toLowerCase()) {
            BaseShopPageHeaderComponentUiModel.ComponentType.IMAGE_ONLY.toLowerCase() -> mapShopHeaderImageOnlyComponent(component, shopId)
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
        component: ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.Widget.Component,
        shopId: String
    ) = ShopPageHeaderImageOnlyComponentUiModel(
        component.name,
        component.type,
        shopId,
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
