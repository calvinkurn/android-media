package com.tokopedia.shop.score.common

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

fun getShopScoreDate(context: Context?): String {
    return if(context == null) {
        ""
    } else {
        FirebaseRemoteConfigImpl(context).getString(ShopScoreConstant.APP_DATE_SHOP_SCORE, "")
    }
}