package com.tokopedia.applink

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import chatbot.DeeplinkMapperChatbot.getChatbotDeeplink
import com.tokopedia.applink.Hotlist.DeeplinkMapperHotlist.getRegisteredHotlist
import com.tokopedia.applink.account.DeeplinkMapperAccount
import com.tokopedia.applink.category.DeeplinkMapperCategory.getRegisteredCategoryNavigation
import com.tokopedia.applink.category.DeeplinkMapperCategory.getRegisteredNavigationCatalog
import com.tokopedia.applink.category.DeeplinkMapperCategory.getRegisteredNavigationExploreCategory
import com.tokopedia.applink.category.DeeplinkMapperCategory.getRegisteredTradeinNavigation
import com.tokopedia.applink.category.DeeplinkMapperMoneyIn.getRegisteredNavigationMoneyIn
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.content.DeeplinkMapperContent
import com.tokopedia.applink.content.DeeplinkMapperContent.getContentCreatePostDeepLink
import com.tokopedia.applink.content.DeeplinkMapperContent.getKolDeepLink
import com.tokopedia.applink.digital.DeeplinkMapperDigital
import com.tokopedia.applink.digital.DeeplinkMapperDigital.getRegisteredNavigationDigital
import com.tokopedia.applink.digitaldeals.DeeplinkMapperDeals.getRegisteredNavigationDeals
import com.tokopedia.applink.entertaiment.DeeplinkMapperEntertainment.getRegisteredNavigationEvents
import com.tokopedia.applink.etalase.DeepLinkMapperEtalase
import com.tokopedia.applink.feed.DeepLinkMapperFeed.getRegisteredFeed
import com.tokopedia.applink.find.DeepLinkMapperFind.getRegisteredFind
import com.tokopedia.applink.fintech.DeeplinkMapperFintech
import com.tokopedia.applink.fintech.DeeplinkMapperFintech.getRegisteredNavigationForFintech
import com.tokopedia.applink.fintech.DeeplinkMapperFintech.getRegisteredNavigationForLayanan
import com.tokopedia.applink.gamification.DeeplinkMapperGamification
import com.tokopedia.applink.home.DeeplinkMapperHome
import com.tokopedia.applink.home.DeeplinkMapperHome.getRegisteredExplore
import com.tokopedia.applink.home.DeeplinkMapperHome.getRegisteredNavigationHome
import com.tokopedia.applink.home.DeeplinkMapperHome.getRegisteredNavigationHomeContentExplore
import com.tokopedia.applink.home.DeeplinkMapperHome.getRegisteredNavigationHomeFeed
import com.tokopedia.applink.home.DeeplinkMapperHome.getRegisteredNavigationHomeOfficialStore
import com.tokopedia.applink.internal.*
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.getDiscoveryDeeplink
import com.tokopedia.applink.marketplace.DeeplinkMapperLogistic
import com.tokopedia.applink.marketplace.DeeplinkMapperMarketplace.getRegisteredNavigationMarketplace
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant.getRegisteredNavigationProductDetailReview
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant.getRegisteredNavigationProductReview
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant.getRegisteredNavigationReputation
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant.getRegisteredNavigationReviewReminder
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant.getRegisteredNavigationSellerReviewDetail
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant.getRegisteredNavigationShopReview
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant.isShopReview
import com.tokopedia.applink.order.DeeplinkMapperOrder
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerAwbChange
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerAwbInvalid
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerComplaint
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerDelivered
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerRetur
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerWaitingAwb
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerWaitingPickup
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationOrder
import com.tokopedia.applink.order.DeeplinkMapperUohOrder
import com.tokopedia.applink.powermerchant.PowerMerchantDeepLinkMapper
import com.tokopedia.applink.productmanage.DeepLinkMapperProductManage
import com.tokopedia.applink.promo.getRegisteredNavigationTokopoints
import com.tokopedia.applink.recommendation.getRegisteredNavigationRecommendation
import com.tokopedia.applink.review.ReviewApplinkConst
import com.tokopedia.applink.salam.DeeplinkMapperSalam.getRegisteredNavigationSalamUmrah
import com.tokopedia.applink.salam.DeeplinkMapperSalam.getRegisteredNavigationSalamUmrahOrderDetail
import com.tokopedia.applink.salam.DeeplinkMapperSalam.getRegisteredNavigationSalamUmrahShop
import com.tokopedia.applink.search.DeeplinkMapperSearch.getRegisteredNavigationSearch
import com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome
import com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome.getSomAllOrderAppLink
import com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome.getSomCancellationRequestAppLink
import com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome.getSomCancelledAppLink
import com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome.getSomDoneAppLink
import com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome.getSomNewOrderAppLink
import com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome.getSomReadyToShipAppLink
import com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome.getSomShippedAppLink
import com.tokopedia.applink.statistic.DeepLinkMapperStatistic
import com.tokopedia.applink.travel.DeeplinkMapperTravel
import com.tokopedia.config.GlobalConfig
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import org.json.JSONObject

/**
 * Function to map the deeplink to applink (registered in manifest)
 *
 * Example when there are 2 deeplink that has the same pattern:
 * tokopedia://product/add and tokopedia://product/{id}
 * tokopedia://product/add will be mapped to tokopedia-android-internal:// to prevent conflict.
 */
object DeeplinkMapper {

    const val MAINAPP_SWITCH_TO_WEBVIEW = "android_mainapp_switch_to_webview"
    const val SELLERAPP_SWITCH_TO_WEBVIEW = "android_sellerapp_switch_to_webview"

    val TOKOPOINTS = "tokopoints"
    val LOCK = Any()
    /**
     * Get registered deeplink navigation in manifest
     * In conventional term, convert deeplink (http or tokopedia) to applink (tokopedia:// or tokopedia-android-internal://)
     * If deeplink have query parameters then we need to keep the query and map the url without query
     */
    @JvmStatic
    fun getRegisteredNavigation(context: Context, deeplink: String): String {
        val uri = Uri.parse(deeplink)
        val scheme = uri.scheme
        val mappedDeepLink: String = when (scheme) {
            DeeplinkConstant.SCHEME_HTTP,
            DeeplinkConstant.SCHEME_HTTPS -> {
                getRegisteredNavigationFromHttp(context, uri, deeplink)
            }
            DeeplinkConstant.SCHEME_TOKOPEDIA -> {
                val query = uri.query
                val tempDeeplink = getRegisteredNavigationFromTokopedia(context, uri, deeplink)
                createAppendDeeplinkWithQuery(tempDeeplink, query)
            }
            DeeplinkConstant.SCHEME_SELLERAPP -> {
                val query = uri.query
                val tempDeeplink = getRegisteredNavigationFromSellerapp(uri, deeplink)
                createAppendDeeplinkWithQuery(tempDeeplink, query)
            }
            DeeplinkConstant.SCHEME_INTERNAL -> {
                getRegisteredNavigationFromInternalTokopedia(context, uri, deeplink)
            }
            else -> deeplink
        }
        return switchToWebviewIfNeeded(context, mappedDeepLink, deeplink)
    }

    private fun switchToWebviewIfNeeded(context: Context, mappedLink: String, originalLink: String) : String {
        try {
            val remoteConfig = FirebaseRemoteConfigImpl(context)
            var webviewSwitchConfig = ""

            if (GlobalConfig.isSellerApp()) {
                webviewSwitchConfig = remoteConfig.getString(SELLERAPP_SWITCH_TO_WEBVIEW)
            } else {
                webviewSwitchConfig = remoteConfig.getString(MAINAPP_SWITCH_TO_WEBVIEW)
            }

            if (TextUtils.isEmpty(webviewSwitchConfig)) return mappedLink

            val configJSON = JSONObject(webviewSwitchConfig)

            val link = if (!TextUtils.isEmpty(mappedLink)) mappedLink else originalLink
            val uri = Uri.parse(link)
            val trimmedDeeplink = trimDeeplink(uri, link)

            val switchData = configJSON.optJSONObject(trimmedDeeplink)

            if (switchData == null) return mappedLink

            val environment = switchData.optString("environment")
            val versions = switchData.optString("versions")
            val weblink = switchData.optString("weblink")

            if (GlobalConfig.isAllowDebuggingTools() && environment != "dev") return mappedLink
            if (!GlobalConfig.isAllowDebuggingTools() && environment != "prod") return mappedLink

            val versionList = versions.split(",")
            if (GlobalConfig.VERSION_NAME !in versionList) return mappedLink

            val webviewApplink = UriUtil.buildUri(ApplinkConstInternalGlobal.WEBVIEW, weblink)

            ServerLogger.log(Priority.P1, "WEBVIEW_SWITCH", mapOf("type" to link, "url" to weblink))

            return createAppendDeeplinkWithQuery(webviewApplink, uri.query)
        } catch (e: Exception) { return mappedLink }
    }

    private fun getRegisteredNavigationOrderHistory(uri: Uri?): String {
        val path = uri?.lastPathSegment ?: ""
        return if (path.isNotEmpty()) {
            UriUtil.buildUri(ApplinkConstInternalMarketplace.ORDER_HISTORY, path)
        } else {
            ""
        }
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
        val paramFilter = "filter"
        var deepLinkInternal = ApplinkConstInternalGlobal.INBOX_TALK
        if (GlobalConfig.isSellerApp()) {
            if (uri.getQueryParameter(paramFilter)?.isNotBlank() == true) {
                return Uri.parse(deepLinkInternal)
                        .buildUpon()
                        .appendQueryParameter(paramFilter, uri.getQueryParameter(paramFilter))
                        .build().toString()
            }
        }
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

    fun createAppendDeeplinkWithQuery(deeplink: String, query: String?): String {
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
    fun getRegisteredNavigationFromHttp(context: Context, uri: Uri, deeplink: String): String {

        if (uri.pathSegments.joinToString("/") == TOKOPOINTS || uri.pathSegments.joinToString("/") == ApplinkConst.RewardFallback.Reward.REWARDS) {
            return ApplinkConstInternalPromo.TOKOPOINTS_HOME
        }
        if (deeplink.startsWithPattern(ApplinkConstInternalContent.TOKOPEDIA_BYME_HTTP) || deeplink.startsWithPattern(ApplinkConstInternalContent.TOKOPEDIA_BYME_HTTPS)) {
            return DeeplinkMapperContent.getRegisteredNavigationContentFromHttp(deeplink)
        }

        val applinkDigital = DeeplinkMapperDigital.getRegisteredNavigationFromHttpDigital(context, deeplink)
        if (applinkDigital.isNotEmpty()) {
            return applinkDigital
        }
        return ""
    }

    private val deeplinkPatternTokopediaSchemeList: MutableList<DLP> = listOf(
            DLP(logic = { _, _, deeplink -> !GlobalConfig.isSellerApp() && deeplink.startsWith(ApplinkConst.HOME) },
                    targetDeeplink = { _, _, deeplink -> getRegisteredNavigationHome(deeplink) }),
            DLP(logic = { _, _, deeplink -> GlobalConfig.isSellerApp() && deeplink.startsWith(ApplinkConst.HOME) },
                    targetDeeplink = { _, _, _ -> ApplinkConstInternalSellerapp.SELLER_HOME }),
            DLP(logic = { _, _, deeplink -> DeeplinkMapperMerchant.isProductDetailPageDeeplink(deeplink) },
                    targetDeeplink = { _, _, deeplink -> DeeplinkMapperMerchant.getRegisteredProductDetail(deeplink) }),
            DLP(logic = { _, _, deeplink -> DeeplinkMapperMerchant.isProductDetailAffiliatePageDeeplink(deeplink) },
                    targetDeeplink = { _, _, deeplink -> DeeplinkMapperMerchant.getRegisteredProductDetailAffiliate(deeplink) }),
            DLP.startWith(ApplinkConst.INBOX) { _, _, deeplink -> DeeplinkMapperHome.getRegisteredInboxNavigation(deeplink) },
            DLP.startWith(ApplinkConst.QRSCAN, ApplinkConstInternalMarketplace.QR_SCANNEER),
            DLP.startWith(ApplinkConst.SALAM_UMRAH_SHOP) { ctx, _, deeplink -> getRegisteredNavigationSalamUmrahShop(deeplink, ctx) },
            DLP(logic = { _, _, deeplink -> deeplink.startsWith(ApplinkConst.TOP_CHAT, true) && isChatBotTrue(deeplink) },
                    targetDeeplink = { _, _, deeplink -> getChatbotDeeplink(deeplink) }),
            DLP(logic = { _, uri, deeplink -> deeplink.startsWith(ApplinkConst.TOP_CHAT, true) && AppLinkMapperSellerHome.shouldRedirectToSellerApp(uri) },
                    targetDeeplink = { _, uri, _ -> AppLinkMapperSellerHome.getTopChatAppLink(uri) }),
            DLP(logic = {_, uri, _ -> (uri.host == ReviewApplinkConst.AUTHORITY_PRODUCT && uri.pathSegments.last() == ReviewApplinkConst.PATH_REVIEW) }, targetDeeplink = { _, uri, _ -> getRegisteredNavigationProductDetailReview(uri) }),
            DLP.startWith(ApplinkConst.ACCOUNT) {ctx, _, deeplink-> DeeplinkMapperAccount.getAccountInternalApplink(ctx, deeplink)},
            DLP.exact(ApplinkConst.BELANJA_ORDER) { ctx, _, deeplink-> DeeplinkMapperUohOrder.getRegisteredNavigationUohOrder(ctx, deeplink) },
            DLP.startWith(ApplinkConst.MARKETPLACE_ORDER) { ctx, _, deeplink-> DeeplinkMapperUohOrder.getRegisteredNavigationUohOrder(ctx, deeplink) },
            DLP.startWith(ApplinkConst.MARKETPLACE_ORDER_SUB) { ctx, _, deeplink -> DeeplinkMapperUohOrder.getRegisteredNavigationUohOrder(ctx, deeplink) },
            DLP.exact(ApplinkConst.ORDER_LIST) { ctx, _, deeplink -> DeeplinkMapperUohOrder.getRegisteredNavigationUohOrder(ctx, deeplink) },
            DLP.exact(ApplinkConst.ORDER_LIST_WEBVIEW) { ctx, _, deeplink -> DeeplinkMapperUohOrder.getRegisteredNavigationUohOrder(ctx, deeplink) },
            DLP.startWith(ApplinkConst.DIGITAL_ORDER) { ctx, _, deeplink -> DeeplinkMapperUohOrder.getRegisteredNavigationUohOrder(ctx, deeplink) },
            DLP.exact(ApplinkConst.Transaction.ORDER_HISTORY) { ctx, _, deeplink -> DeeplinkMapperUohOrder.getRegisteredNavigationUohOrder(ctx, deeplink) },
            DLP.exact(ApplinkConst.EVENTS_ORDER) { ctx, _, deeplink -> DeeplinkMapperUohOrder.getRegisteredNavigationUohOrder(ctx, deeplink) },
            DLP.exact(ApplinkConst.DEALS_ORDER) { ctx, _, deeplink -> DeeplinkMapperUohOrder.getRegisteredNavigationUohOrder(ctx, deeplink) },
            DLP.exact(ApplinkConst.FLIGHT_ORDER) { ctx, _, deeplink -> DeeplinkMapperUohOrder.getRegisteredNavigationUohOrder(ctx, deeplink) },
            DLP.exact(ApplinkConst.TRAIN_ORDER) { ctx, _, deeplink -> DeeplinkMapperUohOrder.getRegisteredNavigationUohOrder(ctx, deeplink)},
            DLP.exact(ApplinkConst.GIFT_CARDS_ORDER) { ctx, _, deeplink -> DeeplinkMapperUohOrder.getRegisteredNavigationUohOrder(ctx, deeplink) },
            DLP.exact(ApplinkConst.INSURANCE_ORDER) { ctx, _, deeplink -> DeeplinkMapperUohOrder.getRegisteredNavigationUohOrder(ctx, deeplink) },
            DLP.exact(ApplinkConst.MODAL_TOKO_ORDER) { ctx, _, deeplink -> DeeplinkMapperUohOrder.getRegisteredNavigationUohOrder(ctx, deeplink) },
            DLP.exact(ApplinkConst.HOTEL_ORDER) { ctx, _, deeplink -> DeeplinkMapperUohOrder.getRegisteredNavigationUohOrder(ctx, deeplink) },
            DLP.exact(ApplinkConst.ORDER_LIST) { ctx, _, deeplink -> DeeplinkMapperUohOrder.getRegisteredNavigationUohOrder(ctx, deeplink) },
            DLP.exact(ApplinkConst.ORDER_LIST_WEBVIEW) { ctx, _, deeplink -> DeeplinkMapperUohOrder.getRegisteredNavigationUohOrder(ctx, deeplink) },
            DLP.exact(ApplinkConst.PURCHASE_ORDER) { ctx, _, deeplink -> DeeplinkMapperUohOrder.getRegisteredNavigationUohOrder(ctx, deeplink) },
            DLP.exact(ApplinkConst.PURCHASE_CONFIRMED) { ctx, _, deeplink -> DeeplinkMapperUohOrder.getRegisteredNavigationUohOrder(ctx, deeplink) },
            DLP.exact(ApplinkConst.PURCHASE_PROCESSED) { ctx, _, deeplink -> DeeplinkMapperUohOrder.getRegisteredNavigationUohOrder(ctx, deeplink) },
            DLP.exact(ApplinkConst.PURCHASE_SHIPPING_CONFIRM) { ctx, _, deeplink -> DeeplinkMapperUohOrder.getRegisteredNavigationUohOrder(ctx, deeplink) },
            DLP.exact(ApplinkConst.PURCHASE_SHIPPED) { ctx, _, deeplink -> DeeplinkMapperUohOrder.getRegisteredNavigationUohOrder(ctx, deeplink) },
            DLP.exact(ApplinkConst.PURCHASE_DELIVERED) { ctx, _, deeplink -> DeeplinkMapperUohOrder.getRegisteredNavigationUohOrder(ctx, deeplink) },
            DLP.startWith(ApplinkConst.PURCHASE_HISTORY) { ctx, _, deeplink -> DeeplinkMapperUohOrder.getRegisteredNavigationUohOrder(ctx, deeplink) },
            DLP.exact(ApplinkConst.ORDER_HISTORY) { ctx, _, deeplink -> DeeplinkMapperUohOrder.getRegisteredNavigationUohOrder(ctx, deeplink) },
            DLP.startWith(ApplinkConst.OMS_ORDER_DETAIL) { ctx, _, deeplink -> DeeplinkMapperUohOrder.getRegisteredNavigationUohOrder(ctx, deeplink) },
            DLP.exact(ApplinkConst.TRAVEL_SUBHOMEPAGE_HOME) { ctx , _, deeplink -> getRegisteredNavigationDigital(ctx, deeplink) },
            DLP.exact(ApplinkConst.TRAVEL_AND_ENTERTAINMENT_ORDER) { ctx, _, deeplink -> DeeplinkMapperUohOrder.getRegisteredNavigationUohOrder(ctx, deeplink) },
            DLP.exact(ApplinkConst.PURCHASE_ONGOING) { ctx , _, deeplink -> DeeplinkMapperUohOrder.getRegisteredNavigationUohOrder(ctx, deeplink) },
            DLP.startWith(ApplinkConst.HOTEL) {ctx, _, deeplink -> DeeplinkMapperTravel.getRegisteredNavigationTravel(ctx, deeplink) },
            DLP.startWith(ApplinkConst.DIGITAL) { ctx, _, deeplink -> getRegisteredNavigationDigital(ctx, deeplink) },
            DLP.startWith(ApplinkConst.RECHARGE) { ctx, _, deeplink -> getRegisteredNavigationDigital(ctx, deeplink) },
            DLP.startWith(ApplinkConst.DISCOVERY_SEARCH) { _, _, deeplink -> getRegisteredNavigationSearch(deeplink) },
            DLP.startWith(ApplinkConst.CART) { _, _, deeplink -> getRegisteredNavigationMarketplace(deeplink) },
            DLP.startWith(ApplinkConst.CHECKOUT) { _, _, deeplink -> getRegisteredNavigationMarketplace(deeplink) },
            DLP.startWith(ApplinkConst.OCC) { _, _, deeplink -> getRegisteredNavigationMarketplace(deeplink) },
            DLP.startWithPattern(ApplinkConst.DEALS_HOME) { ctx, _, deeplink -> getRegisteredNavigationDeals(ctx, deeplink) },
            DLP.startWithPattern(ApplinkConst.FIND) { _, _, deeplink -> getRegisteredFind(deeplink) },
            DLP.startWith(ApplinkConst.AMP_FIND) { _, _, deeplink -> getRegisteredFind(deeplink) },
            DLP.startWithPattern(ApplinkConst.Digital.DIGITAL_BROWSE) { _, _, deeplink -> getRegisteredNavigationExploreCategory(deeplink) },
            DLP.startWithPattern(ApplinkConst.TRADEIN) { _, _, deeplink -> getRegisteredTradeinNavigation(deeplink) },
            DLP.startWithPattern(ApplinkConst.CATEGORY) { _, _, deeplink -> getRegisteredCategoryNavigation(deeplink) },
            DLP.startWithPattern(ApplinkConst.PROFILE) { _, _, deeplink -> DeeplinkMapperContent.getRegisteredNavigation(deeplink) },
            DLP.startWithPattern(ApplinkConst.PLAY_DETAIL) { _, _, deeplink -> DeeplinkMapperContent.getRegisteredNavigation(deeplink) },
            DLP.startWithPattern(ApplinkConst.PLAY_BROADCASTER) { _, _, deeplink -> DeeplinkMapperContent.getRegisteredNavigation(deeplink) },
            DLP.startWithPattern(ApplinkConst.HOME_HOTLIST) { _, _, deeplink -> getRegisteredHotlist(deeplink) },
            DLP.startWithPattern(ApplinkConst.PRODUCT_EDIT) { _, _, deeplink -> DeepLinkMapperProductManage.getEditProductInternalAppLink(deeplink) },
            DLP.startWithPattern(ApplinkConst.SHOP_ETALASE_LIST) { _, _, deeplink -> DeepLinkMapperEtalase.getEtalaseListInternalAppLink(deeplink) },
            DLP.startWith(ApplinkConst.PRODUCT_MANAGE) { _, _, deeplink -> DeepLinkMapperProductManage.getProductListInternalAppLink(deeplink) },
            DLP.startWith(ApplinkConst.PRODUCT_CREATE_REVIEW) { _, _, deeplink -> getRegisteredNavigationProductReview(deeplink) },
            DLP.startWith(ApplinkConst.REVIEW_REMINDER) { _,_, deeplink -> getRegisteredNavigationReviewReminder(deeplink) },
            DLP.startWith(ApplinkConst.REPUTATION) { _, _, deeplink -> getRegisteredNavigationReputation(deeplink) },
            DLP.startWith(ApplinkConst.SELLER_REVIEW) { _,_, deeplink -> getRegisteredNavigationSellerReviewDetail(deeplink) },
            DLP.startWith(ApplinkConst.TOKOPOINTS) { ctx, _, deeplink -> getRegisteredNavigationTokopoints(ctx, deeplink) },
            DLP.startWith(ApplinkConst.TOKOPEDIA_REWARD) { ctx, _, deeplink -> getRegisteredNavigationTokopoints(ctx, deeplink) },
            DLP.startWith(ApplinkConst.DEFAULT_RECOMMENDATION_PAGE) { _, _, deeplink -> getRegisteredNavigationRecommendation(deeplink) },
            DLP.startWith(ApplinkConst.HOME_EXPLORE) { _, _, deeplink -> getRegisteredExplore(deeplink) },
            DLP.startWith(ApplinkConst.CHAT_BOT) { _, _, deeplink -> getChatbotDeeplink(deeplink) },
            DLP.startWith(ApplinkConst.DISCOVERY_CATALOG) { _, _, deeplink -> getRegisteredNavigationCatalog(deeplink) },
            DLP.startWith(ApplinkConst.MONEYIN) { _, _, deeplink -> getRegisteredNavigationMoneyIn(deeplink) },
            DLP.startWith(ApplinkConst.OQR_PIN_URL_ENTRY_LINK) { _, _, deeplink -> getRegisteredNavigationForFintech(deeplink) },
            DLP.startWith(ApplinkConst.LAYANAN_FINANSIAL) { _, _, deeplink -> getRegisteredNavigationForLayanan(deeplink) },
            DLP.startWith(ApplinkConst.SALAM_UMRAH) { ctx, _, deeplink -> getRegisteredNavigationSalamUmrah(deeplink, ctx) },
            DLP.startWith(ApplinkConst.SALAM_UMRAH_ORDER_DETAIL) { ctx, _, deeplink -> getRegisteredNavigationSalamUmrahOrderDetail(deeplink, ctx) },
            DLP.startWith(ApplinkConst.BRAND_LIST) { _, _, deeplink -> getBrandlistInternal(deeplink) },
            DLP.startWith(ApplinkConst.OFFICIAL_STORE) { _, _, deeplink -> getRegisteredNavigationHomeOfficialStore(deeplink) },
            DLP.startWith(ApplinkConst.GOLD_MERCHANT_STATISTIC_DASHBOARD) { _, _, deeplink -> getRegisteredNavigationMarketplace(deeplink) },
            DLP.startWith(ApplinkConst.SHOP_SCORE_DETAIL) { _, _, deeplink -> getRegisteredNavigationMarketplace(deeplink) },
            DLP.startWith(ApplinkConst.Gamification.CRACK) { _, _, deeplink -> DeeplinkMapperGamification.getGamificationDeeplink(deeplink) },
            DLP.startWith(ApplinkConst.Gamification.TAP_TAP_MANTAP) { _, _, deeplink -> DeeplinkMapperGamification.getGamificationTapTapDeeplink(deeplink) },
            DLP.startWith(ApplinkConst.Gamification.DAILY_GIFT_BOX) { _, _, deeplink -> DeeplinkMapperGamification.getDailyGiftBoxDeeplink(deeplink) },
            DLP.startWith(ApplinkConst.Gamification.GIFT_TAP_TAP) { _, _, deeplink -> DeeplinkMapperGamification.getGiftBoxTapTapDeeplink(deeplink) },
            DLP.startWith(ApplinkConst.SELLER_ORDER_DETAIL) { _, _, deeplink -> getRegisteredNavigationOrder(deeplink) },
            DLP(logic = { _, _, deeplink -> isShopReview(deeplink) },
                    targetDeeplink = { _, _, deeplink -> getRegisteredNavigationShopReview(deeplink) }),
            DLP.startWith(ApplinkConst.TOPCHAT_IDLESS) { _, _, deeplink -> getRegisteredNavigationTopChat(deeplink) },
            DLP.startWith(ApplinkConst.TALK) { _, _, deeplink -> getRegisteredNavigationTalk(deeplink) },
            DLP.startWith(ApplinkConst.EVENTS) { ctx, _, deeplink -> getRegisteredNavigationEvents(deeplink, ctx) },
            DLP(logic = { _, _, deeplink -> isProductTalkDeeplink(deeplink) },
                    targetDeeplink = { _, _, deeplink -> getRegisteredNavigationProductTalk(deeplink) }),
            DLP(logic = { _, _, deeplink -> isShopTalkDeeplink(deeplink) },
                    targetDeeplink = { _, _, deeplink -> getRegisteredNavigationShopTalk(deeplink) }),
            DLP.startWith(ApplinkConst.SELLER_NEW_ORDER) { _, uri, _ -> getSomNewOrderAppLink(uri) },
            DLP.startWith(ApplinkConst.SELLER_PURCHASE_READY_TO_SHIP) { _, uri, _ -> getSomReadyToShipAppLink(uri) },
            DLP.startWith(ApplinkConst.SELLER_PURCHASE_DELIVERED) { _, _, deeplink -> getRegisteredNavigationMainAppSellerDelivered() },
            DLP.startWith(ApplinkConst.SELLER_PURCHASE_WAITING_PICKUP) { _, _, deeplink -> getRegisteredNavigationMainAppSellerWaitingPickup() },
            DLP.startWith(ApplinkConst.SELLER_PURCHASE_WAITING_AWB) { _, _, deeplink -> getRegisteredNavigationMainAppSellerWaitingAwb() },
            DLP.startWith(ApplinkConst.SELLER_PURCHASE_AWB_INVALID) { _, _, deeplink -> getRegisteredNavigationMainAppSellerAwbInvalid() },
            DLP.startWith(ApplinkConst.SELLER_PURCHASE_AWB_CHANGE) { _, _, deeplink -> getRegisteredNavigationMainAppSellerAwbChange() },
            DLP.startWith(ApplinkConst.SELLER_PURCHASE_RETUR) { _, _, deeplink -> getRegisteredNavigationMainAppSellerRetur() },
            DLP.startWith(ApplinkConst.SELLER_PURCHASE_COMPLAINT) { _, _, deeplink -> getRegisteredNavigationMainAppSellerComplaint() },
            DLP.startWith(ApplinkConst.SELLER_HISTORY) { _, uri, _ -> getSomAllOrderAppLink(uri) },
            DLP.startWith(ApplinkConst.SELLER_PURCHASE_SHIPPED) { _, uri, _ -> getSomShippedAppLink(uri) },
            DLP.startWith(ApplinkConst.SELLER_SHIPMENT) { _, uri, _ -> getSomReadyToShipAppLink(uri) },
            DLP.startWith(ApplinkConst.SELLER_PURCHASE_FINISHED) { _, uri, _ -> getSomDoneAppLink(uri) },
            DLP.startWith(ApplinkConst.SELLER_PURCHASE_CANCELED) { _, uri, _ -> getSomCancelledAppLink(uri) },
            DLP.startWith(ApplinkConst.SELLER_PURCHASE_CANCELLATION_REQUEST) { _, uri, _ -> getSomCancellationRequestAppLink(uri) },
            DLP.startWith(ApplinkConst.SELLER_STATUS) { _, uri, _ -> getSomShippedAppLink(uri) },
            DLP.startWithPattern(ApplinkConst.FEED_DETAILS) { _, _, deeplink -> getRegisteredFeed(deeplink) },
            DLP.startWithPattern(ApplinkConst.INTEREST_PICK) { _, _, deeplink -> DeeplinkMapperContent.getRegisteredNavigation(deeplink) },
            DLP(logic = { _, uri, _ -> uri.host == Uri.parse(ApplinkConst.FEED).host && uri.pathSegments.isEmpty() },
                    targetDeeplink = { _, _, _ -> getRegisteredNavigationHomeFeed() }),
            DLP.startWithPattern(ApplinkConst.CONTENT_EXPLORE) { _, _, deeplink -> getRegisteredNavigationHomeContentExplore(deeplink) },
            DLP(logic = { _, uri, _ -> DeeplinkMapperMerchant.isShopPageDeeplink(uri) },
                    targetDeeplink = { _, uri, _ -> DeeplinkMapperMerchant.getShopPageInternalApplink(uri) }),
            DLP(logic = { _, uri, _ -> DeeplinkMapperMerchant.isShopPageHomeDeeplink(uri) },
                    targetDeeplink = { _, uri, _ -> DeeplinkMapperMerchant.getShopPageHomeInternalApplink(uri) }),
            DLP(logic = { _, uri, deeplink -> DeeplinkMapperMerchant.isShopPageProductDeeplink(deeplink) },
                    targetDeeplink = { _, uri, deeplink -> DeeplinkMapperMerchant.getRegisteredNavigationShopProduct(deeplink) }),
            DLP(logic = { _, uri, deeplink -> DeeplinkMapperMerchant.isShopPageFeedDeeplink(deeplink) },
                    targetDeeplink = { _, uri, deeplink -> DeeplinkMapperMerchant.getRegisteredNavigationShopFeed(deeplink) }),
            DLP(logic = { _, uri, deeplink -> DeeplinkMapperMerchant.isShopFollowerListDeeplink(deeplink) },
                    targetDeeplink = { _, uri, deeplink -> DeeplinkMapperMerchant.getRegisteredNavigationShopFollowerList(deeplink) }),
            DLP(logic = { _, uri, deeplink -> DeeplinkMapperMerchant.isShopPageSettingCustomerApp(deeplink) },
                    targetDeeplink = { _, uri, deeplink -> DeeplinkMapperMerchant.getRegisteredNavigationShopPageSettingCustomerApp(deeplink) }),
            DLP(logic = { _, uri, _ -> DeeplinkMapperMerchant.isShopPageInfoDeeplink(uri) },
                    targetDeeplink = { _, uri, _ -> DeeplinkMapperMerchant.getShopPageInfoInternalApplink(uri) }),
            DLP(logic = { _, uri, _ -> DeeplinkMapperMerchant.isShopPageNoteDeeplink(uri) },
                    targetDeeplink = { _, uri, _ -> DeeplinkMapperMerchant.getShopPageNoteInternalApplink(uri) }),
            DLP(logic = { _, uri, _ -> DeeplinkMapperMerchant.isShopPageResultEtalaseDeepLink(uri) },
                    targetDeeplink = { _, uri, _ -> DeeplinkMapperMerchant.getShopPageResultEtalaseInternalAppLink(uri) }),
            DLP.startWith(ApplinkConst.SELLER_INFO_DETAIL) { _, uri, _ -> DeeplinkMapperMerchant.getSellerInfoDetailApplink(uri) },
            DLP.startWithPattern(ApplinkConst.ORDER_TRACKING) { _, _, deeplink -> DeeplinkMapperLogistic.getRegisteredNavigationOrder(deeplink) },
            DLP.startWith(ApplinkConst.ORDER_HISTORY) { _, uri, _ -> getRegisteredNavigationOrderHistory(uri) },
            DLP.startWith(ApplinkConst.RESET_PASSWORD, ApplinkConstInternalGlobal.FORGOT_PASSWORD),
            DLP.startWith(ApplinkConst.PRODUCT_ADD) { _, _, deeplink -> getRegisteredNavigationMarketplace(deeplink) },
            DLP.startWithPattern(ApplinkConst.KOL_COMMENT) { _, _, deeplink -> getKolDeepLink(deeplink) },
            DLP(logic = { _, uri, _ -> DeeplinkMapperContent.isPostDetailDeepLink(uri) },
                    targetDeeplink = { _, _, deeplink -> getKolDeepLink(deeplink) }),
            DLP.startWithPattern(ApplinkConst.KOL_YOUTUBE) { _, _, deeplink -> getKolDeepLink(deeplink) },
            DLP.startWith(ApplinkConst.CONTENT_CREATE_POST) { _, _, deeplink -> getContentCreatePostDeepLink(deeplink) },
            DLP.startWithPattern(ApplinkConst.CONTENT_DRAFT_POST) { _, _, deeplink -> getContentCreatePostDeepLink(deeplink) },
            DLP.startWith(ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST) { _, _, deeplink -> getContentCreatePostDeepLink(deeplink) },
            DLP.startWithPattern(ApplinkConst.AFFILIATE_DRAFT_POST) { _, _, deeplink -> getContentCreatePostDeepLink(deeplink) },
            DLP.startWith(ApplinkConst.DISCOVERY) { _, _, deeplink -> getDiscoveryDeeplink(deeplink) },
            DLP(logic = { _, uri, _ -> specialNavigationMapper(uri, ApplinkConst.HOST_CATEGORY_P) },
                    targetDeeplink = { _, _, deeplink -> getRegisteredCategoryNavigation(deeplink) }),
            DLP(logic = { _, uri, _ -> specialNavigationMapper(uri, ApplinkConst.Notification.BUYER_HOST) },
                    targetDeeplink = { _, uri, _ ->
                        UriUtil.buildUri(ApplinkConstInternalMarketplace.NOTIFICATION_BUYER_INFO_WITH_ID,
                                uri.pathSegments.first())
                    }),
            DLP.startWith(ApplinkConst.POWER_MERCHANT_PRO_INTERRUPT) { _, _, deeplink -> PowerMerchantDeepLinkMapper.getInternalAppLinkPmProInterrupt(deeplink) },
            DLP.exact(ApplinkConst.POWER_MERCHANT_SUBSCRIBE, ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE),
            DLP.exact(ApplinkConst.SHOP_PENALTY, ApplinkConstInternalMarketplace.SHOP_PENALTY),
            DLP.exact(ApplinkConst.SHOP_PENALTY_DETAIL, ApplinkConstInternalMarketplace.SHOP_PENALTY_DETAIL),
            DLP.exact(ApplinkConst.SELLER_SHIPPING_EDITOR, ApplinkConstInternalMarketplace.SHOP_SETTINGS_SHIPPING),
            DLP.exact(ApplinkConst.SELLER_COD_ACTIVATION, ApplinkConstInternalMarketplace.SHOP_SETTINGS_COD),
            DLP.exact(ApplinkConst.SELLER_WAREHOUSE_DATA, ApplinkConstInternalMarketplace.SHOP_SETTINGS_ADDRESS),
            DLP.exact(ApplinkConst.SETTING_PROFILE, ApplinkConstInternalGlobal.SETTING_PROFILE),
            DLP.exact(ApplinkConst.ADD_CREDIT_CARD, ApplinkConstInternalPayment.PAYMENT_ADD_CREDIT_CARD),
            DLP.exact(ApplinkConst.PMS, ApplinkConstInternalPayment.PMS_PAYMENT_LIST),
            DLP.exact(ApplinkConst.SETTING_NOTIFICATION, ApplinkConstInternalMarketplace.USER_NOTIFICATION_SETTING),
            DLP.exact(ApplinkConst.KYC, ApplinkConstInternalGlobal.USER_IDENTIFICATION_INFO),
            DLP.exact(ApplinkConst.REGISTER, ApplinkConstInternalGlobal.INIT_REGISTER),
            DLP.exact(ApplinkConst.ADD_NAME_PROFILE, ApplinkConstInternalGlobal.MANAGE_NAME),
            DLP.exact(ApplinkConst.KYC_NO_PARAM, ApplinkConstInternalGlobal.USER_IDENTIFICATION_INFO_BASE),
            DLP.exact(ApplinkConst.KYC_FORM_NO_PARAM, ApplinkConstInternalGlobal.USER_IDENTIFICATION_FORM_BASE),
            DLP.exact(ApplinkConst.SETTING_BANK, ApplinkConstInternalGlobal.SETTING_BANK),
            DLP.exact(ApplinkConst.OTP, ApplinkConstInternalGlobal.COTP),
            DLP.exact(ApplinkConst.OTP_PUSH_NOTIF_RECEIVER, ApplinkConstInternalGlobal.OTP_PUSH_NOTIF_RECEIVER),
            DLP.exact(ApplinkConst.ADD_PIN_ONBOARD, ApplinkConstInternalGlobal.ADD_PIN_ONBOARDING),
            DLP.exact(ApplinkConst.ADD_FINGERPRINT_ONBOARDING, ApplinkConstInternalGlobal.ADD_FINGERPRINT_ONBOARDING),
            DLP.exact(ApplinkConst.FLIGHT, ApplinkConstInternalTravel.DASHBOARD_FLIGHT),
            DLP.exact(ApplinkConst.SALDO, ApplinkConstInternalGlobal.SALDO_DEPOSIT),
            DLP.exact(ApplinkConst.SALDO_INTRO, ApplinkConstInternalGlobal.SALDO_INTRO),
            DLP.exact(ApplinkConst.DigitalInstantDebit.INSTANT_DEBIT_BCA_APPLINK, ApplinkConstInternalGlobal.INSTANT_DEBIT_BCA_ENTRY_PATTERN),
            DLP.exact(ApplinkConst.DigitalInstantDebit.INSTANT_DEBIT_BCA_EDITLIMIT_APPLINK, ApplinkConstInternalGlobal.EDIT_BCA_ONE_KLICK_ENTRY_PATTERN),
            DLP.exact(ApplinkConst.AFFILIATE_EDUCATION, ApplinkConstInternalContent.AFFILIATE_EDUCATION),
            DLP.exact(ApplinkConst.AFFILIATE_DASHBOARD, ApplinkConstInternalContent.AFFILIATE_DASHBOARD),
            DLP.exact(ApplinkConst.AFFILIATE_EXPLORE, ApplinkConstInternalContent.AFFILIATE_EXPLORE),
            DLP.exact(ApplinkConst.INBOX_TICKET, ApplinkConstInternalOperational.INTERNAL_INBOX_LIST),
            DLP.exact(ApplinkConst.Navigation.MAIN_NAV, ApplinkConsInternalNavigation.MAIN_NAVIGATION),
            DLP.exact(ApplinkConst.RECENT_VIEW, ApplinkConsInternalHome.HOME_RECENT_VIEW),
            DLP.exact(ApplinkConst.WISHLIST, ApplinkConsInternalHome.HOME_WISHLIST),
            DLP.exact(ApplinkConst.NEW_WISHLIST, ApplinkConsInternalHome.HOME_WISHLIST),
            DLP.exact(ApplinkConst.CREATE_SHOP, ApplinkConstInternalGlobal.LANDING_SHOP_CREATION),
            DLP.exact(ApplinkConst.CHAT_TEMPLATE, ApplinkConstInternalMarketplace.CHAT_SETTING_TEMPLATE),
            DLP.exact(ApplinkConst.PRODUCT_MANAGE) { _, _, deeplink -> DeepLinkMapperProductManage.getProductListInternalAppLink(deeplink) },
            DLP.exact(ApplinkConst.NOTIFICATION, ApplinkConstInternalMarketplace.NOTIFICATION_CENTER),
            DLP.exact(ApplinkConst.BUYER_INFO, ApplinkConstInternalMarketplace.NOTIFICATION_BUYER_INFO),
            DLP.exact(ApplinkConst.SHOP_SETTINGS_NOTE, ApplinkConstInternalMarketplace.SHOP_SETTINGS_NOTES),
            DLP.exact(ApplinkConst.SHOP_SETTINGS_INFO, ApplinkConstInternalMarketplace.SHOP_SETTINGS_INFO),
            DLP.exact(ApplinkConst.MY_SHOP_ETALASE_LIST, ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST),
            DLP.exact(ApplinkConst.CHANGE_PASSWORD, ApplinkConstInternalGlobal.CHANGE_PASSWORD),
            DLP.exact(ApplinkConst.HAS_PASSWORD, ApplinkConstInternalGlobal.HAS_PASSWORD),
            DLP.exact(ApplinkConst.THANK_YOU_PAGE_NATIVE, ApplinkConstInternalPayment.PAYMENT_THANK_YOU_PAGE),
            DLP.exact(ApplinkConst.HOWTOPAY, ApplinkConstInternalPayment.INTERNAL_HOW_TO_PAY),
            DLP.startWith(ApplinkConst.PAYLATER) { _, _, deeplink -> DeeplinkMapperFintech.getRegisteredNavigationForPayLater(deeplink) },
            DLP.exact(ApplinkConst.PROFILE_COMPLETION, ApplinkConstInternalGlobal.PROFILE_COMPLETION),
            DLP.exact(ApplinkConst.MERCHANT_VOUCHER_LIST, ApplinkConstInternalSellerapp.VOUCHER_LIST),
            DLP.exact(ApplinkConst.NOTIFICATION_TROUBLESHOOTER, ApplinkConstInternalGlobal.PUSH_NOTIFICATION_TROUBLESHOOTER),
            DLP.exact(ApplinkConst.PROFILE_COMPLETION, ApplinkConstInternalGlobal.PROFILE_COMPLETION),
            DLP.exact(ApplinkConst.FEEDBACK_FORM, ApplinkConstInternalGlobal.FEEDBACK_FORM),
            DLP(logic = { _, uri, _ -> uri.host == ApplinkConst.HOST_LOGIN
            }, targetDeeplink = { _, _, _ -> ApplinkConstInternalUserPlatform.LOGIN }),
            DLP.startWith(ApplinkConst.CHANGE_INACTIVE_PHONE, ApplinkConstInternalGlobal.CHANGE_INACTIVE_PHONE),
            DLP.startWith(ApplinkConst.OVO_REGISTER_INIT, ApplinkConstInternalGlobal.OVO_REG_INIT),
            DLP.startWith(ApplinkConst.REGISTER_INIT, ApplinkConstInternalGlobal.INIT_REGISTER),
            DLP.exact(ApplinkConst.OVO_FINAL_PAGE, ApplinkConstInternalGlobal.OVO_FINAL_PAGE),
            DLP.startWith(ApplinkConst.SELLER_CENTER) { _, _, _ -> DeeplinkMapperMerchant.getRegisteredSellerCenter() },
            DLP.startWith(ApplinkConst.SNAPSHOT_ORDER) { ctx, _, deeplink -> DeeplinkMapperOrder.getSnapshotOrderInternalAppLink(ctx, deeplink) }
    ).toMutableList()

    /**
     * Mapping tokopedia link to registered deplink in manifest if necessary
     * eg: tokopedia://product/add to tokopedia-android-internal://marketplace/product-add-item
     * If not found, return current deeplink, means it registered
     */
    private fun getRegisteredNavigationFromTokopedia(context: Context, uri: Uri, deeplink: String): String {
        var trimDeeplink: String? = null // deeplink without query and /, needed for exact matching
        synchronized(LOCK) {
            deeplinkPatternTokopediaSchemeList.forEachIndexed { index, it ->
                val isMatch: Boolean
                if (it.needTrimBeforeLogicRun) {
                    if (trimDeeplink == null) {
                        trimDeeplink = trimDeeplink(uri, deeplink)
                    }
                    isMatch = it.logic(context, uri, trimDeeplink!!)
                } else {
                    isMatch = it.logic(context, uri, deeplink)
                }
                if (isMatch) {
                    val target = it.targetDeeplink(context, uri, deeplink)
                    if (target.isNotEmpty()) {
                        putToTop(index)
                        return target
                    }
                }
            }
        }
        return ""
    }

    // mechanism to bring most frequent deeplink to top of the list
    private fun putToTop(index: Int) {
        // Uncomment this for performance for RouteManager. Currently disabled in production
        // Requirement: deeplinkPatternTokopediaSchemeList should be order-independent
        // deeplinkPatternTokopediaSchemeList.add(0, deeplinkPatternTokopediaSchemeList.removeAt(index))
    }

    private fun getRegisteredNavigationFromInternalTokopedia(context: Context, uri: Uri, deeplink: String): String {
        return when {
            deeplink.startsWith(ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST) -> DeepLinkMapperProductManage.getProductListInternalAppLink(deeplink)
            deeplink.startsWith(ApplinkConstInternalGlobal.TOPCHAT) && AppLinkMapperSellerHome.shouldRedirectToSellerApp(uri) -> AppLinkMapperSellerHome.getTopChatAppLink(uri)
            deeplink.startsWith(ApplinkConstInternalOrder.NEW_ORDER) -> getSomNewOrderAppLink(uri)
            deeplink.startsWith(ApplinkConstInternalOrder.READY_TO_SHIP) -> getSomReadyToShipAppLink(uri)
            deeplink.startsWith(ApplinkConstInternalOrder.SHIPPED) -> getSomShippedAppLink(uri)
            deeplink.startsWith(ApplinkConstInternalOrder.FINISHED) -> getSomDoneAppLink(uri)
            deeplink.startsWith(ApplinkConstInternalOrder.CANCELLED) -> getSomCancelledAppLink(uri)
            deeplink.startsWith(ApplinkConstInternalOrder.CANCELLATION_REQUEST) -> getSomCancellationRequestAppLink(uri)
            deeplink.startsWith(ApplinkConstInternalOrder.HISTORY) -> getSomAllOrderAppLink(uri)
            else -> return ""
        }
    }

    private fun trimDeeplink(uri: Uri, deeplink: String): String {
        val qIndex = deeplink.indexOf('?')
        val deeplinkWithoutQuery = if (uri.query?.isNotEmpty() == true && qIndex > 0) {
            deeplink.substring(0, qIndex)
        } else deeplink
        return if (deeplinkWithoutQuery.endsWith("/")) {
            deeplinkWithoutQuery.substringBeforeLast("/")
        } else {
            deeplinkWithoutQuery
        }
    }

    private fun specialNavigationMapper(uri: Uri, host: String): Boolean {
        return uri.host == host && uri.pathSegments.size > 0
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
    private fun getRegisteredNavigationFromSellerapp(uri: Uri, deeplink: String): String {
        return when (val trimDeeplink = trimDeeplink(uri, deeplink)) {
            ApplinkConst.SellerApp.TOPADS_DASHBOARD -> ApplinkConstInternalTopAds.TOPADS_DASHBOARD_INTERNAL
            ApplinkConst.SellerApp.TOPADS_CREDIT_HISTORY -> ApplinkConstInternalTopAds.TOPADS_HISTORY_CREDIT
            ApplinkConst.SellerApp.TOPADS_CREDIT -> ApplinkConstInternalTopAds.TOPADS_BUY_CREDIT
            ApplinkConst.SellerApp.TOPADS_CREATE_AUTO_ADS -> ApplinkConstInternalTopAds.TOPADS_AUTOADS_CREATE
            ApplinkConst.SellerApp.TOPADS_CREATE_ADS -> ApplinkConstInternalTopAds.TOPADS_CREATE_ADS
            ApplinkConst.SellerApp.TOPADS_CREATE_ONBOARDING -> ApplinkConstInternalTopAds.TOPADS_CREATION_ONBOARD
            ApplinkConst.SellerApp.TOPADS_HEADLINE_CREATE -> ApplinkConstInternalTopAds.TOPADS_HEADLINE_ADS_CREATION
            ApplinkConst.SellerApp.TOPADS_HEADLINE_DETAIL -> ApplinkConstInternalTopAds.TOPADS_HEADLINE_DETAIL
            ApplinkConst.SellerApp.TOPADS_EDIT_AUTO_ADS -> ApplinkConstInternalTopAds.TOPADS_EDIT_AUTOADS
            ApplinkConst.SellerApp.TOPADS_AUTO_TOPUP -> ApplinkConstInternalTopAds.TOPADS_AUTO_TOPUP
            ApplinkConst.SellerApp.TOPADS_CREATE_CHOOSER -> ApplinkConstInternalTopAds.TOPADS_CREATE_CHOOSER
            ApplinkConst.SellerApp.SELLER_APP_HOME -> ApplinkConstInternalSellerapp.SELLER_HOME
            ApplinkConst.SellerApp.PRODUCT_ADD -> ApplinkConstInternalMechant.MERCHANT_OPEN_PRODUCT_PREVIEW
            ApplinkConst.SellerApp.POWER_MERCHANT_SUBSCRIBE -> ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE
            ApplinkConst.SellerApp.CREATE_VOUCHER -> ApplinkConstInternalSellerapp.CREATE_VOUCHER
            ApplinkConst.SellerApp.VOUCHER_LIST -> ApplinkConstInternalSellerapp.VOUCHER_LIST
            ApplinkConst.SellerApp.VOUCHER_ACTIVE -> ApplinkConstInternalSellerapp.VOUCHER_ACTIVE
            ApplinkConst.SellerApp.VOUCHER_HISTORY -> ApplinkConstInternalSellerapp.VOUCHER_HISTORY
            ApplinkConst.SellerApp.VOUCHER_DETAIL -> ApplinkConstInternalSellerapp.VOUCHER_DETAIL
            ApplinkConst.SellerApp.CENTRALIZED_PROMO -> ApplinkConstInternalSellerapp.CENTRALIZED_PROMO
            ApplinkConst.SellerApp.PLAY_BROADCASTER -> ApplinkConstInternalContent.INTERNAL_PLAY_BROADCASTER
            ApplinkConst.SellerApp.CONTENT_CREATE_POST -> ApplinkConstInternalContent.INTERNAL_CONTENT_CREATE_POST
            ApplinkConst.SellerApp.SELLER_SHIPPING_EDITOR -> ApplinkConstInternalMarketplace.SHOP_SETTINGS_SHIPPING
            ApplinkConst.SellerApp.STATISTIC_DASHBOARD -> DeepLinkMapperStatistic.getStatisticAppLink(uri)
            else -> when {
                DeeplinkMapperMerchant.isShopPageFeedDeeplink(deeplink) -> DeeplinkMapperMerchant.getRegisteredNavigationShopFeed(deeplink)
                DeeplinkMapperMerchant.isShopPageSettingSellerApp(deeplink) -> DeeplinkMapperMerchant.getRegisteredNavigationShopPageSettingSellerApp(deeplink)
                else -> ""
            }
        }
    }
}

data class DLP(
        val logic: ((context: Context, uri: Uri, deeplink: String) -> Boolean),
        val targetDeeplink: (context: Context, uri: Uri, deeplink: String) -> String,
        val needTrimBeforeLogicRun: Boolean = false) {
    companion object {
        fun startWith(deeplinkCheck: String, targetDeeplink: String): DLP {
            return DLP({ _, _, deeplink -> deeplink.startsWith(deeplinkCheck, true) },
                    { _, _, _ -> targetDeeplink })
        }

        fun startWith(deeplinkCheck: String, targetDeeplink: (context: Context, uri: Uri, deeplink: String) -> String): DLP {
            return DLP({ _, _, deeplink -> deeplink.startsWith(deeplinkCheck, true) }, targetDeeplink)
        }

        fun startWithPattern(deeplinkCheck: String, targetDeeplink: (context: Context, uri: Uri, deeplink: String) -> String): DLP {
            return DLP({ _, _, deeplink -> deeplink.startsWithPattern(deeplinkCheck) }, targetDeeplink)
        }

        fun exact(deeplinkCheck: String, targetDeeplink: String): DLP {
            return DLP({ _, _, deeplink -> deeplink.equals(deeplinkCheck) }, { _, _, _ -> targetDeeplink }, true)
        }

        fun exact(deeplinkCheck: String, targetDeeplink: (context: Context, uri: Uri, deeplink: String) -> String): DLP {
            return DLP({ _, _, deeplink -> deeplink.equals(deeplinkCheck) }, targetDeeplink, true)
        }
    }

}
