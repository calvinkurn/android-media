package com.tokopedia.shop.pageheader.util

import com.tokopedia.feedcomponent.data.pojo.whitelist.Whitelist
import com.tokopedia.shop.common.graphql.data.isshopofficial.GetIsShopOfficialStore
import com.tokopedia.shop.common.graphql.data.isshoppowermerchant.GetIsShopPowerMerchant
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.pageheader.ShopPageHeaderConstant.SHOP_PAGE_POWER_MERCHANT_ACTIVE
import com.tokopedia.shop.pageheader.data.model.ShopPageGetHomeType
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderLayoutResponse
import com.tokopedia.shop.pageheader.presentation.uimodel.NewShopPageP1HeaderData
import com.tokopedia.shop.pageheader.presentation.uimodel.ShopPageP1HeaderData
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopHeaderComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopHeaderComponentUiModel.ComponentName.SHOP_LOGO
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopHeaderComponentUiModel.ComponentName.SHOP_NAME
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopHeaderBadgeTextValueComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopHeaderButtonComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopHeaderImageOnlyComponentUiModel
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
        ).text.getOrNull(0)?.textHtml.orEmpty()
        val shopAvatar = getShopHeaderWidgetComponentData<ShopHeaderImageOnlyComponentUiModel>(
                listShopHeaderWidget,
                SHOP_BASIC_INFO,
                SHOP_LOGO
        ).image
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
    ): T{
        return listShopHeaderWidgetData.firstOrNull {
            it.name == widgetName
        }?.components?.firstOrNull {
            it.name == componentName
        } as T
    }

    private fun mapToShopPageHeaderLayoutUiModel(
            shopPageHeaderLayoutResponseData: ShopPageHeaderLayoutResponse
    ): List<ShopHeaderWidgetUiModel> {
        return mutableListOf<ShopHeaderWidgetUiModel>().apply {
            shopPageHeaderLayoutResponseData.shopPageGetHeaderLayout.widgets.forEach { widgetResponseData ->
                mapShopHeaderWidget(widgetResponseData)?.let {
                    add(it)
                }
            }
        }
    }

    private fun mapShopHeaderWidget(
            widgetResponseData: ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.Widget
    ): ShopHeaderWidgetUiModel? {
        return if (widgetResponseData.type in ShopHeaderWidgetUiModel.WidgetType.RENDERED_WIDGETS) {
            ShopHeaderWidgetUiModel(
                    widgetResponseData.widgetID.toString(),
                    widgetResponseData.name,
                    widgetResponseData.type,
                    mutableListOf<BaseShopHeaderComponentUiModel>().apply {
                        widgetResponseData.listComponent.forEach { componentResponseData ->
                            mapShopHeaderComponent(componentResponseData)?.let {
                                add(it)
                            }
                        }
                    }
            )
        } else {
            null
        }
    }

    private fun mapShopHeaderComponent(
            component: ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.Widget.Component
    ): BaseShopHeaderComponentUiModel? {
        return when (component.type.toLowerCase()) {
            BaseShopHeaderComponentUiModel.ComponentType.IMAGE_ONLY.toLowerCase() -> mapShopHeaderImageOnlyComponent(component)
            BaseShopHeaderComponentUiModel.ComponentType.BADGE_TEXT_VALUE.toLowerCase() -> mapShopHeaderBadgeTextValueComponent(component)
            BaseShopHeaderComponentUiModel.ComponentType.BUTTON.toLowerCase() -> mapShopHeaderButtonComponent(component)
            else -> null
        }
    }

    private fun mapShopHeaderButtonComponent(
            component: ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.Widget.Component
    ) = ShopHeaderButtonComponentUiModel(
            component.name,
            component.type,
            component.data.icon,
            component.data.label,
            component.data.buttonType,
            component.data.link,
            component.data.isBottomSheet
    )

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