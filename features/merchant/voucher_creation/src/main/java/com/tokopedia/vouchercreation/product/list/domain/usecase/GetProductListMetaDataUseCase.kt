package com.tokopedia.vouchercreation.product.list.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.vouchercreation.product.list.domain.model.response.ProductListMetaResponse
import javax.inject.Inject

class GetProductListMetaDataUseCase @Inject constructor(@ApplicationContext repository: GraphqlRepository)
    : GraphqlUseCase<ProductListMetaResponse>(repository) {

    companion object {

        private const val KEY_SHOP_ID = "shopID"
        private const val KEY_WAREHOUSE_ID = "warehouseID"
        private const val KEY_EXTRA_INFO = "extraInfo"
        private const val ID_CATEGORY = "category"

        @JvmStatic
        fun createParams(shopId: String, warehouseId: String): RequestParams {
            return RequestParams.create().apply {
                putString(KEY_SHOP_ID, shopId)
                putString(KEY_WAREHOUSE_ID, warehouseId)
                putObject(KEY_EXTRA_INFO, listOf(ID_CATEGORY))
            }
        }
    }

    private val query = """
        query ProductListMeta(${'$'}shopID: String!, ${'$'}warehouseID: String, ${'$'}extraInfo:[String]) {
            ProductListMeta(shopID: ${'$'}shopID, warehouseID: ${'$'}warehouseID, extraInfo: ${'$'}extraInfo) {
                header {
                  processTime
                  messages
                  reason
                  errorCode
                }
                data {
                  tab {
                    id
                    name
                    value
                  }                
                  sort {
                    id
                    name
                    value
                  }
                  shopCategories {
                    id
                    name
                    value
                  }
                }
            }
        }
    """.trimIndent()

    init {
        setGraphqlQuery(query)
        setTypeClass(ProductListMetaResponse::class.java)
    }
}
