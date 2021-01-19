package com.tokopedia.shop.common.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.data.source.cloud.query.UpdateProductStockWarehouse
import com.tokopedia.shop.common.data.source.cloud.query.param.UpdateProductStockWarehouseParam
import com.tokopedia.shop.common.domain.interactor.model.adminrevamp.ProductStockWarehouse
import com.tokopedia.shop.common.domain.interactor.model.adminrevamp.UpdateProductStockWarehouseResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class UpdateProductStockWarehouseUseCase @Inject constructor(graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<UpdateProductStockWarehouseResponse>(graphqlRepository) {

    companion object {
        @JvmStatic
        fun createRequestParams(shopId: String,
                                productId: String,
                                warehouseId: String,
                                stock: String): RequestParams {
            return RequestParams.create().apply {
                putObject(UpdateProductStockWarehouse.INPUT_KEY, UpdateProductStockWarehouseParam.mapToParam(shopId, productId, warehouseId, stock))
            }
        }
    }

    init {
        setGraphqlQuery(UpdateProductStockWarehouse.QUERY)
        setTypeClass(UpdateProductStockWarehouseResponse::class.java)
    }

    suspend fun execute(requestParams: RequestParams): ProductStockWarehouse? {
        setRequestParams(requestParams.parameters)
        executeOnBackground().let { response ->
            response.result?.header?.messages?.let {
                if (!it.isNullOrEmpty()) {
                    throw MessageErrorException(it.joinToString())
                }
            }
            return response.result?.data?.firstOrNull()
        }
    }

}