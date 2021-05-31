package com.tokopedia.homenav.mainnav.domain.usecases

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.homenav.mainnav.data.pojo.notif.NavNotificationPojo
import com.tokopedia.homenav.mainnav.domain.model.NavNotificationModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetNavNotification @Inject constructor(
        val graphqlUseCase: GraphqlRepository)
    : UseCase<NavNotificationModel>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): NavNotificationModel {
        val gqlRequest = GraphqlRequest(query, NavNotificationPojo::class.java, params.parameters)
        val gqlResponse = graphqlUseCase.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())

        val error = gqlResponse.getError(NavNotificationPojo::class.java)
        if (error == null || error.isEmpty()) {
            val data: NavNotificationPojo = gqlResponse.getData(NavNotificationPojo::class.java)
            return NavNotificationModel(
                    unreadCountComplain = data.notifications.resolutionAs.buyer,
                    unreadCountInboxTicket = data.notifications.inbox.inbox_ticket,
                    unreadCountReview = data.notifications.inbox.review
            )
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }

    companion object {
        private val query = getQuery()
        private fun getQuery(): String {
            return """{
                        notifications(){
                            resolutionAs {
                                buyer
                            }
                            inbox {
                                inbox_ticket
                                review
                            }
                    }
                }""".trimIndent()
        }
    }
}