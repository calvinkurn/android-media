package com.tokopedia.applink

import android.content.Context
import android.net.Uri
import android.os.Build
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.tokopedia.applink.ApplinkConst.*
import com.tokopedia.applink.ApplinkConst.SellerApp.SELLER_SEARCH
import com.tokopedia.applink.internal.*
import com.tokopedia.applink.internal.ApplinkConsInternalDigital.CAMERA_OCR
import com.tokopedia.applink.internal.ApplinkConsInternalDigital.CART_DIGITAL
import com.tokopedia.applink.internal.ApplinkConsInternalDigital.CHECKOUT_DIGITAL
import com.tokopedia.applink.internal.ApplinkConsInternalDigital.DIGITAL_PRODUCT_FORM
import com.tokopedia.applink.internal.ApplinkConsInternalDigital.GENERAL_TEMPLATE
import com.tokopedia.applink.internal.ApplinkConsInternalDigital.INTERNAL_SMARTCARD_BRIZZI
import com.tokopedia.applink.internal.ApplinkConsInternalDigital.INTERNAL_SMARTCARD_EMONEY
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
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.INTERNAL_FIND
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.INTERNAL_HOTLIST_REVAMP
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.MONEYIN_INTERNAL
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.TRADEIN
import com.tokopedia.applink.internal.ApplinkConstInternalContent.AFFILIATE_EDIT
import com.tokopedia.applink.internal.ApplinkConstInternalContent.COMMENT
import com.tokopedia.applink.internal.ApplinkConstInternalContent.CONTENT_REPORT
import com.tokopedia.applink.internal.ApplinkConstInternalContent.INTERNAL_AFFILIATE_CREATE_POST
import com.tokopedia.applink.internal.ApplinkConstInternalContent.INTERNAL_AFFILIATE_DRAFT_POST
import com.tokopedia.applink.internal.ApplinkConstInternalContent.INTERNAL_CONTENT_POST_DETAIL
import com.tokopedia.applink.internal.ApplinkConstInternalContent.MEDIA_PREVIEW
import com.tokopedia.applink.internal.ApplinkConstInternalContent.VIDEO_DETAIL
import com.tokopedia.applink.internal.ApplinkConstInternalDeals.DEALS_BRAND_PAGE
import com.tokopedia.applink.internal.ApplinkConstInternalDeals.DEALS_CATEGORY_PAGE
import com.tokopedia.applink.internal.ApplinkConstInternalDeals.DEALS_HOMEPAGE
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery.AUTOCOMPLETE
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery.SEARCH_RESULT
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery.SIMILAR_SEARCH_RESULT_BASE
import com.tokopedia.applink.internal.ApplinkConstInternalEntertainment.EVENT_HOME
import com.tokopedia.applink.internal.ApplinkConstInternalEntertainment.EVENT_PDP
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.ADD_BOD
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.ADD_EMAIL
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.ADD_NAME_REGISTER
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.ADD_NAME_REGISTER_CLEAN_VIEW
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
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.CHOOSE_ACCOUNT
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.DETAIL_TALK_BASE
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.DYNAMIC_FEATURE_INSTALL
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.DYNAMIC_FEATURE_INSTALL_BASE
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.EDIT_BCA_ONE_KLICK_ENTRY_PATTERN
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.GLOBAL_INTERNAL_DIGITAL_DEAL_BRAND_DETAIL_BASE
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.GLOBAL_INTERNAL_DIGITAL_DEAL_SLUG_BASE
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.INBOX_TALK
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.INSTANT_DEBIT_BCA_ENTRY_PATTERN
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.LIVENESS_DETECTION
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.OQR_PIN_URL_ENTRY
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.OVO_PAY_WITH_QR_ENTRY
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PUSH_NOTIFICATION_TROUBLESHOOTER
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.SALDO_DEPOSIT
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.SALDO_INTRO
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.SETTING_BANK
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.SETTING_PROFILE
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.USER_IDENTIFICATION_FORM
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.ADD_ADDRESS_V1
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.ADD_ADDRESS_V2
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.DROPOFF_PICKER
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.MANAGE_ADDRESS
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.SHIPPING_CONFIRMATION
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.SHOP_EDIT_ADDRESS
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
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.PREFERENCE_EDIT
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.PREFERENCE_LIST
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.REPORT_PRODUCT
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.SHOP_PAGE_BASE
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.SHOP_SETTINGS_BASE
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.USER_NOTIFICATION_SETTING
import com.tokopedia.applink.internal.ApplinkConstInternalMechant.BRANDLIST
import com.tokopedia.applink.internal.ApplinkConstInternalMechant.BRANDLIST_SEARCH
import com.tokopedia.applink.internal.ApplinkConstInternalMechant.MERCHANT_OPEN_PRODUCT_PREVIEW
import com.tokopedia.applink.internal.ApplinkConstInternalMechant.MERCHANT_PRODUCT_DRAFT
import com.tokopedia.applink.internal.ApplinkConstInternalMechant.MERCHANT_SHOP_SCORE
import com.tokopedia.applink.internal.ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST
import com.tokopedia.applink.internal.ApplinkConstInternalMechant.MERCHANT_STATISTIC_DASHBOARD
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
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.INTERNAL_ORDER_SNAPSHOT
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.INTERNAL_SELLER
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.INTERNAL_TRANSACTION_ORDERLIST
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.MARKETPLACE_INTERNAL_ORDER
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.MODALTOKO_INTERNAL_ORDER
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.ORDERLIST_DIGITAL_INTERNAL
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.ORDER_LIST_INTERNAL
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PESAWAT_INTERNAL_ORDER
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.TRACK
import com.tokopedia.applink.internal.ApplinkConstInternalPayment.PAYMENT_SETTING
import com.tokopedia.applink.internal.ApplinkConstInternalPromo.INTERNAL_TOKOPOINTS
import com.tokopedia.applink.internal.ApplinkConstInternalPromo.PROMO_CAMPAIGN_SHAKE_LANDING_PREFIX
import com.tokopedia.applink.internal.ApplinkConstInternalPromo.PROMO_CHECKOUT_MARKETPLACE
import com.tokopedia.applink.internal.ApplinkConstInternalSalam.SALAM_ORDER_DETAIL
import com.tokopedia.applink.internal.ApplinkConstInternalSalam.SALAM_UMRAH_HOME_PAGE
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp.CREATE_VOUCHER
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp.SELLER_MENU
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp.VOUCHER_DETAIL
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp.VOUCHER_LIST
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp.WELCOME
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds.TOPADS_DASHBOARD_CUSTOMER
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds.TOPADS_DASHBOARD_INTERNAL
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds.TOPADS_DASHBOARD_SELLER
import com.tokopedia.applink.internal.ApplinkConstInternalTravel.INTERNAL_FLIGHT
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
    const val DF_CONTENT_AFFILIATE = "df_content_affiliate"
    const val DF_MERCHANT_SELLER = "df_merchant_seller"
    const val DF_OPERATIONAL_CONTACT_US = "df_operational_contact_us"
    const val DF_SALAM_UMRAH = "df_salam_umrah"
    const val DF_TRAVEL = "df_travel"
    const val DF_USER_LIVENESS = "df_user_liveness"
    const val DF_USER_SETTINGS = "df_user_settings"
    const val DF_PROMO_GAMIFICATION = "df_promo_gamification"
    const val DF_PROMO_TOKOPOINTS = "df_promo_tokopoints"
    const val DF_PROMO_CHECKOUT = "df_promo_checkout"
    const val DF_GAMIFICATION = "df_gamification"
    const val DF_SHOP_SCORE = "shop_score_sellerapp"
    const val DF_ENTERTAINMENT = "df_entertainment"
    const val DF_MERCHANT_LOGIN = "df_merchant_login"
    const val DF_CONTENT_PROFILE = "df_content_profile"

    const val SHARED_PREF_TRACK_DF_USAGE = "pref_track_df_usage"
    var dfUsageList = mutableListOf<String>()

    private var manager: SplitInstallManager? = null
    val deeplinkDFPatternListCustomerApp: List<DFP> by lazy {
        mutableListOf<DFP>().apply {
            // Base
            add(DFP({ it.startsWith(ONBOARDING) }, DF_BASE, R.string.applink_title_affiliate))
            // Category
            add(DFP({ it.startsWith(AGE_RESTRICTION) }, DF_BASE, R.string.applink_title_age_restriction))
            add(DFP({ it.startsWith(TRADEIN) }, DF_CATEGORY_TRADE_IN, R.string.applink_title_tradein))
            add(DFP({ it.startsWith(INTERNAL_BELANJA_CATEGORY) }, DF_BASE, R.string.label_kategori))
            add(DFP({ it.startsWith(INTERNAL_HOTLIST_REVAMP) }, DF_BASE, R.string.hotlist_label))
            add(DFP({ it.startsWith(FINAL_PRICE) }, DF_CATEGORY_TRADE_IN, R.string.applink_harga_final))
            add(DFP({ it.startsWith(MONEYIN_INTERNAL) }, DF_CATEGORY_TRADE_IN, R.string.money_in))
            add(DFP({ it.startsWith(INTERNAL_EXPLORE_CATEGORY) }, DF_BASE, R.string.applink_title_explore_category))
            add(DFP({ it.startsWith(INTERNAL_CATALOG) }, DF_BASE, R.string.applink_title_catalog))
            add(DFP({ it.startsWith(INTERNAL_FIND) }, DF_BASE, R.string.applink_title_find_native))
            add(DFP({ it.startsWith(INTERNAL_CATEGORY) }, DF_BASE, R.string.label_category))


            // Content
            add(DFP({ it.startsWithPattern(ApplinkConstInternalContent.PROFILE_DETAIL) }, DF_CONTENT_PROFILE, R.string.applink_title_profile))
            add(DFP({ it.startsWithPattern(ApplinkConstInternalContent.AFFILIATE_EXPLORE) }, DF_CONTENT_AFFILIATE, R.string.applink_title_affiliate))
            add(DFP({ it.startsWithPattern(ApplinkConstInternalContent.AFFILIATE_DASHBOARD) }, DF_CONTENT_AFFILIATE, R.string.applink_title_affiliate))
            add(DFP({ it.startsWithPattern(ApplinkConstInternalContent.AFFILIATE_EDUCATION) }, DF_CONTENT_AFFILIATE, R.string.applink_title_affiliate))
            add(DFP({ it.startsWithPattern(ApplinkConstInternalContent.AFFILIATE_BYME_TRACKING) }, DF_CONTENT_AFFILIATE, R.string.applink_title_affiliate))
            add(DFP({ it.startsWithPattern(PLAY_DETAIL) }, DF_BASE, R.string.applink_title_play))
            add(DFP({ it.startsWithPattern(COMMENT) }, DF_BASE, R.string.applink_kol_title_comment))
            add(DFP({ it.startsWithPattern(INTERNAL_CONTENT_POST_DETAIL) }, DF_BASE, R.string.applink_kol_title_post_detail))
            add(DFP({ it.startsWithPattern(KOL_YOUTUBE) }, DF_BASE, R.string.applink_kol_title_post_youtube))
            add(DFP({ it.startsWithPattern(CONTENT_REPORT) }, DF_BASE, R.string.applink_kol_title_content_report))
            add(DFP({ it.startsWithPattern(VIDEO_DETAIL) }, DF_BASE, R.string.applink_kol_title_video_detail))
            add(DFP({ it.startsWithPattern(MEDIA_PREVIEW) }, DF_BASE, R.string.applink_kol_title_media_preview))
            add(DFP({ it.startsWithPattern(INTEREST_PICK) }, DF_BASE, R.string.applink_ip_title))
            add(DFP({ it.startsWithPattern(INTERNAL_AFFILIATE_CREATE_POST) }, DF_CONTENT_AFFILIATE, R.string.applink_af_title_create_post))
            add(DFP({ it.startsWithPattern(INTERNAL_AFFILIATE_DRAFT_POST) }, DF_CONTENT_AFFILIATE, R.string.applink_af_title_create_post))
            add(DFP({ it.startsWithPattern(AFFILIATE_EDIT) }, DF_CONTENT_AFFILIATE, R.string.applink_af_title_create_post))
            add(DFP({ it.startsWithPattern(ApplinkConstInternalContent.INTERNAL_CONTENT_CREATE_POST) }, DF_CONTENT_AFFILIATE, R.string.applink_af_title_create_post))
            add(DFP({ it.startsWithPattern(ApplinkConstInternalContent.INTERNAL_CONTENT_DRAFT_POST) }, DF_CONTENT_AFFILIATE, R.string.applink_af_title_create_post))
            add(DFP({ it.startsWithPattern(ApplinkConstInternalContent.SHOP_POST_EDIT) }, DF_CONTENT_AFFILIATE, R.string.applink_af_title_create_post))

            // Digital
            add(DFP({
                it.startsWith(DIGITAL_SUBHOMEPAGE_HOME) ||
                        it.startsWith(TELCO_POSTPAID_DIGITAL) ||
                        it.startsWith(TELCO_PREPAID_DIGITAL) ||
                        it.startsWith(DIGITAL_PRODUCT_FORM) ||
                        it.startsWith(GENERAL_TEMPLATE) ||
                        it.startsWith(CAMERA_OCR) ||
                        it.startsWith(VOUCHER_GAME) ||
                        it.startsWith(CART_DIGITAL) || it.startsWith(DIGITAL_CART) || it.startsWith(CHECKOUT_DIGITAL)
            }, DF_BASE, R.string.title_digital_subhomepage))
            add(DFP({ it.startsWithPattern(INTERNAL_SMARTCARD_EMONEY) }, DF_BASE, R.string.title_digital_emoney))
            add(DFP({ it.startsWithPattern(INTERNAL_SMARTCARD_BRIZZI) }, DF_BASE, R.string.title_digital_emoney))

            // Discovery
            add(DFP({ it.startsWith(SIMILAR_SEARCH_RESULT_BASE) }, DF_BASE, R.string.title_similar_search))
            add(DFP({ it.startsWith(SEARCH_RESULT) || it.startsWith(AUTOCOMPLETE) }, DF_BASE, R.string.title_search_result))
            add(DFP({ it.startsWith(HOME_WISHLIST) }, DF_BASE, R.string.title_wishlist))
            add(DFP({ it.startsWith(DEFAULT_HOME_RECOMMENDATION) }, DF_BASE, R.string.recom_home_recommendation))
            add(DFP({ it.startsWith(HOME_RECENT_VIEW) }, DF_MERCHANT_LOGIN, R.string.title_recent_view, { DFWebviewFallbackUrl.RECENT_VIEW }))

            // Fintech
            add(DFP({ it.startsWith(OVO_PAY_WITH_QR_ENTRY) }, DF_BASE, R.string.ovo_pay_with_qr_title))
            add(DFP({ it.startsWith(OQR_PIN_URL_ENTRY) }, DF_BASE, R.string.ovo_pay_with_qr_title))
            add(DFP({ it.startsWith(OVO_WALLET) }, DF_BASE, R.string.applink_wallet_title))
            add(DFP({ it.startsWith(PAYLATER) }, DF_BASE, R.string.applink_pay_later_title))

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

            // Merchant
            add(DFP({ it.startsWith(OPEN_SHOP) }, DF_BASE, R.string.title_open_shop))

            add(DFP({ it.startsWith(FAVORITE) }, DF_MERCHANT_LOGIN, R.string.favorite_shop, { DFWebviewFallbackUrl.FAVORITE_SHOP }))
            add(DFP({ it.startsWithPattern(REPORT_PRODUCT) }, DF_MERCHANT_LOGIN, R.string.applink_report_title, ::getDefaultFallbackUrl))
            add(DFP({ it.startsWith(ATTACH_INVOICE) }, DF_MERCHANT_LOGIN, R.string.title_module_attachinvoice))
            add(DFP({ it.startsWith(ATTACH_VOUCHER) }, DF_MERCHANT_LOGIN, R.string.title_module_attachvoucher))
            add(DFP({ it.startsWith(ATTACH_PRODUCT) }, DF_MERCHANT_LOGIN, R.string.title_module_attachproduct))

            add(DFP({ it.startsWith(INTERNAL_SELLER) }, DF_MERCHANT_SELLER, R.string.merchant_seller, { DFWebviewFallbackUrl.SELLER_ORDER }))
            add(DFP({
                it.startsWith(PRODUCT_MANAGE)
                        || it.startsWith(PRODUCT_MANAGE_LIST)
                        || it.startsWith(ApplinkConstInternalSellerapp.SELLER_HOME_PRODUCT_MANAGE_LIST)
            }, DF_MERCHANT_SELLER, R.string.merchant_seller, { DFWebviewFallbackUrl.MANAGE_PRODUCT }))
            add(DFP({
                it.startsWith(POWER_MERCHANT_SUBSCRIBE) || it.startsWith(ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE)
            }, DF_MERCHANT_SELLER, R.string.merchant_seller, { DFWebviewFallbackUrl.POWER_MERCHANT }))
            add(DFP({ it.startsWith(SHOP_SETTINGS_BASE) }, DF_MERCHANT_SELLER, R.string.merchant_seller, { DFWebviewFallbackUrl.SHOP_SETTINGS }))
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
            add(DFP({
                val uri = Uri.parse(it).buildUpon().build()
                (uri.host == ReviewApplinkConst.AUTHORITY_PRODUCT && uri.pathSegments.lastOrNull() == ReviewApplinkConst.PATH_REVIEW)
            }, DF_BASE, R.string.title_product_review))
            add(DFP({
                val uri = Uri.parse(it).buildUpon().build()
                (uri.host == ReviewApplinkConst.PATH_REVIEW && uri.pathSegments.size == ReviewApplinkConst.REVIEW_EXPECTED_PATH_SIZE)
            }, DF_BASE, R.string.title_review_inbox))

            add(DFP({
                val uri = Uri.parse(it).buildUpon().build()
                (uri.host == ReviewApplinkConst.PATH_REVIEW && uri.pathSegments.size == ReviewApplinkConst.REVIEW_DETAIL_EXPECTED_PATH_SIZE)
            }, DF_BASE, R.string.title_review_detail))

            add(DFP({
                val uri = Uri.parse(it).buildUpon().build()
                (uri.host == ReviewApplinkConst.PATH_PRODUCT_REVIEW && uri.pathSegments.last() == ReviewApplinkConst.PATH_CREATE)
            }, DF_BASE, R.string.title_create_review))

            add(DFP({
                it.startsWith(ApplinkConstInternalMarketplace.PRODUCT_DETAIL) ||
                        it.startsWith(ApplinkConstInternalMarketplace.PRODUCT_DETAIL_WITH_AFFILIATE) ||
                        it.startsWith(ApplinkConstInternalMarketplace.PRODUCT_DETAIL_WITH_WAREHOUSE_ID) ||
                        it.startsWith(ApplinkConstInternalMarketplace.PRODUCT_DETAIL_DOMAIN) ||
                        it.startsWith(ApplinkConstInternalMarketplace.PRODUCT_DETAIL_DOMAIN_WITH_AFFILIATE)
            }, DF_BASE, R.string.title_product_detail))

            add(DFP({
                it.startsWith(ApplinkConstInternalGlobal.IMAGE_PICKER) ||
                        it.startsWith(ApplinkConstInternalGlobal.IMAGE_EDITOR) ||
                        it.startsWith(ApplinkConstInternalGlobal.VIDEO_PICKER)
            }, DF_BASE, R.string.title_image_picker))

            // Operational
            add(DFP({
                it.startsWith(CONTACT_US_NATIVE) || it.startsWith(CONTACT_US) || it.startsWithPattern(TICKET_DETAIL) ||
                        it.startsWith(INTERNAL_INBOX_LIST)
            }, DF_OPERATIONAL_CONTACT_US, R.string.applink_title_contact_us, { DFWebviewFallbackUrl.OPERATIONAL_CONTACT_US }))
            add(DFP({ it.startsWith(CHAT_BOT) }, DF_OPERATIONAL_CONTACT_US, R.string.title_applink_chatbot, { DFWebviewFallbackUrl.OPERATIONAL_CHAT_BOT }))

            // Payment
            add(DFP({ it.startsWith(PAYMENT_SETTING) }, DF_BASE, R.string.payment_settings_title))
            add(DFP({ it.startsWith(ApplinkConstInternalPayment.PMS_PAYMENT_LIST) }, DF_BASE, R.string.payment_title_payment_status))
            add(DFP({ it.startsWith(ApplinkConstInternalPayment.INTERNAL_HOW_TO_PAY) }, DF_BASE, R.string.payment_title_activity_howtopay))
            add(DFP({ it.startsWith(INSTANT_DEBIT_BCA_ENTRY_PATTERN) || it.startsWith(EDIT_BCA_ONE_KLICK_ENTRY_PATTERN) }, DF_BASE, R.string.payment_instant_debit_bca_title))

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
                        it.startsWith(GLOBAL_INTERNAL_DIGITAL_DEAL_BRAND_DETAIL_BASE)
            }, DF_ENTERTAINMENT, R.string.title_entertainment, { DFWebviewFallbackUrl.ENTERTAINMENT_DEALS }))

            // Salam
            add(DFP({ it.startsWith(SALAM_UMRAH_HOME_PAGE) }, DF_SALAM_UMRAH, R.string.title_salam, { DFWebviewFallbackUrl.SALAM_UMRAH }))
            add(DFP({ it.startsWith(SALAM_ORDER_DETAIL) }, DF_SALAM_UMRAH, R.string.title_salam, { DFWebviewFallbackUrl.SALAM_UMRAH }))

            // Travel
            add(DFP({ it.startsWith(TRAVEL_SUBHOMEPAGE) }, DF_BASE, R.string.title_travel_homepage))
            add(DFP({ it.startsWith(FLIGHT) || it.startsWith(INTERNAL_FLIGHT) }, DF_TRAVEL, R.string.title_flight, { DFWebviewFallbackUrl.TRAVEL_FLIGHT }))
            add(DFP({ it.startsWith(HOTEL) }, DF_BASE, R.string.title_hotel))

            // User
            add(DFP({ it.startsWith(PROMO_CAMPAIGN_SHAKE_LANDING_PREFIX) }, DF_BASE, R.string.title_applink_campaign_shake_landing))

            add(DFP({
                (it.startsWith(SETTING_PROFILE)
                        || it.startsWith(ADD_PHONE)
                        || it.startsWith(ADD_EMAIL)
                        || it.startsWith(ADD_BOD)
                        || it.startsWithPattern(CHANGE_NAME)
                        || it.startsWith(CHANGE_GENDER)
                        || it.startsWith(ADD_NAME_REGISTER)
                        || it.startsWith(ADD_NAME_REGISTER_CLEAN_VIEW)
                        || it.startsWith(CHANGE_PIN)
                        || it.startsWith(ADD_PIN_ONBOARDING)
                        || it.startsWith(ADD_PIN)
                        || it.startsWith(ADD_PIN_COMPLETE)
                        )
            }, DF_USER_SETTINGS, R.string.applink_profile_completion_title, { DFWebviewFallbackUrl.USER_PROFILE_SETTINGS }))
            add(DFP({ it.startsWith(ApplinkConstInternalGlobal.PROFILE_COMPLETION) }, DF_USER_SETTINGS, R.string.applink_profile_completion_title))

            add(DFP({ it.startsWith(CHANGE_PHONE_NUMBER) }, DF_BASE, R.string.applink_change_phone_number))
            add(DFP({ it.startsWith(CHANGE_PASSWORD) }, DF_BASE, R.string.applink_change_password))
            add(DFP({ it.startsWith(SETTING_BANK) }, DF_USER_SETTINGS, R.string.applink_setting_bank_title, { DFWebviewFallbackUrl.USER_SETTING_BANK }))
            add(DFP({ it.startsWith(USER_NOTIFICATION_SETTING) }, DF_BASE, R.string.notif_settings_title))
            add(DFP({ it.startsWithPattern(USER_IDENTIFICATION_FORM) }, DF_BASE, R.string.user_identification_common_title))
            add(DFP({ it.startsWith(ORDER_HISTORY) || it.startsWithPattern(ApplinkConstInternalMarketplace.ORDER_HISTORY) }, DF_MERCHANT_LOGIN, R.string.title_module_attachvoucher))
            add(DFP({
                it.startsWith(TOPCHAT_IDLESS) || it.startsWith(ApplinkConstInternalGlobal.TOPCHAT)
            }, DF_BASE, R.string.title_topchat))
            add(DFP({ it.startsWith(INBOX) }, DF_BASE, R.string.title_inbox))

            add(DFP({ it.startsWith(INBOX_TALK) }, DF_BASE, R.string.talk_title))
            add(DFP({ it.startsWith(SHOP_TALK) }, DF_BASE, R.string.talk_title))
            add(DFP({ it.startsWithPattern(PRODUCT_TALK) }, DF_BASE, R.string.talk_title))
            add(DFP({ it.startsWith(DETAIL_TALK_BASE) }, DF_BASE, R.string.talk_title))
            add(DFP({ it.startsWith(ADD_TALK) }, DF_BASE, R.string.talk_title))

            add(DFP({ it.startsWith(ADD_FINGERPRINT_ONBOARDING) }, DF_BASE, R.string.fingerprint_onboarding))
            add(DFP({ it.startsWith(LIVENESS_DETECTION) }, DF_USER_LIVENESS, R.string.applink_liveness_detection))

            add(DFP({ it.startsWith(NOTIFICATION) }, DF_BASE, R.string.title_notification_center))
            add(DFP({ it.startsWith(NOTIFICATION_BUYER) }, DF_BASE, R.string.title_notification_center))
            add(DFP({ it.startsWith(PUSH_NOTIFICATION_TROUBLESHOOTER) }, DF_BASE, R.string.applink_notif_troubleshooter))

            add(DFP({ it.startsWith(OTP) }, DF_BASE, R.string.title_otp))
            add(DFP({ it.startsWith(CHOOSE_ACCOUNT) }, DF_BASE, R.string.title_loginphone))
            add(DFP({ it.startsWith(CHANGE_INACTIVE_PHONE) }, DF_BASE, R.string.title_update_inactive_phone))

            // Transaction
            add(DFP({ it.startsWith(CHECKOUT) }, DF_BASE, R.string.checkout_module_title_activity_checkout))
            add(DFP({ it.startsWith(CHECKOUT_ADDRESS_SELECTION) }, DF_BASE, R.string.checkout_module_title_activity_shipping_address))
            add(DFP({
                it.startsWith(ONE_CLICK_CHECKOUT) ||
                        it.startsWith(PREFERENCE_LIST) ||
                        it.startsWith(PREFERENCE_EDIT)
            }, DF_BASE, R.string.title_one_click_checkout))
            add(DFP({ it.startsWith(PROMO_CHECKOUT_MARKETPLACE) }, DF_BASE, R.string.promo_checkout_marketplace_module_title_activity_promo_list))

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
                        it.startsWith(BELANJA_INTERNAL_ORDER) ||
                        it.startsWith(MARKETPLACE_INTERNAL_ORDER)
            }, DF_BASE, R.string.title_buyerorder))

            // snapshot
            add(DFP({ it.startsWith(INTERNAL_ORDER_SNAPSHOT) }, DF_BASE, R.string.title_snapshot))

            // Order History
            add(DFP({ it.startsWith(TRACK) }, DF_BASE, R.string.title_order_management_history))
        }
    }

    val deeplinkDFPatternListSellerApp: List<DFP> by lazy {
        mutableListOf<DFP>().apply {
            add(DFP({ it.startsWith(SHOP_SETTINGS_BASE) }, DF_BASE_SELLER_APP, R.string.shop_settings_title))
            add(DFP({ it.startsWith(PAYMENT_SETTING) }, DF_BASE_SELLER_APP, R.string.payment_settings_title))
            add(DFP({ it.startsWith(PRODUCT_MANAGE_LIST) }, DF_BASE_SELLER_APP, R.string.title_applink_product_manage))
            add(DFP({ it.startsWithPattern(USER_IDENTIFICATION_FORM) }, DF_BASE_SELLER_APP, R.string.user_identification_common_title))
            add(DFP({
                it.startsWith(TOPADS_DASHBOARD_SELLER) ||
                        it.startsWith(TOPADS_DASHBOARD_INTERNAL)
            }, DF_BASE_SELLER_APP, R.string.applink_topads_dashboard_title))
            add(DFP({ it.startsWith(MERCHANT_SHOP_SHOWCASE_LIST) }, DF_BASE_SELLER_APP, R.string.merchant_seller))
            add(DFP({ it.startsWith(MERCHANT_SHOP_SCORE)
                    || it.startsWith(SHOP_SCORE_DETAIL)
                    || it.startsWith(ApplinkConstInternalMarketplace.SHOP_SCORE_DETAIL)
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
            add(DFP({ it.startsWith(MERCHANT_OPEN_PRODUCT_PREVIEW) || it.startsWith(PRODUCT_ADD) }, DF_BASE_SELLER_APP, R.string.title_product_add_edit))
            add(DFP({ it.startsWith(WELCOME) }, DF_BASE_SELLER_APP, R.string.title_seller_onboarding))
            add(DFP({ it.startsWith(SELLER_SEARCH) }, DF_BASE_SELLER_APP, R.string.title_global_search_seller))

            // Content
            add(DFP({ it.startsWithPattern(COMMENT) }, DF_BASE_SELLER_APP, R.string.applink_kol_title_comment))
            add(DFP({ it.startsWithPattern(INTERNAL_CONTENT_POST_DETAIL) }, DF_BASE_SELLER_APP, R.string.applink_kol_title_post_detail))
            add(DFP({ it.startsWithPattern(KOL_YOUTUBE) }, DF_BASE_SELLER_APP, R.string.applink_kol_title_post_youtube))
            add(DFP({ it.startsWithPattern(CONTENT_REPORT) }, DF_BASE_SELLER_APP, R.string.applink_kol_title_content_report))
            add(DFP({ it.startsWithPattern(VIDEO_DETAIL) }, DF_BASE_SELLER_APP, R.string.applink_kol_title_video_detail))
            add(DFP({ it.startsWithPattern(MEDIA_PREVIEW) }, DF_BASE_SELLER_APP, R.string.applink_kol_title_media_preview))
            add(DFP({ it.startsWithPattern(ApplinkConstInternalContent.INTERNAL_CONTENT_CREATE_POST) }, DF_BASE_SELLER_APP, R.string.applink_af_title_create_post))
            add(DFP({ it.startsWithPattern(ApplinkConstInternalContent.INTERNAL_CONTENT_DRAFT_POST) }, DF_BASE_SELLER_APP, R.string.applink_af_title_create_post))
            add(DFP({ it.startsWithPattern(ApplinkConstInternalContent.SHOP_POST_EDIT) }, DF_BASE_SELLER_APP, R.string.applink_af_title_create_post))
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
                        it.startsWith(ApplinkConstInternalMarketplace.PRODUCT_DETAIL_WITH_AFFILIATE) ||
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