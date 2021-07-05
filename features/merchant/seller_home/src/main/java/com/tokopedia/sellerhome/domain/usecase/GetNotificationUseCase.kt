package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.config.GlobalConfig
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
        val gqlRequest = GraphqlRequest(QUERY, GetNotificationsResponse::class.java, params.parameters)
        val gqlResponse: GraphqlResponse = gqlRepository.getReseponse(listOf(gqlRequest))

        val errors: List<GraphqlError>? = gqlResponse.getError(GetNotificationsResponse::class.java)

        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetNotificationsResponse>()
            return mapper.mapRemoteModelToUiModel(data)
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        private const val QUERY = "query getNotifications(\$typeId:Int!) {\n" +
                "  notifications {\n" +
                "    chat {\n" +
                "      unreadsSeller\n" +
                "    }\n" +
                "    sellerOrderStatus {\n" +
                "      newOrder\n" +
                "      readyToShip\n" +
                "    }\n" +
                "  }\n" +
                "  notifcenter_unread(type_id:\$typeId) {\n" +
                "    notif_unread_int\n" +
                "  }\n" +
                "  status\n" + //Don't remove `status` field since it's necessary for refresh token flow
                "}"

        private const val TYPE_ID = "typeId"
        private const val TYPE_ID_DEFAULT = 0
        private const val TYPE_ID_SELLER = 2

        fun getRequestParams(): RequestParams = RequestParams.create().apply {
            if (GlobalConfig.isSellerApp()) {
                putInt(TYPE_ID, TYPE_ID_SELLER)
            } else {
                putInt(TYPE_ID, TYPE_ID_DEFAULT)
            }
        }
    }
}