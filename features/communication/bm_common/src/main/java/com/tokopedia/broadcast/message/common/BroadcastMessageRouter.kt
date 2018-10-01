package com.tokopedia.broadcast.message.common

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment

interface BroadcastMessageRouter {

    fun gotoBroadcastMessageList(context: Context, fragment: Fragment?)
    fun getBMAttachProductIntent(context: Context, shopId: String, shopName: String,
                                 isSeller: Boolean, selectedIds: List<Int>,
                                 selectedHashProducts: ArrayList<HashMap<String, String>>): Intent
}