package com.tokopedia.applink

import android.content.Context
import android.net.Uri
import chatbot.DeeplinkMapperChatbot.getChatbotDeeplink
import com.tokopedia.applink.Digital_Deals.DeeplinkMapperDeals.getRegisteredNavigationDeals
import com.tokopedia.applink.Hotlist.DeeplinkMapperHotlist.getRegisteredHotlist
import com.tokopedia.applink.category.DeeplinkMapperCategory.getRegisteredCategoryNavigation
import com.tokopedia.applink.category.DeeplinkMapperCategory.getRegisteredNavigationCatalog
import com.tokopedia.applink.category.DeeplinkMapperCategory.getRegisteredNavigationExploreCategory
import com.tokopedia.applink.category.DeeplinkMapperMoneyIn.getRegisteredNavigationMoneyIn
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.content.DeeplinkMapperContent.getRegisteredNavigationContent
import com.tokopedia.applink.content.DeeplinkMapperContent.getRegisteredNavigationPlay
import com.tokopedia.applink.digital.DeeplinkMapperDigital
import com.tokopedia.applink.digital.DeeplinkMapperDigital.getRegisteredNavigationDigital
import com.tokopedia.applink.entertaiment.DeeplinkMapperEntertainment.getRegisteredNavigationEvents
import com.tokopedia.applink.feed.DeepLinkMapperFeed.getRegisteredFeed
import com.tokopedia.applink.find.DeepLinkMapperFind.getRegisteredFind
import com.tokopedia.applink.fintech.DeeplinkMapperFintech.getRegisteredNavigationForFintech
import com.tokopedia.applink.fintech.DeeplinkMapperFintech.getRegisteredNavigationForLayanan
import com.tokopedia.applink.gamification.DeeplinkMapperGamification
import com.tokopedia.applink.internal.*
import com.tokopedia.applink.marketplace.DeeplinkMapperLogistic
import com.tokopedia.applink.marketplace.DeeplinkMapperMarketplace.getRegisteredNavigationMarketplace
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant.getRegisteredNavigationProductReview
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant.getRegisteredNavigationReputation
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant.getRegisteredNavigationShopReview
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant.isShopReview
import com.tokopedia.applink.merchantvoucher.AppLinkMapperMerchantVoucher
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerDelivered
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerHistory
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationOrder
import com.tokopedia.applink.productmanage.DeepLinkMapperProductManage
import com.tokopedia.applink.promo.getRegisteredNavigationTokopoints
import com.tokopedia.applink.recommendation.getRegisteredNavigationRecommendation
import com.tokopedia.applink.salam.DeeplinkMapperSalam.getRegisteredNavigationSalamUmrah
import com.tokopedia.applink.salam.DeeplinkMapperSalam.getRegisteredNavigationSalamUmrahOrderDetail
import com.tokopedia.applink.salam.DeeplinkMapperSalam.getRegisteredNavigationSalamUmrahShop
import com.tokopedia.applink.search.DeeplinkMapperSearch.getRegisteredNavigationSearch
import com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome
import com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome.getSomCancelledAppLink
import com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome.getSomDoneAppLink
import com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome.getSomNewOrderAppLink
import com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome.getSomReadyToShipAppLink
import com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome.getSomShippedAppLink
import com.tokopedia.config.GlobalConfig

/**
 * Function to map the deeplink to applink (registered in manifest)
 *
 * Example when there are 2 deeplink that has the same pattern:
 * tokopedia://product/add and tokopedia://product/{id}
 * tokopedia://product/add will be mapped to tokopedia-android-internal:// to prevent conflict.
 */
object DeeplinkMapper {

    val TOKOPOINTS = "tokopoints"
    /**
     * Get registered deeplink navigation in manifest
     * In conventional term, convert deeplink (http or tokopedia) to applink (tokopedia:// or tokopedia-android-internal://)
     * If deeplink have query parameters then we need to keep the query and map the url without query
     */
    @JvmStatic
    fun getRegisteredNavigation(context: Context, deeplink: String): String {
        val mappedDeepLink: String = when {
            deeplink.startsWith(DeeplinkConstant.SCHEME_HTTP, true) -> {
                val path = Uri.parse(deeplink).pathSegments.joinToString("/")
                when (path) {
                    TOKOPOINTS -> ApplinkConstInternalPromo.TOKOPOINTS_HOME
                    else -> getRegisteredNavigationFromHttp(context, deeplink)
                }
            }
            deeplink.startsWith(DeeplinkConstant.SCHEME_TOKOPEDIA_SLASH, true) -> {
                val uri = Uri.parse(deeplink)
                val query = Uri.parse(deeplink).query
                var tempDeeplink = when {
                    deeplink.startsWith(ApplinkConst.QRSCAN, true) -> ApplinkConstInternalMarketplace.QR_SCANNEER
                    deeplink.startsWith(ApplinkConst.SALAM_UMRAH_SHOP, true) -> getRegisteredNavigationSalamUmrahShop(deeplink, context)
                    deeplink.startsWith(ApplinkConst.TOP_CHAT, true) && isChatBotTrue(deeplink) ->
                        getChatbotDeeplink(deeplink)
                    deeplink.startsWith(ApplinkConst.HOTEL, true) -> deeplink
                    deeplink.startsWith(ApplinkConst.DIGITAL, true) ->
                        getRegisteredNavigationDigital(context, deeplink)
                    deeplink.startsWith(ApplinkConst.DISCOVERY_SEARCH, true) ->
                        getRegisteredNavigationSearch(deeplink)
                    deeplink.startsWith(ApplinkConst.CART) || deeplink.startsWith(ApplinkConst.CHECKOUT) ->
                        getRegisteredNavigationMarketplace(deeplink)
                    deeplink.startsWithPattern(ApplinkConst.DEALS_HOME) ->
                        getRegisteredNavigationDeals(deeplink)
                    deeplink.startsWithPattern(ApplinkConst.FIND) || deeplink.startsWith(ApplinkConst.AMP_FIND) ->
                        getRegisteredFind(deeplink)
                    deeplink.startsWithPattern(ApplinkConst.Digital.DIGITAL_BROWSE) ->
                        getRegisteredNavigationExploreCategory(deeplink)
                    deeplink.startsWithPattern(ApplinkConst.CATEGORY) ->
                        getRegisteredCategoryNavigation(deeplink)
                    deeplink.startsWithPattern(ApplinkConst.PROFILE) ->
                        getRegisteredNavigationContent(deeplink)
                    deeplink.startsWithPattern(ApplinkConst.PLAY_DETAIL) ->
                        getRegisteredNavigationPlay(deeplink)
                    deeplink.startsWithPattern(ApplinkConst.HOME_HOTLIST) ->
                        getRegisteredHotlist(deeplink)
                    deeplink.startsWithPattern(ApplinkConst.PRODUCT_EDIT) -> DeepLinkMapperProductManage.getEditProductInternalAppLink(deeplink)
                    deeplink.startsWith(ApplinkConst.PRODUCT_MANAGE) -> DeepLinkMapperProductManage.getProductListInternalAppLink(deeplink)
                    GlobalConfig.isSellerApp() && deeplink.startsWith(ApplinkConst.HOME) ->
                       ApplinkConstInternalSellerapp.SELLER_HOME
                    deeplink.startsWith(ApplinkConst.PRODUCT_CREATE_REVIEW, true) -> getRegisteredNavigationProductReview(deeplink)
                    deeplink.startsWith(ApplinkConst.REPUTATION, true) -> getRegisteredNavigationReputation(deeplink)
                    deeplink.startsWith(ApplinkConst.TOKOPOINTS) -> getRegisteredNavigationTokopoints(context, deeplink)
                    deeplink.startsWith(ApplinkConst.DEFAULT_RECOMMENDATION_PAGE) -> getRegisteredNavigationRecommendation(deeplink)
                    deeplink.startsWith(ApplinkConst.CHAT_BOT, true) ->
                        getChatbotDeeplink(deeplink)
                    deeplink.startsWith(ApplinkConst.DISCOVERY_CATALOG, true) ->
                        getRegisteredNavigationCatalog(deeplink)
                    deeplink.startsWith(ApplinkConst.MONEYIN, true) ->
                        getRegisteredNavigationMoneyIn(deeplink)
                    deeplink.startsWith(ApplinkConst.OQR_PIN_URL_ENTRY_LINK) ->
                        getRegisteredNavigationForFintech(deeplink)
                    deeplink.startsWith(ApplinkConst.LAYANAN_FINANSIAL) ->
                         getRegisteredNavigationForLayanan(deeplink)
                    deeplink.startsWith(ApplinkConst.SALAM_UMRAH, true) ->
                        getRegisteredNavigationSalamUmrah(deeplink, context)
                    deeplink.startsWith(ApplinkConst.SALAM_UMRAH_ORDER_DETAIL, true) ->
                        getRegisteredNavigationSalamUmrahOrderDetail(deeplink, context)
                    deeplink.startsWith(ApplinkConst.BRAND_LIST, true) ->
                        getBrandlistInternal(deeplink)
                    deeplink.startsWith(ApplinkConst.GOLD_MERCHANT_STATISTIC_DASHBOARD) ->
                        getRegisteredNavigationMarketplace(deeplink)
                    deeplink.startsWith(ApplinkConst.SHOP_SCORE_DETAIL) ->
                        getRegisteredNavigationMarketplace(deeplink)
                    deeplink.startsWith(ApplinkConst.Gamification.CRACK, true) -> DeeplinkMapperGamification.getGamificationDeeplink(deeplink)
                    deeplink.startsWith(ApplinkConst.Gamification.TAP_TAP_MANTAP, true) -> DeeplinkMapperGamification.getGamificationTapTapDeeplink(deeplink)
                    deeplink.startsWith(ApplinkConst.Gamification.DAILY_GIFT_BOX, true) -> DeeplinkMapperGamification.getDailyGiftBoxDeeplink(deeplink)
                    deeplink.startsWith(ApplinkConst.SELLER_ORDER_DETAIL, true) -> getRegisteredNavigationOrder(deeplink)
                    isShopReview(deeplink) -> getRegisteredNavigationShopReview(deeplink)
                    deeplink.startsWith(ApplinkConst.TOPCHAT_IDLESS) -> getRegisteredNavigationTopChat(deeplink)
                    deeplink.startsWith(ApplinkConst.TALK, true) -> getRegisteredNavigationTalk(deeplink)
                    deeplink.startsWith(ApplinkConst.EVENTS,true) -> getRegisteredNavigationEvents(deeplink, context)
                    isProductTalkDeeplink(deeplink) -> getRegisteredNavigationProductTalk(deeplink)
                    isShopTalkDeeplink(deeplink) -> getRegisteredNavigationShopTalk(deeplink)
                    deeplink.startsWith(ApplinkConst.SELLER_NEW_ORDER, true) -> getSomNewOrderAppLink(deeplink)
                    deeplink.startsWith(ApplinkConst.SELLER_PURCHASE_READY_TO_SHIP, true) -> getSomReadyToShipAppLink(deeplink)
                    deeplink.startsWith(ApplinkConst.SELLER_PURCHASE_DELIVERED, true) -> getRegisteredNavigationMainAppSellerDelivered()
                    deeplink.startsWith(ApplinkConst.SELLER_HISTORY, true) -> getRegisteredNavigationMainAppSellerHistory()
                    deeplink.startsWith(ApplinkConst.SELLER_PURCHASE_SHIPPED) -> getSomShippedAppLink(deeplink)
                    GlobalConfig.isSellerApp() && deeplink.startsWith(ApplinkConst.SELLER_NEW_ORDER, true) -> AppLinkMapperSellerHome.getSomNewOrderAppLink(deeplink)
                    GlobalConfig.isSellerApp() && deeplink.startsWith(ApplinkConst.SELLER_SHIPMENT, true) -> AppLinkMapperSellerHome.getSomReadyToShipAppLink(deeplink)
                    deeplink.startsWith(ApplinkConst.SELLER_PURCHASE_FINISHED, true) -> AppLinkMapperSellerHome.getSomDoneAppLink(deeplink)
                    deeplink.startsWith(ApplinkConst.SELLER_PURCHASE_CANCELED, true) -> AppLinkMapperSellerHome.getSomCancelledAppLink(deeplink)
                    deeplink.startsWithPattern(ApplinkConst.FEED_DETAILS) ->
                        getRegisteredFeed(deeplink)
                    DeeplinkMapperMerchant.isShopPageDeeplink(uri) -> DeeplinkMapperMerchant.getShopPageInternalApplink(uri)
                    DeeplinkMapperMerchant.isShopPageHomeDeeplink(uri) -> DeeplinkMapperMerchant.getShopPageHomeInternalApplink(uri)
                    DeeplinkMapperMerchant.isShopPageInfoDeeplink(uri) -> DeeplinkMapperMerchant.getShopPageInfoInternalApplink(uri)
                    DeeplinkMapperMerchant.isShopPageNoteDeeplink(uri) -> DeeplinkMapperMerchant.getShopPageInfoInternalApplink(uri)
                    DeeplinkMapperMerchant.isShopPageResultEtalaseDeepLink(uri) -> DeeplinkMapperMerchant.getShopPageResultEtalaseInternalAppLink(uri)
                    deeplink.startsWith(ApplinkConst.POWER_MERCHANT_SUBSCRIBE) -> ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE
                    deeplink.startsWith(ApplinkConst.SELLER_INFO_DETAIL, true) -> DeeplinkMapperMerchant.getSellerInfoDetailApplink(uri)
                    deeplink.startsWith(ApplinkConst.MERCHANT_VOUCHER_LIST, true) -> AppLinkMapperMerchantVoucher.getInternalMvcListAppLink()
                    deeplink.startsWithPattern(ApplinkConst.ORDER_TRACKING) -> DeeplinkMapperLogistic.getRegisteredNavigationOrder(deeplink)
                    deeplink.startsWith(ApplinkConst.ORDER_HISTORY, true) -> getRegisteredNavigationOrderHistory(uri)
                    deeplink.startsWith(ApplinkConst.RESET_PASSWORD, true) -> ApplinkConstInternalGlobal.FORGOT_PASSWORD
                    else -> {
                        if (specialNavigationMapper(deeplink, ApplinkConst.HOST_CATEGORY_P)) {
                            getRegisteredCategoryNavigation(deeplink)
                        } else if (query?.isNotEmpty() == true) {
                            val tempDL = if (deeplink.contains('?')) {
                                deeplink.substring(0, deeplink.indexOf('?'))
                            } else {
                                deeplink
                            }
                            getRegisteredNavigationFromTokopedia(tempDL)
                        } else getRegisteredNavigationFromTokopedia(deeplink)
                    }
                }
                tempDeeplink = createAppendDeeplinkWithQuery(tempDeeplink, query)
                tempDeeplink
            }
            deeplink.startsWith(DeeplinkConstant.SCHEME_SELLERAPP, true) -> getRegisteredNavigationFromSellerapp(deeplink)
            deeplink.startsWith(ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST) -> DeepLinkMapperProductManage.getProductListInternalAppLink(deeplink)
            deeplink.startsWith(ApplinkConstInternalGlobal.TOPCHAT) -> AppLinkMapperSellerHome.getTopChatAppLink()
            deeplink.startsWith(ApplinkConstInternalOrder.NEW_ORDER) -> getSomNewOrderAppLink(deeplink)
            deeplink.startsWith(ApplinkConstInternalOrder.READY_TO_SHIP) -> getSomReadyToShipAppLink(deeplink)
            deeplink.startsWith(ApplinkConstInternalOrder.SHIPPED) -> getSomShippedAppLink(deeplink)
            deeplink.startsWith(ApplinkConstInternalOrder.FINISHED) -> getSomDoneAppLink(deeplink)
            deeplink.startsWith(ApplinkConstInternalOrder.CANCELLED) -> getSomCancelledAppLink(deeplink)
            else -> deeplink
        }
        return mappedDeepLink
    }

    private fun getRegisteredNavigationOrderHistory(uri: Uri?): String {
        val path = uri?.lastPathSegment ?: ""
        return if (path.isNotEmpty()) {
            UriUtil.buildUri(ApplinkConstInternalMarketplace.ORDER_HISTORY, path)
        } else { "" }
    }

    private fun isProductTalkDeeplink(deeplink: String): Boolean {
        val prefixTalkProductAppLink = "tokopedia://product/"
        val suffixTalkProductAppLink = "/talk"
        return deeplink.startsWith(prefixTalkProductAppLink) and deeplink.endsWith(suffixTalkProductAppLink)
    }

    private fun getRegisteredNavigationProductTalk(deeplink: String): String {
        val uri = Uri.parse(deeplink) ?: return deeplink
        val paths = uri.pathSegments ?: return deeplink
        if (paths.isEmpty() && uri.pathSegments.size >= 2) return deeplink
        val productId = uri.pathSegments[uri.pathSegments.size - 2]
        return "${ApplinkConstInternalGlobal.PRODUCT_TALK_BASE}$productId/"
    }

    private fun isShopTalkDeeplink(deeplink: String): Boolean {
        val prefixShopTalkAppLink = "tokopedia://shop/"
        val suffixShopTalkAppLink = "/talk"
        return deeplink.startsWith(prefixShopTalkAppLink) and deeplink.endsWith(suffixShopTalkAppLink)
    }

    private fun getRegisteredNavigationShopTalk(deeplink: String): String {
        val uri = Uri.parse(deeplink) ?: return deeplink
        val paths = uri.pathSegments ?: return deeplink
        if (paths.isEmpty() && uri.pathSegments.size >= 2) return deeplink
        val shopId = uri.pathSegments[uri.pathSegments.size - 2]
        return "${ApplinkConstInternalGlobal.SHOP_TALK_BASE}$shopId/"
    }

    private fun getRegisteredNavigationTalk(deeplink: String): String {
        val uri = Uri.parse(deeplink)
        val query = uri.query ?: ""
        val path = uri.lastPathSegment ?: ""
        var deepLinkInternal = ApplinkConstInternalGlobal.INBOX_TALK
        if (path.isNotEmpty()) {
            deepLinkInternal = "${ApplinkConstInternalGlobal.TALK_REPLY_BASE}$path/"
        }
        if (query.isNotEmpty()) {
            deepLinkInternal += "?$query"
        }
        return deepLinkInternal
    }

    private fun isChatBotTrue(deeplink: String): Boolean {
        return Uri.parse(deeplink).getQueryParameter("is_chat_bot")?.equals("true") == true
    }

    private fun createAppendDeeplinkWithQuery(deeplink: String, query: String?): String {
        return if (query?.isNotEmpty() == true && deeplink.isNotEmpty()) {
            val questionMarkIndex = deeplink.indexOf("?")
            deeplink + if (questionMarkIndex == -1) {
                "?$query"
            } else {
                "&$query"
            }
        } else {
            deeplink
        }
    }

    private fun getRegisteredNavigationTopChat(deeplink: String): String {
        val query = Uri.parse(deeplink).query
        val path = Uri.parse(deeplink).path
        var deepLinkInternal = ApplinkConstInternalGlobal.TOPCHAT
        if (query?.isNotEmpty() == true || path?.isNotEmpty() == true) {
            deepLinkInternal = "$deepLinkInternal$path?$query"
            return deepLinkInternal
        } else {
            return if (GlobalConfig.isSellerApp()) {
                ApplinkConstInternalSellerapp.SELLER_HOME_CHAT
            } else {
                deepLinkInternal
            }
        }
    }

    /**
     * Mapping http link to registered deplink in manifest (to deeplink tokopedia or tokopedia-android-internal)
     * Due to no differentiation structure link for shop, product with other link (www.tokopedia.com/{shop_domain}/{product_name})
     * the app need translate
     * This function should be called after checking domain shop from server side
     * eg: https://www.tokopedia.com/pulsa/ to tokopedia://pulsa
     */
    fun getRegisteredNavigationFromHttp(context: Context, deeplink: String): String {
        val applinkDigital = DeeplinkMapperDigital.getRegisteredNavigationFromHttpDigital(context, deeplink)
        if (applinkDigital.isNotEmpty()) {
            return applinkDigital
        }
        return ""
    }

    /**
     * Mapping tokopedia link to registered deplink in manifest if necessary
     * eg: tokopedia://product/add to tokopedia-android-internal://marketplace/product-add-item
     * If not found, return current deeplink, means it registered
     */
    private fun getRegisteredNavigationFromTokopedia(deeplink: String): String {
        val trimDeeplink = trimDeeplink(deeplink)
        val mappedDeeplink = when (trimDeeplink) {
            ApplinkConst.PRODUCT_ADD -> ApplinkConstInternalMechant.MERCHANT_OPEN_PRODUCT_PREVIEW
            ApplinkConst.SETTING_PROFILE -> ApplinkConstInternalGlobal.SETTING_PROFILE
            ApplinkConst.ADD_CREDIT_CARD -> ApplinkConstInternalPayment.PAYMENT_ADD_CREDIT_CARD
            ApplinkConst.SETTING_NOTIFICATION -> ApplinkConstInternalMarketplace.USER_NOTIFICATION_SETTING
            ApplinkConst.GROUPCHAT_LIST -> ApplinkConstInternalPlay.GROUPCHAT_LIST
            ApplinkConst.KYC -> ApplinkConstInternalGlobal.USER_IDENTIFICATION_INFO
            ApplinkConst.KYC_NO_PARAM -> ApplinkConstInternalGlobal.USER_IDENTIFICATION_INFO_BASE
            ApplinkConst.KYC_FORM_NO_PARAM -> ApplinkConstInternalGlobal.USER_IDENTIFICATION_FORM_BASE
            ApplinkConst.SETTING_BANK -> ApplinkConstInternalGlobal.SETTING_BANK
            ApplinkConst.ADD_PIN_ONBOARD -> ApplinkConstInternalGlobal.ADD_PIN_ONBOARDING
            ApplinkConst.ADD_FINGERPRINT_ONBOARDING -> ApplinkConstInternalGlobal.ADD_FINGERPRINT_ONBOARDING
            ApplinkConst.FLIGHT -> ApplinkConstInternalTravel.DASHBOARD_FLIGHT
            ApplinkConst.SALDO -> ApplinkConstInternalGlobal.SALDO_DEPOSIT
            ApplinkConst.SALDO_INTRO -> ApplinkConstInternalGlobal.SALDO_INTRO
            ApplinkConst.AFFILIATE_EDUCATION -> ApplinkConstInternalContent.AFFILIATE_EDUCATION
            ApplinkConst.AFFILIATE_DASHBOARD -> ApplinkConstInternalContent.AFFILIATE_DASHBOARD
            ApplinkConst.AFFILIATE_EXPLORE -> ApplinkConstInternalContent.AFFILIATE_EXPLORE
            ApplinkConst.INBOX_TICKET -> ApplinkConstInternalOperational.INTERNAL_INBOX_LIST
            ApplinkConst.INSTANT_LOAN -> ApplinkConstInternalGlobal.GLOBAL_INTERNAL_INSTANT_LOAN
            ApplinkConst.INSTANT_LOAN_TAB -> ApplinkConstInternalGlobal.GLOBAL_INTERNAL_INSTANT_LOAN_TAB
            ApplinkConst.PINJAMAN_ONLINE_TAB -> ApplinkConstInternalGlobal.GLOBAL_INTERNAL_PINJAMAN_ONLINE_TAB
            ApplinkConst.WISHLIST -> ApplinkConsInternalHome.HOME_WISHLIST
            ApplinkConst.NEW_WISHLIST -> ApplinkConsInternalHome.HOME_WISHLIST
            ApplinkConst.CREATE_SHOP -> ApplinkConstInternalGlobal.LANDING_SHOP_CREATION
            ApplinkConst.CHAT_TEMPLATE -> ApplinkConstInternalMarketplace.CHAT_SETTING_TEMPLATE
            ApplinkConst.PRODUCT_MANAGE -> DeepLinkMapperProductManage.getProductListInternalAppLink(deeplink)
            ApplinkConst.NOTIFICATION -> ApplinkConstInternalMarketplace.NOTIFICATION_CENTER
            ApplinkConst.BUYER_INFO -> ApplinkConstInternalMarketplace.NOTIFICATION_BUYER_INFO
            ApplinkConst.CHANGE_PASSWORD -> return ApplinkConstInternalGlobal.CHANGE_PASSWORD
            ApplinkConst.HAS_PASSWORD -> return ApplinkConstInternalGlobal.HAS_PASSWORD
            ApplinkConst.THANK_YOU_PAGE_NATIVE -> ApplinkConstInternalPayment.PAYMENT_THANK_YOU_PAGE
            else -> ""
        }
        if (mappedDeeplink.isNotEmpty()) {
            return mappedDeeplink
        }
        when {
            specialNavigationMapper(deeplink, ApplinkConst.Play.HOST) -> {
                return UriUtil.buildUri(
                        ApplinkConstInternalPlay.GROUPCHAT_DETAIL,
                        getSegments(deeplink).first()
                )
            }
            specialNavigationMapper(deeplink, ApplinkConst.Notification.BUYER_HOST) -> {
                return UriUtil.buildUri(
                        ApplinkConstInternalMarketplace.NOTIFICATION_BUYER_INFO_WITH_ID,
                        getSegments(deeplink).first()
                )
            }
        }
        return ""
    }

    private fun trimDeeplink(deeplink: String): String {
        return if (deeplink.endsWith("/")) {
            deeplink.substringBeforeLast("/")
        } else {
            deeplink
        }
    }

    private fun getSegments(deeplink: String): List<String> {
        return Uri.parse(deeplink).pathSegments
    }

    private fun specialNavigationMapper(deeplink: String, host: String): Boolean {
        val uri = Uri.parse(deeplink)
        return uri.scheme == ApplinkConst.APPLINK_CUSTOMER_SCHEME
                && uri.host == host
                && uri.pathSegments.size > 0
    }

    private fun getBrandlistInternal(deeplink: String): String {
        val parsedUri = Uri.parse(deeplink)
        val segments = parsedUri.pathSegments

        val categoryId = if (segments.size > 1) segments.last() else "0"
        val completedURI = UriUtil.buildUri(ApplinkConstInternalMechant.BRANDLIST, categoryId)
        return completedURI
    }

    /**
     * Mapping sellerapp link to registered deplink in manifest if necessary
     * eg: sellerapp://product/add to tokopedia-android-internal://marketplace/product-add-item
     * If not found, return current deeplink, means it registered
     */
    private fun getRegisteredNavigationFromSellerapp(deeplink: String): String {
        return when (trimDeeplink(deeplink)) {
            ApplinkConst.SellerApp.TOPADS_DASHBOARD -> ApplinkConstInternalTopAds.TOPADS_DASHBOARD_INTERNAL
            ApplinkConst.SellerApp.SELLER_APP_HOME -> ApplinkConstInternalSellerapp.SELLER_HOME
            ApplinkConst.SellerApp.PRODUCT_ADD -> ApplinkConstInternalMechant.MERCHANT_OPEN_PRODUCT_PREVIEW
            ApplinkConst.SellerApp.CREATE_VOUCHER -> ApplinkConstInternalSellerapp.CREATE_VOUCHER
            ApplinkConst.SellerApp.VOUCHER_LIST -> ApplinkConstInternalSellerapp.VOUCHER_LIST
            ApplinkConst.SellerApp.VOUCHER_ACTIVE -> ApplinkConstInternalSellerapp.VOUCHER_ACTIVE
            ApplinkConst.SellerApp.VOUCHER_HISTORY -> ApplinkConstInternalSellerapp.VOUCHER_HISTORY
            ApplinkConst.SellerApp.VOUCHER_DETAIL -> ApplinkConstInternalSellerapp.VOUCHER_DETAIL
            ApplinkConst.SellerApp.POWER_MERCHANT_SUBSCRIBE -> ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE
            ApplinkConst.SETTING_PROFILE -> ApplinkConstInternalGlobal.SETTING_PROFILE
            ApplinkConst.ADD_CREDIT_CARD -> ApplinkConstInternalPayment.PAYMENT_ADD_CREDIT_CARD
            ApplinkConst.SETTING_BANK -> ApplinkConstInternalGlobal.SETTING_BANK
            ApplinkConst.CREATE_SHOP -> ApplinkConstInternalMarketplace.OPEN_SHOP
            ApplinkConst.CHANGE_PASSWORD -> ApplinkConstInternalGlobal.CHANGE_PASSWORD
            ApplinkConst.SELLER_NEW_ORDER -> getSomNewOrderAppLink(deeplink)
            ApplinkConst.SELLER_SHIPMENT -> getSomReadyToShipAppLink(deeplink)
            ApplinkConst.TOP_CHAT -> AppLinkMapperSellerHome.getTopChatAppLink()
            else -> ""
        }
    }
}
