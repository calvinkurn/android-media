package com.tokopedia.seller.menu.presentation.util

import com.tokopedia.iconunify.IconUnify
import com.tokopedia.seller.menu.R
import com.tokopedia.seller.menu.common.analytics.SettingTrackingConstant
import com.tokopedia.seller.menu.common.constant.MenuItemType
import com.tokopedia.seller.menu.common.view.uimodel.base.DividerType
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuActionClick
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuComposeItem
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuDividerUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuFeatureUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuInfoLoadingUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuItemUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuOrderUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuProductUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuSectionTitleUiModel
import com.tokopedia.seller.menu.presentation.uimodel.compose.SellerMenuSettingTitleUiModel
import com.tokopedia.seller.menu.common.R as sellermenucommonR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

object SellerMenuComposeList {

    fun createInitialItems(): List<SellerMenuComposeItem> {
        val menuList = mutableListOf<SellerMenuComposeItem>()
        val buyerInfoMenu = createBuyerInfoMenu()
        val otherInfoMenu = createOtherInfoMenu()

        menuList.add(SellerMenuInfoLoadingUiModel)
        menuList.add(SellerMenuDividerUiModel(DividerType.THICK, "1"))
        menuList.add(
            SellerMenuSettingTitleUiModel(
                titleRes = R.string.seller_menu_order_section,
                ctaRes = R.string.seller_menu_order_cta,
                dimenRes = unifyprinciplesR.dimen.spacing_lvl4,
                actionClick = SellerMenuActionClick.ALL_ORDER
            )
        )
        menuList.add(SellerMenuOrderUiModel())
        menuList.add(SellerMenuDividerUiModel(DividerType.THIN_PARTIAL, "2"))

        menuList.add(
            SellerMenuSettingTitleUiModel(
                titleRes = R.string.seller_menu_product_section,
                ctaRes = R.string.seller_menu_product_cta,
                dimenRes = unifyprinciplesR.dimen.spacing_lvl4,
                actionClick = SellerMenuActionClick.ADD_PRODUCT
            )
        )
        menuList.add(SellerMenuProductUiModel())
        menuList.add(SellerMenuDividerUiModel(DividerType.THIN_PARTIAL, "3"))

        menuList.addAll(buyerInfoMenu)
        menuList.addAll(otherInfoMenu)
        menuList.add(SellerMenuDividerUiModel(DividerType.THICK, "4"))
        menuList.add(SellerMenuFeatureUiModel)

        return menuList.toList()
    }

    private fun createBuyerInfoMenu(): List<SellerMenuComposeItem> {
        return listOf(
            SellerMenuSettingTitleUiModel(
                titleRes = sellermenucommonR.string.setting_menu_buyer_info,
                ctaRes = null,
                dimenRes = unifyprinciplesR.dimen.spacing_lvl4,
                actionClick = SellerMenuActionClick.NONE
            ),
            SellerMenuItemUiModel(
                titleRes = sellermenucommonR.string.setting_menu_review,
                type = MenuItemType.REVIEW,
                eventActionSuffix = SettingTrackingConstant.REVIEW,
                iconUnifyType = IconUnify.STAR,
                actionClick = SellerMenuActionClick.REVIEW
            ),
            SellerMenuItemUiModel(
                titleRes = sellermenucommonR.string.setting_menu_discussion,
                type = MenuItemType.DISCUSSION,
                eventActionSuffix = SettingTrackingConstant.DISCUSSION,
                iconUnifyType = IconUnify.DISCUSSION,
                actionClick = SellerMenuActionClick.DISCUSSION
            ),
            SellerMenuItemUiModel(
                titleRes = sellermenucommonR.string.setting_menu_complaint,
                type = MenuItemType.COMPLAIN,
                eventActionSuffix = SettingTrackingConstant.COMPLAINT,
                iconUnifyType = IconUnify.PRODUCT_INFO,
                actionClick = SellerMenuActionClick.COMPLAINTS
            )
        )
    }

    private fun createOtherInfoMenu(): List<SellerMenuComposeItem> {
        return listOf(
            SellerMenuDividerUiModel(DividerType.THICK, "5"),
            SellerMenuSectionTitleUiModel(
                titleRes = R.string.setting_menu_other_info
            ),
            SellerMenuItemUiModel(
                titleRes = sellermenucommonR.string.setting_menu_seller_education_center,
                type = MenuItemType.SELLER_EDU,
                eventActionSuffix = SettingTrackingConstant.SELLER_CENTER,
                iconUnifyType = IconUnify.SHOP_INFO,
                actionClick = SellerMenuActionClick.SELLER_EDU
            ),
            SellerMenuItemUiModel(
                titleRes = sellermenucommonR.string.setting_menu_tokopedia_care,
                type = MenuItemType.TOKOPEDIA_CARE,
                eventActionSuffix = SettingTrackingConstant.TOKOPEDIA_CARE,
                iconUnifyType = IconUnify.CALL_CENTER,
                actionClick = SellerMenuActionClick.TOKOPEDIA_CARE
            ),
            SellerMenuItemUiModel(
                titleRes = sellermenucommonR.string.setting_menu_shop_setting,
                type = MenuItemType.SHOP_SETTINGS,
                eventActionSuffix = SettingTrackingConstant.SETTINGS,
                iconUnifyType = IconUnify.SETTING,
                actionClick = SellerMenuActionClick.SETTINGS
            )
        )
    }
}
