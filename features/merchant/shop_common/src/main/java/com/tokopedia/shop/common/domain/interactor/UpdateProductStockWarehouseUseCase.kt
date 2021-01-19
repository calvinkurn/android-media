package com.tokopedia.shop.common.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shop.common.domain.interactor.model.adminrevamp.UpdateProductStockWarehouseParam
import com.tokopedia.shop.common.domain.interactor.model.adminrevamp.UpdateProductStockWarehouseResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class UpdateProductStockWarehouseUseCase @Inject constructor(graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<UpdateProductStockWarehouseResponse>(graphqlRepository) {

    companion object {

        private const val QUERY = "mutation updateStockWarehouse(\$input: UpdatePWRequest!) {\n" +
                "  IMSUpdateProductWarehouse(input: \$input) {\n" +
                "    header {\n" +
                "      messages\n" +
                "      error_code\n" +
                "    }\n" +
                "    data {\n" +
                "      product_id\n" +
                "      warehouse_id\n" +
                "      stock\n" +
                "      shop_id\n" +
                "    }\n" +
                "  }\n" +
                "}"

        private const val INPUT_KEY = "input"

        @JvmStatic
        fun createRequestParams(shopId: String,
                                productId: String,
                                warehouseId: String,
                                stock: String): RequestParams {
            return RequestParams.create().apply {
                putObject(INPUT_KEY, UpdateProductStockWarehouseParam.mapToParam(shopId, productId, warehouseId, stock))
            }

        }
    }

    init {
        setGraphqlQuery(QUERY)
        setTypeClass(UpdateProductStockWarehouseResponse::class.java)
    }

    suspend fun execute(requestParams: RequestParams): UpdateProductStockWarehouseResponse {
        setRequestParams(requestParams.parameters)
        return executeOnBackground()
    }

}