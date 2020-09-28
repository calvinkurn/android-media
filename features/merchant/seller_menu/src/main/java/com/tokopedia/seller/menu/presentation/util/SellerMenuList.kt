package com.tokopedia.seller.menu.presentation.util

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.seller.menu.common.R
import com.tokopedia.seller.menu.common.analytics.SettingTrackingConstant
import com.tokopedia.seller.menu.common.constant.MenuItemType
import com.tokopedia.seller.menu.common.constant.SellerBaseUrl
import com.tokopedia.seller.menu.common.view.uimodel.*
import com.tokopedia.seller.menu.common.view.uimodel.SectionTitleUiModel.SectionTitleType.*
import com.tokopedia.seller.menu.common.view.uimodel.base.DividerType
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiModel
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.ShopInfoLoadingUiModel
import com.tokopedia.seller.menu.presentation.activity.SellerSettingsActivity
import com.tokopedia.seller.menu.presentation.uimodel.OrderSectionTitleUiModel
import com.tokopedia.seller.menu.presentation.uimodel.ProductSectionTitleUiModel
import com.tokopedia.user.session.UserSessionInterface

object SellerMenuList {

    private const val APPLINK_FORMAT = "%s?url=%s%s"
    private const val GO_TO_BUYER_REVIEW = "GO_TO_BUYER_REVIEW"
    private const val GO_TO_MY_PRODUCT = "GO_TO_MY_PRODUCT"

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
                SettingTitleUiModel(sectionTitle, R.dimen.spacing_lvl4),
                SellerMenuItemUiModel(
                        context.getString(R.string.setting_menu_review),
                        R.drawable.ic_star_setting,
                        type = MenuItemType.REVIEW,
                        eventActionSuffix = SettingTrackingConstant.REVIEW) {
                    val intent = RouteManager.getIntent(context, ApplinkConst.REPUTATION)
                    intent.putExtra(GO_TO_BUYER_REVIEW, true)
                    context.startActivity(intent)
                },
                SellerMenuItemUiModel(
                        context.getString(R.string.setting_menu_discussion),
                        R.drawable.ic_setting_discussion,
                        type = MenuItemType.DISCUSSION,
                        eventActionSuffix = SettingTrackingConstant.DISCUSSION) {
                    val intent = RouteManager.getIntent(context, ApplinkConst.TALK)
                    intent.putExtra(GO_TO_MY_PRODUCT, true)
                    context.startActivity(intent)
                },
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
        val sectionTitle = SectionTitleUiModel(title = R.string.setting_menu_other_info, type = OTHER_SECTION_TITLE)
        val sellerEduApplink = String.format(APPLINK_FORMAT, ApplinkConst.WEBVIEW,
            SellerBaseUrl.SELLER_HOSTNAME, SellerBaseUrl.SELLER_EDU)

        return listOf(
                DividerUiModel(),
                sectionTitle,
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
                        R.drawable.ic_icon_tokopedia_care,
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