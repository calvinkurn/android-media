package com.tokopedia.applink.order

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.startsWithPattern
import com.tokopedia.config.GlobalConfig
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.kotlin.extensions.view.EMPTY
import java.net.URLDecoder

/**
 * Created by fwidjaja on 2020-01-26.
 */
object DeeplinkMapperOrder {

    private const val QUERY_TAB_ACTIVE = "tab_active"
    private const val QUERY_TAB_STATUS = "tab_status"
    private const val NEW_ORDER = "new_order"
    private const val CONFIRM_SHIPPING = "confirm_shipping"
    private const val IN_SHIPPING = "in_shipping"
    private const val DELIVERED = "delivered"
    private const val DONE = "done"
    private const val CANCELLED = "order_canceled"
    private const val ALL_ORDER = "all_order"
    private const val FILTER_STATUS_ID = "filter_status_id"
    private const val FILTER_ORDER_TYPE = "filter_order_type"
    private const val FILTER_WAITING_PICKUP = "7"
    private const val FILTER_WAITING_AWB = "9"
    private const val FILTER_AWB_INVALID = "10"
    private const val FILTER_AWB_CHANGE = "11"
    private const val FILTER_RETUR = "13"
    private const val FILTER_COMPLAINT = "15"
    const val FILTER_CANCELLATION_REQUEST = 10
    private const val PATH_ORDER = "order"
    const val PATH_ORDER_ID = "order_id"
    const val PATH_ORDER_DETAIL_ID = "order_detail_id"
    const val QUERY_PARAM_ORDER_ID = "order_id"
    const val QUERY_PARAM_DTL_ID = "dtl_id"
    const val QUERY_COACHMARK = "coachmark"

    fun getRegisteredNavigationOrder(context: Context, uri: Uri, deeplink: String): String {
        return if (deeplink.startsWithPattern(ApplinkConst.SELLER_ORDER_DETAIL)) {
            getRegisteredNavigationOrderInternal(context, uri)
        } else {
            deeplink
        }
    }

    /**
     * tokopedia://seller/order/{order_id}/
     */
    private fun getRegisteredNavigationOrderInternal(context: Context, uri: Uri): String {
        val redirectToSellerApp = uri.getBooleanQueryParameter(RouteManager.KEY_REDIRECT_TO_SELLER_APP, false)
        return if (DeviceScreenInfo.isTablet(context)) {
            val orderId = uri.getQueryParameter(QUERY_PARAM_ORDER_ID) ?: uri.pathSegments.last()
            getRegisteredNavigationMainAppSellerSplitOrderListOrderDetail(orderId, redirectToSellerApp)
        } else {
            val orderId = uri.getQueryParameter(QUERY_PARAM_ORDER_ID) ?: uri.pathSegments.last()
            getRegisteredNavigationMainAppOrderDetail(orderId, redirectToSellerApp)
        }
    }

    private fun getRegisteredNavigationMainAppOrderDetail(orderId: String, redirectToSellerApp: Boolean): String {
        return ApplinkConstInternalOrder.ORDER_DETAIL.replace("{order_id}", orderId).apply {
            if (redirectToSellerApp) plus("&${RouteManager.KEY_REDIRECT_TO_SELLER_APP}=true")
        }
    }

    private fun getRegisteredNavigationMainAppSellerSplitOrderListOrderDetail(orderId: String, redirectToSellerApp: Boolean): String {
        val param = mutableMapOf<String, Any>().apply {
            put(QUERY_PARAM_ORDER_ID, orderId)
            if (redirectToSellerApp) put(RouteManager.KEY_REDIRECT_TO_SELLER_APP, true)
        }
        return if (redirectToSellerApp || GlobalConfig.isSellerApp()) {
            UriUtil.buildUriAppendParams(ApplinkConstInternalSellerapp.SELLER_HOME_SOM_ALL, param)
        } else {
            UriUtil.buildUriAppendParams(ApplinkConstInternalOrder.HISTORY, param)
        }
    }

    fun getRegisteredNavigationMainAppSellerNewOrder(uri: Uri): String {
        val coachMark = uri.getQueryParameter(QUERY_COACHMARK).orEmpty()
        val queryParam = mutableMapOf(QUERY_TAB_ACTIVE to NEW_ORDER).apply {
            if (coachMark.isNotBlank()) {
                put(QUERY_COACHMARK, coachMark)
            }
        }.toMap()
        return UriUtil.buildUriAppendParam(ApplinkConstInternalOrder.NEW_ORDER, queryParam)
    }

    fun getRegisteredNavigationMainAppSellerReadyToShip(uri: Uri): String {
        val coachMark = uri.getQueryParameter(QUERY_COACHMARK).orEmpty()
        val queryParam = mutableMapOf(QUERY_TAB_ACTIVE to CONFIRM_SHIPPING).apply {
            if (coachMark.isNotBlank()) {
                put(QUERY_COACHMARK, coachMark)
            }
        }.toMap()
        return UriUtil.buildUriAppendParam(ApplinkConstInternalOrder.READY_TO_SHIP, queryParam)
    }

    fun getRegisteredNavigationMainAppSellerInShipping(): String {
        val param = mapOf(QUERY_TAB_ACTIVE to IN_SHIPPING)
        return UriUtil.buildUriAppendParam(ApplinkConstInternalOrder.SHIPPED, param)
    }

    fun getRegisteredNavigationMainAppSellerDelivered(): String {
        val param = mapOf(QUERY_TAB_ACTIVE to IN_SHIPPING, QUERY_TAB_STATUS to DELIVERED)
        return UriUtil.buildUriAppendParams(ApplinkConstInternalOrder.DELIVERED, param)
    }

    fun getRegisteredNavigationMainAppSellerFinished(): String {
        val param = mapOf(QUERY_TAB_ACTIVE to DONE)
        return UriUtil.buildUriAppendParam(ApplinkConstInternalOrder.FINISHED, param)
    }

    fun getRegisteredNavigationMainAppSellerCancelled(): String {
        val param = mapOf(QUERY_TAB_ACTIVE to CANCELLED)
        return UriUtil.buildUriAppendParam(ApplinkConstInternalOrder.CANCELLED, param)
    }

    fun getRegisteredNavigationMainAppSellerCancellationRequest(): String {
        val param = mapOf(
            QUERY_TAB_ACTIVE to ALL_ORDER,
            FILTER_ORDER_TYPE to FILTER_CANCELLATION_REQUEST
        )
        return UriUtil.buildUriAppendParams(ApplinkConstInternalOrder.CANCELLATION_REQUEST, param)
    }

    fun getRegisteredNavigationMainAppSellerHistory(orderId: String, coachMark: String): String {
        val param = mutableMapOf<String, Any>().apply {
            if (orderId.isNotEmpty()) put(QUERY_PARAM_ORDER_ID, orderId)
            put(QUERY_TAB_ACTIVE, ALL_ORDER)
            if (coachMark.isNotBlank()) put(QUERY_COACHMARK, coachMark)
        }
        return UriUtil.buildUriAppendParams(ApplinkConstInternalOrder.HISTORY, param)
    }

    fun getRegisteredNavigationMainAppSellerWaitingPickup(): String {
        val param = mapOf(QUERY_TAB_ACTIVE to "", FILTER_STATUS_ID to FILTER_WAITING_PICKUP)
        return UriUtil.buildUriAppendParams(ApplinkConstInternalOrder.WAITING_PICKUP, param)
    }

    fun getRegisteredNavigationMainAppSellerWaitingAwb(): String {
        val param = mapOf(QUERY_TAB_ACTIVE to "", FILTER_STATUS_ID to FILTER_WAITING_AWB)
        return UriUtil.buildUriAppendParams(ApplinkConstInternalOrder.WAITING_AWB, param)
    }

    fun getRegisteredNavigationMainAppSellerAwbInvalid(): String {
        val param = mapOf(QUERY_TAB_ACTIVE to "", FILTER_STATUS_ID to FILTER_AWB_INVALID)
        return UriUtil.buildUriAppendParams(ApplinkConstInternalOrder.AWB_INVALID, param)
    }

    fun getRegisteredNavigationMainAppSellerAwbChange(): String {
        val param = mapOf(QUERY_TAB_ACTIVE to "", FILTER_STATUS_ID to FILTER_AWB_CHANGE)
        return UriUtil.buildUriAppendParams(ApplinkConstInternalOrder.AWB_CHANGE, param)
    }

    fun getRegisteredNavigationMainAppSellerRetur(): String {
        val param = mapOf(QUERY_TAB_ACTIVE to "", FILTER_STATUS_ID to FILTER_RETUR)
        return UriUtil.buildUriAppendParams(ApplinkConstInternalOrder.RETUR, param)
    }

    fun getRegisteredNavigationMainAppSellerComplaint(): String {
        val param = mapOf(QUERY_TAB_ACTIVE to "", FILTER_STATUS_ID to FILTER_COMPLAINT)
        return UriUtil.buildUriAppendParams(ApplinkConstInternalOrder.COMPLAINT, param)
    }

    /**
     * @param deepLink tokopedia://snapshot/order/166497971/20370225
     * @return tokopedia-android-internal://snapshot/order?order_id=166497971&order_detail_id=20370225
     * or will return empty string if given invalid deep link
     * */
    fun getSnapshotOrderInternalAppLink(deepLink: String): String {
        val uri = Uri.parse(deepLink)
        return when {
            uri.pathSegments.size == 3 && uri.pathSegments[0] == PATH_ORDER -> {
                val orderId: String = if (!uri.pathSegments[1].isNullOrBlank()) {
                    uri.pathSegments[1]
                } else {
                    "0"
                }

                val orderDetailId: String = if (!uri.pathSegments[2].isNullOrBlank()) {
                    uri.pathSegments[2]
                } else {
                    "0"
                }

                val internalApplink = ApplinkConstInternalOrder.INTERNAL_ORDER_SNAPSHOT

                Uri.parse(internalApplink)
                    .buildUpon()
                    .appendQueryParameter(PATH_ORDER_ID, orderId)
                    .appendQueryParameter(PATH_ORDER_DETAIL_ID, orderDetailId)
                    .build()
                    .toString()
            }
            else -> ""
        }
    }

    /**
     * map a http url to tokopedia applink. example :
     * Url = https://www.tokopedia.com/snapshot_product?order_id=1149427127&dtl_id=2141552099
     * AppLink = tokopedia-android-internal://snapshot/order?order_id=1149427127&order_detail_id=2141552099
     * */
    fun mapSnapShotAppLinkFromHttp(url: String): String {
        return try {
            val uri = Uri.parse(url)
            val orderId = uri.getQueryParameter(QUERY_PARAM_ORDER_ID)
            val orderDetailId = uri.getQueryParameter(QUERY_PARAM_DTL_ID)

            if (orderId.isNullOrBlank() || orderDetailId.isNullOrBlank()) {
                return url
            }

            Uri.parse(ApplinkConstInternalOrder.INTERNAL_ORDER_SNAPSHOT)
                .buildUpon()
                .appendQueryParameter(QUERY_PARAM_ORDER_ID, orderId)
                .appendQueryParameter(PATH_ORDER_DETAIL_ID, orderDetailId)
                .build()
                .toString()
        } catch (e: Exception) {
            url
        }
    }

    fun getRegisteredNavigationSnapshotFromBranchLink(uri: Uri): String {
        try {
            val decodedUrl = URLDecoder.decode(uri.toString(), Charsets.UTF_8.name())
            val pattern = "(?<==).*?/snapshot_product\\?dtl_id=.*?&order_id=.*?[^&]*".toRegex()
            val result = pattern.find(decodedUrl)
            val matches = result?.groupValues
            if (!matches.isNullOrEmpty()) {
                return mapSnapShotAppLinkFromHttp(matches.first())
            }
        } catch (e: Exception) {
            return String.EMPTY
        }
        return String.EMPTY
    }

    fun getBuyerCancellationRequestInternalAppLink(): String {
        return ApplinkConstInternalOrder.INTERNAL_ORDER_BUYER_CANCELLATION_REQUEST_PAGE
    }
}
