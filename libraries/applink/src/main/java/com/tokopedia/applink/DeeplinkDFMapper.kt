package com.tokopedia.applink

import android.content.Context
import android.net.Uri
import android.os.Build
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.tokopedia.applink.ApplinkConst.*
import com.tokopedia.applink.internal.ApplinkConsInternalDigital.CAMERA_OCR
import com.tokopedia.applink.internal.ApplinkConsInternalDigital.CART_DIGITAL
import com.tokopedia.applink.internal.ApplinkConsInternalDigital.DIGITAL_PRODUCT_FORM
import com.tokopedia.applink.internal.ApplinkConsInternalDigital.GENERAL_TEMPLATE
import com.tokopedia.applink.internal.ApplinkConsInternalDigital.INTERNAL_SMARTCARD
import com.tokopedia.applink.internal.ApplinkConsInternalDigital.TELCO_DIGITAL
import com.tokopedia.applink.internal.ApplinkConsInternalDigital.VOUCHER_GAME
import com.tokopedia.applink.internal.ApplinkConsInternalHome.HOME_WISHLIST
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.AGE_RESTRICTION
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.FINAL_PRICE
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.MONEYIN_INTERNAL
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.TRADEIN
import com.tokopedia.applink.internal.ApplinkConstInternalContent.INTERNAL_AFFILIATE
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery.AUTOCOMPLETE
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery.SEARCH_RESULT
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery.SIMILAR_SEARCH_RESULT_BASE
import com.tokopedia.applink.internal.ApplinkConstInternalEntertainment.EVENT_HOME
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.ADD_BOD
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.ADD_EMAIL
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.ADD_NAME_REGISTER
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.ADD_PHONE
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.ADD_PIN
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.ADD_PIN_COMPLETE
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.ADD_PIN_ONBOARDING
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.ADD_TALK
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.CHANGE_GENDER
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.CHANGE_NAME
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.CHANGE_PASSWORD
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.CHANGE_PHONE_NUMBER
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.CHANGE_PIN
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.CHAT_BOT
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.DETAIL_TALK_BASE
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
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.LIVENESS_DETECTION
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.OQR_PIN_URL_ENTRY
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.OVO_PAY_WITH_QR_ENTRY
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.SALDO_DEPOSIT
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.SALDO_INTRO
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.SETTING_BANK
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.SETTING_PROFILE
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.USER_IDENTIFICATION_FORM
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.SHIPPING_CONFIRMATION
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.ATTACH_INVOICE
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.ATTACH_VOUCHER
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.ONBOARDING
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.OPEN_SHOP
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.REPORT_PRODUCT
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.SHOP_SETTINGS_BASE
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.USER_NOTIFICATION_SETTING
import com.tokopedia.applink.internal.ApplinkConstInternalNotification.NOTIFICATION
import com.tokopedia.applink.internal.ApplinkConstInternalNotification.NOTIFICATION_BUYER
import com.tokopedia.applink.internal.ApplinkConstInternalOperational.INTERNAL_INBOX_LIST
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
import com.tokopedia.config.GlobalConfig
import tokopedia.applink.R
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.net.URLDecoder

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
    private const val DF_BASE = "df_base"
    private const val DF_CATEGORY_TRADE_IN = "df_category_trade_in"
    const val DF_MERCHANT_SELLER = "df_merchant_seller"
    const val DF_OPERATIONAL_CONTACT_US = "df_operational_contact_us"
    const val DF_SALAM_UMRAH = "df_salam_umrah"
    private const val DF_USER_LIVENESS = "df_user_liveness"
    const val DF_USER_SETTINGS = "df_user_settings"

    private var manager: SplitInstallManager? = null
    private val deeplinkDFPatternListCustomerApp: List<DFP> by lazy {
        mutableListOf<DFP>().apply {
            // Base
            add(DFP({ it.startsWith(ONBOARDING) }, DF_BASE, R.string.applink_title_affiliate))
            // Category
            add(DFP({ it.startsWith(TRADEIN) }, DF_CATEGORY_TRADE_IN, R.string.applink_title_tradein))
            add(DFP({ it.startsWith(FINAL_PRICE) }, DF_CATEGORY_TRADE_IN, R.string.applink_harga_final))
            add(DFP({ it.startsWith(MONEYIN_INTERNAL) }, DF_CATEGORY_TRADE_IN, R.string.money_in))

            add(DFP({ it.startsWith(AGE_RESTRICTION) }, DF_BASE, R.string.applink_title_age_restriction))

            // Content
            add(DFP({ it.startsWithPattern(PROFILE) }, DF_BASE, R.string.applink_title_profile))
            add(DFP({ it.startsWithPattern(INTERNAL_AFFILIATE) }, DF_BASE, R.string.applink_title_affiliate))
            add(DFP({ it.startsWithPattern(PLAY_DETAIL) }, DF_BASE, R.string.applink_title_play))

            // Digital
            add(DFP({
                it.startsWith(DIGITAL_SUBHOMEPAGE_HOME) ||
                    it.startsWith(TELCO_DIGITAL) ||
                    it.startsWith(DIGITAL_PRODUCT_FORM) ||
                    it.startsWith(GENERAL_TEMPLATE) ||
                    it.startsWith(CAMERA_OCR) ||
                    it.startsWith(VOUCHER_GAME) ||
                    it.startsWith(CART_DIGITAL) || it.startsWith(DIGITAL_CART)
            }, DF_BASE, R.string.title_digital_subhomepage))
            add(DFP({ it.startsWithPattern(INTERNAL_SMARTCARD) }, DF_BASE, R.string.title_digital_emoney))
            add(DFP({ it.startsWith(GLOBAL_INTERNAL_DIGITAL_DEAL) }, DF_BASE, R.string.title_digital_deals))
            add(DFP({ it.startsWithPattern(GLOBAL_INTERNAL_DIGITAL_DEAL_SLUG) }, DF_BASE, R.string.title_digital_deals))
            add(DFP({ it.startsWith(GLOBAL_INTERNAL_DIGITAL_DEAL_CATEGORY) }, DF_BASE, R.string.title_digital_deals))
            add(DFP({ it.startsWith(GLOBAL_INTERNAL_DIGITAL_DEAL_ALL_BRANDS) }, DF_BASE, R.string.title_digital_deals))
            add(DFP({ it.startsWith(GLOBAL_INTERNAL_DIGITAL_DEAL_BRAND_DETAIL) }, DF_BASE, R.string.title_digital_deals))

            // Discovery
            add(DFP({ it.startsWith(SIMILAR_SEARCH_RESULT_BASE) }, DF_BASE, R.string.title_similar_search))
            add(DFP({ it.startsWith(SEARCH_RESULT) || it.startsWith(AUTOCOMPLETE) }, DF_BASE, R.string.title_search_result))
            add(DFP({ it.startsWith(HOME_WISHLIST) }, DF_BASE, R.string.title_wishlist))

            // Fintech
            add(DFP({ it.startsWith(OVO_PAY_WITH_QR_ENTRY) }, DF_BASE, R.string.ovo_pay_with_qr_title))
            add(DFP({ it.startsWith(OQR_PIN_URL_ENTRY) }, DF_BASE, R.string.ovo_pay_with_qr_title))
            add(DFP({ it.startsWith(OVO_WALLET) }, DF_BASE, R.string.applink_wallet_title))
            add(DFP({ it.startsWith(SALDO_DEPOSIT) }, DF_BASE, R.string.applink_saldo_deposit_title))
            add(DFP({ it.startsWith(SALDO_INTRO) }, DF_BASE, R.string.applink_saldo_intro_title))
            add(DFP({ it.startsWith(OVOP2PTRANSFERFORM_SHORT) }, DF_BASE, R.string.title_ovop2p))
            add(DFP({ it.startsWith(GLOBAL_INTERNAL_INSTANT_LOAN) }, DF_BASE, R.string.instant_loan_title))
            add(DFP({
                it.startsWith(GLOBAL_INTERNAL_INSTANT_LOAN_TAB) || it.startsWith(GLOBAL_INTERNAL_PINJAMAN_ONLINE_TAB)
            }, DF_BASE, R.string.instant_loan_title))

            // IM
            add(DFP({ it.startsWith(REFERRAL) }, DF_BASE, R.string.applink_title_im_referral))

            // Merchant
            add(DFP({ it.startsWith(OPEN_SHOP) }, DF_BASE, R.string.title_open_shop))

            add(DFP({ it.startsWith(INTERNAL_SELLER) }, DF_MERCHANT_SELLER, R.string.merchant_seller, { DFWebviewFallbackUrl.SELLER_ORDER }))
            add(DFP({ it.startsWith(PRODUCT_MANAGE_LIST) }, DF_MERCHANT_SELLER, R.string.merchant_seller, { DFWebviewFallbackUrl.MANAGE_PRODUCT }))
            add(DFP({ it.startsWith(POWER_MERCHANT_SUBSCRIBE) }, DF_MERCHANT_SELLER, R.string.merchant_seller, { DFWebviewFallbackUrl.POWER_MERCHANT }))
            add(DFP({ it.startsWith(SHOP_SETTINGS_BASE) }, DF_MERCHANT_SELLER, R.string.merchant_seller, { DFWebviewFallbackUrl.SHOP_SETTINGS }))
            add(DFP({
                it.startsWith(TOPADS_DASHBOARD_CUSTOMER) || it.startsWith(TOPADS_DASHBOARD_INTERNAL)
            }, DF_MERCHANT_SELLER, R.string.merchant_seller, { DFWebviewFallbackUrl.TOP_ADS_DASHBOARD }))
            add(DFP({ it.startsWith(OPPORTUNITY) }, DF_MERCHANT_SELLER, R.string.merchant_seller, { DFWebviewFallbackUrl.OPPORTUNITY }))
            add(DFP({ it.startsWith(SELLER_TRANSACTION) }, DF_MERCHANT_SELLER, R.string.merchant_seller, { DFWebviewFallbackUrl.SELLER_ORDER }))

            // Operational
            add(DFP({
                it.startsWith(CONTACT_US_NATIVE) || it.startsWith(CONTACT_US) || it.startsWithPattern(TICKET_DETAIL) ||
                    it.startsWith(INTERNAL_INBOX_LIST)
            }, DF_OPERATIONAL_CONTACT_US, R.string.applink_title_contact_us, { DFWebviewFallbackUrl.OPERATIONAL_CONTACT_US }))
            add(DFP({ it.startsWith(CHAT_BOT) }, DF_OPERATIONAL_CONTACT_US, R.string.title_applink_chatbot, { DFWebviewFallbackUrl.OPERATIONAL_CHAT_BOT }))

            // Payment
            add(DFP({ it.startsWith(PAYMENT_SETTING) }, DF_BASE, R.string.payment_settings_title))

            // Promo
            add(DFP({ it.startsWith(INTERNAL_TOKOPOINTS) }, DF_BASE, R.string.title_tokopoints))

            //Entertainment
            add(DFP({ it.startsWith(EVENT_HOME) }, DF_BASE, R.string.title_home_event))
            // Salam
            add(DFP({ it.startsWith(SALAM_UMRAH_HOME_PAGE) }, DF_SALAM_UMRAH, R.string.title_salam, { DFWebviewFallbackUrl.SALAM_UMRAH }))
            add(DFP({ it.startsWith(SALAM_ORDER_DETAIL) }, DF_SALAM_UMRAH, R.string.title_salam, { DFWebviewFallbackUrl.SALAM_UMRAH }))

            // Travel
            add(DFP({ it.startsWith(TRAVEL_SUBHOMEPAGE) }, DF_BASE, R.string.title_travel_homepage))
            add(DFP({ it.startsWith(FLIGHT) }, DF_BASE, R.string.title_flight))
            add(DFP({ it.startsWith(INTERNAL_FLIGHT) }, DF_BASE, R.string.title_flight))
            add(DFP({ it.startsWith(HOTEL) }, DF_BASE, R.string.title_hotel))

            // User
            add(DFP({ it.startsWith(GROUPCHAT_LIST) }, DF_BASE, R.string.title_groupchat))
            add(DFP({ it.startsWith(GROUPCHAT_DETAIL) }, DF_BASE, R.string.title_groupchat))

            add(DFP({ (it.startsWith(SETTING_PROFILE)
                    || it.startsWith(ADD_PHONE)
                    || it.startsWith(ADD_EMAIL)
                    || it.startsWith(ADD_BOD)
                    || it.startsWith(CHANGE_NAME)
                    || it.startsWith(CHANGE_GENDER)
                    || it.startsWith(ADD_NAME_REGISTER)
                    || it.startsWith(CHANGE_PIN)
                    || it.startsWith(ADD_PIN_ONBOARDING)
                    || it.startsWith(ADD_PIN)
                    || it.startsWith(ADD_PIN_COMPLETE)
                    )}, DF_USER_SETTINGS, R.string.applink_profile_completion_title, { DFWebviewFallbackUrl.USER_PROFILE_SETTINGS }))
            add(DFP({ it.startsWithPattern(REPORT_PRODUCT) }, DF_USER_SETTINGS, R.string.applink_report_title, ::getDefaultFallbackUrl))
            add(DFP({ it.startsWith(CHANGE_PHONE_NUMBER) }, DF_BASE, R.string.applink_change_phone_number))
            add(DFP({ it.startsWith(CHANGE_PASSWORD) }, DF_BASE, R.string.applink_change_password))
            add(DFP({ it.startsWith(SETTING_BANK) }, DF_BASE, R.string.applink_setting_bank_title))
            add(DFP({ it.startsWith(USER_NOTIFICATION_SETTING) }, DF_BASE, R.string.notif_settings_title))
            add(DFP({ it.startsWith(USER_IDENTIFICATION_FORM) }, DF_BASE, R.string.user_identification_common_title))
            add(DFP({ it.startsWith(ATTACH_INVOICE) }, DF_BASE, R.string.title_module_attachinvoice))
            add(DFP({ it.startsWith(ATTACH_VOUCHER) }, DF_BASE, R.string.title_module_attachvoucher))
            add(DFP({
                it.startsWith(TOPCHAT_IDLESS) || it.startsWith(ApplinkConstInternalGlobal.TOPCHAT)
            }, DF_BASE, R.string.title_topchat))

            add(DFP({ it.startsWith(INBOX_TALK) }, DF_BASE, R.string.talk_title))
            add(DFP({ it.startsWith(SHOP_TALK) }, DF_BASE, R.string.talk_title))
            add(DFP({ it.startsWith(PRODUCT_TALK) }, DF_BASE, R.string.talk_title))
            add(DFP({ it.startsWith(DETAIL_TALK_BASE) }, DF_BASE, R.string.talk_title))
            add(DFP({ it.startsWith(ADD_TALK) }, DF_BASE, R.string.talk_title))

            add(DFP({ it.startsWith(ADD_FINGERPRINT_ONBOARDING) }, DF_BASE, R.string.fingerprint_onboarding))
            add(DFP({ it.startsWith(LIVENESS_DETECTION) }, DF_USER_LIVENESS, R.string.applink_liveness_detection))

            add(DFP({ it.startsWith(NOTIFICATION) }, DF_BASE, R.string.title_notification_center))
            add(DFP({ it.startsWith(NOTIFICATION_BUYER) }, DF_BASE, R.string.title_notification_center))

            add(DFP({ it.startsWith(SHIPPING_CONFIRMATION) }, DF_BASE, R.string.path_shipping_confirmation))
            add(DFP({ it.startsWith(ORDER_TRACKING) }, DF_BASE, R.string.path_order_tracking))
        }
    }

    private val deeplinkDFPatternListSellerApp: List<DFP> by lazy {
        mutableListOf<DFP>().apply {
            add(DFP({ it.startsWith(SHOP_SETTINGS_BASE) }, DF_BASE, R.string.shop_settings_title))
            add(DFP({ it.startsWith(PAYMENT_SETTING) }, DF_BASE, R.string.payment_settings_title))
            add(DFP({ it.startsWith(PRODUCT_MANAGE_LIST) }, DF_BASE, R.string.title_applink_product_manage))
            add(DFP({ it.startsWith(USER_IDENTIFICATION_FORM) }, DF_BASE, R.string.user_identification_common_title))
            add(DFP({ it.startsWith(TOPADS_DASHBOARD_SELLER) ||
                it.startsWith(TOPADS_DASHBOARD_INTERNAL) }, DF_BASE, R.string.applink_topads_dashboard_title))
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
                    deeplink, it.moduleId, context.getString(it.moduleNameResourceId), (it.webviewFallbackLogic?.invoke(deeplink))
                    ?: "")
            }
        }
        return null
    }

    private fun getDFDeeplinkIfNotInstalled(context: Context, deeplink: String,
                                            moduleId: String, moduleName: String,
                                            fallbackUrl: String = ""): String? {
        getSplitManager(context)?.let {
            val hasInstalled = it.installedModules.contains(moduleId)
            if (hasInstalled) {
                return null
            } else {
                return UriUtil.buildUri(
                    DYNAMIC_FEATURE_INSTALL,
                    moduleId,
                    moduleName,
                    Uri.encode(deeplink).toString(),
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

    private fun getDefaultFallbackUrl(deeplink: String): String {
        val uri = Uri.parse(deeplink)
        val fallbackUrl = uri.getQueryParameter(DFFALLBACKURL_KEY) ?: return ""
        return URLDecoder.decode(fallbackUrl, "UTF-8")
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
    val webviewFallbackLogic: ((deeplink: String) -> String)? = null
)

fun String.startsWithPattern(prefix: String): Boolean {
    return startsWith(prefix.substringBefore("{")) || startsWith(prefix.substringBefore("?"))
}