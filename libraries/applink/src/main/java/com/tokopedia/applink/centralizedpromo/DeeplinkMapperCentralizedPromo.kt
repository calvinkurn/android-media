package com.tokopedia.applink.centralizedpromo

import android.content.Context
import com.tokopedia.applink.FirebaseRemoteConfigInstance
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import timber.log.Timber

object DeeplinkMapperCentralizedPromo {
    private const val ENABLE_COMPOSE_CENTRALIZED_PROMO = "android_enable_compose_centralized_promo"

    private fun isRemoteConfigActive(
        context: Context,
    ): Boolean {
        return try {
            FirebaseRemoteConfigInstance.get(context)
                .getBoolean(ENABLE_COMPOSE_CENTRALIZED_PROMO, false)
        } catch (throwable: Throwable) {
            Timber.d(throwable)
            false
        }
    }

    fun getRegisteredNavigationCentralizedPromo(context: Context): String {
        return if (isRemoteConfigActive(context)) {
            ApplinkConstInternalSellerapp.CENTRALIZED_PROMO_COMPOSE
        } else {
            ApplinkConstInternalSellerapp.CENTRALIZED_PROMO
        }
    }
}