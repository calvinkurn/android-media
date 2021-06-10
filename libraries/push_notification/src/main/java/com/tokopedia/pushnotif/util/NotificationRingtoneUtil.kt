package com.tokopedia.pushnotif.util

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.media.RingtoneManager.getActualDefaultRingtoneUri as getActualDefaultRingtoneUri
import android.media.RingtoneManager.getDefaultUri as getDefaultUri
import android.media.RingtoneManager.getValidRingtoneUri as getValidRingtoneUri

object NotificationRingtoneUtil {

    @JvmStatic
    fun ringtoneUri(context: Context): Uri {
        /*
         * according CM Push notification use getDefaultUri as secondary options,
         * so first uri should be return is from getDefaultUri.
         */
        val defaultRingtone: Uri? = getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        if (defaultRingtone != null) return defaultRingtone

        // another options if default uri is null
        val actualDefaultRingtone: Uri? = getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION)
        if (actualDefaultRingtone != null) return actualDefaultRingtone

        val actualDefaultAlarm: Uri? = getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM)
        if (actualDefaultAlarm != null) return actualDefaultAlarm

        // final option
        return getValidRingtoneUri(context)
    }

}