package com.tokopedia.product.manage.feature.stockreminder.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.product.manage.common.feature.variant.data.model.response.GetProductVariantResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetProductStockReminderUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<GetProductVariantResponse>(graphqlRepository) {

    companion object {

        val QUERY = """
        query getProductV3(${'$'}productID:String!, ${'$'}options:OptionV3!, ${'$'}extraInfo:ExtraInfoV3!, ${'$'}warehouseID:String){
          getProductV3(productID:${'$'}productID, options:${'$'}options, extraInfo:${'$'}extraInfo, warehouseID:${'$'}warehouseID){
            productID
            productName
            stockAlertCount
            stockAlertStatus
            stock
            variant{
              products{
                productID
                stockAlertCount
                stockAlertStatus
                isBelowStockAlert
                stock
                combination
              }
              selections {
                  variantID
                  variantName
                  unitID
                  identifier
                  options {
                    unitValueID
                    value
                    hexCode
                  }
              }
            }
          }
        }
    """.trimIndent()

        private const val PARAM_PRODUCT_ID = "productID"
        private const val PARAM_OPTIONS = "options"
        private const val PARAM_EXTRA_INFO = "extraInfo"
        private const val PARAM_EDIT = "edit"
        private const val PARAM_WAREHOUSE_ID = "warehouseID"
        private const val PARAM_STOCK_ALERT = "stockAlert"
        private const val PARAM_AGGREGATE = "aggregate"

        fun createRequestParams(productId: String,
                                paramEdit: Boolean = true,
                                warehouseId: String? = null, ): RequestParams {
            val optionsParam = RequestParams().apply {
                putBoolean(PARAM_EDIT, paramEdit)
            }.parameters

            val extraInfoParam = RequestParams().apply {
                putBoolean(PARAM_STOCK_ALERT, true)
                putBoolean(PARAM_AGGREGATE, true)
            }.parameters

            return RequestParams().apply {
                putString(PARAM_PRODUCT_ID, productId)
                putObject(PARAM_OPTIONS, optionsParam)
                putObject(PARAM_EXTRA_INFO, extraInfoParam)
                putObject(PARAM_WAREHOUSE_ID, warehouseId)
            }
        }
    }

    init {
        setGraphqlQuery(QUERY)
        setTypeClass(GetProductVariantResponse::class.java)
    }

    suspend fun execute(requestParams: RequestParams): GetProductVariantResponse {
        setRequestParams(requestParams.parameters)
        return executeOnBackground()
    }
}