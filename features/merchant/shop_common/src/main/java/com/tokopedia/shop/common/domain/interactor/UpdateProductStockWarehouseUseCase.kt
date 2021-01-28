package com.tokopedia.shop.common.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.data.source.cloud.query.UpdateProductStockWarehouse
import com.tokopedia.shop.common.data.model.ProductStock
import com.tokopedia.shop.common.data.source.cloud.query.param.UpdateProductStockWarehouseParam
import com.tokopedia.shop.common.domain.interactor.model.adminrevamp.ProductStockWarehouse
import com.tokopedia.shop.common.domain.interactor.model.adminrevamp.UpdateProductStockWarehouseResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class UpdateProductStockWarehouseUseCase @Inject constructor(graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<UpdateProductStockWarehouseResponse>(graphqlRepository) {

    companion object {
        private const val ERROR_MESSAGE = "Error updating IMS product warehouse stock"

        @JvmStatic
        fun createRequestParams(shopId: String,
                                productId: String,
                                warehouseId: String,
                                stock: String): RequestParams {
            val products = listOf(ProductStock(productId, stock))
            return createRequestParams(shopId, warehouseId, products)
        }

        @JvmStatic
        fun createRequestParams(shopId: String,
                                warehouseId: String,
                                products: List<ProductStock>): RequestParams {
            return RequestParams.create().apply {
                val params = UpdateProductStockWarehouseParam
                    .mapToParam(shopId, warehouseId, products)
                putObject(UpdateProductStockWarehouse.INPUT_KEY, params)
            }
        }
    }

    init {
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        setCacheStrategy(cacheStrategy)

        setGraphqlQuery(UpdateProductStockWarehouse.QUERY)
        setTypeClass(UpdateProductStockWarehouseResponse::class.java)
    }

    suspend fun execute(requestParams: RequestParams): ProductStockWarehouse {
        setRequestParams(requestParams.parameters)
        executeOnBackground().let { response ->
            response.result?.header?.messages?.let { errors ->
                if (!errors.isNullOrEmpty()) {
                    throw MessageErrorException(errors.joinToString())
                }
            }
            response.result?.data?.firstOrNull()?.let {
                return it
            }
            throw MessageErrorException(ERROR_MESSAGE)
        }
    }

}