package com.tokopedia.applink.sellermenu

import android.content.Context
import com.tokopedia.applink.FirebaseRemoteConfigInstance
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.remoteconfig.RemoteConfigKey

object SellerMenuDeeplinkMapper {

    fun getInternalApplinkSellerMenu(context: Context?): String {
        return if (isSellerMenuCompose(context)) {
            ApplinkConstInternalSellerapp.SELLER_MENU_COMPOSE
        } else {
            ApplinkConstInternalSellerapp.SELLER_MENU
        }
    }

    private fun isSellerMenuCompose(context: Context?): Boolean {
        return context?.let {
            FirebaseRemoteConfigInstance.get(it)
                .getBoolean(RemoteConfigKey.SELLER_MENU_COMPOSE, false)
                .orFalse()
        }.orFalse()
    }

}
