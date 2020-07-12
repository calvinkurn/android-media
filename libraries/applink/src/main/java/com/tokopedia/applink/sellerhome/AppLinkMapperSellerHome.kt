package com.tokopedia.applink.sellerhome

import android.net.Uri
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerCancelled
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerFinished
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerInShipping
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerNewOrder
import com.tokopedia.applink.order.DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerReadyToShip
import com.tokopedia.config.GlobalConfig

/**
 * Created By @ilhamsuaib on 2020-03-06
 */

object AppLinkMapperSellerHome {

    const val QUERY_PARAM_SEARCH = "search"

    fun getSomNewOrderAppLink(deepLink: String): String {
        val uri = Uri.parse(deepLink)
        val searchKeyword = uri.getQueryParameter(QUERY_PARAM_SEARCH).orEmpty()
        return if (GlobalConfig.isSellerApp()) {
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

    fun getSomReadyToShipAppLink(deepLink: String): String {
        val uri = Uri.parse(deepLink)
        val searchKeyword = uri.getQueryParameter(QUERY_PARAM_SEARCH).orEmpty()
        return if (GlobalConfig.isSellerApp()) {
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

    fun getSomShippedAppLink(deepLink: String): String {
        val uri = Uri.parse(deepLink)
        val searchKeyword = uri.getQueryParameter(QUERY_PARAM_SEARCH).orEmpty()
        return if (GlobalConfig.isSellerApp()) {
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

    fun getSomDoneAppLink(deepLink: String): String {
        val uri = Uri.parse(deepLink)
        val searchKeyword = uri.getQueryParameter(QUERY_PARAM_SEARCH).orEmpty()
        return if (GlobalConfig.isSellerApp()) {
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

    fun getSomCancelledAppLink(deepLink: String): String {
        val uri = Uri.parse(deepLink)
        val searchKeyword = uri.getQueryParameter(QUERY_PARAM_SEARCH).orEmpty()
        return if (GlobalConfig.isSellerApp()) {
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

    fun getTopChatAppLink(): String {
        return if (GlobalConfig.isSellerApp()) {
            ApplinkConstInternalSellerapp.SELLER_HOME_CHAT
        } else {
            ApplinkConstInternalGlobal.TOPCHAT
        }
    }
}