package com.tokopedia.pushnotif.data

import android.content.Context
import com.tokopedia.pushnotif.db.PushNotificationDB
import com.tokopedia.pushnotif.db.model.TransactionNotification

object TransactionRepository {

    fun insert(
            context: Context,
            senderName: String,
            message: String,
            notificationType: Int,
            notificationId: Int,
            transactionId: String
    ) {
        PushNotificationDB.getInstance(context)
                .transactionNotificationDao()
                .storeNotification(TransactionNotification(
                        senderName,
                        message,
                        notificationType,
                        notificationId,
                        transactionId
                ))
    }

    fun isRenderable(context: Context, transactionId: String): Boolean {
        if (transactionId.isEmpty()) return true

        return PushNotificationDB.getInstance(context)
                .transactionNotificationDao()
                .isRenderable(transactionId) == 0
    }

}