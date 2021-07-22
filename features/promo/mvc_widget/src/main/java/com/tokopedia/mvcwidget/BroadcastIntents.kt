package com.tokopedia.mvcwidget

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager

object BroadcastIntents {

    const val ACTION_REGISTER_MEMBER = "com.tokopedia.mvcwidget.REGISTER_MEMBER"
    const val REGISTER_MEMBER_SUCCESS = "register_member_success"

    fun broadcastJadiMember(context: Context){
        LocalBroadcastManager.getInstance(context).sendBroadcast(getJadiMemberIntent())
    }

    fun getJadiMemberIntent():Intent{
        return Intent().also {
            it.action = ACTION_REGISTER_MEMBER
            it.putExtra(REGISTER_MEMBER_SUCCESS,true)
        }
    }
}
