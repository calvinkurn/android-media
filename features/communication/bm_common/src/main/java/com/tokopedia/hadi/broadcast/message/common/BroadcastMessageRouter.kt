package com.tokopedia.hadi.broadcast.message.common

import android.content.Context
import android.support.v4.app.Fragment

interface BroadcastMessageRouter {

    fun gotoBroadcastMessageList(context: Context, fragment: Fragment?)
}