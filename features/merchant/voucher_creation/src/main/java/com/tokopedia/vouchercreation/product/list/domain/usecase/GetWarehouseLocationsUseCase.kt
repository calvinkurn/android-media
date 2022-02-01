package com.tokopedia.vouchercreation.product.list.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.vouchercreation.product.list.domain.model.response.ShopLocGetWarehouseByShopIdsResponse
import javax.inject.Inject

class GetWarehouseLocationsUseCase @Inject constructor(@ApplicationContext repository: GraphqlRepository)
    : GraphqlUseCase<ShopLocGetWarehouseByShopIdsResponse>(repository) {

    companion object {

        private const val KEY_INPUT = "input"
        private const val KEY_SHOP_IDS = "shop_ids"
        private const val KEY_SOURCE = "source"
        private const val KEY_INCLUDE_TC = "include_tc"
        private const val KEY_INCLUDE_INACTIVE_WH = "include_inactive_wh"
        private const val SELLER_APP_SOURCE = "Seller App"

        @JvmStatic
        fun createRequestParams(
                shopId: Int,
                source: String = SELLER_APP_SOURCE,
                includeTc: Boolean = false,
                includeInactiveWh: Boolean = false): RequestParams {
            val inputParam = RequestParams().apply {
                putObject(KEY_SHOP_IDS, listOf(shopId))
                putString(KEY_SOURCE, source)
                putBoolean(KEY_INCLUDE_TC, includeTc)
                putBoolean(KEY_INCLUDE_INACTIVE_WH, includeInactiveWh)
            }.parameters
            return RequestParams().apply {
                putObject(KEY_INPUT, inputParam)
            }
        }
    }

    private val query = """
        query ShopLocGetWarehouseByShopIDs(${'$'}input: ShopLocParamWarehouseByShopIDs!) {
            ShopLocGetWarehouseByShopIDs(input: ${'$'}input) {
               warehouses {
                  warehouse_id
                  warehouse_name
                }                
            }
        }
    """.trimIndent()

    init {
        setGraphqlQuery(query)
        setTypeClass(ShopLocGetWarehouseByShopIdsResponse::class.java)
    }
}