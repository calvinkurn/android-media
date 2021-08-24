package com.tokopedia.sellerhome.settings.view.adapter

import android.content.Context
import android.content.Intent
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.seller.menu.common.analytics.SettingTrackingConstant
import com.tokopedia.seller.menu.common.constant.SellerBaseUrl
import com.tokopedia.seller.menu.common.view.typefactory.OtherMenuAdapterTypeFactory
import com.tokopedia.seller.menu.common.view.uimodel.*
import com.tokopedia.seller.menu.common.view.uimodel.base.DividerType
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiModel
import com.tokopedia.sellerhome.R

class OtherMenuAdapter(private val context: Context?,
                       private val listener: Listener,
                       typeFactory: OtherMenuAdapterTypeFactory):
    BaseListAdapter<SettingUiModel, OtherMenuAdapterTypeFactory>(typeFactory) {

    companion object {
        private const val APPLINK_FORMAT = "%s?url=%s"
    }

    private val settingList = listOf(
        SettingTitleUiModel(context?.getString(R.string.setting_menu_improve_sales).orEmpty()),
        StatisticMenuItemUiModel(
            title = context?.getString(R.string.setting_menu_statistic).orEmpty(),
            clickApplink = ApplinkConstInternalMechant.MERCHANT_STATISTIC_DASHBOARD,
            iconUnify = IconUnify.GRAPH),
        MenuItemUiModel(
            title = context?.getString(R.string.setting_menu_ads_and_shop_promotion).orEmpty(),
            clickApplink = ApplinkConstInternalSellerapp.CENTRALIZED_PROMO,
            eventActionSuffix = SettingTrackingConstant.SHOP_ADS_AND_PROMOTION,
            iconUnify = IconUnify.PROMO_ADS),
        MenuItemUiModel(
            title = context?.getString(R.string.setting_menu_performance).orEmpty(),
            clickApplink = ApplinkConstInternalMarketplace.SHOP_PERFORMANCE,
            eventActionSuffix = SettingTrackingConstant.SHOP_PERFORMANCE,
            iconUnify = IconUnify.PERFORMANCE,
        ),
        SettingTitleUiModel(context?.getString(R.string.setting_menu_buyer_info).orEmpty()),
        MenuItemUiModel(
            title = context?.getString(R.string.setting_menu_discussion).orEmpty(),
            clickApplink = ApplinkConst.TALK,
            eventActionSuffix = SettingTrackingConstant.DISCUSSION,
            iconUnify = IconUnify.DISCUSSION),
        MenuItemUiModel(
            title = context?.getString(R.string.setting_menu_review).orEmpty(),
            clickApplink = ApplinkConst.REPUTATION,
            eventActionSuffix = SettingTrackingConstant.REVIEW,
            iconUnify = IconUnify.STAR),
        MenuItemUiModel(
            title = context?.getString(R.string.setting_menu_complaint).orEmpty(),
            clickApplink = null,
            eventActionSuffix = SettingTrackingConstant.COMPLAINT,
            iconUnify = IconUnify.PRODUCT_INFO
        ) {
            val applink = String.format(APPLINK_FORMAT, ApplinkConst.WEBVIEW, "${SellerBaseUrl.HOSTNAME}${SellerBaseUrl.RESO_INBOX_SELLER}")
            val intent = RouteManager.getIntent(context, applink)
            context?.startActivity(intent)
        },
        DividerUiModel(),
        PrintingMenuItemUiModel(
            title = context?.getString(R.string.setting_menu_product_package).orEmpty(),
            iconUnify = IconUnify.PACKAGE
        ) {
            listener.goToPrintingPage()
        },
        MenuItemUiModel(
            title = context?.getString(R.string.setting_menu_finance_service).orEmpty(),
            clickApplink = null,
            eventActionSuffix = SettingTrackingConstant.FINANCIAL_SERVICE,
            iconUnify = IconUnify.FINANCE
        ) {
            RouteManager.route(context, ApplinkConst.LAYANAN_FINANSIAL)
        },
        MenuItemUiModel(
            title = context?.getString(R.string.setting_menu_seller_education_center).orEmpty(),
            clickApplink = null,
            eventActionSuffix = SettingTrackingConstant.SELLER_CENTER,
            iconUnify = IconUnify.SHOP_INFO
        ) {
            val applink = String.format(APPLINK_FORMAT, ApplinkConst.WEBVIEW, "${SellerBaseUrl.SELLER_HOSTNAME}${SellerBaseUrl.SELLER_EDU}")
            val intent = RouteManager.getIntent(context, applink)
            context?.startActivity(intent)
        },
        MenuItemUiModel(
            title = context?.getString(R.string.setting_menu_tokopedia_care).orEmpty(),
            clickApplink = ApplinkConst.CONTACT_US_NATIVE,
            eventActionSuffix = SettingTrackingConstant.TOKOPEDIA_CARE,
            iconUnify = IconUnify.CALL_CENTER),
        DividerUiModel(DividerType.THIN_PARTIAL),
        MenuItemUiModel(
            title = context?.getString(R.string.setting_menu_setting).orEmpty(),
            clickApplink = null,
            eventActionSuffix = SettingTrackingConstant.SETTINGS,
            iconUnify = IconUnify.SETTING) {
            listener.goToSettings()
        }
    )

    fun populateAdapterData() {
        clearAllElements()
        addElement(settingList)
    }

    interface Listener {
        fun goToPrintingPage()
        fun goToSettings()
    }

}