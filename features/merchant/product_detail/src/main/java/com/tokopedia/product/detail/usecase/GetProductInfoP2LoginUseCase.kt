package com.tokopedia.product.detail.usecase

import com.tokopedia.affiliatecommon.data.pojo.productaffiliate.TopAdsPdpAffiliateResponse
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.product.ProductInfo
import com.tokopedia.product.detail.common.data.model.product.TopAdsGetProductManage
import com.tokopedia.product.detail.common.data.model.product.TopAdsGetProductManageResponse
import com.tokopedia.product.detail.data.model.ProductInfoP2Login
import com.tokopedia.product.detail.di.RawQueryKeyConstant
import com.tokopedia.product.detail.view.util.doActionIfNotNull
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import timber.log.Timber
import javax.inject.Inject

class GetProductInfoP2LoginUseCase @Inject constructor(private val rawQueries: Map<String, String>,
                                                       private val graphqlRepository: GraphqlRepository
) : UseCase<ProductInfoP2Login>() {

    companion object {
        fun createParams(shopId: Int, productId: Int, isShopOwner: Boolean): RequestParams = RequestParams.create().apply {
            putInt(ProductDetailCommonConstant.PARAM_SHOP_IDS, shopId)
            putInt(ProductDetailCommonConstant.PARAM_PRODUCT_ID, productId)
            putBoolean(ProductDetailCommonConstant.PARAM_IS_SHOP_OWNER, isShopOwner)
        }
    }

    var requestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): ProductInfoP2Login {
        val p2Login = ProductInfoP2Login()
        val productId = requestParams.getInt(ProductDetailCommonConstant.PARAM_PRODUCT_ID, 0)
        val shopId = requestParams.getInt(ProductDetailCommonConstant.PARAM_SHOP_IDS, 0)
        val isShopOwner = requestParams.getBoolean(ProductDetailCommonConstant.PARAM_IS_SHOP_OWNER, false)

        val isWishlistedParams = mapOf(ProductDetailCommonConstant.PARAM_PRODUCT_ID to productId.toString())
        val isWishlistedRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_WISHLIST_STATUS],
                ProductInfo.WishlistStatus::class.java, isWishlistedParams)

        val affilateParams = mapOf(ProductDetailCommonConstant.PRODUCT_ID_PARAM to listOf(productId),
                ProductDetailCommonConstant.SHOP_ID_PARAM to shopId,
                ProductDetailCommonConstant.INCLUDE_UI_PARAM to true)
        val affiliateRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_PRODUCT_AFFILIATE],
                TopAdsPdpAffiliateResponse::class.java, affilateParams)

        val topAdsManageParams = mapOf(ProductDetailCommonConstant.PARAM_PRODUCT_ID to productId, ProductDetailCommonConstant.PARAM_SHOP_ID to shopId)
        val topAdsManageRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_GET_TOP_ADS_MANAGE_PRODUCT],
                TopAdsGetProductManageResponse::class.java, topAdsManageParams)


        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        val requests = mutableListOf(isWishlistedRequest, affiliateRequest)

        if (isShopOwner) requests.add(topAdsManageRequest)

        try {
            val gqlResponse = graphqlRepository.getReseponse(requests, cacheStrategy)

            if (gqlResponse.getError(ProductInfo.WishlistStatus::class.java)?.isNotEmpty() != true)
                p2Login.isWishlisted = gqlResponse.getData<ProductInfo.WishlistStatus>(ProductInfo.WishlistStatus::class.java)
                        .isWishlisted == true
            else
                p2Login.isWishlisted = true

            if (gqlResponse.getError(TopAdsPdpAffiliateResponse::class.java)?.isNotEmpty() != true) {
                p2Login.pdpAffiliate = gqlResponse
                        .getData<TopAdsPdpAffiliateResponse>(TopAdsPdpAffiliateResponse::class.java)
                        .topAdsPDPAffiliate.data.affiliate.firstOrNull()
            }

            if (gqlResponse.getError(TopAdsGetProductManageResponse::class.java)?.isNotEmpty() != true) {
                gqlResponse.doActionIfNotNull<TopAdsGetProductManageResponse> {
                    p2Login.topAdsGetProductManage = gqlResponse.getData<TopAdsGetProductManageResponse>(TopAdsGetProductManageResponse::class.java).topAdsGetProductManage
                            ?: TopAdsGetProductManage()
                }
            }
        } catch (t: Throwable) {
            Timber.d(t)
        }

        return p2Login
    }
}