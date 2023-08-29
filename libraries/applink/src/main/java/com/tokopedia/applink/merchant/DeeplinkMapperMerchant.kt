package com.tokopedia.applink.merchant

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.inbox.DeeplinkMapperInbox
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.startsWithPattern
import com.tokopedia.config.GlobalConfig

/**
 * Created by Rafli Syam on 2020-02-04.
 */

object DeeplinkMapperMerchant {

    const val PARAM_CREATE_SHOWCASE = "is_create_showcase"
    const val PARAM_UTM_SOURCE = "utm_source"
    private const val PARAM_RATING = "rating"
    private const val PARAM_SOURCE = "source"
    private const val PRODUCT_SEGMENT = "product"
    private const val FEED_SEGMENT = "feed"
    private const val PARAM_BUNDLE_ID = "bundleId"
    private const val PARAM_SELECTED_PRODUCT_IDS = "selectedProductIds"
    private const val PARAM_CART_IDS = "cartIds"
    private const val CREATE_SHOWCASE_SEGMENT = "showcase-create"
    private const val FOLLOWER_LIST_SEGMENT = "follower"
    private const val SHOP_PAGE_SETTING_SEGMENT = "settings"
    private const val SHOP_PAGE_SEGMENT_SIZE = 1
    private const val SHOP_FEED_SEGMENT_SIZE = 2
    private const val SHOP_FOLLOWER_LIST_SEGMENT_SIZE = 2
    private const val SHOP_PAGE_SETTING_WITH_SHOP_ID_SEGMENT_SIZE = 2
    private const val PARAM_SELLER_TAB = "tab"
    private const val SELLER_CENTER_URL = "https://seller.tokopedia.com/edu/"
    private const val SHOP_PAGE_RESULT_ETALASE_PATH_SEGMENT_SIZE = 3
    private const val SHOP_PAGE_RESULT_ETALASE_INTERNAL_PATH_SEGMENT_SIZE = 3
    private const val PARAM_SELLER_CAMPAIGN_ID = "campaign_id"
    private const val FLASH_SALE_TOKOPEDIA_LIST_SEGMENT_SIZE = 1
    private const val FLASH_SALE_TOKOPEDIA_DETAIL_SEGMENT_SIZE = 2
    private const val CAMPAIGN_DETAIL_SEGMENT = "campaign-detail"

    private const val URL_SEGMENT_FLASH_SALE_TOKOPEDIA = "manage-campaign/flash-sale"
    private const val URL_SEGMENT_FLASH_SALE_TOKO = "flash-sale-toko"
    private const val URL_SEGMENT_SELLER_MVC = "coupon"
    private const val URL_SEGMENT_SLASH_PRICE = "pengaturan-diskon"

    private const val PARAM_URL = "url"

    fun getRegisteredNavigationReputation(deeplink: String): String {
        if (deeplink.startsWith(ApplinkConst.REPUTATION)) {
            val parsedUri = Uri.parse(deeplink)
            val segments = parsedUri.pathSegments

            if (GlobalConfig.isSellerApp()) {
                if (parsedUri.getQueryParameter(PARAM_SELLER_TAB)?.isNotBlank() == true) {
                    return Uri.parse(ApplinkConstInternalMarketplace.INBOX_REPUTATION)
                        .buildUpon()
                        .appendQueryParameter(
                            PARAM_SELLER_TAB,
                            parsedUri.getQueryParameter(PARAM_SELLER_TAB)
                        )
                        .build().toString()
                }
            }

            if (segments.size > 1) {
                val feedbackId = segments.last()
                return UriUtil.buildUri(ApplinkConstInternalMarketplace.REVIEW_DETAIL, feedbackId)
            }
            if (segments.size > 0) {
                val feedbackId = segments.last()
                return UriUtil.buildUri(
                    ApplinkConstInternalMarketplace.INBOX_REPUTATION_DETAIL,
                    feedbackId
                )
            }
            return ApplinkConstInternalMarketplace.INBOX_REPUTATION
        }
        return deeplink
    }

    fun isShopPage(deeplink: String): Boolean {
        val uri = Uri.parse(deeplink)
        return deeplink.startsWithPattern(ApplinkConst.SHOP) && uri.pathSegments.size == SHOP_PAGE_SEGMENT_SIZE
    }

    fun getRegisteredNavigationShopPage(deeplink: String): String {
        if (deeplink.startsWithPattern(ApplinkConst.SHOP)) {
            val segments = Uri.parse(deeplink).pathSegments
            if (segments.size > 0) {
                val shopId = segments[0]
                return UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_PAGE, shopId)
            }
        }
        return deeplink
    }

    fun getRegisteredNavigationProductReview(uri: Uri): String {
        val segments = uri.pathSegments
        val rating = uri.getQueryParameter(PARAM_RATING) ?: "5"
        val utmSource = uri.getQueryParameter(PARAM_UTM_SOURCE) ?: uri.getQueryParameter(PARAM_SOURCE) ?: ""

        val reputationId = segments[segments.size - 2]
        val productId = segments.last()
        val newUri =
            UriUtil.buildUri(ApplinkConstInternalMarketplace.CREATE_REVIEW, reputationId, productId)
        return Uri.parse(newUri)
            .buildUpon()
            .appendQueryParameter(PARAM_RATING, rating)
            .appendQueryParameter(PARAM_UTM_SOURCE, utmSource)
            .build()
            .toString()
    }

    fun getRegisteredNavigationProductDetailReview(uri: Uri): String {
        val segments = uri.pathSegments
        val productId = segments.first()
        return Uri.parse(
            UriUtil.buildUri(
                ApplinkConstInternalMarketplace.PRODUCT_REVIEW,
                productId
            )
        ).buildUpon().build().toString()
    }

    fun getRegisteredNavigationProductDetailReviewGallery(uri: Uri): String {
        val segment = uri.pathSegments
        val productId = segment.first()
        return Uri.parse(
            UriUtil.buildUri(
                ApplinkConstInternalMarketplace.IMAGE_REVIEW_GALLERY,
                productId
            )
        ).buildUpon().build().toString()
    }

    fun getRegisteredProductEducational(uri: Uri): String {
        val segments = uri.pathSegments
        val type = segments.last()
        return Uri.parse(
            UriUtil.buildUri(
                ApplinkConstInternalMarketplace.PRODUCT_DETAIL_EDUCATIONAL,
                type
            )
        ).buildUpon().build().toString()
    }

    fun isShopPageDeeplink(uri: Uri?): Boolean {
        return uri?.let {
            val pathSegment = it.pathSegments ?: return false
            val prefixShopPageApplink = "tokopedia://shop/"
            it.toString().startsWith(prefixShopPageApplink).and(pathSegment.size == 1)
        } ?: false
    }

    fun getShopPageInternalApplink(shopId: String?): String {
        return UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_PAGE, shopId)
    }

    fun isShopPageHomeDeeplink(uri: Uri?): Boolean {
        return uri?.let {
            val pathSegment = uri.pathSegments ?: return false
            val prefixShopPageHomeAppLink = "tokopedia://shop/"
            val pathHome = "home"
            return if (pathSegment.size == 2) {
                it.toString().startsWith(prefixShopPageHomeAppLink).and(pathSegment[1] == pathHome)
            } else {
                false
            }
        } ?: false
    }

    fun getShopPageHomeInternalApplink(uri: Uri?): String {
        return uri?.let {
            val pathSegment = uri.pathSegments ?: return it.toString()
            return if (pathSegment.size == 2) {
                val shopId = pathSegment[0]
                UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_PAGE_HOME, shopId)
            } else {
                it.toString()
            }
        } ?: ""
    }

    fun isShopPageInfoDeeplink(uri: Uri?): Boolean {
        return uri?.let {
            val pathSegment = uri.pathSegments ?: return false
            val prefixShopPageHomeAppLink = "tokopedia://shop/"
            val pathInfo = "info"
            return if (pathSegment.size == 2) {
                it.toString().startsWith(prefixShopPageHomeAppLink) and (pathSegment[1] == pathInfo)
            } else {
                false
            }
        } ?: false
    }

    fun getShopPageInfoInternalApplink(uri: Uri?): String {
        return uri?.let {
            val pathSegment = uri.pathSegments ?: return it.toString()
            return if (pathSegment.size == 2) {
                val shopId = pathSegment[0]
                UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_PAGE_INFO, shopId)
            } else {
                it.toString()
            }
        } ?: ""
    }

    fun isShopPageResultEtalaseDeepLink(uri: Uri?): Boolean {
        return uri?.let {
            val pathSegment = uri.pathSegments ?: return false
            val prefixShopPageHomeAppLink = "tokopedia://shop/"
            val pathEtalase = "etalase"
            return if (pathSegment.size == SHOP_PAGE_RESULT_ETALASE_PATH_SEGMENT_SIZE) {
                it.toString()
                    .startsWith(prefixShopPageHomeAppLink) and (pathSegment[1] == pathEtalase)
            } else {
                false
            }
        } ?: false
    }

    fun isShopPageNoteDeeplink(uri: Uri?): Boolean {
        return uri?.let {
            val pathSegment = uri.pathSegments ?: return false
            val prefixShopPageHomeAppLink = "tokopedia://shop/"
            val pathNote = "note"
            return if (pathSegment.size == 2) {
                it.toString().startsWith(prefixShopPageHomeAppLink) and (pathSegment[1] == pathNote)
            } else {
                false
            }
        } ?: false
    }

    fun getShopPageNoteInternalApplink(uri: Uri?): String {
        return uri?.let {
            val pathSegment = uri.pathSegments ?: return it.toString()
            return if (pathSegment.size == 2) {
                val shopId = pathSegment[0]
                UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_PAGE_NOTE, shopId)
            } else {
                it.toString()
            }
        } ?: ""
    }

    fun getShopPageResultEtalaseInternalAppLink(uri: Uri?): String {
        return uri?.let {
            val pathSegment = uri.pathSegments ?: return it.toString()
            return if (pathSegment.size == SHOP_PAGE_RESULT_ETALASE_INTERNAL_PATH_SEGMENT_SIZE) {
                val shopId = pathSegment[0]
                val etalaseId = pathSegment[2]
                UriUtil.buildUri(
                    ApplinkConstInternalMarketplace.SHOP_PAGE_PRODUCT_LIST,
                    shopId,
                    etalaseId
                )
            } else {
                it.toString()
            }
        } ?: ""
    }

    fun getSellerInfoDetailApplink(uri: Uri?): String {
        return uri?.let {
            val url = uri.getQueryParameter(PARAM_URL)
            return if (url.isNullOrEmpty()) {
                if (GlobalConfig.isSellerApp()) {
                    ApplinkConst.SELLER_INFO
                } else {
                    DeeplinkMapperInbox.getRegisteredNavigationNotifcenter()
                }
            } else {
                UriUtil.buildUri(ApplinkConstInternalGlobal.WEBVIEW, url)
            }
        } ?: ""
    }

    fun isShopPageProductDeeplink(deeplink: String): Boolean {
        val uri = Uri.parse(deeplink)
        return deeplink.startsWithPattern(ApplinkConst.SHOP_PRODUCT) && uri.lastPathSegment == PRODUCT_SEGMENT
    }

    fun getRegisteredNavigationShopProduct(shopId: String?): String {
        return UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_PAGE_PRODUCT, shopId)
    }

    fun isShopPageFeedDeeplink(deeplink: String): Boolean {
        val uri = Uri.parse(deeplink)
        return (
            deeplink.startsWithPattern(ApplinkConst.SHOP_FEED) ||
                deeplink.startsWithPattern(ApplinkConst.SellerApp.SHOP_FEED)
            ) &&
            uri.lastPathSegment == FEED_SEGMENT
    }

    fun getRegisteredNavigationShopFeed(deeplink: String): String {
        if (deeplink.startsWithPattern(ApplinkConst.SHOP_FEED) || deeplink.startsWithPattern(
                ApplinkConst.SellerApp.SHOP_FEED
            )
        ) {
            val segments = Uri.parse(deeplink).pathSegments
            val shopId = segments[0]
            return if (segments.size == SHOP_FEED_SEGMENT_SIZE) {
                UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_PAGE_FEED, shopId)
            } else {
                deeplink
            }
        }
        return deeplink
    }

    fun isShopFollowerListDeeplink(deeplink: String): Boolean {
        val uri = Uri.parse(deeplink)
        return deeplink.startsWithPattern(ApplinkConst.SHOP_FOLLOWER_LIST) && uri.lastPathSegment == FOLLOWER_LIST_SEGMENT
    }

    fun getRegisteredNavigationShopFollowerList(deeplink: String): String {
        if (deeplink.startsWithPattern(ApplinkConst.SHOP_FOLLOWER_LIST)) {
            val segments = Uri.parse(deeplink).pathSegments
            val shopId = segments[0]
            return if (segments.size == SHOP_FOLLOWER_LIST_SEGMENT_SIZE) {
                UriUtil.buildUri(
                    ApplinkConstInternalMarketplace.SHOP_FAVOURITE_LIST_WITH_SHOP_ID,
                    shopId
                )
            } else {
                deeplink
            }
        }
        return deeplink
    }

    fun getRegisteredNavigationShopPageSettingCustomerApp(deeplink: String): String {
        if (deeplink.startsWithPattern(ApplinkConst.SHOP_SETTINGS_CUSTOMER_APP)) {
            val segments = Uri.parse(deeplink).pathSegments
            val shopId = segments[0]
            return if (segments.size == SHOP_PAGE_SETTING_WITH_SHOP_ID_SEGMENT_SIZE) {
                UriUtil.buildUri(
                    ApplinkConstInternalMarketplace.SHOP_PAGE_SETTING_CUSTOMER_APP_WITH_SHOP_ID,
                    shopId
                )
            } else {
                deeplink
            }
        }
        return deeplink
    }

    fun getRegisteredNavigationShopPageSettingSellerApp(deeplink: String): String {
        if (deeplink.startsWithPattern(ApplinkConst.SellerApp.SHOP_SETTINGS_SELLER_APP)) {
            val segments = Uri.parse(deeplink).pathSegments
            return if (segments.size == SHOP_PAGE_SETTING_WITH_SHOP_ID_SEGMENT_SIZE) {
                UriUtil.buildUri(ApplinkConstInternalSellerapp.MENU_SETTING)
            } else {
                deeplink
            }
        }
        return deeplink
    }

    fun getRegisteredSellerCenter(): String {
        return UriUtil.buildUri(ApplinkConstInternalGlobal.WEBVIEW, SELLER_CENTER_URL)
    }

    fun getRegisteredNavigationForCreateShopVoucher(): String {
        val voucherType = "shop"
        return UriUtil.buildUri(ApplinkConstInternalSellerapp.SELLER_MVC_CREATE, voucherType)
    }

    fun getRegisteredNavigationForCreateVoucherProduct(): String {
        val voucherType = "product"
        return UriUtil.buildUri(ApplinkConstInternalSellerapp.SELLER_MVC_CREATE, voucherType)
    }

    fun getRegisteredNavigationForOfferLandingPage(deeplink: String): String {
        return deeplink.replace("${ApplinkConst.APPLINK_CUSTOMER_SCHEME}://", "${ApplinkConstInternalMechant.INTERNAL_MERCHANT}/")
    }

    fun isShopPageSettingSellerApp(deeplink: String): Boolean {
        val uri = Uri.parse(deeplink)
        return deeplink.startsWithPattern(ApplinkConst.SellerApp.SHOP_SETTINGS_SELLER_APP) && uri.lastPathSegment == SHOP_PAGE_SETTING_SEGMENT
    }

    fun isCreateShowcaseApplink(deeplink: String): Boolean {
        val uri = Uri.parse(deeplink)
        return (deeplink.startsWithPattern(ApplinkConst.SellerApp.SHOP_PAGE_PRODUCTS_CREATE_SHOWCASE) && uri.lastPathSegment == CREATE_SHOWCASE_SEGMENT)
    }

    fun isCreateShopVoucherApplink(deeplink: String): Boolean {
        return deeplink.startsWith(ApplinkConst.SellerApp.CREATE_VOUCHER)
    }

    fun isCreateVoucherProductApplink(deeplink: String): Boolean {
        return deeplink.startsWith(ApplinkConst.SellerApp.CREATE_VOUCHER_PRODUCT)
    }

    fun isVoucherProductListApplink(deeplink: String): Boolean {
        return deeplink.startsWith(ApplinkConst.SellerApp.VOUCHER_PRODUCT_LIST)
    }

    fun isShopVoucherDetailApplink(deeplink: String): Boolean {
        return deeplink.startsWith(ApplinkConst.SellerApp.VOUCHER_DETAIL)
    }

    fun isVoucherProductDetailApplink(deeplink: String): Boolean {
        return deeplink.startsWith(ApplinkConst.SellerApp.VOUCHER_PRODUCT_DETAIL)
    }

    fun isBuyMoreGetMoreOLPApplink(deeplink: String): Boolean {
        return deeplink.startsWith(ApplinkConst.BUY_MORE_GET_MORE_OLP)
    }

    fun isSellerShopFlashSaleApplink(deeplink: String): Boolean {
        val path = Uri.parse(deeplink).path.orEmpty()
        val removedPathLink = if (path.isEmpty()) {
            deeplink // already removed
        } else {
            deeplink.split(path).firstOrNull()
        }
        return removedPathLink == ApplinkConst.SellerApp.SELLER_SHOP_FLASH_SALE
    }

    fun isSellerTokopediaFlashSaleApplink(deeplink: String): Boolean {
        return deeplink.startsWith(ApplinkConst.SellerApp.SELLER_TOKOPEDIA_FLASH_SALE)
    }

    fun isSellerTokopediaUpcomingFlashSaleApplink(deeplink: String): Boolean {
        return deeplink.startsWith(ApplinkConst.SellerApp.SELLER_TOKOPEDIA_FLASH_SALE_UPCOMING)
    }

    fun isSellerTokopediaRegisteredFlashSaleApplink(deeplink: String): Boolean {
        return deeplink.startsWith(ApplinkConst.SellerApp.SELLER_TOKOPEDIA_FLASH_SALE_REGISTERED)
    }

    fun isSellerTokopediaOngoingFlashSaleApplink(deeplink: String): Boolean {
        return deeplink.startsWith(ApplinkConst.SellerApp.SELLER_TOKOPEDIA_FLASH_SALE_ONGOING)
    }

    fun isSellerTokopediaFinishedFlashSaleApplink(deeplink: String): Boolean {
        return deeplink.startsWith(ApplinkConst.SellerApp.SELLER_TOKOPEDIA_FLASH_SALE_FINISHED)
    }

    fun isSellerTokopediaFlashSaleCampaignDetailApplink(deeplink: String): Boolean {
        val appLink = Uri.parse(deeplink)
        return UriUtil.matchWithPattern(ApplinkConst.SellerApp.SELLER_TOKOPEDIA_FLASH_SALE_CAMPAIGN_DETAIL, appLink) != null
    }

    fun isSellerMvcIntroAppLink(deeplink: String): Boolean {
        return ApplinkConst.SellerApp.SELLER_MVC_INTRO == deeplink
    }

    fun isSellerMvcCreate(deeplink: String): Boolean {
        val uriAppLink = Uri.parse(deeplink)
        return UriUtil.matchWithPattern(ApplinkConst.SellerApp.SELLER_MVC_CREATE, uriAppLink) != null
    }

    fun isSellerMvcDetailAppLink(deeplink: String): Boolean {
        val appLink = Uri.parse(deeplink)
        return UriUtil.matchWithPattern(ApplinkConst.SellerApp.SELLER_MVC_DETAIL, appLink) != null
    }

    fun isSellerShopNibAppLink(deeplink: String): Boolean {
        return deeplink.startsWith(ApplinkConst.SellerApp.SELLER_SHOP_NIB)
    }

    fun getRegisteredNavigationForVoucherProductList(deeplink: String): String {
        val lastSegment = Uri.parse(deeplink).lastPathSegment.orEmpty()
        return UriUtil.buildUri(ApplinkConstInternalSellerapp.SELLER_MVC_LIST, lastSegment)
    }

    fun getRegisteredNavigationForShopVoucherDetail(deeplink: String): String {
        val lastSegment = Uri.parse(deeplink).lastPathSegment.orEmpty()
        return UriUtil.buildUri(ApplinkConstInternalSellerapp.SELLER_MVC_DETAIL, lastSegment)
    }

    fun getRegisteredNavigationForVoucherProductDetail(deeplink: String): String {
        val lastSegment = Uri.parse(deeplink).lastPathSegment.orEmpty()
        return UriUtil.buildUri(ApplinkConstInternalSellerapp.SELLER_MVC_DETAIL, lastSegment)
    }

    fun getRegisteredNavigationForSellerShopFlashSale(deeplink: String): String {
        val lastSegment = Uri.parse(deeplink).lastPathSegment.orEmpty()
        return UriUtil.buildUri(ApplinkConstInternalSellerapp.SELLER_SHOP_FLASH_SALE, lastSegment)
    }

    fun getRegisteredNavigationForSellerTokopediaFlashSale(): String {
        return ApplinkConstInternalSellerapp.SELLER_TOKOPEDIA_FLASH_SALE_UPCOMING
    }

    fun getRegisteredNavigationForSellerTokopediaFlashSaleUpcoming(): String {
        return ApplinkConstInternalSellerapp.SELLER_TOKOPEDIA_FLASH_SALE_UPCOMING
    }

    fun getRegisteredNavigationForSellerTokopediaFlashSaleRegistered(): String {
        return ApplinkConstInternalSellerapp.SELLER_TOKOPEDIA_FLASH_SALE_REGISTERED
    }

    fun getRegisteredNavigationForSellerTokopediaFlashSaleOngoing(): String {
        return ApplinkConstInternalSellerapp.SELLER_TOKOPEDIA_FLASH_SALE_ONGOING
    }

    fun getRegisteredNavigationForSellerTokopediaFlashSaleFinished(): String {
        return ApplinkConstInternalSellerapp.SELLER_TOKOPEDIA_FLASH_SALE_FINISHED
    }

    fun getRegisteredNavigationForSellerTokopediaFlashSaleCampaignDetail(deeplink: String): String {
        val appLink = Uri.parse(deeplink)
        val lastSegment = appLink.lastPathSegment.orEmpty()
        return UriUtil.buildUri(
            ApplinkConstInternalSellerapp.SELLER_TOKOPEDIA_FLASH_SALE_CAMPAIGN_DETAIL,
            lastSegment
        )
    }

    fun getRegisteredNavigationForSellerMvcIntro(): String {
        return ApplinkConstInternalSellerapp.SELLER_MVC_INTRO
    }

    fun getRegisteredNavigationForSellerMvcCreate(deeplink: String): String {
        val appLink = Uri.parse(deeplink)
        val lastSegment = appLink.lastPathSegment.orEmpty()
        return UriUtil.buildUri(
            ApplinkConstInternalSellerapp.SELLER_MVC_CREATE,
            lastSegment
        )
    }

    fun getRegisteredNavigationForSellerMvcDetail(deeplink: String): String {
        val appLink = Uri.parse(deeplink)
        val lastSegment = appLink.lastPathSegment.orEmpty()
        return UriUtil.buildUri(
            ApplinkConstInternalSellerapp.SELLER_MVC_DETAIL,
            lastSegment
        )
    }

    fun getRegisteredNavigationForSellerShopNib(): String {
        return ApplinkConstInternalSellerapp.SELLER_SHOP_NIB
    }

    fun getRegisteredNavigationForCreateShowcase(deeplink: String): String {
        val uri = Uri.parse(deeplink)
        if (deeplink.startsWithPattern(ApplinkConst.SellerApp.SHOP_PAGE_PRODUCTS_CREATE_SHOWCASE) && uri.lastPathSegment == CREATE_SHOWCASE_SEGMENT) {
            return Uri.parse(ApplinkConstInternalMarketplace.SHOP_PAGE_PRODUCT).buildUpon().apply {
                appendQueryParameter(PARAM_CREATE_SHOWCASE, "true")
            }.build().toString()
        }
        return deeplink
    }

    fun getRegisteredNavigationFromHttpForSellerTokopediaFlashSale(uri: Uri, deepLink: String): String {
        return if (uri.pathSegments
            .joinToString("/")
            .startsWith(URL_SEGMENT_FLASH_SALE_TOKOPEDIA, false)
        ) {
            ApplinkConstInternalSellerapp.SELLER_TOKOPEDIA_FLASH_SALE_UPCOMING
        } else {
            ""
        }
    }

    fun getRegisteredNavigationFromHttpForSellerShopFlashSale(uri: Uri, deepLink: String): String {
        return if (uri.pathSegments
            .joinToString("/")
            .startsWith(URL_SEGMENT_FLASH_SALE_TOKO, false)
        ) {
            getRegisteredNavigationForSellerShopFlashSale(deepLink)
        } else {
            ""
        }
    }

    fun getRegisteredNavigationFromHttpForSellerMvc(uri: Uri, deepLink: String): String {
        return if (uri.pathSegments
            .joinToString("/")
            .startsWith(URL_SEGMENT_SELLER_MVC, false)
        ) {
            getRegisteredNavigationForVoucherProductList(deepLink)
        } else {
            ""
        }
    }

    fun getRegisteredNavigationFromHttpForSlashPrice(uri: Uri, deepLink: String): String {
        return if (uri.pathSegments
            .joinToString("/")
            .startsWith(URL_SEGMENT_SLASH_PRICE, false)
        ) {
            ApplinkConstInternalSellerapp.SHOP_DISCOUNT
        } else {
            ""
        }
    }
}
