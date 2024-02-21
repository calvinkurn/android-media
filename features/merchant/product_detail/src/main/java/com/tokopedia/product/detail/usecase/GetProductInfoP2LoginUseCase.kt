package com.tokopedia.product.detail.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
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
import com.tokopedia.product.detail.view.util.CacheStrategyUtil
import com.tokopedia.product.detail.view.util.doActionIfNotNull
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import timber.log.Timber
import javax.inject.Inject

class GetProductInfoP2LoginUseCase @Inject constructor(
    private val rawQueries: Map<String, String>,
    private val graphqlRepository: GraphqlRepository,
    private val remoteConfig: RemoteConfig
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

        val QUERY_TOP_ADS_PRODUCT_MANAGE = """
            query topAdsGetProductManageV2(${'$'}productID :String!,${'$'}shopID:String!){
               	topAdsGetProductManageV2(type:1, shop_id:${'$'}shopID,item_id:${'$'}productID,source: "pdp.manage_product"){
            			data {
            			  ad_id
            			  ad_type
            			  is_enable_ad
            			  item_id
            			  item_image
            			  item_name
            			  shop_id
            			  manage_link
            			}
                }
            }
        """.trimIndent()

        fun createParams(
            shopId: String,
            productId: String,
            isShopOwner: Boolean,
            isFromCache: Boolean
        ): RequestParams = RequestParams.create().apply {
            putString(ProductDetailCommonConstant.PARAM_SHOP_IDS, shopId)
            putString(ProductDetailCommonConstant.PARAM_PRODUCT_ID, productId)
            putBoolean(ProductDetailCommonConstant.PARAM_IS_SHOP_OWNER, isShopOwner)
            putBoolean(ProductDetailCommonConstant.PARAM_FROM_CACHE, isFromCache)
        }
    }

    var requestParams = RequestParams.EMPTY
    private var errorLogListener: OnErrorLog? = null

    private val cacheAge
        get() = remoteConfig.getLong(
            RemoteConfigKey.ENABLE_PDP_P1_CACHE_AGE,
            CacheStrategyUtil.EXPIRY_TIME_MULTIPLIER
        )

    override suspend fun executeOnBackground(): ProductInfoP2Login {
        val p2Login = ProductInfoP2Login()
        val productId = requestParams.getString(ProductDetailCommonConstant.PARAM_PRODUCT_ID, "")
        val shopId = requestParams.getString(ProductDetailCommonConstant.PARAM_SHOP_IDS, "")
        val isShopOwner =
            requestParams.getBoolean(ProductDetailCommonConstant.PARAM_IS_SHOP_OWNER, false)
        val fromCache =
            requestParams.getBoolean(ProductDetailCommonConstant.PARAM_FROM_CACHE, false)

        val isWishlistedParams = mapOf(
            ProductDetailCommonConstant.PARAM_PRODUCT_ID to (
                productId
                    ?: ""
                )
        )
        val isWishlistedRequest = GraphqlRequest(
            rawQueries[RawQueryKeyConstant.QUERY_WISHLIST_STATUS],
            WishlistStatus::class.java,
            isWishlistedParams
        )

        val topAdsManageParams = mapOf(
            ProductDetailCommonConstant.PARAM_PRODUCT_ID to productId,
            ProductDetailCommonConstant.PARAM_SHOP_ID to shopId,
            ProductDetailCommonConstant.PARAM_TEASER_SOURCE to "pdp"
        )
        val topAdsManageRequest = GraphqlRequest(
            QUERY_TOP_ADS_PRODUCT_MANAGE,
            TopAdsGetProductManageResponse::class.java,
            topAdsManageParams
        )

        val topAdsShopParams = mapOf(ProductDetailCommonConstant.PARAM_APPLINK_SHOP_ID to shopId)
        val topAdsShopRequest = GraphqlRequest(
            QUERY_TOP_ADS_SHOP,
            TopAdsGetShopInfo.Response::class.java,
            topAdsShopParams
        )

        val shopFollowParams = mapOf(
            ProductDetailCommonConstant.PARAM_SHOP_IDS to listOf(shopId),
            ProductDetailCommonConstant.FIELDS_PARAM to listOf("favorite")
        )
        val shopFollowRequest = GraphqlRequest(
            QUERY_SHOP_FOLLOW_STATUS,
            ProductShopFollowResponse::class.java,
            shopFollowParams
        )

        val requests = mutableListOf(isWishlistedRequest)

        if (isShopOwner) {
            requests.addAll(listOf(topAdsShopRequest, topAdsManageRequest))
        } else {
            requests.addAll(listOf(shopFollowRequest))
        }

        try {
            val gqlResponse = graphqlRepository.response(requests, getCacheStrategy(fromCache = fromCache))

            if (gqlResponse.getError(WishlistStatus::class.java)?.isNotEmpty() != true) {
                p2Login.isWishlisted = gqlResponse.getData<WishlistStatus>(WishlistStatus::class.java)
                    .isWishlisted == true
            } else {
                p2Login.isWishlisted = true
                logError(gqlResponse, WishlistStatus::class.java)
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

    private fun getCacheStrategy(fromCache: Boolean) = CacheStrategyUtil.getCacheStrategy(
        forceRefresh = !fromCache,
        cacheAge = cacheAge
    )

    fun setErrorLogListener(setErrorLogListener: OnErrorLog) {
        this.errorLogListener = setErrorLogListener
    }

    private fun logError(gqlResponse: GraphqlResponse, className: Class<*>) {
        val error: List<GraphqlError>? = gqlResponse.getError(className)
        errorLogListener?.invoke(
            MessageErrorException(
                error?.mapNotNull { it.message }
                    ?.joinToString(separator = ", ")
            )
        )
    }
}
