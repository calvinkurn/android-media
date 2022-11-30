package com.tokopedia.mvc.domain.usecase


import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.mvc.data.mapper.ProductV3Mapper
import com.tokopedia.mvc.data.request.ProductV3ExtraInfo
import com.tokopedia.mvc.data.request.ProductV3Options
import com.tokopedia.mvc.data.response.ProductV3Response
import com.tokopedia.mvc.domain.entity.VariantResult
import javax.inject.Inject

class ProductV3UseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: ProductV3Mapper,
) : GraphqlUseCase<VariantResult>(repository) {

    init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    companion object {
        private const val REQUEST_PARAM_PRODUCT_ID = "productID"
        private const val REQUEST_PARAM_OPTIONS = "options"
        private const val REQUEST_PARAM_EXTRA_INFO = "extraInfo"
    }

    private val query = object : GqlQueryInterface {
        private val OPERATION_NAME = "getProductV3"
        private val QUERY = """
           query $OPERATION_NAME(${'$'}productID: String!, ${'$'}options: OptionV3!, ${'$'}extraInfo: ExtraInfoV3, ${'$'}warehouseID: String) {
             $OPERATION_NAME(productID: ${'$'}productID, options: ${'$'}options, extraInfo: ${'$'}extraInfo, warehouseID: ${'$'}warehouseID) {
               status
               productName
               price
               stock
               pictures {
                 urlThumbnail
               }
               stats {
                 countView
                 countReview
                 countTalk
                 rating
               }
               txStats {
                 itemSold
                 txSuccess
                 txReject
               }
               variant {
                 selections {
                   unitID
                   options {
                     value
                     hexCode
                     unitValueID
                   }
                   unitName
                   variantID
                   identifier
                   variantName
                 }
                 products {
                   productID
                   isPrimary
                   sku
                   price
                   stock
                   status
                   combination
                   warehouseIDAll
                   warehouseCount
                   hasInbound
                 }
               }
             }
           }


    """.trimIndent()

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
        override fun getQuery(): String = QUERY
        override fun getTopOperationName(): String = OPERATION_NAME
    }

    suspend fun execute(param: Param): VariantResult {
        val request = buildRequest(param)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<ProductV3Response>()
        return mapper.map(data)
    }

    private fun buildRequest(param: Param): GraphqlRequest {
        val params = mapOf(
            REQUEST_PARAM_PRODUCT_ID to param.productId.toString(),
            REQUEST_PARAM_OPTIONS to ProductV3Options(stats = true, txStats = true, variant = true, basic = true, picture = true),
            REQUEST_PARAM_EXTRA_INFO to ProductV3ExtraInfo(event = true)
        )

        return GraphqlRequest(
            query,
            ProductV3Response::class.java,
            params
        )
    }


    data class Param(val productId: Long)
}

