package com.tokopedia.pushnotif.data.mapper

import com.tokopedia.pushnotif.db.model.TransactionNotification
import com.tokopedia.pushnotif.model.ApplinkNotificationModel

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
                data.transactionId
        )
    }

}