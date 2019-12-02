package com.tokopedia.product.detail.usecase

import com.tokopedia.affiliatecommon.data.pojo.productaffiliate.TopAdsPdpAffiliateResponse
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.debugTrace
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.data.model.ProductInfoP2Login
import com.tokopedia.product.detail.data.model.checkouttype.GetCheckoutTypeResponse
import com.tokopedia.product.detail.di.RawQueryKeyConstant
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetProductInfoP2LoginUseCase @Inject constructor(private val rawQueries: Map<String, String>,
                                                       private val graphqlRepository: GraphqlRepository) : UseCase<ProductInfoP2Login>() {

    var shopId: Int = 0
    var productId: Int = 0

    fun createRequestParams(shopId: Int, productId: Int) {
        this.shopId = shopId
        this.productId = productId
    }

    override suspend fun executeOnBackground(): ProductInfoP2Login {
        val p2Login = ProductInfoP2Login()

        val getCheckoutTypeRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_CHECKOUTTYPE],
                GetCheckoutTypeResponse::class.java)

        val affilateParams = mapOf(ProductDetailCommonConstant.PRODUCT_ID_PARAM to listOf(productId),
                ProductDetailCommonConstant.SHOP_ID_PARAM to shopId,
                ProductDetailCommonConstant.INCLUDE_UI_PARAM to true)
        val affiliateRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_PRODUCT_AFFILIATE],
                TopAdsPdpAffiliateResponse::class.java, affilateParams)

        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

        val requests = mutableListOf(getCheckoutTypeRequest, affiliateRequest)

        try {
            val gqlResponse = graphqlRepository.getReseponse(requests, cacheStrategy)

            if (gqlResponse.getError(TopAdsPdpAffiliateResponse::class.java)?.isNotEmpty() != true) {
                p2Login.pdpAffiliate = gqlResponse
                        .getData<TopAdsPdpAffiliateResponse>(TopAdsPdpAffiliateResponse::class.java)
                        .topAdsPDPAffiliate.data.affiliate.firstOrNull()
            }

            if (gqlResponse.getError(GetCheckoutTypeResponse::class.java)?.isNotEmpty() != true) {
                p2Login.cartType = gqlResponse
                        .getData<GetCheckoutTypeResponse>(GetCheckoutTypeResponse::class.java)
                        .getCartType.data.cartType
            }
        } catch (t: Throwable) {
            t.debugTrace()
        }

        return p2Login
    }
}