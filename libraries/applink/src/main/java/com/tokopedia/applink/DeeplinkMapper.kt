package com.tokopedia.applink

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.account.DeeplinkMapperAccount
import com.tokopedia.applink.category.DeeplinkMapperCategory
import com.tokopedia.applink.chatbot.DeeplinkMapperChatbot.getChatbotDeeplink
import com.tokopedia.applink.communication.DeeplinkMapperCommunication
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.content.DeeplinkMapperContent
import com.tokopedia.applink.digital.DeeplinkMapperDigital
import com.tokopedia.applink.digitaldeals.DeeplinkMapperDeals
import com.tokopedia.applink.entertaiment.DeeplinkMapperEntertainment
import com.tokopedia.applink.find.DeepLinkMapperFind
import com.tokopedia.applink.internal.ApplinkConsInternalHome
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.applink.marketplace.DeeplinkMapperMarketplace.getRegisteredNavigationMarketplace
import com.tokopedia.applink.marketplace.DeeplinkMapperMarketplace.getShopPageInternalAppLink
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant
import com.tokopedia.applink.model.Always
import com.tokopedia.applink.model.DLP
import com.tokopedia.applink.model.DLPLogic
import com.tokopedia.applink.order.DeeplinkMapperOrder
import com.tokopedia.applink.powermerchant.PowerMerchantDeepLinkMapper
import com.tokopedia.applink.productmanage.DeepLinkMapperProductManage
import com.tokopedia.applink.promo.DeeplinkMapperPromo.getDynamicDeeplinkForTokomember
import com.tokopedia.applink.promo.DeeplinkMapperPromo.getRegisteredNavigationPromoFromHttp
import com.tokopedia.applink.purchaseplatform.DeeplinkMapperWishlist
import com.tokopedia.applink.recommendation.DeeplinkMapperRecommendation.getRegisteredNavigationRecommendationFromHttp
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
import com.tokopedia.applink.tokonow.DeeplinkMapperTokopediaNow.getRegisteredNavigationTokopediaNowSearch
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
                val tempDeeplink =
                    getRegisteredNavigation(context, getTokopediaSchemeList(), uri, deeplink)
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
        } else {
            uri.query
        }
    }

    fun getRegisteredNavigationProductTalk(productId: String?): String {
        return "${ApplinkConstInternalGlobal.PRODUCT_TALK_BASE}$productId/"
    }

    fun getRegisteredNavigationShopTalk(shopId: String?): String {
        return "${ApplinkConstInternalGlobal.SHOP_TALK_BASE}$shopId/"
    }

    fun getRegisteredNavigationTalk(deeplink: String): String {
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

    fun getRegisteredNavigationTopChat(uri: Uri, deeplink: String): String {
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
        if (pathSize == 1 && (
                uri.pathSegments[0] == TOKOPOINTS ||
                    uri.pathSegments[0] == ApplinkConst.RewardFallback.Reward.REWARDS
                )
        ) {
            return ApplinkConstInternalPromo.TOKOPOINTS_HOME
        }

        val appLinkContent =
            DeeplinkMapperContent.getRegisteredNavigationContentFromHttp(uri, deeplink)
        if (appLinkContent.isNotBlank()) return appLinkContent

        val appLinkFeed =
            DeeplinkMapperContent.getRegisteredNavigationFeedVideoFromHttp(uri, deeplink)
        if (appLinkFeed.isNotBlank()) return appLinkFeed

        val applinkDigital =
            DeeplinkMapperDigital.getRegisteredNavigationFromHttpDigital(context, deeplink)
        if (applinkDigital.isNotEmpty()) {
            return applinkDigital
        }

        val appLinkEvents =
            DeeplinkMapperEntertainment.getRegisteredNavigationFromHttpEvents(deeplink, context)
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
        if (applinkPromo.isNotBlank()) {
            return applinkPromo
        }

        val applinkFlashSaleTokopedia =
            DeeplinkMapperMerchant.getRegisteredNavigationFromHttpForSellerTokopediaFlashSale(
                uri,
                deeplink
            )
        if (applinkFlashSaleTokopedia.isNotBlank()) return applinkFlashSaleTokopedia

        val applinkFlashSaleToko =
            DeeplinkMapperMerchant.getRegisteredNavigationFromHttpForSellerShopFlashSale(
                uri,
                deeplink
            )
        if (applinkFlashSaleToko.isNotBlank()) return applinkFlashSaleToko

        val applinkSellerMvc =
            DeeplinkMapperMerchant.getRegisteredNavigationFromHttpForSellerMvc(uri, deeplink)
        if (applinkSellerMvc.isNotBlank()) return applinkSellerMvc

        val applinkSlashPrice =
            DeeplinkMapperMerchant.getRegisteredNavigationFromHttpForSlashPrice(uri, deeplink)
        if (applinkSlashPrice.isNotBlank()) return applinkSlashPrice

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

    fun getWebviewApplink(deeplink: String): String {
        return if (deeplink.contains("landing-dana-instant")) {
            "tokopedia-android-internal://user/webviewkyc"
        } else {
            ApplinkConstInternalGlobal.WEBVIEW_BASE
        }
    }

    fun getTokopediaSchemeList(): Map<String, MutableList<DLP>> {
        return DeeplinkMainApp.deeplinkPatternTokopediaSchemeListv2
    }

    /**
     * Mapping tokopedia link to registered deplink in manifest if necessary
     * eg: tokopedia://product/add to tokopedia-android-internal://marketplace/product-add-item
     * If not found, return current deeplink, means it registered
     */
    private fun getRegisteredNavigation(
        context: Context,
        sourceMap: Map<String, MutableList<DLP>>,
        uri: Uri,
        deeplink: String
    ): String {
        synchronized(LOCK) {
            val sourceList = getList(sourceMap, uri)
            if (sourceList == null) {
                return ""
            } else {
                sourceList.forEachIndexed { index, it ->
                    val isMatch: Boolean
                    val idList: List<String>?
                    val result = it.logic.logic(context, uri, deeplink)
                    isMatch = result.first
                    idList = result.second
                    if (isMatch) {
                        val target = it.targetDeeplink(context, uri, deeplink, idList)
                        if (target.isNotEmpty()) {
                            putToTop(sourceList, it.logic, index)
                            return target
                        }
                    }
                }
            }
        }
        return ""
    }

    fun getList(sourceMap: Map<String, MutableList<DLP>>, uri: Uri): MutableList<DLP>? {
        return sourceMap[uri.host ?: ""]
    }

    // mechanism to bring most frequent deeplink to top of the list
    private fun putToTop(list: MutableList<DLP>, logic: DLPLogic, index: Int) {
        if (index == 0 || logic is Always) {
            return
        }
        list.add(
            0,
            list.removeAt(index)
        )
    }

    private fun getRegisteredNavigationFromInternalTokopedia(
        context: Context,
        uri: Uri,
        deeplink: String
    ): String {
        return when {
            deeplink.startsWith(ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST) -> DeepLinkMapperProductManage.getProductListInternalAppLink(
                deeplink
            )

            deeplink.startsWith(ApplinkConstInternalMarketplace.TOPCHAT) && shouldRedirectToSellerApp(
                uri
            ) -> AppLinkMapperSellerHome.getTopChatAppLink(uri)

            deeplink.startsWith(ApplinkConstInternalMarketplace.STOCK_REMINDER) -> DeepLinkMapperProductManage.getStockReminderInternalAppLink(
                deeplink
            )

            deeplink.startsWith(ApplinkConstInternalOrder.NEW_ORDER) -> getSomNewOrderAppLink(uri)
            deeplink.startsWith(ApplinkConstInternalOrder.READY_TO_SHIP) -> getSomReadyToShipAppLink(
                uri
            )

            deeplink.startsWith(ApplinkConstInternalOrder.SHIPPED) -> getSomShippedAppLink(uri)
            deeplink.startsWith(ApplinkConstInternalOrder.FINISHED) -> getSomDoneAppLink(uri)
            deeplink.startsWith(ApplinkConstInternalOrder.CANCELLED) -> getSomCancelledAppLink(uri)
            deeplink.startsWith(ApplinkConstInternalOrder.CANCELLATION_REQUEST) -> getSomCancellationRequestAppLink(
                uri
            )

            deeplink.startsWith(ApplinkConstInternalOrder.HISTORY) -> getSomAllOrderAppLink(uri)
            deeplink.startsWith(ApplinkConstInternalMarketplace.SHOP_PAGE_BASE) -> getShopPageInternalAppLink(
                context,
                uri,
                deeplink,
                "",
                uri.pathSegments.getOrNull(1).orEmpty()
            )

            deeplink.startsWith(ApplinkConstInternalGlobal.ADVANCED_SETTING) -> DeeplinkMapperUser.getRegisteredNavigationUser(
                deeplink
            )

            deeplink.startsWith(ApplinkConstInternalGlobal.GENERAL_SETTING) -> DeeplinkMapperUser.getRegisteredNavigationUser(
                deeplink
            )

            deeplink.startsWith(ApplinkConsInternalHome.HOME_WISHLIST) -> DeeplinkMapperWishlist.getRegisteredNavigationWishlist()

            deeplink.startsWith(ApplinkConstInternalMarketplace.ADD_ON_GIFTING) -> getRegisteredNavigationMarketplace(
                context,
                deeplink
            )

            deeplink == ApplinkConstInternalUserPlatform.SETTING_PROFILE -> DeeplinkMapperUser.getRegisteredNavigationUser(
                deeplink
            )

            deeplink.startsWithPattern(ApplinkConstInternalUserPlatform.GOTO_KYC) -> DeeplinkMapperUser.getRegisteredNavigationUser(
                deeplink
            )

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
    private fun getRegisteredNavigationFromSellerapp(
        context: Context,
        uri: Uri,
        deeplink: String
    ): String {
        return when (val trimmedDeeplink = UriUtil.trimDeeplink(uri, deeplink)) {
            ApplinkConst.SellerApp.WEBVIEW -> ApplinkConstInternalGlobal.WEBVIEW_BASE
            ApplinkConst.SellerApp.BROWSER -> ApplinkConstInternalGlobal.BROWSER
            ApplinkConst.SellerApp.TOPADS_DASHBOARD -> ApplinkConstInternalTopAds.TOPADS_DASHBOARD_INTERNAL
            ApplinkConst.SellerApp.SHOP_DISCOUNT -> ApplinkConstInternalSellerapp.SHOP_DISCOUNT
            ApplinkConst.SellerApp.TOPADS_CREDIT_HISTORY -> ApplinkConstInternalTopAds.TOPADS_HISTORY_CREDIT
            ApplinkConst.SellerApp.TOPADS_CREDIT -> ApplinkConstInternalTopAds.TOPADS_BUY_CREDIT
            ApplinkConst.SellerApp.TOPADS_ADD_CREDIT -> ApplinkConstInternalTopAds.TOP_ADS_ADD_CREDIT
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
            ApplinkConst.SellerApp.POWER_MERCHANT_SUBSCRIBE -> PowerMerchantDeepLinkMapper.getPowerMerchantAppLink(
                context,
                uri
            )

            ApplinkConst.SellerApp.PM_BENEFIT_PACKAGE -> ApplinkConstInternalMarketplace.PM_BENEFIT_PACKAGE
            ApplinkConst.SellerApp.VOUCHER_LIST -> ApplinkConstInternalSellerapp.SELLER_MVC_REDIRECTION_PAGE
            ApplinkConst.SellerApp.VOUCHER_ACTIVE -> ApplinkConstInternalSellerapp.SELLER_MVC_LIST_ACTIVE
            ApplinkConst.SellerApp.VOUCHER_HISTORY -> ApplinkConstInternalSellerapp.SELLER_MVC_LIST_HISTORY
            ApplinkConst.SellerApp.CAMPAIGN_LIST -> ApplinkConstInternalSellerapp.CAMPAIGN_LIST
            ApplinkConst.SellerApp.CENTRALIZED_PROMO -> ApplinkConstInternalSellerapp.CENTRALIZED_PROMO
            ApplinkConst.SellerApp.PLAY_BROADCASTER -> ApplinkConstInternalContent.INTERNAL_PLAY_BROADCASTER
            ApplinkConst.SellerApp.SELLER_SHIPPING_EDITOR -> ApplinkConstInternalMarketplace.SHOP_SETTINGS_SHIPPING
            ApplinkConst.SellerApp.REVIEW_REMINDER -> ApplinkConstInternalSellerapp.REVIEW_REMINDER
            ApplinkConst.SellerApp.STATISTIC_DASHBOARD -> DeepLinkMapperStatistic.getStatisticAppLink(
                uri
            )

            ApplinkConst.SellerApp.SHOP_SCORE_DETAIL -> ShopScoreDeepLinkMapper.getInternalAppLinkShopScore(
                uri
            )

            ApplinkConst.SellerApp.TOKOMEMBER -> ApplinkConstInternalSellerapp.TOKOMEMBER
            ApplinkConst.SellerApp.TOKOMEMBER_PROGRAM_LIST -> ApplinkConstInternalSellerapp.TOKOMEMBER_PROGRAM_LIST
            ApplinkConst.SellerApp.TOKOMEMBER_COUPON_LIST -> ApplinkConstInternalSellerapp.TOKOMEMBER_COUPON_LIST
            ApplinkConst.SellerApp.TOKOMEMBER_PROGRAM_CREATION -> ApplinkConstInternalSellerapp.TOKOMEMBER_PROGRAM_CREATION
            ApplinkConst.SellerApp.TOKOMEMBER_COUPON_CREATION -> ApplinkConstInternalSellerapp.TOKOMEMBER_COUPON_CREATION
            ApplinkConst.SellerApp.ADMIN_INVITATION -> ApplinkConstInternalMarketplace.ADMIN_INVITATION
            ApplinkConst.SellerApp.ADMIN_ACCEPTED -> ShopAdminDeepLinkMapper.getInternalAppLinkAdminAccepted(
                uri
            )

            ApplinkConst.SellerApp.ADMIN_REDIRECTION -> ApplinkConstInternalMarketplace.ADMIN_REDIRECTION
            ApplinkConst.SellerApp.PRODUCT_MANAGE -> DeepLinkMapperProductManage.getProductListInternalAppLink(
                deeplink
            )

            ApplinkConst.SellerApp.TOPCHAT_BUBBLE_ACTIVATION -> DeeplinkMapperCommunication.getRegisteredNavigationBubbleActivation(
                deeplink
            )

            ApplinkConst.SellerApp.SELLER_MVC_LIST -> ApplinkConstInternalSellerapp.SELLER_MVC_LIST
            ApplinkConst.SellerApp.SELLER_MVC_LIST_ACTIVE -> ApplinkConstInternalSellerapp.SELLER_MVC_LIST_ACTIVE
            ApplinkConst.SellerApp.SELLER_MVC_LIST_HISTORY -> ApplinkConstInternalSellerapp.SELLER_MVC_LIST_HISTORY
            ApplinkConst.SellerApp.SELLER_MVC_LIST_UPCOMING -> ApplinkConstInternalSellerapp.SELLER_MVC_LIST_UPCOMING
            ApplinkConst.SellerApp.SELLER_MVC_LIST_ONGOING -> ApplinkConstInternalSellerapp.SELLER_MVC_LIST_ONGOING
            ApplinkConst.SellerApp.SELLER_MVC_REDIRECTION_PAGE -> ApplinkConstInternalSellerapp.SELLER_MVC_REDIRECTION_PAGE
            ApplinkConst.SellerApp.SELLER_PERSONA -> ApplinkConstInternalSellerapp.SELLER_PERSONA
            ApplinkConst.SellerApp.SELLER_SHOP_HOUR -> ApplinkConstInternalMarketplace.SHOP_SETTINGS_OPERATIONAL_HOURS
            else -> when {
                DeepLinkMapperProductManage.isStockReminderPattern(deeplink) -> DeepLinkMapperProductManage.getStockReminderInternalAppLink(
                    deeplink
                )

                DeeplinkMapperMerchant.isShopPageFeedDeeplink(deeplink) -> DeeplinkMapperMerchant.getRegisteredNavigationShopFeed(
                    deeplink
                )

                DeeplinkMapperMerchant.isShopPageSettingSellerApp(deeplink) -> DeeplinkMapperMerchant.getRegisteredNavigationShopPageSettingSellerApp(
                    deeplink
                )

                DeeplinkMapperMerchant.isCreateShowcaseApplink(deeplink) -> DeeplinkMapperMerchant.getRegisteredNavigationForCreateShowcase(
                    deeplink
                )

                DeeplinkMapperMerchant.isCreateVoucherProductApplink(deeplink) -> DeeplinkMapperMerchant.getRegisteredNavigationForCreateVoucherProduct()
                DeeplinkMapperMerchant.isCreateShopVoucherApplink(deeplink) -> DeeplinkMapperMerchant.getRegisteredNavigationForCreateShopVoucher()
                DeeplinkMapperMerchant.isVoucherProductListApplink(deeplink) -> DeeplinkMapperMerchant.getRegisteredNavigationForVoucherProductList(
                    deeplink
                )

                DeeplinkMapperMerchant.isShopVoucherDetailApplink(deeplink) -> DeeplinkMapperMerchant.getRegisteredNavigationForShopVoucherDetail(
                    deeplink
                )

                DeeplinkMapperMerchant.isVoucherProductDetailApplink(deeplink) -> DeeplinkMapperMerchant.getRegisteredNavigationForVoucherProductDetail(
                    deeplink
                )

                DeeplinkMapperMerchant.isSellerShopFlashSaleApplink(deeplink) -> DeeplinkMapperMerchant.getRegisteredNavigationForSellerShopFlashSale(
                    deeplink
                )

                DeeplinkMapperMerchant.isSellerTokopediaFlashSaleCampaignDetailApplink(deeplink) -> DeeplinkMapperMerchant.getRegisteredNavigationForSellerTokopediaFlashSaleCampaignDetail(
                    deeplink
                )

                DeeplinkMapperMerchant.isSellerTokopediaUpcomingFlashSaleApplink(deeplink) -> DeeplinkMapperMerchant.getRegisteredNavigationForSellerTokopediaFlashSaleUpcoming()
                DeeplinkMapperMerchant.isSellerTokopediaRegisteredFlashSaleApplink(deeplink) -> DeeplinkMapperMerchant.getRegisteredNavigationForSellerTokopediaFlashSaleRegistered()
                DeeplinkMapperMerchant.isSellerTokopediaOngoingFlashSaleApplink(deeplink) -> DeeplinkMapperMerchant.getRegisteredNavigationForSellerTokopediaFlashSaleOngoing()
                DeeplinkMapperMerchant.isSellerTokopediaFinishedFlashSaleApplink(deeplink) -> DeeplinkMapperMerchant.getRegisteredNavigationForSellerTokopediaFlashSaleFinished()
                DeeplinkMapperMerchant.isSellerTokopediaFlashSaleApplink(deeplink) -> DeeplinkMapperMerchant.getRegisteredNavigationForSellerTokopediaFlashSale()
                DeeplinkMapperMerchant.isSellerMvcIntroAppLink(deeplink) -> DeeplinkMapperMerchant.getRegisteredNavigationForSellerMvcIntro()
                DeeplinkMapperMerchant.isSellerMvcCreate(deeplink) -> DeeplinkMapperMerchant.getRegisteredNavigationForSellerMvcCreate(
                    deeplink
                )

                DeeplinkMapperMerchant.isSellerMvcDetailAppLink(deeplink) -> DeeplinkMapperMerchant.getRegisteredNavigationForSellerMvcDetail(
                    deeplink
                )

                DeeplinkMapperMerchant.isSellerShopNibAppLink(deeplink) -> DeeplinkMapperMerchant.getRegisteredNavigationForSellerShopNib()
                // For Tokomember applinks with params
                trimmedDeeplink.startsWith(ApplinkConst.Tokomember.MAIN_PATH) -> getDynamicDeeplinkForTokomember(
                    trimmedDeeplink
                )

                else -> ""
            }
        }
    }
}
