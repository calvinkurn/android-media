package com.tokopedia.applink.travel

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.digital.DeeplinkMapperDigital
import com.tokopedia.applink.internal.ApplinkConstInternalTravel
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RemoteConfigKey

object DeeplinkMapperTravel {
    const val HOTEL_AB_TEST_KEY = "Hotel_SearchByMap_An"
    const val HOTEL_AB_TEST_NEW_VARIANT = "new_map"
    const val HOTEL_AB_TEST_OLD_VARIANT = "old_map"

    fun getRegisteredNavigationTravel(context: Context, deeplink: String): String {
        val uri = Uri.parse(deeplink)
        return when {
            deeplink.startsWith(ApplinkConst.HOTEL_SRP, true) -> {
                getHotelSrp(context)
            }
            else -> deeplink
        }
    }

    private fun getHotelSrp(context: Context): String {
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        val getHotelSrp = remoteConfig.getBoolean(RemoteConfigKey.CUSTOMER_HOTEL_SEARCH_WITH_MAP, true)
        return if (getHotelSrp && isABTestHotelRevamp()) {
            ApplinkConstInternalTravel.HOTEL_MAP_SRP
        } else {
            ApplinkConstInternalTravel.HOTEL_OLD_SRP
        }
    }

    private fun isABTestHotelRevamp(): Boolean = (RemoteConfigInstance.getInstance().abTestPlatform
            .getString(HOTEL_AB_TEST_KEY, HOTEL_AB_TEST_OLD_VARIANT)
            == HOTEL_AB_TEST_NEW_VARIANT)
}