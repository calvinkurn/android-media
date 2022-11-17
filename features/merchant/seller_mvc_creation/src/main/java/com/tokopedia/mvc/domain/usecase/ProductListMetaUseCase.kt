package com.tokopedia.mvc.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.mvc.data.mapper.ProductListMetaMapper
import com.tokopedia.mvc.data.response.ProductListMetaResponse
import com.tokopedia.mvc.domain.entity.ProductMetadata
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ProductListMetaUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: ProductListMetaMapper,
    private val userSession: UserSessionInterface
) : GraphqlUseCase<ProductMetadata>(repository) {

    init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    companion object {
        private const val REQUEST_PARAM_SHOP_ID = "shopID"
        private const val REQUEST_PARAM_WAREHOUSE_ID = "warehouseID"
        private const val REQUEST_PARAM_EXTRA_INFO = "extraInfo"
    }


    private val query = object : GqlQueryInterface {
        private val OPERATION_NAME = "ProductListMeta"
        private val QUERY = """
             query $OPERATION_NAME(${'$'}shopID: String!, ${'$'}warehouseID: String, ${'$'}extraInfo: [String]) {
                    $OPERATION_NAME(shopID: ${'$'}shopID, warehouseID: ${'$'}warehouseID, extraInfo: ${'$'}extraInfo) {
                        data {
                            sort {
                                id
                                name
                                value
                            }
                            shopCategories {
                                id
                                value
                                name
                            }
                        }
                    }
        }
    """.trimIndent()

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
        override fun getQuery(): String = QUERY
        override fun getTopOperationName(): String = OPERATION_NAME
    }

    suspend fun execute(param: Param): ProductMetadata {
        val request = buildRequest(param)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<ProductListMetaResponse>()
        return mapper.map(data)
    }

    private fun buildRequest(param: Param): GraphqlRequest {
        val extraInfo = listOf("category")
        val params = mapOf(
            REQUEST_PARAM_SHOP_ID to userSession.shopId,
            REQUEST_PARAM_WAREHOUSE_ID to param.warehouseId.toString(),
            REQUEST_PARAM_EXTRA_INFO to extraInfo
        )

        return GraphqlRequest(
            query,
            ProductListMetaResponse::class.java,
            params
        )
    }

    data class Param(val warehouseId : Long)
}
