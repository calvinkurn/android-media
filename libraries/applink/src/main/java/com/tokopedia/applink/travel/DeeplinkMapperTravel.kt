package com.tokopedia.applink.travel

import android.content.Context
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.FirebaseRemoteConfigInstance
import com.tokopedia.applink.internal.ApplinkConstInternalTravel
import com.tokopedia.applink.order.DeeplinkMapperUohOrder
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
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
            deeplink.startsWith(ApplinkConst.HOTEL_ORDER, true) -> {
                DeeplinkMapperUohOrder.getRegisteredNavigationUohOrder(context, deeplink)
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