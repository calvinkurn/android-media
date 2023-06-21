package com.tokopedia.buyerorder

import android.content.Context
import com.tokopedia.buyerorder.detail.revamp.util.Utils.Const.PREFERENCES_NAME
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey

/**
 * created by @bayazidnasir on 31/8/2022
 */

const val KEY_CONTAINS_ORDER_DETAILS = "orderDetails"
const val ORDER_DETAIL_APPLINK = "tokopedia://order/72b9fd8f-2e86-4484-8577-16cf1d97e16c?upstream-ORDERINTERNAL&vertical_category=foodvchr"
const val ORDER_ID_KEY = "order_id"
const val ORDER_ID_VALUE = "72b9fd8f-2e86-4484-8577-16cf1d97e16c"

fun disableCoachMark(context: Context){
    val sharedPref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    sharedPref.edit().putBoolean("show_coach_mark_key_deals_banner", false).apply()
}
