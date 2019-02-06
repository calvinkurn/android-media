package com.tokopedia.product.detail.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.content.res.Resources
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.ProductInfoP2
import com.tokopedia.product.detail.data.model.product.ProductInfo
import com.tokopedia.product.detail.data.model.product.ProductParams
import com.tokopedia.product.detail.data.model.product.Rating
import com.tokopedia.product.detail.data.model.shop.ShopInfo
import com.tokopedia.product.detail.data.model.variant.ProductVariant
import com.tokopedia.product.detail.data.model.variant.ProductDetailVariantResponse
import com.tokopedia.product.detail.di.RawQueryKeyConstant
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.coroutineScope
import kotlinx.coroutines.experimental.withContext
import javax.inject.Inject
import javax.inject.Named

class ProductInfoViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                               private val userSessionInterface: UserSessionInterface,
                                               private val rawQueries: Map<String, String>,
                                               @Named("Main")
                                               val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val productInfoResp = MutableLiveData<Result<ProductInfo>>()
    val productInfoP2resp = MutableLiveData<ProductInfoP2>()
    val productVariantResp = MutableLiveData<Result<ProductVariant>>()

    fun getProductInfo(productInfoQuery: String, productVariantQuery: String, productParams: ProductParams, resources: Resources) {

        launchCatchError(block = {
            val data = withContext(Dispatchers.IO) {
                val paramsInfo = mapOf(PARAM_PRODUCT_ID to productParams.productId,
                        PARAM_SHOP_DOMAIN to productParams.shopDomain,
                        PARAM_PRODUCT_KEY to productParams.productName)
                val graphqlInfoRequest = GraphqlRequest(productInfoQuery, ProductInfo.Response::class.java, paramsInfo)

                val paramsVariant = mapOf(PARAM_PRODUCT_ID to productParams.productId)
                val graphqlVariantRequest = GraphqlRequest(productVariantQuery, ProductDetailVariantResponse::class.java, paramsVariant)
                val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build()
                graphqlRepository.getReseponse(listOf(graphqlInfoRequest, graphqlVariantRequest), cacheStrategy)
            }
            val productInfo = data.getSuccessData<ProductInfo.Response>()
            productInfoResp.value = Success(productInfo.data)
            getProductInfoP2(productInfo.data.basic.shopID, productInfo.data.basic.id, resources)

            //if fail, will not interrupt the product info
            try {
                val productVariant = data.getSuccessData<ProductDetailVariantResponse>()
                productVariantResp.value = Success(productVariant.data)
            } catch (e: Throwable) {
                // productVariantResp.value = Fail(e)
                //FOR Testing
                val gson = Gson()
                val responseVariant = gson.fromJson(GraphqlHelper.loadRawString(resources, R.raw.dummy_product_variant),
                        ProductDetailVariantResponse::class.java)
                productVariantResp.value = Success(responseVariant.data)
            }

        }) {
            //productInfoResp.value = Fail(it)
            // for testing
            val gson = Gson()
            val response = gson.fromJson(GraphqlHelper.loadRawString(resources, R.raw.dummy_product_info_p1),
                    ProductInfo.Response::class.java)
            productInfoResp.value = Success(response.data)
            getProductInfoP2(response.data.basic.shopID, response.data.basic.id, resources)

            //FOR Testing only, remove all below code after testing
            val responseVariant = gson.fromJson(GraphqlHelper.loadRawString(resources, R.raw.dummy_product_variant),
                    ProductDetailVariantResponse::class.java)
            productVariantResp.value = Success(responseVariant.data)

        }
    }

    private suspend fun getProductInfoP2(shopId: Int, productId: Int, resources: Resources) = coroutineScope{

        val data = withContext(Dispatchers.IO){
            val productInfoP2 = ProductInfoP2()

            val shopParams = mapOf(PARAM_SHOP_IDS to listOf(shopId),
                    PARAM_SHOP_FIELDS to listOf<String>())
            val shopRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_SHOP], ShopInfo.Response::class.java, shopParams)

            val isWishlistedParams = mapOf(PARAM_PRODUCT_ID to productId.toString())
            val isWishlistedRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_WISHLIST_STATUS],
                    ProductInfo.WishlistStatus::class.java, isWishlistedParams)

            val ratingParams = mapOf(PARAM_PRODUCT_ID to productId)
            val ratingRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_PRODUCT_RATING],
                    Rating.Response::class.java, ratingParams)

            val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build()
            try {
                val gqlResponse = graphqlRepository.getReseponse(listOf(shopRequest, isWishlistedRequest, ratingRequest), cacheStrategy)

                val result = if (gqlResponse.getError(ShopInfo.Response::class.java).isEmpty()){
                    (gqlResponse.getData(ShopInfo.Response::class.java) as ShopInfo.Response).result
                } else {
                    ShopInfo.Result()
                }
                if (result.data.isNotEmpty())
                    productInfoP2.shopInfo = result.data.first()

                if (gqlResponse.getError(ProductInfo.WishlistStatus::class.java).isEmpty())
                    productInfoP2.isWishlisted = (gqlResponse.getData(ProductInfo.WishlistStatus::class.java)
                            as ProductInfo.WishlistStatus).isWishlisted == true

                if (gqlResponse.getError(Rating.Response::class.java).isEmpty())
                    productInfoP2.rating = (gqlResponse.getData(Rating.Response::class.java)
                            as Rating.Response).data

                productInfoP2
            } catch (t: Throwable){
                // for testing
                val gson = Gson()
                val response = gson.fromJson(GraphqlHelper.loadRawString(resources, R.raw.dummy_shop_info_p2),
                        ShopInfo.Response::class.java)
                val ratingResponse = gson.fromJson(GraphqlHelper.loadRawString(resources, R.raw.dummy_product_rating_p2),
                        Rating.Response::class.java)
                productInfoP2.shopInfo = response.result.data.first()
                productInfoP2.isWishlisted = true
                productInfoP2.rating = ratingResponse.data
                productInfoP2
            }
        }
        productInfoP2resp.value = data
    }

    fun isShopOwner(shopId: Int): Boolean = userSessionInterface.shopId.toIntOrNull() == shopId

    fun isUserSessionActive(): Boolean = userSessionInterface.userId != null

    companion object {
        private const val PARAM_PRODUCT_ID = "productID"
        private const val PARAM_SHOP_DOMAIN = "shopDomain"
        private const val PARAM_PRODUCT_KEY = "productKey"

        private const val PARAM_SHOP_IDS = "shopIds"
        private const val PARAM_SHOP_FIELDS = "fields"
    }

}