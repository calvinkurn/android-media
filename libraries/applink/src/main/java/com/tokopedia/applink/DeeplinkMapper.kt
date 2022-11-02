package com.tokopedia.applink

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.Hotlist.DeeplinkMapperHotlist.getRegisteredHotlist
import com.tokopedia.applink.account.DeeplinkMapperAccount
import com.tokopedia.applink.category.DeeplinkMapperCategory
import com.tokopedia.applink.category.DeeplinkMapperCategory.getRegisteredCategoryNavigation
import com.tokopedia.applink.category.DeeplinkMapperCategory.getRegisteredNavigationAffiliate
import com.tokopedia.applink.category.DeeplinkMapperCategory.getRegisteredNavigationCatalog
import com.tokopedia.applink.category.DeeplinkMapperCategory.getRegisteredNavigationCategory
import com.tokopedia.applink.category.DeeplinkMapperCategory.getRegisteredNavigationExploreCategory
import com.tokopedia.applink.category.DeeplinkMapperMoneyIn.getRegisteredNavigationMoneyIn
import com.tokopedia.applink.chatbot.DeeplinkMapperChatbot.getChatbotDeeplink
import com.tokopedia.applink.common.DeeplinkMapperExternal
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.content.DeeplinkMapperContent
import com.tokopedia.applink.content.DeeplinkMapperContent.getContentCreatePostDeepLink
import com.tokopedia.applink.content.DeeplinkMapperContent.getKolDeepLink
import com.tokopedia.applink.content.DeeplinkMapperContent.getWebHostWebViewLink
import com.tokopedia.applink.digital.DeeplinkMapperDigital
import com.tokopedia.applink.digital.DeeplinkMapperDigital.getRegisteredNavigationDigital
import com.tokopedia.applink.digitaldeals.DeeplinkMapperDeals
import com.tokopedia.applink.digitaldeals.DeeplinkMapperDeals.getRegisteredNavigationDeals
import com.tokopedia.applink.entertaiment.DeeplinkMapperEntertainment
import com.tokopedia.applink.entertaiment.DeeplinkMapperEntertainment.getRegisteredNavigationEvents
import com.tokopedia.applink.etalase.DeepLinkMapperEtalase
import com.tokopedia.applink.feed.DeepLinkMapperFeed.getRegisteredFeed
import com.tokopedia.applink.find.DeepLinkMapperFind
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
import com.tokopedia.applink.imagepicker.DeeplinkMapperImagePicker
import com.tokopedia.applink.inbox.DeeplinkMapperInbox
import com.tokopedia.applink.internal.*
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.getDiscoveryDeeplink
import com.tokopedia.applink.logistic.DeeplinkMapperLogistic
import com.tokopedia.applink.marketplace.DeeplinkMapperMarketplace
import com.tokopedia.applink.marketplace.DeeplinkMapperMarketplace.getRegisteredNavigationMarketplace
import com.tokopedia.applink.marketplace.DeeplinkMapperMarketplace.getShopPageInternalAppLink
import com.tokopedia.applink.marketplace.DeeplinkMapperMarketplace.getTokopediaInternalProduct
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant.getRegisteredNavigationProductReview
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant.getRegisteredNavigationReputation
import com.tokopedia.applink.order.DeeplinkMapperOrder
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerAwbChange
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerAwbInvalid
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerComplaint
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerDelivered
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerRetur
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerWaitingAwb
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerWaitingPickup
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationOrder
import com.tokopedia.applink.powermerchant.PowerMerchantDeepLinkMapper
import com.tokopedia.applink.productmanage.DeepLinkMapperProductManage
import com.tokopedia.applink.promo.getDynamicDeeplinkForTokomember
import com.tokopedia.applink.promo.getRegisteredNavigationPromoFromHttp
import com.tokopedia.applink.promo.getRegisteredNavigationTokopoints
import com.tokopedia.applink.purchaseplatform.DeeplinkMapperWishlist
import com.tokopedia.applink.purchaseplatform.DeeplinkMapperUoh
import com.tokopedia.applink.recommendation.getRegisteredNavigationRecommendation
import com.tokopedia.applink.recommendation.getRegisteredNavigationRecommendationFromHttp
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
import com.tokopedia.applink.shopadmin.ShopAdminDeepLinkMapper
import com.tokopedia.applink.shopscore.ShopScoreDeepLinkMapper
import com.tokopedia.applink.statistic.DeepLinkMapperStatistic
import com.tokopedia.applink.teleporter.Teleporter
import com.tokopedia.applink.tokofood.DeeplinkMapperTokoFood
import com.tokopedia.applink.tokonow.DeeplinkMapperTokopediaNow.getRegisteredNavigationTokopediaNowCategory
import com.tokopedia.applink.tokonow.DeeplinkMapperTokopediaNow.getRegisteredNavigationTokopediaNowRecipeAutoComplete
import com.tokopedia.applink.tokonow.DeeplinkMapperTokopediaNow.getRegisteredNavigationTokopediaNowRecipeBookmark
import com.tokopedia.applink.tokonow.DeeplinkMapperTokopediaNow.getRegisteredNavigationTokopediaNowRecipeDetail
import com.tokopedia.applink.tokonow.DeeplinkMapperTokopediaNow.getRegisteredNavigationTokopediaNowRecipeHome
import com.tokopedia.applink.tokonow.DeeplinkMapperTokopediaNow.getRegisteredNavigationTokopediaNowRecipeSearch
import com.tokopedia.applink.tokonow.DeeplinkMapperTokopediaNow.getRegisteredNavigationTokopediaNowSearch
import com.tokopedia.applink.travel.DeeplinkMapperTravel
import com.tokopedia.applink.user.DeeplinkMapperUser
import com.tokopedia.config.GlobalConfig

/**
 * Function to map the deeplink to applink (registered in manifest)
 *
 * Example when there are 2 deeplink that has the same pattern:
 * tokopedia://product/add and tokopedia://product/{id}
 * tokopedia://product/add will be mapped to tokopedia-android-internal:// to prevent conflict.
 */
object DeeplinkMapper {

    private const val TOKOPEDIANOW_SEARCH_PATH = "/now/search"
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
                val query = getQuery(deeplink, uri)
                val tempDeeplink = getRegisteredNavigationFromHttp(context, uri, deeplink)
                UriUtil.appendDiffDeeplinkWithQuery(tempDeeplink, query)
            }
            DeeplinkConstant.SCHEME_TOKOPEDIA -> {
                val query = getQuery(deeplink, uri)
                val tempDeeplink = getRegisteredNavigationFromTokopedia(context, uri, deeplink)
                UriUtil.appendDiffDeeplinkWithQuery(tempDeeplink, query)
            }
            DeeplinkConstant.SCHEME_SELLERAPP -> {
                val query = getQuery(deeplink, uri)
                val tempDeeplink = getRegisteredNavigationFromSellerapp(context, uri, deeplink)
                UriUtil.appendDiffDeeplinkWithQuery(tempDeeplink, query)
            }
            DeeplinkConstant.SCHEME_INTERNAL -> {
                getRegisteredNavigationFromInternalTokopedia(context, uri, deeplink)
            }
            else -> deeplink
        }
        return mappedDeepLink
    }

    /**
     * Improvement from uri.query
     * Example url:
     * tokopedia://webview?url=https://registeruat.dbank.co.id/web-verification/#/tokopedia/
     * Expected query = https://registeruat.dbank.co.id/web-verification/#/tokopedia/
     */
    private fun getQuery(uriString: String, uri: Uri): String? {
        return if (uriString.contains("?")) {
            uriString.substringAfter("?")
        } else uri.query
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
        var deepLinkInternal = ApplinkConstInternalMarketplace.TOPCHAT
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
                    uri.pathSegments[0] == ApplinkConst.RewardFallback.Reward.REWARDS)
        ) {
            return ApplinkConstInternalPromo.TOKOPOINTS_HOME
        }

        val appLinkContent =
            DeeplinkMapperContent.getRegisteredNavigationContentFromHttp(uri, deeplink)
        if (appLinkContent.isNotBlank()) return appLinkContent

        val applinkDigital =
            DeeplinkMapperDigital.getRegisteredNavigationFromHttpDigital(context, deeplink)
        if (applinkDigital.isNotEmpty()) {
            return applinkDigital
        }

        val appLinkEvents =
            DeeplinkMapperEntertainment.getRegisteredNavigationFromHttpEvents(deeplink)
        if (appLinkEvents.isNotEmpty()) {
            return appLinkEvents
        }

        val appLinkDeals =
            DeeplinkMapperDeals.getRegisteredNavigationFromHttpDeals(deeplink)
        if (appLinkDeals.isNotEmpty()) {
            return appLinkDeals
        }

        val applinkFind =
                DeepLinkMapperFind.getRegisteredNavigationFindFromHttp(context, uri)
        if (applinkFind.isNotEmpty()) {
            return applinkFind
        }

        val appLinkSnapshot = DeeplinkMapperOrder.getRegisteredNavigationSnapshotFromBranchLink(uri)
        if (appLinkSnapshot.isNotBlank()) {
            return appLinkSnapshot
        }

        val applinkRecommendation = getRegisteredNavigationRecommendationFromHttp(uri)
        if (applinkRecommendation.isNotBlank()) {
            return applinkRecommendation
        }

        val applinkPromo = getRegisteredNavigationPromoFromHttp(uri)
        if(applinkPromo.isNotBlank()){
            return applinkPromo
        }

        if (pathSize >= 1 && uri.pathSegments[0] == "qrcode-login") {
            return DeeplinkMapperAccount.getLoginByQr(uri)
        }

        if (uri.path == TOKOPEDIANOW_SEARCH_PATH) {
            return getRegisteredNavigationTokopediaNowSearch(deeplink)
        }

        if (pathSize >= 1 && uri.pathSegments[0] == ApplinkConst.AFFILIATE_TOKO_HOST) {
            return DeeplinkMapperCategory.getRegisteredNavigationAffiliateFromHttp(uri)
        }

        return ""
    }

    val deeplinkPatternTokopediaSchemeList: MutableList<DLP> = mutableListOf(
            DLP(StartsWith(ApplinkConst.HOME),
                    targetDeeplink = { context, _, deeplink, _ -> getRegisteredNavigationHome(context, deeplink) }),
            DLP.matchPattern(ApplinkConst.PRODUCT_INFO,
                    targetDeeplink = { _, uri, _, idList -> getTokopediaInternalProduct(uri, idList) }),
            DLP.host(ApplinkConst.WEBVIEW_HOST) { _, _, _, _ -> ApplinkConstInternalGlobal.WEBVIEW_BASE },
            DLP.host(ApplinkConst.INBOX_HOST) { _, _, deeplink, _ -> DeeplinkMapperHome.getRegisteredInboxNavigation(deeplink) },
            DLP.startWith(ApplinkConst.QRSCAN, ApplinkConstInternalMarketplace.QR_SCANNEER),
            DLP.matchPattern(ApplinkConst.PRODUCT_REVIEW, targetDeeplink = { _, uri, _, _ -> DeeplinkMapperMerchant.getRegisteredNavigationProductDetailReview(uri) }),
            DLP.host(ApplinkConst.ACCOUNT_HOST) { ctx, _, deeplink, _ -> DeeplinkMapperAccount.getAccountInternalApplink(deeplink) },
            DLP.host(ApplinkConst.HOTEL_HOST) { ctx, _, deeplink, _ -> DeeplinkMapperTravel.getRegisteredNavigationTravel(ctx, deeplink) },
            DLP(DLPLogic { _, _, deeplink -> DeeplinkMapperUoh.isNavigationUohOrder(deeplink) },
                    targetDeeplink = { ctx, _, deeplink, _ -> DeeplinkMapperUoh.getRegisteredNavigationUohOrder(ctx, deeplink) }),
            DLP.startWith(ApplinkConst.BUYER_ORDER_EXTENSION, ApplinkConstInternalOrder.MARKETPLACE_INTERNAL_BUYER_ORDER_EXTENSION),
            DLP.exact(ApplinkConst.TRAVEL_SUBHOMEPAGE_HOME) { ctx, _, deeplink, _ -> getRegisteredNavigationDigital(ctx, deeplink) },
            DLP.startWith(ApplinkConst.DIGITAL) { ctx, _, deeplink, _ -> getRegisteredNavigationDigital(ctx, deeplink) },
            DLP.startWith(ApplinkConst.RECHARGE) { ctx, _, deeplink, _ -> getRegisteredNavigationDigital(ctx, deeplink) },
            DLP.startWith(ApplinkConst.DISCOVERY_SEARCH) { _, _, deeplink, _ -> getRegisteredNavigationSearch(deeplink) },
            DLP.startWith(ApplinkConst.CART) { ctx, _, deeplink, _ -> getRegisteredNavigationMarketplace(ctx, deeplink) },
            DLP.startWith(ApplinkConst.CHECKOUT) { ctx, _, deeplink, _ -> getRegisteredNavigationMarketplace(ctx, deeplink) },
            DLP.startWith(ApplinkConst.OCC) { ctx, _, deeplink, _ -> getRegisteredNavigationMarketplace(ctx, deeplink) },
            DLP.startWith(ApplinkConst.DEALS_HOME) { ctx, _, deeplink, _ -> getRegisteredNavigationDeals(ctx, deeplink) },
            DLP.startWith(ApplinkConst.FIND) { _, _, deeplink, _ -> getRegisteredFind(deeplink) },
            DLP.startWith(ApplinkConst.AMP_FIND) { _, _, deeplink, _ -> getRegisteredFind(deeplink) },
            DLP.matchPattern(ApplinkConst.PROFILE) { _, _, deeplink, _ -> DeeplinkMapperContent.getRegisteredNavigation(deeplink) },
            DLP.host(ApplinkConst.Digital.CATEGORY_EXPLORE_HOST) { _, _, deeplink, _ -> getRegisteredNavigationExploreCategory(deeplink) },
            DLP.host(ApplinkConst.CATEGORY_HOST) { _, uri, deeplink, _ -> getRegisteredCategoryNavigation(deeplink, uri) },
            DLP.matchPattern(ApplinkConst.PLAY_DETAIL) { _, _, deeplink, _ -> DeeplinkMapperContent.getRegisteredNavigation(deeplink) },
            DLP.startWith(ApplinkConst.PLAY_BROADCASTER) { _, _, deeplink, _ -> DeeplinkMapperContent.getRegisteredNavigation(deeplink) },
            DLP.startWith(ApplinkConst.PLAY_SHORTS) { _, _, deeplink, _ -> DeeplinkMapperContent.getRegisteredNavigation(deeplink) },
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
            DLP.host(ApplinkConst.REVIEW_HOST) { _, _, deeplink, _ -> getRegisteredNavigationReputation(deeplink) },
            DLP.startWith(ApplinkConst.SELLER_REVIEW) { _, _, deeplink, _ -> ApplinkConstInternalMarketplace.SELLER_REVIEW_DETAIL },
            DLP.startWith(ApplinkConst.TOKOPOINTS) { ctx, _, deeplink, _ -> getRegisteredNavigationTokopoints( deeplink) },
            DLP.startWith(ApplinkConst.TOKOPEDIA_REWARD) { ctx, _, deeplink, _ -> getRegisteredNavigationTokopoints( deeplink) },
            DLP.startWith(ApplinkConst.DEFAULT_RECOMMENDATION_PAGE) { _, _, deeplink, _ -> getRegisteredNavigationRecommendation(deeplink) },
            DLP.startWith(ApplinkConst.HOME_EXPLORE) { _, _, deeplink, _ -> getRegisteredExplore(deeplink) },
            DLP.host(ApplinkConst.CHATBOT_HOST) { _, _, deeplink, _ -> getChatbotDeeplink(deeplink) },
            DLP.startWith(ApplinkConst.DISCOVERY_CATALOG) { _, _, deeplink, _ -> getRegisteredNavigationCatalog(deeplink) },
            DLP.startWith(ApplinkConst.EPHARMACY) { _, _, deeplink, _ -> getRegisteredNavigationCategory(deeplink) },
            DLP.matchPattern(ApplinkConst.AFFILIATE_TOKO) { _, _, deeplink, _ -> getRegisteredNavigationAffiliate(deeplink) },
            DLP.matchPattern(ApplinkConst.AFFILIATE_TOKO_HELP) { _, _, deeplink, _ -> getRegisteredNavigationAffiliate(deeplink) },
            DLP.matchPattern(ApplinkConst.AFFILIATE_TOKO_TRANSACTION_HISTORY) { _, _, deeplink, _ -> getRegisteredNavigationAffiliate(deeplink) },
            DLP.matchPattern(ApplinkConst.AFFILIATE_TOKO_ONBOARDING) { _, _, deeplink, _ -> getRegisteredNavigationAffiliate(deeplink) },
            DLP.startWith(ApplinkConst.MONEYIN) { _, _, deeplink, _ -> getRegisteredNavigationMoneyIn(deeplink) },
            DLP.startWith(ApplinkConst.OQR_PIN_URL_ENTRY_LINK) { _, uri, _, _ -> getRegisteredNavigationForFintech(uri) },
            DLP.startWith(ApplinkConst.LAYANAN_FINANSIAL) { _, _, deeplink, _ -> getRegisteredNavigationForLayanan(deeplink) },
            DLP.startWith(ApplinkConst.SALAM_UMRAH) { ctx, _, deeplink, _ -> getRegisteredNavigationSalamUmrah(deeplink, ctx) },
            DLP.startWith(ApplinkConst.SALAM_UMRAH_ORDER_DETAIL) { ctx, _, deeplink, _ -> getRegisteredNavigationSalamUmrahOrderDetail(deeplink, ctx) },
            DLP.startWith(ApplinkConst.OFFICIAL_STORE) { _, _, deeplink, _ -> getRegisteredNavigationHomeOfficialStore(deeplink) },
            DLP.startWith(ApplinkConst.GOLD_MERCHANT_STATISTIC_DASHBOARD) { ctx, _, deeplink, _ -> getRegisteredNavigationMarketplace(ctx, deeplink) },
            DLP.startWith(ApplinkConst.SHOP_SCORE_DETAIL) { _, uri, _, _ -> ShopScoreDeepLinkMapper.getInternalAppLinkShopScore(uri) },
            DLP.exact(ApplinkConst.SHOP_PENALTY, ApplinkConstInternalMarketplace.SHOP_PENALTY),
            DLP.exact(ApplinkConst.SHOP_PENALTY_DETAIL, ApplinkConstInternalMarketplace.SHOP_PENALTY_DETAIL),
            DLP.exact(ApplinkConst.ADMIN_INVITATION, ApplinkConstInternalMarketplace.ADMIN_INVITATION),
            DLP.startWith(ApplinkConst.ADMIN_ACCEPTED) { _, uri, _, _ -> ShopAdminDeepLinkMapper.getInternalAppLinkAdminAccepted(uri) },
            DLP.exact(ApplinkConst.ADMIN_REDIRECTION, ApplinkConstInternalMarketplace.ADMIN_REDIRECTION),
            DLP.startWith(ApplinkConst.POWER_MERCHANT_PRO_INTERRUPT) { _, _, deeplink, _ -> PowerMerchantDeepLinkMapper.getInternalAppLinkPmProInterrupt(deeplink) },
            DLP.startWith(ApplinkConst.Gamification.CRACK) { _, _, deeplink, _ -> DeeplinkMapperGamification.getGamificationDeeplink(deeplink) },
            DLP.startWith(ApplinkConst.SELLER_ORDER_DETAIL) { context, uri, deeplink, _ -> getRegisteredNavigationOrder(context, uri, deeplink) },
            DLP.startWith(ApplinkConst.LOGISTIC_SELLER_RESCHEDULE) { context, uri, deeplink, _ -> DeeplinkMapperLogistic.getReschedulePickupDeeplink(context, uri, deeplink) },
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
            DLP.startWith(ApplinkConst.FEED_PlAY_LIVE_DETAIL, ApplinkConstInternalFeed.INTERNAL_PLAY_LIVE_DETAILS),
            DLP.exact(ApplinkConst.FEED,
                targetDeeplink = { _, _, _, _ -> getRegisteredNavigationHomeFeed() }),
            DLP(Exact(ApplinkConst.PROMO).or(Exact(ApplinkConst.PROMO_LIST)),
                targetDeeplink = { _, _, _, _ -> ApplinkConstInternalPromo.PROMO_LIST }),
            DLP.matchPattern(ApplinkConst.PROMO_DETAIL,
                targetDeeplink = { _, _, _, idList -> UriUtil.buildUri(ApplinkConstInternalPromo.PROMO_DETAIL, idList?.getOrNull(0)) }),
            DLP.matchPattern(ApplinkConst.CONTENT_EXPLORE) { _, _, deeplink, _ -> getRegisteredNavigationHomeContentExplore(deeplink) },
            DLP(MatchPattern(ApplinkConst.SHOP),
                    targetDeeplink = { ctx, uri, deeplink, idList -> getShopPageInternalAppLink(ctx, uri, deeplink, UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_PAGE, idList?.getOrNull(0)), idList?.getOrNull(0).orEmpty()) }),
            DLP.matchPattern(ApplinkConst.SHOP_HOME,
                    targetDeeplink = { ctx, uri, deeplink, idList -> getShopPageInternalAppLink(ctx, uri, deeplink, UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_PAGE_HOME, idList?.getOrNull(0)), idList?.getOrNull(0).orEmpty()) }),
            DLP.matchPattern(ApplinkConst.SHOP_PRODUCT,
                    targetDeeplink = { ctx, uri, deeplink, idList -> getShopPageInternalAppLink(ctx, uri, deeplink, UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_PAGE_PRODUCT, idList?.getOrNull(0)), idList?.getOrNull(0).orEmpty()) }),
            DLP.matchPattern(ApplinkConst.SHOP_FEED,
                    targetDeeplink = { ctx, uri, deeplink, idList -> getShopPageInternalAppLink(ctx, uri, deeplink,UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_PAGE_FEED, idList?.getOrNull(0)), idList?.getOrNull(0).orEmpty()) }),
            DLP.matchPattern(ApplinkConst.SHOP_REVIEW,
                    targetDeeplink = { ctx, uri, deeplink, idList -> getShopPageInternalAppLink(ctx, uri, deeplink,UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_PAGE_REVIEW, idList?.getOrNull(0)), idList?.getOrNull(0).orEmpty()) }),
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
            DLP.matchPattern(ApplinkConst.SHOP_OPERATIONAL_HOUR) { _, uri, _, idList -> DeeplinkMapperMarketplace.getShopOperationalHourInternalAppLink(idList?.getOrNull(0).orEmpty()) },
            DLP.matchPattern(ApplinkConst.SHOP_MVC_LOCKED_TO_PRODUCT) { _, uri, _, idList ->
                val shopId = idList?.getOrNull(0).orEmpty()
                val voucherId = idList?.getOrNull(1).orEmpty()
                DeeplinkMapperMarketplace.getShopMvcLockedToProductShopIdInternalAppLink(shopId, voucherId)
            },
            DLP.startWith(ApplinkConst.SELLER_INFO_DETAIL) { _, uri, _, _ -> DeeplinkMapperMerchant.getSellerInfoDetailApplink(uri) },
            DLP.matchPattern(ApplinkConst.ORDER_TRACKING) { _, _, deeplink, _ -> DeeplinkMapperLogistic.getRegisteredNavigationOrder(deeplink) },
            DLP.matchPattern(ApplinkConst.ORDER_POD) { _, _, deeplink, _ -> DeeplinkMapperLogistic.getRegisteredNavigationPod(deeplink) },

            DLP.matchPattern(ApplinkConst.ORDER_HISTORY_SHOP) { _, _, _, idList -> UriUtil.buildUri(ApplinkConstInternalMarketplace.ORDER_HISTORY, idList?.getOrNull(0)) },
            DLP.startWith(ApplinkConst.RESET_PASSWORD, ApplinkConstInternalUserPlatform.FORGOT_PASSWORD),
            DLP.matchPattern(ApplinkConst.KOL_COMMENT) { _, _, deeplink, _ -> getKolDeepLink(deeplink) },
            DLP.matchPattern(ApplinkConst.CONTENT_DETAIL,
                    targetDeeplink = { _, _, deeplink, _ -> getKolDeepLink(deeplink) }),
            DLP.host(ApplinkConst.KOL_YOUTUBE_HOST_BASE) { _, _, deeplink, _ -> getKolDeepLink(deeplink) },
            DLP.startWith(ApplinkConst.DISCOVERY) { _, _, deeplink, _ -> getDiscoveryDeeplink(deeplink) },
            DLP(DLPLogic { _, uri, _ -> matchWithHostAndHasPath(uri, ApplinkConst.HOST_CATEGORY_P) },
                    targetDeeplink = { _, uri, _, _ -> getRegisteredCategoryNavigation(uri) }),
            DLP.startWith(ApplinkConst.MARKETPLACE_ONBOARDING, ApplinkConstInternalMarketplace.ONBOARDING),
            DLP.startWith(ApplinkConst.POWER_MERCHANT_SUBSCRIBE) { ctx, _, _, _ -> PowerMerchantDeepLinkMapper.getPowerMerchantAppLink(ctx) },
            DLP.exact(ApplinkConst.PM_BENEFIT_PACKAGE, ApplinkConstInternalMarketplace.PM_BENEFIT_PACKAGE),
            DLP.exact(ApplinkConst.SELLER_SHIPPING_EDITOR, ApplinkConstInternalMarketplace.SHOP_SETTINGS_SHIPPING),
            DLP.exact(ApplinkConst.SELLER_CUSTOM_PRODUCT_LOGISTIC, ApplinkConstInternalLogistic.CUSTOM_PRODUCT_LOGISTIC),
            DLP.exact(ApplinkConst.SELLER_COD_ACTIVATION, ApplinkConstInternalMarketplace.SHOP_SETTINGS_COD),
            DLP.exact(ApplinkConst.SELLER_WAREHOUSE_DATA, ApplinkConstInternalMarketplace.SHOP_SETTINGS_ADDRESS),
            DLP.exact(ApplinkConst.SETTING_ADDRESS, ApplinkConstInternalLogistic.MANAGE_ADDRESS),
            DLP.exact(ApplinkConst.SETTING_PAYMENT, ApplinkConstInternalUserPlatform.PAYMENT_SETTING),
            DLP.exact(ApplinkConst.SETTING_ACCOUNT, ApplinkConstInternalUserPlatform.ACCOUNT_SETTING),
            DLP.exact(ApplinkConst.ADD_CREDIT_CARD, ApplinkConstInternalPayment.PAYMENT_ADD_CREDIT_CARD),
            DLP.exact(ApplinkConst.PMS, ApplinkConstInternalPayment.PMS_PAYMENT_LIST),
            DLP.exact(ApplinkConst.GOPAY_KYC, ApplinkConstInternalPayment.GOPAY_KYC),
            DLP.exact(ApplinkConst.SETTING_NOTIFICATION, ApplinkConstInternalMarketplace.USER_NOTIFICATION_SETTING),
            DLP.exact(ApplinkConst.REGISTER, ApplinkConstInternalUserPlatform.INIT_REGISTER),
            DLP.exact(ApplinkConst.ADD_NAME_PROFILE, ApplinkConstInternalUserPlatform.MANAGE_NAME),
            DLP.exact(ApplinkConst.KYC_NO_PARAM, ApplinkConstInternalGlobal.USER_IDENTIFICATION_INFO_BASE),
            DLP.exact(ApplinkConst.KYC_FORM_NO_PARAM, ApplinkConstInternalGlobal.USER_IDENTIFICATION_FORM_BASE),
            DLP.startWith(ApplinkConst.KYC_FORM_ONLY_NO_PARAM) { _, _, deeplink, _ ->
                DeeplinkMapperExternal.getRegisteredNavigation(deeplink) },
            DLP.exact(ApplinkConst.SETTING_BANK, ApplinkConstInternalGlobal.SETTING_BANK),
            DLP.exact(ApplinkConst.OTP, ApplinkConstInternalUserPlatform.COTP),
            DLP.startWith(ApplinkConst.QR_LOGIN, ApplinkConstInternalUserPlatform.QR_LOGIN),
            DLP.exact(ApplinkConst.LOGIN, ApplinkConstInternalUserPlatform.LOGIN),
            DLP.exact(ApplinkConst.OTP_PUSH_NOTIF_RECEIVER, ApplinkConstInternalUserPlatform.OTP_PUSH_NOTIF_RECEIVER),
            DLP.exact(ApplinkConst.FLIGHT, ApplinkConstInternalTravel.DASHBOARD_FLIGHT),
            DLP.exact(ApplinkConst.SALDO, ApplinkConstInternalGlobal.SALDO_DEPOSIT),
            DLP.exact(ApplinkConst.SALDO_INTRO, ApplinkConstInternalGlobal.SALDO_INTRO),
            DLP.exact(ApplinkConst.INBOX_TICKET, ApplinkConstInternalOperational.INTERNAL_INBOX_LIST),
            DLP.exact(ApplinkConst.Navigation.MAIN_NAV, ApplinkConsInternalNavigation.MAIN_NAVIGATION),
            DLP.exact(ApplinkConst.RECENT_VIEW, ApplinkConsInternalHome.HOME_RECENT_VIEW),
            DLP.exact(ApplinkConst.WISHLIST) { ctx, _, _, _ -> DeeplinkMapperWishlist.getRegisteredNavigationWishlist(ctx) },
            DLP.exact(ApplinkConst.NEW_WISHLIST) { ctx, _, _, _ -> DeeplinkMapperWishlist.getRegisteredNavigationWishlist(ctx) },
            DLP.exact(ApplinkConst.CREATE_SHOP, ApplinkConstInternalUserPlatform.LANDING_SHOP_CREATION),
            DLP.exact(ApplinkConst.CHAT_TEMPLATE, ApplinkConstInternalMarketplace.CHAT_SETTING_TEMPLATE),
            DLP.exact(ApplinkConst.NOTIFICATION) { _, _, _, _ ->
                DeeplinkMapperInbox.getRegisteredNavigationNotifcenter()
            },
            DLP.exact(ApplinkConst.BUYER_INFO) { _, _, _, _ ->
                DeeplinkMapperInbox.getRegisteredNavigationNotifcenter()
            },
            DLP.exact(ApplinkConst.SHOP_SETTINGS_NOTE, ApplinkConstInternalMarketplace.SHOP_SETTINGS_NOTES),
            DLP.exact(ApplinkConst.SHOP_SETTINGS_INFO, ApplinkConstInternalMarketplace.SHOP_SETTINGS_INFO),
            DLP.exact(ApplinkConst.MY_SHOP_ETALASE_LIST, ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST),
            DLP.exact(ApplinkConst.HAS_PASSWORD, ApplinkConstInternalUserPlatform.HAS_PASSWORD),
            DLP.exact(ApplinkConst.THANK_YOU_PAGE_NATIVE, ApplinkConstInternalPayment.PAYMENT_THANK_YOU_PAGE),
            DLP.exact(ApplinkConst.HOWTOPAY, ApplinkConstInternalPayment.INTERNAL_HOW_TO_PAY),
            DLP.startWith(ApplinkConst.PAYLATER, ApplinkConstInternalFintech.PAYLATER),
            DLP.startWith(ApplinkConst.ACTIVATION_GOPAY, ApplinkConstInternalFintech.ACTIVATE_GOPAY),
            DLP.startWith(ApplinkConst.OPTIMIZED_CHECKOUT, ApplinkConstInternalFintech.OCC_CHECKOUT),
            DLP.exact(ApplinkConst.MERCHANT_VOUCHER_LIST, ApplinkConstInternalSellerapp.VOUCHER_LIST),
            DLP.exact(ApplinkConst.NOTIFICATION_TROUBLESHOOTER, ApplinkConstInternalUserPlatform.PUSH_NOTIFICATION_TROUBLESHOOTER),
            DLP.exact(ApplinkConst.PROFILE_COMPLETION, ApplinkConstInternalUserPlatform.PROFILE_COMPLETION),
            DLP.exact(ApplinkConst.FEEDBACK_FORM, ApplinkConstInternalGlobal.FEEDBACK_FORM),
            DLP.startWith(ApplinkConst.REGISTER_INIT, ApplinkConstInternalUserPlatform.INIT_REGISTER),
            DLP.host(ApplinkConst.SHARING_HOST) { _, _, deeplink, _ -> DeeplinkMapperExternal.getRegisteredNavigation(deeplink) },
            DLP.exact(ApplinkConst.LINK_ACCOUNT, ApplinkConstInternalUserPlatform.LINK_ACCOUNT_WEBVIEW),
            DLP.exact(ApplinkConst.EXPLICIT_PROFILE, ApplinkConstInternalUserPlatform.EXPLICIT_PROFILE),
            DLP.startWith(ApplinkConst.SELLER_CENTER) { _, _, _, _ -> DeeplinkMapperMerchant.getRegisteredSellerCenter() },
            DLP.startWith(ApplinkConst.SNAPSHOT_ORDER) { ctx, _, deeplink, _ -> DeeplinkMapperOrder.getSnapshotOrderInternalAppLink(ctx, deeplink) },
            DLP.startWith(ApplinkConst.ORDER_BUYER_CANCELLATION_REQUEST_PAGE) { _, _, _, _ -> DeeplinkMapperOrder.getBuyerCancellationRequestInternalAppLink() },
            DLP.exact(ApplinkConst.TokopediaNow.HOME) { _, _, _, _ -> ApplinkConstInternalTokopediaNow.HOME },
            DLP.startWith(ApplinkConst.TokopediaNow.SEARCH) { _, _, deeplink, _ -> getRegisteredNavigationTokopediaNowSearch(deeplink) },
            DLP.startWith(ApplinkConst.TokopediaNow.CATEGORY) { _, _, deeplink, _ -> getRegisteredNavigationTokopediaNowCategory(deeplink) },
            DLP.startWith(ApplinkConst.TokopediaNow.REPURCHASE) { _, _, _, _ -> ApplinkConstInternalTokopediaNow.REPURCHASE },
            DLP.matchPattern(ApplinkConst.TokopediaNow.RECIPE_DETAIL) { _, _, deeplink, _ -> getRegisteredNavigationTokopediaNowRecipeDetail(deeplink) },
            DLP.startWith(ApplinkConst.TokopediaNow.RECIPE_BOOKMARK) { _, _, deeplink, _ -> getRegisteredNavigationTokopediaNowRecipeBookmark(deeplink) },
            DLP.exact(ApplinkConst.TokopediaNow.RECIPE_HOME) { _, _, deeplink, _ -> getRegisteredNavigationTokopediaNowRecipeHome(deeplink) },
            DLP.startWith(ApplinkConst.TokopediaNow.RECIPE_SEARCH) { _, _, deeplink, _ -> getRegisteredNavigationTokopediaNowRecipeSearch(deeplink) },
            DLP.startWith(ApplinkConst.TokopediaNow.RECIPE_AUTO_COMPLETE) { _, _, deeplink, _ -> getRegisteredNavigationTokopediaNowRecipeAutoComplete(deeplink) },
            DLP.startWith(ApplinkConst.TELEPHONY_MASKING, ApplinkConstInternalUserPlatform.TELEPHONY_MASKING),
            DLP.startWith(ApplinkConst.SellerApp.TOPADS_CREATE_MANUAL_ADS, ApplinkConstInternalTopAds.TOPADS_AUTOADS_CREATE_MANUAL_ADS),
            DLP.matchPattern(ApplinkConst.PRODUCT_BUNDLE, targetDeeplink = { _, _, _, idList ->
                UriUtil.buildUri(ApplinkConstInternalMechant.MERCHANT_PRODUCT_BUNDLE, idList?.getOrNull(0)) }),
            DLP.matchPattern(ApplinkConst.GIFTING, targetDeeplink = { _, _, _, idList ->
                UriUtil.buildUri(ApplinkConstInternalMechant.MERCHANT_GIFTING, idList?.getOrNull(0)) }),
            DLP.startWith(ApplinkConst.SellerApp.TOPADS_CREATE_MANUAL_ADS, ApplinkConstInternalTopAds.TOPADS_AUTOADS_CREATE_MANUAL_ADS),
            DLP.host(ApplinkConst.WEBVIEW_DOWNLOAD_HOST) { _, _, _, _ -> ApplinkConstInternalGlobal.WEBVIEW_DOWNLOAD },
            DLP.host(ApplinkConst.WEBVIEW_PARENT_HOME_HOST) { _, _, _, _ -> ApplinkConstInternalGlobal.WEBVIEW_BACK_HOME },
            DLP.host(ApplinkConst.BROWSER_HOST) { _, _, _, _ -> ApplinkConstInternalGlobal.BROWSER },
            DLP.startWith(ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST_V2) { _, _, deeplink, _ -> getContentCreatePostDeepLink(deeplink) },
            DLP.startWith(ApplinkConst.FEED_CREATION_PRODUCT_SEARCH) { _, _, deeplink, _ -> getContentCreatePostDeepLink(deeplink) },
            DLP.startWith(ApplinkConst.FEED_CREATION_SHOP_SEARCH) { _, _, deeplink, _ -> getContentCreatePostDeepLink(deeplink) },
            DLP.host(ApplinkConst.AFFILIATE_PRODUCT_PICKER_FROM_SHOP_HOST) { _, _, deeplink, _ -> getContentCreatePostDeepLink(deeplink) },
            DLP.exact(ApplinkConst.REVIEW_REMINDER_PREVIOUS, ApplinkConstInternalSellerapp.REVIEW_REMINDER),
            DLP.matchPattern(ApplinkConst.IMAGE_PICKER_V2) { _, _, deeplink, _ -> DeeplinkMapperImagePicker.getImagePickerV2Deeplink(deeplink) },
            DLP(DLPLogic { _, uri, _ -> DeeplinkMapperFintech.isHomeCreditRegister(uri) },
                targetDeeplink = { _, uri, _, _ -> DeeplinkMapperFintech.getRegisteredNavigationForHomeCreditRegister(uri) }),

            DLP.host(ApplinkConst.HOST_PLAY_NOTIF_VIDEO) { _, _, _, _ -> ApplinkConstInternalGlobal.YOUTUBE_VIDEO },
            DLP.startWith(ApplinkConst.CHANGE_INACTIVE_PHONE) { ctx, _, deeplink, _ -> DeeplinkMapperUser.getRegisteredNavigationUser(ctx, deeplink)},
            DLP.exact(ApplinkConst.ADD_PIN_ONBOARD) { ctx, _, deeplink, _ -> DeeplinkMapperUser.getRegisteredNavigationUser(ctx, deeplink)},
            DLP.exact(ApplinkConst.SETTING_PROFILE) { ctx, _, deeplink, _ -> DeeplinkMapperUser.getRegisteredNavigationUser(ctx, deeplink) },
            DLP.exact(ApplinkConst.MediaEditor.MEDIA_EDITOR, ApplinkConstInternalMedia.INTERNAL_MEDIA_EDITOR),
            DLP.exact(ApplinkConst.MediaPicker.MEDIA_PICKER, ApplinkConstInternalMedia.INTERNAL_MEDIA_PICKER),
            DLP.exact(ApplinkConst.MediaPicker.MEDIA_PICKER_PREVIEW, ApplinkConstInternalMedia.INTERNAL_MEDIA_PICKER_PREVIEW),
            DLP.host(ApplinkConst.WEB_HOST) {_, _, deeplink, _ -> getWebHostWebViewLink(deeplink)},
            DLP.exact(ApplinkConst.INPUT_INACTIVE_NUMBER) { ctx, _, deeplink, _ -> DeeplinkMapperUser.getRegisteredNavigationUser(ctx, deeplink) },
            DLP.exact(ApplinkConst.ADD_PHONE) { ctx, _, deeplink, _ -> DeeplinkMapperUser.getRegisteredNavigationUser(ctx, deeplink) },
            DLP.matchPattern(ApplinkConst.PRODUCT_EDUCATIONAL, targetDeeplink = { _, _, _, idList ->
                UriUtil.buildUri(
                        ApplinkConstInternalMarketplace.PRODUCT_DETAIL_EDUCATIONAL,
                        idList?.lastOrNull() ?: "")
            }),
            DLP.startWith(ApplinkConst.TokoFood.MAIN_PATH) { _, uri, _, _ -> DeeplinkMapperTokoFood.mapperInternalApplinkTokoFood(uri) },
            DLP.startWith(ApplinkConst.TokoFood.GOFOOD) { _, uri, _, _ -> DeeplinkMapperTokoFood.mapperInternalApplinkTokoFood(uri) },
            DLP.matchPattern(ApplinkConst.RESOLUTION_SUCCESS) { _, uri, _, _ -> ApplinkConstInternalOperational.buildApplinkResolution(uri)},
            DLP.exact(ApplinkConst.DISCOVERY_SEARCH_UNIVERSAL) { _, _, deeplink, _ -> getRegisteredNavigationSearch(deeplink) },
            DLP.matchPattern(ApplinkConst.WISHLIST_COLLECTION_DETAIL, targetDeeplink = { _, _, _, idList ->
                UriUtil.buildUri(ApplinkConstInternalPurchasePlatform.WISHLIST_COLLECTION_DETAIL_INTERNAL, idList?.getOrNull(0)) })
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
        deeplinkPatternTokopediaSchemeList.add(
            0,
            deeplinkPatternTokopediaSchemeList.removeAt(index)
        )
    }

    private fun getRegisteredNavigationFromInternalTokopedia(context: Context, uri: Uri, deeplink: String): String {
        return when {
            deeplink.startsWith(ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST) -> DeepLinkMapperProductManage.getProductListInternalAppLink(deeplink)
            deeplink.startsWith(ApplinkConstInternalMarketplace.TOPCHAT) && shouldRedirectToSellerApp(uri) -> AppLinkMapperSellerHome.getTopChatAppLink(uri)
            deeplink.startsWith(ApplinkConstInternalOrder.NEW_ORDER) -> getSomNewOrderAppLink(uri)
            deeplink.startsWith(ApplinkConstInternalOrder.READY_TO_SHIP) -> getSomReadyToShipAppLink(uri)
            deeplink.startsWith(ApplinkConstInternalOrder.SHIPPED) -> getSomShippedAppLink(uri)
            deeplink.startsWith(ApplinkConstInternalOrder.FINISHED) -> getSomDoneAppLink(uri)
            deeplink.startsWith(ApplinkConstInternalOrder.CANCELLED) -> getSomCancelledAppLink(uri)
            deeplink.startsWith(ApplinkConstInternalOrder.CANCELLATION_REQUEST) -> getSomCancellationRequestAppLink(uri)
            deeplink.startsWith(ApplinkConstInternalOrder.HISTORY) -> getSomAllOrderAppLink(uri)
            deeplink.startsWith(ApplinkConstInternalMarketplace.SHOP_PAGE_BASE) -> getShopPageInternalAppLink(context,uri,deeplink, "", uri.pathSegments.getOrNull(1).orEmpty())
            deeplink.startsWith(ApplinkConstInternalGlobal.ADVANCED_SETTING) -> DeeplinkMapperUser.getRegisteredNavigationUser(context,deeplink)
            deeplink.startsWith(ApplinkConstInternalGlobal.GENERAL_SETTING) -> DeeplinkMapperUser.getRegisteredNavigationUser(context, deeplink)
            deeplink.startsWith(ApplinkConsInternalHome.HOME_WISHLIST) -> DeeplinkMapperWishlist.getRegisteredNavigationWishlist(context)
            deeplink.startsWith(ApplinkConstInternalMarketplace.ADD_ON_GIFTING) -> getRegisteredNavigationMarketplace(context, deeplink)
            deeplink == ApplinkConstInternalUserPlatform.SETTING_PROFILE -> DeeplinkMapperUser.getRegisteredNavigationUser(context, deeplink)
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
    private fun getRegisteredNavigationFromSellerapp(context: Context, uri: Uri, deeplink: String): String {
        return when (val trimmedDeeplink = UriUtil.trimDeeplink(uri, deeplink)) {
            ApplinkConst.SellerApp.WEBVIEW -> ApplinkConstInternalGlobal.WEBVIEW_BASE
            ApplinkConst.SellerApp.BROWSER -> ApplinkConstInternalGlobal.BROWSER
            ApplinkConst.SellerApp.TOPADS_DASHBOARD -> ApplinkConstInternalTopAds.TOPADS_DASHBOARD_INTERNAL
            ApplinkConst.SellerApp.SHOP_DISCOUNT -> ApplinkConstInternalSellerapp.SHOP_DISCOUNT
            ApplinkConst.SellerApp.TOPADS_CREDIT_HISTORY -> ApplinkConstInternalTopAds.TOPADS_HISTORY_CREDIT
            ApplinkConst.SellerApp.TOPADS_CREDIT -> ApplinkConstInternalTopAds.TOPADS_BUY_CREDIT
            ApplinkConst.SellerApp.TOPADS_CREATE_AUTO_ADS -> ApplinkConstInternalTopAds.TOPADS_AUTOADS_CREATE
            ApplinkConst.SellerApp.TOPADS_CREATE_ADS -> ApplinkConstInternalTopAds.TOPADS_CREATE_ADS
            ApplinkConst.SellerApp.TOPADS_CREATE_ONBOARDING -> ApplinkConstInternalTopAds.TOPADS_CREATION_ONBOARD
            ApplinkConst.SellerApp.TOPADS_ONBOARDING -> ApplinkConstInternalTopAds.TOPADS_ONBOARDING
            ApplinkConst.SellerApp.TOPADS_HEADLINE_CREATE -> ApplinkConstInternalTopAds.TOPADS_HEADLINE_ADS_CREATION
            ApplinkConst.SellerApp.TOPADS_HEADLINE_DETAIL -> ApplinkConstInternalTopAds.TOPADS_HEADLINE_DETAIL
            ApplinkConst.SellerApp.TOPADS_EDIT_AUTO_ADS -> ApplinkConstInternalTopAds.TOPADS_EDIT_AUTOADS
            ApplinkConst.SellerApp.TOPADS_AUTO_TOPUP -> ApplinkConstInternalTopAds.TOPADS_AUTO_TOPUP
            ApplinkConst.SellerApp.TOPADS_CREATE_CHOOSER -> ApplinkConstInternalTopAds.TOPADS_CREATE_CHOOSER
            ApplinkConst.SellerApp.SELLER_ONBOARDING -> ApplinkConstInternalSellerapp.WELCOME
            ApplinkConst.SellerApp.SELLER_APP_HOME -> ApplinkConstInternalSellerapp.SELLER_HOME
            ApplinkConst.SellerApp.SELLER_SEARCH -> ApplinkConstInternalSellerapp.SELLER_SEARCH
            ApplinkConst.SellerApp.PRODUCT_ADD -> ApplinkConstInternalMechant.MERCHANT_OPEN_PRODUCT_PREVIEW
            ApplinkConst.SellerApp.POWER_MERCHANT_SUBSCRIBE -> PowerMerchantDeepLinkMapper.getPowerMerchantAppLink(context)
            ApplinkConst.SellerApp.PM_BENEFIT_PACKAGE -> ApplinkConstInternalMarketplace.PM_BENEFIT_PACKAGE
            ApplinkConst.SellerApp.CREATE_VOUCHER -> ApplinkConstInternalSellerapp.CREATE_VOUCHER
            ApplinkConst.SellerApp.VOUCHER_LIST -> ApplinkConstInternalSellerapp.VOUCHER_LIST
            ApplinkConst.SellerApp.VOUCHER_ACTIVE -> ApplinkConstInternalSellerapp.VOUCHER_ACTIVE
            ApplinkConst.SellerApp.VOUCHER_HISTORY -> ApplinkConstInternalSellerapp.VOUCHER_HISTORY
            ApplinkConst.SellerApp.VOUCHER_DETAIL -> ApplinkConstInternalSellerapp.VOUCHER_DETAIL
            ApplinkConst.SellerApp.CAMPAIGN_LIST -> ApplinkConstInternalSellerapp.CAMPAIGN_LIST
            ApplinkConst.SellerApp.CENTRALIZED_PROMO -> ApplinkConstInternalSellerapp.CENTRALIZED_PROMO
            ApplinkConst.SellerApp.PLAY_BROADCASTER -> ApplinkConstInternalContent.INTERNAL_PLAY_BROADCASTER
            ApplinkConst.SellerApp.SELLER_SHIPPING_EDITOR -> ApplinkConstInternalMarketplace.SHOP_SETTINGS_SHIPPING
            ApplinkConst.SellerApp.REVIEW_REMINDER -> ApplinkConstInternalSellerapp.REVIEW_REMINDER
            ApplinkConst.SellerApp.STATISTIC_DASHBOARD -> DeepLinkMapperStatistic.getStatisticAppLink(uri)
            ApplinkConst.SellerApp.SHOP_SCORE_DETAIL -> ShopScoreDeepLinkMapper.getInternalAppLinkShopScore(uri)
            ApplinkConst.SellerApp.TOKOMEMBER -> ApplinkConstInternalSellerapp.TOKOMEMBER
            ApplinkConst.SellerApp.TOKOMEMBER_PROGRAM_LIST -> ApplinkConstInternalSellerapp.TOKOMEMBER_PROGRAM_LIST
            ApplinkConst.SellerApp.TOKOMEMBER_COUPON_LIST -> ApplinkConstInternalSellerapp.TOKOMEMBER_COUPON_LIST
            ApplinkConst.SellerApp.TOKOMEMBER_PROGRAM_CREATION -> ApplinkConstInternalSellerapp.TOKOMEMBER_PROGRAM_CREATION
            ApplinkConst.SellerApp.TOKOMEMBER_COUPON_CREATION -> ApplinkConstInternalSellerapp.TOKOMEMBER_COUPON_CREATION
            ApplinkConst.SellerApp.ADMIN_INVITATION -> ApplinkConstInternalMarketplace.ADMIN_INVITATION
            ApplinkConst.SellerApp.ADMIN_ACCEPTED -> ShopAdminDeepLinkMapper.getInternalAppLinkAdminAccepted(uri)
            ApplinkConst.SellerApp.ADMIN_REDIRECTION -> ApplinkConstInternalMarketplace.ADMIN_REDIRECTION
            ApplinkConst.SellerApp.PRODUCT_MANAGE -> DeepLinkMapperProductManage.getProductListInternalAppLink(deeplink)
            else -> when {
                DeeplinkMapperMerchant.isShopPageFeedDeeplink(deeplink) -> DeeplinkMapperMerchant.getRegisteredNavigationShopFeed(deeplink)
                DeeplinkMapperMerchant.isShopPageSettingSellerApp(deeplink) -> DeeplinkMapperMerchant.getRegisteredNavigationShopPageSettingSellerApp(deeplink)
                DeeplinkMapperMerchant.isCreateShowcaseApplink(deeplink) -> DeeplinkMapperMerchant.getRegisteredNavigationForCreateShowcase(deeplink)
                DeeplinkMapperMerchant.isCreateVoucherProductApplink(deeplink) -> DeeplinkMapperMerchant.getRegisteredNavigationForCreateVoucherProduct(deeplink)
                DeeplinkMapperMerchant.isVoucherProductListApplink(deeplink) -> DeeplinkMapperMerchant.getRegisteredNavigationForVoucherProductList(deeplink)
                DeeplinkMapperMerchant.isVoucherProductDetailApplink(deeplink) -> DeeplinkMapperMerchant.getRegisteredNavigationForVoucherProductDetail(deeplink)
                DeeplinkMapperMerchant.isSellerShopFlashSaleApplink(deeplink) -> DeeplinkMapperMerchant.getRegisteredNavigationForSellerShopFlashSale(deeplink)

                //For Tokomember applinks with params
                trimmedDeeplink.startsWith(ApplinkConst.Tokomember.MAIN_PATH) -> getDynamicDeeplinkForTokomember(trimmedDeeplink)
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
