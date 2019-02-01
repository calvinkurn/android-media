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
import com.tokopedia.product.detail.data.model.ProductInfo
import com.tokopedia.product.detail.data.model.ProductParams
import com.tokopedia.product.detail.data.model.variant.ProductVariant
import com.tokopedia.product.detail.data.model.variant.ProductDetailVariantResponse
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.withContext
import javax.inject.Inject
import javax.inject.Named

class ProductInfoViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                               private val userSessionInterface: UserSessionInterface,
                                               @Named("Main")
                                               val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val productInfoResp = MutableLiveData<Result<ProductInfo>>()
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

            //FOR Testing only, remove all below code after testing
            val responseVariant = gson.fromJson(GraphqlHelper.loadRawString(resources, R.raw.dummy_product_variant),
                    ProductDetailVariantResponse::class.java)
            productVariantResp.value = Success(responseVariant.data)
        }
    }

    fun isShopOwner(shopId: Int): Boolean = userSessionInterface.shopId.toIntOrNull() == shopId

    fun isUserSessionActive(): Boolean = userSessionInterface.userId != null

    companion object {
        private const val PARAM_PRODUCT_ID = "productID"
        private const val PARAM_SHOP_DOMAIN = "shopDomain"
        private const val PARAM_PRODUCT_KEY = "productKey"
    }

}