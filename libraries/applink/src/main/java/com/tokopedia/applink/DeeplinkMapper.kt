package com.tokopedia.applink

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.Hotlist.DeeplinkMapperHotlist.getRegisteredHotlist
import com.tokopedia.applink.account.DeeplinkMapperAccount
import com.tokopedia.applink.category.DeeplinkMapperCategory.getRegisteredCategoryNavigation
import com.tokopedia.applink.category.DeeplinkMapperCategory.getRegisteredNavigationCatalog
import com.tokopedia.applink.category.DeeplinkMapperCategory.getRegisteredNavigationExploreCategory
import com.tokopedia.applink.category.DeeplinkMapperMoneyIn.getRegisteredNavigationMoneyIn
import com.tokopedia.applink.chatbot.DeeplinkMapperChatbot.getChatbotDeeplink
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
import com.tokopedia.applink.marketplace.DeeplinkMapperMarketplace.getShopPageInternalAppLink
import com.tokopedia.applink.marketplace.DeeplinkMapperMarketplace.getRegisteredNavigationMarketplace
import com.tokopedia.applink.marketplace.DeeplinkMapperMarketplace.getTokopediaInternalProduct
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant.getRegisteredNavigationProductReview
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant.getRegisteredNavigationReputation
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant.getRegisteredNavigationReviewReminder
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant.getRegisteredNavigationShopReview
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
import com.tokopedia.applink.penalty.DeepLinkMapperPenalty
import com.tokopedia.applink.powermerchant.PowerMerchantDeepLinkMapper
import com.tokopedia.applink.productmanage.DeepLinkMapperProductManage
import com.tokopedia.applink.promo.getRegisteredNavigationTokopoints
import com.tokopedia.applink.recommendation.getRegisteredNavigationRecommendation
import com.tokopedia.applink.salam.DeeplinkMapperSalam.getRegisteredNavigationSalamUmrah
import com.tokopedia.applink.salam.DeeplinkMapperSalam.getRegisteredNavigationSalamUmrahOrderDetail
import com.tokopedia.applink.search.DeeplinkMapperSearch.getRegisteredNavigationSearch
import com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome
import com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome.getSomAllOrderAppLink
import com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome.getSomCancellationRequestAppLink
import com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome.getSomCancelledAppLink
import com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome.getSomDoneAppLink
import com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome.getSomNewOrderAppLink
import com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome.getSomReadyToShipAppLink
import com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome.getSomShippedAppLink
import com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome.shouldRedirectToSellerApp
import com.tokopedia.applink.statistic.DeepLinkMapperStatistic
import com.tokopedia.applink.teleporter.Teleporter
import com.tokopedia.applink.travel.DeeplinkMapperTravel
import com.tokopedia.config.GlobalConfig

/**
 * Function to map the deeplink to applink (registered in manifest)
 *
 * Example when there are 2 deeplink that has the same pattern:
 * tokopedia://product/add and tokopedia://product/{id}
 * tokopedia://product/add will be mapped to tokopedia-android-internal:// to prevent conflict.
 */
object DeeplinkMapper {

    const val TOKOPOINTS = "tokopoints"
    val LOCK = Any()

    /**
     * Get registered deeplink navigation in manifest
     * In conventional term, convert deeplink (http or tokopedia) to applink (tokopedia:// or tokopedia-android-internal://)
     * If deeplink have query parameters then we need to keep the query and map the url without query
     */
    @JvmStatic
    fun getRegisteredNavigation(context: Context, deeplink: String): String {
        val uri = Uri.parse(deeplink)
        val teleporterResult = Teleporter.switchIfNeeded(context, uri)
        if (teleporterResult.isNotEmpty()) {
            return UriUtil.appendDiffDeeplinkWithQuery(teleporterResult, uri.query)
        }
        val mappedDeepLink: String = when (uri.scheme) {
            DeeplinkConstant.SCHEME_HTTP,
            DeeplinkConstant.SCHEME_HTTPS -> {
                getRegisteredNavigationFromHttp(context, uri, deeplink)
            }
            DeeplinkConstant.SCHEME_TOKOPEDIA -> {
                val query = uri.query
                val tempDeeplink = getRegisteredNavigationFromTokopedia(context, uri, deeplink)
                UriUtil.appendDiffDeeplinkWithQuery(tempDeeplink, query)
            }
            DeeplinkConstant.SCHEME_SELLERAPP -> {
                val query = uri.query
                val tempDeeplink = getRegisteredNavigationFromSellerapp(uri, deeplink)
                UriUtil.appendDiffDeeplinkWithQuery(tempDeeplink, query)
            }
            DeeplinkConstant.SCHEME_INTERNAL -> {
                getRegisteredNavigationFromInternalTokopedia(context, uri, deeplink)
            }
            else -> deeplink
        }
        return mappedDeepLink
    }

    private fun getRegisteredNavigationProductTalk(productId: String?): String {
        return "${ApplinkConstInternalGlobal.PRODUCT_TALK_BASE}$productId/"
    }

    private fun getRegisteredNavigationShopTalk(shopId: String?): String {
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

    private fun isChatBotTrue(uri: Uri): Boolean {
        return uri.getQueryParameter("is_chat_bot")?.equals("true") == true
    }

    private fun getRegisteredNavigationTopChat(uri: Uri, deeplink: String): String {
        val query = uri.query
        val path = uri.path
        var deepLinkInternal = ApplinkConstInternalGlobal.TOPCHAT
        if (query?.isNotEmpty() == true || path?.isNotEmpty() == true) {
            return when {
                isChatBotTrue(uri) -> {
                    getChatbotDeeplink(deeplink)
                }
                shouldRedirectToSellerApp(uri) -> {
                    ApplinkConstInternalSellerapp.SELLER_HOME_CHAT
                }
                else -> {
                    deepLinkInternal = "$deepLinkInternal$path"
                    deepLinkInternal
                }
            }
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
        val pathSize = uri.pathSegments.size
        if (pathSize == 1 && (uri.pathSegments[0] == TOKOPOINTS ||
                        uri.pathSegments[0] == ApplinkConst.RewardFallback.Reward.REWARDS)) {
            return ApplinkConstInternalPromo.TOKOPOINTS_HOME
        }
        val appLinkContent = DeeplinkMapperContent.getRegisteredNavigationContentFromHttp(uri, deeplink)
        if (appLinkContent.isNotBlank()) return appLinkContent

        val applinkDigital = DeeplinkMapperDigital.getRegisteredNavigationFromHttpDigital(context, deeplink)
        if (applinkDigital.isNotEmpty()) {
            return applinkDigital
        }
        return ""
    }

    fun isMainAppOrPro() = !GlobalConfig.isSellerApp()

    val deeplinkPatternTokopediaSchemeList: MutableList<DLP> = mutableListOf(
            DLP(StartsWith(ApplinkConst.HOME),
                    targetDeeplink = { _, _, deeplink, _ -> getRegisteredNavigationHome(deeplink) }),
            DLP.matchPattern(ApplinkConst.PRODUCT_INFO,
                    targetDeeplink = { _, uri, _, idList -> getTokopediaInternalProduct(uri, idList) }),
            DLP.matchPattern(ApplinkConst.AFFILIATE_PRODUCT,
                    targetDeeplink = { _, _, _, idList ->
                        UriUtil.buildUri(ApplinkConstInternalMarketplace.PRODUCT_DETAIL_WITH_AFFILIATE, idList?.getOrNull(0), "isAffiliate")
                    }),
            DLP.host(ApplinkConst.INBOX_HOST) { _, _, deeplink, _ -> DeeplinkMapperHome.getRegisteredInboxNavigation(deeplink) },
            DLP.startWith(ApplinkConst.QRSCAN, ApplinkConstInternalMarketplace.QR_SCANNEER),
            DLP.matchPattern(ApplinkConst.PRODUCT_REVIEW, targetDeeplink = { _, _, _, idList -> UriUtil.buildUri(ApplinkConstInternalMarketplace.PRODUCT_REVIEW, idList?.getOrNull(0)) }),
            DLP.host(ApplinkConst.ACCOUNT_HOST) { ctx, _, deeplink, _ -> DeeplinkMapperAccount.getAccountInternalApplink(ctx, deeplink) },
            DLP(DLPLogic { _, _, deeplink -> DeeplinkMapperUohOrder.isNavigationUohOrder(deeplink) },
                    targetDeeplink = { ctx, _, deeplink, _ -> DeeplinkMapperUohOrder.getRegisteredNavigationUohOrder(ctx, deeplink) }),
            DLP.exact(ApplinkConst.TRAVEL_SUBHOMEPAGE_HOME) { ctx, _, deeplink, _ -> getRegisteredNavigationDigital(ctx, deeplink) },
            DLP.host(ApplinkConst.HOTEL_HOST) { ctx, _, deeplink, _ -> DeeplinkMapperTravel.getRegisteredNavigationTravel(ctx, deeplink) },
            DLP.startWith(ApplinkConst.DIGITAL) { ctx, _, deeplink, _ -> getRegisteredNavigationDigital(ctx, deeplink) },
            DLP.startWith(ApplinkConst.RECHARGE) { ctx, _, deeplink, _ -> getRegisteredNavigationDigital(ctx, deeplink) },
            DLP.startWith(ApplinkConst.DISCOVERY_SEARCH) { _, _, deeplink, _ -> getRegisteredNavigationSearch(deeplink) },
            DLP.startWith(ApplinkConst.CART) { ctx, _, deeplink, _ -> getRegisteredNavigationMarketplace(ctx, deeplink) },
            DLP.startWith(ApplinkConst.CHECKOUT) { ctx, _, deeplink, _ -> getRegisteredNavigationMarketplace(ctx, deeplink) },
            DLP.startWith(ApplinkConst.OCC) { ctx, _, deeplink, _ -> getRegisteredNavigationMarketplace(ctx, deeplink) },
            DLP.startWith(ApplinkConst.DEALS_HOME) { ctx, _, deeplink, _ -> getRegisteredNavigationDeals(ctx, deeplink) },
            DLP.startWith(ApplinkConst.FIND) { _, _, deeplink, _ -> getRegisteredFind(deeplink) },
            DLP.startWith(ApplinkConst.AMP_FIND) { _, _, deeplink, _ -> getRegisteredFind(deeplink) },
            DLP.host(ApplinkConst.Digital.CATEGORY_EXPLORE_HOST) { _, _, deeplink, _ -> getRegisteredNavigationExploreCategory(deeplink) },
            DLP.host(ApplinkConst.CATEGORY_HOST) { _, uri, deeplink, _ -> getRegisteredCategoryNavigation(deeplink, uri) },
            DLP.matchPattern(ApplinkConst.PROFILE) { _, _, deeplink, _ -> DeeplinkMapperContent.getRegisteredNavigation(deeplink) },
            DLP.matchPattern(ApplinkConst.PLAY_DETAIL) { _, _, deeplink, _ -> DeeplinkMapperContent.getRegisteredNavigation(deeplink) },
            DLP.startWith(ApplinkConst.PLAY_BROADCASTER) { _, _, deeplink, _ -> DeeplinkMapperContent.getRegisteredNavigation(deeplink) },
            DLP.host(ApplinkConst.HOME_HOT_HOST) { _, _, deeplink, _ -> getRegisteredHotlist(deeplink) },
            DLP.matchPattern(ApplinkConst.PRODUCT_EDIT) { _, _, _, idList ->
                DeepLinkMapperProductManage.getEditProductInternalAppLink(idList?.getOrNull(0)
                        ?: "")
            },
            DLP.matchPattern(ApplinkConst.SHOP_ETALASE_LIST) { _, _, _, idList ->
                DeepLinkMapperEtalase.getEtalaseListInternalAppLink(idList?.getOrNull(0) ?: "")
            },
            DLP.startWith(ApplinkConst.PRODUCT_MANAGE) { _, _, deeplink, _ -> DeepLinkMapperProductManage.getProductListInternalAppLink(deeplink) },
            DLP.startWith(ApplinkConst.PRODUCT_CREATE_REVIEW) { _, uri, _, _ -> getRegisteredNavigationProductReview(uri) },
            DLP.host(ApplinkConst.REVIEW_REMINDER_HOST) { _, _, deeplink, _ -> getRegisteredNavigationReviewReminder(deeplink) },
            DLP.host(ApplinkConst.REVIEW_HOST) { _, _, deeplink, _ -> getRegisteredNavigationReputation(deeplink) },
            DLP.startWith(ApplinkConst.SELLER_REVIEW) { _, _, deeplink, _ -> ApplinkConstInternalMarketplace.SELLER_REVIEW_DETAIL },
            DLP.startWith(ApplinkConst.TOKOPOINTS) { ctx, _, deeplink, _ -> getRegisteredNavigationTokopoints(ctx, deeplink) },
            DLP.startWith(ApplinkConst.TOKOPEDIA_REWARD) { ctx, _, deeplink, _ -> getRegisteredNavigationTokopoints(ctx, deeplink) },
            DLP.startWith(ApplinkConst.DEFAULT_RECOMMENDATION_PAGE) { _, _, deeplink, _ -> getRegisteredNavigationRecommendation(deeplink) },
            DLP.startWith(ApplinkConst.HOME_EXPLORE) { _, _, deeplink, _ -> getRegisteredExplore(deeplink) },
            DLP.host(ApplinkConst.CHATBOT_HOST) { _, _, deeplink, _ -> getChatbotDeeplink(deeplink) },
            DLP.startWith(ApplinkConst.DISCOVERY_CATALOG) { _, _, deeplink, _ -> getRegisteredNavigationCatalog(deeplink) },
            DLP.startWith(ApplinkConst.MONEYIN) { _, _, deeplink, _ -> getRegisteredNavigationMoneyIn(deeplink) },
            DLP.startWith(ApplinkConst.OQR_PIN_URL_ENTRY_LINK) { _, uri, _, _ -> getRegisteredNavigationForFintech(uri) },
            DLP.startWith(ApplinkConst.LAYANAN_FINANSIAL) { _, _, deeplink, _ -> getRegisteredNavigationForLayanan(deeplink) },
            DLP.startWith(ApplinkConst.SALAM_UMRAH) { ctx, _, deeplink, _ -> getRegisteredNavigationSalamUmrah(deeplink, ctx) },
            DLP.startWith(ApplinkConst.SALAM_UMRAH_ORDER_DETAIL) { ctx, _, deeplink, _ -> getRegisteredNavigationSalamUmrahOrderDetail(deeplink, ctx) },
            DLP.startWith(ApplinkConst.OFFICIAL_STORE) { _, _, deeplink, _ -> getRegisteredNavigationHomeOfficialStore(deeplink) },
            DLP.startWith(ApplinkConst.GOLD_MERCHANT_STATISTIC_DASHBOARD) { ctx, _, deeplink, _ -> getRegisteredNavigationMarketplace(ctx, deeplink) },
            DLP.startWith(ApplinkConst.SHOP_SCORE_DETAIL) { ctx, _, deeplink, _ -> getRegisteredNavigationMarketplace(ctx, deeplink) },
            DLP.startWith(ApplinkConst.SHOP_PENALTY) { ctx, _, deeplink, _ -> DeepLinkMapperPenalty.getPenaltyApplink(ctx) },
            DLP.exact(ApplinkConst.SHOP_PENALTY_DETAIL, ApplinkConstInternalMarketplace.SHOP_PENALTY_DETAIL),
            DLP.startWith(ApplinkConst.POWER_MERCHANT_PRO_INTERRUPT) { _, _, deeplink, _ -> PowerMerchantDeepLinkMapper.getInternalAppLinkPmProInterrupt(deeplink) },
            DLP.startWith(ApplinkConst.Gamification.CRACK) { _, _, deeplink, _ -> DeeplinkMapperGamification.getGamificationDeeplink(deeplink) },
            DLP.startWith(ApplinkConst.SELLER_ORDER_DETAIL) { _, _, deeplink, _ -> getRegisteredNavigationOrder(deeplink) },
            DLP.matchPattern(ApplinkConst.SHOP_REVIEW,
                    targetDeeplink = { _, _, _, idList -> getRegisteredNavigationShopReview(idList?.getOrNull(0)) }),
            DLP(Host(ApplinkConst.TOPCHAT_HOST) or Host(ApplinkConst.TOPCHAT_OLD_HOST)) { _, uri, deeplink, _ -> getRegisteredNavigationTopChat(uri, deeplink) },
            DLP.startWith(ApplinkConst.TALK) { _, _, deeplink, _ -> getRegisteredNavigationTalk(deeplink) },
            DLP.startWith(ApplinkConst.EVENTS) { ctx, _, deeplink, _ -> getRegisteredNavigationEvents(deeplink, ctx) },
            DLP.matchPattern(ApplinkConst.PRODUCT_TALK,
                    targetDeeplink = { _, _, _, idList -> getRegisteredNavigationProductTalk(idList?.getOrNull(0)) }),
            DLP.matchPattern(ApplinkConst.SHOP_TALK,
                    targetDeeplink = { _, _, _, idList -> getRegisteredNavigationShopTalk(idList?.getOrNull(0)) }),
            DLP.startWith(ApplinkConst.SELLER_NEW_ORDER) { _, uri, _, _ -> getSomNewOrderAppLink(uri) },
            DLP.startWith(ApplinkConst.SELLER_PURCHASE_READY_TO_SHIP) { _, uri, _, _ -> getSomReadyToShipAppLink(uri) },
            DLP.startWith(ApplinkConst.SELLER_PURCHASE_DELIVERED) { _, _, deeplink, _ -> getRegisteredNavigationMainAppSellerDelivered() },
            DLP.startWith(ApplinkConst.SELLER_PURCHASE_WAITING_PICKUP) { _, _, deeplink, _ -> getRegisteredNavigationMainAppSellerWaitingPickup() },
            DLP.startWith(ApplinkConst.SELLER_PURCHASE_WAITING_AWB) { _, _, deeplink, _ -> getRegisteredNavigationMainAppSellerWaitingAwb() },
            DLP.startWith(ApplinkConst.SELLER_PURCHASE_AWB_INVALID) { _, _, deeplink, _ -> getRegisteredNavigationMainAppSellerAwbInvalid() },
            DLP.startWith(ApplinkConst.SELLER_PURCHASE_AWB_CHANGE) { _, _, deeplink, _ -> getRegisteredNavigationMainAppSellerAwbChange() },
            DLP.startWith(ApplinkConst.SELLER_PURCHASE_RETUR) { _, _, deeplink, _ -> getRegisteredNavigationMainAppSellerRetur() },
            DLP.startWith(ApplinkConst.SELLER_PURCHASE_COMPLAINT) { _, _, deeplink, _ -> getRegisteredNavigationMainAppSellerComplaint() },
            DLP.startWith(ApplinkConst.SELLER_HISTORY) { _, uri, _, _ -> getSomAllOrderAppLink(uri) },
            DLP.startWith(ApplinkConst.SELLER_PURCHASE_SHIPPED) { _, uri, _, _ -> getSomShippedAppLink(uri) },
            DLP.startWith(ApplinkConst.SELLER_SHIPMENT) { _, uri, _, _ -> getSomReadyToShipAppLink(uri) },
            DLP.startWith(ApplinkConst.SELLER_PURCHASE_FINISHED) { _, uri, _, _ -> getSomDoneAppLink(uri) },
            DLP.startWith(ApplinkConst.SELLER_PURCHASE_CANCELED) { _, uri, _, _ -> getSomCancelledAppLink(uri) },
            DLP.startWith(ApplinkConst.SELLER_PURCHASE_CANCELLATION_REQUEST) { _, uri, _, _ -> getSomCancellationRequestAppLink(uri) },
            DLP.startWith(ApplinkConst.SELLER_STATUS) { _, uri, _, _ -> getSomShippedAppLink(uri) },
            DLP.startWith(ApplinkConst.FEED_DETAILS) { _, _, deeplink, _ -> getRegisteredFeed(deeplink) },
            DLP.startWith(ApplinkConst.INTEREST_PICK) { _, _, deeplink, _ -> DeeplinkMapperContent.getRegisteredNavigation(deeplink) },
            DLP.exact(ApplinkConst.FEED,
                    targetDeeplink = { _, _, _, _ -> getRegisteredNavigationHomeFeed() }),
            DLP.matchPattern(ApplinkConst.CONTENT_EXPLORE) { _, _, deeplink, _ -> getRegisteredNavigationHomeContentExplore(deeplink) },
            DLP(MatchPattern(ApplinkConst.SHOP),
                    targetDeeplink = { ctx, uri, deeplink, idList -> getShopPageInternalAppLink(ctx, uri, deeplink, UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_PAGE, idList?.getOrNull(0)), idList?.getOrNull(0).orEmpty()) }),
            DLP.matchPattern(ApplinkConst.SHOP_HOME,
                    targetDeeplink = { ctx, uri, deeplink, idList -> getShopPageInternalAppLink(ctx, uri, deeplink, UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_PAGE_HOME, idList?.getOrNull(0)), idList?.getOrNull(0).orEmpty()) }),
            DLP.matchPattern(ApplinkConst.SHOP_PRODUCT,
                    targetDeeplink = { ctx, uri, deeplink, idList -> getShopPageInternalAppLink(ctx, uri, deeplink, UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_PAGE_PRODUCT, idList?.getOrNull(0)), idList?.getOrNull(0).orEmpty()) }),
            DLP.matchPattern(ApplinkConst.SHOP_FEED,
                    targetDeeplink = { ctx, uri, deeplink, idList -> getShopPageInternalAppLink(ctx, uri, deeplink,UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_PAGE_FEED, idList?.getOrNull(0)), idList?.getOrNull(0).orEmpty()) }),
            DLP.matchPattern(ApplinkConst.SHOP_FOLLOWER_LIST,
                    targetDeeplink = { ctx, uri, deeplink, idList -> getShopPageInternalAppLink(ctx, uri, deeplink,UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_FAVOURITE_LIST_WITH_SHOP_ID, idList?.getOrNull(0)), idList?.getOrNull(0).orEmpty()) }),
            DLP.matchPattern(ApplinkConst.SHOP_SETTINGS_CUSTOMER_APP,
                    targetDeeplink = { ctx, uri, deeplink, idList -> getShopPageInternalAppLink(ctx, uri, deeplink,UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_PAGE_SETTING_CUSTOMER_APP_WITH_SHOP_ID, idList?.getOrNull(0)), idList?.getOrNull(0).orEmpty()) }),
            DLP.matchPattern(ApplinkConst.SHOP_INFO,
                    targetDeeplink = { ctx, uri, deeplink, idList -> getShopPageInternalAppLink(ctx, uri, deeplink,UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_PAGE_INFO, idList?.getOrNull(0)), idList?.getOrNull(0).orEmpty()) }),
            DLP.matchPattern(ApplinkConst.SHOP_NOTE,
                    targetDeeplink = { ctx, uri, deeplink, idList -> getShopPageInternalAppLink(ctx, uri, deeplink,UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_PAGE_NOTE, idList?.getOrNull(0)), idList?.getOrNull(0).orEmpty()) }),
            DLP.matchPattern(ApplinkConst.SHOP_ETALASE,
                    targetDeeplink = { ctx, uri, deeplink, idList -> getShopPageInternalAppLink(ctx, uri, deeplink,UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_PAGE_PRODUCT_LIST, idList?.getOrNull(0), idList?.getOrNull(1)), idList?.getOrNull(0).orEmpty()) }),
            DLP.startWith(ApplinkConst.SELLER_INFO_DETAIL) { _, uri, _, _ -> DeeplinkMapperMerchant.getSellerInfoDetailApplink(uri) },
            DLP.matchPattern(ApplinkConst.ORDER_TRACKING) { _, _, deeplink, _ -> DeeplinkMapperLogistic.getRegisteredNavigationOrder(deeplink) },
            DLP.matchPattern(ApplinkConst.ORDER_HISTORY_SHOP) { _, _, _, idList -> UriUtil.buildUri(ApplinkConstInternalMarketplace.ORDER_HISTORY, idList?.getOrNull(0)) },
            DLP.startWith(ApplinkConst.RESET_PASSWORD, ApplinkConstInternalGlobal.FORGOT_PASSWORD),
            DLP.matchPattern(ApplinkConst.KOL_COMMENT) { _, _, deeplink, _ -> getKolDeepLink(deeplink) },
            DLP.matchPattern(ApplinkConst.CONTENT_DETAIL,
                    targetDeeplink = { _, _, deeplink, _ -> getKolDeepLink(deeplink) }),
            DLP.host(ApplinkConst.KOL_YOUTUBE_HOST_BASE) { _, _, deeplink, _ -> getKolDeepLink(deeplink) },
            DLP.matchPattern(ApplinkConst.CONTENT_DRAFT_POST) { _, _, deeplink, _ -> getContentCreatePostDeepLink(deeplink) },
            DLP.startWith(ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST) { _, _, deeplink, _ -> getContentCreatePostDeepLink(deeplink) },
            DLP.matchPattern(ApplinkConst.AFFILIATE_DRAFT_POST) { _, _, deeplink, _ -> getContentCreatePostDeepLink(deeplink) },
            DLP.startWith(ApplinkConst.DISCOVERY) { _, _, deeplink, _ -> getDiscoveryDeeplink(deeplink) },
            DLP(DLPLogic { _, uri, _ -> matchWithHostAndHasPath(uri, ApplinkConst.HOST_CATEGORY_P) },
                    targetDeeplink = { _, uri, _, _ -> getRegisteredCategoryNavigation(uri) }),
            DLP(DLPLogic { _, uri, _ -> matchWithHostAndHasPath(uri, ApplinkConst.Notification.BUYER_HOST) },
                    targetDeeplink = { _, uri, _, _ ->
                        UriUtil.buildUri(ApplinkConstInternalMarketplace.NOTIFICATION_BUYER_INFO_WITH_ID,
                                uri.pathSegments.first())
                    }),
            DLP.exact(ApplinkConst.POWER_MERCHANT_SUBSCRIBE, ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE),
            DLP.exact(ApplinkConst.SELLER_SHIPPING_EDITOR, ApplinkConstInternalMarketplace.SHOP_SETTINGS_SHIPPING),
            DLP.exact(ApplinkConst.SELLER_COD_ACTIVATION, ApplinkConstInternalMarketplace.SHOP_SETTINGS_COD),
            DLP.exact(ApplinkConst.SELLER_WAREHOUSE_DATA, ApplinkConstInternalMarketplace.SHOP_SETTINGS_ADDRESS),
            DLP.exact(ApplinkConst.SETTING_PROFILE, ApplinkConstInternalGlobal.SETTING_PROFILE),
            DLP.exact(ApplinkConst.ADD_CREDIT_CARD, ApplinkConstInternalPayment.PAYMENT_ADD_CREDIT_CARD),
            DLP.exact(ApplinkConst.PMS, ApplinkConstInternalPayment.PMS_PAYMENT_LIST),
            DLP.exact(ApplinkConst.SETTING_NOTIFICATION, ApplinkConstInternalMarketplace.USER_NOTIFICATION_SETTING),
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
            DLP.exact(ApplinkConst.NOTIFICATION, ApplinkConstInternalMarketplace.NOTIFICATION_CENTER),
            DLP.exact(ApplinkConst.BUYER_INFO, ApplinkConstInternalMarketplace.NOTIFICATION_BUYER_INFO),
            DLP.exact(ApplinkConst.SHOP_SETTINGS_NOTE, ApplinkConstInternalMarketplace.SHOP_SETTINGS_NOTES),
            DLP.exact(ApplinkConst.SHOP_SETTINGS_INFO, ApplinkConstInternalMarketplace.SHOP_SETTINGS_INFO),
            DLP.exact(ApplinkConst.MY_SHOP_ETALASE_LIST, ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST),
            DLP.exact(ApplinkConst.CHANGE_PASSWORD, ApplinkConstInternalGlobal.CHANGE_PASSWORD),
            DLP.exact(ApplinkConst.HAS_PASSWORD, ApplinkConstInternalGlobal.HAS_PASSWORD),
            DLP.exact(ApplinkConst.THANK_YOU_PAGE_NATIVE, ApplinkConstInternalPayment.PAYMENT_THANK_YOU_PAGE),
            DLP.exact(ApplinkConst.HOWTOPAY, ApplinkConstInternalPayment.INTERNAL_HOW_TO_PAY),
            DLP.startWith(ApplinkConst.PAYLATER, ApplinkConstInternalFintech.PAYLATER),
            DLP.exact(ApplinkConst.MERCHANT_VOUCHER_LIST, ApplinkConstInternalSellerapp.VOUCHER_LIST),
            DLP.exact(ApplinkConst.NOTIFICATION_TROUBLESHOOTER, ApplinkConstInternalGlobal.PUSH_NOTIFICATION_TROUBLESHOOTER),
            DLP.exact(ApplinkConst.PROFILE_COMPLETION, ApplinkConstInternalGlobal.PROFILE_COMPLETION),
            DLP.exact(ApplinkConst.FEEDBACK_FORM, ApplinkConstInternalGlobal.FEEDBACK_FORM),
            DLP.host(ApplinkConst.HOST_LOGIN, targetDeeplink = { _, _, _, _ -> ApplinkConstInternalUserPlatform.LOGIN }),
            DLP.startWith(ApplinkConst.CHANGE_INACTIVE_PHONE, ApplinkConstInternalGlobal.CHANGE_INACTIVE_PHONE),
            DLP.startWith(ApplinkConst.OVO_REGISTER_INIT, ApplinkConstInternalGlobal.OVO_REG_INIT),
            DLP.startWith(ApplinkConst.REGISTER_INIT, ApplinkConstInternalGlobal.INIT_REGISTER),
            DLP.exact(ApplinkConst.OVO_FINAL_PAGE, ApplinkConstInternalGlobal.OVO_FINAL_PAGE),
            DLP.startWith(ApplinkConst.SELLER_CENTER) { _, _, _, _ -> DeeplinkMapperMerchant.getRegisteredSellerCenter() },
            DLP.startWith(ApplinkConst.SNAPSHOT_ORDER) { ctx, _, deeplink, _ -> DeeplinkMapperOrder.getSnapshotOrderInternalAppLink(ctx, deeplink) },
            DLP.startWith(ApplinkConst.ORDER_BUYER_CANCELLATION_REQUEST_PAGE) { _, _, _, _ -> DeeplinkMapperOrder.getBuyerCancellationRequestInternalAppLink() },
            DLP.exact(ApplinkConst.TokoNow.HOME, ApplinkConstInternalTokoMart.HOME)
    )

    fun getTokopediaSchemeList():List<DLP>{
        return deeplinkPatternTokopediaSchemeList
    }

    /**
     * Mapping tokopedia link to registered deplink in manifest if necessary
     * eg: tokopedia://product/add to tokopedia-android-internal://marketplace/product-add-item
     * If not found, return current deeplink, means it registered
     */
    private fun getRegisteredNavigationFromTokopedia(context: Context, uri: Uri, deeplink: String): String {
        var trimDeeplink: String? = null // deeplink without query and /, needed for exact matching
        synchronized(LOCK) {
            getTokopediaSchemeList().forEachIndexed { index, it ->
                val isMatch: Boolean
                val idList: List<String>?
                val deeplinkToCheck: String?
                if (it.logic.needTrimBeforeLogicRun) {
                    if (trimDeeplink == null) {
                        trimDeeplink = UriUtil.trimDeeplink(uri, deeplink)
                    }
                    deeplinkToCheck = trimDeeplink
                } else {
                    deeplinkToCheck = deeplink
                }
                val result = it.logic.logic(context, uri, deeplinkToCheck ?: deeplink)
                isMatch = result.first
                idList = result.second
                if (isMatch) {
                    val target = it.targetDeeplink(context, uri, deeplink, idList)
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
            deeplink.startsWith(ApplinkConstInternalGlobal.TOPCHAT) && shouldRedirectToSellerApp(uri) -> AppLinkMapperSellerHome.getTopChatAppLink(uri)
            deeplink.startsWith(ApplinkConstInternalOrder.NEW_ORDER) -> getSomNewOrderAppLink(uri)
            deeplink.startsWith(ApplinkConstInternalOrder.READY_TO_SHIP) -> getSomReadyToShipAppLink(uri)
            deeplink.startsWith(ApplinkConstInternalOrder.SHIPPED) -> getSomShippedAppLink(uri)
            deeplink.startsWith(ApplinkConstInternalOrder.FINISHED) -> getSomDoneAppLink(uri)
            deeplink.startsWith(ApplinkConstInternalOrder.CANCELLED) -> getSomCancelledAppLink(uri)
            deeplink.startsWith(ApplinkConstInternalOrder.CANCELLATION_REQUEST) -> getSomCancellationRequestAppLink(uri)
            deeplink.startsWith(ApplinkConstInternalOrder.HISTORY) -> getSomAllOrderAppLink(uri)
            deeplink.startsWith(ApplinkConstInternalMarketplace.SHOP_PAGE_BASE) -> getShopPageInternalAppLink(context,uri,deeplink, "", uri.pathSegments.getOrNull(1).orEmpty())
            else -> return ""
        }
    }

    private fun matchWithHostAndHasPath(uri: Uri, host: String): Boolean {
        return uri.host == host && uri.pathSegments.size > 0
    }

    /**
     * Mapping sellerapp link to registered deplink in manifest if necessary
     * eg: sellerapp://product/add to tokopedia-android-internal://marketplace/product-add-item
     * If not found, return current deeplink, means it registered
     */
    private fun getRegisteredNavigationFromSellerapp(uri: Uri, deeplink: String): String {
        return when (UriUtil.trimDeeplink(uri, deeplink)) {
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

open class DLPLogic(val logic: ((context: Context, uri: Uri, deeplink: String) -> Pair<Boolean, List<String>?>),
                    val needTrimBeforeLogicRun: Boolean = false) {
    constructor(mapLogic: (context: Context, uri: Uri, deeplink: String) -> Boolean) : this(
            logic = { context, uri, deeplink -> mapLogic(context, uri, deeplink) to null }, false
    )

    operator fun plus(additionalLogic: () -> Boolean): DLPLogic {
        return DLPLogic(logic = { context, uri, deeplink ->
            val resultFirstLogic = logic.invoke(context, uri, deeplink)
            val isMatchResult = resultFirstLogic.first
            val idListResult = resultFirstLogic.second
            ((isMatchResult && additionalLogic.invoke()) to idListResult)
        }, needTrimBeforeLogicRun)
    }

    operator fun plus(additionalLogic: (context: Context, uri: Uri, deeplink: String) -> Boolean): DLPLogic {
        return DLPLogic(logic = { context, uri, deeplink ->
            val resultFirstLogic = logic.invoke(context, uri, deeplink)
            val isMatchResult = resultFirstLogic.first
            val idListResult = resultFirstLogic.second
            val resultSecondLogic = additionalLogic.invoke(context, uri, deeplink)
            ((isMatchResult && resultSecondLogic) to idListResult)
        }, needTrimBeforeLogicRun)
    }
}

infix fun DLPLogic.or(other: DLPLogic): DLPLogic {
    return DLPLogic(logic = { context, uri, deeplink ->
        val resultFirstLogic = this.logic.invoke(context, uri, deeplink)
        val isMatchResult = resultFirstLogic.first
        val resultSecondLogic = other.logic.invoke(context, uri, deeplink)
        val isMatchResultOther = resultSecondLogic.first
        val idListResultOther = resultSecondLogic.second
        val idList: List<String>? = resultFirstLogic.second ?: idListResultOther
        ((isMatchResult || isMatchResultOther) to idList)
    }, needTrimBeforeLogicRun)
}

class Exact(target: String) : DLPLogic(logic = { _, _, deeplink -> ((deeplink == target) to null) }, true)
class StartsWith(target: String) : DLPLogic(logic = { _, _, deeplink ->
    (deeplink.startsWith(target) to null)
})

class MatchPattern(target: String) : DLPLogic(logic = { _, uri, _ ->
    val list = UriUtil.matchWithPattern(target, uri, false)
    ((list != null) to list)
})

class Host(target: String) : DLPLogic(logic = { _, uri, _ ->
    ((uri.host == target) to null)
})

class DLP(
        val logic: DLPLogic,
        val targetDeeplink: (context: Context, uri: Uri, deeplink: String, idList: List<String>?) -> String) {
    companion object {
        fun startWith(deeplinkCheck: String, targetDeeplink: String): DLP {
            return DLP(StartsWith(deeplinkCheck)) { _, _, _, _ -> targetDeeplink }
        }

        fun startWith(deeplinkCheck: String, targetDeeplink: (context: Context, uri: Uri, deeplink: String, idList: List<String>?) -> String): DLP {
            return DLP(StartsWith(deeplinkCheck), targetDeeplink)
        }

        fun matchPattern(deeplinkCheck: String, targetDeeplink: (context: Context, uri: Uri, deeplink: String, idList: List<String>?) -> String): DLP {
            return DLP(MatchPattern(deeplinkCheck), targetDeeplink)
        }

        fun exact(deeplinkCheck: String, targetDeeplink: String): DLP {
            return DLP(Exact(deeplinkCheck)) { _, _, _, _ -> targetDeeplink }
        }

        fun host(host: String, targetDeeplink: (context: Context, uri: Uri, deeplink: String, idList: List<String>?) -> String): DLP {
            return DLP(Host(host), targetDeeplink)
        }

        fun exact(deeplinkCheck: String, targetDeeplink: (context: Context, uri: Uri, deeplink: String, idList: List<String>?) -> String): DLP {
            return DLP(Exact(deeplinkCheck), targetDeeplink)
        }
    }
}