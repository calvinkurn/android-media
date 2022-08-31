package com.tokopedia.buyerorder

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey

/**
 * created by @bayazidnasir on 31/8/2022
 */

const val KEY_CONTAINS_ORDER_DETAILS = "orderDetails"

fun setupRemoteConfig(context: Context, isNew: Boolean){
    val remoteConfig = FirebaseRemoteConfigImpl(context)
    remoteConfig.setString(RemoteConfigKey.MAINAPP_RECHARGE_BUYER_ORDER_DETAIL, isNew.toString())
}