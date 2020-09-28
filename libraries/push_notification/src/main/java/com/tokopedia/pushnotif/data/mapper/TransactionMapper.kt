package com.tokopedia.pushnotif.data.mapper

import com.tokopedia.pushnotif.data.db.model.TransactionNotification
import com.tokopedia.pushnotif.data.model.ApplinkNotificationModel

object TransactionMapper {

    fun mapToTransaction(
            data: ApplinkNotificationModel,
            notificationType: Int,
            notificationId: Int
    ): TransactionNotification {
        return TransactionNotification(
                data.fullName,
                data.summary,
                notificationType,
                notificationId,
                data.transactionId.trim()
        )
    }

}