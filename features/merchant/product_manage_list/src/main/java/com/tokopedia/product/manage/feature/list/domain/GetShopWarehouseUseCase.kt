package com.tokopedia.product.manage.feature.list.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.manage.feature.list.data.model.ShopWarehouseResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery("GetShopWarehouseQuery", GetShopWarehouseUseCase.QUERY)
class GetShopWarehouseUseCase @Inject constructor(repository: GraphqlRepository) :
    GraphqlUseCase<ShopWarehouseResponse>(repository) {

    companion object {

        private const val SHOP_ID = "shopId"

        const val QUERY = """
            query getShopWarehouse(${'$'}shopId: Int!) {
              keroWarehouseShop(input: {shop_id: ${'$'}shopId, show_shop: 1, show_fulfillment: 1, show_enabler: 1, token: "", ut: ""}) {
                status
                config
                server_process_time
                data {
                  fulfillment {
                    partner_id
                    partner_name
                    status
                  }
                }
              }
            }
        """

        private fun createRequestParams(shopId:Long): RequestParams {
            return RequestParams.create().apply {
                putLong(SHOP_ID, shopId)
            }
        }
    }

    init {
        setGraphqlQuery(GetShopWarehouseQuery())
        setTypeClass(ShopWarehouseResponse::class.java)
    }

    suspend fun execute(shopId:Long): ShopWarehouseResponse {
        val requestParams = createRequestParams(shopId)
        setRequestParams(requestParams.parameters)
        return executeOnBackground()
    }
}
