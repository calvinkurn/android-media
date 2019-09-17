package com.tokopedia.applink

import android.content.Context
import android.net.Uri
import android.os.Build
import com.crashlytics.android.Crashlytics
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.DYNAMIC_FEATURE_INSTALL
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.DYNAMIC_FEATURE_INSTALL_BASE
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.SETTING_BANK
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.SETTING_PROFILE
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.REPORT_PRODUCT
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.SHOP_SETTINGS_BASE
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds.TOPADS_DASHBOARD_CUSTOMER
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds.TOPADS_DASHBOARD_INTERNAL
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds.TOPADS_DASHBOARD_SELLER
import com.tokopedia.config.GlobalConfig
import tokopedia.applink.R

/**
 * Dynamic Feature Deeplink Mapper
 *
 * If
 * 1) the deeplink is in the dynamic feature module and
 * 2) the module is not installed int the app yet,
 * this will map the deeplink to [install dynamic feature] deeplink
 *
 * Example tokopedia-android-internal://marketplace/shop-settings-info
 *
 * will map to
 *
 * tokopedia-android-internal://global/dynamic-feature-install/shop_settings_sellerapp?
 * applink=[tokopedia-android-internal://marketplace/shop-settings-info]&auto=true&name=Pengaturan Toko&image=
 *
 */
object DeeplinkDFMapper {
    // it should have the same name with the folder of dynamic feature
    private val DFM_SHOP_SETTINGS_SELLERAPP = "shop_settings_sellerapp"
    private val DFM_SHOP_SETTINGS_CUSTOMERAPP = "shop_settings"
    private val DFM_SHOP_OPEN_CUSTOMERAPP = "shop_open"
    private val DFM_HOTEL_TRAVEL = "hotel_travel"
    private val DFM_DIGITAL_TOPUP = "digital_topup"
    private val DFM_USER_PROFILE_COMPLETION = "profilecompletion"
    private val DFM_USER_SETTING_BANK = "settingbank"
    private val DFM_HOMEPAGE_TRAVEL = "homepage_travel"
    private val DFM_CUSTOMER_TOPADS_DASHBOARD = "customer_topads_dashboard"
    private val DFM_SELLER_TOPADS_DASHBOARD = "seller_topads_dashboard"
    private val DFM_CUSTOMER_TOPADS_AUTOADS = "customer_topads_autoads"
    private val DFM_SELLER_TOPADS_AUTOADS = "seller_topads_autoads"
    private val DFM_CUSTOMER_REPORT_PRODUCT = "customer_report_product"
    private val DFM_SELLER_REPORT_PRODUCT = "seller_report_product"


    private var manager: SplitInstallManager? = null
    private val deeplinkDFPatternListCustomerApp: List<DFP> by lazy {
        mutableListOf<DFP>().apply {
//            add(DFP({ it.startsWith(HOTEL) }, DFM_HOTEL_TRAVEL, R.string.title_hotel))
//            add(DFP({ it.startsWith(TRAVEL_SUBHOMEPAGE) }, DFM_HOMEPAGE_TRAVEL, R.string.title_travel_homepage))
//            add(DFP({ it.startsWith(TELCO_DIGITAL) }, DFM_DIGITAL_TOPUP, R.string.digital_topup_title))
//            add(DFP({ it.startsWith(OPEN_SHOP) }, DFM_SHOP_OPEN_CUSTOMERAPP, R.string.title_open_shop))
            add(DFP({ it.startsWith(SHOP_SETTINGS_BASE) }, DFM_SHOP_SETTINGS_CUSTOMERAPP, R.string.shop_settings_title))
            add(DFP({ it.startsWith(SETTING_PROFILE) }, DFM_USER_PROFILE_COMPLETION, R.string.applink_profile_completion_title))
            add(DFP({ it.startsWith(SETTING_BANK) }, DFM_USER_SETTING_BANK, R.string.applink_setting_bank_title))
//            add(DFP({ it.startsWith(REPORT_PRODUCT) }, DFM_CUSTOMER_REPORT_PRODUCT, R.string.applink_report_title))
//            add(DFP({ it.startsWith(TOPADS_DASHBOARD_CUSTOMER) || it.startsWith(TOPADS_DASHBOARD_INTERNAL) }, DFM_CUSTOMER_TOPADS_DASHBOARD, R.string.applink_topads_dashboard_title))
//            add(DFP({ it.startsWith(DFM_CUSTOMER_TOPADS_AUTOADS) }, DFM_CUSTOMER_TOPADS_AUTOADS, R.string.applink_topads_dashboard_title))
        }
    }

    private val deeplinkDFPatternListSellerApp: List<DFP> by lazy {
        mutableListOf<DFP>().apply {
            add(DFP({ it.startsWith(SHOP_SETTINGS_BASE) }, DFM_SHOP_SETTINGS_SELLERAPP, R.string.shop_settings_title))
//            add(DFP({ it.startsWith(TOPADS_DASHBOARD_SELLER) || it.startsWith(TOPADS_DASHBOARD_INTERNAL) }, DFM_SELLER_TOPADS_DASHBOARD, R.string.applink_topads_dashboard_title))
//            add(DFP({ it.startsWith(DFM_SELLER_TOPADS_AUTOADS) }, DFM_CUSTOMER_TOPADS_AUTOADS, R.string.applink_topads_dashboard_title))
//            add(DFP({ it.startsWith(REPORT_PRODUCT) }, DFM_SELLER_REPORT_PRODUCT, R.string.applink_report_title))
        }
    }

    /**
     * map the original deeplink to [Dynamic Feature Install] Deeplink
     * (only if the deeplink is dynamic Feature deeplink and if the module is not installed yet)
     */
    @JvmStatic
    fun getDFDeeplinkIfNotInstalled(context: Context, deeplink: String): String? {
        //KITKAT does not support dynamic feature
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            return null
        }
        if (deeplink.startsWith(DYNAMIC_FEATURE_INSTALL_BASE)) {
            return null
        }
        return if (GlobalConfig.isSellerApp()) {
            executeDeeplinkPattern(context, deeplink, deeplinkDFPatternListSellerApp)
        } else {
            executeDeeplinkPattern(context, deeplink, deeplinkDFPatternListCustomerApp)
        }
    }

    private fun executeDeeplinkPattern(context: Context,
                                       deeplink: String,
                                       list: List<DFP>): String? {
        list.forEach {
            if (it.logic(deeplink)) {
                return getDFDeeplinkIfNotInstalled(context,
                        deeplink, it.moduleId, context.getString(it.moduleNameResourceId))
            }
        }
        return null
    }

    private fun getDFDeeplinkIfNotInstalled(context: Context, deeplink: String,
                                            moduleId: String, moduleName: String,
                                            isAuto: Boolean? = true,
                                            imageUrl: String? = ""): String? {
        getSplitManager(context)?.let {
            if (it.installedModules.contains(moduleId)) {
                if (!GlobalConfig.DEBUG) {
                    Crashlytics.logException(Exception("Open module " + moduleId));
                }
                return null
            } else {
                if (!GlobalConfig.DEBUG) {
                    Crashlytics.logException(Exception("Install module " + moduleId));
                }
                return UriUtil.buildUri(
                        DYNAMIC_FEATURE_INSTALL,
                        moduleId,
                        moduleName,
                        Uri.encode(deeplink).toString(),
                        isAuto.toString(),
                        imageUrl)
            }
        } ?: return null
    }

    private fun getSplitManager(context: Context): SplitInstallManager? {
        if (manager === null) {
            manager = SplitInstallManagerFactory.create(context.applicationContext)
        }
        return manager
    }

}

/**
 * Class to hold dynamic feature pattern, used for mapping
 */
class DFP(
        val logic: ((deeplink: String) -> Boolean),
        val moduleId: String,
        val moduleNameResourceId: Int
)