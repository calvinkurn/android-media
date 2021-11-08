package com.tokopedia.attachproduct.fake.di

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import java.lang.reflect.Type
import javax.inject.Inject

class GraphqlRepositoryStub @Inject constructor() : GraphqlRepository {

    var resultData: MutableMap<Type, Any> = hashMapOf()
    var errorData: MutableMap<Type, List<GraphqlError>> = hashMapOf()

    override suspend fun response(
        requests: List<GraphqlRequest>,
        cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse {
        return GraphqlResponse(resultData, errorData, false)
    }

    fun setResultData(type: Type, response: Any) {
        resultData[type] = response
    }

    fun setError(type: Type, error: String) {
        val graphqlError = GraphqlError().apply {
            message = error
        }
        errorData[type] = listOf(graphqlError)
    }
}