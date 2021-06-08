package com.tokopedia.seller.menu.presentation.util

import android.content.Context
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.seller.menu.common.R
import com.tokopedia.seller.menu.common.analytics.SettingTrackingConstant
import com.tokopedia.seller.menu.common.constant.AdminFeature
import com.tokopedia.seller.menu.common.constant.MenuItemType
import com.tokopedia.seller.menu.common.constant.SellerBaseUrl
import com.tokopedia.seller.menu.common.view.uimodel.*
import com.tokopedia.seller.menu.common.view.uimodel.SectionTitleUiModel.SectionTitleType.OTHER_SECTION_TITLE
import com.tokopedia.seller.menu.common.view.uimodel.base.DividerType
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiModel
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.ShopInfoLoadingUiModel
import com.tokopedia.seller.menu.presentation.activity.AdminRoleAuthorizeActivity
import com.tokopedia.seller.menu.presentation.uimodel.OrderSectionTitleUiModel
import com.tokopedia.seller.menu.presentation.uimodel.ProductSectionTitleUiModel
import com.tokopedia.user.session.UserSessionInterface

object SellerMenuList {

    private const val APPLINK_FORMAT = "%s?url=%s%s"

    fun create(context: Context,
               userSession: UserSessionInterface,
               mapper: AdminPermissionMapper): List<SettingUiModel> {
        val isShopOwner = userSession.isShopOwner
        val menuList = mutableListOf<SettingUiModel>()
        val buyerInfoMenu = createBuyerInfoMenu(context, isShopOwner, mapper)
        val helpAndOtherMenu = createOtherInfoMenu(context, isShopOwner, mapper)

        menuList.add(ShopInfoLoadingUiModel)
        menuList.add(OrderSectionTitleUiModel(
                context.getString(com.tokopedia.seller.menu.R.string.seller_menu_order_section),
                context.getString(com.tokopedia.seller.menu.R.string.seller_menu_order_cta),
                isShopOwner
        ))
        menuList.add(ShopOrderUiModel(isShopOwner = isShopOwner))
        menuList.add(DividerUiModel(DividerType.THIN_PARTIAL))
        menuList.add(ProductSectionTitleUiModel(
                context.getString(com.tokopedia.seller.menu.R.string.seller_menu_product_section),
                context.getString(com.tokopedia.seller.menu.R.string.seller_menu_product_cta),
                isShopOwner
        ))
        menuList.add(ShopProductUiModel(isShopOwner = isShopOwner))
        menuList.add(DividerUiModel(DividerType.THIN_PARTIAL))
        menuList.addAll(buyerInfoMenu)
        menuList.addAll(helpAndOtherMenu)
        menuList.add(DividerUiModel())
        menuList.add(SellerFeatureUiModel(userSession))

        return menuList.toList()
    }

    private fun createBuyerInfoMenu(context: Context, isShopOwner: Boolean, mapper: AdminPermissionMapper): List<SettingUiModel> {
        val sectionTitle = context.getString(R.string.setting_menu_buyer_info)

        return listOf(
                SettingTitleUiModel(sectionTitle, R.dimen.spacing_lvl4),
                SellerMenuItemUiModel(
                        context.getString(R.string.setting_menu_review),
                        R.drawable.ic_star_setting,
                        type = MenuItemType.REVIEW,
                        eventActionSuffix = SettingTrackingConstant.REVIEW) {
                    checkAccessPermissionIfNotShopOwner(context, isShopOwner, mapper, AdminFeature.REVIEW)
                },
                SellerMenuItemUiModel(
                        context.getString(R.string.setting_menu_discussion),
                        R.drawable.ic_setting_discussion,
                        type = MenuItemType.DISCUSSION,
                        eventActionSuffix = SettingTrackingConstant.DISCUSSION) {
                    checkAccessPermissionIfNotShopOwner(context, isShopOwner, mapper, AdminFeature.DISCUSSION)
                },
                SellerMenuItemUiModel(
                        context.getString(R.string.setting_menu_complaint),
                        R.drawable.ic_complaint,
                        null,
                        type = MenuItemType.COMPLAIN,
                        eventActionSuffix = SettingTrackingConstant.COMPLAINT) {
                    checkAccessPermissionIfNotShopOwner(context, isShopOwner, mapper, AdminFeature.COMPLAINT)
                }
        )
    }

    private fun createOtherInfoMenu(context: Context, isShopOwner: Boolean, mapper: AdminPermissionMapper): List<SettingUiModel> {
        val titleText = context.getString(R.string.setting_menu_other_info)
        val sectionTitle = SectionTitleUiModel(title = titleText, type = OTHER_SECTION_TITLE)
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
                    checkAccessPermissionIfNotShopOwner(context, isShopOwner, mapper, AdminFeature.MANAGE_SHOP)
                }
        )
    }

    private fun checkAccessPermissionIfNotShopOwner(context: Context, isShopOwner: Boolean, mapper: AdminPermissionMapper, @AdminFeature feature: String) {
        val intent =
                if (isShopOwner) {
                    mapper.mapFeatureToDestination(context, feature)
                } else {
                    AdminRoleAuthorizeActivity.createIntent(context, feature)
                }
        context.startActivity(intent)
    }

}