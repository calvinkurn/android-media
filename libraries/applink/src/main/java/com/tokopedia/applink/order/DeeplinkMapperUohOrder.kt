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
object DeeplinkMapperUohOrder {
    private var UOH_AB_TEST_KEY = "uoh_android"
    private var UOH_AB_TEST_VALUE = "uoh_android"

    fun getRegisteredNavigationUohOrder(deeplink: String): String {
        var returnedDeeplink = ""
        if (deeplink.startsWith(ApplinkConst.DIGITAL_ORDER)
                || deeplink.startsWith(ApplinkConst.EVENTS_ORDER) || deeplink.startsWith(ApplinkConst.DEALS_ORDER)
                || deeplink.startsWith(ApplinkConst.FLIGHT_ORDER) || deeplink.startsWith(ApplinkConst.GIFT_CARDS_ORDER)
                || deeplink.startsWith(ApplinkConst.INSURANCE_ORDER) || deeplink.startsWith(ApplinkConst.MODAL_TOKO_ORDER)
                || deeplink.startsWith(ApplinkConst.HOTEL_ORDER)) {
            returnedDeeplink = if (useUoh()) ApplinkConstInternalOrder.UNIFY_ORDER
            else deeplink
        } else if (deeplink.startsWith(ApplinkConst.MARKETPLACE_ORDER_SUB)) {
            returnedDeeplink = if (useUoh()) ApplinkConstInternalOrder.UNIFY_ORDER_IN_PROCESS
            else deeplink
        } else if (deeplink.startsWith(ApplinkConst.BELANJA_ORDER)) {
            returnedDeeplink = if (useUoh()) ApplinkConstInternalOrder.UNIFY_ORDER_MARKETPLACE
            else deeplink
        }
        return returnedDeeplink
    }

    private fun useUoh(): Boolean {
        val remoteConfigValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(UOH_AB_TEST_KEY, "")
        return remoteConfigValue == UOH_AB_TEST_VALUE
    }
}