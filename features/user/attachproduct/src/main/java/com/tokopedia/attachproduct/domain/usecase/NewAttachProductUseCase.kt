package com.tokopedia.attachproduct.domain.usecase

import com.tokopedia.attachproduct.data.model.AceSearchProductResponse
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import kotlinx.coroutines.CoroutineDispatcher

class NewAttachProductUseCase(private val repository: GraphqlRepository,
                              private val graphQuery: String,
                              private val dispatcher: CoroutineDispatcher):
        CoroutineUseCase<Map<String, Any>, AceSearchProductResponse>(dispatcher) {
    override suspend fun execute(params: Map<String, Any>): AceSearchProductResponse {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String {
        return graphQuery
    }
}
