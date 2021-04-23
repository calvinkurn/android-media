package com.tokopedia.product.detail.usecase

import com.tokopedia.affiliatecommon.data.pojo.productaffiliate.TopAdsPdpAffiliateResponse
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.*
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.product.TopAdsGetProductManage
import com.tokopedia.product.detail.common.data.model.product.TopAdsGetProductManageResponse
import com.tokopedia.product.detail.common.data.model.product.WishlistStatus
import com.tokopedia.product.detail.data.model.ProductInfoP2Login
import com.tokopedia.product.detail.data.model.shop.ProductShopFollowResponse
import com.tokopedia.product.detail.data.model.topads.TopAdsGetShopInfo
import com.tokopedia.product.detail.data.util.OnErrorLog
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
        val QUERY_SHOP_FOLLOW_STATUS = """
            query getShopInfo(${'$'}shopIds : [Int!]!, ${'$'}fields : [String!]!){
                shopInfoByID(input: {shopIDs: ${'$'}shopIds, fields: ${'$'}fields}){
                    result {
                        favoriteData{
                            alreadyFavorited
                        }
                    }
                }
            }
        """.trimIndent()

        val QUERY_TOP_ADS_SHOP = """
            query getTopAdsGetShopInfo(${'$'}shop_id: Int!){
                  topAdsGetShopInfo(shop_id:${'$'}shop_id) {   
              data{
                category
                category_desc
              	}
              }
            }
        """.trimIndent()

        fun createParams(shopId: Int, productId: String, isShopOwner: Boolean): RequestParams = RequestParams.create().apply {
            putInt(ProductDetailCommonConstant.PARAM_SHOP_IDS, shopId)
            putString(ProductDetailCommonConstant.PARAM_PRODUCT_ID, productId)
            putBoolean(ProductDetailCommonConstant.PARAM_IS_SHOP_OWNER, isShopOwner)
        }
    }

    var requestParams = RequestParams.EMPTY
    private var errorLogListener: OnErrorLog? = null

    override suspend fun executeOnBackground(): ProductInfoP2Login {
        val p2Login = ProductInfoP2Login()
        val productId = requestParams.getString(ProductDetailCommonConstant.PARAM_PRODUCT_ID, "")
        val shopId = requestParams.getInt(ProductDetailCommonConstant.PARAM_SHOP_IDS, 0)
        val isShopOwner = requestParams.getBoolean(ProductDetailCommonConstant.PARAM_IS_SHOP_OWNER, false)

        val isWishlistedParams = mapOf(ProductDetailCommonConstant.PARAM_PRODUCT_ID to (productId
                ?: ""))
        val isWishlistedRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_WISHLIST_STATUS],
                WishlistStatus::class.java, isWishlistedParams)

        val affilateParams = mapOf(ProductDetailCommonConstant.PRODUCT_ID_PARAM to listOf(productId.toLongOrZero()),
                ProductDetailCommonConstant.SHOP_ID_PARAM to shopId,
                ProductDetailCommonConstant.INCLUDE_UI_PARAM to true)
        val affiliateRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_PRODUCT_AFFILIATE],
                TopAdsPdpAffiliateResponse::class.java, affilateParams)

        val topAdsManageParams = mapOf(ProductDetailCommonConstant.PARAM_PRODUCT_ID to productId.toLongOrZero(), ProductDetailCommonConstant.PARAM_SHOP_ID to shopId)
        val topAdsManageRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_GET_TOP_ADS_MANAGE_PRODUCT],
                TopAdsGetProductManageResponse::class.java, topAdsManageParams)

        val topAdsShopParams = mapOf(ProductDetailCommonConstant.PARAM_APPLINK_SHOP_ID to shopId)
        val topAdsShopRequest = GraphqlRequest(QUERY_TOP_ADS_SHOP,
                TopAdsGetShopInfo.Response::class.java, topAdsShopParams)

        val shopFollowParams = mapOf(
                ProductDetailCommonConstant.PARAM_SHOP_IDS to listOf(shopId),
                ProductDetailCommonConstant.FIELDS_PARAM to listOf("favorite")
        )
        val shopFollowRequest = GraphqlRequest(QUERY_SHOP_FOLLOW_STATUS,
                ProductShopFollowResponse::class.java, shopFollowParams)

        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        val requests = mutableListOf(isWishlistedRequest, affiliateRequest)

        if (isShopOwner) requests.addAll(listOf(topAdsShopRequest, topAdsManageRequest)) else requests.addAll(listOf(shopFollowRequest))

        try {
            val gqlResponse = graphqlRepository.getReseponse(requests, cacheStrategy)

            if (gqlResponse.getError(WishlistStatus::class.java)?.isNotEmpty() != true) {
                p2Login.isWishlisted = gqlResponse.getData<WishlistStatus>(WishlistStatus::class.java)
                        .isWishlisted == true
            } else {
                p2Login.isWishlisted = true
                logError(gqlResponse, WishlistStatus::class.java)
            }

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

            if (gqlResponse.getError(TopAdsGetShopInfo.Response::class.java)?.isNotEmpty() != true) {
                gqlResponse.doActionIfNotNull<TopAdsGetShopInfo.Response> {
                    p2Login.topAdsGetShopInfo = gqlResponse.getData<TopAdsGetShopInfo.Response>(TopAdsGetShopInfo.Response::class.java).topAdsGetShopInfo.topAdsShopData
                }
            }

            if (gqlResponse.getError(ProductShopFollowResponse::class.java)?.isNotEmpty() != true) {
                gqlResponse.doActionIfNotNull<ProductShopFollowResponse> {
                    p2Login.isFollow = gqlResponse.getData<ProductShopFollowResponse>(ProductShopFollowResponse::class.java).shopInfoByID.result.firstOrNull()?.favoriteData?.alreadyFavorited
                            ?: 0
                }
            } else {
                logError(gqlResponse, ProductShopFollowResponse::class.java)
            }
        } catch (t: Throwable) {
            errorLogListener?.invoke(t)
            Timber.d(t)
        }

        return p2Login
    }

    fun setErrorLogListener(setErrorLogListener: OnErrorLog) {
        this.errorLogListener = setErrorLogListener
    }

    private fun logError(gqlResponse: GraphqlResponse, className: Class<*>) {
        val error: List<GraphqlError>? = gqlResponse.getError(className)
        errorLogListener?.invoke(MessageErrorException(error?.mapNotNull { it.message }?.joinToString(separator = ", ")))
    }
}