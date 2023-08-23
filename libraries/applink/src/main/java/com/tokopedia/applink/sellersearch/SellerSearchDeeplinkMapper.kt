package com.tokopedia.applink.sellersearch

import android.content.Context
import com.tokopedia.applink.FirebaseRemoteConfigInstance
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.remoteconfig.RemoteConfigKey

object SellerSearchDeeplinkMapper {

    fun getInternalApplinkSellerSearch(context: Context): String {
        return if (isSellerSearchCompose(context)) {
            ApplinkConstInternalSellerapp.SELLER_SEARCH_COMPOSE
        } else {
            ApplinkConstInternalSellerapp.SELLER_SEARCH
        }
    }

    private fun isSellerSearchCompose(context: Context): Boolean {
        return FirebaseRemoteConfigInstance.get(context)
            .getBoolean(RemoteConfigKey.SELLER_SEARCH_COMPOSE, true)
            .orFalse()
    }
}
