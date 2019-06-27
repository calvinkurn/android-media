package com.tokopedia.kelontongapp.notification

import android.content.Context
import android.os.Bundle
import android.support.v4.app.NotificationManagerCompat

import com.tokopedia.kelontongapp.*

/**
 * Created by meta on 19/10/18.
 */
object NotificationFactory {

    fun show(context: Context, data: Bundle) {

        val notificationModel = NotificationModel.convertBundleToModel(data)
        if (allowToShow(notificationModel)) {
            val notificationManagerCompat = NotificationManagerCompat.from(context)
            val notificationId = NOTIFICATION_ID_GENERAL
            val notifChat = NotificationBuilder(context, notificationModel, notificationId).build()

            notificationManagerCompat.notify(notificationId, notifChat)
        }
    }

    fun allowToShow(notificationModel: NotificationModel): Boolean {
        val notificationCode: Boolean = LOWER_CODE <= notificationModel.tkpCode && notificationModel.tkpCode <= UPPER_CODE
        // still on progress
//        val targetApp: Boolean = notificationModel.targetApp != null && notificationModel.targetApp.equals(TARGET_APP)
        return notificationCode
                //&& targetApp
    }
}
