package com.tokopedia.shop.flashsale.common.util

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey.IS_SHOW_OUT_OF_STOCK_SECTION

object RemoteConfigUtil {

    fun isShowOutOfStockSection(context: Context?): Boolean {
        return context?.let {
            FirebaseRemoteConfigImpl(context).getBoolean(IS_SHOW_OUT_OF_STOCK_SECTION, true)
        } ?: false
    }

}
