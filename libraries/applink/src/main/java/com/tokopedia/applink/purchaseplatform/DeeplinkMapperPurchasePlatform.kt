package com.tokopedia.applink.purchaseplatform

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.FirebaseRemoteConfigInstance
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.home.DeeplinkMapperHome
import com.tokopedia.applink.internal.ApplinkConsInternalHome
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform
import com.tokopedia.applink.startsWithPattern
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.remoteconfig.RollenceKey

object DeeplinkMapperPurchasePlatform {
    fun getRegisteredNavigationWishlist(context: Context): String {
        return if (isUsingWishlistCollection(context)) {
            ApplinkConstInternalPurchasePlatform.WISHLIST_COLLECTION
        } else {
            ApplinkConstInternalPurchasePlatform.WISHLIST_V2
        }
    }

    fun isUsingWishlistCollection(context: Context): Boolean {
        return isEnableRemoteConfigWishlistCollection(context) && isEnableRollenceWishlistCollection()
    }

    private fun isEnableRemoteConfigWishlistCollection(context: Context) = FirebaseRemoteConfigInstance.get(context).getBoolean(RemoteConfigKey.ENABLE_WISHLIST_COLLECTION)

    private fun isEnableRollenceWishlistCollection(): Boolean {
        return try {
            val remoteConfigRollenceValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.WISHLIST_COLLECTION, RollenceKey.WISHLIST_CONTROL_VARIANT)
            return (remoteConfigRollenceValue == RollenceKey.WISHLIST_EXPERIMENT_VARIANT)

        } catch (e: Exception) {
            true
        }
    }
}
