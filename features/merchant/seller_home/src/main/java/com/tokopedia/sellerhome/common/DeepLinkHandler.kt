package com.tokopedia.sellerhome.common

import android.content.Intent
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace

/**
 * Created By @ilhamsuaib on 2020-03-05
 */

object DeepLinkHandler {

    fun handleAppLink(
            intent: Intent?,
            callback: (page: PageFragment) -> Unit
    ) {
        if (null == intent) return
        val data = intent.data?.toString().orEmpty()

        when {
            //Seller Order Management (som)
            data.startsWith(ApplinkConstInternalMarketplace.SELLER_HOME_SOM_ALL) -> {
                callback(PageFragment(FragmentType.ORDER, SomTabConst.STATUS_ALL_ORDER))
            }
            data.startsWith(ApplinkConstInternalMarketplace.SELLER_HOME_SOM_NEW_ORDER) -> {
                callback(PageFragment(FragmentType.ORDER, SomTabConst.STATUS_NEW_ORDER))
            }
            data.startsWith(ApplinkConstInternalMarketplace.SELLER_HOME_SOM_READY_TO_SHIP) -> {
                callback(PageFragment(FragmentType.ORDER, SomTabConst.STATUS_READY_TO_SHIP))
            }

            //Top Chat
            data.startsWith(ApplinkConstInternalMarketplace.SELLER_HOME_CHAT) -> {
                callback(PageFragment(FragmentType.CHAT))
            }
        }
    }
}