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
            val database = PushNotificationDB.getInstance(context)

            if (database.isOpen) {
                database.transactionNotificationDao()
                    .storeNotification(mapToTransaction(data, notificationType, notificationId))
            }
        }
    }

    @JvmStatic
    fun isRenderable(context: Context, transactionId: String): Boolean {
        if (transactionId.isBlank()) return true

        return try {
            val database = PushNotificationDB.getInstance(context)

            if (database.isOpen) {
                database.transactionNotificationDao()
                    .isRenderable(transactionId.trim()) == 0
            } else {
                true
            }
        } catch (e: Exception) {
            true
        }
    }

}