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
        return if (isWishlistV2(context)) {
            ApplinkConstInternalPurchasePlatform.WISHLIST_V2
        } else {
            ApplinkConsInternalHome.HOME_WISHLIST
        }
    }

    fun isWishlistV2(context: Context): Boolean {
        return useWishlistV2RemoteConfig(context) && useWishlistV2Rollence()
    }

    private fun useWishlistV2RemoteConfig(context: Context) = FirebaseRemoteConfigInstance.get(context).getBoolean(RemoteConfigKey.ENABLE_WISHLIST_REVAMP_v2)

    private fun useWishlistV2Rollence(): Boolean {
        return try {
            val remoteConfigRollenceValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.WISHLIST_V2_REVAMP, RollenceKey.WISHLIST_OLD_VARIANT)
            return (remoteConfigRollenceValue == RollenceKey.WISHLIST_V2_VARIANT)

        } catch (e: Exception) {
            true
        }
    }
}