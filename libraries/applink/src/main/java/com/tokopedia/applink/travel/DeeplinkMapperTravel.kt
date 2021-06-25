package com.tokopedia.applink.travel

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalTravel
import com.tokopedia.applink.order.DeeplinkMapperUohOrder

object DeeplinkMapperTravel {
    private const val HOTEL_LAST_PATH_DETAIL = "detail"

    @JvmStatic
    fun getRegisteredNavigationTravel(context: Context, deeplink: String): String {
        return when {
            deeplink.startsWith(ApplinkConst.HOTEL_SRP, true) -> {
                ApplinkConstInternalTravel.HOTEL_SRP
            }
            deeplink.equals(ApplinkConst.HOTEL_ORDER, true) -> {
                DeeplinkMapperUohOrder.getRegisteredNavigationUohOrder(context, deeplink)
            }
            deeplink.startsWith(ApplinkConst.HOTEL_DASHBOARD, true) -> {
                ApplinkConstInternalTravel.DASHBOARD_HOTEL
            }
            deeplink.startsWith(ApplinkConst.HOTEL_DETAIL, true) -> {
                val uri = Uri.parse(deeplink)
                if(uri.lastPathSegment.equals(HOTEL_LAST_PATH_DETAIL)){
                    ApplinkConstInternalTravel.HOTEL_DETAIL
                }else{
                    ApplinkConstInternalTravel.HOTEL_DETAIL + "/" + uri.lastPathSegment
                }
            }
            else -> deeplink
        }
    }
}