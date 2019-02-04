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
import com.tokopedia.product.detail.data.model.product.ProductInfo
import com.tokopedia.product.detail.data.model.product.ProductParams
import com.tokopedia.product.detail.data.model.shop.ShopInfo
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
                                               val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher){

    val productInfoResp = MutableLiveData<Result<ProductInfo>>()
    val shopResp = MutableLiveData<ShopInfo>()

    fun getProductInfo(rawQuery: String, productParams: ProductParams, resources: Resources) {
        val params = mapOf(PARAM_PRODUCT_ID to productParams.productId,
                PARAM_SHOP_DOMAIN to productParams.shopDomain,
                PARAM_PRODUCT_KEY to productParams.productName)
        launchCatchError(block = {
            val data = withContext(Dispatchers.IO){
                val graphqlRequest = GraphqlRequest(rawQuery, ProductInfo.Response::class.java, params)
                val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build()
                graphqlRepository.getReseponse(listOf(graphqlRequest), cacheStrategy)
            }.getSuccessData<ProductInfo.Response>()
            productInfoResp.value = Success(data.data)
            getShopInfo(data.data.basic.shopID, resources)
        }){
            //productInfoResp.value = Fail(it)
            // for testing
            val gson = Gson()
            val response = gson.fromJson(GraphqlHelper.loadRawString(resources, R.raw.dummy_product_info_p1),
                    ProductInfo.Response::class.java)
            productInfoResp.value = Success(response.data)
            getShopInfo(response.data.basic.shopID, resources)

        }
    }

    private suspend fun getShopInfo(shopId: Int, resources: Resources) = coroutineScope{
        val data = withContext(Dispatchers.IO){
            val shopParams = mapOf(PARAM_SHOP_IDS to listOf(shopId),
                    PARAM_SHOP_FIELDS to listOf<String>())
            val shopRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_SHOP], ShopInfo.Response::class.java, shopParams)
            val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build()
            try {
                val gqlResponse = graphqlRepository.getReseponse(listOf(shopRequest), cacheStrategy)

                if (gqlResponse.getError(ShopInfo.Response::class.java).isNotEmpty()){
                    (gqlResponse.getData(ShopInfo.Response::class.java) as ShopInfo.Response).result
                } else {
                    ShopInfo.Result()
                }
            } catch (t: Throwable){
                // for testing
                val gson = Gson()
                val response = gson.fromJson(GraphqlHelper.loadRawString(resources, R.raw.dummy_shop_info_p2),
                        ShopInfo.Response::class.java)
                response.result
            }
        }
        if (data.data.isNotEmpty())
            shopResp.value = data.data.first()
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