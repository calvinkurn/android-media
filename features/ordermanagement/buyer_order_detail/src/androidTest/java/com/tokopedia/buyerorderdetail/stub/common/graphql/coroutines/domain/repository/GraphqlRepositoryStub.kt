package com.tokopedia.buyerorderdetail.stub.common.graphql.coroutines.domain.repository

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import java.lang.reflect.Type
import javax.inject.Inject

class GraphqlRepositoryStub @Inject constructor() : GraphqlRepository {

    var mapResult: MutableMap<Type, MutableList<Any>> = hashMapOf()
    var mapGraphqlError: MutableMap<Type, MutableList<List<GraphqlError>>> = hashMapOf()
    var isCached = false

    override suspend fun response(
        requests: List<GraphqlRequest>,
        cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse {
        val mapResult = mutableMapOf<Type, Any>()
        val mapGraphqlError = mutableMapOf<Type, List<GraphqlError>>()
        this.mapResult.forEach { (type, results) ->
            if (requests.any { it.typeOfT == type }) {
                mapResult[type] = if (results.size > 1) results.removeFirst() else results.first()
            }
        }
        this.mapGraphqlError.forEach { (type, graphqlErrors) ->
            if (requests.any { it.typeOfT == type }) {
                mapGraphqlError[type] = if (graphqlErrors.size > 1) graphqlErrors.removeFirst() else graphqlErrors.first()
            }
        }
        return GraphqlResponse(mapResult, mapGraphqlError, isCached)
    }

    fun createMapResult(pojo: Type, vararg resultData: Any) {
        mapResult[pojo] = resultData.toMutableList()
    }

    fun createErrorMapResult(pojo: Type, vararg errorMessage: String) {
        val gqlErrors = errorMessage.map {
            listOf(GraphqlError().apply {
                this.message = it
            })
        }.toMutableList()
        mapGraphqlError[pojo] = gqlErrors
    }

    fun clearMocks() {
        mapResult.clear()
        mapGraphqlError.clear()
    }
}