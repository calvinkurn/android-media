package com.tokopedia.gm.subscribe

import android.content.Context
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager

object GMSubscribeInternalRouter {

    @JvmStatic
    fun getGMMembershipIntent(context: Context) =
        RouteManager.getIntent(context, ApplinkConst.SellerApp.POWER_MERCHANT_SUBSCRIBE)

    @JvmStatic
    fun getGMSubscribeHomeIntent(context: Context) =
        RouteManager.getIntent(context, ApplinkConst.SellerApp.POWER_MERCHANT_SUBSCRIBE)
}