package com.tokopedia.applink.order

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.digital.DeeplinkMapperDigital
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance

/**
 * Created by fwidjaja on 27/08/20.
 */
object DeeplinkMapperBuyerOrder {
    private var UOH_AB_TEST_KEY = "UOH_android"

    fun getRegisteredNavigationBuyerOrder(deeplink: String): String {
        var returnedDeeplink = ""
        if (deeplink.startsWith(ApplinkConst.BELANJA_ORDER)) {
            useUoh()?.let { newFlow ->
                returnedDeeplink = if (newFlow) ApplinkConstInternalOrder.UNIFY_ORDER
                else deeplink
            }
        } else if (deeplink.startsWith(ApplinkConst.MARKETPLACE_ORDER_SUB)) {
            useUoh()?.let { newFlow ->
                returnedDeeplink = if (newFlow) ApplinkConstInternalOrder.UNIFY_ORDER_IN_PROCESS
                else deeplink
            }
        }
        return returnedDeeplink
    }

    private fun getABTestRemoteConfig(): RemoteConfig? {
        return RemoteConfigInstance.getInstance().abTestPlatform
    }

    private fun useUoh(): Boolean? {
        /*val remoteConfigValue = getABTestRemoteConfig()?.getString(UOH_AB_TEST_KEY)
        return remoteConfigValue?.isNotEmpty()*/
        return true
    }
}