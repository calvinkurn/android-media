package com.tokopedia.broadcast.message.common

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment

interface BroadcastMessageRouter {

    fun getBroadcastMessageListIntent(context: Context): Intent
    fun getBroadcastMessageAttachProductIntent(context: Context, shopId: String, shopName: String,
                                 isSeller: Boolean, selectedIds: List<Int>,
                                 selectedHashProducts: ArrayList<HashMap<String, String>>): Intent
    fun sendEventTracking(event: String, category: String, action: String?, label: String?)
}