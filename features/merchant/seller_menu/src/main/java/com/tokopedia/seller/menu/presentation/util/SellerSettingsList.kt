package com.tokopedia.seller.menu.presentation.util

import android.content.Context
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.seller.menu.common.analytics.SettingTrackingConstant
import com.tokopedia.seller.menu.common.constant.MenuItemType
import com.tokopedia.seller.menu.common.view.uimodel.DividerUiModel
import com.tokopedia.seller.menu.common.view.uimodel.IndentedSettingTitleUiModel
import com.tokopedia.seller.menu.common.view.uimodel.SellerMenuItemUiModel
import com.tokopedia.seller.menu.common.view.uimodel.SellerSettingsTitleUiModel
import com.tokopedia.seller.menu.common.view.uimodel.base.DividerType
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiModel
import com.tokopedia.shopadmin.common.util.AdminFeature
import com.tokopedia.shopadmin.common.util.AdminPermissionMapper
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.seller.menu.R as sellermenuR
import com.tokopedia.seller.menu.common.R as sellermenucommonR

object SellerSettingsList {

    private const val LOGOUT_BUTTON_NAME = "Logout"
    private const val SHIPPING_SERVICE_ALIAS = "shipping service"
    private const val LOGOUT_ALIAS = "logout"
    private const val EXTRA_OPEN_SELLER_NOTIF = "extra_open_seller_notif"

    fun create(
        context: Context,
        userSession: UserSessionInterface,
        adminPermissionMapper: AdminPermissionMapper
    ): List<SettingUiModel> {
        val trackingAliasMap = trackingAliasMap(context)

        val isShopOwner = userSession.isShopOwner

        return listOf(
            SellerSettingsTitleUiModel(
                context.getString(sellermenucommonR.string.setting_menu_shop_profile),
                IconUnify.SHOP_SETTING
            ),
            SellerMenuItemUiModel(
                context.getString(sellermenucommonR.string.setting_menu_basic_info),
                clickApplink = null,
                settingTypeInfix = SettingTrackingConstant.SHOP_SETTING,
                type = MenuItemType.BASIC_INFO,
                clickAction = {
                    checkAccessPermissionIfNotShopOwner(
                        context,
                        isShopOwner,
                        adminPermissionMapper,
                        AdminFeature.SHOP_SETTINGS_INFO
                    )
                }
            ),
            SellerMenuItemUiModel(
                context.getString(sellermenucommonR.string.setting_menu_shop_notes),
                clickApplink = null,
                settingTypeInfix = SettingTrackingConstant.SHOP_SETTING,
                type = MenuItemType.NOTES,
                clickAction = {
                    checkAccessPermissionIfNotShopOwner(
                        context,
                        isShopOwner,
                        adminPermissionMapper,
                        AdminFeature.SHOP_SETTINGS_NOTES
                    )
                }
            ),
            SellerMenuItemUiModel(
                context.getString(sellermenucommonR.string.setting_menu_shop_working_hours),
                clickApplink = null,
                settingTypeInfix = SettingTrackingConstant.SHOP_SETTING,
                type = MenuItemType.SCHEDULE,
                clickAction = {
                    checkAccessPermissionIfNotShopOwner(
                        context,
                        isShopOwner,
                        adminPermissionMapper,
                        AdminFeature.SHOP_OPERATIONAL_HOURS
                    )
                }
            ),
            DividerUiModel(DividerType.THIN_INDENTED),
            IndentedSettingTitleUiModel(context.getString(sellermenucommonR.string.setting_menu_location_and_shipment)),
            SellerMenuItemUiModel(
                context.getString(sellermenucommonR.string.setting_menu_add_and_shop_location),
                clickApplink = null,
                settingTypeInfix = SettingTrackingConstant.SHOP_SETTING,
                type = MenuItemType.LOCATION,
                clickAction = {
                    checkAccessPermissionIfNotShopOwner(
                        context,
                        isShopOwner,
                        adminPermissionMapper,
                        AdminFeature.SHOP_SETTING_ADDR
                    )
                }
            ),
            SellerMenuItemUiModel(
                context.getString(sellermenucommonR.string.setting_menu_set_shipment_method),
                clickApplink = null,
                settingTypeInfix = SettingTrackingConstant.SHOP_SETTING,
                trackingAlias = trackingAliasMap[context.getString(sellermenucommonR.string.setting_menu_set_shipment_method)],
                type = MenuItemType.SHIPPING,
                clickAction = {
                    checkAccessPermissionIfNotShopOwner(
                        context,
                        isShopOwner,
                        adminPermissionMapper,
                        AdminFeature.SHIPPING_EDITOR
                    )
                }
            ),
            DividerUiModel(DividerType.THICK),
            SellerMenuItemUiModel(
                context.getString(sellermenuR.string.seller_menu_notification_setting),
                eventActionSuffix = SettingTrackingConstant.SETTINGS,
                type = MenuItemType.NOTIFICATION,
                iconUnify = IconUnify.PHONE_SETTING
            ) {
                val intent = RouteManager.getIntent(
                    context,
                    ApplinkConstInternalMarketplace.USER_NOTIFICATION_SETTING
                )
                intent.putExtra(EXTRA_OPEN_SELLER_NOTIF, true)
                context.startActivity(intent)
            },
            DividerUiModel(DividerType.THICK),
            IndentedSettingTitleUiModel(context.getString(sellermenucommonR.string.seller_content_settings_header)),
            SellerMenuItemUiModel(
                context.getString(sellermenucommonR.string.seller_content_settings_body),
                eventActionSuffix = SettingTrackingConstant.SETTINGS,
                type = MenuItemType.CONTENT,
            ) {
               RouteManager.route(context, ApplinkConst.CONTENT_SETTINGS)
            },

        )
    }

    private fun trackingAliasMap(context: Context): Map<String, String?> {
        return mapOf<String, String?>(
            context.getString(sellermenucommonR.string.setting_menu_set_shipment_method) to SHIPPING_SERVICE_ALIAS,
            LOGOUT_BUTTON_NAME to LOGOUT_ALIAS
        )
    }

    private fun checkAccessPermissionIfNotShopOwner(
        context: Context?,
        isShopOwner: Boolean,
        mapper: AdminPermissionMapper,
        @AdminFeature feature: String
    ) {
        if (context != null) {
            val intent =
                if (isShopOwner) {
                    mapper.mapFeatureToDestination(context, feature)
                } else {
                    RouteManager.getIntent(
                        context,
                        ApplinkConstInternalSellerapp.ADMIN_AUTHORIZE,
                        feature
                    )
                }
            context.startActivity(intent)
        }
    }
}
