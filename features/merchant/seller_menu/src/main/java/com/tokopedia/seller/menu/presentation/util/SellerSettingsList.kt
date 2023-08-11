package com.tokopedia.seller.menu.presentation.util

import android.content.Context
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
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

object SellerSettingsList {

    private const val LOGOUT_BUTTON_NAME = "Logout"
    private const val SHIPPING_SERVICE_ALIAS = "shipping service"
    private const val LOGOUT_ALIAS = "logout"
    private const val EXTRA_OPEN_SELLER_NOTIF = "extra_open_seller_notif"

    fun create(
        context: Context,
        isMultilocation: Boolean = false,
        userSession: UserSessionInterface,
        adminPermissionMapper: AdminPermissionMapper
    ): List<SettingUiModel> {
        val trackingAliasMap = trackingAliasMap(context)

        val isShopOwner = userSession.isShopOwner

        return if (isMultilocation) {
            listOf(
                SellerSettingsTitleUiModel(
                    context.getString(com.tokopedia.seller.menu.common.R.string.setting_menu_shop_profile),
                    IconUnify.SHOP_SETTING
                ),
                SellerMenuItemUiModel(
                    context.getString(com.tokopedia.seller.menu.common.R.string.setting_menu_basic_info),
                    clickApplink = ApplinkConstInternalMarketplace.SHOP_SETTINGS_INFO,
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
                    context.getString(com.tokopedia.seller.menu.common.R.string.setting_menu_shop_notes),
                    clickApplink = ApplinkConstInternalMarketplace.SHOP_SETTINGS_NOTES,
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
                    context.getString(com.tokopedia.seller.menu.common.R.string.setting_menu_shop_working_hours),
                    clickApplink = ApplinkConstInternalMarketplace.SHOP_SETTINGS_OPERATIONAL_HOURS,
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
                IndentedSettingTitleUiModel(context.getString(com.tokopedia.seller.menu.common.R.string.setting_menu_location_and_shipment)),
                SellerMenuItemUiModel(
                    context.getString(com.tokopedia.seller.menu.common.R.string.setting_menu_add_and_shop_location),
                    clickApplink = ApplinkConstInternalMarketplace.SHOP_SETTINGS_ADDRESS,
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
                    context.getString(com.tokopedia.seller.menu.common.R.string.setting_menu_set_shipment_method),
                    clickApplink = ApplinkConst.SELLER_SHIPPING_EDITOR,
                    settingTypeInfix = SettingTrackingConstant.SHOP_SETTING,
                    trackingAlias = trackingAliasMap[context.getString(com.tokopedia.seller.menu.common.R.string.setting_menu_set_shipment_method)],
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
                    context.getString(com.tokopedia.seller.menu.R.string.seller_menu_notification_setting),
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
                }
            )
        } else {
            listOf(
                SellerSettingsTitleUiModel(
                    context.getString(com.tokopedia.seller.menu.common.R.string.setting_menu_shop_profile),
                    IconUnify.SHOP_SETTING
                ),
                SellerMenuItemUiModel(
                    context.getString(com.tokopedia.seller.menu.common.R.string.setting_menu_basic_info),
                    clickApplink = ApplinkConstInternalMarketplace.SHOP_SETTINGS_INFO,
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
                    context.getString(com.tokopedia.seller.menu.common.R.string.setting_menu_shop_notes),
                    clickApplink = ApplinkConstInternalMarketplace.SHOP_SETTINGS_NOTES,
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
                    context.getString(com.tokopedia.seller.menu.common.R.string.setting_menu_shop_working_hours),
                    clickApplink = ApplinkConstInternalMarketplace.SHOP_SETTINGS_OPERATIONAL_HOURS,
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
                IndentedSettingTitleUiModel(context.getString(com.tokopedia.seller.menu.common.R.string.setting_menu_location_and_shipment)),
                SellerMenuItemUiModel(
                    context.getString(com.tokopedia.seller.menu.common.R.string.setting_menu_set_shipment_method),
                    clickApplink = ApplinkConst.SELLER_SHIPPING_EDITOR,
                    settingTypeInfix = SettingTrackingConstant.SHOP_SETTING,
                    trackingAlias = trackingAliasMap[context.getString(com.tokopedia.seller.menu.common.R.string.setting_menu_set_shipment_method)],
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
                    context.getString(com.tokopedia.seller.menu.R.string.seller_menu_notification_setting),
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
                }
            )
        }
    }

    private fun trackingAliasMap(context: Context): Map<String, String?> {
        return mapOf<String, String?>(
            context.getString(com.tokopedia.seller.menu.common.R.string.setting_menu_set_shipment_method) to SHIPPING_SERVICE_ALIAS,
            LOGOUT_BUTTON_NAME to LOGOUT_ALIAS
        )
    }

    private fun checkAccessPermissionIfNotShopOwner(context: Context?, isShopOwner: Boolean, mapper: AdminPermissionMapper, @AdminFeature feature: String) {
        if (context != null) {
            val intent =
                if (isShopOwner) {
                    mapper.mapFeatureToDestination(context, feature)
                } else {
                    RouteManager.getIntent(context, ApplinkConstInternalSellerapp.ADMIN_AUTHORIZE, feature)
                }
            context.startActivity(intent)
        }
    }
}
