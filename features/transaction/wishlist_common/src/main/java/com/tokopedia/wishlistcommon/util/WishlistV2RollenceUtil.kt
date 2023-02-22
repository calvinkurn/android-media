package com.tokopedia.wishlistcommon.util

import android.content.Context
import com.tokopedia.remoteconfig.*

object WishlistV2RemoteConfigRollenceUtil {

    private var isUseWishlistCollection: Boolean? = null

    fun isUsingWishlistCollection(context: Context): Boolean {
        return isEnableRemoteConfigWishlistCollection(context) && isEnableRollenceWishlistCollection()
    }

    fun isEnableRollenceWishlistSharing(): Boolean {
        return try {
            val abTestPlatform = RemoteConfigInstance.getInstance().abTestPlatform
            val abTestWishlistCollectionSharing = abTestPlatform.getString(RollenceKey.WISHLIST_COLLECTION_SHARING, "")

            abTestWishlistCollectionSharing == RollenceKey.WISHLIST_COLLECTION_SHARING
        } catch (throwable: Throwable) {
            false
        }
    }

    private fun isEnableRemoteConfigWishlistCollection(context: Context): Boolean {
        val config: RemoteConfig = FirebaseRemoteConfigImpl(context)
        isUseWishlistCollection = config.getBoolean(RemoteConfigKey.ENABLE_WISHLIST_COLLECTION, true)
        return isUseWishlistCollection ?: true
    }

    private fun isEnableRollenceWishlistCollection(): Boolean {
        return try {
            val remoteConfigRollenceValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.WISHLIST_COLLECTION, RollenceKey.CONTROL_VARIANT)
            return (remoteConfigRollenceValue == RollenceKey.EXPERIMENT_VARIANT)
        } catch (e: Exception) {
            true
        }
    }
}
