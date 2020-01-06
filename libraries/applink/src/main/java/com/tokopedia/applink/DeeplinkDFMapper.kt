package com.tokopedia.applink

import android.content.Context
import android.net.Uri
import android.os.Build
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
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds.TOPADS_DASHBOARD_CUSTOMER
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds.TOPADS_DASHBOARD_INTERNAL
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds.TOPADS_DASHBOARD_SELLER
import com.tokopedia.applink.internal.ApplinkConstInternalTravel.INTERNAL_FLIGHT
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.FINAL_PRICE
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.MONEYIN_INTERNAL
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.TRADEIN
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.OVO_PAY_WITH_QR_ENTRY
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.OQR_PIN_URL_ENTRY
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.ATTACH_INVOICE
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.USER_NOTIFICATION_SETTING
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.OPPORTUNITY
import com.tokopedia.applink.internal.ApplinkConstInternalPayment.PAYMENT_SETTING
import com.tokopedia.applink.internal.ApplinkConstInternalSalam.SALAM_ORDER_DETAIL
import com.tokopedia.applink.internal.ApplinkConstInternalSalam.SALAM_UMRAH_HOME_PAGE
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
    val DFM_ONBOARDING = "df_base_onboarding"
    private val DFM_CATEGORY = "df_category"
    private val DFM_CATEGORY_TRADEIN = "df_category_tradein"
    private val DFM_CONTENT = "df_content"
    private val DFM_DIGITAL = "df_digital"
    private val DFM_DISCOVERY = "df_discovery"
    private val DFM_FINTECH = "df_fintech"
    private val DFM_IM = "df_im"
    @JvmField
    val DFM_MERCHANT_SELLER_CUSTOMERAPP = "df_merchant_seller"
    private val DFM_MERCHANT_BUYER = "df_merchant_buyer"
    private val DFM_OPERATIONAL = "df_operational"
    private val DFM_PAYMENT = "df_payment"
    private val DFM_PROMO = "df_promo"
    private val DFM_TRAVEL = "df_travel"
    private val DFM_USER = "df_user"

    //sellerapp
    private val DFM_PRODUCT_MANAGE_SELLER = "product_manage_seller"
    private val DFM_SHOP_SETTINGS_SELLERAPP = "shop_settings_sellerapp"
    private val DFM_SELLER_TOPADS_DASHBOARD = "seller_topads_dashboard"

    private var manager: SplitInstallManager? = null
    private val deeplinkDFPatternListCustomerApp: List<DFP> by lazy {
        mutableListOf<DFP>().apply {
            // Base

            // Category
            add(DFP({ it.startsWith(TRADEIN) }, DFM_CATEGORY_TRADEIN, R.string.applink_title_tradein))
            add(DFP({ it.startsWith(FINAL_PRICE) }, DFM_CATEGORY_TRADEIN, R.string.applink_harga_final))
            add(DFP({ it.startsWith(MONEYIN_INTERNAL) }, DFM_CATEGORY_TRADEIN, R.string.money_in))

            add(DFP({ it.startsWith(AGE_RESTRICTION) }, DFM_CATEGORY, R.string.applink_title_age_restriction))

            // Content
            add(DFP({ it.startsWithPattern(PROFILE) }, DFM_CONTENT, R.string.applink_title_profile))
            add(DFP({ it.startsWithPattern(INTERNAL_AFFILIATE) }, DFM_CONTENT, R.string.applink_title_affiliate))

            // Digital
            add(DFP({ it.startsWith(DIGITAL_SUBHOMEPAGE) }, DFM_DIGITAL, R.string.title_digital_subhomepage))
            add(DFP({ it.startsWith(TELCO_DIGITAL) }, DFM_DIGITAL, R.string.digital_topup_title))
            add(DFP({ it.startsWith(VOUCHER_GAME) }, DFM_DIGITAL, R.string.title_voucher_game))

            add(DFP({ it.startsWith(GLOBAL_INTERNAL_DIGITAL_DEAL) }, DFM_DIGITAL, R.string.title_digital_deals))
            add(DFP({ it.startsWithPattern(GLOBAL_INTERNAL_DIGITAL_DEAL_SLUG) }, DFM_DIGITAL, R.string.title_digital_deals))
            add(DFP({ it.startsWith(GLOBAL_INTERNAL_DIGITAL_DEAL_CATEGORY) }, DFM_DIGITAL, R.string.title_digital_deals))
            add(DFP({ it.startsWith(GLOBAL_INTERNAL_DIGITAL_DEAL_ALL_BRANDS) }, DFM_DIGITAL, R.string.title_digital_deals))
            add(DFP({ it.startsWith(GLOBAL_INTERNAL_DIGITAL_DEAL_BRAND_DETAIL) }, DFM_DIGITAL, R.string.title_digital_deals))

            // Discovery
            add(DFP({ it.startsWith(IMAGE_SEARCH_RESULT) ||
                    it.startsWith(SIMILAR_SEARCH_RESULT_BASE) }, DFM_DISCOVERY, R.string.title_image_search))
            add(DFP({ it.startsWith(SEARCH_RESULT) ||
                    it.startsWith(AUTOCOMPLETE)}, DFM_DISCOVERY, R.string.title_search_result))

            // Fintech
            add(DFP({it.startsWith(OVO_PAY_WITH_QR_ENTRY)}, DFM_FINTECH, R.string.ovo_pay_with_qr_title))
            add(DFP({it.startsWith(OQR_PIN_URL_ENTRY)}, DFM_FINTECH, R.string.ovo_pay_with_qr_title))
            add(DFP({ it.startsWith(OVO_WALLET)}, DFM_FINTECH, R.string.applink_wallet_title))
            add(DFP({ it.startsWith(SALDO_DEPOSIT)}, DFM_FINTECH, R.string.applink_saldo_deposit_title))
            add(DFP({ it.startsWith(SALDO_INTRO)}, DFM_FINTECH, R.string.applink_saldo_intro_title))
            add(DFP({ it.startsWith(OVOP2PTRANSFERFORM_SHORT) }, DFM_FINTECH, R.string.title_ovop2p))
            add(DFP({ it.startsWith(GLOBAL_INTERNAL_INSTANT_LOAN) }, DFM_FINTECH, R.string.instant_loan_title))
            add(DFP({ it.startsWith(GLOBAL_INTERNAL_INSTANT_LOAN_TAB) ||
                    it.startsWith(GLOBAL_INTERNAL_PINJAMAN_ONLINE_TAB) }, DFM_FINTECH, R.string.instant_loan_title))

            // IM
            add(DFP({ it.startsWith(REFERRAL) }, DFM_IM, R.string.applink_title_im_referral))

            // Merchant
            add(DFP({ it.startsWithPattern(REPORT_PRODUCT) }, DFM_MERCHANT_BUYER, R.string.applink_report_title))
            add(DFP({ it.startsWith(OPEN_SHOP) }, DFM_MERCHANT_BUYER, R.string.title_open_shop))

            add(DFP({ it.startsWith(PRODUCT_MANAGE_LIST) ||
                    it.startsWith(POWER_MERCHANT_SUBSCRIBE) ||
                    it.startsWith(SHOP_SETTINGS_BASE) ||
                    it.startsWith(TOPADS_DASHBOARD_CUSTOMER) ||
                    it.startsWith(TOPADS_DASHBOARD_INTERNAL) ||
                    it.startsWith(OPPORTUNITY) ||
                    it.startsWith(SELLER_TRANSACTION)
            }, DFM_MERCHANT_SELLER_CUSTOMERAPP, R.string.merchant_seller))

            // Operational
            add(DFP({ it.startsWith(CONTACT_US_NATIVE) ||
                    it.startsWith(CONTACT_US) ||
                    it.startsWithPattern(TICKET_DETAIL) }, DFM_OPERATIONAL, R.string.applink_title_contact_us))
            add(DFP({it.startsWith(CHAT_BOT)}, DFM_OPERATIONAL, R.string.title_applink_chatbot))

            // Payment
            add(DFP({ it.startsWith(PAYMENT_SETTING) }, DFM_PAYMENT, R.string.payment_settings_title))

            // Promo
            add(DFP({ it.startsWith(INTERNAL_TOKOPOINTS)},DFM_PROMO,R.string.title_tokopoints))

            // Travel
            add(DFP({ it.startsWith(TRAVEL_SUBHOMEPAGE) }, DFM_TRAVEL, R.string.title_travel_homepage))
            add(DFP({ it.startsWith(FLIGHT) }, DFM_TRAVEL, R.string.title_flight))
            add(DFP({ it.startsWith(INTERNAL_FLIGHT) }, DFM_TRAVEL, R.string.title_flight))
            add(DFP({ it.startsWith(HOTEL) }, DFM_TRAVEL, R.string.title_hotel))

            // User
            add(DFP({ it.startsWith(GROUPCHAT_LIST) }, DFM_USER, R.string.title_groupchat))
            add(DFP({ it.startsWith(GROUPCHAT_DETAIL) }, DFM_USER, R.string.title_groupchat))
            add(DFP({ it.startsWith(SETTING_PROFILE) }, DFM_USER, R.string.applink_profile_completion_title))
            add(DFP({ it.startsWith(CHANGE_PHONE_NUMBER) }, DFM_USER, R.string.applink_change_phone_number))
            add(DFP({ it.startsWith(SETTING_BANK) }, DFM_USER, R.string.applink_setting_bank_title))
            add(DFP({ it.startsWith(USER_NOTIFICATION_SETTING) }, DFM_USER, R.string.notif_settings_title))
            add(DFP({ it.startsWith(ATTACH_INVOICE) }, DFM_USER, R.string.title_module_attachinvoice))
            add(DFP({ it.startsWith(SALAM_UMRAH_HOME_PAGE) }, DFM_SALAM, R.string.title_salam))
            add(DFP({ it.startsWith(SALAM_ORDER_DETAIL) }, DFM_SALAM, R.string.title_salam))
        }
    }

    private val deeplinkDFPatternListSellerApp: List<DFP> by lazy {
        mutableListOf<DFP>().apply {
            add(DFP({ it.startsWith(SHOP_SETTINGS_BASE) }, DFM_SHOP_SETTINGS_SELLERAPP, R.string.shop_settings_title))
            add(DFP({ it.startsWith(TOPADS_DASHBOARD_SELLER) ||
                it.startsWith(TOPADS_DASHBOARD_INTERNAL) }, DFM_SELLER_TOPADS_DASHBOARD, R.string.applink_topads_dashboard_title))
            add(DFP({ it.startsWith(PRODUCT_MANAGE_LIST) }, DFM_PRODUCT_MANAGE_SELLER, R.string.title_applink_product_manage))
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
                return null
            } else {
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