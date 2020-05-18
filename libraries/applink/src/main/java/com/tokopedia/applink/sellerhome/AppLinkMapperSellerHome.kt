package com.tokopedia.applink.sellerhome

import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
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

    fun getSomNewOrderAppLink(): String {
        return if (GlobalConfig.isSellerApp()) {
            ApplinkConstInternalSellerapp.SELLER_HOME_SOM_NEW_ORDER
        } else {
            getRegisteredNavigationMainAppSellerNewOrder()
        }
    }

    fun getSomReadyToShipAppLink(): String {
        return if (GlobalConfig.isSellerApp()) {
            ApplinkConstInternalSellerapp.SELLER_HOME_SOM_READY_TO_SHIP
        } else {
            getRegisteredNavigationMainAppSellerReadyToShip()
        }
    }

    fun getSomShippedAppLink(): String {
        return if (GlobalConfig.isSellerApp()) {
            ApplinkConstInternalSellerapp.SELLER_HOME_SOM_SHIPPED
        } else {
            getRegisteredNavigationMainAppSellerInShipping()
        }
    }

    fun getSomDoneAppLink(): String {
        return if (GlobalConfig.isSellerApp()) {
            ApplinkConstInternalSellerapp.SELLER_HOME_SOM_DONE
        } else {
            getRegisteredNavigationMainAppSellerFinished()
        }
    }

    fun getSomCancelledAppLink(): String {
        return if (GlobalConfig.isSellerApp()) {
            ApplinkConstInternalSellerapp.SELLER_HOME_SOM_CANCELLED
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