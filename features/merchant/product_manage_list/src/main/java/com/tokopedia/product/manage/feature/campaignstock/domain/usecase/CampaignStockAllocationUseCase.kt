package com.tokopedia.product.manage.feature.campaignstock.domain.usecase

import com.tokopedia.abstraction.common.network.exception.ResponseDataNullException
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.constant.ErrorNetMessage
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.GetStockAllocationData
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.GetStockAllocationResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class CampaignStockAllocationUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository): GraphqlUseCase<GetStockAllocationData>(gqlRepository) {

    companion object {
        private const val QUERY = "query getStockAllocation (\$productIds: String!, \$shopId: String!, \$warehouseID: String) {\n" +
                "  GetStockAllocation(productIDs: \$productIds, shopID: \$shopId, sellerWh: true, warehouseID: \$warehouseID) {\n" +
                "    header {\n" +
                "      process_time\n" +
                "      messages\n" +
                "      reason\n" +
                "      error_code\n" +
                "    }\n" +
                "    data {\n" +
                "      summary {\n" +
                "        is_variant\n" +
                "        product_name\n" +
                "        sellable_stock\n" +
                "        reserve_stock\n" +
                "        total_stock\n" +
                "      }\n" +
                "      detail {\n" +
                "        sellable {\n" +
                "          product_id\n" +
                "          warehouse_id\n" +
                "          product_name\n" +
                "          stock\n" +
                "        }\n" +
                "        reserve {\n" +
                "          event_info {\n" +
                "            event_type\n" +
                "            event_name\n" +
                "            description\n" +
                "            stock\n" +
                "            action_wording\n" +
                "            action_url\n" +
                "            product {\n" +
                "              product_id\n" +
                "              warehouse_id\n" +
                "              product_name\n" +
                "              description\n" +
                "              stock\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}"

        private const val PRODUCT_IDS_KEY = "productIds"
        private const val SHOP_ID_KEY = "shopId"
        private const val WAREHOUSE_ID_KEY = "warehouseID"

        @JvmStatic
        fun createRequestParam(
            productIds: List<String>,
            shopId: String,
            warehouseId: String
        ): RequestParams = RequestParams.create().apply {
            putString(PRODUCT_IDS_KEY, productIds.joinToString())
            putString(SHOP_ID_KEY, shopId)
            putString(WAREHOUSE_ID_KEY, warehouseId)
        }
    }

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): GetStockAllocationData {
        val gqlRequest = GraphqlRequest(QUERY, GetStockAllocationResponse::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest))

        val errors = gqlResponse.getError(GetStockAllocationResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetStockAllocationResponse>(GetStockAllocationResponse::class.java)
            data.getStockAllocation.data.let { stockData ->
                stockData.firstOrNull()?.run {
                    return this
                }
            }
            throw ResponseDataNullException(ErrorNetMessage.MESSAGE_ERROR_NULL_DATA)
        } else {
            throw MessageErrorException(errors.joinToString { it.message })
        }
    }
}