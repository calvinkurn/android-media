package com.tokopedia.mvc.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.mvc.data.mapper.ShopBasicDataMapper
import com.tokopedia.mvc.data.response.ShopBasicDataResponse
import com.tokopedia.mvc.domain.entity.ShopData
import javax.inject.Inject

class ShopBasicDataUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: ShopBasicDataMapper
) : GraphqlUseCase<ShopData>(repository) {

    init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }
    private val query = object : GqlQueryInterface {
        private val OPERATION_NAME = "shopBasicData"
        private val QUERY = """
             query $OPERATION_NAME {
             	$OPERATION_NAME {
                 result {
                   domain
                   logo
                   name
                 }
                 error {
                   message
                 }
               }
             }
    """.trimIndent()

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
        override fun getQuery(): String = QUERY
        override fun getTopOperationName(): String = OPERATION_NAME
    }

    suspend fun execute(): ShopData {
        val request = buildRequest()
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<ShopBasicDataResponse>()
        return mapper.map(data)
    }

    private fun buildRequest(): GraphqlRequest {
        return GraphqlRequest(
            query,
            ShopBasicDataResponse::class.java,
            emptyMap()
        )
    }


}
