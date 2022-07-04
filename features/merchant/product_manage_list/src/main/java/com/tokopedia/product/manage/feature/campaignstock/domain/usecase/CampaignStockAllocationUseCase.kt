package com.tokopedia.product.manage.feature.campaignstock.domain.usecase

import com.tokopedia.abstraction.common.network.exception.ResponseDataNullException
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.constant.ErrorNetMessage
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.GetStockAllocationData
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.GetStockAllocationResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

private const val QUERY = """
        query CampaignGetStockAllocation (${'$'}productIds: String!, ${'$'}shopId: String!, ${'$'}warehouseID: String, ${'$'}isBundle: Boolean) {
          GetStockAllocation(productIDs: ${'$'}productIds, shopID: ${'$'}shopId, sellerWh: true, warehouseID: ${'$'}warehouseID, extraInfo:{campaignMultiloc: true, bundle: ${'$'}isBundle}) {
            header {
              process_time
              messages
              reason
              error_code
            }
            data {
              summary {
                is_variant
                product_name
                sellable_stock
                reserve_stock
                total_stock
              }
              detail {
                sellable {
                  product_id
                  warehouse_id
                  product_name
                  stock
                  campaign_types {
                    name
                    icon_url
                  }
                }
                reserve {
                  event_info {
                    event_type
                    event_name
                    description
                    stock
                    start_time
                    end_time
                    campaign_type {
                      id
                      name
                      icon_url
                    }
                    product {
                      product_id
                      warehouse_id
                      product_name
                      description
                      stock
                    }
                  }
                }
              }
            }
          }
        }
    """

@GqlQuery("CampaignGetStockAllocation", QUERY)
class CampaignStockAllocationUseCase @Inject constructor(gqlRepository: GraphqlRepository) :
    GraphqlUseCase<GetStockAllocationResponse>(gqlRepository) {

    companion object {
        private const val PRODUCT_IDS_KEY = "productIds"
        private const val SHOP_ID_KEY = "shopId"
        private const val WAREHOUSE_ID_KEY = "warehouseID"
        private const val IS_BUNDLE_KEY = "isBundle"

        @JvmStatic
        fun createRequestParam(
            productIds: List<String>,
            shopId: String,
            warehouseId: String,
            isBundling: Boolean
        ): RequestParams = RequestParams.create().apply {
            putString(PRODUCT_IDS_KEY, productIds.joinToString())
            putString(SHOP_ID_KEY, shopId)
            putString(WAREHOUSE_ID_KEY, warehouseId)
            putBoolean(IS_BUNDLE_KEY, isBundling)
        }
    }

    init {
        setTypeClass(GetStockAllocationResponse::class.java)
        setGraphqlQuery(CampaignGetStockAllocation())
    }

    suspend fun execute(productIds: List<String>,
                        shopId: String,
                        warehouseId: String,
                        isBundling: Boolean): GetStockAllocationData {
        val requestParams = createRequestParam(productIds, shopId, warehouseId, isBundling)
        setRequestParams(requestParams.parameters)
        val data = executeOnBackground()
        data.getStockAllocation.data.let { stockData ->
            stockData.firstOrNull()?.run {
                return this
            }
        }
        throw ResponseDataNullException(ErrorNetMessage.MESSAGE_ERROR_NULL_DATA)
    }

}