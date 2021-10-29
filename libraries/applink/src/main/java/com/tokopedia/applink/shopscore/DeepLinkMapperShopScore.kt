package com.tokopedia.applink.shopscore

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.config.GlobalConfig
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey

/**
 * Created By @ilhamsuaib on 13/04/21
 */

object DeepLinkMapperShopScore {

    fun getShopScoreApplink(context: Context): String {
        return if (isEnableNewShopScore(context)) {
            ApplinkConstInternalMarketplace.SHOP_PERFORMANCE
        } else {
            if (GlobalConfig.isSellerApp()) {
                ApplinkConstInternalSellerapp.SELLER_HOME
            } else {
                ApplinkConstInternalSellerapp.SELLER_MENU
            }
        }
    }

    fun isEnableNewShopScore(context: Context): Boolean {
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        return remoteConfig.getBoolean(RemoteConfigKey.ENABLE_NEW_SHOP_SCORE, true)
    }
}