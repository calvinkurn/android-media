package com.tokopedia.seller.menu.presentation.util

import android.content.Context
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.seller.menu.R
import com.tokopedia.seller.menu.common.analytics.SettingTrackingConstant
import com.tokopedia.seller.menu.common.view.uimodel.DividerUiModel
import com.tokopedia.seller.menu.common.view.uimodel.IndentedSettingTitleUiModel
import com.tokopedia.seller.menu.common.view.uimodel.MenuItemUiModel
import com.tokopedia.seller.menu.common.view.uimodel.SettingTitleMenuUiModel
import com.tokopedia.seller.menu.common.view.uimodel.base.DividerType
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiModel

object SellerSettingsList {

    private const val LOGOUT_BUTTON_NAME = "Logout"
    private const val SHIPPING_SERVICE_ALIAS = "shipping service"
    private const val LOGOUT_ALIAS = "logout"

    fun create(context: Context): List<SettingUiModel> {
        val trackingAliasMap = trackingAliasMap(context)

        return listOf(
            SettingTitleMenuUiModel(context.getString(R.string.setting_menu_shop_profile), R.drawable.ic_pengaturan_toko),
            MenuItemUiModel(
                context.getString(R.string.setting_menu_basic_info),
                clickApplink = ApplinkConstInternalMarketplace.SHOP_SETTINGS_INFO,
                settingTypeInfix = SettingTrackingConstant.SHOP_SETTING),
            MenuItemUiModel(
                context.getString(R.string.setting_menu_shop_notes),
                clickApplink = ApplinkConstInternalMarketplace.SHOP_SETTINGS_NOTES,
                settingTypeInfix = SettingTrackingConstant.SHOP_SETTING),
            MenuItemUiModel(
                context.getString(R.string.setting_menu_shop_working_hours),
                clickApplink = ApplinkConstInternalMarketplace.SHOP_EDIT_SCHEDULE,
                settingTypeInfix = SettingTrackingConstant.SHOP_SETTING),
            DividerUiModel(DividerType.THIN_INDENTED),
            IndentedSettingTitleUiModel(context.getString(R.string.setting_menu_location_and_shipment)),
            MenuItemUiModel(
                context.getString(R.string.setting_menu_add_and_shop_location),
                clickApplink =  ApplinkConstInternalMarketplace.SHOP_SETTINGS_ADDRESS,
                settingTypeInfix = SettingTrackingConstant.SHOP_SETTING),
            MenuItemUiModel(
                context.getString(R.string.setting_menu_set_shipment_method),
                clickApplink = ApplinkConst.SELLER_SHIPPING_EDITOR,
                settingTypeInfix = SettingTrackingConstant.SHOP_SETTING,
                trackingAlias = trackingAliasMap[context.getString(R.string.setting_menu_set_shipment_method)]),
            DividerUiModel(DividerType.THICK),
            MenuItemUiModel(
                context.getString(R.string.seller_menu_notification_setting),
                R.drawable.ic_app_setting,
                clickApplink = ApplinkConstInternalMarketplace.USER_NOTIFICATION_SETTING,
                eventActionSuffix = SettingTrackingConstant.SETTINGS)
        )
    }

    private fun trackingAliasMap(context: Context): Map<String, String?> {
        return mapOf<String, String?>(
            context.getString(R.string.setting_menu_set_shipment_method) to SHIPPING_SERVICE_ALIAS,
            LOGOUT_BUTTON_NAME to LOGOUT_ALIAS
        )
    }
}