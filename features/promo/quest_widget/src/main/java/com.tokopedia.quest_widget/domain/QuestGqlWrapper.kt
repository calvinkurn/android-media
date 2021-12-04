package com.tokopedia.quest_widget.domain

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import javax.inject.Inject

class QuestGqlWrapper @Inject constructor() {

    suspend fun <T : Any> getResponse(gqlResponseType: Class<T>, query: String, params: Map<String, Any>): T {
        val multiGraphqlUseCase = MultiRequestGraphqlUseCase()
        val graphQlRequest = GraphqlRequest(query, gqlResponseType, params)
        multiGraphqlUseCase.addRequest(graphQlRequest)
        multiGraphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())

        return multiGraphqlUseCase.executeOnBackground().getData(gqlResponseType)
    }

}