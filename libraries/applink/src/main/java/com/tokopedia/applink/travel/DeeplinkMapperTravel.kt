package com.tokopedia.applink.travel

import android.content.Context
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalTravel
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RemoteConfigKey

object DeeplinkMapperTravel {
    private const val HOTEL_AB_TEST_KEY = "Hotel_SearchByMap_An"
    private const val HOTEL_AB_TEST_NEW_VARIANT = "new_map"
    private const val HOTEL_AB_TEST_OLD_VARIANT = "old_map"

    fun getRegisteredNavigationTravel(context: Context, deeplink: String): String {
        return when {
            deeplink.startsWith(ApplinkConst.HOTEL_SRP, true) -> {
                getHotelSrp(context)
            }
            else -> deeplink
        }
    }

    private fun getHotelSrp(context: Context): String {
        return if (isHotelSrpShowMap(context) && isABTestHotelShowMap()) {
            ApplinkConstInternalTravel.HOTEL_MAP_SRP
        } else {
            ApplinkConstInternalTravel.HOTEL_OLD_SRP
        }
    }

    fun isHotelSrpShowMap(context: Context): Boolean = FirebaseRemoteConfigImpl(context)
            .getBoolean(RemoteConfigKey.CUSTOMER_HOTEL_SEARCH_WITH_MAP, true)

    fun isABTestHotelShowMap(): Boolean = (RemoteConfigInstance.getInstance().abTestPlatform
            .getString(HOTEL_AB_TEST_KEY, HOTEL_AB_TEST_OLD_VARIANT)
            == HOTEL_AB_TEST_NEW_VARIANT)
}