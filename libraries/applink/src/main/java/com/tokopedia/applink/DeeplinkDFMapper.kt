package com.tokopedia.applink

import android.content.Context
import android.net.Uri
import android.os.Build
import com.crashlytics.android.Crashlytics
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.tokopedia.applink.ApplinkConst.*
import com.tokopedia.applink.internal.ApplinkConsInternalDigital.TELCO_DIGITAL
import com.tokopedia.applink.internal.ApplinkConsInternalDigital.VOUCHER_GAME
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.AGE_RESTRICTION
import com.tokopedia.applink.internal.ApplinkConstInternalContent.INTERNAL_AFFILIATE
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery.AUTOCOMPLETE
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery.IMAGE_SEARCH_RESULT
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery.SEARCH_RESULT
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery.SIMILAR_SEARCH_RESULT_BASE
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.CHANGE_PHONE_NUMBER
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.CHAT_BOT
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.DYNAMIC_FEATURE_INSTALL
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.DYNAMIC_FEATURE_INSTALL_BASE
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.GLOBAL_INTERNAL_DIGITAL_DEAL
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.GLOBAL_INTERNAL_DIGITAL_DEAL_ALL_BRANDS
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.GLOBAL_INTERNAL_DIGITAL_DEAL_BRAND_DETAIL
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.GLOBAL_INTERNAL_DIGITAL_DEAL_CATEGORY
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.GLOBAL_INTERNAL_DIGITAL_DEAL_SLUG
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.GLOBAL_INTERNAL_INSTANT_LOAN
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.GLOBAL_INTERNAL_INSTANT_LOAN_TAB
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.GLOBAL_INTERNAL_PINJAMAN_ONLINE_TAB
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.SALDO_DEPOSIT
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.SALDO_INTRO
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.SETTING_BANK
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.SETTING_PROFILE
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.OPEN_SHOP
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.REPORT_PRODUCT
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.SHOP_SETTINGS_BASE
import com.tokopedia.applink.internal.ApplinkConstInternalPlay.GROUPCHAT_DETAIL
import com.tokopedia.applink.internal.ApplinkConstInternalPlay.GROUPCHAT_LIST
import com.tokopedia.applink.internal.ApplinkConstInternalPromo.INTERNAL_TOKOPOINTS
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds.TOPADS_AUTOADS
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds.TOPADS_DASHBOARD_CUSTOMER
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds.TOPADS_DASHBOARD_INTERNAL
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds.TOPADS_DASHBOARD_SELLER
import com.tokopedia.applink.internal.ApplinkConstInternalTravel.INTERNAL_FLIGHT
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.FINAL_PRICE
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.MONEYIN_INTERNAL
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.TRADEIN
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.OVO_PAY_WITH_QR_ENTRY
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.OQR_PIN_URL_ENTRY
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.USER_NOTIFICATION_SETTING
import com.tokopedia.applink.internal.ApplinkConstInternalPayment.PAYMENT_SETTING
import com.tokopedia.config.GlobalConfig
import tokopedia.applink.R
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader

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
    private val DFM_GROUPCHAT = "groupchat"
    private val DFM_SIMILAR_SEARCH = "similarsearch"
    private val DFM_IMAGE_SEARCH = "image_search"
    private val DFM_AUTOCOMPLETE = "autocomplete"
    private val DFM_SEARCH_RESULT = "search"
    private val DFM_HOMEPAGE_DIGITAL = "homepage_digital"
    private val DFM_SHOP_SETTINGS_SELLERAPP = "shop_settings_sellerapp"
    val DFM_SHOP_SETTINGS_CUSTOMERAPP = "shop_settings"
    private val DFM_SHOP_OPEN_CUSTOMERAPP = "shop_open"
    private val DFM_HOTEL_TRAVEL = "hotel_travel"
    private val DFM_FLIGHT_TRAVEL = "flight_travel"
    private val DFM_DIGITAL_TOPUP = "digital_topup"
    private val DFM_VOUCHER_GAME = "voucher_game"
    private val DFM_DIGITAL_DEALS = "digital_deals"
    private val DFM_USER_PROFILE_COMPLETION = "profilecompletion"
    private val DFM_USER_CHANGE_PHONE_NUMBER = "changephonenumber"
    private val DFM_USER_SETTING_BANK = "settingbank"
    private val DFM_HOMEPAGE_TRAVEL = "homepage_travel"
    private val DFM_CUSTOMER_TOPADS_DASHBOARD = "customer_topads_dashboard"
    private val DFM_SELLER_TOPADS_DASHBOARD = "seller_topads_dashboard"
    private val DFM_CUSTOMER_TOPADS_AUTOADS = "customer_topads_autoads"
    private val DFM_SELLER_TOPADS_AUTOADS = "seller_topads_autoads"
    private val DFM_CUSTOMER_REPORT_PRODUCT = "customer_report_product"
    private val DFM_SELLER_REPORT_PRODUCT = "seller_report_product"
    private val DFM_AGE_RESTRICTION = "age_restriction"
    private val DFM_TOKOPOINTS = "tokopoints"
    private var DFM_MODULE_FINTECH_INSTANT_LOAN = "instantloan"
    private val DFM_CONTACT_US = "contact_us"
    private val DFM_SALDO_DEPOSIT = "saldo_deposit"
    private val DFM_OVOP2P = "ovop2p"
    private val DFM_SALDO_INTRO = "saldo_deposit"
    private val DFM_OVO_PAY_WITH_QR = "df_ovo_pay_with_qr"
    private val DFM_REFERRAL = "im_referral"
    private val DFM_TRADEIN = "tradein"
    private val DFM_TRADEIN_FINAL_PRICE = "tradein"
    private val DFM_MONEYIN = "tradein"
    private val DFM_WALLET = "fintech_wallet"
    private val DFM_PROFILE = "profile"
    private val DFM_AFFILIATE = "affiliate"
    private val DFM_CHAT_BOT = "chatbot"
    private val DFM_POWER_MERCHANT_SUBSCRIBE = "power_merchant_subscribe"
    private val DFM_SETTING_NOTIF = "settingnotif"
    private val DFM_PAYMENT_SETTING = "payment_setting"
    private val DFM_PAYMENT_SETTING_SELLERAPP = "payment_setting_sellerapp"

    @JvmField
    val DFM_ONBOARDING = "onboarding"

    private var manager: SplitInstallManager? = null
    private val deeplinkDFPatternListCustomerApp: List<DFP> by lazy {
        mutableListOf<DFP>().apply {
            add(DFP({ it.startsWith(SHOP_SETTINGS_BASE) }, DFM_SHOP_SETTINGS_CUSTOMERAPP, R.string.shop_settings_title))
            add(DFP({ it.startsWith(IMAGE_SEARCH_RESULT) }, DFM_IMAGE_SEARCH, R.string.title_image_search))
            add(DFP({ it.startsWith(DIGITAL_SUBHOMEPAGE) }, DFM_HOMEPAGE_DIGITAL, R.string.title_digital_subhomepage))
            add(DFP({ it.startsWith(HOTEL) }, DFM_HOTEL_TRAVEL, R.string.title_hotel))
            add(DFP({ it.startsWith(GROUPCHAT_LIST) }, DFM_GROUPCHAT, R.string.title_groupchat))
            add(DFP({ it.startsWith(GROUPCHAT_DETAIL) }, DFM_GROUPCHAT, R.string.title_groupchat))
            add(DFP({ it.startsWith(FLIGHT) }, DFM_FLIGHT_TRAVEL, R.string.title_flight))
            add(DFP({ it.startsWith(INTERNAL_FLIGHT) }, DFM_FLIGHT_TRAVEL, R.string.title_flight))
            add(DFP({ it.startsWith(TRAVEL_SUBHOMEPAGE) }, DFM_HOMEPAGE_TRAVEL, R.string.title_travel_homepage))
            add(DFP({ it.startsWith(TELCO_DIGITAL) }, DFM_DIGITAL_TOPUP, R.string.digital_topup_title))
            add(DFP({ it.startsWith(VOUCHER_GAME) }, DFM_VOUCHER_GAME, R.string.title_voucher_game))
            add(DFP({ it.startsWith(GLOBAL_INTERNAL_DIGITAL_DEAL) }, DFM_DIGITAL_DEALS, R.string.title_digital_deals))
            add(DFP({ it.startsWithPattern(GLOBAL_INTERNAL_DIGITAL_DEAL_SLUG) }, DFM_DIGITAL_DEALS, R.string.title_digital_deals))
            add(DFP({ it.startsWith(GLOBAL_INTERNAL_DIGITAL_DEAL_CATEGORY) }, DFM_DIGITAL_DEALS, R.string.title_digital_deals))
            add(DFP({ it.startsWith(GLOBAL_INTERNAL_DIGITAL_DEAL_ALL_BRANDS) }, DFM_DIGITAL_DEALS, R.string.title_digital_deals))
            add(DFP({ it.startsWith(GLOBAL_INTERNAL_DIGITAL_DEAL_BRAND_DETAIL) }, DFM_DIGITAL_DEALS, R.string.title_digital_deals))
            add(DFP({ it.startsWith(OPEN_SHOP) }, DFM_SHOP_OPEN_CUSTOMERAPP, R.string.title_open_shop))
            add(DFP({ it.startsWith(SETTING_PROFILE) }, DFM_USER_PROFILE_COMPLETION, R.string.applink_profile_completion_title))
            add(DFP({ it.startsWith(CHANGE_PHONE_NUMBER) }, DFM_USER_CHANGE_PHONE_NUMBER, R.string.applink_change_phone_number))
            add(DFP({ it.startsWith(SETTING_BANK) }, DFM_USER_SETTING_BANK, R.string.applink_setting_bank_title))
            add(DFP({ it.startsWithPattern(REPORT_PRODUCT) }, DFM_CUSTOMER_REPORT_PRODUCT, R.string.applink_report_title))
            add(DFP({ it.startsWith(TOPADS_DASHBOARD_CUSTOMER) || it.startsWith(TOPADS_DASHBOARD_INTERNAL) }, DFM_CUSTOMER_TOPADS_DASHBOARD, R.string.applink_topads_dashboard_title))
            add(DFP({ it.startsWith(TOPADS_AUTOADS) }, DFM_CUSTOMER_TOPADS_AUTOADS, R.string.applink_topads_dashboard_title))
            add(DFP({ it.startsWith(SIMILAR_SEARCH_RESULT_BASE) }, DFM_SIMILAR_SEARCH, R.string.title_similar_search))
            add(DFP({ it.startsWith(AUTOCOMPLETE) }, DFM_AUTOCOMPLETE, R.string.title_autocomplete))
            add(DFP({ it.startsWith(SEARCH_RESULT) }, DFM_SEARCH_RESULT, R.string.title_search_result))
            add(DFP({ it.startsWith(AGE_RESTRICTION) }, DFM_AGE_RESTRICTION, R.string.applink_title_age_restriction))
            add(DFP({it.startsWith(INTERNAL_TOKOPOINTS)},DFM_TOKOPOINTS,R.string.title_tokopoints))
            add(DFP({it.startsWith(SALDO_DEPOSIT)}, DFM_SALDO_DEPOSIT, R.string.applink_saldo_deposit_title))
            add(DFP({it.startsWith(SALDO_INTRO)}, DFM_SALDO_INTRO, R.string.applink_saldo_intro_title))
            add(DFP({ it.startsWith(OVOP2PTRANSFERFORM_SHORT) }, DFM_OVOP2P, R.string.title_ovop2p))
            add(DFP({ it.startsWith(SALDO_DEPOSIT) }, DFM_SALDO_DEPOSIT, R.string.applink_saldo_deposit_title))
            add(DFP({ it.startsWith(SALDO_INTRO) }, DFM_SALDO_INTRO, R.string.applink_saldo_intro_title))
            add(DFP({ it.startsWith(GLOBAL_INTERNAL_INSTANT_LOAN) }, DFM_MODULE_FINTECH_INSTANT_LOAN, R.string.instant_loan_title))
            add(DFP({ it.startsWith(GLOBAL_INTERNAL_INSTANT_LOAN_TAB)
                    || it.startsWith(GLOBAL_INTERNAL_PINJAMAN_ONLINE_TAB) }, DFM_MODULE_FINTECH_INSTANT_LOAN, R.string.instant_loan_title))
            add(DFP({ it.startsWith(REFERRAL) }, DFM_REFERRAL, R.string.applink_title_im_referral))
            add(DFP({it.startsWith(OVO_WALLET)}, DFM_WALLET, R.string.applink_wallet_title))
            add(DFP({ it.startsWith(CONTACT_US_NATIVE) || it.startsWith(CONTACT_US) || it.startsWithPattern(TICKET_DETAIL) }, DFM_CONTACT_US, R.string.applink_title_contact_us))
            add(DFP({ it.startsWith(TRADEIN) }, DFM_TRADEIN, R.string.applink_title_tradein))
            add(DFP({ it.startsWith(FINAL_PRICE) }, DFM_TRADEIN_FINAL_PRICE, R.string.applink_harga_final))
            add(DFP({ it.startsWith(MONEYIN_INTERNAL) }, DFM_MONEYIN, R.string.money_in))
            add(DFP({ it.startsWithPattern(PROFILE) }, DFM_PROFILE, R.string.applink_title_profile))
            add(DFP({ it.startsWithPattern(INTERNAL_AFFILIATE) }, DFM_AFFILIATE, R.string.applink_title_affiliate))
            add(DFP({it.startsWith(CHAT_BOT)}, DFM_CHAT_BOT, R.string.title_applink_chatbot))
            add(DFP({ it.startsWith(POWER_MERCHANT_SUBSCRIBE) }, DFM_POWER_MERCHANT_SUBSCRIBE, R.string.title_applink_pm_subscribe))
            add(DFP({it.startsWith(OVO_PAY_WITH_QR_ENTRY)}, DFM_OVO_PAY_WITH_QR, R.string.ovo_pay_with_qr_title))
            add(DFP({it.startsWith(OQR_PIN_URL_ENTRY)}, DFM_OVO_PAY_WITH_QR, R.string.ovo_pay_with_qr_title))

            add(DFP({ it.startsWith(PAYMENT_SETTING) }, DFM_PAYMENT_SETTING, R.string.payment_settings_title))
            add(DFP({ it.startsWith(USER_NOTIFICATION_SETTING) }, DFM_SETTING_NOTIF, R.string.notif_settings_title))
        }
    }

    private val deeplinkDFPatternListSellerApp: List<DFP> by lazy {
        mutableListOf<DFP>().apply {
            add(DFP({ it.startsWith(SHOP_SETTINGS_BASE) }, DFM_SHOP_SETTINGS_SELLERAPP, R.string.shop_settings_title))
            add(DFP({ it.startsWith(PAYMENT_SETTING) }, DFM_PAYMENT_SETTING_SELLERAPP, R.string.payment_settings_title))
            add(DFP({ it.startsWith(TOPADS_DASHBOARD_SELLER) || it.startsWith(TOPADS_DASHBOARD_INTERNAL) }, DFM_SELLER_TOPADS_DASHBOARD, R.string.applink_topads_dashboard_title))
            add(DFP({ it.startsWith(TOPADS_AUTOADS) }, DFM_CUSTOMER_TOPADS_AUTOADS, R.string.applink_topads_dashboard_title))
            add(DFP({ it.startsWith(REPORT_PRODUCT) }, DFM_SELLER_REPORT_PRODUCT, R.string.applink_report_title))
        }
    }

    /**
     * Deeplink Pattern List for customerapp defined by config in dynamic-feature-customerapp.cfg
     * If the cfg consist only 2 lines (not commented) this pattern will consist also 2 lines.
     */
    private var turnedOnDeeplinkDFPatternCustomerapp: List<DFP>? = null

    /**
     * Deeplink Pattern List for customerapp defined by config in dynamic-feature-sellerapp.cfg
     * If the cfg consist only 2 lines (not commented) this pattern will consist also 2 lines.
     */
    private var turnedOnDeeplinkDFPatternSellerapp: List<DFP>? = null

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
            if (turnedOnDeeplinkDFPatternSellerapp == null) {
                turnedOnDeeplinkDFPatternSellerapp = getTurnOnDeeplinkDFPattern(context, deeplinkDFPatternListSellerApp)
            }
            executeDeeplinkPattern(context, deeplink, turnedOnDeeplinkDFPatternSellerapp)
        } else {
            if (turnedOnDeeplinkDFPatternCustomerapp == null) {
                turnedOnDeeplinkDFPatternCustomerapp = getTurnOnDeeplinkDFPattern(context, deeplinkDFPatternListCustomerApp)
            }
            executeDeeplinkPattern(context, deeplink, turnedOnDeeplinkDFPatternCustomerapp)
        }
    }

    private fun executeDeeplinkPattern(context: Context,
                                       deeplink: String,
                                       list: List<DFP>?): String? {
        list?.forEach {
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

    /**
     * Supplied with list of DFP, it will return the list of turned on DFP by the config
     * Example:
     * Input:   original DFP list = {DFP(shop), DFP(hotel), DFP(flight)}
     *          cfg -> setOf(hotel)
     * Output:  DFP list {DFP(hotel)}
     */
    private fun getTurnOnDeeplinkDFPattern(context: Context, dfpList: List<DFP>?): List<DFP>? {
        val turnedOnDfSet = getDFFilterMap(context)
        if (turnedOnDfSet == null || turnedOnDfSet.isEmpty()) {
            return listOf()
        }
        val resultList = mutableListOf<DFP>()
        dfpList?.forEach {
            if (turnedOnDfSet.contains(it.moduleId)) {
                resultList.add(it)
            }
        }
        return resultList
    }

    /**
     * return the set from cfg file. Set will be distinct
     * @return Set of dynamic features that turned on by the config "dynamic-feature-<app>.cfg"
     * Example return: setOf{shop_settings, hotel}
     */
    private fun getDFFilterMap(context: Context): Set<String>? {
        try {
            val set: HashSet<String> = hashSetOf()
            val reader = BufferedReader(InputStreamReader(context.assets.open("df.cfg")))
            var line: String? = reader.readLine()
            while (line != null) {
                if (line.isNotEmpty()) {
                    set.add(line)
                }
                line = reader.readLine()
            }
            return set
        } catch (e: FileNotFoundException) {
            return null
        }
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

fun String.startsWithPattern(prefix: String): Boolean {
    return startsWith(prefix.substringBefore("{")) || startsWith(prefix.substringBefore("?"))
}