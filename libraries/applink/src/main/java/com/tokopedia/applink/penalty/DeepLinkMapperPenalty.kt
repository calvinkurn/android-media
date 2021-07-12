package com.tokopedia.applink.penalty

import android.content.Context
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.config.GlobalConfig
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey

object DeepLinkMapperPenalty {

    fun getPenaltyApplink(context: Context): String {
        return if (isEnableNewPenalty(context)) {
            ApplinkConstInternalMarketplace.SHOP_PENALTY
        } else {
            if (GlobalConfig.isSellerApp()) {
                ApplinkConstInternalSellerapp.SELLER_HOME
            } else {
                ApplinkConstInternalSellerapp.SELLER_MENU
            }
        }
    }

    fun isEnableNewPenalty(context: Context): Boolean{
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        return remoteConfig.getBoolean(RemoteConfigKey.ENABLE_NEW_PENALTY, true)
    }
}