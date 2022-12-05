package com.tokopedia.pushnotif.data.mapper

import com.tokopedia.pushnotif.data.constant.Constant
import com.tokopedia.pushnotif.data.db.model.TransactionNotification
import com.tokopedia.pushnotif.data.model.ApplinkNotificationModel

object TransactionMapper {

    fun mapToTransaction(
            data: ApplinkNotificationModel,
            notificationType: Int,
            notificationId: Int
    ): TransactionNotification {
        val thumbnailUrl = if (data.thumbnail.isNullOrBlank()) {
            Constant.DEFAULT_AVATAR_URL
        } else {
            data.thumbnail
        }
        return TransactionNotification(
                data.fullName,
                data.summary,
                notificationType,
                notificationId,
                data.transactionId.trim(),
                thumbnailUrl,
                data.applinks
        )
    }

}
