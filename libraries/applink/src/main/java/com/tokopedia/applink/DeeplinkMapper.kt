package com.tokopedia.applink

import android.content.Context
import android.net.Uri
import chatbot.DeeplinkMapperChatbot.getChatbotDeeplink
import com.tokopedia.applink.Digital_Deals.DeeplinkMapperDeals.getRegisteredNavigationDeals
import com.tokopedia.applink.Hotlist.DeeplinkMapperHotlist.getRegisteredHotlist
import com.tokopedia.applink.category.DeeplinkMapperCategory.getRegisteredCategoryNavigation
import com.tokopedia.applink.category.DeeplinkMapperCategory.getRegisteredNavigationExploreCategory
import com.tokopedia.applink.category.DeeplinkMapperMoneyIn.getRegisteredNavigationMoneyIn
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.content.DeeplinkMapperContent.getRegisteredNavigationContent
import com.tokopedia.applink.content.DeeplinkMapperContent.getRegisteredNavigationPlay
import com.tokopedia.applink.digital.DeeplinkMapperDigital
import com.tokopedia.applink.digital.DeeplinkMapperDigital.getRegisteredNavigationDigital
import com.tokopedia.applink.find.DeepLinkMapperFind.getRegisteredFind
import com.tokopedia.applink.fintech.DeeplinkMapperFintech.getRegisteredNavigationForFintech
import com.tokopedia.applink.gamification.DeeplinkMapperGamification
import com.tokopedia.applink.internal.*
import com.tokopedia.applink.marketplace.DeeplinkMapperMarketplace.getRegisteredNavigationMarketplace
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant.getRegisteredNavigationProductReview
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant.getRegisteredNavigationReputation
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant.getRegisteredNavigationShopPage
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant.getRegisteredNavigationShopReview
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant.isShopPage
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant.isShopReview
import com.tokopedia.applink.notification.DeeplinkMapperNotification.getRegisteredNotification
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationOrder
import com.tokopedia.applink.promo.getRegisteredNavigationTokopoints
import com.tokopedia.applink.recommendation.getRegisteredNavigationRecommendation
import com.tokopedia.applink.salam.DeeplinkMapperSalam.getRegisteredNavigationSalamUmrah
import com.tokopedia.applink.salam.DeeplinkMapperSalam.getRegisteredNavigationSalamUmrahOrderDetail
import com.tokopedia.applink.salam.DeeplinkMapperSalam.getRegisteredNavigationSalamUmrahShop
import com.tokopedia.applink.search.DeeplinkMapperSearch.getRegisteredNavigationSearch
import com.tokopedia.config.GlobalConfig

/**
 * Function to map the deeplink to applink (registered in manifest)
 *
 * Example when there are 2 deeplink that has the same pattern:
 * tokopedia://product/add and tokopedia://product/{id}
 * tokopedia://product/add will be mapped to tokopedia-android-internal:// to prevent conflict.
 */
object DeeplinkMapper {

    val TOKOPOINTS="tokopoints"
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
                    deeplink.startsWithPattern(ApplinkConst.PROFILE) ->
                        getRegisteredNavigationContent(deeplink)
                    deeplink.startsWithPattern(ApplinkConst.PLAY_DETAIL) ->
                        getRegisteredNavigationPlay(deeplink)
                    deeplink.startsWithPattern(ApplinkConst.HOME_HOTLIST) ->
                        getRegisteredHotlist(deeplink)
                    GlobalConfig.isSellerApp() && deeplink.startsWith(ApplinkConst.HOME) ->
                        ApplinkConst.SellerApp.SELLER_APP_HOME
                    deeplink.startsWith(ApplinkConst.PRODUCT_CREATE_REVIEW, true) -> getRegisteredNavigationProductReview(deeplink)
                    deeplink.startsWith(ApplinkConst.REPUTATION, true) -> getRegisteredNavigationReputation(deeplink)
                    deeplink.startsWith(ApplinkConst.TOKOPOINTS) -> getRegisteredNavigationTokopoints(context, deeplink)
                    deeplink.startsWith(ApplinkConst.DEFAULT_RECOMMENDATION_PAGE) -> getRegisteredNavigationRecommendation(deeplink)
                    deeplink.startsWith(ApplinkConst.CHAT_BOT, true) ->
                        getChatbotDeeplink(deeplink)
                    deeplink.startsWith(ApplinkConst.MONEYIN, true) ->
                        getRegisteredNavigationMoneyIn(deeplink)
                    deeplink.startsWith(ApplinkConst.OQR_PIN_URL_ENTRY_LINK) ->
                        getRegisteredNavigationForFintech(deeplink)
                    deeplink.startsWith(ApplinkConst.SALAM_UMRAH, true) ->
                        getRegisteredNavigationSalamUmrah(deeplink, context)
                    deeplink.startsWith(ApplinkConst.SALAM_UMRAH_ORDER_DETAIL, true) ->
                        getRegisteredNavigationSalamUmrahOrderDetail(deeplink, context)
                    deeplink.startsWith(ApplinkConst.BRAND_LIST, true) ->
                        getBrandlistInternal(deeplink)
                    deeplink.startsWith(ApplinkConst.Gamification.CRACK, true) -> DeeplinkMapperGamification.getGamificationDeeplink(deeplink)
                    deeplink.startsWith(ApplinkConst.Gamification.TAP_TAP_MANTAP, true) -> DeeplinkMapperGamification.getGamificationTapTapDeeplink(deeplink)
                    deeplink.startsWith(ApplinkConst.SELLER_ORDER_DETAIL, true) -> getRegisteredNavigationOrder(deeplink)
                    isShopReview(deeplink) -> getRegisteredNavigationShopReview(deeplink)
                    deeplink.startsWith(ApplinkConst.TOPCHAT_IDLESS) -> getRegisteredNavigationTopChat(deeplink)
                    deeplink.startsWith(ApplinkConst.TALK, true) -> getRegisteredNavigationTalk(deeplink)
                    deeplink.startsWithPattern(ApplinkConst.BUYER_INFO_WITH_ID) -> getRegisteredNotification(deeplink)
                    isProductTalkDeeplink(deeplink) -> getRegisteredNavigationProductTalk(deeplink)
                    isShopTalkDeeplink(deeplink) -> getRegisteredNavigationShopTalk(deeplink)
                    else -> {
                        if (specialNavigationMapper(deeplink, ApplinkConst.HOST_CATEGORY_P)) {
                            getRegisteredCategoryNavigation(getSegments(deeplink), deeplink)
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
            else -> deeplink
        }
        return mappedDeepLink
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
        if (path.isNotEmpty()){
            deepLinkInternal = "${ApplinkConstInternalGlobal.DETAIL_TALK_BASE}$path/"
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
        if(query?.isNotEmpty() == true || path?.isNotEmpty() == true){
            deepLinkInternal = "$deepLinkInternal$path?$query"
            return deepLinkInternal
        } else {
            return deepLinkInternal
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
            ApplinkConst.PRODUCT_ADD -> ApplinkConstInternalMarketplace.PRODUCT_ADD_ITEM
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
            ApplinkConst.PRODUCT_MANAGE -> ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST
            ApplinkConst.NOTIFICATION -> ApplinkConstInternalMarketplace.NOTIFICATION_CENTER
            ApplinkConst.CHANGE_PASSWORD -> return ApplinkConstInternalGlobal.CHANGE_PASSWORD
            else -> ""
        }
        if (mappedDeeplink.isNotEmpty()) {
            return mappedDeeplink
        }
        when {
            specialNavigationMapper(deeplink, ApplinkConst.Play.HOST) -> {
                return UriUtil.buildUri(ApplinkConstInternalPlay.GROUPCHAT_DETAIL, getSegments(deeplink).first())
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
        val trimDeeplink = trimDeeplink(deeplink)
        return when (trimDeeplink) {
            ApplinkConst.SellerApp.PRODUCT_ADD -> ApplinkConstInternalMarketplace.PRODUCT_ADD_ITEM
            ApplinkConst.SETTING_PROFILE -> ApplinkConstInternalGlobal.SETTING_PROFILE
            ApplinkConst.ADD_CREDIT_CARD -> ApplinkConstInternalPayment.PAYMENT_ADD_CREDIT_CARD
            ApplinkConst.SETTING_BANK -> ApplinkConstInternalGlobal.SETTING_BANK
            ApplinkConst.CREATE_SHOP -> ApplinkConstInternalMarketplace.OPEN_SHOP
            ApplinkConst.PRODUCT_MANAGE -> ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST
            else -> ""
        }
    }
}
