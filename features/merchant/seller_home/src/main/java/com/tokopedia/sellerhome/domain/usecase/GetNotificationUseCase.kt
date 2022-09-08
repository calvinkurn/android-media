package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.config.GlobalConfig
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seller.menu.common.domain.entity.Param
import com.tokopedia.sellerhome.domain.mapper.NotificationMapper
import com.tokopedia.sellerhome.domain.model.GetNotificationsResponse
import com.tokopedia.sellerhome.view.model.NotificationUiModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created By @ilhamsuaib on 2020-03-03
 */

@GqlQuery("GetNotificationGqlQuery", GetNotificationUseCase.QUERY)
class GetNotificationUseCase(
    private val gqlRepository: GraphqlRepository,
    private val mapper: NotificationMapper,
    private val userSession: UserSessionInterface
) : BaseGqlUseCase<NotificationUiModel>() {

    override suspend fun executeOnBackground(): NotificationUiModel {
        setAdditionalParam()
        val gqlRequest = GraphqlRequest(
            GetNotificationGqlQuery(),
            GetNotificationsResponse::class.java,
            params.parameters
        )
        val gqlResponse: GraphqlResponse = gqlRepository.response(listOf(gqlRequest))

        val errors: List<GraphqlError>? = gqlResponse.getError(GetNotificationsResponse::class.java)

        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetNotificationsResponse>()
            return mapper.mapRemoteModelToUiModel(data)
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    private fun setAdditionalParam() {
        params.parameters[PARAM_INPUT] = Param(userSession.shopId)
    }

    companion object {
        private const val PARAM_INPUT = "input"

        const val QUERY = """
            query getNotifications(${'$'}typeId: Int!, $$PARAM_INPUT: NotificationRequest) {
              notifications(input: $$PARAM_INPUT){
                chat {
                  unreadsSeller
                }
                sellerOrderStatus {
                  newOrder
                  readyToShip
                }
              }
              notifcenter_unread(type_id: ${'$'}typeId) {
                notif_unread_int
              }
              status
            }
        """
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