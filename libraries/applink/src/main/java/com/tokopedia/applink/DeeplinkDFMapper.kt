package com.tokopedia.applink

import android.content.Context
import android.net.Uri
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.tokopedia.applink.ApplinkConst.*
import com.tokopedia.applink.ApplinkConst.SellerApp.REVIEW_REMINDER
import com.tokopedia.applink.ApplinkConst.SellerApp.SELLER_SEARCH
import com.tokopedia.applink.internal.*
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
import com.tokopedia.applink.internal.ApplinkConsInternalHome.HOME_RECENT_VIEW
import com.tokopedia.applink.internal.ApplinkConsInternalHome.HOME_WISHLIST
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.AGE_RESTRICTION
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.FINAL_PRICE
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.INTERNAL_BELANJA_CATEGORY
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.INTERNAL_CATALOG
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.INTERNAL_CATEGORY
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.INTERNAL_EXPLORE_CATEGORY
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.INTERNAL_E_PHARMACY
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.INTERNAL_FIND
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.INTERNAL_HOTLIST_REVAMP
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.MONEYIN_INTERNAL
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.TRADEIN
import com.tokopedia.applink.internal.ApplinkConstInternalContent.COMMENT
import com.tokopedia.applink.internal.ApplinkConstInternalContent.CONTENT_REPORT
import com.tokopedia.applink.internal.ApplinkConstInternalContent.INTERNAL_CONTENT_POST_DETAIL
import com.tokopedia.applink.internal.ApplinkConstInternalContent.MEDIA_PREVIEW
import com.tokopedia.applink.internal.ApplinkConstInternalContent.VIDEO_DETAIL
import com.tokopedia.applink.internal.ApplinkConstInternalDeals.DEALS_BRAND_DETAIL_PAGE
import com.tokopedia.applink.internal.ApplinkConstInternalDeals.DEALS_BRAND_PAGE
import com.tokopedia.applink.internal.ApplinkConstInternalDeals.DEALS_CATEGORY_PAGE
import com.tokopedia.applink.internal.ApplinkConstInternalDeals.DEALS_HOMEPAGE
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery.AUTOCOMPLETE
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery.SEARCH_RESULT
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery.SIMILAR_SEARCH_RESULT_BASE
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery.UNIVERSAL
import com.tokopedia.applink.internal.ApplinkConstInternalEntertainment.EVENT_HOME
import com.tokopedia.applink.internal.ApplinkConstInternalEntertainment.EVENT_PDP
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.ADD_TALK
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.CHANGE_PHONE_NUMBER
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.CHAT_BOT
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.DETAIL_TALK_BASE
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.DYNAMIC_FEATURE_INSTALL
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.DYNAMIC_FEATURE_INSTALL_BASE
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.GLOBAL_INTERNAL_DIGITAL_DEAL_SLUG_BASE
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.INBOX_TALK
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.OQR_PIN_URL_ENTRY
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.OVO_PAY_WITH_QR_ENTRY
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.SALDO_DEPOSIT
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.SALDO_INTRO
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.SETTING_BANK
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.ADD_ADDRESS_V1
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.ADD_ADDRESS_V2
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.ADD_ADDRESS_V3
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.DROPOFF_PICKER
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.MANAGE_ADDRESS
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.SHIPPING_CONFIRMATION
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.SHOP_EDIT_ADDRESS
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.ADD_ON_GIFTING
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.ATTACH_INVOICE
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.ATTACH_PRODUCT
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.ATTACH_VOUCHER
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.CHECKOUT
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.CHECKOUT_ADDRESS_SELECTION
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
import com.tokopedia.applink.internal.ApplinkConstInternalMechant.BRANDLIST
import com.tokopedia.applink.internal.ApplinkConstInternalMechant.BRANDLIST_SEARCH
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
import com.tokopedia.applink.internal.ApplinkConstInternalOperational.INTERNAL_INBOX_LIST
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
import com.tokopedia.applink.internal.ApplinkConstInternalPayment.PAYMENT_SETTING
import com.tokopedia.applink.internal.ApplinkConstInternalPromo.INTERNAL_TOKOPOINTS
import com.tokopedia.applink.internal.ApplinkConstInternalPromo.PROMO_CAMPAIGN_SHAKE_LANDING_PREFIX
import com.tokopedia.applink.internal.ApplinkConstInternalPromo.PROMO_CHECKOUT_MARKETPLACE
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform.WISHLIST_COLLECTION
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform.WISHLIST_V2
import com.tokopedia.applink.internal.ApplinkConstInternalSalam.SALAM_ORDER_DETAIL
import com.tokopedia.applink.internal.ApplinkConstInternalSalam.SALAM_UMRAH_HOME_PAGE
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp.CREATE_VOUCHER
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp.CREATE_VOUCHER_PRODUCT
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp.SELLER_MENU
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp.SELLER_SHOP_FLASH_SALE
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp.SELLER_TOKOPEDIA_FLASH_SALE
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp.SELLER_TOKOPEDIA_FLASH_SALE_CAMPAIGN_DETAIL
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp.TOKOMEMBER
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp.VOUCHER_DETAIL
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp.VOUCHER_LIST
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp.VOUCHER_PRODUCT_DETAIL
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp.WELCOME
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
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.NEW_ADD_PHONE
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
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PRIVACY_CENTER
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.SETTING_PROFILE
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PUSH_NOTIFICATION_TROUBLESHOOTER
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.SHARING_WISHLIST
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.SEARCH_HISTORY
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
    const val DF_MERCHANT_SELLER = "df_merchant_seller"
    const val DF_MERCHANT_NONLOGIN = "df_merchant_nonlogin"
    const val DF_MERCHANT_PRODUCT_AR = "df_merchant_product_ar"
    const val DF_OPERATIONAL_CONTACT_US = "df_operational_contact_us"
    const val DF_SALAM_UMRAH = "df_salam_umrah"
    const val DF_TRAVEL = "df_travel"
    const val DF_USER_PRIVACYCENTER = "df_user_privacycenter"
    const val DF_USER_LIVENESS = "df_user_liveness"
    const val DF_USER_SETTINGS = "df_user_settings"
    const val DF_USER_FINGERPRINT = "df_user_fingerprint"
    const val DF_PROMO_GAMIFICATION = "df_promo_gamification"
    const val DF_PROMO_TOKOPOINTS = "df_promo_tokopoints"
    const val DF_PROMO_CHECKOUT = "df_promo_checkout"
    const val DF_GAMIFICATION = "df_gamification"
    const val DF_SHOP_SCORE = "shop_score_sellerapp"
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

    const val SHARED_PREF_TRACK_DF_USAGE = "pref_track_df_usage"
    var dfUsageList = mutableListOf<String>()

    private var manager: SplitInstallManager? = null
    val deeplinkDFPatternListCustomerApp: List<DFP> by lazy {
        mutableListOf<DFP>().apply {
            // Base
            add(DFP({ it.startsWithPattern(ONBOARDING) }, DF_BASE, R.string.applink_title_on_boarding))
            // Category
            add(DFP({ it.startsWith(AGE_RESTRICTION) }, DF_BASE, R.string.applink_title_age_restriction))
            add(DFP({ it.startsWith(TRADEIN) }, DF_CATEGORY_TRADE_IN, R.string.applink_title_tradein))
            add(DFP({ it.startsWith(INTERNAL_BELANJA_CATEGORY) }, DF_BASE, R.string.label_kategori))
            add(DFP({ it.startsWith(INTERNAL_HOTLIST_REVAMP) }, DF_BASE, R.string.hotlist_label))
            add(DFP({ it.startsWith(FINAL_PRICE) }, DF_CATEGORY_TRADE_IN, R.string.applink_harga_final))
            add(DFP({ it.startsWith(MONEYIN_INTERNAL) }, DF_CATEGORY_TRADE_IN, R.string.money_in))
            add(DFP({ it.startsWith(INTERNAL_EXPLORE_CATEGORY) }, DF_BASE, R.string.applink_title_explore_category))
            add(DFP({ it.startsWith(INTERNAL_CATALOG) }, DF_BASE, R.string.applink_title_catalog))
            add(DFP({ it.startsWith(INTERNAL_E_PHARMACY) }, DF_CATEGORY_EPHARMACY, R.string.applink_title_e_pharmacy))
            add(DFP({ it.startsWith(INTERNAL_FIND) }, DF_BASE, R.string.applink_title_find_native))
            add(DFP({ it.startsWith(INTERNAL_CATEGORY) }, DF_BASE, R.string.label_category))


            // Content
            add(DFP({ it.startsWithPattern(ApplinkConstInternalContent.PROFILE_DETAIL) }, DF_PEOPLE, R.string.applink_title_people))
            add(DFP({ it.startsWithPattern(PLAY_DETAIL) }, DF_BASE, R.string.applink_title_play))
            add(DFP({ it.startsWithPattern(COMMENT) }, DF_BASE, R.string.applink_kol_title_comment))
            add(DFP({ it.startsWithPattern(INTERNAL_CONTENT_POST_DETAIL) }, DF_BASE, R.string.applink_kol_title_post_detail))
            add(DFP({ it.startsWithPattern(KOL_YOUTUBE) }, DF_BASE, R.string.applink_kol_title_post_youtube))
            add(DFP({ it.startsWithPattern(CONTENT_REPORT) }, DF_BASE, R.string.applink_kol_title_content_report))
            add(DFP({ it.startsWithPattern(VIDEO_DETAIL) }, DF_BASE, R.string.applink_kol_title_video_detail))
            add(DFP({ it.startsWithPattern(MEDIA_PREVIEW) }, DF_BASE, R.string.applink_kol_title_media_preview))
            add(DFP({ it.startsWithPattern(PLAY_BROADCASTER)
                    || it.startsWith(ApplinkConstInternalContent.INTERNAL_PLAY_BROADCASTER) }, DF_CONTENT_PLAY_BROADCASTER, R.string.applink_title_play_broadcaster))
            add(DFP({
                it.startsWith(ApplinkConstInternalGlobal.IMAGE_PICKER) ||
                        it.startsWith(ApplinkConstInternalGlobal.IMAGE_EDITOR) ||
                        it.startsWith(ApplinkConstInternalGlobal.VIDEO_PICKER)
            }, DF_FEED_CONTENT_CREATION, R.string.title_image_picker))

            add(DFP({ it.startsWithPattern(ApplinkConstInternalContent.INTERNAL_AFFILIATE_CREATE_POST_V2) }, DF_FEED_CONTENT_CREATION, R.string.title_feed_create_post))
            add(DFP({ it.startsWithPattern(ApplinkConstInternalGlobal.IMAGE_PICKER_V2) }, DF_FEED_CONTENT_CREATION, R.string.title_image_picker))

            // Digital
            add(DFP({
                it.startsWith(DIGITAL_SUBHOMEPAGE_HOME) ||
                        it.startsWith(TELCO_POSTPAID_DIGITAL) ||
                        it.startsWith(TELCO_PREPAID_DIGITAL) ||
                        it.startsWith(DIGITAL_PRODUCT_FORM) ||
                        it.startsWith(GENERAL_TEMPLATE) ||
                        it.startsWith(CAMERA_OCR) ||
                        it.startsWith(VOUCHER_GAME) ||
                        it.startsWith(DIGITAL_CART) ||
                        it.startsWith(CHECKOUT_DIGITAL)
            }, DF_BASE, R.string.title_digital_subhomepage))
            add(DFP({ it.startsWithPattern(INTERNAL_SMARTCARD_EMONEY) }, DF_BASE, R.string.title_digital_emoney))
            add(DFP({ it.startsWithPattern(INTERNAL_SMARTCARD_BRIZZI) }, DF_BASE, R.string.title_digital_emoney))

            add(DFP({
                it.startsWith(SMART_BILLS)
            }, DF_DIGITAL, R.string.title_digital, { DFWebviewFallbackUrl.DIGITAL_SMART_BILLS }))

            // Discovery
            add(DFP({ it.startsWith(SIMILAR_SEARCH_RESULT_BASE) }, DF_BASE, R.string.title_similar_search))
            add(DFP({ it.startsWith(SEARCH_RESULT) || it.startsWith(AUTOCOMPLETE) || it.startsWith(
                UNIVERSAL) }, DF_BASE, R.string.title_search_result))
            add(DFP({ it.startsWith(HOME_WISHLIST) }, DF_BASE, R.string.title_wishlist))
            add(DFP({ it.startsWith(DEFAULT_HOME_RECOMMENDATION) }, DF_BASE, R.string.recom_home_recommendation))
            add(DFP({ it.startsWith(HOME_RECENT_VIEW) }, DF_MERCHANT_LOGIN, R.string.title_recent_view, { DFWebviewFallbackUrl.RECENT_VIEW }))

            // Fintech
            add(DFP({ it.startsWith(OVO_PAY_WITH_QR_ENTRY) }, DF_BASE, R.string.ovo_pay_with_qr_title))
            add(DFP({ it.startsWith(OQR_PIN_URL_ENTRY) }, DF_BASE, R.string.ovo_pay_with_qr_title))
            add(DFP({ it.startsWith(OVO_WALLET) }, DF_BASE, R.string.applink_wallet_title))
            add(DFP({ it.startsWith(PAYLATER) }, DF_BASE, R.string.applink_pay_later_title))
            add(DFP({ it.startsWith(OPTIMIZED_CHECKOUT) }, DF_BASE, R.string.applink_pay_later_title))

            add(DFP({
                it.startsWith(SALDO_DEPOSIT) ||
                        it.startsWith(SALDO_INTRO)
            }, DF_USER_SETTINGS, R.string.applink_saldo_deposit_title, { DFWebviewFallbackUrl.FINTECH_SALDO }))

            add(DFP({ it.startsWith(OVOP2PTRANSFERFORM_SHORT) }, DF_BASE, R.string.title_ovop2p))

            // IM
            add(DFP({ it.startsWith(REFERRAL) }, DF_BASE, R.string.applink_title_im_referral))

            // Logistic
            add(DFP({ it.startsWith(DROPOFF_PICKER) }, DF_BASE, R.string.dropoff_title))
            add(DFP({ it.startsWith(SHIPPING_CONFIRMATION) }, DF_BASE, R.string.path_shipping_confirmation))
            add(DFP({ it.startsWithPattern(ORDER_TRACKING) }, DF_BASE, R.string.path_order_tracking))
            add(DFP({ it.startsWith(MANAGE_ADDRESS) }, DF_BASE, R.string.path_manage_address))
            add(DFP({ it.startsWithPattern(ADD_ADDRESS_V1) }, DF_BASE, R.string.path_add_address_v1))
            add(DFP({ it.startsWith(ADD_ADDRESS_V2) }, DF_BASE, R.string.path_add_address_v2))
            add(DFP({ it.startsWith(DISTRICT_RECOMMENDATION_SHOP_SETTINGS) }, DF_BASE, R.string.path_district_recommendation_shop_settings))
            add(DFP({ it.startsWith(GEOLOCATION) }, DF_BASE, R.string.path_geolocation))
            add(DFP({ it.startsWith(SHOP_EDIT_ADDRESS) }, DF_BASE, R.string.path_edit_shop_address))
            add(DFP({ it.startsWith(SELLER_WAREHOUSE_DATA) }, DF_BASE, R.string.path_shop_settings_address))
            add(DFP({ it.startsWith(ADD_ADDRESS_V3) }, DF_BASE, R.string.path_add_address_v3))

            // Merchant
            add(DFP({ it.startsWith(OPEN_SHOP) }, DF_BASE, R.string.title_open_shop))
            add(DFP({ it.startsWithPattern(MERCHANT_PRODUCT_BUNDLE) }, DF_MERCHANT_NONLOGIN, R.string.title_bundling_selection_page ))
            add(DFP({ it.startsWithPattern(MERCHANT_GIFTING) }, DF_MERCHANT_NONLOGIN, R.string.title_gifting_bottomsheet ))

            add(DFP({ it.startsWith(FAVORITE) }, DF_MERCHANT_LOGIN, R.string.favorite_shop, { DFWebviewFallbackUrl.FAVORITE_SHOP }))
            add(DFP({ it.startsWithPattern(REPORT_PRODUCT) }, DF_MERCHANT_LOGIN, R.string.applink_report_title, ::getDefaultFallbackUrl))
            add(DFP({ it.startsWith(ATTACH_INVOICE) }, DF_MERCHANT_LOGIN, R.string.title_module_attachinvoice))
            add(DFP({ it.startsWith(ATTACH_VOUCHER) }, DF_MERCHANT_LOGIN, R.string.title_module_attachvoucher))
            add(DFP({ it.startsWith(ATTACH_PRODUCT) }, DF_MERCHANT_LOGIN, R.string.title_module_attachproduct))

            add(DFP({ it.startsWith(INTERNAL_SELLER) }, DF_MERCHANT_SELLER, R.string.merchant_seller, { DFWebviewFallbackUrl.SELLER_ORDER }))
            add(DFP({
                it.startsWith(PRODUCT_MANAGE)
                        || it.startsWith(PRODUCT_MANAGE_LIST)
                        || it.startsWith(RESERVED_STOCK_BASE)
                        || it.startsWith(ApplinkConstInternalSellerapp.SELLER_HOME_PRODUCT_MANAGE_LIST)
            }, DF_MERCHANT_SELLER, R.string.merchant_seller, { DFWebviewFallbackUrl.MANAGE_PRODUCT }))
            add(DFP({
                it.startsWith(POWER_MERCHANT_SUBSCRIBE) || it.startsWith(ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE)
                        || it.startsWith(PM_BENEFIT_PACKAGE)
                        || it.startsWith(ApplinkConstInternalMarketplace.PM_BENEFIT_PACKAGE)
            }, DF_MERCHANT_SELLER, R.string.merchant_seller, { DFWebviewFallbackUrl.POWER_MERCHANT }))
            add(DFP({ it.startsWith(SHOP_SETTINGS_BASE) }, DF_MERCHANT_SELLER, R.string.merchant_seller, { DFWebviewFallbackUrl.SHOP_SETTINGS }))
            add(DFP({
                val regexPatternToReplace = "(?=\\{)[^\\}]+\\}".toRegex()
                val cleanInternalAppLinkPattern = SHOP_PAGE_SETTING_CUSTOMER_APP_WITH_SHOP_ID.replace(regexPatternToReplace,".*")
                val cleanExternalAppLinkPattern = SHOP_SETTINGS_CUSTOMER_APP.replace(regexPatternToReplace,".*")
                val regexMatcherInternalAppLink = cleanInternalAppLinkPattern.toRegex()
                val regexMatcherExternalAppLink = cleanExternalAppLinkPattern.toRegex()
                regexMatcherInternalAppLink.matches(it) || regexMatcherExternalAppLink.matches(it)
            }, DF_MERCHANT_SELLER, R.string.merchant_seller))
            add(DFP({
                it.startsWith(TOPADS_DASHBOARD_CUSTOMER) || it.startsWith(TOPADS_DASHBOARD_INTERNAL)
            }, DF_MERCHANT_SELLER, R.string.merchant_seller, { DFWebviewFallbackUrl.TOP_ADS_DASHBOARD }))
            add(DFP({ it.startsWith(SELLER_TRANSACTION) }, DF_MERCHANT_SELLER, R.string.merchant_seller, { DFWebviewFallbackUrl.SELLER_ORDER }))
            add(DFP({ it.startsWith(MERCHANT_SHOP_SHOWCASE_LIST) }, DF_BASE, R.string.merchant_seller))
            add(DFP({ it.startsWithPattern(BRANDLIST) }, DF_BASE, R.string.title_brandlist))
            add(DFP({ it.startsWith(BRANDLIST_SEARCH) }, DF_BASE, R.string.title_brandlist))
            add(DFP({ it.startsWithPattern(BRAND_LIST) }, DF_BASE, R.string.title_brandlist))
            add(DFP({ it.startsWith(BRAND_LIST_WITH_SLASH) }, DF_BASE, R.string.title_brandlist))
            add(DFP({ it.startsWith(MERCHANT_OPEN_PRODUCT_PREVIEW) || it.startsWith(PRODUCT_ADD) }, DF_MERCHANT_SELLER, R.string.title_product_add_edit))
            add(DFP({ it.startsWith(MERCHANT_PRODUCT_DRAFT) }, DF_MERCHANT_SELLER, R.string.title_product_add_edit))
            add(DFP({ it.startsWith(SELLER_MENU) }, DF_MERCHANT_SELLER, R.string.title_seller_menu))
            add(DFP({
                it.startsWithPattern(SHOP_PAGE_BASE) ||
                        it.startsWithPattern(SHOP) ||
                        it.startsWithPattern(SHOP_ETALASE) ||
                        it.startsWithPattern(SHOP_ETALASE_WITH_KEYWORD_AND_SORT) ||
                        it.startsWithPattern(SHOP_REVIEW) ||
                        it.startsWithPattern(SHOP_NOTE) ||
                        it.startsWithPattern(SHOP_INFO) ||
                        it.startsWithPattern(SHOP_HOME) ||
                        it.startsWith(SHOP_SETTINGS_NOTE)
            }, DF_BASE, R.string.title_shop_page))
            add(DFP({ it.startsWithPattern(SHOP_OPERATIONAL_HOUR_BOTTOM_SHEET) }, DF_BASE, R.string.title_shop_widget))
            add(DFP({
                val regexPatternToReplace = "(?=\\{)[^\\}]+\\}".toRegex()
                val cleanExternalAppLinkPattern = SHOP_MVC_LOCKED_TO_PRODUCT.replace(regexPatternToReplace,".*")
                val regexMatcherExternalAppLink = cleanExternalAppLinkPattern.toRegex()
                regexMatcherExternalAppLink.matches(it)
            }, DF_BASE, R.string.title_shop_widget))
            add(DFP({
                val uri = Uri.parse(it).buildUpon().build()
                (uri.host == ReviewApplinkConst.AUTHORITY_PRODUCT && uri.pathSegments.lastOrNull() == ReviewApplinkConst.PATH_REVIEW)
            }, DF_MERCHANT_NONLOGIN, R.string.title_product_review))
            add(DFP({
                val uri = Uri.parse(it).buildUpon().build()
                (uri.host == ReviewApplinkConst.PATH_REVIEW && uri.pathSegments.size == ReviewApplinkConst.REVIEW_EXPECTED_PATH_SIZE)
            }, DF_MERCHANT_NONLOGIN, R.string.title_review_inbox))

            add(DFP({
                val uri = Uri.parse(it).buildUpon().build()
                (uri.host == ReviewApplinkConst.PATH_REVIEW && uri.pathSegments.size == ReviewApplinkConst.REVIEW_DETAIL_EXPECTED_PATH_SIZE)
            }, DF_MERCHANT_NONLOGIN, R.string.title_review_detail))

            add(DFP({
                val uri = Uri.parse(it).buildUpon().build()
                (uri.host == ReviewApplinkConst.PATH_PRODUCT_REVIEW && uri.pathSegments.last() == ReviewApplinkConst.PATH_CREATE)
            }, DF_MERCHANT_NONLOGIN, R.string.title_create_review))

            add(DFP({
                it.startsWithPattern(ApplinkConstInternalMarketplace.SHOP_REVIEW_FULL_PAGE) ||
                        it.startsWithPattern(ApplinkConstInternalMarketplace.PRODUCT_REVIEW)
            }, DF_MERCHANT_NONLOGIN, R.string.title_product_review))

            add(DFP({
                it.startsWithPattern(ApplinkConstInternalMarketplace.INBOX_REPUTATION)
            }, DF_MERCHANT_NONLOGIN, R.string.title_review_inbox))

            add(DFP({
                it.startsWithPattern(ApplinkConstInternalMarketplace.CREATE_REVIEW)
            }, DF_MERCHANT_NONLOGIN, R.string.title_create_review))

            add(DFP({
                it.startsWithPattern(ApplinkConstInternalMarketplace.REVIEW_DETAIL)
            }, DF_MERCHANT_NONLOGIN, R.string.title_review_detail))

            add(DFP({
                it.startsWith(ApplinkConstInternalMarketplace.PRODUCT_DETAIL) ||
                        it.startsWith(ApplinkConstInternalMarketplace.PRODUCT_DETAIL_WITH_WAREHOUSE_ID) ||
                        it.startsWith(ApplinkConstInternalMarketplace.PRODUCT_DETAIL_DOMAIN) ||
                        it.startsWith(ApplinkConstInternalMarketplace.PRODUCT_DETAIL_DOMAIN_WITH_AFFILIATE)
            }, DF_BASE, R.string.title_product_detail))

            add(DFP({
                it.startsWithPattern(ApplinkConstInternalMarketplace.PRODUCT_AR)
            }, DF_MERCHANT_PRODUCT_AR, R.string.title_product_ar))

            // Operational
            add(DFP({
                it.startsWith(CONTACT_US_NATIVE) || it.startsWith(CONTACT_US) || it.startsWithPattern(TICKET_DETAIL) ||
                        it.startsWith(INTERNAL_INBOX_LIST)
            }, DF_OPERATIONAL_CONTACT_US, R.string.applink_title_contact_us, { DFWebviewFallbackUrl.OPERATIONAL_CONTACT_US }))
            add(DFP({ it.startsWith(CHAT_BOT) }, DF_OPERATIONAL_CONTACT_US, R.string.title_applink_chatbot, { DFWebviewFallbackUrl.OPERATIONAL_CHAT_BOT }))
            add(DFP({ it.startsWith(ApplinkConstInternalUserPlatform.TELEPHONY_MASKING) }, DF_OPERATIONAL_CONTACT_US, R.string.applink_telephony))
            add(DFP({ it.startsWithPattern(ApplinkConstInternalOperational.SUCCESS_RESO) }, DF_OPERATIONAL_CONTACT_US, R.string.applink_title_resolution))


            // Payment
            add(DFP({ it.startsWith(PAYMENT_SETTING) }, DF_BASE, R.string.payment_settings_title))
            add(DFP({ it.startsWith(ApplinkConstInternalPayment.PMS_PAYMENT_LIST) }, DF_BASE, R.string.payment_title_payment_status))
            add(DFP({ it.startsWith(ApplinkConstInternalPayment.GOPAY_KYC) }, DF_BASE, R.string.title_gopay_kyc))
            add(DFP({ it.startsWith(ApplinkConstInternalPayment.INTERNAL_HOW_TO_PAY) }, DF_BASE, R.string.payment_title_activity_howtopay))

            add(DFP({ it.startsWith(ApplinkConstInternalGlobal.WITHDRAW) }, DF_BASE, R.string.payment_title_withdraw))
            add(DFP({ it.startsWith(ApplinkConstInternalGlobal.AUTO_WITHDRAW_SETTING) }, DF_BASE, R.string.payment_title_auto_withdraw))

            // Promo
            add(DFP({ it.startsWith(INTERNAL_TOKOPOINTS) }, DF_PROMO_TOKOPOINTS, R.string.title_tokopoints, { DFWebviewFallbackUrl.PROMO_TOKOPOINTS }))

            add(DFP({
                it.startsWith(ApplinkConstInternalPromo.INTERNAL_GAMIFICATION_CRACK) ||
                        it.startsWith(ApplinkConstInternalPromo.INTERNAL_GAMIFICATION_TAP_TAP_MANTAP) ||
                        it.startsWith(ApplinkConstInternalPromo.INTERNAL_GAMIFICATION_SMC_REFERRAL) ||
                        it.startsWith(ApplinkConstInternalPromo.INTERNAL_GAMIFICATION_DAILY_GIFT) ||
                        it.startsWith(ApplinkConstInternalPromo.INTERNAL_GAMIFICATION_TAP_TAP_GIFT)
            }, DF_PROMO_GAMIFICATION, R.string.internet_title_gamification))

            add(DFP({
                it.startsWith(ApplinkConstInternalPromo.PROMO_LIST_DIGITAL) ||
                        it.startsWith(ApplinkConstInternalPromo.PROMO_DETAIL_DIGITAL) ||
                        it.startsWith(ApplinkConstInternalPromo.PROMO_LIST_FLIGHT) ||
                        it.startsWith(ApplinkConstInternalPromo.PROMO_DETAIL_FLIGHT) ||
                        it.startsWith(ApplinkConstInternalPromo.PROMO_LIST_DEALS) ||
                        it.startsWith(ApplinkConstInternalPromo.PROMO_DETAIL_DEALS) ||
                        it.startsWith(ApplinkConstInternalPromo.PROMO_LIST_HOTEL) ||
                        it.startsWith(ApplinkConstInternalPromo.PROMO_DETAIL_HOTEL) ||
                        it.startsWith(ApplinkConstInternalPromo.PROMO_LIST_UMROH) ||
                        it.startsWith(ApplinkConstInternalPromo.PROMO_DETAIL_UMROH) ||
                        it.startsWith(ApplinkConstInternalPromo.PROMO_LIST_EVENT) ||
                        it.startsWith(ApplinkConstInternalPromo.PROMO_DETAIL_EVENT)
            }, DF_PROMO_CHECKOUT, R.string.title_promo_checkout))

            //Entertainment
            add(DFP({
                it.startsWith(EVENT_HOME) ||
                        it.startsWith(EVENT_PDP)
            }, DF_ENTERTAINMENT, R.string.title_entertainment, { DFWebviewFallbackUrl.ENTERTAINMENT_EVENT }))

            add(DFP({
                it.startsWith(DEALS_HOMEPAGE) ||
                        it.startsWith(DEALS_BRAND_PAGE) ||
                        it.startsWith(DEALS_CATEGORY_PAGE) ||
                        it.startsWith(GLOBAL_INTERNAL_DIGITAL_DEAL_SLUG_BASE) ||
                        it.startsWith(DEALS_BRAND_DETAIL_PAGE)
            }, DF_ENTERTAINMENT, R.string.title_entertainment, { DFWebviewFallbackUrl.ENTERTAINMENT_DEALS }))

            // Salam
            add(DFP({ it.startsWith(SALAM_UMRAH_HOME_PAGE) }, DF_SALAM_UMRAH, R.string.title_salam, { DFWebviewFallbackUrl.SALAM_UMRAH }))
            add(DFP({ it.startsWith(SALAM_ORDER_DETAIL) }, DF_SALAM_UMRAH, R.string.title_salam, { DFWebviewFallbackUrl.SALAM_UMRAH }))

            // Travel
            add(DFP({ it.startsWith(TRAVEL_SUBHOMEPAGE) }, DF_BASE, R.string.title_travel_homepage))
            add(DFP({ it.startsWith(FLIGHT) || it.startsWith(INTERNAL_FLIGHT) }, DF_TRAVEL, R.string.title_flight, { DFWebviewFallbackUrl.TRAVEL_FLIGHT }))
            add(DFP({ it.startsWith(HOTEL) || it.startsWith(INTERNAL_HOTEL)}, DF_TRAVEL, R.string.title_hotel, {DFWebviewFallbackUrl.TRAVEL_HOTEL}))

            // User
            add(DFP({ it.startsWith(PROMO_CAMPAIGN_SHAKE_LANDING_PREFIX) }, DF_BASE, R.string.title_applink_campaign_shake_landing))

            add(DFP({
                (it.startsWith(SETTING_PROFILE)
                        || it.startsWith(ADD_PHONE)
                        || it.startsWith(NEW_ADD_PHONE)
                        || it.startsWith(ADD_EMAIL)
                        || it.startsWith(ADD_BOD)
                        || it.startsWithPattern(CHANGE_NAME)
                        || it.startsWith(CHANGE_GENDER)
                        || it.startsWith(ADD_NAME_REGISTER)
                        || it.startsWith(CHANGE_PIN)
                        || it.startsWith(ApplinkConstInternalUserPlatform.ADD_PIN_ONBOARDING)
                        || it.startsWith(ADD_PIN)
                        || it.startsWith(ApplinkConstInternalUserPlatform.ADD_PIN_COMPLETE)
                        || it.startsWith(ApplinkConstInternalUserPlatform.SETTING_PROFILE)
                        )
            }, DF_USER_SETTINGS, R.string.applink_profile_completion_title, { DFWebviewFallbackUrl.USER_PROFILE_SETTINGS }))
            add(DFP({ it.startsWith(ApplinkConstInternalUserPlatform.PROFILE_COMPLETION) }, DF_USER_SETTINGS, R.string.applink_profile_completion_title))

            add(DFP({ it.startsWith(CHANGE_PHONE_NUMBER) }, DF_BASE, R.string.applink_change_phone_number))
            add(DFP({ it.startsWith(HAS_PASSWORD) }, DF_BASE, R.string.applink_change_password))
            add(DFP({ it.startsWith(FORGOT_PASSWORD) }, DF_BASE, R.string.applink_change_password))
            add(DFP({ it.startsWith(SETTING_BANK) }, DF_USER_SETTINGS, R.string.applink_setting_bank_title, { DFWebviewFallbackUrl.USER_SETTING_BANK }))
            add(DFP({ it.startsWith(USER_NOTIFICATION_SETTING) }, DF_BASE, R.string.notif_settings_title))
            add(DFP({ it.startsWithPattern(KYC_INFO) }, DF_USER_SETTINGS, R.string.user_identification_common_title))
            add(DFP({ it.startsWithPattern(KYC_FORM) }, DF_USER_SETTINGS, R.string.user_identification_form_title))
            add(DFP({ it.startsWithPattern(KYC_ALA_CARTE) }, DF_USER_SETTINGS, R.string.user_identification_info_simple))
            add(DFP({ it.startsWith(ORDER_HISTORY) || it.startsWithPattern(ApplinkConstInternalMarketplace.ORDER_HISTORY) }, DF_MERCHANT_LOGIN, R.string.title_module_attachvoucher))
            add(DFP({
                it.startsWith(TOPCHAT_IDLESS) || it.startsWith(ApplinkConstInternalMarketplace.TOPCHAT)
            }, DF_BASE, R.string.title_topchat))
            add(DFP({ it.startsWithPattern(INBOX) }, DF_BASE, R.string.title_inbox))

            add(DFP({ it.startsWith(INBOX_TALK) }, DF_BASE, R.string.talk_title))
            add(DFP({ it.startsWith(SHOP_TALK) }, DF_BASE, R.string.talk_title))
            add(DFP({ it.startsWithPattern(PRODUCT_TALK) }, DF_BASE, R.string.talk_title))
            add(DFP({ it.startsWith(DETAIL_TALK_BASE) }, DF_BASE, R.string.talk_title))
            add(DFP({ it.startsWith(ADD_TALK) }, DF_BASE, R.string.talk_title))

            add(DFP({ it.startsWith(ADD_FINGERPRINT_ONBOARDING) }, DF_BASE, R.string.fingerprint_onboarding))
            add(DFP({ it.startsWith(KYC_LIVENESS_BASE) }, DF_USER_LIVENESS, R.string.applink_liveness_detection))
            add(DFP({
                    it.startsWith(ApplinkConstInternalUserPlatform.VERIFY_BIOMETRIC) ||
                    it.startsWith(ApplinkConstInternalUserPlatform.BIOMETRIC_SETTING)
                    }, DF_BASE, R.string.applink_fingerprint))

            add(DFP({ it.startsWith(NOTIFICATION) }, DF_BASE, R.string.title_notification_center))
            add(DFP({ it.startsWith(NOTIFICATION_BUYER) }, DF_BASE, R.string.title_notification_center))
            add(DFP({ it.startsWith(PUSH_NOTIFICATION_TROUBLESHOOTER) }, DF_BASE, R.string.applink_notif_troubleshooter))

            add(DFP({ it.startsWith(OTP) }, DF_BASE, R.string.title_otp))
            add(DFP({ it.startsWith(CHOOSE_ACCOUNT) }, DF_BASE, R.string.title_choose_account))
            add(DFP({ it.startsWith(CHANGE_INACTIVE_PHONE) }, DF_BASE, R.string.title_update_inactive_phone))
            add(DFP({
                it.startsWithPattern(PRIVACY_CENTER) ||
                it.startsWithPattern(CONSENT_WITHDRAWAL_NEW) ||
                it.startsWithPattern(ACCOUNT_LINKING_WEBVIEW) ||
                it.startsWithPattern(SEARCH_HISTORY) ||
                it.startsWithPattern(SHARING_WISHLIST)
            }, DF_USER_PRIVACYCENTER, R.string.title_privacy_center))

            // Media
            add(DFP({
                it.startsWith(INTERNAL_MEDIA_PICKER) ||
                        it.startsWith(INTERNAL_MEDIA_PICKER_ALBUM) ||
                        it.startsWith(INTERNAL_MEDIA_PICKER_PREVIEW)
            }, DF_FEED_CONTENT_CREATION, R.string.title_image_picker))

            add(DFP({
                it.startsWith(INTERNAL_MEDIA_EDITOR)
            }, DF_FEED_CONTENT_CREATION, R.string.title_image_editor))

            // Transaction
            add(DFP({ it.startsWith(CHECKOUT) }, DF_BASE, R.string.checkout_module_title_activity_checkout))
            add(DFP({ it.startsWith(CHECKOUT_ADDRESS_SELECTION) }, DF_BASE, R.string.checkout_module_title_activity_shipping_address))
            add(DFP({ it.startsWith(ONE_CLICK_CHECKOUT) }, DF_BASE, R.string.title_one_click_checkout))
            add(DFP({ it.startsWith(PROMO_CHECKOUT_MARKETPLACE) }, DF_BASE, R.string.promo_checkout_marketplace_module_title_activity_promo_list))
            add(DFP({ it.startsWith(WISHLIST_V2) }, DF_BASE, R.string.title_wishlist))
            add(DFP({ it.startsWith(ADD_ON_GIFTING) }, DF_BASE, R.string.add_on_gifting_module_title_activity_add_on_selection))
            add(DFP({ it.startsWith(WISHLIST_COLLECTION) }, DF_BASE, R.string.title_collection_wishlist))

            // buyerorder
            add(DFP({
                it.startsWith(INTERNAL_ORDER) ||
                        it.startsWith(INTERNAL_BUYER) ||
                        it.startsWith(ORDER_LIST_INTERNAL) ||
                        it.startsWith(INTERNAL_TRANSACTION_ORDERLIST) ||
                        it.startsWith(ApplinkConstInternalOrder.DIGITAL_ORDER) ||
                        it.startsWith(DIGITAL_ORDER_LIST_INTERNAL) ||
                        it.startsWith(ORDERLIST_DIGITAL_INTERNAL) ||
                        it.startsWith(DEALS_INTERNAL_ORDER) ||
                        it.startsWith(EVENTS_INTERNAL_ORDER) ||
                        it.startsWith(GIFTCARDS_INTERNAL_ORDER) ||
                        it.startsWith(INSURANCE_INTERNAL_ORDER) ||
                        it.startsWith(MODALTOKO_INTERNAL_ORDER) ||
                        it.startsWith(HOTEL_INTERNAL_ORDER) ||
                        it.startsWith(PESAWAT_INTERNAL_ORDER) ||
                        it.startsWith(BELANJA_INTERNAL_ORDER)
            }, DF_BASE, R.string.title_buyerorder))

            // snapshot
            add(DFP({ it.startsWith(INTERNAL_ORDER_SNAPSHOT) }, DF_BASE, R.string.title_snapshot))

            //buyercancelllationorder
            add(DFP({ it.startsWith(INTERNAL_ORDER_BUYER_CANCELLATION_REQUEST_PAGE) }, DF_BASE, R.string.title_buyer_request_cancel))

            // Order History
            add(DFP({ it.startsWith(TRACK) }, DF_BASE, R.string.title_order_management_history))

            // Revamped buyer order detail (features/ordermanagement/buyer_order_detail)
            add(DFP({ it.startsWith(MARKETPLACE_INTERNAL_BUYER_ORDER_DETAIL) ||
                    it.startsWith(BUYER_ORDER_EXTENSION) ||
                    it.startsWith(ApplinkConstInternalOrder.MARKETPLACE_INTERNAL_BUYER_ORDER_EXTENSION)
                    }, DF_BASE, R.string.title_revamped_buyer_order_detail)
            )

            // Shop Admin
            add(DFP({ it.startsWith(ApplinkConstInternalMarketplace.ADMIN_ACCEPTED) ||
                    it.startsWith(ApplinkConstInternalMarketplace.ADMIN_INVITATION) ||
                    it.startsWith(ApplinkConstInternalMarketplace.ADMIN_REDIRECTION) ||
                    it.startsWith(ApplinkConst.ADMIN_INVITATION) ||
                    it.startsWith(ApplinkConst.ADMIN_ACCEPTED) ||
                    it.startsWith(ApplinkConst.ADMIN_REDIRECTION)
                }, DF_MERCHANT_SELLER, R.string.title_shop_admin)
            )


            // Tokopedia NOW!
            add(DFP({
                it.startsWith(TokopediaNow.HOME) ||
                    it.startsWith(TokopediaNow.CATEGORY) ||
                    it.startsWith(TokopediaNow.SEARCH) ||
                    it.startsWith(TokopediaNow.REPURCHASE) ||
                    it.startsWithPattern(TokopediaNow.RECIPE_DETAIL) ||
                    it.startsWith(TokopediaNow.RECIPE_BOOKMARK) ||
                    it.startsWith(TokopediaNow.RECIPE_HOME) ||
                    it.startsWith(TokopediaNow.RECIPE_SEARCH) ||
                    it.startsWith(TokopediaNow.RECIPE_AUTO_COMPLETE) ||
                    it.startsWith(ApplinkConstInternalTokopediaNow.HOME) ||
                    it.startsWith(ApplinkConstInternalTokopediaNow.CATEGORY) ||
                    it.startsWith(ApplinkConstInternalTokopediaNow.SEARCH) ||
                    it.startsWith(ApplinkConstInternalTokopediaNow.REPURCHASE) ||
                    it.startsWithPattern(ApplinkConstInternalTokopediaNow.RECIPE_DETAIL) ||
                    it.startsWith(ApplinkConstInternalTokopediaNow.RECIPE_BOOKMARK) ||
                    it.startsWith(ApplinkConstInternalTokopediaNow.RECIPE_HOME) ||
                    it.startsWith(ApplinkConstInternalTokopediaNow.RECIPE_SEARCH) ||
                    it.startsWith(ApplinkConstInternalTokopediaNow.RECIPE_AUTO_COMPLETE)
            }, DF_TOKOPEDIA_NOW, R.string.title_tokopedia_now))

            // Tokofood
            add(DFP({
                it.startsWith(ApplinkConstInternalTokoFood.INTERNAL_TOKO_FOOD)
            }, DF_TOKOFOOD, R.string.title_tokofood))

            // Review Reminder
            add(DFP({ it.startsWith(REVIEW_REMINDER_PREVIOUS) }, DF_MERCHANT_NONLOGIN, R.string.title_review_reminder))

            // Review Credibility
            add(DFP({ it.startsWith(ApplinkConstInternalMarketplace.REVIEW_CREDIBILITY)}, DF_MERCHANT_NONLOGIN, R.string.title_review_credibility))

            // Review Media Gallery
            add(DFP({ it.startsWith(ApplinkConstInternalMarketplace.REVIEW_MEDIA_GALLERY)}, DF_MERCHANT_NONLOGIN, R.string.title_review_media_gallery))

            //Feedback Form
            add(DFP({ it.startsWith(ApplinkConstInternalGlobal.FEEDBACK_FORM) ||
                    it == ApplinkConstInternalGlobal.FEEDBACK_FORM }, DF_ALPHA_TESTING, R.string.internal_feedback))

            //Toko Chat
            add(DFP({ it.startsWithPattern(ApplinkConstInternalCommunication.TOKO_CHAT) }, DF_TOKOCHAT, R.string.title_applink_toko_chat))
        }
    }

    val deeplinkDFPatternListSellerApp: List<DFP> by lazy {
        mutableListOf<DFP>().apply {
            add(DFP({ it.startsWith(SHOP_SETTINGS_BASE) }, DF_BASE_SELLER_APP, R.string.shop_settings_title))
            add(DFP({ it.startsWith(PAYMENT_SETTING) }, DF_BASE_SELLER_APP, R.string.payment_settings_title))
            add(DFP({ it.startsWith(ApplinkConstInternalGlobal.WITHDRAW) }, DF_BASE_SELLER_APP, R.string.payment_title_withdraw))
            add(DFP({ it.startsWith(ApplinkConstInternalGlobal.AUTO_WITHDRAW_SETTING) }, DF_BASE_SELLER_APP, R.string.payment_title_auto_withdraw))

            add(DFP({ it.startsWith(PRODUCT_MANAGE_LIST)
                    || it.startsWith(RESERVED_STOCK_BASE) }, DF_BASE_SELLER_APP, R.string.title_applink_product_manage))
            add(DFP({ it.startsWithPattern(KYC_FORM) }, DF_BASE_SELLER_APP, R.string.user_identification_common_title))
            add(DFP({ it.startsWithPattern(KYC_ALA_CARTE) }, DF_BASE_SELLER_APP, R.string.user_identification_info_simple))
            add(DFP({
                it.startsWith(TOPADS_DASHBOARD_SELLER) ||
                        it.startsWith(TOPADS_DASHBOARD_INTERNAL)
            }, DF_BASE_SELLER_APP, R.string.applink_topads_dashboard_title))
            add(DFP({ it.startsWith(MERCHANT_SHOP_SHOWCASE_LIST) }, DF_BASE_SELLER_APP, R.string.merchant_seller))
            add(DFP({ it.startsWith(MERCHANT_SHOP_SCORE)
                    || it.startsWith(SHOP_SCORE_DETAIL)
                    || it.startsWith(SellerApp.SHOP_SCORE_DETAIL)
                    || it.startsWith(ApplinkConstInternalMarketplace.SHOP_PERFORMANCE)
                    || it.startsWith(SHOP_PENALTY)
                    || it.startsWith(SHOP_PENALTY_DETAIL)
                    || it.startsWith(ApplinkConstInternalMarketplace.SHOP_PENALTY)
                    || it.startsWith(ApplinkConstInternalMarketplace.SHOP_PENALTY_DETAIL)
            }, DF_SHOP_SCORE, R.string.title_shop_score_sellerapp))
            add(DFP({
                it.startsWith(CREATE_VOUCHER) ||
                        it.startsWith(VOUCHER_LIST) ||
                        it.startsWith(VOUCHER_DETAIL)
            }, DF_BASE_SELLER_APP, R.string.title_voucher_creation))
            add(DFP({
                it.startsWith(CREATE_VOUCHER_PRODUCT) ||
                        it.startsWith(VOUCHER_PRODUCT_DETAIL)
            }, DF_BASE_SELLER_APP, R.string.title_voucher_creation))
            add(DFP({ it.startsWith(MERCHANT_OPEN_PRODUCT_PREVIEW) || it.startsWith(PRODUCT_ADD) }, DF_BASE_SELLER_APP, R.string.title_product_add_edit))
            add(DFP({ it.startsWith(WELCOME) }, DF_BASE_SELLER_APP, R.string.title_seller_onboarding))
            add(DFP({ it.startsWith(SELLER_SEARCH) || it.startsWith(ApplinkConstInternalSellerapp.SELLER_SEARCH) }, DF_BASE_SELLER_APP, R.string.title_global_search_seller))
            add(DFP({ it.startsWith(SELLER_SHOP_FLASH_SALE) }, DF_BASE_SELLER_APP, R.string.title_shop_flash_sale))
            add(DFP({ it.startsWith(SELLER_TOKOPEDIA_FLASH_SALE)}, DF_BASE_SELLER_APP, R.string.title_tokopedia_flash_sale))
            add(DFP({ it.startsWith(SELLER_TOKOPEDIA_FLASH_SALE_CAMPAIGN_DETAIL)}, DF_BASE_SELLER_APP, R.string.title_tokopedia_flash_sale_campaign_detail))

            // Content
            add(DFP({ it.startsWithPattern(COMMENT) }, DF_BASE_SELLER_APP, R.string.applink_kol_title_comment))
            add(DFP({ it.startsWithPattern(INTERNAL_CONTENT_POST_DETAIL) }, DF_BASE_SELLER_APP, R.string.applink_kol_title_post_detail))
            add(DFP({ it.startsWithPattern(KOL_YOUTUBE) }, DF_BASE_SELLER_APP, R.string.applink_kol_title_post_youtube))
            add(DFP({ it.startsWithPattern(CONTENT_REPORT) }, DF_BASE_SELLER_APP, R.string.applink_kol_title_content_report))
            add(DFP({ it.startsWithPattern(VIDEO_DETAIL) }, DF_BASE_SELLER_APP, R.string.applink_kol_title_video_detail))
            add(DFP({ it.startsWithPattern(MEDIA_PREVIEW) }, DF_BASE_SELLER_APP, R.string.applink_kol_title_media_preview))
            add(DFP({ it.startsWithPattern(PLAY_BROADCASTER) }, DF_BASE_SELLER_APP, R.string.applink_title_play_broadcaster))

            // Logistic
            add(DFP({ it.startsWith(SELLER_COD_ACTIVATION) }, DF_BASE_SELLER_APP, R.string.path_shop_setting_cod_activation))
            add(DFP({ it.startsWith(SELLER_WAREHOUSE_DATA) }, DF_BASE_SELLER_APP, R.string.path_shop_settings_address))


            add(DFP({
                it.startsWithPattern(SHOP_PAGE_BASE) ||
                        it.startsWithPattern(SHOP) ||
                        it.startsWithPattern(SHOP_ETALASE) ||
                        it.startsWithPattern(SHOP_ETALASE_WITH_KEYWORD_AND_SORT) ||
                        it.startsWithPattern(SHOP_REVIEW) ||
                        it.startsWithPattern(SHOP_NOTE) ||
                        it.startsWithPattern(SHOP_INFO) ||
                        it.startsWithPattern(SHOP_HOME) ||
                        it.startsWith(SHOP_SETTINGS_NOTE)
            }, DF_BASE_SELLER_APP, R.string.title_shop_page))
            add(DFP({ it.startsWithPattern(SHOP_OPERATIONAL_HOUR_BOTTOM_SHEET) }, DF_BASE_SELLER_APP, R.string.title_shop_widget))
            add(DFP({
                val regexPatternToReplace = "(?=\\{)[^\\}]+\\}".toRegex()
                val cleanExternalAppLinkPattern = SHOP_MVC_LOCKED_TO_PRODUCT.replace(regexPatternToReplace,".*")
                val regexMatcherExternalAppLink = cleanExternalAppLinkPattern.toRegex()
                regexMatcherExternalAppLink.matches(it)
            }, DF_BASE_SELLER_APP, R.string.title_shop_widget))
            add(DFP({ it.startsWith(MERCHANT_STATISTIC_DASHBOARD) }, DF_BASE_SELLER_APP, R.string.title_statistic))

            add(DFP({
                val uri = Uri.parse(it).buildUpon().build()
                (uri.host == ReviewApplinkConst.AUTHORITY_PRODUCT && uri.pathSegments.last() == ReviewApplinkConst.PATH_REVIEW)
            }, DF_BASE_SELLER_APP, R.string.title_product_review))
            add(DFP({
                val uri = Uri.parse(it).buildUpon().build()
                (uri.host == ReviewApplinkConst.PATH_REVIEW && uri.pathSegments.size == ReviewApplinkConst.REVIEW_EXPECTED_PATH_SIZE)
            }, DF_BASE_SELLER_APP, R.string.title_review_inbox))

            add(DFP({
                val uri = Uri.parse(it).buildUpon().build()
                (uri.host == ReviewApplinkConst.PATH_REVIEW && uri.pathSegments.size == ReviewApplinkConst.REVIEW_DETAIL_EXPECTED_PATH_SIZE)
            }, DF_BASE_SELLER_APP, R.string.title_review_detail))

            add(DFP({
                val uri = Uri.parse(it).buildUpon().build()
                (uri.host == ReviewApplinkConst.PATH_PRODUCT_REVIEW && uri.pathSegments.last() == ReviewApplinkConst.PATH_CREATE)
            }, DF_BASE_SELLER_APP, R.string.title_create_review))

            add(DFP({
                it.startsWith(ApplinkConstInternalMarketplace.PRODUCT_DETAIL) ||
                        it.startsWith(ApplinkConstInternalMarketplace.PRODUCT_DETAIL_WITH_WAREHOUSE_ID) ||
                        it.startsWith(ApplinkConstInternalMarketplace.PRODUCT_DETAIL_DOMAIN) ||
                        it.startsWith(ApplinkConstInternalMarketplace.PRODUCT_DETAIL_DOMAIN_WITH_AFFILIATE)
            }, DF_BASE_SELLER_APP, R.string.title_product_detail))
            // User
            add(DFP({ it.startsWithPattern(CHANGE_INACTIVE_PHONE) }, DF_BASE, R.string.title_update_inactive_phone))

            // Order History
            add(DFP({ it.startsWith(TRACK) }, DF_BASE_SELLER_APP, R.string.title_order_management_history))

            // Review Reminder
            add(DFP({ it.startsWith(REVIEW_REMINDER) }, DF_BASE_SELLER_APP, R.string.title_review_reminder))

            // Review Credibility
            add(DFP({ it.startsWith(ApplinkConstInternalMarketplace.REVIEW_CREDIBILITY)}, DF_BASE_SELLER_APP, R.string.title_review_credibility))

            // Review Media Gallery
            add(DFP({ it.startsWith(ApplinkConstInternalMarketplace.REVIEW_MEDIA_GALLERY)}, DF_BASE_SELLER_APP, R.string.title_review_media_gallery))

            // Shop Admin
            add(DFP({ it.startsWith(ApplinkConstInternalMarketplace.ADMIN_ACCEPTED) ||
                    it.startsWith(ApplinkConstInternalMarketplace.ADMIN_INVITATION) ||
                    it.startsWith(ApplinkConstInternalMarketplace.ADMIN_REDIRECTION) ||
                    it.startsWith(SellerApp.ADMIN_INVITATION) ||
                    it.startsWith(SellerApp.ADMIN_ACCEPTED) ||
                    it.startsWith(SellerApp.ADMIN_REDIRECTION)
            }, DF_BASE_SELLER_APP, R.string.title_shop_admin))

            // Tokomember dashboard
            add(DFP({ it.startsWith(TOKOMEMBER) }, DF_BASE_SELLER_APP, R.string.title_tokomember))
            add(DFP({ it.startsWith(ApplinkConstInternalSellerapp.TOKOMEMBER_PROGRAM_LIST) }, DF_BASE_SELLER_APP, R.string.title_tokomember))
            add(DFP({ it.startsWith(ApplinkConstInternalSellerapp.TOKOMEMBER_COUPON_LIST) }, DF_BASE_SELLER_APP, R.string.title_tokomember))
            add(DFP({ it.startsWith(ApplinkConstInternalSellerapp.TOKOMEMBER_PROGRAM_CREATION) }, DF_BASE_SELLER_APP, R.string.title_tokomember))
            add(DFP({ it.startsWith(ApplinkConstInternalSellerapp.TOKOMEMBER_COUPON_CREATION) }, DF_BASE_SELLER_APP, R.string.title_tokomember))
            add(DFP({ it.startsWith(ApplinkConstInternalSellerapp.TOKOMEMBER_PROGRAM_EXTENSION) }, DF_BASE_SELLER_APP, R.string.title_tokomember))

            // Media
            add(DFP({
                it.startsWith(INTERNAL_MEDIA_EDITOR)
            }, DF_FEED_CONTENT_CREATION, R.string.title_image_editor))
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
            return if (hasInstalled) {
                trackDFUsageOnce(context.applicationContext, moduleId, deeplink)
                null
            } else {
                UriUtil.buildUri(
                        DYNAMIC_FEATURE_INSTALL,
                        moduleId,
                        moduleName,
                        Uri.encode(deeplink).toString(),
                        fallbackUrl)
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

    /**
     * Supplied with list of DFP, it will return the list of turned on DFP by the config
     * Example:
     * Input:   original DFP list = {DFP(shop), DFP(hotel), DFP(flight)}
     *          cfg -> setOf(hotel)
     * Output:  DFP list {DFP(hotel)}
     */
    private fun getTurnOnDeeplinkDFPattern(context: Context, dfpList: List<DFP>?): List<DFP> {
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
        return try {
            val set: HashSet<String> = hashSetOf()
            val reader = BufferedReader(InputStreamReader(context.assets.open("df.cfg")))
            var line: String? = reader.readLine()
            while (line != null) {
                if (line.isNotEmpty()) {
                    set.add(line)
                }
                line = reader.readLine()
            }
            set
        } catch (e: FileNotFoundException) {
            null
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + CoroutineExceptionHandler { _, _ -> }

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
