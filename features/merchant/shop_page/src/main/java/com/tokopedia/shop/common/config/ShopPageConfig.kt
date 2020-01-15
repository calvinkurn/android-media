package com.tokopedia.shop.common.config

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey.ENABLE_NEW_SHOP_PAGE

class ShopPageConfig(context: Context?) {

    private val remoteConfig = FirebaseRemoteConfigImpl(context)

    fun isNewShopPageEnabled(): Boolean {
        return remoteConfig.getBoolean(ENABLE_NEW_SHOP_PAGE, true)
    }
}