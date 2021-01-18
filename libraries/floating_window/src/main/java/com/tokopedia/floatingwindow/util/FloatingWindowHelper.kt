package com.tokopedia.floatingwindow.util

import android.content.Context
import android.content.Intent
import android.os.Build
import com.tokopedia.floatingwindow.service.FloatingWindowService
import com.tokopedia.floatingwindow.service.FloatingWindowService.Companion.INTENT_COMMAND
import com.tokopedia.floatingwindow.service.FloatingWindowService.Companion.INTENT_COMMAND_EXIT
import com.tokopedia.floatingwindow.service.FloatingWindowService.Companion.INTENT_COMMAND_START

/**
 * Created by jegul on 26/11/20
 */
internal object FloatingWindowHelper {

    fun startService(context: Context) {
        val intent = Intent(context, FloatingWindowService::class.java)
                .putExtra(INTENT_COMMAND, INTENT_COMMAND_START)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

    fun stopService(context: Context) {
        val intent = Intent(context, FloatingWindowService::class.java)
                .putExtra(INTENT_COMMAND, INTENT_COMMAND_EXIT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }
}