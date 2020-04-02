package com.tokopedia.applink.sellerhome

import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.config.GlobalConfig

/**
 * Created By @ilhamsuaib on 2020-03-06
 */

object AppLinkMapperSellerHome {

    fun getSomNewOrderAppLink(): String {
        return if (GlobalConfig.isSellerApp()) {
            ApplinkConstInternalSellerapp.SELLER_HOME_SOM_NEW_ORDER
        } else {
            ApplinkConstInternalOrder.NEW_ORDER
        }
    }

    fun getSomReadyToShipAppLink(): String {
        return if (GlobalConfig.isSellerApp()) {
            ApplinkConstInternalSellerapp.SELLER_HOME_SOM_READY_TO_SHIP
        } else {
            ApplinkConstInternalOrder.READY_TO_SHIP
        }
    }

    fun getSomShippedAppLink(): String {
        return if (GlobalConfig.isSellerApp()) {
            ApplinkConstInternalSellerapp.SELLER_HOME_SOM_SHIPPED
        } else {
            ApplinkConstInternalOrder.SHIPPED
        }
    }

    fun getSomDoneAppLink(): String {
        return if (GlobalConfig.isSellerApp()) {
            ApplinkConstInternalSellerapp.SELLER_HOME_SOM_DONE
        } else {
            ApplinkConstInternalOrder.FINISHED
        }
    }

    fun getSomCancelledAppLink(): String {
        return if (GlobalConfig.isSellerApp()) {
            ApplinkConstInternalSellerapp.SELLER_HOME_SOM_CANCELLED
        } else {
            ApplinkConstInternalOrder.CANCELLED
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