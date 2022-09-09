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
class GetProductDetailBottomSheetUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository) :
    UseCase<PdpGetDetailBottomSheet>() {

    companion object {
        private const val SHOP_ID_PARAM = "shopId"
        private const val PRODUCT_ID_PARAM = "productId"
        private const val GIFTABLE_PARAM = "isGiftable"
        private const val PARENT_ID_PARAM = "parentId"
        private const val BOTTOM_SHEET_PARAM = "bottomsheetParam"
        private const val CATALOG_ID_PARAM = "catalogId"

        fun createParams(
            productId: String,
            shopId: String,
            parentId: String,
            isGiftable: Boolean,
            catalogId: String,
            bottomSheetParam: String,
        ): RequestParams = RequestParams.create().apply {
            putString(PRODUCT_ID_PARAM, productId)
            putString(SHOP_ID_PARAM, shopId)
            putString(PARENT_ID_PARAM, parentId)
            putBoolean(GIFTABLE_PARAM, isGiftable)
            putString(CATALOG_ID_PARAM, catalogId)
            putString(BOTTOM_SHEET_PARAM, bottomSheetParam)
        }

        val QUERY = """
            query PdpGetDetailBottomSheet(${'$'}$PRODUCT_ID_PARAM:String,${'$'}$SHOP_ID_PARAM:String,${'$'}$CATALOG_ID_PARAM:String, ${'$'}$GIFTABLE_PARAM:Boolean, ${'$'}$PARENT_ID_PARAM:String, ${'$'}$BOTTOM_SHEET_PARAM:String){
              pdpGetDetailBottomSheet(productID:${'$'}$PRODUCT_ID_PARAM, shopID:${'$'}$SHOP_ID_PARAM, catalogID:${'$'}$CATALOG_ID_PARAM, isGiftable:${'$'}$GIFTABLE_PARAM, parentID:${'$'}$PARENT_ID_PARAM, bottomsheetParam:${'$'}$BOTTOM_SHEET_PARAM){
                bottomsheetData{
                  title
                  componentName
                  isApplink
                  isShowable
                  value
                  applink
                  icon
                  row {
                    key
                    value
                  }
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

    suspend fun executeOnBackground(
        requestParams: RequestParams,
        forceRefresh: Boolean
    ): PdpGetDetailBottomSheet {
        this.requestParams = requestParams
        this.forceRefresh = forceRefresh
        return executeOnBackground()
    }

    override suspend fun executeOnBackground(): PdpGetDetailBottomSheet {
        val request = GraphqlRequest(
            QUERY,
            BottomSheetProductDetailInfoResponse::class.java, requestParams.parameters
        )
        val cacheStrategy =
            GraphqlCacheStrategy.Builder(if (forceRefresh) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST)
                .setSessionIncluded(false)
                .build()

        val gqlResponse = graphqlRepository.response(listOf(request), cacheStrategy)
        val error: List<GraphqlError>? =
            gqlResponse.getError(BottomSheetProductDetailInfoResponse::class.java)
        val data = gqlResponse.getData<BottomSheetProductDetailInfoResponse>(
            BottomSheetProductDetailInfoResponse::class.java
        )

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