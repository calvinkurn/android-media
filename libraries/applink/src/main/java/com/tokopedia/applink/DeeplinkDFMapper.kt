package com.tokopedia.applink

import android.content.Context
import android.net.Uri
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.tokopedia.applink.ApplinkConst.ADD_FINGERPRINT_ONBOARDING
import com.tokopedia.applink.ApplinkConst.BRAND_LIST
import com.tokopedia.applink.ApplinkConst.BRAND_LIST_WITH_SLASH
import com.tokopedia.applink.ApplinkConst.BUYER_ORDER_EXTENSION
import com.tokopedia.applink.ApplinkConst.CHANGE_INACTIVE_PHONE
import com.tokopedia.applink.ApplinkConst.CONTACT_US
import com.tokopedia.applink.ApplinkConst.CONTACT_US_NATIVE
import com.tokopedia.applink.ApplinkConst.DFFALLBACKURL_KEY
import com.tokopedia.applink.ApplinkConst.DIGITAL_CART
import com.tokopedia.applink.ApplinkConst.DIGITAL_SUBHOMEPAGE_HOME
import com.tokopedia.applink.ApplinkConst.FAVORITE
import com.tokopedia.applink.ApplinkConst.FLIGHT
import com.tokopedia.applink.ApplinkConst.HAS_PASSWORD
import com.tokopedia.applink.ApplinkConst.HOTEL
import com.tokopedia.applink.ApplinkConst.OPTIMIZED_CHECKOUT
import com.tokopedia.applink.ApplinkConst.ORDER_HISTORY
import com.tokopedia.applink.ApplinkConst.ORDER_TRACKING
import com.tokopedia.applink.ApplinkConst.OTP
import com.tokopedia.applink.ApplinkConst.OVOP2PTRANSFERFORM_SHORT
import com.tokopedia.applink.ApplinkConst.OVO_WALLET
import com.tokopedia.applink.ApplinkConst.PAYLATER
import com.tokopedia.applink.ApplinkConst.PLAY_BROADCASTER
import com.tokopedia.applink.ApplinkConst.PLAY_DETAIL
import com.tokopedia.applink.ApplinkConst.PLAY_RECOM
import com.tokopedia.applink.ApplinkConst.PLAY_SHORTS
import com.tokopedia.applink.ApplinkConst.PM_BENEFIT_PACKAGE
import com.tokopedia.applink.ApplinkConst.POWER_MERCHANT_SUBSCRIBE
import com.tokopedia.applink.ApplinkConst.PRODUCT_ADD
import com.tokopedia.applink.ApplinkConst.PRODUCT_MANAGE
import com.tokopedia.applink.ApplinkConst.PRODUCT_TALK
import com.tokopedia.applink.ApplinkConst.REFERRAL
import com.tokopedia.applink.ApplinkConst.REVIEW_REMINDER_PREVIOUS
import com.tokopedia.applink.ApplinkConst.SELLER_COD_ACTIVATION
import com.tokopedia.applink.ApplinkConst.SELLER_TRANSACTION
import com.tokopedia.applink.ApplinkConst.SELLER_WAREHOUSE_DATA
import com.tokopedia.applink.ApplinkConst.SHOP
import com.tokopedia.applink.ApplinkConst.SHOP_ETALASE
import com.tokopedia.applink.ApplinkConst.SHOP_ETALASE_WITH_KEYWORD_AND_SORT
import com.tokopedia.applink.ApplinkConst.SHOP_HOME
import com.tokopedia.applink.ApplinkConst.SHOP_INFO
import com.tokopedia.applink.ApplinkConst.SHOP_MVC_LOCKED_TO_PRODUCT
import com.tokopedia.applink.ApplinkConst.SHOP_NOTE
import com.tokopedia.applink.ApplinkConst.SHOP_PENALTY
import com.tokopedia.applink.ApplinkConst.SHOP_PENALTY_DETAIL
import com.tokopedia.applink.ApplinkConst.SHOP_REVIEW
import com.tokopedia.applink.ApplinkConst.SHOP_SCORE_DETAIL
import com.tokopedia.applink.ApplinkConst.SHOP_SETTINGS_CUSTOMER_APP
import com.tokopedia.applink.ApplinkConst.SHOP_SETTINGS_NOTE
import com.tokopedia.applink.ApplinkConst.SHOP_TALK
import com.tokopedia.applink.ApplinkConst.SellerApp
import com.tokopedia.applink.ApplinkConst.SellerApp.REVIEW_REMINDER
import com.tokopedia.applink.ApplinkConst.SellerApp.SELLER_SEARCH
import com.tokopedia.applink.ApplinkConst.SellerApp.STATISTIC_DASHBOARD
import com.tokopedia.applink.ApplinkConst.TICKET_DETAIL
import com.tokopedia.applink.ApplinkConst.TOPCHAT_IDLESS
import com.tokopedia.applink.ApplinkConst.TokopediaNow
import com.tokopedia.applink.DeeplinkDFApp.deeplinkDFPatternListCustomerAppv2
import com.tokopedia.applink.DeeplinkDFApp.deeplinkDFPatternListSellerAppv2
import com.tokopedia.applink.internal.ApplinkConsInternalDigital.CAMERA_OCR
import com.tokopedia.applink.internal.ApplinkConsInternalDigital.CHECKOUT_DIGITAL
import com.tokopedia.applink.internal.ApplinkConsInternalDigital.DIGITAL_PRODUCT_FORM
import com.tokopedia.applink.internal.ApplinkConsInternalDigital.GENERAL_TEMPLATE
import com.tokopedia.applink.internal.ApplinkConsInternalDigital.INTERNAL_SMARTCARD_BRIZZI
import com.tokopedia.applink.internal.ApplinkConsInternalDigital.INTERNAL_SMARTCARD_EMONEY
import com.tokopedia.applink.internal.ApplinkConsInternalDigital.SMART_BILLS
import com.tokopedia.applink.internal.ApplinkConsInternalDigital.TELCO_POSTPAID_DIGITAL
import com.tokopedia.applink.internal.ApplinkConsInternalDigital.TELCO_PREPAID_DIGITAL
import com.tokopedia.applink.internal.ApplinkConsInternalDigital.VOUCHER_GAME
import com.tokopedia.applink.internal.ApplinkConsInternalHome.DEFAULT_HOME_RECOMMENDATION
import com.tokopedia.applink.internal.ApplinkConsInternalHome.HOME_WISHLIST
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.AGE_RESTRICTION
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.FINAL_PRICE
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.INTERNAL_BELANJA_CATEGORY
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.INTERNAL_CATALOG
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.INTERNAL_CATALOG_LIBRARY
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.INTERNAL_CATEGORY
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.INTERNAL_EXPLORE_CATEGORY
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.INTERNAL_E_PHARMACY
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.INTERNAL_FIND
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.INTERNAL_HOTLIST_REVAMP
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.MONEYIN_INTERNAL
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.TRADEIN
import com.tokopedia.applink.internal.ApplinkConstInternalCommunication
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.applink.internal.ApplinkConstInternalContent.COMMENT
import com.tokopedia.applink.internal.ApplinkConstInternalContent.CONTENT_REPORT
import com.tokopedia.applink.internal.ApplinkConstInternalContent.INTERNAL_AFFILIATE
import com.tokopedia.applink.internal.ApplinkConstInternalContent.INTERNAL_CONTENT_POST_DETAIL
import com.tokopedia.applink.internal.ApplinkConstInternalContent.VIDEO_DETAIL
import com.tokopedia.applink.internal.ApplinkConstInternalDeals.DEALS_BRAND_DETAIL_PAGE
import com.tokopedia.applink.internal.ApplinkConstInternalDeals.DEALS_BRAND_PAGE
import com.tokopedia.applink.internal.ApplinkConstInternalDeals.DEALS_CATEGORY_PAGE
import com.tokopedia.applink.internal.ApplinkConstInternalDeals.DEALS_HOMEPAGE
import com.tokopedia.applink.internal.ApplinkConstInternalDilayaniTokopedia
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery.AUTOCOMPLETE
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery.SEARCH_RESULT
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery.SIMILAR_SEARCH_RESULT_BASE
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery.UNIVERSAL
import com.tokopedia.applink.internal.ApplinkConstInternalEntertainment.EVENT_HOME
import com.tokopedia.applink.internal.ApplinkConstInternalEntertainment.EVENT_PDP
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.ADD_TALK
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.CHANGE_PHONE_NUMBER
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.CHAT_BOT
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.DETAIL_TALK_BASE
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.DYNAMIC_FEATURE_INSTALL
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.DYNAMIC_FEATURE_INSTALL_BASE
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.INBOX_TALK
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.OQR_PIN_URL_ENTRY
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.OVO_PAY_WITH_QR_ENTRY
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PRODUCT_TALK_BASE
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.SALDO_DEPOSIT
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.SALDO_INTRO
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.SETTING_BANK
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.TALK_REPLY_BASE
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.TALK_SELLER_SETTINGS
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.ADD_ADDRESS_V1
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.ADD_ADDRESS_V2
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.ADD_ADDRESS_V3
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.DROPOFF_PICKER
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.MANAGE_ADDRESS
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.PINPOINT
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.SHOP_EDIT_ADDRESS
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.ADD_ON_GIFTING
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.ATTACH_INVOICE
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.ATTACH_PRODUCT
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.ATTACH_VOUCHER
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.CHECKOUT
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.DISTRICT_RECOMMENDATION_SHOP_SETTINGS
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.GEOLOCATION
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.INBOX
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.ONBOARDING
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.ONE_CLICK_CHECKOUT
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.OPEN_SHOP
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.REPORT_PRODUCT
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.RESERVED_STOCK_BASE
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.SHOP_OPERATIONAL_HOUR_BOTTOM_SHEET
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.SHOP_PAGE_BASE
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.SHOP_PAGE_SETTING_CUSTOMER_APP_WITH_SHOP_ID
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.SHOP_SETTINGS_BASE
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.USER_NOTIFICATION_SETTING
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.applink.internal.ApplinkConstInternalMechant.BRANDLIST
import com.tokopedia.applink.internal.ApplinkConstInternalMechant.BRANDLIST_SEARCH
import com.tokopedia.applink.internal.ApplinkConstInternalMechant.MERCHANT_ADDON
import com.tokopedia.applink.internal.ApplinkConstInternalMechant.MERCHANT_GIFTING
import com.tokopedia.applink.internal.ApplinkConstInternalMechant.MERCHANT_OPEN_PRODUCT_PREVIEW
import com.tokopedia.applink.internal.ApplinkConstInternalMechant.MERCHANT_PRODUCT_BUNDLE
import com.tokopedia.applink.internal.ApplinkConstInternalMechant.MERCHANT_PRODUCT_DRAFT
import com.tokopedia.applink.internal.ApplinkConstInternalMechant.MERCHANT_SHOP_SCORE
import com.tokopedia.applink.internal.ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST
import com.tokopedia.applink.internal.ApplinkConstInternalMechant.MERCHANT_STATISTIC_DASHBOARD
import com.tokopedia.applink.internal.ApplinkConstInternalMedia.INTERNAL_MEDIA_EDITOR
import com.tokopedia.applink.internal.ApplinkConstInternalMedia.INTERNAL_MEDIA_PICKER
import com.tokopedia.applink.internal.ApplinkConstInternalMedia.INTERNAL_MEDIA_PICKER_ALBUM
import com.tokopedia.applink.internal.ApplinkConstInternalMedia.INTERNAL_MEDIA_PICKER_PREVIEW
import com.tokopedia.applink.internal.ApplinkConstInternalNotification.NOTIFICATION
import com.tokopedia.applink.internal.ApplinkConstInternalNotification.NOTIFICATION_BUYER
import com.tokopedia.applink.internal.ApplinkConstInternalOperational
import com.tokopedia.applink.internal.ApplinkConstInternalOperational.INTERNAL_INBOX_LIST
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.BELANJA_INTERNAL_ORDER
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.DEALS_INTERNAL_ORDER
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.DIGITAL_ORDER_LIST_INTERNAL
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.EVENTS_INTERNAL_ORDER
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.GIFTCARDS_INTERNAL_ORDER
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.HOTEL_INTERNAL_ORDER
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.INSURANCE_INTERNAL_ORDER
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.INTERNAL_BUYER
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.INTERNAL_ORDER
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.INTERNAL_ORDER_BUYER_CANCELLATION_REQUEST_PAGE
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.INTERNAL_ORDER_SNAPSHOT
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.INTERNAL_SELLER
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.INTERNAL_TRANSACTION_ORDERLIST
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.MARKETPLACE_INTERNAL_BUYER_ORDER_DETAIL
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.MODALTOKO_INTERNAL_ORDER
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.ORDERLIST_DIGITAL_INTERNAL
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.ORDER_LIST_INTERNAL
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PESAWAT_INTERNAL_ORDER
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.TRACK
import com.tokopedia.applink.internal.ApplinkConstInternalPayment
import com.tokopedia.applink.internal.ApplinkConstInternalPayment.PAYMENT_SETTING
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.applink.internal.ApplinkConstInternalPromo.INTERNAL_TOKOPOINTS
import com.tokopedia.applink.internal.ApplinkConstInternalPromo.PROMO_CAMPAIGN_SHAKE_LANDING_PREFIX
import com.tokopedia.applink.internal.ApplinkConstInternalPromo.PROMO_CHECKOUT_MARKETPLACE
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform.WISHLIST_COLLECTION
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform.WISHLIST_V2
import com.tokopedia.applink.internal.ApplinkConstInternalSalam.SALAM_ORDER_DETAIL
import com.tokopedia.applink.internal.ApplinkConstInternalSalam.SALAM_UMRAH_HOME_PAGE
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp.CAMPAIGN_LIST
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp.SELLER_MENU
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp.SELLER_MVC_CREATE
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp.SELLER_MVC_DETAIL
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp.SELLER_MVC_INTRO
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp.SELLER_MVC_LIST
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp.SELLER_SHOP_FLASH_SALE
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp.TOKOMEMBER
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp.WELCOME
import com.tokopedia.applink.internal.ApplinkConstInternalTokoFood
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds.TOPADS_DASHBOARD_CUSTOMER
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds.TOPADS_DASHBOARD_INTERNAL
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds.TOPADS_DASHBOARD_SELLER
import com.tokopedia.applink.internal.ApplinkConstInternalTravel.INTERNAL_FLIGHT
import com.tokopedia.applink.internal.ApplinkConstInternalTravel.INTERNAL_HOTEL
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.ACCOUNT_LINKING_WEBVIEW
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.ADD_BOD
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.ADD_EMAIL
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.ADD_NAME_REGISTER
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.ADD_PHONE
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.ADD_PIN
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.CHANGE_GENDER
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.CHANGE_NAME
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.CHANGE_PIN
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.CHOOSE_ACCOUNT
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.CONSENT_WITHDRAWAL_NEW
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.FORGOT_PASSWORD
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.KYC_ALA_CARTE
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.KYC_FORM
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.KYC_INFO
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.KYC_LIVENESS_BASE
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.NEW_ADD_PHONE
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PRIVACY_CENTER
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PUSH_NOTIFICATION_TROUBLESHOOTER
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.SEARCH_HISTORY
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.SETTING_PROFILE
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.SHARING_WISHLIST
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.GOTO_KYC
import com.tokopedia.applink.model.DFPPath
import com.tokopedia.applink.model.DFPSchemeToDF
import com.tokopedia.applink.review.ReviewApplinkConst
import com.tokopedia.config.GlobalConfig
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tokopedia.applink.R
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.net.URLDecoder
import kotlin.coroutines.CoroutineContext

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
object DeeplinkDFMapper : CoroutineScope {
    // it should have the same name with the folder of dynamic feature
    const val DF_BASE = "df_base"
    const val DF_BASE_SELLER_APP = "df_base_sellerapp"
    const val DF_CATEGORY_TRADE_IN = "df_category_trade_in"
    const val DF_CATEGORY_EPHARMACY = "df_category_epharmacy"
    const val DF_CATEGORY_CATALOG_LIBRARY = "df_category_catalog_library"
    const val DF_CATEGORY_AFFILIATE = "df_category_affiliate"
    const val DF_MERCHANT_SELLER = "df_merchant_seller"
    const val DF_MERCHANT_NONLOGIN = "df_merchant_nonlogin"
    const val DF_MERCHANT_PRODUCT_AR = "df_merchant_product_ar"
    const val DF_OPERATIONAL_CONTACT_US = "df_operational_contact_us"
    const val DF_SALAM_UMRAH = "df_salam_umrah"
    const val DF_TRAVEL = "df_travel"
    const val DF_USER_LIVENESS = "df_user_liveness"
    const val DF_USER_SETTINGS = "df_user_settings"
    const val DF_KYC_SELLERAPP = "df_kyc_sellerapp"
    const val DF_USER_FINGERPRINT = "df_user_fingerprint"
    const val DF_FLASH_SALE_TOKOPEDIA = "df_flash_sale_tokopedia"
    const val DF_PROMO_GAMIFICATION = "df_promo_gamification"
    const val DF_PROMO_TOKOPOINTS = "df_promo_tokopoints"
    const val DF_PROMO_CHECKOUT = "df_promo_checkout"
    const val DF_GAMIFICATION = "df_gamification"
    const val DF_SHOP_SETTINGS_SELLER_APP = "df_shop_settings_sellerapp"
    const val DF_ENTERTAINMENT = "df_entertainment"
    const val DF_MERCHANT_LOGIN = "df_merchant_login"
    const val DF_TOKOPEDIA_NOW = "df_tokopedianow"
    const val DF_TOKOFOOD = "df_tokofood"
    const val DF_CONTENT_PLAY_BROADCASTER = "df_content_play_broadcaster"
    const val DF_FEED_CONTENT_CREATION = "df_feed_content_creation"
    const val DF_PEOPLE = "df_people"
    const val DF_ALPHA_TESTING = "df_alpha_testing"
    const val DF_DIGITAL = "df_digital"
    const val DF_TOKOCHAT = "df_comm_tokochat"
    const val DF_SELLER_FRONT_FUNNEL = "df_seller_front_funnel"
    const val DF_DILAYANI_TOKOPEDIA = "df_dilayanitokopedia"
    const val DF_CAMPAIGN_LIST = "df_campaign_list"
    const val DF_SELLER_FEEDBACK = "df_seller_feedback"
    const val DF_SELLER_TALK = "df_seller_talk"

    const val SHARED_PREF_TRACK_DF_USAGE = "pref_track_df_usage"
    var dfUsageList = mutableListOf<String>()

    private var manager: SplitInstallManager? = null

    /**
     * map the original deeplink to [Dynamic Feature Install] Deeplink
     * (only if the deeplink is dynamic Feature deeplink and if the module is not installed yet)
     */
    @JvmStatic
    fun getDFDeeplinkIfNotInstalled(context: Context, deeplink: String): String? {
        if (deeplink.startsWith(DYNAMIC_FEATURE_INSTALL_BASE)) {
            return null
        }
        return if (GlobalConfig.isSellerApp()) {
            executeDeeplinkPatternv2(
                context,
                Uri.parse(deeplink),
                deeplink,
                deeplinkDFPatternListSellerAppv2(context)
            )
        } else {
            executeDeeplinkPatternv2(
                context,
                Uri.parse(deeplink),
                deeplink,
                deeplinkDFPatternListCustomerAppv2(context)
            )
        }
    }

    private fun executeDeeplinkPatternv2(
        context: Context,
        uri: Uri,
        deeplink: String,
        list: List<DFPSchemeToDF>
    ): String? {
        val dfModuleFromDeeplinkPath = getDFModuleFromDeeplink(uri, list)
        return if (dfModuleFromDeeplinkPath == null) {
            null
        } else {
            getDFDeeplinkIfNotInstalled(
                context,
                deeplink,
                dfModuleFromDeeplinkPath.dfTarget,
                dfModuleFromDeeplinkPath.webviewFallbackUrl
            )
        }
    }

    fun getDFModuleFromDeeplink(
        uri: Uri,
        list: List<DFPSchemeToDF>
    ): DFPPath? {
        val uriPathSingle = uri.path?.replace("//", "/")
        for (dfpSchemeToDF in list) {
            if (dfpSchemeToDF.scheme != uri.scheme) {
                continue
            }
            val hostList = dfpSchemeToDF.hostList
            for (dfpHost in hostList) {
                if (dfpHost.host != uri.host) {
                    continue
                }
                val dfpPathList = dfpHost.dfpPathObj
                for (dfpPath in dfpPathList) {
                    if (dfpPath.pattern == null || dfpPath.pattern.matches(
                            (uriPathSingle ?: "").toString()
                        )
                    ) {
                        return dfpPath
                    }
                }
            }
        }
        return null
    }

    private fun getDFDeeplinkIfNotInstalled(
        context: Context,
        deeplink: String,
        moduleId: String,
        fallbackUrl: String? = ""
    ): String? {
        getSplitManager(context)?.let {
            val hasInstalled = it.installedModules.contains(moduleId)
            return if (hasInstalled) {
                trackDFUsageOnce(context.applicationContext, moduleId, deeplink)
                null
            } else {
                UriUtil.buildUri(
                    DYNAMIC_FEATURE_INSTALL,
                    moduleId,
                    Uri.encode(deeplink).toString(),
                    if (fallbackUrl.isNullOrEmpty()) {
                        getDefaultFallbackUrl(deeplink)
                    } else {
                        fallbackUrl
                    }
                )
            }
        } ?: return null
    }

    private fun trackDFUsageOnce(context: Context, moduleId: String, deeplink: String) {
        if (moduleId == DF_BASE || moduleId in dfUsageList) {
            return
        }
        launch(Dispatchers.IO) {
            try {
                val sp = context.getSharedPreferences(SHARED_PREF_TRACK_DF_USAGE, Context.MODE_PRIVATE)
                val hasAccessedModule = sp.getBoolean(moduleId, false)
                if (!hasAccessedModule) {
                    ServerLogger.log(Priority.P1, "DFM_OPENED", mapOf("type" to moduleId, "deeplink" to deeplink))
                    sp.edit().putBoolean(moduleId, true).apply()
                }
                dfUsageList.add(moduleId)
            } catch (ignored: Exception) {
            }
        }
    }

    private fun getSplitManager(context: Context): SplitInstallManager? {
        if (manager === null) {
            manager = SplitInstallManagerFactory.create(context.applicationContext)
        }
        return manager
    }

    private fun getDefaultFallbackUrl(deeplink: String?): String {
        if (deeplink.isNullOrEmpty()) {
            return ""
        }
        val uri = Uri.parse(deeplink)
        val fallbackUrl = uri.getQueryParameter(DFFALLBACKURL_KEY) ?: return ""
        return URLDecoder.decode(fallbackUrl, "UTF-8")
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + CoroutineExceptionHandler { _, _ -> }
}

fun String.startsWithPattern(prefix: String): Boolean {
    return startsWith(prefix.substringBefore("{")) || startsWith(prefix.substringBefore("?"))
}
