package com.tokopedia.shop.pageheader.util

import com.tokopedia.feedcomponent.data.pojo.whitelist.Whitelist
import com.tokopedia.shop.common.graphql.data.isshopofficial.GetIsShopOfficialStore
import com.tokopedia.shop.common.graphql.data.isshoppowermerchant.GetIsShopPowerMerchant
import com.tokopedia.shop.pageheader.ShopPageHeaderConstant.SHOP_PAGE_POWER_MERCHANT_ACTIVE
import com.tokopedia.shop.pageheader.data.model.ShopPageGetHomeType
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderLayoutResponse
import com.tokopedia.shop.pageheader.presentation.uimodel.NewShopPageP1HeaderData
import com.tokopedia.shop.pageheader.presentation.uimodel.component.*
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopHeaderComponentUiModel.ComponentName.BUTTON_FOLLOW
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopHeaderComponentUiModel.ComponentName.BUTTON_PLAY
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopHeaderComponentUiModel.ComponentName.SHOP_LOGO
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopHeaderComponentUiModel.ComponentName.SHOP_NAME
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel.WidgetType.SHOP_BASIC_INFO

object NewShopPageHeaderMapper {

    fun mapToShopPageP1HeaderData(
            shopInfoOsData: GetIsShopOfficialStore,
            shopInfoGoldData: GetIsShopPowerMerchant,
            shopPageHomeTypeData: ShopPageGetHomeType,
            feedWhitelistData: Whitelist,
            shopPageHeaderLayoutData: ShopPageHeaderLayoutResponse
    ) : NewShopPageP1HeaderData {
        val listShopHeaderWidget = mapToShopPageHeaderLayoutUiModel(shopPageHeaderLayoutData)
        val shopName = getShopHeaderWidgetComponentData<ShopHeaderBadgeTextValueComponentUiModel>(
                listShopHeaderWidget,
                SHOP_BASIC_INFO,
                SHOP_NAME
        )?.text?.getOrNull(0)?.textHtml.orEmpty()
        val shopAvatar = getShopHeaderWidgetComponentData<ShopHeaderImageOnlyComponentUiModel>(
                listShopHeaderWidget,
                SHOP_BASIC_INFO,
                SHOP_LOGO
        )?.image.orEmpty()
        return NewShopPageP1HeaderData(
                shopInfoOsData.data.isOfficial,
                shopInfoGoldData.data.powerMerchant.status == SHOP_PAGE_POWER_MERCHANT_ACTIVE,
                shopPageHomeTypeData.shopHomeType,
                shopName,
                shopAvatar,
                "",
                feedWhitelistData.isWhitelist,
                feedWhitelistData.url,
                listShopHeaderWidget
        )
    }

    private inline fun <reified T> getShopHeaderWidgetComponentData(
            listShopHeaderWidgetData: List<ShopHeaderWidgetUiModel>,
            widgetName: String,
            componentName: String
    ): T?{
        return listShopHeaderWidgetData.firstOrNull {
            it.name == widgetName
        }?.components?.firstOrNull {
            it.name == componentName
        } as T?
    }

    private var headerComponentPosition: Int = -1
    private fun mapToShopPageHeaderLayoutUiModel(
            shopPageHeaderLayoutResponseData: ShopPageHeaderLayoutResponse
    ): List<ShopHeaderWidgetUiModel> {
        return mutableListOf<ShopHeaderWidgetUiModel>().apply {
            headerComponentPosition = 0
            shopPageHeaderLayoutResponseData.shopPageGetHeaderLayout.widgets.forEach { widgetResponseData ->
                add(mapShopHeaderWidget(widgetResponseData))
            }
        }
    }

    private fun mapShopHeaderWidget(
            widgetResponseData: ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.Widget
    ): ShopHeaderWidgetUiModel {
        return ShopHeaderWidgetUiModel(
                widgetResponseData.widgetID,
                widgetResponseData.name,
                widgetResponseData.type,
                mutableListOf<BaseShopHeaderComponentUiModel>().apply {
                    widgetResponseData.listComponent.forEachIndexed { index, componentResponseData ->
                        if(shouldIncrementHeaderComponentPosition(widgetResponseData, index)){
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
    ): BaseShopHeaderComponentUiModel? {
        return when (component.type.toLowerCase()) {
            BaseShopHeaderComponentUiModel.ComponentType.IMAGE_ONLY.toLowerCase() -> mapShopHeaderImageOnlyComponent(component)
            BaseShopHeaderComponentUiModel.ComponentType.BADGE_TEXT_VALUE.toLowerCase() -> mapShopHeaderBadgeTextValueComponent(component)
            BaseShopHeaderComponentUiModel.ComponentType.BUTTON.toLowerCase() -> mapShopHeaderButtonComponent(component)
            else -> null
        }?.apply {
            this.componentPosition = componentPosition
        }
    }

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
    ) = ShopHeaderActionWidgetFollowButtonComponentUiModel().apply {
        mapComponentModel(component)
    }

    private fun mapToPlayButtonComponent(
            component: ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.Widget.Component
    ) = ShopHeaderPlayWidgetButtonComponentUiModel().apply {
        mapComponentModel(component)
    }

    private fun mapToGeneralButtonComponent(
            component: ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.Widget.Component
    ) = ShopHeaderButtonComponentUiModel().apply {
        mapComponentModel(component)
    }

    private fun mapShopHeaderImageOnlyComponent(
            component: ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.Widget.Component
    ) = ShopHeaderImageOnlyComponentUiModel(
            component.name,
            component.type,
            component.data.image,
            component.data.imageLink,
            component.data.isBottomSheet
    )

    private fun mapShopHeaderBadgeTextValueComponent(
            component: ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.Widget.Component
    ) = ShopHeaderBadgeTextValueComponentUiModel(
            component.name,
            component.type,
            component.data.ctaText,
            component.data.ctaLink,
            component.data.ctaIcon,
            mutableListOf<ShopHeaderBadgeTextValueComponentUiModel.Text>().apply {
                component.data.listText.forEach {
                    add(ShopHeaderBadgeTextValueComponentUiModel.Text(
                            it.icon,
                            it.textLink,
                            it.textHtml,
                            it.isBottomSheet
                    ))
                }
            }
    )

}