package com.tokopedia.homenav.mainnav.domain.interactor

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.homenav.mainnav.data.pojo.notif.NotificationResolutionPojo
import com.tokopedia.homenav.mainnav.domain.model.NotificationResolutionModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetResolutionNotification @Inject constructor(
        val graphqlUseCase: GraphqlRepository)
    : UseCase<NotificationResolutionModel>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): NotificationResolutionModel {
        val gqlRequest = GraphqlRequest(query, NotificationResolutionPojo::class.java, params.parameters)
        val gqlResponse = graphqlUseCase.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())

        val error = gqlResponse.getError(NotificationResolutionPojo::class.java)
        if (error == null || error.isEmpty()) {
            val data: NotificationResolutionPojo = gqlResponse.getData(NotificationResolutionPojo::class.java)
            return NotificationResolutionModel(data.notifications.resolutionAs.buyer)
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
                    }
                }""".trimIndent()
        }
    }
}