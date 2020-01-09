package com.tokopedia.applink

import android.content.Context
import android.net.Uri
import chatbot.DeeplinkMapperChatbot.getChatbotDeeplink
import com.tokopedia.applink.Digital_Deals.DeeplinkMapperDeals.getRegisteredNavigationDeals
import com.tokopedia.applink.Hotlist.DeeplinkMapperHotlist.getRegisteredHotlist
import com.tokopedia.applink.category.DeeplinkMapperCategory.getRegisteredCategoryNavigation
import com.tokopedia.applink.category.DeeplinkMapperMoneyIn.getRegisteredNavigationMoneyIn
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.content.DeeplinkMapperContent.getRegisteredNavigationContent
import com.tokopedia.applink.digital.DeeplinkMapperDigital
import com.tokopedia.applink.digital.DeeplinkMapperDigital.getRegisteredNavigationDigital
import com.tokopedia.applink.fintech.DeeplinkMapperFintech.getRegisteredNavigationForFintech
import com.tokopedia.applink.internal.*
import com.tokopedia.applink.marketplace.DeeplinkMapperMarketplace.getRegisteredNavigationMarketplace
import com.tokopedia.applink.promo.getRegisteredNavigationTokopoints
import com.tokopedia.applink.recommendation.getRegisteredNavigationRecommendation
import com.tokopedia.applink.search.DeeplinkMapperSearch.getRegisteredNavigationSearch
import com.tokopedia.config.GlobalConfig
import com.tokopedia.applink.internal.ApplinkConstInternalTravel
import com.tokopedia.applink.salam.DeeplinkMapperSalam.getRegisteredNavigationSalamUmrah
import com.tokopedia.applink.salam.DeeplinkMapperSalam.getRegisteredNavigationSalamUmrahOrderDetail

/**
 * Function to map the deeplink to applink (registered in manifest)
 *
 * Example when there are 2 deeplink that has the same pattern:
 * tokopedia://product/add and tokopedia://product/{id}
 * tokopedia://product/add will be mapped to tokopedia-android-internal:// to prevent conflict.
 */
object DeeplinkMapper {

    /**
     * Get registered deeplink navigation in manifest
     * In conventional term, convert deeplink (http or tokopedia) to applink (tokopedia:// or tokopedia-android-internal://)
     * If deeplink have query parameters then we need to keep the query and map the url without query
     */
    @JvmStatic
    fun getRegisteredNavigation(context: Context, deeplink: String): String {
        val mappedDeepLink: String = when {
            deeplink.startsWith(DeeplinkConstant.SCHEME_HTTP, true) -> getRegisteredNavigationFromHttp(context, deeplink)
            deeplink.startsWith(DeeplinkConstant.SCHEME_TOKOPEDIA_SLASH, true) -> {
                val query = Uri.parse(deeplink).query
                var tempDeeplink = when {
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
                    deeplink.startsWithPattern(ApplinkConst.PROFILE) ->
                        getRegisteredNavigationContent(deeplink)
                    deeplink.startsWithPattern(ApplinkConst.HOME_HOTLIST) ->
                        getRegisteredHotlist(context, deeplink)
                    GlobalConfig.isSellerApp() && deeplink.startsWith(ApplinkConst.HOME) ->
                        ApplinkConst.SellerApp.SELLER_APP_HOME
                    deeplink.startsWith(ApplinkConst.PRODUCT_CREATE_REVIEW, true) ->
                        getCreateReviewInternal(deeplink)
                    deeplink.startsWith(ApplinkConst.TOKOPOINTS) -> getRegisteredNavigationTokopoints(context, deeplink)
                    deeplink.startsWith(ApplinkConst.DEFAULT_RECOMMENDATION_PAGE) -> getRegisteredNavigationRecommendation(deeplink)
                    deeplink.startsWith(ApplinkConst.CHAT_BOT, true) ->
                        getChatbotDeeplink(deeplink)
                    deeplink.startsWith(ApplinkConst.MONEYIN, true) ->
                        getRegisteredNavigationMoneyIn(deeplink)
                    deeplink.startsWith(ApplinkConst.OQR_PIN_URL_ENTRY_LINK) ->
                        getRegisteredNavigationForFintech(deeplink)
                    deeplink.startsWith(ApplinkConst.SALAM_UMRAH,true) ->
                        getRegisteredNavigationSalamUmrah(deeplink, context)
                    deeplink.startsWith(ApplinkConst.SALAM_UMRAH_ORDER_DETAIL,true) ->
                        getRegisteredNavigationSalamUmrahOrderDetail(deeplink, context)
                    else -> {
                        if(specialNavigationMapper(deeplink,ApplinkConst.HOST_CATEGORY_P)){
                            getRegisteredCategoryNavigation(getSegments(deeplink),deeplink)
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
            ApplinkConst.PRODUCT_ADD ->  ApplinkConstInternalMarketplace.PRODUCT_ADD_ITEM
            ApplinkConst.SETTING_PROFILE -> ApplinkConstInternalGlobal.SETTING_PROFILE
            ApplinkConst.ADD_CREDIT_CARD -> ApplinkConstInternalPayment.PAYMENT_ADD_CREDIT_CARD
            ApplinkConst.SETTING_NOTIFICATION -> ApplinkConstInternalMarketplace.USER_NOTIFICATION_SETTING
            ApplinkConst.GROUPCHAT_LIST -> ApplinkConstInternalPlay.GROUPCHAT_LIST
            ApplinkConst.KYC -> ApplinkConstInternalGlobal.USER_IDENTIFICATION_INFO
            ApplinkConst.KYC_NO_PARAM -> ApplinkConstInternalGlobal.USER_IDENTIFICATION_INFO_BASE
            ApplinkConst.KYC_FORM_NO_PARAM -> ApplinkConstInternalGlobal.USER_IDENTIFICATION_FORM_BASE
            ApplinkConst.SETTING_BANK -> ApplinkConstInternalGlobal.SETTING_BANK
            ApplinkConst.ADD_PIN_ONBOARD -> ApplinkConstInternalGlobal.ADD_PIN_ONBOARDING
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
            ApplinkConst.NEW_WISHLIST -> ApplinkConsInternalHome.HOME_WISHLIST
            ApplinkConst.CREATE_SHOP -> ApplinkConstInternalMarketplace.OPEN_SHOP
            ApplinkConst.CHAT_TEMPLATE -> ApplinkConstInternalMarketplace.CHAT_SETTING_TEMPLATE
            ApplinkConst.PRODUCT_MANAGE -> ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST
            ApplinkConst.NOTIFICATION -> ApplinkConstInternalMarketplace.NOTIFICATION_CENTER
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

    private fun trimDeeplink(deeplink:String) = deeplink.substringBeforeLast("/")

    private fun getSegments(deeplink: String): List<String> {
        return Uri.parse(deeplink).pathSegments
    }

    private fun specialNavigationMapper(deeplink: String, host: String): Boolean {
        val uri = Uri.parse(deeplink)
        return uri.scheme == ApplinkConst.APPLINK_CUSTOMER_SCHEME
                && uri.host == host
                && uri.pathSegments.size > 0
    }

    private fun getCreateReviewInternal(deeplink: String): String {
        val parsedUri = Uri.parse(deeplink)
        val segments = parsedUri.pathSegments
        val rating = parsedUri.getQueryParameter("rating")?: "5"

        val reputationId = segments[segments.size - 2]
        val productId = segments.last()
        return UriUtil.buildUri(ApplinkConstInternalMarketplace.CREATE_REVIEW, reputationId, productId, rating)
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
