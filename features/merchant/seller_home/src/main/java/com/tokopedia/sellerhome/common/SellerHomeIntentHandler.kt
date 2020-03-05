package com.tokopedia.sellerhome.common

import android.content.Intent
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace

/**
 * Created By @ilhamsuaib on 2020-03-05
 */

object SellerHomeIntentHandler {

    fun handleDeepLink(
            intent: Intent?,
            callback: (fragmentType: Int, somTab: String) -> Unit
    ) {
        if (null == intent) return
        val data = intent.data?.toString().orEmpty()

        when {
            data.startsWith(ApplinkConstInternalMarketplace.SELLER_HOME_SOM_NEW_ORDER) -> {
                callback(FragmentType.ORDER, SomTabConst.STATUS_NEW_ORDER)
            }
        }
    }
}