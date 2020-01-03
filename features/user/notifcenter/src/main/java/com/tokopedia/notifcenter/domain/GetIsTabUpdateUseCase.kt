package com.tokopedia.notifcenter.domain

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.notifcenter.data.entity.NotifcenterIsTabUpdateEntity
import com.tokopedia.notifcenter.data.consts.NotificationQueriesConstant
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class GetIsTabUpdateUseCase @Inject constructor(
        @Named(NotificationQueriesConstant.QUERY_IS_TAB_UPDATE)
        private val query: String,
        private val graphQlRequest: MultiRequestGraphqlUseCase
) : UseCase<NotifcenterIsTabUpdateEntity>() {

    override suspend fun executeOnBackground(): NotifcenterIsTabUpdateEntity {
        val request = GraphqlRequest(query, NotifcenterIsTabUpdateEntity::class.java)
        graphQlRequest.clearRequest()
        graphQlRequest.addRequest(request)
        val graphQlResponse: GraphqlResponse = graphQlRequest.executeOnBackground()
        return graphQlResponse.getData(NotifcenterIsTabUpdateEntity::class.java)
    }

}