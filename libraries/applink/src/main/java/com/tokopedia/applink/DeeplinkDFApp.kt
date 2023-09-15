package com.tokopedia.applink

import android.content.Context
import com.tokopedia.applink.DFWebviewFallbackUrl.DIGITAL_SMART_BILLS
import com.tokopedia.applink.DFWebviewFallbackUrl.ENTERTAINMENT_DEALS
import com.tokopedia.applink.DFWebviewFallbackUrl.ENTERTAINMENT_EVENT
import com.tokopedia.applink.DFWebviewFallbackUrl.FINTECH_SALDO
import com.tokopedia.applink.DFWebviewFallbackUrl.MANAGE_PRODUCT
import com.tokopedia.applink.DFWebviewFallbackUrl.OPERATIONAL_CONTACT_US
import com.tokopedia.applink.DFWebviewFallbackUrl.PROMO_TOKOPOINTS
import com.tokopedia.applink.DFWebviewFallbackUrl.SELLER_ORDER
import com.tokopedia.applink.DFWebviewFallbackUrl.SHOP_SETTINGS
import com.tokopedia.applink.DFWebviewFallbackUrl.TOP_ADS_DASHBOARD
import com.tokopedia.applink.DFWebviewFallbackUrl.TRAVEL_FLIGHT
import com.tokopedia.applink.DFWebviewFallbackUrl.TRAVEL_HOTEL
import com.tokopedia.applink.DFWebviewFallbackUrl.USER_PROFILE_SETTINGS
import com.tokopedia.applink.DFWebviewFallbackUrl.USER_SETTING_BANK
import com.tokopedia.applink.DeeplinkDFMapper.DF_ALPHA_TESTING
import com.tokopedia.applink.DeeplinkDFMapper.DF_CAMPAIGN_LIST
import com.tokopedia.applink.DeeplinkDFMapper.DF_CATEGORY_AFFILIATE
import com.tokopedia.applink.DeeplinkDFMapper.DF_CATEGORY_TRADE_IN
import com.tokopedia.applink.DeeplinkDFMapper.DF_CONTENT_PLAY_BROADCASTER
import com.tokopedia.applink.DeeplinkDFMapper.DF_DIGITAL
import com.tokopedia.applink.DeeplinkDFMapper.DF_DILAYANI_TOKOPEDIA
import com.tokopedia.applink.DeeplinkDFMapper.DF_ENTERTAINMENT
import com.tokopedia.applink.DeeplinkDFMapper.DF_FEED_CONTENT_CREATION
import com.tokopedia.applink.DeeplinkDFMapper.DF_FLASH_SALE_TOKOPEDIA
import com.tokopedia.applink.DeeplinkDFMapper.DF_KYC_SELLERAPP
import com.tokopedia.applink.DeeplinkDFMapper.DF_MERCHANT_LOGIN
import com.tokopedia.applink.DeeplinkDFMapper.DF_MERCHANT_NONLOGIN
import com.tokopedia.applink.DeeplinkDFMapper.DF_MERCHANT_PRODUCT_AR
import com.tokopedia.applink.DeeplinkDFMapper.DF_MERCHANT_SELLER
import com.tokopedia.applink.DeeplinkDFMapper.DF_OPERATIONAL_CONTACT_US
import com.tokopedia.applink.DeeplinkDFMapper.DF_PEOPLE
import com.tokopedia.applink.DeeplinkDFMapper.DF_PROMO_GAMIFICATION
import com.tokopedia.applink.DeeplinkDFMapper.DF_PROMO_TOKOPOINTS
import com.tokopedia.applink.DeeplinkDFMapper.DF_SELLER_FEEDBACK
import com.tokopedia.applink.DeeplinkDFMapper.DF_SELLER_FRONT_FUNNEL
import com.tokopedia.applink.DeeplinkDFMapper.DF_SELLER_TALK
import com.tokopedia.applink.DeeplinkDFMapper.DF_SHOP_SETTINGS_SELLER_APP
import com.tokopedia.applink.DeeplinkDFMapper.DF_TOKOCHAT
import com.tokopedia.applink.DeeplinkDFMapper.DF_TOKOFOOD
import com.tokopedia.applink.DeeplinkDFMapper.DF_TOKOPEDIA_NOW
import com.tokopedia.applink.DeeplinkDFMapper.DF_TRAVEL
import com.tokopedia.applink.DeeplinkDFMapper.DF_USER_LIVENESS
import com.tokopedia.applink.DeeplinkDFMapper.DF_USER_SETTINGS
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.constant.DeeplinkConstant.SCHEME_SELLERAPP
import com.tokopedia.applink.internal.ApplinkConsInternalDigital.HOST_RECHARGE
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.HOST_CATEGORY
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.HOST_MONEYIN
import com.tokopedia.applink.internal.ApplinkConstInternalCommunication.HOST_COMMUNICATION
import com.tokopedia.applink.internal.ApplinkConstInternalContent.HOST_AFFILIATE
import com.tokopedia.applink.internal.ApplinkConstInternalContent.HOST_CONTENT
import com.tokopedia.applink.internal.ApplinkConstInternalContent.HOST_PLAY_BROADCASTER
import com.tokopedia.applink.internal.ApplinkConstInternalContent.HOST_PLAY_SHORTS
import com.tokopedia.applink.internal.ApplinkConstInternalDeals.HOST_DEALS
import com.tokopedia.applink.internal.ApplinkConstInternalDilayaniTokopedia.HOST_DILAYANI_TOKOPEDIA
import com.tokopedia.applink.internal.ApplinkConstInternalEntertainment.HOST_EVENT
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.HOST_GLOBAL
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.HOST_LOGISTIC
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.HOST_HOME
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.HOST_MARKETPLACE
import com.tokopedia.applink.internal.ApplinkConstInternalMechant.HOST_MERCHANT
import com.tokopedia.applink.internal.ApplinkConstInternalOperational.HOST_CONTACT_US
import com.tokopedia.applink.internal.ApplinkConstInternalOperational.HOST_CUSTOMERAPP_INBOX_LIST
import com.tokopedia.applink.internal.ApplinkConstInternalOperational.HOST_TICKET
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.HOST_SELLER
import com.tokopedia.applink.internal.ApplinkConstInternalPromo.HOST_TOKOPOINTS
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp.HOST_SELLERAPP
import com.tokopedia.applink.internal.ApplinkConstInternalTokoFood.HOST_FOOD
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow.HOST_TOKOPEDIA_NOW
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds.HOST_TOPADS
import com.tokopedia.applink.internal.ApplinkConstInternalTravel.HOST_FLIGHT
import com.tokopedia.applink.internal.ApplinkConstInternalTravel.HOST_HOTEL
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.HOST_PEOPLE
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.HOST_USER
import com.tokopedia.applink.model.DFPHost
import com.tokopedia.applink.model.DFPPath
import com.tokopedia.applink.model.DFPSchemeToDF
import com.tokopedia.applink.model.DFP
import com.tokopedia.applink.model.PathType
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader

/**
 * Containers for all mappings from [registered deeplink in manifest] to [dynamic feature module]
 * DF Module with configuration "install-time" is excluded.
 */
object DeeplinkDFApp {
    private var deeplinkDFPatternListCustomerAppv2: MutableList<DFPSchemeToDF>? = null
    private var deeplinkDFPatternListSellerAppv2: MutableList<DFPSchemeToDF>? = null

    private const val INTERNAL = DeeplinkConstant.SCHEME_INTERNAL
    private const val TOKOPEDIA = DeeplinkConstant.SCHEME_TOKOPEDIA

    fun getDeeplinkDFPatternList(isSellerapp: Boolean, context: Context): List<DFPSchemeToDF> {
        val d = getDeeplinkPattern(isSellerapp)
        if (d == null) {
            getDfCustomerappMap().filteredOnDF(context).mapDF().also {
                if (isSellerapp) {
                    deeplinkDFPatternListSellerAppv2 = it
                } else {
                    deeplinkDFPatternListCustomerAppv2 = it
                }
                return it
            }
        } else {
            return d
        }
    }

    fun getDeeplinkPattern(isSellerapp: Boolean): MutableList<DFPSchemeToDF>? {
        return if (isSellerapp) {
            deeplinkDFPatternListSellerAppv2
        } else {
            deeplinkDFPatternListCustomerAppv2
        }
    }

    /**
     * remove selected df module from path.
     * If the host/scheme does not have the path anymore, remove it.
     */
    fun removeDFModuleFromList(isSellerapp: Boolean, moduleId: String) {
        val dfpList = getDeeplinkPattern(isSellerapp)
        val dfpIterator = dfpList?.iterator() ?: return
        while (dfpIterator.hasNext()) {
            val dfpSchemeToDF = dfpIterator.next()
            val hostIterator = dfpSchemeToDF.hostList.iterator()
            while (hostIterator.hasNext()) {
                val dfpHost = hostIterator.next()
                val dfpPathIterator = dfpHost.dfpPathObj.iterator()
                while (dfpPathIterator.hasNext()) {
                    val dfpPath = dfpPathIterator.next()
                    if (dfpPath.dfTarget == moduleId) {
                        dfpPathIterator.remove()
                    }
                }
                if (dfpHost.dfpPathObj.isEmpty()) {
                    hostIterator.remove()
                }
            }
            if (dfpSchemeToDF.hostList.isEmpty()) {
                dfpIterator.remove()
            }
        }
    }

    fun getDfCustomerappMap() = mapOf(
        DF_ALPHA_TESTING to getDfAlphaTesting(),
        DF_CATEGORY_AFFILIATE to getDfCategoryAffiliate(),
        DF_CATEGORY_TRADE_IN to getDfCategoryTradeIn(),
        DF_TOKOCHAT to getDfCommTokochat(),
        DF_CONTENT_PLAY_BROADCASTER to getDfContentPlayBroadcaster(),
        DF_DIGITAL to getDfDigital(),
        DF_DILAYANI_TOKOPEDIA to getDfDilayaniTokopedia(),
        DF_ENTERTAINMENT to getDfEntertainment(),
        DF_FEED_CONTENT_CREATION to getDfFeedContentCreation(),
        DF_MERCHANT_LOGIN to getDfMerchantLogin(),
        DF_MERCHANT_NONLOGIN to getDfMerchantNonLogin(),
        DF_MERCHANT_PRODUCT_AR to getDfMerchantProductAR(),
        DF_MERCHANT_SELLER to getDfMerchantSeller(),
        DF_OPERATIONAL_CONTACT_US to getDfOperationalContactUs(),
        DF_PEOPLE to getDfPeople(),
        DF_PROMO_GAMIFICATION to getDfPromoGamification(),
        DF_PROMO_TOKOPOINTS to getDfPromoTokopoints(),
        DF_TOKOFOOD to getDfTokofood(),
        DF_TOKOPEDIA_NOW to getDfTokopediaNow(),
        DF_TRAVEL to getDfTravel(),
        DF_USER_LIVENESS to getDfUserLiveness(),
        DF_USER_SETTINGS to getDfUserSettings(),
    )

    fun getDfSellerappMap() = mapOf(
        DF_CAMPAIGN_LIST to getDfCampaignList(),
        DF_CONTENT_PLAY_BROADCASTER to getDfContentPlayBroadcaster(),
        DF_FLASH_SALE_TOKOPEDIA to getDfFlashSaleTokopedia(),
        DF_KYC_SELLERAPP to getDfKycSellerapp(),
        DF_SELLER_FEEDBACK to getDfSellerFeedback(),
        DF_SELLER_FRONT_FUNNEL to getDfSellerFrontFunnel(),
        DF_SELLER_TALK to getDfSellerTalk(),
        DF_SHOP_SETTINGS_SELLER_APP to getDfShopSettingsSellerapp(),
    )

    private fun Map<String, List<DFP>>?.filteredOnDF(context: Context): Map<String, List<DFP>> {
        val turnedOnDfSet = getDFFilterMap(context)
        if (turnedOnDfSet.isNullOrEmpty()) {
            return emptyMap()
        }
        return this?.filter { turnedOnDfSet.contains(it.key) } ?: emptyMap()
    }

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

    private fun getDfAlphaTesting() = mutableListOf(
        // feedback_form
        DFP(INTERNAL, HOST_GLOBAL, PathType.PATTERN, "/internal-feedback")
    )

    private fun getDfCategoryAffiliate() = mutableListOf(
        // affiliate_toko
        DFP(INTERNAL, HOST_AFFILIATE, PathType.NO_PATH, "")
    )

    private fun getDfCategoryTradeIn() = mutableListOf(
        // tradein
        DFP(INTERNAL, HOST_CATEGORY, PathType.PATTERN, "/tradein_tnc"),
        DFP(INTERNAL, HOST_CATEGORY, PathType.PATTERN, "/tradein_black_market"),
        DFP(INTERNAL, HOST_CATEGORY, PathType.PATH, "/tradein"),
        DFP(INTERNAL, HOST_CATEGORY, PathType.PATTERN, "/tradein/.*\\"),
        // moneyin
        DFP(INTERNAL, HOST_MONEYIN, PathType.PATTERN, "/device_validation"),
        // dropoff
        DFP(INTERNAL, HOST_LOGISTIC, PathType.PATTERN, "/dropoff/"),
    )

    private fun getDfCommTokochat() = mutableListOf(
        // tokochat
        DFP(INTERNAL, HOST_COMMUNICATION, PathType.PATTERN, "/tokochat"),
        DFP(INTERNAL, HOST_COMMUNICATION, PathType.PATTERN, "/tokochat/list"),
    )

    private fun getDfContentPlayBroadcaster() = mutableListOf(
        // play_broadcaster
        DFP(INTERNAL, HOST_PLAY_BROADCASTER, PathType.NO_PATH, ""),
        DFP(INTERNAL, HOST_PLAY_SHORTS, PathType.NO_PATH, ""),
        // mediapicker
        DFP(INTERNAL, HOST_GLOBAL, PathType.PATH, "/media-picker-album"),
        DFP(INTERNAL, HOST_GLOBAL, PathType.PATH, "/media-picker"),
        DFP(INTERNAL, HOST_GLOBAL, PathType.PATH, "/media-picker-preview"),
        // live-broadcaster
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATH, "/chucker"),
    )

    private fun getDfDigital() = mutableListOf(
        // smart_bills
        DFP(INTERNAL, HOST_RECHARGE, PathType.PATTERN, "/bayarsekaligus", DIGITAL_SMART_BILLS),
        DFP(INTERNAL, HOST_RECHARGE, PathType.PATTERN, "/add_telco"),
    )

    private fun getDfDilayaniTokopedia() = mutableListOf(
        // dilayanitokopedia
        DFP(INTERNAL, HOST_DILAYANI_TOKOPEDIA, PathType.PATTERN, "/home"),
    )

    private fun getDfEntertainment() = mutableListOf(
        // event
        DFP(INTERNAL, HOST_EVENT, PathType.PATH, "/search", ENTERTAINMENT_EVENT),
        DFP(INTERNAL, HOST_EVENT, PathType.PATH, "/category", ENTERTAINMENT_EVENT),
        DFP(INTERNAL, HOST_EVENT, PathType.PATTERN, "/choose-package/.*", ENTERTAINMENT_EVENT),
        DFP(INTERNAL, HOST_EVENT, PathType.PATTERN, "/checkout", ENTERTAINMENT_EVENT),
        DFP(INTERNAL, HOST_EVENT, PathType.PATTERN, "/form/.*", ENTERTAINMENT_EVENT),
        DFP(INTERNAL, HOST_EVENT, PathType.PATH, "/redeem", ENTERTAINMENT_EVENT),
        DFP(INTERNAL, HOST_EVENT, PathType.PATTERN, "/detail/.*", ENTERTAINMENT_EVENT),
        DFP(INTERNAL, HOST_EVENT, PathType.PATH, "/home", ENTERTAINMENT_EVENT),
        //deals
        DFP(INTERNAL, HOST_DEALS, PathType.PATH, "/home-new", ENTERTAINMENT_DEALS),
        DFP(INTERNAL, HOST_DEALS, PathType.PATH, "/search-new", ENTERTAINMENT_DEALS),
        DFP(INTERNAL, HOST_DEALS, PathType.PATH, "/category-new/page", ENTERTAINMENT_DEALS),
        DFP(INTERNAL, HOST_DEALS, PathType.PATH, "/brand-new/page", ENTERTAINMENT_DEALS),
        DFP(INTERNAL, HOST_DEALS, PathType.PATH, "/brand-detail-new", ENTERTAINMENT_DEALS),
        DFP(INTERNAL, HOST_DEALS, PathType.PATH, "/pdp-new", ENTERTAINMENT_DEALS),
    )

    private fun getDfFeedContentCreation() = mutableListOf(
        // createpost
        DFP(INTERNAL, HOST_CONTENT, PathType.PATTERN, "/productpickerfromshop/.*"),
        DFP(INTERNAL, HOST_CONTENT, PathType.PATTERN, "/creation_product_search.*"),
        DFP(INTERNAL, HOST_CONTENT, PathType.PATTERN, "/creation_shop_search/.*"),
        DFP(INTERNAL, HOST_CONTENT, PathType.PATTERN, "/create_post_v2/"),
        // image_picker_insta
        DFP(INTERNAL, HOST_GLOBAL, PathType.PATH, "/image-picker/v2/"),
        // mediaeditor
        DFP(INTERNAL, HOST_GLOBAL, PathType.PATH, "/media-editor"),
        // image_picker
        DFP(INTERNAL, HOST_GLOBAL, PathType.PATTERN, "/image-picker"),
        DFP(INTERNAL, HOST_GLOBAL, PathType.PATTERN, "/video-picker"),
        DFP(INTERNAL, HOST_GLOBAL, PathType.PATTERN, "/image-editor"),
    )

    private fun getDfMerchantLogin() = mutableListOf(
        // favorite
        DFP(TOKOPEDIA, HOST_HOME, PathType.PATH, "/favorite", DFWebviewFallbackUrl.FAVORITE_SHOP),
        // orderhistory
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATTERN, "/user-order-history/.*/"),
        // report
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATTERN, "/product-report/.*/"),
        // attachinvoice
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATH, "/user-attach-invoice"),
        // attachvoucher
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATH, "/user-attach-voucher"),
        // shop_open
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATH, "/shop-open"),
    )

    private fun getDfMerchantNonLogin() = mutableListOf(
        // review
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATTERN, "/seller-review-detail"),
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATTERN, "/product-review/create/.*/.*/"),
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATTERN, "/product-review/edit/.*/.*/"),
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATTERN, "/review/detail/.*/"),
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATTERN, "/review"),
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATTERN, "/reputation/.*/"),
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATTERN, "/review-report"),
        DFP(INTERNAL, HOST_SELLERAPP, PathType.PATTERN, "/review-reminder"),
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATTERN, "/product/.*/review"),
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATTERN, "/shop/.*/review"),
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATTERN, "/product/.*/review/gallery"),
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATTERN, "/review/credibility/.*/.*/"),
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATTERN, "/review/media-gallery"),
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATTERN, "/product-review/bulk-create"),
    )

    private fun getDfMerchantProductAR() = mutableListOf(
        // product_ar
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATTERN, "/productar/.*/"),
    )

    private fun getDfMerchantSeller() = mutableListOf(
        // product_add_edit
        DFP(INTERNAL, HOST_MERCHANT, PathType.PATH, "/open-product-preview"),
        DFP(INTERNAL, HOST_MERCHANT, PathType.PATH, "/product-draft"),
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATTERN, "/product-category-picker/.*/"),
        // power_merchant_subscribe
        DFP(
            INTERNAL, HOST_MARKETPLACE, PathType.PATTERN, "/power-merchant-subscribe",
            DFWebviewFallbackUrl.POWER_MERCHANT
        ),
        DFP(
            INTERNAL, HOST_MARKETPLACE, PathType.PATH, "/pm-benefit-package",
            DFWebviewFallbackUrl.POWER_MERCHANT
        ),
        // shop_settings
        DFP(
            INTERNAL, HOST_MARKETPLACE, PathType.PATTERN, "/shop-settings-info",
            SHOP_SETTINGS
        ),
        DFP(
            INTERNAL, HOST_MARKETPLACE, PathType.PATTERN,
            "/shop-settings-operational-hours", SHOP_SETTINGS
        ),
        DFP(
            INTERNAL, HOST_MARKETPLACE, PathType.PATTERN,
            "/shop-settings-edit-schedule", SHOP_SETTINGS
        ),
        DFP(
            INTERNAL, HOST_MARKETPLACE, PathType.PATTERN,
            "/shop-settings-notes", SHOP_SETTINGS
        ),
        DFP(
            INTERNAL, HOST_MARKETPLACE, PathType.PATTERN, "/shop-settings-etalase/add",
            SHOP_SETTINGS
        ),
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATH, "/shop-page-setting", SHOP_SETTINGS),
        DFP(
            INTERNAL, HOST_MARKETPLACE, PathType.PATTERN, "/shop-page/.*/settings",
            SHOP_SETTINGS
        ),
        // top_ads_dashboard
        DFP(TOKOPEDIA, HOST_TOPADS, PathType.PATTERN, "/dashboard", TOP_ADS_DASHBOARD),
        DFP(INTERNAL, HOST_TOPADS, PathType.PATTERN, "/dashboard", TOP_ADS_DASHBOARD),
        DFP(SCHEME_SELLERAPP, HOST_TOPADS, PathType.PATH, "/buy"),
        DFP(INTERNAL, HOST_TOPADS, PathType.PATH, "/buy"),
        DFP(SCHEME_SELLERAPP, HOST_TOPADS, PathType.PATH, "/add-credit"),
        DFP(INTERNAL, HOST_TOPADS, PathType.PATH, "/add-credit"),
        DFP(INTERNAL, HOST_TOPADS, PathType.PATH, "/headline-ad-detail"),
        DFP(INTERNAL, HOST_TOPADS, PathType.PATH, "/history-credit"),
        DFP(INTERNAL, HOST_TOPADS, PathType.PATH, "/auto-topup"),
        DFP(INTERNAL, HOST_TOPADS, PathType.PATH, "/ad-selection"),
        DFP(INTERNAL, HOST_TOPADS, PathType.PATTERN, "/product-recommendation"),
        // sellerorder
        DFP(INTERNAL, HOST_SELLER, PathType.PATH, "/new-order", SELLER_ORDER),
        DFP(INTERNAL, HOST_SELLER, PathType.PATH, "/ready-to-ship", SELLER_ORDER),
        DFP(INTERNAL, HOST_SELLER, PathType.PATH, "/shipped", SELLER_ORDER),
        DFP(INTERNAL, HOST_SELLER, PathType.PATH, "/delivered", SELLER_ORDER),
        DFP(INTERNAL, HOST_SELLER, PathType.PATH, "/history", SELLER_ORDER),
        DFP(INTERNAL, HOST_SELLER, PathType.PATH, "/cancelled", SELLER_ORDER),
        DFP(INTERNAL, HOST_SELLER, PathType.PATH, "/waiting-pickup", SELLER_ORDER),
        DFP(INTERNAL, HOST_SELLER, PathType.PATH, "/waiting-awb", SELLER_ORDER),
        DFP(INTERNAL, HOST_SELLER, PathType.PATH, "/awb-invalid", SELLER_ORDER),
        DFP(INTERNAL, HOST_SELLER, PathType.PATH, "/awb-change", SELLER_ORDER),
        DFP(INTERNAL, HOST_SELLER, PathType.PATH, "/retur", SELLER_ORDER),
        DFP(INTERNAL, HOST_SELLER, PathType.PATH, "/complaint", SELLER_ORDER),
        DFP(INTERNAL, HOST_SELLER, PathType.PATH, "/finished", SELLER_ORDER),
        DFP(INTERNAL, HOST_SELLER, PathType.PATH, "/cancellationrequest", SELLER_ORDER),
        DFP(INTERNAL, HOST_SELLER, PathType.PATH, "/order", SELLER_ORDER),

        // editshipping
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATH, "/shop-settings-shipping"),
        DFP(INTERNAL, HOST_LOGISTIC, PathType.PATTERN, "/editaddress/"),
        DFP(INTERNAL, HOST_LOGISTIC, PathType.PATTERN, "/customproductlogistic"),
        // shop_score
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATH, "/shop/performance"),
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATH, "/shop-penalty-old"),
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATH, "/shop-penalty"),
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATH, "/shop-penalty-detail"),

        // seller_menu
        DFP(INTERNAL, HOST_SELLERAPP, PathType.PATH, "/seller-menu"),
        DFP(INTERNAL, HOST_SELLERAPP, PathType.PATH, "/seller-settings"),

        // shop_admin
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATH, "/shop-admin/invitation-page"),
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATH, "/shop-admin/accepted-page"),
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATH, "/shop-admin/redirection-page"),
        DFP(INTERNAL, HOST_SELLERAPP, PathType.PATTERN, "/admin-authorize/.*/"),

        // product_manage_list
        DFP(
            INTERNAL, HOST_MARKETPLACE, PathType.PATH, "/product-manage-list",
            MANAGE_PRODUCT
        ),
        DFP(
            INTERNAL, HOST_MARKETPLACE, PathType.PATTERN, "/stock-reminder/.*/.*/.*",
            MANAGE_PRODUCT
        ),
        DFP(
            INTERNAL, HOST_MARKETPLACE, PathType.PATTERN, "/reserved-stock/.*/.*/",
            MANAGE_PRODUCT
        ),
    )

    private fun getDfOperationalContactUs() = mutableListOf(
        // contact_us
        DFP(TOKOPEDIA, HOST_CONTACT_US, PathType.NO_PATH, "", OPERATIONAL_CONTACT_US),
        DFP(
            INTERNAL, HOST_CUSTOMERAPP_INBOX_LIST, PathType.NO_PATH, "",
            OPERATIONAL_CONTACT_US
        ),
        DFP(TOKOPEDIA, HOST_TICKET, PathType.PATTERN, "/.*", OPERATIONAL_CONTACT_US),
        DFP(TOKOPEDIA, HOST_CONTACT_US, PathType.PATTERN, "/clear-cache"),

        // chatbot
        DFP(
            INTERNAL, HOST_TICKET, PathType.PATH, "/inbox-list",
            DFWebviewFallbackUrl.OPERATIONAL_CHAT_BOT
        ),
        DFP(
            INTERNAL, HOST_GLOBAL, PathType.PATTERN, "/chatbot/.*",
            DFWebviewFallbackUrl.OPERATIONAL_CHAT_BOT
        ),

        // telephony_masking
        DFP(INTERNAL, HOST_USER, PathType.PATTERN, "/telephony-masking"),
    )

    private fun getDfPeople() = mutableListOf(
        // people
        DFP(INTERNAL, HOST_PEOPLE, PathType.PATTERN, "/settings/.*"),
        DFP(INTERNAL, HOST_PEOPLE, PathType.PATTERN, "/.*"),
        DFP(INTERNAL, HOST_GLOBAL, PathType.PATH, "/people/followers/"),
    )

    private fun getDfPromoGamification() = mutableListOf(
        // gamification
        DFP(INTERNAL, HOST_GLOBAL, PathType.PATTERN, "/gamification_gift_daily"),
        DFP(INTERNAL, HOST_GLOBAL, PathType.PATTERN, "/gamification_gift_60s"),
    )

    private fun getDfPromoTokopoints() = mutableListOf(
        // tokopoints
        DFP(INTERNAL, HOST_TOKOPOINTS, PathType.PATH, "/home", PROMO_TOKOPOINTS),
        DFP(INTERNAL, HOST_TOKOPOINTS, PathType.PREFIX, "/tukar-point", PROMO_TOKOPOINTS),
        DFP(INTERNAL, HOST_TOKOPOINTS, PathType.PREFIX, "/kupon-saya", PROMO_TOKOPOINTS),
        DFP(INTERNAL, HOST_TOKOPOINTS, PathType.PREFIX, "/tukar-detail", PROMO_TOKOPOINTS),
        DFP(INTERNAL, HOST_TOKOPOINTS, PathType.PREFIX, "/kupon-detail", PROMO_TOKOPOINTS),
        DFP(INTERNAL, HOST_TOKOPOINTS, PathType.PREFIX, "/introduction", PROMO_TOKOPOINTS),
    )

    private fun getDfTokofood() = mutableListOf(
        // tokofood
        DFP(INTERNAL, HOST_FOOD, PathType.PATH, "/postpurchase"),
        DFP(INTERNAL, HOST_FOOD, PathType.PATH, "/home"),
        DFP(INTERNAL, HOST_FOOD, PathType.PATH, "/merchant"),
        DFP(INTERNAL, HOST_FOOD, PathType.PATH, "/category"),
        DFP(INTERNAL, HOST_FOOD, PathType.PATH, "/purchase"),
        DFP(INTERNAL, HOST_FOOD, PathType.PATH, "/search"),
    )

    private fun getDfTokopediaNow() = mutableListOf(
        // tokopedianow
        DFP(INTERNAL, HOST_TOKOPEDIA_NOW, PathType.PATTERN, "/home"),
        DFP(INTERNAL, HOST_TOKOPEDIA_NOW, PathType.PATTERN, "/see-all-category"),
        DFP(INTERNAL, HOST_TOKOPEDIA_NOW, PathType.PATTERN, "/category-list"),
        DFP(INTERNAL, HOST_TOKOPEDIA_NOW, PathType.PATTERN, "/sort-filter"),
        DFP(INTERNAL, HOST_TOKOPEDIA_NOW, PathType.PATTERN, "/date-filter"),
        DFP(INTERNAL, HOST_TOKOPEDIA_NOW, PathType.PATH, "/search"),
        DFP(INTERNAL, HOST_TOKOPEDIA_NOW, PathType.PATTERN, "/category"),
        DFP(INTERNAL, HOST_TOKOPEDIA_NOW, PathType.PATTERN, "/category/l1"),
        DFP(INTERNAL, HOST_TOKOPEDIA_NOW, PathType.PATH, "/repurchase-page"),
        DFP(INTERNAL, HOST_TOKOPEDIA_NOW, PathType.PATTERN, "/category-filter"),
        DFP(INTERNAL, HOST_TOKOPEDIA_NOW, PathType.PATTERN, "/educational-info"),
        DFP(INTERNAL, HOST_TOKOPEDIA_NOW, PathType.PATTERN, "/recipe/detail"),
        DFP(INTERNAL, HOST_TOKOPEDIA_NOW, PathType.PATTERN, "/recipe/bookmark"),
        DFP(INTERNAL, HOST_TOKOPEDIA_NOW, PathType.PATTERN, "/recipe/home"),
        DFP(INTERNAL, HOST_TOKOPEDIA_NOW, PathType.PATTERN, "/recipe/search"),
        DFP(INTERNAL, HOST_TOKOPEDIA_NOW, PathType.PATTERN, "/recipe/filter"),
        DFP(INTERNAL, HOST_TOKOPEDIA_NOW, PathType.PATTERN, "/recipe/auto-complete"),
        DFP(
            INTERNAL, HOST_TOKOPEDIA_NOW,
            PathType.PATTERN, "/recipe/ingredient-bottomsheet"
        ),
        DFP(
            INTERNAL, HOST_TOKOPEDIA_NOW,
            PathType.PATTERN, "/recipe/similar-product-bottomsheet"
        ),
        DFP(INTERNAL, HOST_TOKOPEDIA_NOW, PathType.PATTERN, "/buyer-communication"),
    )

    private fun getDfTravel() = mutableListOf(
        // flight
        DFP(INTERNAL, HOST_FLIGHT, PathType.PATH, "/dashboard", TRAVEL_FLIGHT),
        DFP(TOKOPEDIA, HOST_FLIGHT, PathType.PATH, "/search", TRAVEL_FLIGHT),
        DFP(INTERNAL, HOST_FLIGHT, PathType.PATTERN, "/cancellation", TRAVEL_FLIGHT),
        DFP(TOKOPEDIA, HOST_FLIGHT, PathType.PATTERN, "/order/.*", TRAVEL_FLIGHT),
        DFP(TOKOPEDIA, HOST_FLIGHT, PathType.PATH, "/cancellation", TRAVEL_FLIGHT),

        // hotel
        DFP(INTERNAL, HOST_HOTEL, PathType.PATH, "/dashboard", TRAVEL_HOTEL),
        DFP(INTERNAL, HOST_HOTEL, PathType.PATH, "/result", TRAVEL_HOTEL),
        DFP(INTERNAL, HOST_HOTEL, PathType.PATTERN, "/detail/.*", TRAVEL_HOTEL),
        DFP(TOKOPEDIA, HOST_HOTEL, PathType.PATTERN, "/order/.*", TRAVEL_HOTEL),
        DFP(TOKOPEDIA, HOST_HOTEL, PathType.PATTERN, "/booking/.*", TRAVEL_HOTEL),
        DFP(TOKOPEDIA, HOST_HOTEL, PathType.PATTERN, "/cancel/.*", TRAVEL_HOTEL),
    )

    private fun getDfUserLiveness() = mutableListOf(
        // liveness
        DFP(INTERNAL, HOST_USER, PathType.PATTERN, "/liveness-detection"),
    )

    private fun getDfUserSettings() = mutableListOf(
        // profilecompletion
        DFP(INTERNAL, HOST_USER, PathType.PATTERN, "/setting-profile", USER_PROFILE_SETTINGS),
        DFP(INTERNAL, HOST_USER, PathType.PATTERN, "/add-name-register", USER_PROFILE_SETTINGS),
        DFP(INTERNAL, HOST_USER, PathType.PATTERN, "/change-gender", USER_PROFILE_SETTINGS),
        DFP(INTERNAL, HOST_USER, PathType.PATTERN, "/add-email", USER_PROFILE_SETTINGS),
        DFP(INTERNAL, HOST_USER, PathType.PATTERN, "/add-phone", USER_PROFILE_SETTINGS),
        DFP(INTERNAL, HOST_USER, PathType.PATTERN, "/new-add-phone", USER_PROFILE_SETTINGS),
        DFP(INTERNAL, HOST_USER, PathType.PATTERN, "/change-pin", USER_PROFILE_SETTINGS),
        DFP(INTERNAL, HOST_USER, PathType.PATTERN, "/add-bod", USER_PROFILE_SETTINGS),
        DFP(INTERNAL, HOST_USER, PathType.PATTERN, "/add-pin-onboarding", USER_PROFILE_SETTINGS),
        DFP(INTERNAL, HOST_USER, PathType.PATTERN, "/add-pin", USER_PROFILE_SETTINGS),
        DFP(INTERNAL, HOST_USER, PathType.PATTERN, "/add-pin-from-2fa", USER_PROFILE_SETTINGS),
        DFP(INTERNAL, HOST_USER, PathType.PATTERN, "/add-pin-complete", USER_PROFILE_SETTINGS),
        DFP(INTERNAL, HOST_USER, PathType.PATTERN, "/change-name", USER_PROFILE_SETTINGS),
        DFP(INTERNAL, HOST_USER, PathType.PATTERN, "/profile-completion", USER_PROFILE_SETTINGS),
        DFP(INTERNAL, HOST_USER, PathType.PATTERN, "/edit-profile-info", USER_PROFILE_SETTINGS),
        DFP(INTERNAL, HOST_USER, PathType.PATTERN, "/profile-management", USER_PROFILE_SETTINGS),
        DFP(INTERNAL, HOST_USER, PathType.PATTERN, "/webview-kyc", USER_PROFILE_SETTINGS),

        // settingbank
        DFP(INTERNAL, HOST_GLOBAL, PathType.PATH, "/setting-bank", USER_SETTING_BANK),
        DFP(INTERNAL, HOST_GLOBAL, PathType.PATH, "/add-bank"),

        // saldo_details
        DFP(INTERNAL, HOST_GLOBAL, PathType.PATH, "/saldo", FINTECH_SALDO),
        DFP(INTERNAL, HOST_GLOBAL, PathType.PATH, "/saldo-intro", FINTECH_SALDO),

        // kyc_centralized
        DFP(INTERNAL, HOST_USER, PathType.PATTERN, "/user-identification-form"),
        DFP(INTERNAL, HOST_USER, PathType.PATTERN, "/user-identification-info"),
        DFP(INTERNAL, HOST_USER, PathType.PATTERN, "/user-identification-only"),
        DFP(INTERNAL, HOST_USER, PathType.PATTERN, "/goto-kyc.*"),

        // privacycenter
        DFP(INTERNAL, HOST_USER, PathType.PATTERN, "/privacy-center"),
        DFP(INTERNAL, HOST_USER, PathType.PATTERN, "/consent/withdrawal/new"),
        DFP(INTERNAL, HOST_USER, PathType.PATTERN, "/account-linking-webview"),
        DFP(INTERNAL, HOST_USER, PathType.PATTERN, "/search-history"),
        DFP(INTERNAL, HOST_USER, PathType.PATTERN, "/sharing-wishlist"),
        DFP(INTERNAL, HOST_USER, PathType.PATTERN, "/dsar"),
        DFP(INTERNAL, HOST_USER, PathType.PATTERN, "/dsar/add-email"),
    )

    private fun getDfCampaignList() = mutableListOf(
        // campaign_list
        DFP(INTERNAL, HOST_SELLERAPP, PathType.PATTERN, "/campaign-list"),
    )

    private fun getDfFlashSaleTokopedia() = mutableListOf(
        // seller_tokopedia_flash_sale
        DFP(INTERNAL, HOST_SELLERAPP, PathType.PATTERN, "/tokopedia-flash-sale/.*/"),
        DFP(
            INTERNAL, HOST_SELLERAPP, PathType.PATTERN,
            "/tokopedia-flash-sale/campaign-detail/.*/"
        )
    )

    private fun getDfKycSellerapp() = mutableListOf(
        // kyc_centralized
        DFP(INTERNAL, HOST_USER, PathType.PATTERN, "/user-identification-form"),
        DFP(INTERNAL, HOST_USER, PathType.PATTERN, "/user-identification-info"),
        DFP(INTERNAL, HOST_USER, PathType.PATTERN, "/user-identification-only"),
        DFP(INTERNAL, HOST_USER, PathType.PATTERN, "/goto-kyc.*"),
        DFP(INTERNAL, HOST_USER, PathType.PATTERN, "/webview-kyc"),

        // liveness
        DFP(INTERNAL, HOST_USER, PathType.PATTERN, "/liveness-detection"),
    )

    private fun getDfSellerFeedback() = mutableListOf(
        // seller_feedback
        DFP(INTERNAL, HOST_SELLERAPP, PathType.PATH, "/seller-feedback"),
    )

    private fun getDfSellerFrontFunnel() = mutableListOf(
        // seller_persona
        DFP(INTERNAL, HOST_SELLERAPP, PathType.PATH, "/seller-persona"),

        // statistic
        DFP(INTERNAL, HOST_MERCHANT, PathType.PATH, "/statistic_dashboard"),

        // shop_score
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATH, "/shop/performance"),
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATH, "/shop-penalty-old"),
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATH, "/shop-penalty"),
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATH, "/shop-penalty-detail"),
    )

    private fun getDfSellerTalk() = mutableListOf(
        // talk
        DFP(INTERNAL, HOST_GLOBAL, PathType.PATTERN, "/inbox-talk"),
        DFP(INTERNAL, HOST_GLOBAL, PathType.PATTERN, "/product-talk/.*/"),
        DFP(INTERNAL, HOST_GLOBAL, PathType.PATTERN, "/reply-talk/.*/"),
        DFP(INTERNAL, HOST_GLOBAL, PathType.PATTERN, "/add-talk"),
        DFP(INTERNAL, HOST_GLOBAL, PathType.PATTERN, "/talk-seller-settings/"),
    )

    private fun getDfShopSettingsSellerapp() = mutableListOf(
        // shop_settings
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATTERN, "/shop-settings-info"),
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATTERN, "/shop-settings-operational-hours"),
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATTERN, "/shop-settings-edit-schedule"),
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATTERN, "/shop-settings-notes"),
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATTERN, "/shop-settings-etalase/add"),
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATH, "/shop-page-setting"),
        DFP(INTERNAL, HOST_MARKETPLACE, PathType.PATTERN, "/shop-page/.*/settings"),
    )

    fun Map<String, List<DFP>>.mapDF(): MutableList<DFPSchemeToDF> {
        val dfpSchemeToDf = mutableListOf<DFPSchemeToDF>()
        forEach { dfMapItem ->
            dfMapItem.value.forEach { dfpItem ->
                addDFPath(dfpSchemeToDf, dfMapItem.key, dfpItem)
            }
        }
        return dfpSchemeToDf
    }

    private fun addDFPath(
        listDF: MutableList<DFPSchemeToDF>,
        dfModuleName: String,
        dfp: DFP
    ) {
        var schemeInList = listDF.find { it.scheme == dfp.scheme }
        if (schemeInList == null) {
            schemeInList = DFPSchemeToDF(dfp.scheme, mutableListOf())
            listDF.add(schemeInList)
        }
        var hostInList = schemeInList.hostList.find { it.host == dfp.host }
        if (hostInList == null) {
            hostInList = DFPHost(dfp.host, mutableListOf())
            schemeInList.hostList.add(hostInList)
        }
        val pattern = createPattern(dfp)
        val patternInList = hostInList.dfpPathObj.find { pattern?.pattern == it.pattern?.pattern }
        if (patternInList == null) {
            hostInList.dfpPathObj.add(DFPPath(pattern, dfModuleName, dfp.webviewFallbackLogic))
        } else {
            throw RuntimeException("Duplicate Pattern: " + pattern?.pattern + " " + patternInList.pattern + " " + pattern?.pattern)
        }
    }

    private fun createPattern(dfp: DFP): Regex? {
        return when (dfp.pathType) {
            PathType.NO_PATH -> {
                null
            }

            PathType.PREFIX -> {
                (dfp.pathString + ".*").toRegex()
            }

            PathType.PATH -> {
                (dfp.pathString).toRegex()
            }

            PathType.PATTERN -> {
                (dfp.pathString)
                    .replace("\\", "")
                    .replace(".*", "[a-zA-Z0-9_ %-]*").toRegex()
            }
        }
    }
}