package com.tokopedia.sellerhome.stub.gql

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import java.lang.reflect.Type

/**
 * Created by @ilhamsuaib on 24/03/22.
 */

class GraphqlRepositoryStub : GraphqlRepository {

    companion object {
        private var INSTANCE: GraphqlRepositoryStub? = null

        fun getInstance(): GraphqlRepositoryStub {
            if (INSTANCE == null) {
                INSTANCE = GraphqlRepositoryStub()
            }
            return INSTANCE!!
        }
    }

    var mapResult: MutableMap<Type, Any> = hashMapOf()
    var mapGraphqlError: MutableMap<Type, List<GraphqlError>> = hashMapOf()
    var isCached = false

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