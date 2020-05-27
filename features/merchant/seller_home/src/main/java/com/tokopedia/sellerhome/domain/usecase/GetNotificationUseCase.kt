package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.sellerhome.domain.mapper.NotificationMapper
import com.tokopedia.sellerhome.domain.model.GetNotificationsResponse
import com.tokopedia.sellerhome.view.model.NotificationUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 2020-03-03
 */

class GetNotificationUseCase(
        private val gqlRepository: GraphqlRepository,
        private val mapper: NotificationMapper
) : BaseGqlUseCase<NotificationUiModel>() {

    override suspend fun executeOnBackground(): NotificationUiModel {
        val gqlRequest = GraphqlRequest(QUERY, GetNotificationsResponse::class.java)
        val gqlResponse: GraphqlResponse = gqlRepository.getReseponse(listOf(gqlRequest))

        val errors: List<GraphqlError>? = gqlResponse.getError(GetNotificationsResponse::class.java)

        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetNotificationsResponse>()
            return mapper.mapRemoteModelToUiModel(data.notifications)
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        private const val QUERY = "query getNotifications {\n" +
                "  notifications {\n" +
                "    chat {\n" +
                "      unreads\n" +
                "      unreadsSeller\n" +
                "      unreadsUser\n" +
                "    }\n" +
                "    sellerOrderStatus {\n" +
                "      newOrder\n" +
                "      readyToShip\n" +
                "      shipped\n" +
                "      arriveAtDestination\n" +
                "    }\n" +
                "    notifcenter_unread {\n" +
                "      notif_unread\n" +
                "      notif_unread_int\n" +
                "    }\n" +
                "  }\n" +
                "}"
    }
}