package com.tokopedia.notifications.data

import com.tokopedia.notifications.domain.AttributionUseCase
import com.tokopedia.notifications.model.BaseNotificationModel
import javax.inject.Inject

class AttributionManager @Inject constructor(
        private val attributionUseCase: AttributionUseCase
) {

    fun post(notification: BaseNotificationModel?) {
        val params = attributionUseCase.params(
                transactionId = notification?.transactionId,
                userTransId = notification?.userTransactionId,
                recipientId = notification?.userId,
                shopId = notification?.shopId,
                blastId = notification?.blastId
        )
        attributionUseCase.execute(params)
    }

}