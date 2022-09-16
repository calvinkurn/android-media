package com.tokopedia.wishlistcommon.util

import android.content.Context
import com.tokopedia.remoteconfig.*

object WishlistV2RemoteConfigRollenceUtil {

    private var isUseAddRemoveWishlistV2: Boolean? = null
    private var isUseWishlistCollection: Boolean? = null

    fun isUsingAddRemoveWishlistV2(context: Context) :Boolean {
        return isEnableRemoteConfigAddRemoveWishlistV2(context) && isEnableRollenceAddRemoveWishlistV2()
    }

    private fun isEnableRemoteConfigAddRemoveWishlistV2(context: Context): Boolean {
        val config: RemoteConfig = FirebaseRemoteConfigImpl(context)
        isUseAddRemoveWishlistV2 = config.getBoolean(RemoteConfigKey.ENABLE_ADD_REMOVE_WISHLIST_V2, true)
        return isUseAddRemoveWishlistV2 ?: true
    }

    private fun isEnableRollenceAddRemoveWishlistV2(): Boolean {
        return try {
            val abTestPlatform = RemoteConfigInstance.getInstance().abTestPlatform
            val abTestAddRemoveWishlistV2 = abTestPlatform.getString(RollenceKey.ADD_REMOVE_WISHLIST_V2, "")

            abTestAddRemoveWishlistV2 == RollenceKey.ADD_REMOVE_WISHLIST_V2
        } catch (throwable: Throwable) {
            false
        }
    }

    fun isUsingWishlistCollection(context: Context) : Boolean {
        return isEnableRemoteConfigWishlistCollection(context) && isEnableRollenceWishlistCollection()
    }

    private fun isEnableRemoteConfigWishlistCollection(context: Context): Boolean {
        val config: RemoteConfig = FirebaseRemoteConfigImpl(context)
        isUseWishlistCollection = config.getBoolean(RemoteConfigKey.ENABLE_WISHLIST_COLLECTION, true)
        return isUseWishlistCollection ?: true
    }

    private fun isEnableRollenceWishlistCollection(): Boolean {
        return try {
            val remoteConfigRollenceValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.WISHLIST_COLLECTION, RollenceKey.WISHLIST_CONTROL_VARIANT)
            return (remoteConfigRollenceValue == RollenceKey.WISHLIST_EXPERIMENT_VARIANT)

        } catch (e: Exception) {
            true
        }

        // return true
    }
}