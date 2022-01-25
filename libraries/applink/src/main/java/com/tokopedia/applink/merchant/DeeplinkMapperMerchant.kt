package com.tokopedia.applink.merchant

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.inbox.DeeplinkMapperInbox
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.startsWithPattern
import com.tokopedia.config.GlobalConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey


/**
 * Created by Rafli Syam on 2020-02-04.
 */

object DeeplinkMapperMerchant {

    const val PARAM_CREATE_SHOWCASE = "is_create_showcase"
    private const val PARAM_RATING = "rating"
    private const val PARAM_SOURCE = "source"
    private const val ACTION_REVIEW = "review"
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

    private const val PARAM_URL = "url"

    fun getRegisteredNavigationReputation(deeplink: String): String {
        if (deeplink.startsWith(ApplinkConst.REPUTATION)) {
            val parsedUri = Uri.parse(deeplink)
            val segments = parsedUri.pathSegments

            if (GlobalConfig.isSellerApp()) {
                if (parsedUri.getQueryParameter(PARAM_SELLER_TAB)?.isNotBlank() == true) {
                    return Uri.parse(ApplinkConstInternalMarketplace.INBOX_REPUTATION)
                            .buildUpon()
                            .appendQueryParameter(PARAM_SELLER_TAB, parsedUri.getQueryParameter(PARAM_SELLER_TAB))
                            .build().toString()
                }
            }

            if (segments.size > 1) {
                val feedbackId = segments.last()
                return UriUtil.buildUri(ApplinkConstInternalMarketplace.REVIEW_DETAIL, feedbackId)
            }
            if (segments.size > 0) {
                val feedbackId = segments.last()
                return UriUtil.buildUri(ApplinkConstInternalMarketplace.INBOX_REPUTATION_DETAIL, feedbackId)
            }
            return if (goToInboxUnified()) {
                Uri.parse(ApplinkConstInternalMarketplace.INBOX).buildUpon().apply {
                    appendQueryParameter(ApplinkConst.Inbox.PARAM_PAGE, ApplinkConst.Inbox.VALUE_PAGE_REVIEW)
                    appendQueryParameter(ApplinkConst.Inbox.PARAM_ROLE, ApplinkConst.Inbox.VALUE_ROLE_BUYER)
                }.build().toString()
            } else {
                return ApplinkConstInternalMarketplace.INBOX_REPUTATION
            }
        }
        return deeplink
    }

    fun isShopPage(deeplink: String): Boolean {
        val uri = Uri.parse(deeplink)
        return deeplink.startsWithPattern(ApplinkConst.SHOP) && uri.pathSegments.size == SHOP_PAGE_SEGMENT_SIZE
    }

    fun isShopReview(deeplink: String): Boolean {
        val uri = Uri.parse(deeplink)
        return deeplink.startsWithPattern(ApplinkConst.SHOP_REVIEW) && uri.lastPathSegment == ACTION_REVIEW
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

    fun getRegisteredNavigationShopReview(shopId: String?): String {
        return UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_REVIEW, shopId)
    }

    fun getRegisteredNavigationProductReview(uri: Uri): String {
        val segments = uri.pathSegments
        val rating = uri.getQueryParameter(PARAM_RATING) ?: "5"
        val utmSource = uri.getQueryParameter(PARAM_SOURCE) ?: ""

        val reputationId = segments[segments.size - 2]
        val productId = segments.last()
        val newUri = UriUtil.buildUri(ApplinkConstInternalMarketplace.CREATE_REVIEW, reputationId, productId)
        return Uri.parse(newUri)
                .buildUpon()
                .appendQueryParameter(PARAM_RATING, rating)
                .appendQueryParameter(PARAM_SOURCE, utmSource)
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
            return if (pathSegment.size == 2)
                it.toString().startsWith(prefixShopPageHomeAppLink).and(pathSegment[1] == pathHome)
            else false
        } ?: false
    }

    fun getShopPageHomeInternalApplink(uri: Uri?): String {
        return uri?.let {
            val pathSegment = uri.pathSegments ?: return it.toString()
            return if (pathSegment.size == 2) {
                val shopId = pathSegment[0]
                UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_PAGE_HOME, shopId)
            } else it.toString()
        } ?: ""
    }

    fun isShopPageInfoDeeplink(uri: Uri?): Boolean {
        return uri?.let {
            val pathSegment = uri.pathSegments ?: return false
            val prefixShopPageHomeAppLink = "tokopedia://shop/"
            val pathInfo = "info"
            return if (pathSegment.size == 2)
                it.toString().startsWith(prefixShopPageHomeAppLink) and (pathSegment[1] == pathInfo)
            else false
        } ?: false

    }

    fun getShopPageInfoInternalApplink(uri: Uri?): String {
        return uri?.let {
            val pathSegment = uri.pathSegments ?: return it.toString()
            return if (pathSegment.size == 2) {
                val shopId = pathSegment[0]
                UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_PAGE_INFO, shopId)
            } else it.toString()
        } ?: ""
    }

    fun isShopPageResultEtalaseDeepLink(uri: Uri?): Boolean {
        return uri?.let {
            val pathSegment = uri.pathSegments ?: return false
            val prefixShopPageHomeAppLink = "tokopedia://shop/"
            val pathEtalase = "etalase"
            return if (pathSegment.size == SHOP_PAGE_RESULT_ETALASE_PATH_SEGMENT_SIZE)
                it.toString().startsWith(prefixShopPageHomeAppLink) and (pathSegment[1] == pathEtalase)
            else false
        } ?: false
    }

    fun isShopPageNoteDeeplink(uri: Uri?): Boolean {
        return uri?.let {
            val pathSegment = uri.pathSegments ?: return false
            val prefixShopPageHomeAppLink = "tokopedia://shop/"
            val pathNote = "note"
            return if (pathSegment.size == 2)
                it.toString().startsWith(prefixShopPageHomeAppLink) and (pathSegment[1] == pathNote)
            else false
        } ?: false
    }

    fun getShopPageNoteInternalApplink(uri: Uri?): String {
        return uri?.let {
            val pathSegment = uri.pathSegments ?: return it.toString()
            return if (pathSegment.size == 2) {
                val shopId = pathSegment[0]
                UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_PAGE_NOTE, shopId)
            } else it.toString()
        } ?: ""
    }

    fun getShopPageResultEtalaseInternalAppLink(uri: Uri?): String {
        return uri?.let {
            val pathSegment = uri.pathSegments ?: return it.toString()
            return if (pathSegment.size == SHOP_PAGE_RESULT_ETALASE_INTERNAL_PATH_SEGMENT_SIZE) {
                val shopId = pathSegment[0]
                val etalaseId = pathSegment[2]
                UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_PAGE_PRODUCT_LIST, shopId, etalaseId)
            } else it.toString()
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
        return (deeplink.startsWithPattern(ApplinkConst.SHOP_FEED) ||
                deeplink.startsWithPattern(ApplinkConst.SellerApp.SHOP_FEED)) &&
                uri.lastPathSegment == FEED_SEGMENT
    }

    fun getRegisteredNavigationShopFeed(deeplink: String): String {
        if (deeplink.startsWithPattern(ApplinkConst.SHOP_FEED) || deeplink.startsWithPattern(ApplinkConst.SellerApp.SHOP_FEED)) {
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
                UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_FAVOURITE_LIST_WITH_SHOP_ID, shopId)
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
                UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_PAGE_SETTING_CUSTOMER_APP_WITH_SHOP_ID, shopId)
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

    fun getRegisteredNavigationForCreateVoucherProduct(deeplink: String): String {
        val lastSegment = Uri.parse(deeplink).lastPathSegment.orEmpty()
        return UriUtil.buildUri(ApplinkConstInternalSellerapp.CREATE_VOUCHER_PRODUCT, lastSegment)
    }

    fun isShopPageSettingSellerApp(deeplink: String): Boolean {
        val uri = Uri.parse(deeplink)
        return deeplink.startsWithPattern(ApplinkConst.SellerApp.SHOP_SETTINGS_SELLER_APP) && uri.lastPathSegment == SHOP_PAGE_SETTING_SEGMENT
    }

    fun isCreateShowcaseApplink(deeplink: String): Boolean {
        val uri = Uri.parse(deeplink)
        return (deeplink.startsWithPattern(ApplinkConst.SellerApp.SHOP_PAGE_PRODUCTS_CREATE_SHOWCASE) && uri.lastPathSegment == CREATE_SHOWCASE_SEGMENT)
    }

    fun isCreateVoucherProductApplink(deeplink: String): Boolean {
        return deeplink.startsWith(ApplinkConst.SellerApp.CREATE_VOUCHER_PRODUCT)
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

    fun goToInboxUnified(): Boolean {
        if (GlobalConfig.isSellerApp()) return false
        return try {
            val useNewInbox = RemoteConfigInstance.getInstance().abTestPlatform.getString(
                    RollenceKey.KEY_AB_INBOX_REVAMP, RollenceKey.VARIANT_OLD_INBOX
            ) == RollenceKey.VARIANT_NEW_INBOX
            useNewInbox
        } catch (e: Exception) {
            false
        }
    }
}