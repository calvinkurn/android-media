package com.tokopedia.applink.sellerhome

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.config.GlobalConfig

/**
 * Created By @ilhamsuaib on 2020-03-06
 */

object DeepLinkMapperSellerHome {

    fun getSomNewOrderDeepLink(): String {
        return if (GlobalConfig.isSellerApp())
            ApplinkConstInternalMarketplace.SELLER_HOME_SOM_NEW_ORDER
        else
            ApplinkConstInternalOrder.NEW_ORDER
    }

    fun getSomReadyToShipDeepLink(): String {
        return if (GlobalConfig.isSellerApp())
            ApplinkConstInternalMarketplace.SELLER_HOME_SOM_READY_TO_SHIP
        else
            ApplinkConstInternalOrder.READY_TO_SHIP
    }

    fun getTopChatDeepLink(): String {
        return if (GlobalConfig.isSellerApp())
            ApplinkConstInternalMarketplace.SELLER_HOME_CHAT
        else
            ApplinkConst.TOP_CHAT
    }
}