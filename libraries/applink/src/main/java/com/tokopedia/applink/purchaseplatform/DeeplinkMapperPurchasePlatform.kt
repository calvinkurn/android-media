package com.tokopedia.applink.purchaseplatform

import android.content.Context
import com.tokopedia.applink.FirebaseRemoteConfigInstance
import com.tokopedia.applink.internal.ApplinkConsInternalHome
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.remoteconfig.RollenceKey

object DeeplinkMapperPurchasePlatform {
    fun getRegisteredNavigationWishlist(context: Context): String {
        var returnedDeeplink: String
        if (useWishlistV2(context)) {
            returnedDeeplink = ApplinkConstInternalPurchasePlatform.WISHLIST_V2
        } else {
            returnedDeeplink = ApplinkConsInternalHome.HOME_WISHLIST
        }
        return returnedDeeplink
    }

    private fun useWishlistV2(context: Context): Boolean {
        return try {
            val remoteConfigRollenceValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.WISHLIST_V2_REVAMP, "")

            val remoteConfig = FirebaseRemoteConfigInstance.get(context)
            val remoteConfigFirebase: Boolean = remoteConfig.getBoolean(RemoteConfigKey.ENABLE_WISHLIST_REVAMP_v2)
            return (remoteConfigRollenceValue == RollenceKey.WISHLIST_V2_REVAMP && remoteConfigFirebase)

        } catch (e: Exception) {
            true
        }
    }
}