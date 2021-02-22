package com.tokopedia.moengage_wrapper.interfaces

import android.content.Intent
import androidx.annotation.DrawableRes

interface CustomPushDataListener {
    fun getNotificationBroadcastIntent() : Intent
    fun getPersistentNotificationIntent() : Intent
    @DrawableRes fun getIcStatNotifyWhiteDrawable() : Int
}