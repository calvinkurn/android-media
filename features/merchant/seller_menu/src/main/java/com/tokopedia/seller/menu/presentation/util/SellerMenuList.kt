package com.tokopedia.seller.menu.presentation.util

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.seller.menu.R
import com.tokopedia.seller.menu.common.analytics.SettingTrackingConstant
import com.tokopedia.seller.menu.common.constant.MenuItemType
import com.tokopedia.seller.menu.common.constant.SellerBaseUrl
import com.tokopedia.seller.menu.common.view.uimodel.*
import com.tokopedia.seller.menu.common.view.uimodel.base.DividerType
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiModel
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.ShopInfoLoadingUiModel
import com.tokopedia.seller.menu.presentation.activity.SellerSettingsActivity
import com.tokopedia.seller.menu.presentation.uimodel.OrderSectionTitleUiModel
import com.tokopedia.seller.menu.presentation.uimodel.ProductSectionTitleUiModel
import com.tokopedia.user.session.UserSessionInterface

object SellerMenuList {

    private const val APPLINK_FORMAT = "%s?url=%s%s"

    fun create(context: Context, userSession: UserSessionInterface): List<SettingUiModel> {
        val menuList = mutableListOf<SettingUiModel>()
        val buyerInfoMenu = createBuyerInfoMenu(context)
        val helpAndOtherMenu = createOtherInfoMenu(context)

        menuList.add(ShopInfoLoadingUiModel)
        menuList.add(OrderSectionTitleUiModel)
        menuList.add(ShopOrderUiModel())
        menuList.add(DividerUiModel(DividerType.THIN_PARTIAL))
        menuList.add(ProductSectionTitleUiModel)
        menuList.add(ShopProductUiModel())
        menuList.add(DividerUiModel(DividerType.THIN_PARTIAL))
        menuList.addAll(buyerInfoMenu)
        menuList.addAll(helpAndOtherMenu)
        menuList.add(DividerUiModel())
        menuList.add(SellerFeatureUiModel(userSession))

        return menuList.toList()
    }

    private fun createBuyerInfoMenu(context: Context): List<SettingUiModel> {
        val sectionTitle = context.getString(R.string.setting_menu_buyer_info)
        val resolutionInboxApplink = String.format(APPLINK_FORMAT, ApplinkConst.WEBVIEW,
            SellerBaseUrl.HOSTNAME, SellerBaseUrl.RESO_INBOX_SELLER)

        return listOf(
                SettingTitleUiModel(sectionTitle),
                SellerMenuItemUiModel(
                        context.getString(R.string.setting_menu_review),
                        R.drawable.ic_star_setting,
                        ApplinkConst.REPUTATION,
                        type = MenuItemType.REVIEW,
                        eventActionSuffix = SettingTrackingConstant.REVIEW),
                SellerMenuItemUiModel(
                        context.getString(R.string.setting_menu_discussion),
                        R.drawable.ic_setting_discussion,
                        ApplinkConst.TALK,
                        type = MenuItemType.DISCUSSION,
                        eventActionSuffix = SettingTrackingConstant.DISCUSSION),
                SellerMenuItemUiModel(
                        context.getString(R.string.setting_menu_complaint),
                        R.drawable.ic_complaint,
                        null,
                        type = MenuItemType.COMPLAIN,
                        eventActionSuffix = SettingTrackingConstant.COMPLAINT) {
                    val intent = RouteManager.getIntent(context, resolutionInboxApplink)
                    context.startActivity(intent)
                }
        )
    }

    private fun createOtherInfoMenu(context: Context): List<SettingUiModel> {
        val sectionTitle = context.getString(R.string.setting_menu_other_info)
        val sellerEduApplink = String.format(APPLINK_FORMAT, ApplinkConst.WEBVIEW,
            SellerBaseUrl.SELLER_HOSTNAME, SellerBaseUrl.SELLER_EDU)

        return listOf(
                DividerUiModel(),
                SettingTitleUiModel(sectionTitle),
                SellerMenuItemUiModel(
                        context.getString(R.string.setting_menu_seller_education_center),
                        R.drawable.ic_seller_edu,
                        type = MenuItemType.SELLER_EDU,
                        eventActionSuffix = SettingTrackingConstant.SELLER_CENTER) {
                    val intent = RouteManager.getIntent(context, sellerEduApplink)
                    context.startActivity(intent)
                },
                SellerMenuItemUiModel(
                        context.getString(R.string.setting_menu_tokopedia_care),
                        R.drawable.ic_tokopedia_care,
                        ApplinkConst.CONTACT_US_NATIVE,
                        type = MenuItemType.TOKOPEDIA_CARE,
                        eventActionSuffix = SettingTrackingConstant.TOKOPEDIA_CARE),
                SellerMenuItemUiModel(
                        context.getString(R.string.setting_menu_shop_setting),
                        R.drawable.ic_pengaturan_toko,
                        null,
                        type = MenuItemType.SHOP_SETTINGS,
                        eventActionSuffix = SettingTrackingConstant.SETTINGS) {
                    context.startActivity(Intent(context, SellerSettingsActivity::class.java))
                }
        )
    }
}