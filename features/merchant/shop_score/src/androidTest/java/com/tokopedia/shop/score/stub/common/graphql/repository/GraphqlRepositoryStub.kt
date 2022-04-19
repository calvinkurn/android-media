package com.tokopedia.shop.score.stub.common.graphql.repository

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import java.lang.reflect.Type
import javax.inject.Inject

class GraphqlRepositoryStub @Inject constructor() : GraphqlRepository {

    private var mapResult: MutableMap<Type, Any> = hashMapOf()
    private var mapGraphqlError: MutableMap<Type, List<GraphqlError>> = hashMapOf()
    private var isCached = false

    override suspend fun response(
        requests: List<GraphqlRequest>,
        cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse {
        return GraphqlResponse(mapResult, mapGraphqlError, isCached)
    }

    fun createMapResult(pojo: Type, resultData: Any) {
        mapResult[pojo] = resultData
    }

    fun clearMocks() {
        mapResult.clear()
        mapGraphqlError.clear()
    }
}