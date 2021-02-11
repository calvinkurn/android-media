package com.tokopedia.product.info.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.di.ProductDetailScope
import com.tokopedia.product.info.model.productdetail.response.BottomSheetProductDetailInfoResponse
import com.tokopedia.product.info.model.productdetail.response.PdpGetDetailBottomSheet
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by Yehezkiel on 13/10/20
 */
@ProductDetailScope
class GetProductDetailBottomSheetUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository)
    : UseCase<PdpGetDetailBottomSheet>() {

    companion object {
        fun createParams(productId: String, shopId: String): RequestParams = RequestParams.create().apply {
            putString(ProductDetailCommonConstant.PRODUCT_ID_PARAM, productId)
            putString(ProductDetailCommonConstant.SHOP_ID_PARAM, shopId)
        }

        val QUERY = """
            query PdpGetDetailBottomSheet(${'$'}productId:String,${'$'}shopId:String,${'$'}catalogId:String){
              pdpGetDetailBottomSheet(productID:${'$'}productId, shopID:${'$'}shopId, catalogID:${'$'}catalogId){
                bottomsheetData{
                  title
                  componentName
                  isApplink
                  isShowable
                  value
                }
                dataShopNotes{
                  shopNotesData{
                    shopNotesID
                    title
                    content
                    isTerms
                    position
                    url
                    updateTime
                    updateTimeUTC
                  }
                  error
                }
                discussion{
                  title
                  buttonType
                  buttonCopy
                }
                error{
                  Code
                  Message
                  DevMessage
                }
              }
            }""".trimIndent()
    }

    private var requestParams = RequestParams.EMPTY
    private var forceRefresh = false

    suspend fun executeOnBackground(requestParams: RequestParams, forceRefresh: Boolean): PdpGetDetailBottomSheet {
        this.requestParams = requestParams
        this.forceRefresh = forceRefresh
        return executeOnBackground()
    }

    override suspend fun executeOnBackground(): PdpGetDetailBottomSheet {
        val request = GraphqlRequest(QUERY,
                BottomSheetProductDetailInfoResponse::class.java, requestParams.parameters)
        val cacheStrategy = GraphqlCacheStrategy.Builder(if (forceRefresh) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST)
                .setSessionIncluded(false)
                .build()

        val gqlResponse = graphqlRepository.getReseponse(listOf(request), cacheStrategy)
        val error: List<GraphqlError>? = gqlResponse.getError(BottomSheetProductDetailInfoResponse::class.java)
        val data = gqlResponse.getData<BottomSheetProductDetailInfoResponse>(BottomSheetProductDetailInfoResponse::class.java)

        if (error != null && error.isNotEmpty()) {
            throw MessageErrorException(error.firstOrNull()?.message ?: "")
        } else if (data == null) {
            throw RuntimeException()
        } else if (data.response.error.code > 0) {
            throw MessageErrorException(data.response.error.message)
        }

        return data.response
    }
}