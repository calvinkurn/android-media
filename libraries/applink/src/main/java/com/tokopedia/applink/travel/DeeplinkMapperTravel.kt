package com.tokopedia.applink.travel

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalTravel
import com.tokopedia.applink.purchaseplatform.DeeplinkMapperUoh

object DeeplinkMapperTravel {
    private const val HOTEL_LAST_PATH_DETAIL = "detail"

    @JvmStatic
    fun getRegisteredNavigationTravel(context: Context, deeplink: String): String {
        return when {
            deeplink.startsWith(ApplinkConst.HOTEL_SRP, true) -> {
                ApplinkConstInternalTravel.HOTEL_SRP
            }

            deeplink.equals(ApplinkConst.HOTEL_ORDER, true) -> {
                DeeplinkMapperUoh.getRegisteredNavigationUohOrder(context, deeplink)
            }

            deeplink.startsWith(ApplinkConst.HOTEL_DASHBOARD, true) -> {
                ApplinkConstInternalTravel.DASHBOARD_HOTEL
            }

            deeplink.startsWith(ApplinkConst.HOTEL_DETAIL, true) -> {
                val uri = Uri.parse(deeplink)
                if (uri.lastPathSegment.equals(HOTEL_LAST_PATH_DETAIL)) {
                    ApplinkConstInternalTravel.HOTEL_DETAIL
                } else {
                    ApplinkConstInternalTravel.HOTEL_DETAIL + "/" + uri.lastPathSegment
                }
            }

            else -> {
                checkDefaultHotelApplink(deeplink)
            }
        }
    }

    private fun checkDefaultHotelApplink(deeplink: String): String {
        return if (deeplink == ApplinkConst.HOTEL) {
            ApplinkConstInternalTravel.DASHBOARD_HOTEL
        } else {
            deeplink
        }
    }
}
