package com.tokopedia.applink.travel

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.FirebaseRemoteConfigInstance
import com.tokopedia.applink.internal.ApplinkConstInternalTravel
import com.tokopedia.applink.order.DeeplinkMapperUohOrder
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RemoteConfigKey

object DeeplinkMapperTravel {
    private const val HOTEL_AB_TEST_KEY = "Hotel_SearchByMap_An"
    private const val HOTEL_AB_TEST_NEW_VARIANT = "new_map"
    private const val HOTEL_AB_TEST_OLD_VARIANT = "old_map"

    @JvmStatic
    fun getRegisteredNavigationTravel(context: Context, deeplink: String): String {
        return when {
            deeplink.startsWith(ApplinkConst.HOTEL_SRP, true) -> {
                getHotelSrp(context)
            }
            deeplink.equals(ApplinkConst.HOTEL_ORDER, true) -> {
                DeeplinkMapperUohOrder.getRegisteredNavigationUohOrder(context, deeplink)
            }
            deeplink.startsWith(ApplinkConst.HOTEL, true) -> {
                ApplinkConstInternalTravel.DASHBOARD_HOTEL
            }
            deeplink.startsWith(ApplinkConst.HOTEL_DETAIL, true) -> {
                val uri = Uri.parse(deeplink)
                if(uri.pathSegments.size >= 4 && uri.pathSegments[2] == "h"){
                    val arrayOfHotelNames = uri.pathSegments[3].split("-")[1]
                    val hotelID = arrayOfHotelNames[arrayOfHotelNames.length - 1]
                    ApplinkConstInternalTravel.HOTEL_DETAIL + "/" + hotelID
                }else{
                    ApplinkConstInternalTravel.HOTEL_DETAIL + "/" + uri.lastPathSegment
                }
            }
            else -> deeplink
        }
    }

    @JvmStatic
    private fun getHotelSrp(context: Context): String {
        return if (isHotelSrpShowMap(context) && isABTestHotelShowMap()) {
            ApplinkConstInternalTravel.HOTEL_MAP_SRP
        } else {
            ApplinkConstInternalTravel.HOTEL_OLD_SRP
        }
    }

    @JvmStatic
    fun isHotelSrpShowMap(context: Context): Boolean = FirebaseRemoteConfigInstance.get(context)
            .getBoolean(RemoteConfigKey.CUSTOMER_HOTEL_SEARCH_WITH_MAP, true)

    @JvmStatic
    fun isABTestHotelShowMap(): Boolean = (RemoteConfigInstance.getInstance().abTestPlatform
            .getString(HOTEL_AB_TEST_KEY, HOTEL_AB_TEST_OLD_VARIANT)
            == HOTEL_AB_TEST_NEW_VARIANT)
}