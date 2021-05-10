package com.tokopedia.applink.sellerhome

import android.net.Uri
import com.tokopedia.applink.DeeplinkMapper
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
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
        val searchKeyword = uri.getQueryParameter(QUERY_PARAM_SEARCH).orEmpty()
        return if (GlobalConfig.isSellerApp() || shouldRedirectToSellerApp(uri)) {
            if(searchKeyword.isNotBlank()) {
                val param = mapOf(QUERY_PARAM_SEARCH to searchKeyword)
                UriUtil.buildUriAppendParam(ApplinkConstInternalSellerapp.SELLER_HOME_SOM_NEW_ORDER, param)
            } else {
                ApplinkConstInternalSellerapp.SELLER_HOME_SOM_NEW_ORDER
            }
        } else {
            getRegisteredNavigationMainAppSellerNewOrder()
        }
    }

    fun getSomReadyToShipAppLink(uri: Uri): String {
        val searchKeyword = uri.getQueryParameter(QUERY_PARAM_SEARCH).orEmpty()
        return if (GlobalConfig.isSellerApp() || shouldRedirectToSellerApp(uri)) {
            if (searchKeyword.isNotBlank()) {
                val param = mapOf(QUERY_PARAM_SEARCH to searchKeyword)
                UriUtil.buildUriAppendParam(ApplinkConstInternalSellerapp.SELLER_HOME_SOM_READY_TO_SHIP, param)
            } else {
                ApplinkConstInternalSellerapp.SELLER_HOME_SOM_READY_TO_SHIP
            }
        } else {
            getRegisteredNavigationMainAppSellerReadyToShip()
        }
    }

    fun getSomShippedAppLink(uri: Uri): String {
        val searchKeyword = uri.getQueryParameter(QUERY_PARAM_SEARCH).orEmpty()
        return if (GlobalConfig.isSellerApp() || shouldRedirectToSellerApp(uri)) {
            if(searchKeyword.isNotBlank()) {
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
            if(searchKeyword.isNotBlank()) {
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
            if(searchKeyword.isNotBlank()) {
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
            if(searchKeyword.isNotBlank()) {
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
        return if (GlobalConfig.isSellerApp() || shouldRedirectToSellerApp(uri)) {
            if(searchKeyword.isNotBlank()) {
                val param = mapOf(QUERY_PARAM_SEARCH to searchKeyword)
                UriUtil.buildUriAppendParam(ApplinkConstInternalSellerapp.SELLER_HOME_SOM_ALL, param)
            } else {
                ApplinkConstInternalSellerapp.SELLER_HOME_SOM_ALL
            }
        } else {
            getRegisteredNavigationMainAppSellerHistory()
        }
    }

    fun getTopChatAppLink(uri: Uri): String {
        return if (GlobalConfig.isSellerApp() || shouldRedirectToSellerApp(uri)) {
            ApplinkConstInternalSellerapp.SELLER_HOME_CHAT
        } else {
            ApplinkConstInternalGlobal.TOPCHAT
        }
    }

    fun shouldRedirectToSellerApp(uri: Uri): Boolean {
        return uri.getBooleanQueryParameter(RouteManager.KEY_REDIRECT_TO_SELLER_APP, false)
    }

    fun getSellerHomeAppLink(deepLink: String): String {
        val uri = Uri.parse(deepLink)
        val query = uri.query
        return DeeplinkMapper.createAppendDeeplinkWithQuery(ApplinkConstInternalSellerapp.SELLER_HOME, query)
    }
}