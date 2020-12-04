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
        if (data.transactionId.isNotEmpty()) {
            PushNotificationDB.getInstance(context)
                    .transactionNotificationDao()
                    .storeNotification(mapToTransaction(data, notificationType, notificationId))
        }
    }

    @JvmStatic
    fun isRenderable(context: Context, transactionId: String): Boolean {
        if (transactionId.isBlank()) return true

        return try {
            PushNotificationDB.getInstance(context)
                    .transactionNotificationDao()
                    .isRenderable(transactionId.trim()) == 0
        } catch (e: Exception) {
            true
        }
    }

}