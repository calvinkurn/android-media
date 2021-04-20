package com.tokopedia.promotionstarget.domain.usecase

import com.tokopedia.promotionstarget.data.GratifParams
import com.tokopedia.promotionstarget.data.di.GRATIFF_NOTIFICATION
import com.tokopedia.promotionstarget.data.gql.GqlUseCaseWrapper
import com.tokopedia.promotionstarget.data.notification.GratifNotificationResponse
import javax.inject.Inject
import javax.inject.Named

class NotificationUseCase @Inject constructor(@Named(GRATIFF_NOTIFICATION) val queryString: String,
                                              val gqlWrapper: GqlUseCaseWrapper) {
    private val PARAMS = GratifParams

    suspend fun getResponse(map: HashMap<String, Any>): GratifNotificationResponse {
        return gqlWrapper.getResponse(GratifNotificationResponse::class.java, queryString, map)
    }

    fun getQueryParams(notificationID: Int, notificationEntryType: Int, paymentID: Long): HashMap<String, Any> {
        val variables = HashMap<String, Any>()
        variables[PARAMS.NOTIFICATION_ID] = notificationID
        variables[PARAMS.NOTIFICATION_ENTRY_TYPE] = notificationEntryType
        variables[PARAMS.PAYMENT_ID] = paymentID
        return variables
    }
}