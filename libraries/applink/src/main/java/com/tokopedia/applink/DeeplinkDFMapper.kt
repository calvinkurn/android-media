package com.tokopedia.applink

import android.content.Context
import android.net.Uri
import android.os.Build
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.tokopedia.applink.ApplinkConst.*
import com.tokopedia.applink.internal.ApplinkConsInternalHome.HOME_WISHLIST
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.AGE_RESTRICTION
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.FINAL_PRICE
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.MONEYIN_INTERNAL
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.TRADEIN
import com.tokopedia.applink.internal.ApplinkConstInternalContent.INTERNAL_AFFILIATE
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery.AUTOCOMPLETE
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery.SEARCH_RESULT
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery.SIMILAR_SEARCH_RESULT_BASE
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.ADD_TALK
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
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.INBOX_TALK
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.OQR_PIN_URL_ENTRY
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.OVO_PAY_WITH_QR_ENTRY
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.SALDO_DEPOSIT
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.SALDO_INTRO
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.SETTING_BANK
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.SETTING_PROFILE
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.USER_IDENTIFICATION_FORM
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.ATTACH_INVOICE
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.ATTACH_VOUCHER
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.ONBOARDING
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.OPEN_SHOP
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.REPORT_PRODUCT
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.SHOP_SETTINGS_BASE
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.USER_NOTIFICATION_SETTING
import com.tokopedia.applink.internal.ApplinkConstInternalNotification.NOTIFICATION
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.INTERNAL_SELLER
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.OPPORTUNITY
import com.tokopedia.applink.internal.ApplinkConstInternalPayment.PAYMENT_SETTING
import com.tokopedia.applink.internal.ApplinkConstInternalPlay.GROUPCHAT_DETAIL
import com.tokopedia.applink.internal.ApplinkConstInternalPlay.GROUPCHAT_LIST
import com.tokopedia.applink.internal.ApplinkConstInternalPromo.INTERNAL_TOKOPOINTS
import com.tokopedia.applink.internal.ApplinkConstInternalSalam.SALAM_ORDER_DETAIL
import com.tokopedia.applink.internal.ApplinkConstInternalSalam.SALAM_UMRAH_HOME_PAGE
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds.TOPADS_DASHBOARD_CUSTOMER
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds.TOPADS_DASHBOARD_INTERNAL
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds.TOPADS_DASHBOARD_SELLER
import com.tokopedia.applink.internal.ApplinkConstInternalTravel.INTERNAL_FLIGHT
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.CHANGE_PASSWORD
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.DETAIL_TALK_BASE
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.LIVENESS_DETECTION
import com.tokopedia.applink.internal.ApplinkConstInternalNotification.NOTIFICATION_BUYER
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

    @JvmField
    val DFM_BASE = "df_base"
    @JvmField
    val DFM_ONBOARDING = DFM_BASE // "df_base_onboarding"
    private val DFM_CATEGORY_TRADEIN = "df_category_tradein"
    @JvmField
    val DFM_MERCHANT_SELLER_CUSTOMERAPP = "df_merchant_seller"

    @JvmField
    val DFM_FACE_DETECTION = "df_user_liveness"

    //sellerapp
    private val DFM_PRODUCT_MANAGE_SELLER = "product_manage_seller"
    private val DFM_USER_IDENTIFICATION_COMMON = "user_identification_common"
    private val DFM_OPPORTUNITY = "opportunity"
    private val DFM_SHOP_SETTINGS_SELLERAPP = "shop_settings_sellerapp"
    private val DFM_SELLER_TOPADS_DASHBOARD = "seller_topads_dashboard"

    private var manager: SplitInstallManager? = null
    private val deeplinkDFPatternListCustomerApp: List<DFP> by lazy {
        mutableListOf<DFP>().apply {
            // Base
            add(DFP({ it.startsWith(ONBOARDING) }, DFM_BASE, R.string.applink_title_affiliate))
            // Category
            add(DFP({ it.startsWith(TRADEIN) }, DFM_BASE, R.string.applink_title_tradein))
            add(DFP({ it.startsWith(FINAL_PRICE) }, DFM_BASE, R.string.applink_harga_final))
            add(DFP({ it.startsWith(MONEYIN_INTERNAL) }, DFM_BASE, R.string.money_in))

            add(DFP({ it.startsWith(AGE_RESTRICTION) }, DFM_BASE, R.string.applink_title_age_restriction))

            // Content
            add(DFP({ it.startsWithPattern(PROFILE) }, DFM_BASE, R.string.applink_title_profile))
            add(DFP({ it.startsWithPattern(INTERNAL_AFFILIATE) }, DFM_BASE, R.string.applink_title_affiliate))

            // Digital
            add(DFP({ it.startsWith(DIGITAL_RECHARGE) || it.startsWith(DIGITAL) }, DFM_BASE, R.string.title_digital_subhomepage))

            add(DFP({ it.startsWith(GLOBAL_INTERNAL_DIGITAL_DEAL) }, DFM_BASE, R.string.title_digital_deals))
            add(DFP({ it.startsWithPattern(GLOBAL_INTERNAL_DIGITAL_DEAL_SLUG) }, DFM_BASE, R.string.title_digital_deals))
            add(DFP({ it.startsWith(GLOBAL_INTERNAL_DIGITAL_DEAL_CATEGORY) }, DFM_BASE, R.string.title_digital_deals))
            add(DFP({ it.startsWith(GLOBAL_INTERNAL_DIGITAL_DEAL_ALL_BRANDS) }, DFM_BASE, R.string.title_digital_deals))
            add(DFP({ it.startsWith(GLOBAL_INTERNAL_DIGITAL_DEAL_BRAND_DETAIL) }, DFM_BASE, R.string.title_digital_deals))

            // Discovery
            add(DFP({ it.startsWith(SIMILAR_SEARCH_RESULT_BASE) }, DFM_BASE, R.string.title_similar_search))
            add(DFP({
                it.startsWith(SEARCH_RESULT) ||
                    it.startsWith(AUTOCOMPLETE)
            }, DFM_BASE, R.string.title_search_result))

            // Fintech
            add(DFP({ it.startsWith(OVO_PAY_WITH_QR_ENTRY) }, DFM_BASE, R.string.ovo_pay_with_qr_title))
            add(DFP({ it.startsWith(OQR_PIN_URL_ENTRY) }, DFM_BASE, R.string.ovo_pay_with_qr_title))
            add(DFP({ it.startsWith(OVO_WALLET) }, DFM_BASE, R.string.applink_wallet_title))
            add(DFP({ it.startsWith(SALDO_DEPOSIT) }, DFM_BASE, R.string.applink_saldo_deposit_title))
            add(DFP({ it.startsWith(SALDO_INTRO) }, DFM_BASE, R.string.applink_saldo_intro_title))
            add(DFP({ it.startsWith(OVOP2PTRANSFERFORM_SHORT) }, DFM_BASE, R.string.title_ovop2p))
            add(DFP({ it.startsWith(GLOBAL_INTERNAL_INSTANT_LOAN) }, DFM_BASE, R.string.instant_loan_title))
            add(DFP({
                it.startsWith(GLOBAL_INTERNAL_INSTANT_LOAN_TAB) ||
                    it.startsWith(GLOBAL_INTERNAL_PINJAMAN_ONLINE_TAB)
            }, DFM_BASE, R.string.instant_loan_title))

            // IM
            add(DFP({ it.startsWith(REFERRAL) }, DFM_BASE, R.string.applink_title_im_referral))

            // Merchant
            add(DFP({ it.startsWithPattern(REPORT_PRODUCT) }, DFM_BASE, R.string.applink_report_title))
            add(DFP({ it.startsWith(OPEN_SHOP) }, DFM_BASE, R.string.title_open_shop))

            add(DFP({ it.startsWith(INTERNAL_SELLER) }, DFM_MERCHANT_SELLER_CUSTOMERAPP,
                R.string.merchant_seller,
                DFWebviewFallbackUrl.SELLER_ORDER))
            add(DFP({ it.startsWith(PRODUCT_MANAGE_LIST) }, DFM_MERCHANT_SELLER_CUSTOMERAPP,
                R.string.merchant_seller,
                DFWebviewFallbackUrl.MANAGE_PRODUCT))
            add(DFP({ it.startsWith(POWER_MERCHANT_SUBSCRIBE) }, DFM_MERCHANT_SELLER_CUSTOMERAPP,
                R.string.merchant_seller,
                DFWebviewFallbackUrl.POWER_MERCHANT))
            add(DFP({ it.startsWith(SHOP_SETTINGS_BASE) }, DFM_MERCHANT_SELLER_CUSTOMERAPP,
                R.string.merchant_seller,
                DFWebviewFallbackUrl.SHOP_SETTINGS))
            add(DFP({
                it.startsWith(TOPADS_DASHBOARD_CUSTOMER) ||
                    it.startsWith(TOPADS_DASHBOARD_INTERNAL)
            }, DFM_MERCHANT_SELLER_CUSTOMERAPP,
                R.string.merchant_seller,
                DFWebviewFallbackUrl.TOP_ADS_DASHBOARD))
            add(DFP({ it.startsWith(OPPORTUNITY) }, DFM_MERCHANT_SELLER_CUSTOMERAPP,
                R.string.merchant_seller,
                DFWebviewFallbackUrl.OPPORTUNITY))
            add(DFP({ it.startsWith(SELLER_TRANSACTION) }, DFM_MERCHANT_SELLER_CUSTOMERAPP,
                R.string.merchant_seller,
                DFWebviewFallbackUrl.SELLER_ORDER))

            // Operational
            add(DFP({
                it.startsWith(CONTACT_US_NATIVE) ||
                    it.startsWith(CONTACT_US) ||
                    it.startsWithPattern(TICKET_DETAIL)
            }, DFM_BASE, R.string.applink_title_contact_us))
            add(DFP({ it.startsWith(CHAT_BOT) }, DFM_BASE, R.string.title_applink_chatbot))

            // Payment
            add(DFP({ it.startsWith(PAYMENT_SETTING) }, DFM_BASE, R.string.payment_settings_title))

            // Promo
            add(DFP({ it.startsWith(INTERNAL_TOKOPOINTS) }, DFM_BASE, R.string.title_tokopoints))

            // Travel
            add(DFP({ it.startsWith(TRAVEL_SUBHOMEPAGE) }, DFM_BASE, R.string.title_travel_homepage))
            add(DFP({ it.startsWith(FLIGHT) }, DFM_BASE, R.string.title_flight))
            add(DFP({ it.startsWith(INTERNAL_FLIGHT) }, DFM_BASE, R.string.title_flight))
            add(DFP({ it.startsWith(HOTEL) }, DFM_BASE, R.string.title_hotel))

            // User
            add(DFP({ it.startsWith(GROUPCHAT_LIST) }, DFM_BASE, R.string.title_groupchat))
            add(DFP({ it.startsWith(GROUPCHAT_DETAIL) }, DFM_BASE, R.string.title_groupchat))
            add(DFP({ it.startsWith(SETTING_PROFILE) }, DFM_BASE, R.string.applink_profile_completion_title))
            add(DFP({ it.startsWith(CHANGE_PHONE_NUMBER) }, DFM_BASE, R.string.applink_change_phone_number))
            add(DFP({ it.startsWith(CHANGE_PASSWORD) }, DFM_BASE, R.string.applink_change_password))
            add(DFP({ it.startsWith(SETTING_BANK) }, DFM_BASE, R.string.applink_setting_bank_title))
            add(DFP({ it.startsWith(USER_NOTIFICATION_SETTING) }, DFM_BASE, R.string.notif_settings_title))
            add(DFP({ it.startsWith(USER_IDENTIFICATION_FORM) }, DFM_BASE, R.string.user_identification_common_title))
            add(DFP({ it.startsWith(ATTACH_INVOICE) }, DFM_BASE, R.string.title_module_attachinvoice))
            add(DFP({ it.startsWith(ATTACH_VOUCHER) }, DFM_BASE, R.string.title_module_attachvoucher))
            add(DFP({
                it.startsWith(TOPCHAT_IDLESS) ||
                    it.startsWith(ApplinkConstInternalGlobal.TOPCHAT)
            }, DFM_BASE, R.string.title_topchat))

            add(DFP({ it.startsWith(INBOX_TALK) }, DFM_BASE, R.string.talk_title))
            add(DFP({ it.startsWith(SHOP_TALK) }, DFM_BASE, R.string.talk_title))
            add(DFP({ it.startsWith(PRODUCT_TALK) }, DFM_BASE, R.string.talk_title))
            add(DFP({ it.startsWith(DETAIL_TALK_BASE) }, DFM_BASE, R.string.talk_title))
            add(DFP({ it.startsWith(ADD_TALK) }, DFM_BASE, R.string.talk_title))

            add(DFP({ it.startsWith(ADD_FINGERPRINT_ONBOARDING) }, DFM_BASE, R.string.fingerprint_onboarding))

            add(DFP({ it.startsWith(SALAM_UMRAH_HOME_PAGE) }, DFM_BASE, R.string.title_salam))
            add(DFP({ it.startsWith(SALAM_ORDER_DETAIL) }, DFM_BASE, R.string.title_salam))
            add(DFP({ it.startsWith(NOTIFICATION) }, DFM_BASE, R.string.title_notification_center))
            add(DFP({ it.startsWith(NOTIFICATION_BUYER) }, DFM_BASE, R.string.title_notification_center))

            add(DFP({ it.startsWith(HOME_WISHLIST) }, DFM_BASE, R.string.title_wishlist))

            add(DFP({ it.startsWith(LIVENESS_DETECTION) }, DFM_FACE_DETECTION, R.string.applink_liveness_detection))
        }
    }

    private val deeplinkDFPatternListSellerApp: List<DFP> by lazy {
        mutableListOf<DFP>().apply {
            add(DFP({ it.startsWith(SHOP_SETTINGS_BASE) }, DFM_SHOP_SETTINGS_SELLERAPP, R.string.shop_settings_title, DFWebviewFallbackUrl.SHOP_SETTINGS))
            add(DFP({
                it.startsWith(TOPADS_DASHBOARD_SELLER) ||
                    it.startsWith(TOPADS_DASHBOARD_INTERNAL)
            }, DFM_SELLER_TOPADS_DASHBOARD, R.string.applink_topads_dashboard_title, DFWebviewFallbackUrl.TOP_ADS_DASHBOARD))
            add(DFP({ it.startsWith(PRODUCT_MANAGE_LIST) }, DFM_PRODUCT_MANAGE_SELLER, R.string.title_applink_product_manage,
                DFWebviewFallbackUrl.MANAGE_PRODUCT))
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
                    deeplink, it.moduleId, context.getString(it.moduleNameResourceId),true,
                    "", it.webviewFallback)
            }
        }
        return null
    }

    private fun getDFDeeplinkIfNotInstalled(context: Context, deeplink: String,
                                            moduleId: String, moduleName: String,
                                            isAuto: Boolean? = true,
                                            imageUrl: String = "",
                                            fallbackUrl: String = ""): String? {
        getSplitManager(context)?.let {
            if (it.installedModules.contains(moduleId)) {
                return null
            } else {
                return UriUtil.buildUri(
                    DYNAMIC_FEATURE_INSTALL,
                    moduleId,
                    moduleName,
                    Uri.encode(deeplink).toString(),
                    isAuto.toString(),
                    imageUrl,
                    fallbackUrl)
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
    val moduleNameResourceId: Int,
    val webviewFallback: String = ""
)

fun String.startsWithPattern(prefix: String): Boolean {
    return startsWith(prefix.substringBefore("{")) || startsWith(prefix.substringBefore("?"))
}