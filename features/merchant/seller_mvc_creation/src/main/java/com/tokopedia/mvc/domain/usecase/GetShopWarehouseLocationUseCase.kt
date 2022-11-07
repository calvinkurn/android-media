package com.tokopedia.mvc.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.mvc.data.mapper.GetShopWarehouseMapper
import com.tokopedia.mvc.data.request.GetShopWarehouseRequest
import com.tokopedia.mvc.data.response.GetShopWarehouseResponse
import com.tokopedia.mvc.domain.entity.Warehouse
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class GetShopWarehouseLocationUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: GetShopWarehouseMapper,
    private val userSession: UserSessionInterface
) : GraphqlUseCase<List<Warehouse>>(repository) {

    init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    companion object {
        private const val REQUEST_PARAM_INPUT = "input"
    }


    private val query = object : GqlQueryInterface {
        private val OPERATION_NAME = "ShopLocGetWarehouseByShopIDs"
        private val QUERY = """
        query $OPERATION_NAME(${'$'}input: ShopLocParamWarehouseByShopIDs!) {
             $OPERATION_NAME(input: ${'$'}input) {
                warehouses {
                   warehouse_id
                   warehouse_name
                   warehouse_type 
                }
             }
       }
    """.trimIndent()

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
        override fun getQuery(): String = QUERY
        override fun getTopOperationName(): String = OPERATION_NAME
    }

    suspend fun execute(param: Param): List<Warehouse> {
        val request = buildRequest(param)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<GetShopWarehouseResponse>()
        return mapper.map(data)
    }

    private fun buildRequest(param: Param): GraphqlRequest {
        val shopIds = listOf(userSession.shopId.toLongOrZero())
        val payload = GetShopWarehouseRequest(
            shopIds,
            param.includeTc,
            param.warehouseType
        )
        val params = mapOf(REQUEST_PARAM_INPUT to payload)

        return GraphqlRequest(
            query,
            GetShopWarehouseResponse::class.java,
            params
        )
    }

    data class Param(
        val includeTc: Boolean = false,
        val warehouseType: Int = 0
    )
}
