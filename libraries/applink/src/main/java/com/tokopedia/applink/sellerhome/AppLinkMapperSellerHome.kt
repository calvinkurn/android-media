package com.tokopedia.applink.sellerhome

import android.net.Uri
import com.tokopedia.applink.DeeplinkMapper
import com.tokopedia.applink.RouteManager
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

    fun getSomNewOrderAppLink(deepLink: String): String {
        return if (GlobalConfig.isSellerApp() || shouldRedirectToSellerApp(deepLink)) {
            ApplinkConstInternalSellerapp.SELLER_HOME_SOM_NEW_ORDER
        } else {
            getRegisteredNavigationMainAppSellerNewOrder()
        }
    }

    fun getSomReadyToShipAppLink(deepLink: String): String {
        return if (GlobalConfig.isSellerApp() || shouldRedirectToSellerApp(deepLink)) {
            ApplinkConstInternalSellerapp.SELLER_HOME_SOM_READY_TO_SHIP
        } else {
            getRegisteredNavigationMainAppSellerReadyToShip()
        }
    }

    fun getSomShippedAppLink(deepLink: String): String {
        return if (GlobalConfig.isSellerApp() || shouldRedirectToSellerApp(deepLink)) {
            ApplinkConstInternalSellerapp.SELLER_HOME_SOM_SHIPPED
        } else {
            getRegisteredNavigationMainAppSellerInShipping()
        }
    }

    fun getSomDoneAppLink(deepLink: String): String {
        return if (GlobalConfig.isSellerApp() || shouldRedirectToSellerApp(deepLink)) {
            ApplinkConstInternalSellerapp.SELLER_HOME_SOM_DONE
        } else {
            getRegisteredNavigationMainAppSellerFinished()
        }
    }

    fun getSomCancelledAppLink(deepLink: String): String {
        return if (GlobalConfig.isSellerApp() || shouldRedirectToSellerApp(deepLink)) {
            ApplinkConstInternalSellerapp.SELLER_HOME_SOM_CANCELLED
        } else {
            getRegisteredNavigationMainAppSellerCancelled()
        }
    }

    fun getTopChatAppLink(deepLink: String): String {
        return if (GlobalConfig.isSellerApp() || shouldRedirectToSellerApp(deepLink)) {
            ApplinkConstInternalSellerapp.SELLER_HOME_CHAT
        } else {
            ApplinkConstInternalGlobal.TOPCHAT
        }
    }

    private fun shouldRedirectToSellerApp(deepLink: String): Boolean {
        val uri = Uri.parse(deepLink)
        return uri.getBooleanQueryParameter(RouteManager.KEY_REDIRECT_TO_SELLER_APP, false)
    }

    fun getSellerHomeAppLink(deepLink: String): String {
        val query = Uri.parse(deepLink).query
        return DeeplinkMapper.createAppendDeeplinkWithQuery(ApplinkConstInternalSellerapp.SELLER_HOME, query)
    }
}