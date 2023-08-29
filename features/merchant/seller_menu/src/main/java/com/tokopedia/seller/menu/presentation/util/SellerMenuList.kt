package com.tokopedia.seller.menu.presentation.util

import com.tokopedia.iconunify.IconUnify
import com.tokopedia.seller.menu.R
import com.tokopedia.seller.menu.common.analytics.SettingTrackingConstant
import com.tokopedia.seller.menu.common.constant.MenuItemType
import com.tokopedia.seller.menu.common.view.uimodel.base.DividerType
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuComposeItem
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuDividerUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuItemUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuOrderUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuProductUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuSectionTitleUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuSettingTitleUiModel
import com.tokopedia.shopadmin.common.util.AdminPermissionMapper
import com.tokopedia.user.session.UserSessionInterface

object SellerMenuList {

    fun createInitialItems(
        userSession: UserSessionInterface,
        mapper: AdminPermissionMapper
    ): List<SellerMenuComposeItem> {
        val isShopOwner = userSession.isShopOwner
        val menuList = mutableListOf<SellerMenuComposeItem>()
        val buyerInfoMenu = createBuyerInfoMenu()
        val otherInfoMenu = createOtherInfoMenu()

        menuList.add(
            SellerMenuSettingTitleUiModel(
                titleRes = R.string.seller_menu_order_section,
                ctaRes = R.string.seller_menu_order_cta,
                dimenRes = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4
            )
        )
        menuList.add(SellerMenuOrderUiModel())
        menuList.add(SellerMenuDividerUiModel(DividerType.THIN_PARTIAL))

        menuList.add(
            SellerMenuSettingTitleUiModel(
                titleRes = R.string.seller_menu_product_section,
                ctaRes = R.string.seller_menu_product_cta,
                dimenRes = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4
            )
        )
        menuList.add(SellerMenuProductUiModel())
        menuList.add(SellerMenuDividerUiModel(DividerType.THIN_PARTIAL))

        menuList.addAll(buyerInfoMenu)
        menuList.addAll(otherInfoMenu)
        menuList.add(SellerMenuDividerUiModel(DividerType.THICK))

        return menuList.toList()
    }

    private fun createBuyerInfoMenu(): List<SellerMenuComposeItem> {
        return listOf(
            SellerMenuSettingTitleUiModel(
                titleRes = com.tokopedia.seller.menu.common.R.string.setting_menu_buyer_info,
                ctaRes = null,
                dimenRes = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4
            ),
            SellerMenuItemUiModel(
                titleRes = com.tokopedia.seller.menu.common.R.string.setting_menu_review,
                type = MenuItemType.REVIEW,
                eventActionSuffix = SettingTrackingConstant.REVIEW,
                iconUnifyType = IconUnify.STAR
            ),
            SellerMenuItemUiModel(
                titleRes = com.tokopedia.seller.menu.common.R.string.setting_menu_discussion,
                type = MenuItemType.DISCUSSION,
                eventActionSuffix = SettingTrackingConstant.DISCUSSION,
                iconUnifyType = IconUnify.DISCUSSION
            ),
            SellerMenuItemUiModel(
                titleRes = com.tokopedia.seller.menu.common.R.string.setting_menu_complaint,
                type = MenuItemType.COMPLAIN,
                eventActionSuffix = SettingTrackingConstant.COMPLAINT,
                iconUnifyType = IconUnify.PRODUCT_INFO
            )
        )
    }

    private fun createOtherInfoMenu(): List<SellerMenuComposeItem> {
        return listOf(
            SellerMenuDividerUiModel(DividerType.THICK),
            SellerMenuSectionTitleUiModel(
                titleRes = R.string.setting_menu_other_info
            ),
            SellerMenuItemUiModel(
                titleRes = com.tokopedia.seller.menu.common.R.string.setting_menu_seller_education_center,
                type = MenuItemType.SELLER_EDU,
                eventActionSuffix = SettingTrackingConstant.SELLER_CENTER,
                iconUnifyType = IconUnify.SHOP_INFO
            ),
            SellerMenuItemUiModel(
                titleRes = com.tokopedia.seller.menu.common.R.string.setting_menu_tokopedia_care,
                type = MenuItemType.TOKOPEDIA_CARE,
                eventActionSuffix = SettingTrackingConstant.TOKOPEDIA_CARE,
                iconUnifyType = IconUnify.CALL_CENTER
            ),
            SellerMenuItemUiModel(
                titleRes = com.tokopedia.seller.menu.common.R.string.setting_menu_shop_setting,
                type = MenuItemType.SHOP_SETTINGS,
                eventActionSuffix = SettingTrackingConstant.SETTINGS,
                iconUnifyType = IconUnify.SETTING
            )
        )
    }

}
