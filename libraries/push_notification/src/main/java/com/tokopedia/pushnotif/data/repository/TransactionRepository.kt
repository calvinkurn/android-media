package com.tokopedia.pushnotif.data.repository

import android.content.Context
import com.tokopedia.pushnotif.data.mapper.TransactionMapper.mapToTransaction
import com.tokopedia.pushnotif.data.db.PushNotificationDB
import com.tokopedia.pushnotif.data.model.ApplinkNotificationModel

object TransactionRepository {

    @JvmStatic
    fun insert(
            context: Context,
            data: ApplinkNotificationModel,
            notificationType: Int,
            notificationId: Int
    ) {
        val notification = mapToTransaction(data, notificationType, notificationId)
        PushNotificationDB.getInstance(context)
                .transactionNotificationDao()
                .storeNotification(notification)
    }

    @JvmStatic
    fun isRenderable(context: Context, transactionId: String): Boolean {
        if (transactionId.isEmpty()) return true

        return PushNotificationDB.getInstance(context)
                .transactionNotificationDao()
                .isRenderable(transactionId) == 0
    }

}