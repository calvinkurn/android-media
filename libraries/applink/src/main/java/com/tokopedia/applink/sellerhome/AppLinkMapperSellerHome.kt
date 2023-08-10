package com.tokopedia.applink.sellerhome

import android.net.Uri
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.order.DeeplinkMapperOrder
import com.tokopedia.applink.order.DeeplinkMapperOrder.FILTER_CANCELLATION_REQUEST
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerCancellationRequest
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerCancelled
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerFinished
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerHistory
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerInShipping
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerNewOrder
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerReadyToShip
import com.tokopedia.config.GlobalConfig

/**
 * Created By @ilhamsuaib on 2020-03-06
 */

object AppLinkMapperSellerHome {

    const val QUERY_PARAM_SEARCH = "search"
    const val FILTER_ORDER_TYPE = "filter_order_type"

    fun getSomNewOrderAppLink(uri: Uri): String {
        val param = mutableMapOf<String, String>()
        return if (GlobalConfig.isSellerApp() || shouldRedirectToSellerApp(uri)) {
            uri.queryParameterNames.forEach {
                param[it] = uri.getQueryParameter(it).orEmpty()
            }
            UriUtil.buildUriAppendParam(
                ApplinkConstInternalSellerapp.SELLER_HOME_SOM_NEW_ORDER,
                param
            )
        } else {
            val coachMark = uri.getQueryParameter(DeeplinkMapperOrder.QUERY_COACHMARK).orEmpty()
            if (coachMark.isNotBlank()) param[DeeplinkMapperOrder.QUERY_COACHMARK] = coachMark
            getRegisteredNavigationMainAppSellerNewOrder(param)
        }
    }

    fun getSomReadyToShipAppLink(uri: Uri): String {
        val param = mutableMapOf<String, String>()
        return if (GlobalConfig.isSellerApp() || shouldRedirectToSellerApp(uri)) {
            uri.queryParameterNames.forEach {
                param[it] = uri.getQueryParameter(it).orEmpty()
            }
            UriUtil.buildUriAppendParam(
                ApplinkConstInternalSellerapp.SELLER_HOME_SOM_READY_TO_SHIP,
                param
            )
        } else {
            val coachMark = uri.getQueryParameter(DeeplinkMapperOrder.QUERY_COACHMARK).orEmpty()
            if (coachMark.isNotBlank()) param[DeeplinkMapperOrder.QUERY_COACHMARK] = coachMark
            getRegisteredNavigationMainAppSellerReadyToShip(param)
        }
    }

    fun getSomShippedAppLink(uri: Uri): String {
        val searchKeyword = uri.getQueryParameter(QUERY_PARAM_SEARCH).orEmpty()
        return if (GlobalConfig.isSellerApp() || shouldRedirectToSellerApp(uri)) {
            if (searchKeyword.isNotBlank()) {
                val param = mapOf(QUERY_PARAM_SEARCH to searchKeyword)
                UriUtil.buildUriAppendParam(ApplinkConstInternalSellerapp.SELLER_HOME_SOM_SHIPPED, param)
            } else {
                ApplinkConstInternalSellerapp.SELLER_HOME_SOM_SHIPPED
            }
        } else {
            getRegisteredNavigationMainAppSellerInShipping()
        }
    }

    fun getSomDoneAppLink(uri: Uri): String {
        val searchKeyword = uri.getQueryParameter(QUERY_PARAM_SEARCH).orEmpty()
        return if (GlobalConfig.isSellerApp() || shouldRedirectToSellerApp(uri)) {
            if (searchKeyword.isNotBlank()) {
                val param = mapOf(QUERY_PARAM_SEARCH to searchKeyword)
                UriUtil.buildUriAppendParam(ApplinkConstInternalSellerapp.SELLER_HOME_SOM_DONE, param)
            } else {
                ApplinkConstInternalSellerapp.SELLER_HOME_SOM_DONE
            }
        } else {
            getRegisteredNavigationMainAppSellerFinished()
        }
    }

    fun getSomCancelledAppLink(uri: Uri): String {
        val searchKeyword = uri.getQueryParameter(QUERY_PARAM_SEARCH).orEmpty()
        return if (GlobalConfig.isSellerApp() || shouldRedirectToSellerApp(uri)) {
            if (searchKeyword.isNotBlank()) {
                val param = mapOf(QUERY_PARAM_SEARCH to searchKeyword)
                UriUtil.buildUriAppendParam(ApplinkConstInternalSellerapp.SELLER_HOME_SOM_CANCELLED, param)
            } else {
                ApplinkConstInternalSellerapp.SELLER_HOME_SOM_CANCELLED
            }
        } else {
            getRegisteredNavigationMainAppSellerCancelled()
        }
    }

    fun getSomCancellationRequestAppLink(uri: Uri): String {
        val searchKeyword = uri.getQueryParameter(QUERY_PARAM_SEARCH).orEmpty()
        return if (GlobalConfig.isSellerApp() || shouldRedirectToSellerApp(uri)) {
            val param = mutableMapOf<String, Any>(FILTER_ORDER_TYPE to FILTER_CANCELLATION_REQUEST)
            if (searchKeyword.isNotBlank()) {
                param[QUERY_PARAM_SEARCH] = searchKeyword
                UriUtil.buildUriAppendParams(ApplinkConstInternalSellerapp.SELLER_HOME_SOM_CANCELLATION_REQUEST, param)
            } else {
                UriUtil.buildUriAppendParams(ApplinkConstInternalSellerapp.SELLER_HOME_SOM_CANCELLATION_REQUEST, param)
            }
        } else {
            getRegisteredNavigationMainAppSellerCancellationRequest()
        }
    }

    fun getSomAllOrderAppLink(uri: Uri): String {
        val searchKeyword = uri.getQueryParameter(QUERY_PARAM_SEARCH).orEmpty()
        val orderId = uri.getQueryParameter(DeeplinkMapperOrder.QUERY_PARAM_ORDER_ID).orEmpty()
        val coachMark = uri.getQueryParameter(DeeplinkMapperOrder.QUERY_COACHMARK).orEmpty()
        return if (GlobalConfig.isSellerApp() || shouldRedirectToSellerApp(uri)) {
            val param = mutableMapOf<String, Any>().apply {
                if (searchKeyword.isNotEmpty()) put(QUERY_PARAM_SEARCH, searchKeyword)
                if (orderId.isNotEmpty()) put(DeeplinkMapperOrder.QUERY_PARAM_ORDER_ID, orderId)
                if (shouldRedirectToSellerApp(uri)) put(RouteManager.KEY_REDIRECT_TO_SELLER_APP, true)
                if (coachMark.isNotBlank()) put(DeeplinkMapperOrder.QUERY_COACHMARK, coachMark)
            }
            UriUtil.buildUriAppendParams(ApplinkConstInternalSellerapp.SELLER_HOME_SOM_ALL, param)
        } else {
            getRegisteredNavigationMainAppSellerHistory(orderId, coachMark)
        }
    }

    fun getTopChatAppLink(uri: Uri): String {
        return if (GlobalConfig.isSellerApp() || shouldRedirectToSellerApp(uri)) {
            ApplinkConstInternalSellerapp.SELLER_HOME_CHAT
        } else {
            ApplinkConstInternalMarketplace.TOPCHAT
        }
    }

    fun shouldRedirectToSellerApp(uri: Uri): Boolean {
        return uri.getBooleanQueryParameter(RouteManager.KEY_REDIRECT_TO_SELLER_APP, false)
    }

    fun getSellerHomeAppLink(deepLink: String): String {
        val uri = Uri.parse(deepLink)
        val query = uri.query
        return UriUtil.appendDiffDeeplinkWithQuery(ApplinkConstInternalSellerapp.SELLER_HOME, query)
    }
}
