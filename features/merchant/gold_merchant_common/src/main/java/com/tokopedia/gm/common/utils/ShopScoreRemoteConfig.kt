package com.tokopedia.gm.common.utils

import android.content.Context
import com.tokopedia.gm.common.constant.START_DATE_TRANSITION_SHOP_SCORE
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

fun getShopScoreDate(context: Context?): String {
    return if(context == null) {
        ""
    } else {
        FirebaseRemoteConfigImpl(context).getString(START_DATE_TRANSITION_SHOP_SCORE, "")
    }
}