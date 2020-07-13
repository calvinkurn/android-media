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
import com.tokopedia.applink.content.DeeplinkMapperContent
import com.tokopedia.applink.content.DeeplinkMapperContent.getKolDeepLink
import com.tokopedia.applink.content.DeeplinkMapperContent.getRegisteredNavigationContent
import com.tokopedia.applink.content.DeeplinkMapperContent.getRegisteredNavigationInterestPick
import com.tokopedia.applink.content.DeeplinkMapperContent.getRegisteredNavigationPlay
import com.tokopedia.applink.digital.DeeplinkMapperDigital
import com.tokopedia.applink.digital.DeeplinkMapperDigital.getRegisteredNavigationDigital
import com.tokopedia.applink.entertaiment.DeeplinkMapperEntertainment.getRegisteredNavigationEvents
import com.tokopedia.applink.feed.DeepLinkMapperFeed.getRegisteredFeed
import com.tokopedia.applink.find.DeepLinkMapperFind.getRegisteredFind
import com.tokopedia.applink.fintech.DeeplinkMapperFintech.getRegisteredNavigationForFintech
import com.tokopedia.applink.fintech.DeeplinkMapperFintech.getRegisteredNavigationForLayanan
import com.tokopedia.applink.gamification.DeeplinkMapperGamification
import com.tokopedia.applink.home.DeeplinkMapperHome.getRegisteredNavigationHome
import com.tokopedia.applink.home.DeeplinkMapperHome.getRegisteredNavigationHomeContentExplore
import com.tokopedia.applink.home.DeeplinkMapperHome.getRegisteredNavigationHomeFeed
import com.tokopedia.applink.home.DeeplinkMapperHome.getRegisteredNavigationHomeOfficialStore
import com.tokopedia.applink.internal.*
import com.tokopedia.applink.internal.ApplinkConstInternalCategory.getDiscoveryDeeplink
import com.tokopedia.applink.marketplace.DeeplinkMapperLogistic
import com.tokopedia.applink.marketplace.DeeplinkMapperMarketplace.getRegisteredNavigationMarketplace
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant.getRegisteredNavigationProductReview
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant.getRegisteredNavigationReputation
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant.getRegisteredNavigationShopReview
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant.isShopReview
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerAwbChange
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerAwbInvalid
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerComplaint
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerDelivered
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerHistory
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerRetur
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerWaitingAwb
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerWaitingPickup
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
                return createAppendDeeplinkWithQuery(tempDeeplink, query)
            }
            DeeplinkConstant.SCHEME_SELLERAPP -> {
                val query = uri.query
                val tempDeeplink = getRegisteredNavigationFromSellerapp(context, uri, deeplink)
                return createAppendDeeplinkWithQuery(tempDeeplink, query)
            }
            DeeplinkConstant.SCHEME_INTERNAL -> {
                getRegisteredNavigationFromInternalTokopedia(context, uri, deeplink)
            }
            else -> deeplink
        }
        return mappedDeepLink
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
        if (uri.pathSegments.joinToString("/") == TOKOPOINTS) {
            return ApplinkConstInternalPromo.TOKOPOINTS_HOME
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
            DLP.startWith(ApplinkConst.INBOX, ApplinkConsInternalHome.HOME_INBOX),
            DLP.startWith(ApplinkConst.QRSCAN, ApplinkConstInternalMarketplace.QR_SCANNEER),
            DLP.startWith(ApplinkConst.SALAM_UMRAH_SHOP) { ctx, _, deeplink -> getRegisteredNavigationSalamUmrahShop(deeplink, ctx) },
            DLP(logic = { _, _, deeplink -> deeplink.startsWith(ApplinkConst.TOP_CHAT, true) && isChatBotTrue(deeplink) },
                    targetDeeplink = { _, _, deeplink -> getChatbotDeeplink(deeplink) }),
            DLP(logic = { _, _, deeplink -> deeplink.startsWith(ApplinkConst.TOP_CHAT, true) && AppLinkMapperSellerHome.shouldRedirectToSellerApp(deeplink) },
                    targetDeeplink = { _, _, deeplink -> AppLinkMapperSellerHome.getTopChatAppLink(deeplink) }),
            DLP.startWith(ApplinkConst.HOTEL) { _, _, deeplink -> deeplink },
            DLP.startWith(ApplinkConst.DIGITAL) { ctx, _, deeplink -> getRegisteredNavigationDigital(ctx, deeplink) },
            DLP.startWith(ApplinkConst.DISCOVERY_SEARCH) { _, _, deeplink -> getRegisteredNavigationSearch(deeplink) },
            DLP.startWith(ApplinkConst.CART) { _, _, deeplink -> getRegisteredNavigationMarketplace(deeplink) },
            DLP.startWith(ApplinkConst.CHECKOUT) { _, _, deeplink -> getRegisteredNavigationMarketplace(deeplink) },
            DLP.startWith(ApplinkConst.OCC) { _, _, deeplink -> getRegisteredNavigationMarketplace(deeplink) },
            DLP.startWithPattern(ApplinkConst.DEALS_HOME) { _, _, deeplink -> getRegisteredNavigationDeals(deeplink) },
            DLP.startWithPattern(ApplinkConst.FIND) { _, _, deeplink -> getRegisteredFind(deeplink) },
            DLP.startWith(ApplinkConst.AMP_FIND) { _, _, deeplink -> getRegisteredFind(deeplink) },
            DLP.startWithPattern(ApplinkConst.Digital.DIGITAL_BROWSE) { _, _, deeplink -> getRegisteredNavigationExploreCategory(deeplink) },
            DLP.startWithPattern(ApplinkConst.CATEGORY) { _, _, deeplink -> getRegisteredCategoryNavigation(deeplink) },
            DLP.startWithPattern(ApplinkConst.PROFILE) { _, _, deeplink -> getRegisteredNavigationContent(deeplink) },
            DLP.startWithPattern(ApplinkConst.PLAY_DETAIL) { _, _, deeplink -> getRegisteredNavigationPlay(deeplink) },
            DLP.startWithPattern(ApplinkConst.HOME_HOTLIST) { _, _, deeplink -> getRegisteredHotlist(deeplink) },
            DLP.startWithPattern(ApplinkConst.PRODUCT_EDIT) { _, _, deeplink -> DeepLinkMapperProductManage.getEditProductInternalAppLink(deeplink) },
            DLP.startWith(ApplinkConst.PRODUCT_MANAGE) { _, _, deeplink -> DeepLinkMapperProductManage.getProductListInternalAppLink(deeplink) },
            DLP.startWith(ApplinkConst.PRODUCT_CREATE_REVIEW) { _, _, deeplink -> getRegisteredNavigationProductReview(deeplink) },
            DLP.startWith(ApplinkConst.REPUTATION) { _, _, deeplink -> getRegisteredNavigationReputation(deeplink) },
            DLP.startWith(ApplinkConst.TOKOPOINTS) { ctx, _, deeplink -> getRegisteredNavigationTokopoints(ctx, deeplink) },
            DLP.startWith(ApplinkConst.DEFAULT_RECOMMENDATION_PAGE) { _, _, deeplink -> getRegisteredNavigationRecommendation(deeplink) },
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
            DLP.startWith(ApplinkConst.SELLER_NEW_ORDER) { _, _, deeplink -> getSomNewOrderAppLink(deeplink) },
            DLP.startWith(ApplinkConst.SELLER_PURCHASE_READY_TO_SHIP) { _, _, deeplink -> getSomReadyToShipAppLink(deeplink) },
            DLP.startWith(ApplinkConst.SELLER_PURCHASE_DELIVERED) { _, _, deeplink -> getRegisteredNavigationMainAppSellerDelivered() },
            DLP.startWith(ApplinkConst.SELLER_PURCHASE_WAITING_PICKUP) { _, _, deeplink -> getRegisteredNavigationMainAppSellerWaitingPickup() },
            DLP.startWith(ApplinkConst.SELLER_PURCHASE_WAITING_AWB) { _, _, deeplink -> getRegisteredNavigationMainAppSellerWaitingAwb() },
            DLP.startWith(ApplinkConst.SELLER_PURCHASE_AWB_INVALID) { _, _, deeplink -> getRegisteredNavigationMainAppSellerAwbInvalid() },
            DLP.startWith(ApplinkConst.SELLER_PURCHASE_AWB_CHANGE) { _, _, deeplink -> getRegisteredNavigationMainAppSellerAwbChange() },
            DLP.startWith(ApplinkConst.SELLER_PURCHASE_RETUR) { _, _, deeplink -> getRegisteredNavigationMainAppSellerRetur() },
            DLP.startWith(ApplinkConst.SELLER_PURCHASE_COMPLAINT) { _, _, deeplink -> getRegisteredNavigationMainAppSellerComplaint() },
            DLP.startWith(ApplinkConst.SELLER_HISTORY) { _, _, deeplink -> getRegisteredNavigationMainAppSellerHistory() },
            DLP.startWith(ApplinkConst.SELLER_PURCHASE_SHIPPED) { _, _, deeplink -> getSomShippedAppLink(deeplink) },
            DLP.startWith(ApplinkConst.SELLER_SHIPMENT) { _, _, deeplink -> getSomReadyToShipAppLink(deeplink) },
            DLP.startWith(ApplinkConst.SELLER_PURCHASE_FINISHED) { _, _, deeplink -> getSomDoneAppLink(deeplink) },
            DLP.startWith(ApplinkConst.SELLER_PURCHASE_CANCELED) { _, _, deeplink -> getSomCancelledAppLink(deeplink) },
            DLP.startWith(ApplinkConst.SELLER_STATUS) { _, _, deeplink -> getSomShippedAppLink(deeplink) },
            DLP.startWithPattern(ApplinkConst.FEED_DETAILS) { _, _, deeplink -> getRegisteredFeed(deeplink) },
            DLP.startWithPattern(ApplinkConst.INTEREST_PICK) { _, _, deeplink -> getRegisteredNavigationInterestPick(deeplink) },
            DLP(logic = { _, uri, _ -> uri.host == Uri.parse(ApplinkConst.FEED).host && uri.pathSegments.isEmpty() },
                    targetDeeplink = { _, _, _ -> getRegisteredNavigationHomeFeed() }),
            DLP.startWithPattern(ApplinkConst.CONTENT_EXPLORE) { _, _, deeplink -> getRegisteredNavigationHomeContentExplore(deeplink) },
            DLP(logic = { _, uri, _ -> DeeplinkMapperMerchant.isShopPageDeeplink(uri) },
                    targetDeeplink = { _, uri, _ -> DeeplinkMapperMerchant.getShopPageInternalApplink(uri) }),
            DLP(logic = { _, uri, _ -> DeeplinkMapperMerchant.isShopPageHomeDeeplink(uri) },
                    targetDeeplink = { _, uri, _ -> DeeplinkMapperMerchant.getShopPageHomeInternalApplink(uri) }),
            DLP(logic = { _, uri, _ -> DeeplinkMapperMerchant.isShopPageInfoDeeplink(uri) },
                    targetDeeplink = { _, uri, _ -> DeeplinkMapperMerchant.getShopPageInfoInternalApplink(uri) }),
            DLP(logic = { _, uri, _ -> DeeplinkMapperMerchant.isShopPageNoteDeeplink(uri) },
                    targetDeeplink = { _, uri, _ -> DeeplinkMapperMerchant.getShopPageInfoInternalApplink(uri) }),
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
            DLP.startWith(ApplinkConst.DISCOVERY) { _, _, deeplink -> getDiscoveryDeeplink(deeplink) },
            DLP(logic = { _, uri, _ -> specialNavigationMapper(uri, ApplinkConst.HOST_CATEGORY_P) },
                    targetDeeplink = { _, _, deeplink -> getRegisteredCategoryNavigation(deeplink) }),
            DLP(logic = { _, uri, _ -> specialNavigationMapper(uri, ApplinkConst.Play.HOST) },
                    targetDeeplink = { _, uri, _ -> UriUtil.buildUri(ApplinkConstInternalPlay.GROUPCHAT_DETAIL, uri.pathSegments.first()) }),
            DLP(logic = { _, uri, _ -> specialNavigationMapper(uri, ApplinkConst.Notification.BUYER_HOST) },
                    targetDeeplink = { _, uri, _ ->
                        UriUtil.buildUri(ApplinkConstInternalMarketplace.NOTIFICATION_BUYER_INFO_WITH_ID,
                                uri.pathSegments.first())
                    }),
            DLP.exact(ApplinkConst.POWER_MERCHANT_SUBSCRIBE, ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE),
            DLP.exact(ApplinkConst.SETTING_PROFILE, ApplinkConstInternalGlobal.SETTING_PROFILE),
            DLP.exact(ApplinkConst.ADD_CREDIT_CARD, ApplinkConstInternalPayment.PAYMENT_ADD_CREDIT_CARD),
            DLP.exact(ApplinkConst.SETTING_NOTIFICATION, ApplinkConstInternalMarketplace.USER_NOTIFICATION_SETTING),
            DLP.exact(ApplinkConst.GROUPCHAT_LIST, ApplinkConstInternalPlay.GROUPCHAT_LIST),
            DLP.exact(ApplinkConst.KYC, ApplinkConstInternalGlobal.USER_IDENTIFICATION_INFO),
            DLP.exact(ApplinkConst.ADD_NAME_PROFILE, ApplinkConstInternalGlobal.MANAGE_NAME),
            DLP.exact(ApplinkConst.KYC_NO_PARAM, ApplinkConstInternalGlobal.USER_IDENTIFICATION_INFO_BASE),
            DLP.exact(ApplinkConst.KYC_FORM_NO_PARAM, ApplinkConstInternalGlobal.USER_IDENTIFICATION_FORM_BASE),
            DLP.exact(ApplinkConst.SETTING_BANK, ApplinkConstInternalGlobal.SETTING_BANK),
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
            DLP.exact(ApplinkConst.INSTANT_LOAN, ApplinkConstInternalGlobal.GLOBAL_INTERNAL_INSTANT_LOAN),
            DLP.exact(ApplinkConst.INSTANT_LOAN_TAB, ApplinkConstInternalGlobal.GLOBAL_INTERNAL_INSTANT_LOAN_TAB),
            DLP.exact(ApplinkConst.PINJAMAN_ONLINE_TAB, ApplinkConstInternalGlobal.GLOBAL_INTERNAL_PINJAMAN_ONLINE_TAB),
            DLP.exact(ApplinkConst.RECENT_VIEW, ApplinkConsInternalHome.HOME_WISHLIST),
            DLP.exact(ApplinkConst.WISHLIST, ApplinkConsInternalHome.HOME_WISHLIST),
            DLP.exact(ApplinkConst.NEW_WISHLIST, ApplinkConsInternalHome.HOME_WISHLIST),
            DLP.exact(ApplinkConst.CREATE_SHOP, ApplinkConstInternalGlobal.LANDING_SHOP_CREATION),
            DLP.exact(ApplinkConst.CHAT_TEMPLATE, ApplinkConstInternalMarketplace.CHAT_SETTING_TEMPLATE),
            DLP.exact(ApplinkConst.PRODUCT_MANAGE) { _, _, deeplink -> DeepLinkMapperProductManage.getProductListInternalAppLink(deeplink) },
            DLP.exact(ApplinkConst.NOTIFICATION, ApplinkConstInternalMarketplace.NOTIFICATION_CENTER),
            DLP.exact(ApplinkConst.BUYER_INFO, ApplinkConstInternalMarketplace.NOTIFICATION_BUYER_INFO),
            DLP.exact(ApplinkConst.CHANGE_PASSWORD, ApplinkConstInternalGlobal.CHANGE_PASSWORD),
            DLP.exact(ApplinkConst.HAS_PASSWORD, ApplinkConstInternalGlobal.HAS_PASSWORD),
            DLP.exact(ApplinkConst.THANK_YOU_PAGE_NATIVE, ApplinkConstInternalPayment.PAYMENT_THANK_YOU_PAGE),
            DLP.exact(ApplinkConst.PROFILE_COMPLETION, ApplinkConstInternalGlobal.PROFILE_COMPLETION)
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
            deeplink.startsWith(ApplinkConstInternalGlobal.TOPCHAT) && AppLinkMapperSellerHome.shouldRedirectToSellerApp(deeplink) -> AppLinkMapperSellerHome.getTopChatAppLink(deeplink)
            deeplink.startsWith(ApplinkConstInternalOrder.NEW_ORDER) -> getSomNewOrderAppLink(deeplink)
            deeplink.startsWith(ApplinkConstInternalOrder.READY_TO_SHIP) -> getSomReadyToShipAppLink(deeplink)
            deeplink.startsWith(ApplinkConstInternalOrder.SHIPPED) -> getSomShippedAppLink(deeplink)
            deeplink.startsWith(ApplinkConstInternalOrder.FINISHED) -> getSomDoneAppLink(deeplink)
            deeplink.startsWith(ApplinkConstInternalOrder.CANCELLED) -> getSomCancelledAppLink(deeplink)
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
    private fun getRegisteredNavigationFromSellerapp(context: Context, uri: Uri, deeplink: String): String {
        val trimDeeplink = trimDeeplink(uri, deeplink)
        if (trimDeeplink == ApplinkConst.SellerApp.TOPADS_DASHBOARD) {
            if (AppUtil.isSellerInstalled(context)) {
                return ApplinkConst.SellerApp.TOPADS_DASHBOARD
            } else
                ApplinkConstInternalTopAds.TOPADS_DASHBOARD_INTERNAL
        }
        return when (trimDeeplink) {
            ApplinkConst.SellerApp.SELLER_APP_HOME -> ApplinkConstInternalSellerapp.SELLER_HOME
            ApplinkConst.SellerApp.PRODUCT_ADD -> ApplinkConstInternalMechant.MERCHANT_OPEN_PRODUCT_PREVIEW
            ApplinkConst.SellerApp.POWER_MERCHANT_SUBSCRIBE -> ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE
            ApplinkConst.SETTING_PROFILE -> ApplinkConstInternalGlobal.SETTING_PROFILE
            ApplinkConst.ADD_CREDIT_CARD -> ApplinkConstInternalPayment.PAYMENT_ADD_CREDIT_CARD
            ApplinkConst.SETTING_BANK -> ApplinkConstInternalGlobal.SETTING_BANK
            ApplinkConst.CREATE_SHOP -> ApplinkConstInternalMarketplace.OPEN_SHOP
            ApplinkConst.CHANGE_PASSWORD -> ApplinkConstInternalGlobal.CHANGE_PASSWORD
            ApplinkConst.SELLER_NEW_ORDER -> getSomNewOrderAppLink(trimDeeplink)
            ApplinkConst.SELLER_SHIPMENT -> getSomReadyToShipAppLink(trimDeeplink)
            ApplinkConst.TOP_CHAT -> AppLinkMapperSellerHome.getTopChatAppLink(deeplink)
            else -> ""
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
