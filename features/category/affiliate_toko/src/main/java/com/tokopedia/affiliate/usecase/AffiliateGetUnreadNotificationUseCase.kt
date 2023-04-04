package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.response.AffiliateUnreadNotificationResponse
import com.tokopedia.affiliate.repository.AffiliateRepository
import com.tokopedia.affiliate.usecase.AffiliateGetUnreadNotificationUseCase.Companion.GQL_GET_UNREAD_NOTIFICATION
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.kotlin.extensions.view.orZero
import javax.inject.Inject

@GqlQuery("GetUnreadNotification", GQL_GET_UNREAD_NOTIFICATION)
class AffiliateGetUnreadNotificationUseCase @Inject constructor(
    private val repository: AffiliateRepository
) {
    companion object {
        const val GQL_GET_UNREAD_NOTIFICATION: String = """
            query getUnreadNotification {
                notifcenter_total_unread(type_id: 6, type_of_notif: 0) {
                notif_total_unread_int
            }
        }"""
    }

    suspend fun getUnreadNotifications(): Int {
        return repository.getGQLData(
            GetUnreadNotification.GQL_QUERY,
            AffiliateUnreadNotificationResponse::class.java,
            mapOf()
        ).totalUnread?.totalUnreadCount.orZero()
    }
}
