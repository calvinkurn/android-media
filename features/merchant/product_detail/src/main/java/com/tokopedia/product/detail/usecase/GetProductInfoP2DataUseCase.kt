package com.tokopedia.product.detail.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.data.model.ProductInfoP2Data
import com.tokopedia.product.detail.data.model.ProductInfoP2UiData
import com.tokopedia.product.detail.di.RawQueryKeyConstant
import com.tokopedia.product.detail.view.util.CacheStrategyUtil
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Yehezkiel on 20/07/20
 */
class GetProductInfoP2DataUseCase @Inject constructor(private val rawQueries: Map<String, String>,
                                                      private val graphqlRepository: GraphqlRepository) : UseCase<ProductInfoP2UiData>() {
    companion object {
        fun createParams(productId: String, pdpSession: String): RequestParams =
                RequestParams.create().apply {
                    putString(ProductDetailCommonConstant.PARAM_PRODUCT_ID, productId)
                    putString(ProductDetailCommonConstant.PARAM_PDP_SESSION, pdpSession)
                }
    }

    private var requestParams: RequestParams = RequestParams.EMPTY
    private var forceRefresh: Boolean = false

    suspend fun executeOnBackground(requestParams: RequestParams, forceRefresh: Boolean) : ProductInfoP2UiData{
        this.requestParams = requestParams
        this.forceRefresh = forceRefresh
        return executeOnBackground()
    }

    override suspend fun executeOnBackground(): ProductInfoP2UiData {
        var p2UiData = ProductInfoP2UiData()
        val p2DataRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_P2_DATA],
                ProductInfoP2Data.Response::class.java, requestParams.parameters)

        try {
            val gqlResponse = graphqlRepository.getReseponse(listOf(p2DataRequest), CacheStrategyUtil.getCacheStrategy(forceRefresh))
            val successData = gqlResponse.getData<ProductInfoP2Data.Response>(ProductInfoP2Data.Response::class.java)
            val errorData : List<GraphqlError>? = gqlResponse.getError(ProductInfoP2Data.Response::class.java)

            if (successData == null || errorData?.isNotEmpty() == true || successData.response.error.errorCode != 0) {
                throw RuntimeException()
            }

            p2UiData = mapIntoUiData(successData.response)
        } catch (t: Throwable) {
            Timber.d(t)
        }
        return p2UiData
    }

    private fun mapIntoUiData(responseData: ProductInfoP2Data) : ProductInfoP2UiData{
        val p2UiData = ProductInfoP2UiData()
        responseData.run {
            p2UiData.shopInfo = responseData.shopInfo
            p2UiData.shopSpeed = shopSpeed.hour
            p2UiData.shopChatSpeed = shopChatSpeed.messageResponseTime
            p2UiData.shopRating = shopRating.ratingScore
            p2UiData.productView = productView
            p2UiData.wishlistCount = wishlistCount
            p2UiData.isGoApotik = shopFeature.isGoApotik
            p2UiData.shopBadge = shopBadge.badge
            p2UiData.shopCommitment = shopCommitment
            p2UiData.productPurchaseProtectionInfo = productPurchaseProtectionInfo
            p2UiData.validateTradeIn = validateTradeIn
            p2UiData.cartRedirection = cartRedirection.data.associateBy({ it.productId }, { it })
            p2UiData.nearestWarehouseInfo = nearestWarehouseInfo.associateBy({ it.productId }, { it.warehouseInfo })
            p2UiData.upcomingCampaigns = upcomingCampaigns.associateBy { it.productId ?: "" }
            p2UiData.vouchers = merchantVoucher.vouchers?.map { MerchantVoucherViewModel(it) } ?: listOf()
            p2UiData.productFinancingRecommendationData = productFinancingRecommendationData
            p2UiData.productFinancingCalculationData = productFinancingCalculationData
        }
        return p2UiData
    }
}