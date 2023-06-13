package com.tokopedia.shop.common.util

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey

object ShopPageRemoteConfigChecker {
    fun isEnableShopHomeNplWidget(context: Context?): Boolean {
        return FirebaseRemoteConfigImpl(context).getBoolean(
            RemoteConfigKey.ENABLE_SHOP_HOME_NPL_WIDGET,
            true
        )
    }
}
