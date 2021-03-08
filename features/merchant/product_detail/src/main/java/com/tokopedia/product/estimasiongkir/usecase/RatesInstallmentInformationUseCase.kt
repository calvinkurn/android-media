package com.tokopedia.product.estimasiongkir.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.di.ProductDetailScope
import com.tokopedia.product.estimasiongkir.data.model.RatesInstallmentInformation
import com.tokopedia.product.info.model.productdetail.response.BottomSheetProductDetailInfoResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by Yehezkiel on 16/02/21
 */
@ProductDetailScope
class RatesInstallmentInformationUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository) : UseCase<String>() {

    companion object {
        fun createParams(productId: String, shopId: String): RequestParams = RequestParams.create().apply {
            putString(ProductDetailCommonConstant.PRODUCT_ID_PARAM, productId)
            putString(ProductDetailCommonConstant.SHOP_ID_PARAM, shopId)
        }

        val QUERY = """
            query PdpGetDetailBottomSheet(${'$'}productId:String,${'$'}shopId:String,${'$'}catalogId:String){
              pdpGetDetailBottomSheet(productID:${'$'}productId, shopID:${'$'}shopId, catalogID:${'$'}catalogId, types:"usp_tokocabang"){
                uspTokoCabang{
                    html
                }
              }
            }""".trimIndent()
    }

    private var requestParams = RequestParams.EMPTY
    private var forceRefresh = false

    suspend fun executeOnBackground(requestParams: RequestParams, forceRefresh: Boolean): String {
        this.requestParams = requestParams
        this.forceRefresh = forceRefresh
        return executeOnBackground()
    }

    override suspend fun executeOnBackground(): String {
        val request = GraphqlRequest(QUERY,
                BottomSheetProductDetailInfoResponse::class.java, requestParams.parameters)
        val cacheStrategy = GraphqlCacheStrategy.Builder(if (forceRefresh) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST)
                .setSessionIncluded(true)
                .build()

        val gqlResponse = graphqlRepository.getReseponse(listOf(request), cacheStrategy)
        val error: List<GraphqlError>? = gqlResponse.getError(RatesInstallmentInformation.Response::class.java)
        val data = gqlResponse.getData<RatesInstallmentInformation.Response>(RatesInstallmentInformation.Response::class.java)

        if (error != null && error.isNotEmpty() || data == null) {
            return ""
        }

        return data.response.tokoCabangData.html
    }
}
