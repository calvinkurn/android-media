package com.tokopedia.gm.common.utils

import android.content.Context
import com.tokopedia.gm.common.constant.APP_DATE_SHOP_SCORE
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

fun getShopScoreDate(context: Context?): String {
    return if(context == null) {
        ""
    } else {
        FirebaseRemoteConfigImpl(context).getString(APP_DATE_SHOP_SCORE, "")
    }
}