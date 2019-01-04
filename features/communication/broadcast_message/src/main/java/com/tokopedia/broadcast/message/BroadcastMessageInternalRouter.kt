package com.tokopedia.broadcast.message

import android.content.Context
import com.tokopedia.broadcast.message.view.activity.BroadcastMessageListActivity

object BroadcastMessageInternalRouter {

    fun getBroadcastMessageListIntent(context: Context) = BroadcastMessageListActivity.createIntent(context)
}